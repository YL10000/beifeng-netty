package com.beifeng.hadoop.netty.protocol;

import java.util.HashMap;
import java.util.Map;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class CodecTest {

    public static void main(String[] args) throws Exception {
        /*NettyMessageHeader header=new NettyMessageHeader(0xabef0102, 123, 99999, (byte)1, (byte)2);
        Map<String, Object> attachments=new HashMap<String, Object>();
        attachments.put("aa", 123);
        attachments.put("bb", 456);
        header.setAttachment(attachments);
        NettyMessage message=new NettyMessage(header, "body36");
        System.out.println("原消息：");
        System.out.println(message);
        ByteBuf out=Unpooled.buffer();
        new NettyMessageEncoder().encode(null, message, out);
        System.out.println(out.readableBytes());
        message=(NettyMessage) new NettyMessageDecoder(362521, 4, 4,-8,0).decode(null, out);
        System.out.println("解码后的消息");
        System.out.println(message);*/
    }
}
