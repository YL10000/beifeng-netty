package com.beifeng.hadoop.netty.decoder.msgPack;

import java.util.List;

import org.msgpack.MessagePack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

/**
 * 
 * MsgPackDecoder
 *	
 * @Description 自定义对象解码器
 * @author yanglin
 * @version 1.0,2017年6月15日
 * @see
 * @since
 */
public class MsgPackDecoder extends MessageToMessageDecoder<ByteBuf> {

    @Override
    protected void decode(ChannelHandlerContext context, ByteBuf source, List<Object> desc)
            throws Exception {
        final byte[] array;
        final int length=source.readableBytes();
        array=new byte[length];
        source.getBytes(source.readerIndex(), array, 0, length);
        MessagePack messagePack=new MessagePack();
        desc.add(messagePack.read(array));
    }

    

}
