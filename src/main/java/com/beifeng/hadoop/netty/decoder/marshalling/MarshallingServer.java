package com.beifeng.hadoop.netty.decoder.marshalling;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class MarshallingServer {
    
    public static void main(String[] args) throws InterruptedException {
        int port=18080;
        new MarshallingServer().bind(port);
    }
    
    private void bind(int port) throws InterruptedException{
        EventLoopGroup ipGroup=new NioEventLoopGroup();
        EventLoopGroup workGroup =new NioEventLoopGroup();
        
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(workGroup, ipGroup).channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel channel)
                                throws Exception {
                            channel.pipeline().addLast(MarshallingCodecFactory.buildDecoder());
                            channel.pipeline().addLast(MarshallingCodecFactory.buildEncoder());
                            channel.pipeline().addLast(new MarshallingServerHandler());

                        }
                    });
            ChannelFuture future = bootstrap.bind(port).sync();
            future.channel().closeFuture().sync();
        } finally {
            ipGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

}
