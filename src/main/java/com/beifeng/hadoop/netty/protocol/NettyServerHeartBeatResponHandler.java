package com.beifeng.hadoop.netty.protocol;

import org.apache.tools.ant.util.DateUtils;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class NettyServerHeartBeatResponHandler extends ChannelHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage message=(NettyMessage) msg;
        if (message!=null&&message.getHeader().getType()==NettyMessageType.HEART_BEAT_REQ.value()) {
            System.out
            .println(DateUtils.format(new java.util.Date(), "yyyy-MM-dd HH:mm:ss")
                    + "服务器接收到客户端 发送来的心跳：" + message);
            NettyMessage heartBeatResp=buildHeartBeatRespon();
            System.out
            .println(DateUtils.format(new java.util.Date(), "yyyy-MM-dd HH:mm:ss")
                    + "服务器向客户端 发送心跳：" + message);
            ctx.writeAndFlush(heartBeatResp);
        }
    }
    

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }


    private NettyMessage buildHeartBeatRespon(){
        NettyMessage message=new NettyMessage();
        NettyMessageHeader header=new NettyMessageHeader();
        header.setType(NettyMessageType.HEART_BEAT_RESP.value());
        message.setHeader(header);
        return message;
    }
}
