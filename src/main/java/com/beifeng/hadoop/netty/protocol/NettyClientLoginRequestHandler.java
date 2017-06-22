package com.beifeng.hadoop.netty.protocol;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class NettyClientLoginRequestHandler extends ChannelHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(buildLoginReqest());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage message=(NettyMessage) msg;
        //如果是登录请求，需要判断是否登录
        if (message.getHeader()!=null&&message.getHeader().getType()==NettyMessageType.LOGIN_RESP.value()) {
            //返回登录请求的消息体为byte类型，0表示认证成功，-1表示认证失败
            byte body=(byte) message.getBody();
            if (body!=(byte)0) {
                //登录失败，关闭连接
                System.out.println("客户端登录失败："+message);
                ctx.close();
            }else{
                System.out.println("客户端登录成功："+message);
                ctx.fireChannelRead(msg);
            }
        }else{
            ctx.fireChannelRead(msg);
        }
    }
    
    

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        ctx.fireExceptionCaught(cause);
    }
    
    private NettyMessage buildLoginReqest(){
        NettyMessage message=new NettyMessage();
        NettyMessageHeader header=new NettyMessageHeader();
        header.setType(NettyMessageType.LOGIN_REQ.value());
        message.setHeader(header);
        return message;
    }

}
