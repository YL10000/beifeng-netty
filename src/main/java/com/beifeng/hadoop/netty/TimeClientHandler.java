package com.beifeng.hadoop.netty;

import java.util.logging.Logger;

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
public class TimeClientHandler extends ChannelHandlerAdapter {

    private static final Logger LOGGER = Logger
            .getLogger(TimeClientHandler.class.getName());

    //private final ByteBuf firstMessage;
    
    private int counter;
    
    private byte[] req;

    public TimeClientHandler() {
        
        req=(NettyConstant.SEND_MESSAGE+NettyConstant.LINE_SEPARATOR).getBytes();

        //byte[] req = "QUERY TIME ORDER"+.getBytes();
        //firstMessage = Unpooled.buffer(req.length);
        //firstMessage.writeBytes(req);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //ctx.writeAndFlush(firstMessage);
        ByteBuf message=null;
        for(int i=0;i<100;i++){
            message=Unpooled.buffer(req.length);
            message.writeBytes(req);
            ctx.writeAndFlush(message);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req, "UTF-8");
        System.out.println("客户端结束消息的序号为："+ ++counter+",接受到的请求为："+body);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        LOGGER.warning("异常："+cause.getMessage());
        ctx.close();
    }

}
