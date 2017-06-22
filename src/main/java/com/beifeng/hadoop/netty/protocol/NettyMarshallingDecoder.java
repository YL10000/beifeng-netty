package com.beifeng.hadoop.netty.protocol;

import java.io.IOException;
import java.util.List;

import org.jboss.marshalling.ByteInput;
import org.jboss.marshalling.Unmarshaller;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.marshalling.MarshallingDecoder;
import io.netty.handler.codec.marshalling.UnmarshallerProvider;

public class NettyMarshallingDecoder extends MarshallingDecoder {

    //private final Unmarshaller unmarshaller;
    
    

    public NettyMarshallingDecoder(UnmarshallerProvider provider, int maxObjectSize) {
        super(provider, maxObjectSize);
    }

    public NettyMarshallingDecoder(UnmarshallerProvider provider) {
        super(provider);
    }

    @Override
    public Object decode(ChannelHandlerContext ctx, ByteBuf in)
            throws Exception {
        return super.decode(ctx, in);
    }

    /*public NettyMarshallingDecoder() throws Exception {
        this.unmarshaller=NettyMarshallingCodecFactory.buildUnMarshaller();
    }*/
    
    /*protected Object decode(ByteBuf in) throws Exception {
        int objectSize=in.readInt();
        ByteBuf buf=in.slice(in.readerIndex(), objectSize);
        ByteInput input=new NettyChannelBufferByteInput(buf);
        try {
            unmarshaller.start(input);
            Object object = unmarshaller.readObject();
            unmarshaller.finish();
            in.readerIndex(in.readerIndex() + objectSize);
            return object;
        } finally {
            unmarshaller.close();
        }
    }*/
    
    
}
