package com.beifeng.hadoop.netty.protocol;

import java.awt.RenderingHints.Key;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;

import com.beifeng.hadoop.netty.NettyConstant;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class NettyMessageDecoder extends LengthFieldBasedFrameDecoder {
    
    private NettyMarshallingDecoder marshallingDecoder;

    public NettyMessageDecoder(int maxFrameLength,
            int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment,
            int initialBytesToStrip) throws Exception {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment,
                initialBytesToStrip);
        this.marshallingDecoder=NettyMarshallingCodecFactory.buildMarshallingDecoder();
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf frame=(ByteBuf) super.decode(ctx, in);
        if (frame==null) {
            return null;
        }
        //ByteBuf frame=in;
        NettyMessage message=new NettyMessage();
        NettyMessageHeader header=new NettyMessageHeader();
        header.setCrcCode(frame.readInt());
        header.setLength(frame.readInt());
        header.setSessionID(frame.readLong());
        header.setType(frame.readByte());
        header.setPriority(frame.readByte());
        int attachmentSize=frame.readInt();
        if (attachmentSize>0) {
            Map<String, Object> attachments=new HashMap<String, Object>();
            for(int i=0;i<attachmentSize;i++){
                int keyLength=frame.readInt();
                byte[] keyArray=new byte[keyLength];
                frame.readBytes(keyArray);
                String key=new String(keyArray, NettyConstant.CHARSET_UTF_8);
                attachments.put(key, marshallingDecoder.decode(ctx, frame));
            }
            header.setAttachment(attachments);
        }
        
        if (frame.readableBytes()>0) {
            message.setBody(marshallingDecoder.decode(ctx,frame));
        }
        message.setHeader(header);
        return message;
        
    }

    
    
    

   

}
