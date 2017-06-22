package com.beifeng.hadoop.netty.decoder.msgPack;

import org.msgpack.MessagePack;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 
 * MsgPackEncoder
 *	
 * @Description 自定义对象编码器
 * @author yanglin
 * @version 1.0,2017年6月15日
 * @see
 * @since
 */
public class MsgPackEncoder extends MessageToByteEncoder<Object> {

    @Override
    protected void encode(ChannelHandlerContext context, Object source, ByteBuf des)
            throws Exception {
        MessagePack messagePack=new MessagePack();
        des.writeBytes(messagePack.write(source));
    }

   

}
