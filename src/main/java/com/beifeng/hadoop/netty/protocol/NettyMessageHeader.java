package com.beifeng.hadoop.netty.protocol;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * NettyMessageHeader
 *	
 * @Description 消息头
 * @author yanglin
 * @version 1.0,2017年6月19日
 * @see
 * @since
 */
public class NettyMessageHeader {
    
    /**
     * 消息校验码：由三部分组成
     * 1)0xabef 固定值，表示该消息为netty协议消息
     * 2)主版本号
     * 3)次版本号
     * crcCode=0xabef+主版本号+次版本号
     */
    private int crcCode=0xabef0101;
    
    private int length;//消息长度
    
    private long sessionID;
    
    /**
     * 0:业务请求消息
     * 1:业务响应消息
     * 2:业务ONE_WAY消息（既是请求消息又是响应消息）
     * 3:握手请求消息
     * 4:握手应答消息
     * 5:心跳请求消息
     * 6:心跳应答消息
     */
    private byte type;//消息类型
    
    private byte priority;//消息优先级
    
    private Map<String, Object> attachment=new HashMap<String, Object>();//附带参数
    
    

    public NettyMessageHeader() {
        super();
    }

    public NettyMessageHeader(int crcCode, int length, long sessionID, byte type,
            byte priority) {
        super();
        this.crcCode = crcCode;
        this.length = length;
        this.sessionID = sessionID;
        this.type = type;
        this.priority = priority;
    }



    public int getCrcCode() {
        return crcCode;
    }

    public void setCrcCode(int crcCode) {
        this.crcCode = crcCode;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public long getSessionID() {
        return sessionID;
    }

    public void setSessionID(long sessionID) {
        this.sessionID = sessionID;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public byte getPriority() {
        return priority;
    }

    public void setPriority(byte priority) {
        this.priority = priority;
    }

    public Map<String, Object> getAttachment() {
        return attachment;
    }

    public void setAttachment(Map<String, Object> attachment) {
        this.attachment = attachment;
    }

    @Override
    public String toString() {
        return "NettyMessageHeader [crcCode=" + crcCode + ", length=" + length
                + ", sessionID=" + sessionID + ", type=" + type + ", priority=" + priority
                + ", attachment=" + attachment + "]";
    }
    
    
    

}
