package com.beifeng.hadoop.netty.decoder.marshalling;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class MarshallingClient {
    
    public static void main(String[] args) throws InterruptedException {
        String host="127.0.0.1";
        int port=18080;
        new MarshallingClient().connect(host, port);
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
                            channel.pipeline().addLast(MarshallingCodecFactory.buildDecoder());
                            channel.pipeline().addLast(MarshallingCodecFactory.buildEncoder());
                            channel.pipeline().addLast(new MarshallingClientHandler());

                        }
                    });
            ChannelFuture future = bootstrap.connect(host, port).sync();
            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }
}
