package com.beifeng.hadoop.netty.websocket;

import java.util.logging.Logger;

import org.apache.tools.ant.types.resources.selectors.Date;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderUtil;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.CharsetUtil;
import javassist.compiler.ast.NewExpr;

public class WebSocketHandler extends SimpleChannelInboundHandler<Object> {

    private static final Logger logger = Logger
            .getLogger(WebSocketServer.class.getName());

    private WebSocketServerHandshaker handshaker = null;

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        //普通的http请求
        if (msg instanceof FullHttpRequest) {
            handleHttpRequest(ctx, (FullHttpRequest) msg);
        }else if (msg instanceof WebSocketFrame) {
            //websocket请求
            handlerWebsocketFrame(ctx, (WebSocketFrame) msg);
        }
    }

    private void handleHttpRequest(ChannelHandlerContext context,
            FullHttpRequest request) {
        // 如果不是websocket连接
        if (!request.decoderResult().isSuccess()
                || !"websocket".equals(request.headers().get("Upgrade"))) {
            sendResponse(context, request, new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }

        // 构建握手响应
        WebSocketServerHandshakerFactory handshakerFactory = new WebSocketServerHandshakerFactory(
                "wx://localhost:18080/websocket", null, false);
        handshaker = handshakerFactory.newHandshaker(request);
        if (handshaker == null) {
            WebSocketServerHandshakerFactory
                    .sendUnsupportedVersionResponse(context.channel());
        } else {
            handshaker.handshake(context.channel(), request);
        }
    }

    private void handlerWebsocketFrame(ChannelHandlerContext context,
            WebSocketFrame webSocketFrame) {
        // 判断是否为关闭链路的指令
        if (webSocketFrame instanceof CloseWebSocketFrame) {
            handshaker.close(context.channel(),
                    (CloseWebSocketFrame) webSocketFrame.retain());
            return;
        }

        // 判断是否为ping消息
        if (webSocketFrame instanceof PingWebSocketFrame) {
            context.channel()
                    .write(new PongWebSocketFrame(webSocketFrame.content().retain()));
            return;
        }

        // 本例子只支持文本消息，不支持二进制消息
        if (!(webSocketFrame instanceof TextWebSocketFrame)) {
            throw new UnsupportedOperationException(
                    String.format("%s frame类型不支持", webSocketFrame.getClass().getName()));
        }
        
        //返回应答消息
        String requestString=((TextWebSocketFrame)webSocketFrame).text();
        context.channel().write(new TextWebSocketFrame(requestString+"，欢迎使用Netty Socket服务，现在时刻为："+new Date().toString()));
    }

    private void sendResponse(ChannelHandlerContext context, FullHttpRequest request,
            FullHttpResponse response) {
        if (response.status().code() != 200) {
            ByteBuf byteBuf = Unpooled.copiedBuffer(response.status().toString(),
                    CharsetUtil.UTF_8);
            response.content().writeBytes(byteBuf);
            byteBuf.release();
            HttpHeaderUtil.setContentLength(response, response.content().readableBytes());
        }

        ChannelFuture future = context.channel().writeAndFlush(response);

        if (!HttpHeaderUtil.isKeepAlive(request) || response.status().code() != 200) {
            future.addListener(ChannelFutureListener.CLOSE);
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        ctx.close();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    
    
    
}
