package com.beifeng.hadoop.netty.decoder.msgPack;

import java.util.Random;
import java.util.logging.Logger;

import org.msgpack.MessagePack;

import com.beifeng.hadoop.netty.NettyConstant;
import com.beifeng.hadoop.netty.decoder.UserInfo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * 
 * TimeClientHandler
 * 
 * @Description netty 客户端消息处理类
 * @author yanglin
 * @version 1.0,2017年6月15日
 * @see
 * @since
 */
public class MsgPackTimeClientHandler extends ChannelHandlerAdapter {

    private static final Logger LOGGER = Logger
            .getLogger(MsgPackTimeClientHandler.class.getName());

    private int counter;
    
    private byte[] req;
    
    private ByteBuf message;
    
    private MessagePack messagePack=new MessagePack();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        UserInfo[] userInfos=getUserInfos(10);
        for(UserInfo userInfo:userInfos){
            //req=messagePack.write(userInfo);
            //message=Unpooled.copiedBuffer(req);
            ctx.write(userInfo);
        }
        ctx.flush();
        
        /*ByteBuf message=null;
        for(int i=0;i<100;i++){
            message=Unpooled.buffer(req.length);
            message.writeBytes(req);
            ctx.writeAndFlush(message);
        }*/
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //String body=(String) msg;
        System.out.println("客户端结束消息的序号为："+ ++counter+",接受到的请求为："+msg);
        //ctx.write(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        LOGGER.warning("异常："+cause.getMessage());
        ctx.close();
    }

    
    private UserInfo[] getUserInfos(int sendNumber){
        UserInfo[] userInfos=new UserInfo[sendNumber];
        for(int i=0;i<sendNumber;i++){
            userInfos[i]=new UserInfo(i, "user"+i, new Random().nextInt(80));
        }
        return userInfos;
    }
}
