package com.beifeng.hadoop.netty.decoder.protobuf;

import com.beifeng.hadoop.netty.decoder.protobuf.RespProto.Resp;

import io.netty.bootstrap.Bootstrap;
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

public class ProtoClient {
    
    public static void main(String[] args) throws InterruptedException {
        String host="127.0.0.1";
        int port=18080;
        new ProtoClient().connect(host, port);
    }
    
    private void connect(String host,int port) throws InterruptedException{
        EventLoopGroup group=new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel channel)
                                throws Exception {
                            channel.pipeline()
                                    .addLast(new ProtobufVarint32FrameDecoder());
                            channel.pipeline().addLast(
                                    new ProtobufDecoder(Resp.getDefaultInstance()));
                            channel.pipeline()
                                    .addLast(new ProtobufVarint32LengthFieldPrepender());
                            channel.pipeline().addLast(new ProtobufEncoder());
                            channel.pipeline().addLast(new ProtoClientHandler());
                        }

                    });
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }

}
