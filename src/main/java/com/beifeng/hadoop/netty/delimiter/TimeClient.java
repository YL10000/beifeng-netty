package com.beifeng.hadoop.netty.delimiter;

import com.beifeng.hadoop.netty.NettyConstant;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * 
 * TimeClient
 *	
 * @Description netty 客户端 
 * @author yanglin
 * @version 1.0,2017年6月15日
 * @see
 * @since
 */
public class TimeClient {
    
    public static void main(String[] args) {
        int port=18080;
        new TimeClient().connet(port, "127.0.0.1");
    }

    /**
     * 
     * connet
     * 
     * @Description 连接到netty服务器
     * @param port
     * @param host
     * @return void 
     * @see
     * @since
     */
    public void connet(int port,String host) {
        //配置客户端nio线程组
        EventLoopGroup group=new NioEventLoopGroup();
        
        try {
            Bootstrap bootstrap=new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ByteBuf delimiter=Unpooled.copiedBuffer(NettyConstant.DELIMTER_SEPARATOR.getBytes());
                        socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimiter));
                        socketChannel.pipeline().addLast(new StringDecoder());
                        socketChannel.pipeline().addLast(new TimeClientHandler());
                    }
                });
           
            ChannelFuture future=bootstrap.connect(host, port).sync();
            
            future.channel().closeFuture().sync();
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            group.shutdownGracefully();
        }
        
    }
}
