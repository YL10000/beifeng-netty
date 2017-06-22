package com.beifeng.hadoop.netty.decoder.msgPack;

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
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * 
 * TimeClient
 *	
 * @Description netty 序列化对象客户端 
 * @author yanglin
 * @version 1.0,2017年6月15日
 * @see
 * @since
 */
public class MsgPackTimeClient {
    
    public static void main(String[] args) {
        int port=18080;
        new MsgPackTimeClient().connet(port, "127.0.0.1");
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
                //.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                .handler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        
                        channel.pipeline().addLast(new LengthFieldBasedFrameDecoder(65535,0, 2,0,2));
                        channel.pipeline().addLast(new MsgPackDecoder());
                        channel.pipeline().addLast(new LengthFieldPrepender(2));
                        channel.pipeline().addLast(new MsgPackEncoder());
                        
                        /*channel.pipeline().addLast(new LineBasedFrameDecoder(1024));
                        channel.pipeline().addLast(new StringDecoder());*/
                        channel.pipeline().addLast(new MsgPackTimeClientHandler());
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
