package com.beifeng.hadoop.netty.protocol;

import java.awt.List;
import java.util.Map.Entry;

import com.beifeng.hadoop.netty.NettyConstant;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.marshalling.MarshallingEncoder;

public class NettyMessageEncoder extends MessageToMessageEncoder<NettyMessage> {
    
    private NettyMarshallingEncoder marshallingEncoder;
    
    

    public NettyMessageEncoder() throws Exception {
        super();
       this.marshallingEncoder=NettyMarshallingCodecFactory.buildMarshallingEncoder();
    }



    @Override
    protected void encode(ChannelHandlerContext ctx, NettyMessage msg, java.util.List<Object> list)
            throws Exception {
        if (msg==null||msg.getHeader()==null) {
            throw new Exception("该编码的消息为空！");
        }
        ByteBuf out=Unpooled.buffer();
        out.writeInt(msg.getHeader().getCrcCode());
        out.writeInt(msg.getHeader().getLength());
        out.writeLong(msg.getHeader().getSessionID());
        out.writeByte(msg.getHeader().getType());
        out.writeByte(msg.getHeader().getPriority());
        out.writeInt(msg.getHeader().getAttachment().size());
        String key=null;
        byte[] keyArray=null;
        Object value=null;
        for(Entry<String, Object> entry:msg.getHeader().getAttachment().entrySet()){
            key=entry.getKey();
            keyArray=key.getBytes(NettyConstant.CHARSET_UTF_8);
            out.writeInt(key.length());
            out.writeBytes(keyArray);
            value=entry.getValue();
            marshallingEncoder.encode(ctx,value, out);
        }
        if (msg.getBody()!=null) {
            marshallingEncoder.encode(ctx,msg.getBody(), out);
        }
        out.setInt(4, out.readableBytes());
        list.add(out);
    }

}
