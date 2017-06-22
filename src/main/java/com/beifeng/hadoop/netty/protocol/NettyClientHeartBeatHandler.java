package com.beifeng.hadoop.netty.protocol;

import java.util.concurrent.TimeUnit;

import org.apache.tools.ant.types.resources.comparators.Date;
import org.apache.tools.ant.util.DateUtils;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.ScheduledFuture;

public class NettyClientHeartBeatHandler extends ChannelHandlerAdapter {

    // 对于volatile修饰的变量，jvm虚拟机只是保证从主内存加载到线程工作内存的值是最新的
    private volatile ScheduledFuture<?> heartBeat;

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        cause.printStackTrace();
        if (heartBeat != null) {
            heartBeat.cancel(true);
            heartBeat = null;
        }
        ctx.fireExceptionCaught(cause);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage message = (NettyMessage) msg;
        // 握手成功，主动发送心跳请求
        if (message.getHeader() != null
                && message.getHeader().getType() == NettyMessageType.LOGIN_RESP.value()) {
            heartBeat = ctx.executor().scheduleAtFixedRate(
                    new NettyClientHeartBeatHandler.NettyClientHeartBeatTask(ctx), 0,
                    5000, TimeUnit.MILLISECONDS);

        }else if (message.getHeader()!=null&&message.getHeader().getType()==NettyMessageType.HEART_BEAT_RESP.value()) {
            //接受到服务器发送的心跳
            System.out
            .println(DateUtils.format(new java.util.Date(), "yyyy-MM-dd HH:mm:ss")
                    + "客户端接收到服务器 发送来的心跳：" + message);
        }else {
            ctx.fireChannelRead(message);
        }

    }
    
    

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    private NettyMessage buildHeartBeatReqest() {
        NettyMessage message = new NettyMessage();
        NettyMessageHeader header = new NettyMessageHeader();
        header.setType(NettyMessageType.HEART_BEAT_REQ.value());
        message.setHeader(header);
        return message;
    }

    private class NettyClientHeartBeatTask implements Runnable {

        private final ChannelHandlerContext context;

        public NettyClientHeartBeatTask(final ChannelHandlerContext context) {
            super();
            this.context = context;
        }

        public void run() {
            NettyMessage message = buildHeartBeatReqest();
            System.out
                    .println(DateUtils.format(new java.util.Date(), "yyyy-MM-dd HH:mm:ss")
                            + "客户端向服务器 发送心跳：" + message);
            context.writeAndFlush(message);
        }
    }

}
