package com.beifeng.hadoop.netty.decoder.protobuf;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.beifeng.hadoop.netty.decoder.protobuf.ReqProto.Req;
import com.beifeng.hadoop.netty.decoder.protobuf.ReqProto.Req.Builder;
import com.beifeng.hadoop.netty.decoder.protobuf.RespProto.Resp;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

public class ProtoClientHandler extends ChannelHandlerAdapter {

    int counter=0;
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Resp resp=(Resp) msg;
        System.out.println("客户端接受到消息："+ ++counter);
        System.out.println(resp);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        List<Req> reqs=getReqs();
        for(Req req:reqs){
            ctx.write(req);
        }
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
       ctx.close();
    }
    
    private List<Req> getReqs(){
        List<Req> reqs=new ArrayList<Req>();
        for(int i=0;i<10;i++){
            Builder builder=Req.newBuilder();
            builder.setId(i);
            builder.setUserName("username"+i);
            builder.setProductName("product"+i);
            List<String> address=Arrays.asList("北京,上海,天津".split(","));
            builder.addAllAddress(address);
            reqs.add(builder.build());
        }
        return reqs;
    }

    
}
