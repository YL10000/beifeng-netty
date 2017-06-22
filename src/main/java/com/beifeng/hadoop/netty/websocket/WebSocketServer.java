package com.beifeng.hadoop.netty.websocket;

import java.net.InetAddress;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class WebSocketServer {
    
    public static void main(String[] args) throws Exception {
        int port=18080;
        new WebSocketServer().bind(port);
    }
    
    private void bind(int port) throws Exception{
        EventLoopGroup bossGroup=new NioEventLoopGroup();
        EventLoopGroup workGroup=new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap=new ServerBootstrap();
            String ip = InetAddress.getLocalHost().getHostAddress();
            bootstrap.group(bossGroup,workGroup).channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        ChannelPipeline pipeline=channel.pipeline();
                        pipeline.addLast("http-codec",new HttpServerCodec());
                        pipeline.addLast("aggregator",new HttpObjectAggregator(65536));
                        pipeline.addLast("http-chunked",new ChunkedWriteHandler());
                        pipeline.addLast("websockethandler",new WebSocketHandler());
                    }
                });
            
            
            Channel channel=bootstrap.bind(port).sync().channel();
            System.out.println("服务器正在监听：http:"+ip+":"+port+"/");
            channel.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
        
    }

}
