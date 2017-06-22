package com.beifeng.hadoop.netty.decoder.protobuf;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class ProtoServer {
    
    public static void main(String[] args) throws InterruptedException {
        int port=18080;
        new ProtoServer().bind(port);
    }

    private void bind(int port) throws InterruptedException{
        EventLoopGroup ipGroup=new NioEventLoopGroup();
        EventLoopGroup workGroup=new NioEventLoopGroup();
        
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(ipGroup, workGroup).channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel channel)
                                throws Exception {
                            channel.pipeline()
                                    .addLast(new ProtobufVarint32FrameDecoder());
                            channel.pipeline().addLast(new ProtobufDecoder(
                                    ReqProto.Req.getDefaultInstance()));
                            channel.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
                            channel.pipeline().addLast(new ProtobufEncoder());
                            channel.pipeline().addLast(new ProtoServerHandler());
                        }
                    });
            ChannelFuture channelFuture = bootstrap.bind(port).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            workGroup.shutdownGracefully();
            ipGroup.shutdownGracefully();
        }
        
    }
}
