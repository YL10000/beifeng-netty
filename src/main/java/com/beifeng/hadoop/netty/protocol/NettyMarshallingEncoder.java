package com.beifeng.hadoop.netty.protocol;

import java.io.IOException;

import org.jboss.marshalling.Marshaller;
import org.jboss.marshalling.MarshallerFactory;

import com.beifeng.hadoop.netty.decoder.marshalling.MarshallingCodecFactory;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.marshalling.MarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallingEncoder;

@Sharable
public class NettyMarshallingEncoder extends MarshallingEncoder {

    public NettyMarshallingEncoder(MarshallerProvider provider) {
        super(provider);
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out)
            throws Exception {
        super.encode(ctx, msg, out);
    }
    
    
    

    /*private final static  byte[] LENGTH_PLACEHOLDER=new byte[4];
    
    private Marshaller marshaller=null;

    public NettyMarshallingEncoder() throws Exception {
        marshaller=NettyMarshallingCodecFactory.buildMarshaller();
    }
    
    protected void encode(Object msg,ByteBuf out) throws Exception {
        try {
            int lengthPos = out.writerIndex();
            out.writeBytes(LENGTH_PLACEHOLDER);
            NettyChannelBufferByteOutput bufferByteOutput = new NettyChannelBufferByteOutput(
                    out);
            marshaller.start(bufferByteOutput);
            marshaller.writeObject(msg);
            marshaller.finish();
            out.setInt(lengthPos, out.writerIndex() - lengthPos - 4);
        } finally {
            marshaller.close();
        }
    }*/
}
