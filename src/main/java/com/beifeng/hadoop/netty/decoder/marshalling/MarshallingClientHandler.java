package com.beifeng.hadoop.netty.decoder.marshalling;

import java.util.List;
import java.util.Random;

import com.beifeng.hadoop.netty.decoder.UserInfo;
import com.beifeng.hadoop.netty.decoder.protobuf.ReqProto.Req;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class MarshallingClientHandler extends ChannelHandlerAdapter {
    
    private int counter=0;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        MarshallingReq[] reqs=getReqs(10);
        for(MarshallingReq req:reqs){
            ctx.write(req);
            //ctx.writeAndFlush(req);
        }
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MarshallingResp resp=(MarshallingResp) msg;
        System.out.println("客户端端接收到第"+ ++counter +"个消息：");
        System.out.println(resp);
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
    
    
    private MarshallingReq[] getReqs(int sendNumber){
        MarshallingReq[] reqs=new MarshallingReq[sendNumber];
        for(int i=0;i<sendNumber;i++){
            reqs[i]=new MarshallingReq(i, "request"+i, new Random().nextInt(80));
        }
        return reqs;
        
    }

    
}
