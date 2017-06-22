package com.beifeng.hadoop.netty.protocol;

import java.net.InetAddress;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;

public class NettyServer {
    
    public static void main(String[] args) throws Exception {
        int port=38080;
        new NettyServer().bind(port);
    }
    
    private void bind(int port) throws Exception{
        EventLoopGroup bossGroup=new NioEventLoopGroup();
        EventLoopGroup workGroup=new NioEventLoopGroup();
        ServerBootstrap bootstrap=new ServerBootstrap();
        bootstrap.group(bossGroup,workGroup).channel(NioServerSocketChannel.class)
            .option(ChannelOption.SO_BACKLOG, 100)
            .handler(new LoggingHandler(LogLevel.INFO))
            .childHandler(new ChannelInitializer<SocketChannel>() {

                @Override
                protected void initChannel(SocketChannel channel) throws Exception {
                    ChannelPipeline pipeline=channel.pipeline();
                    pipeline.addLast(new NettyMessageDecoder(1024*1024, 4, 4,-8,0));
                    pipeline.addLast(new NettyMessageEncoder());
                    pipeline.addLast(new ReadTimeoutHandler(50));
                    pipeline.addLast(new NettyServerLoginResponHandler());
                    pipeline.addLast(new NettyServerHeartBeatResponHandler());
                }
            });
        //String ip = InetAddress.getLocalHost().getHostAddress();
        ChannelFuture future=bootstrap.bind(port).sync();
        System.out.println("服务器启动成功！");
        future.channel().closeFuture().sync();
        
        
    }

}
