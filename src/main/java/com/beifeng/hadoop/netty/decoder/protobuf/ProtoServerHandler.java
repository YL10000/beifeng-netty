package com.beifeng.hadoop.netty.decoder.protobuf;

import com.beifeng.hadoop.netty.decoder.protobuf.ReqProto.Req;
import com.beifeng.hadoop.netty.decoder.protobuf.RespProto.Resp;
import com.beifeng.hadoop.netty.decoder.protobuf.RespProto.Resp.Builder;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class ProtoServerHandler extends ChannelHandlerAdapter {
    
    int counter=0;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Req req=(Req) msg;
        System.out.println("服务器端接收到消息"+ ++counter);
        System.out.println(msg);
        ctx.writeAndFlush(getResp(req));
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
    
    private Resp getResp(Req req){
        Builder builder = Resp.newBuilder();
        builder.setId(req.getId());
        builder.setCode("200");
        builder.setDesc("请求成功");
        return builder.build();
    }

    
}
