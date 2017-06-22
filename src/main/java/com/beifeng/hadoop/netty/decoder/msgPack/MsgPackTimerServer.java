package com.beifeng.hadoop.netty.decoder.msgPack;

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
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
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
public class MsgPackTimerServer {
    
    public static void main(String[] args) throws Exception {
        int port=18080;
        new MsgPackTimerServer().bind(port);
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
            channel.pipeline().addLast(new LengthFieldBasedFrameDecoder(65535,0, 2,0,2));
            channel.pipeline().addLast(new MsgPackDecoder());
            channel.pipeline().addLast(new LengthFieldPrepender(2));
            channel.pipeline().addLast(new MsgPackEncoder());
            
            channel.pipeline().addLast(new MsgPackTimerServerHandler());
        }
    }
}
