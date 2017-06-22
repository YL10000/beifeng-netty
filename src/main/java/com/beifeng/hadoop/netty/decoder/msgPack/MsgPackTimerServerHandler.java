package com.beifeng.hadoop.netty.decoder.msgPack;

import java.net.SocketAddress;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.msgpack.MessagePack;
import org.msgpack.type.ArrayValue;

import com.beifeng.hadoop.netty.NettyConstant;
import com.beifeng.hadoop.netty.decoder.UserInfo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import javassist.expr.Instanceof;

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
public class MsgPackTimerServerHandler extends ChannelHandlerAdapter {
    
    //计数器
    private int counter;

    @Override
    public void channelRead(ChannelHandlerContext context, Object msg) throws Exception {
        System.out.println("服务器结束消息的序号为："+ ++counter+",接受到的请求为："+msg);
        context.write(msg);
        context.flush();
        
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
