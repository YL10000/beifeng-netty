package com.beifeng.hadoop.netty.protocol;

public enum NettyMessageType {

    SERVICE_REQ((byte) 0), SERVICE_RESP((byte) 1), ONE_WAY((byte) 2), LOGIN_REQ(
            (byte) 3), LOGIN_RESP(
                    (byte) 4), HEART_BEAT_REQ((byte) 5), HEART_BEAT_RESP((byte) 6);

    private byte value;

    private NettyMessageType(byte value) {
        this.value = value;
    }
    
    public byte value(){
        return this.value;
    }
}
