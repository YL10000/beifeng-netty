package com.beifeng.hadoop.netty.protocol;

import java.io.IOException;

import org.jboss.marshalling.ByteInput;

import io.netty.buffer.ByteBuf;

public class NettyChannelBufferByteInput implements ByteInput {
    
    private final ByteBuf byteBuffer;
    
    public NettyChannelBufferByteInput(ByteBuf byteBuffer) {
        super();
        this.byteBuffer = byteBuffer;
    }

    @Override
    public void close() throws IOException {
    }

    @Override
    public int available() throws IOException {
        return byteBuffer.readableBytes();
    }

    @Override
    public int read() throws IOException {
        if (byteBuffer.isReadable()) {
            return byteBuffer.readByte()&0xff;
        }
        return -1;
    }

    @Override
    public int read(byte[] arg0) throws IOException {
        return read(arg0, 0, arg0.length);
    }

    @Override
    public int read(byte[] dst, int index, int length) throws IOException {
        int avaliable=available();
        if (avaliable==0) {
            return -1;
        }
        length=Math.min(avaliable, length);
        byteBuffer.readBytes(dst, index, length);
        return length;
    }

    @Override
    public long skip(long bytes) throws IOException {
        int reables=byteBuffer.readableBytes();
        if (reables<bytes) {
            bytes=reables;
        }
        byteBuffer.readerIndex((int)(byteBuffer.readerIndex()+bytes));
        return bytes;
    }

}
