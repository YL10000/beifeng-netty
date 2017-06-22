package com.beifeng.hadoop.netty.protocol;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.beifeng.hadoop.netty.NettyConstant;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

public class NettyClient {

    private String local_host = "127.0.0.1";
    private int local_port = 48080;
    
    private ScheduledExecutorService executor=Executors.newScheduledThreadPool(1);
    EventLoopGroup group = new NioEventLoopGroup();
    
    public static void main(String[] args) throws Exception {
        String remote_host="127.0.0.1";
        int remote_port=38080;
        new NettyClient().connect(remote_host, remote_port);
    }

    private void connect(String host, int port) throws Exception {
        
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        public void initChannel(SocketChannel channel)
                                throws Exception {
                                ChannelPipeline pipeline=channel.pipeline();
                                pipeline.addLast("netty message decoder",new NettyMessageDecoder(1024*1024, 4, 4,-8,0));
                                pipeline.addLast("netty message encoder",new NettyMessageEncoder());
                                pipeline.addLast("time out handler",new ReadTimeoutHandler(50));
                                pipeline.addLast("login reqest handler",new NettyClientLoginRequestHandler());
                                pipeline.addLast("heart beat handler",new NettyClientHeartBeatHandler());
                                
                        }
                    });
            //发起异步连接操作
            ChannelFuture future = bootstrap.connect(new InetSocketAddress(host, port),
                    new InetSocketAddress(local_host, local_port)).sync();
            future.channel().closeFuture().sync();
        } finally {
            //所有资源释放之后，清空资源，再次发起连接请求
            //group.shutdownGracefully();
            executor.execute(new Runnable() {
                
                @Override
                public void run() {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                        try {
                            connect(NettyConstant.REMOTE_HOST, NettyConstant.REMOTE_PORT);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            
        }
    }
}
