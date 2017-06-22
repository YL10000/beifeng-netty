package com.beifeng.hadoop.netty.delimiter;

import java.net.SocketAddress;
import java.util.Date;

import com.beifeng.hadoop.netty.NettyConstant;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

/**
 * 
 * TimerServerHandler
 *	
 * @Description 服务器端具体的处理类
 * @author yanglin
 * @version 1.0,2017年6月13日
 * @see
 * @since
 */
public class TimerServerHandler extends ChannelHandlerAdapter {
    
    //计数器
    private int counter;

    @Override
    public void channelRead(ChannelHandlerContext context, Object msg) throws Exception {
        String body=(String) msg;
        System.out.println("服务器结束消息的序号为："+ ++counter+",接受到的请求为："+body);
        //如果是QUERY TIME ORDER 表示创建应答消息
        String currentTime=NettyConstant.SEND_MESSAGE.equalsIgnoreCase(body)?new Date().toString():NettyConstant.BAD_MESSAGE;
        currentTime+=NettyConstant.DELIMTER_SEPARATOR;
        ByteBuf resp=Unpooled.copiedBuffer(currentTime.getBytes());
        //发送应答消息给客户端（只是将带发消息放到发送缓冲数组中，调用flush方法时，才将消息缓冲中的数据写到SocketChannel中）
        context.writeAndFlush(resp);
        
    }
    
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext context) throws Exception {
        //将消息发送队列中的消息写入到SocketChannel中，发送给对方
        context.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable arg1)
            throws Exception {
        context.close();
    }

    

}
