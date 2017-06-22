package com.beifeng.hadoop.netty.delimiter;

import com.beifeng.hadoop.netty.NettyConstant;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * 
 * TimerServer
 * 
 * @Description netty 服务器端
 * @author yanglin
 * @version 1.0,2017年6月13日
 * @see
 * @since
 */
public class TimerServer {
    
    public static void main(String[] args) throws Exception {
        int port=18080;
        new TimerServer().bind(port);
    }
    
    //绑定端口
    public void bind(int port) throws Exception {
        //配置服务端的nio线程组
        EventLoopGroup bossGroup=new NioEventLoopGroup();//用来接受客户端的连接
        EventLoopGroup workerGroup=new NioEventLoopGroup();//用于socketchannel的网络读写
        
        try {
            ServerBootstrap bootstrap=new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChildChannelHandler());
            
            //绑定端口，同步等待成功(主要用于异步操作的回调通知)
            ChannelFuture channelFuture=bootstrap.bind(port).sync();
            
            //等待服务端监听端口关闭
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //退出，并释放资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {

        @Override
        protected void initChannel(SocketChannel channel) throws Exception {
            //添加按指定分割符的解码包
            ByteBuf delimiter=Unpooled.copiedBuffer(NettyConstant.DELIMTER_SEPARATOR.getBytes());
            channel.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimiter));
            channel.pipeline().addLast(new StringDecoder());
            channel.pipeline().addLast(new TimerServerHandler());
        }
    }
}
