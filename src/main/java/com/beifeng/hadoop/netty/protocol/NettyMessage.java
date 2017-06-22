package com.beifeng.hadoop.netty.protocol;

/**
 * 
 * NettyMessage
 *	
 * @Description 消息
 * @author yanglin
 * @version 1.0,2017年6月19日
 * @see
 * @since
 */
public class NettyMessage {

    private NettyMessageHeader header;//消息头
    
    private Object body;//消息体
    
    public NettyMessage() {
        super();
    }

    public NettyMessage(NettyMessageHeader header, Object body) {
        super();
        this.header = header;
        this.body = body;
    }

    public NettyMessageHeader getHeader() {
        return header;
    }

    public void setHeader(NettyMessageHeader header) {
        this.header = header;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "NettyMessage [header=" + header + ", body=" + body + "]";
    }
    
}
