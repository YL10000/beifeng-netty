package com.beifeng.hadoop.netty.decoder.marshalling;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class MarshallingServerHandler extends ChannelHandlerAdapter {

    int counter=0;
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MarshallingReq req=(MarshallingReq) msg;
        System.out.println("服务器端接收到第"+ ++counter +"个消息：");
        System.out.println(req);
        MarshallingResp resp=new MarshallingResp(req.getId(), 200, "server response");
        ctx.writeAndFlush(resp);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        ctx.close();
    }
    
    

}
