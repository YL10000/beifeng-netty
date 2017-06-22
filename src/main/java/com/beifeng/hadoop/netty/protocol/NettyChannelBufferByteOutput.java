package com.beifeng.hadoop.netty.protocol;

import java.io.IOException;

import org.jboss.marshalling.ByteOutput;

import io.netty.buffer.ByteBuf;

public class NettyChannelBufferByteOutput implements ByteOutput {
    
    private final ByteBuf byteBuffer;
    
    public NettyChannelBufferByteOutput(ByteBuf byteBuffer) {
        super();
        this.byteBuffer=byteBuffer;
    }

    @Override
    public void close() throws IOException {
    }

    @Override
    public void flush() throws IOException {
    }

    @Override
    public void write(int b) throws IOException {
        byteBuffer.writeByte(b);
    }

    @Override
    public void write(byte[] bytes) throws IOException {
        byteBuffer.writeBytes(bytes);
    }

    @Override
    public void write(byte[] bytes, int srcIndex, int length) throws IOException {
        byteBuffer.writeBytes(bytes, srcIndex, length);
    }

    public ByteBuf getByteBuffer() {
        return byteBuffer;
    }

}
