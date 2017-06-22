package com.beifeng.hadoop.netty.protocol;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.beifeng.hadoop.netty.NettyConstant;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class NettyServerLoginResponHandler extends ChannelHandlerAdapter {

    private Map<String, Boolean> loginSuccess=new HashMap<String, Boolean>();
    
    private String[] whiteIps=new String[]{"127.0.0.1","192.168.0.65","localhost","cas.5teacher.com"};

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        cause.printStackTrace();
        loginSuccess.remove(ctx.channel().remoteAddress().toString());
        ctx.close();
        ctx.fireExceptionCaught(cause);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage message=(NettyMessage) msg;
        //如果是握手请求，则进行处理，否则进行透传
        if (message.getHeader()!=null&&message.getHeader().getType()==NettyMessageType.LOGIN_REQ.value()) {
            InetSocketAddress loginNode=(InetSocketAddress) ctx.channel().remoteAddress();
            NettyMessage loginRespon=null;
            //判断是否已经登录成功
            if (loginSuccess.containsKey(loginNode.toString())) {
                System.out.println("重复登录");
                loginRespon=buildLoginRespon((byte)-1);
            }
            //判断是否在白名单中
            else if (!Arrays.asList(whiteIps).contains(loginNode.getHostName())) {
                System.out.println(loginNode.getHostName());
                System.out.println("不在白名单中");
                loginRespon=buildLoginRespon((byte)-1);
            }else {
                loginRespon=buildLoginRespon((byte)0);
                //添加到登录缓存中
                loginSuccess.put(loginNode.toString(), true);
            }
            System.out.println("服务器发送登录回应消息："+message);
            ctx.writeAndFlush(loginRespon);
        }else{
            ctx.fireChannelRead(msg);
        }
    }
    
    
    
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    private NettyMessage buildLoginRespon(byte result){
        NettyMessage message=new NettyMessage();
        NettyMessageHeader header=new NettyMessageHeader();
        header.setType(NettyMessageType.LOGIN_RESP.value());
        message.setHeader(header);
        message.setBody(result);
        return message;
    }
    
    
}
