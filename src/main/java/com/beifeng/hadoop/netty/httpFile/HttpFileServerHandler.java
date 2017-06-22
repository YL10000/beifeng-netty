package com.beifeng.hadoop.netty.httpFile;

import java.io.File;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.RandomAccess;
import java.util.regex.Pattern;

import javax.activation.MimetypesFileTypeMap;

import org.omg.CORBA.portable.ResponseHandler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelProgressiveFuture;
import io.netty.channel.ChannelProgressiveFutureListener;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderUtil;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.stream.ChunkedFile;
import io.netty.util.CharsetUtil;

public class HttpFileServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private final String url;

    private static final Pattern INSECURE_URI = Pattern.compile(".*[<>&\"].*");

    private static final Pattern ALLOWED_FILE_NAME = Pattern
            .compile("[A-Za-z0-9][-_A-Za-z0-9\\.]*");

    public HttpFileServerHandler(String url) {
        super();
        this.url = url;
    }

    @Override
    protected void messageReceived(ChannelHandlerContext context, FullHttpRequest request)
            throws Exception {
        //转码成功
        if (!request.decoderResult().isSuccess()) {
            sendError(context, HttpResponseStatus.BAD_REQUEST);
            return;
        }

        //限制为get请求
        if (request.method() != HttpMethod.GET) {
            sendError(context, HttpResponseStatus.METHOD_NOT_ALLOWED);
            return;
        }

        final String uri = request.uri();
        //获取文件的本地决定路径
        final String path = sanitizeUrl(uri);
        
        if (path == null) {
            sendError(context, HttpResponseStatus.FORBIDDEN);
            return;
        }
        File file = new File(path);
        if (file.isHidden() || !file.exists()) {
            sendError(context, HttpResponseStatus.NOT_FOUND);
            return;
        }
        
        //如果是目录
        if (file.isDirectory()) {
            if (uri.endsWith("/")) {
                //遍历当前目录下的文件
                sendFileList(context, file);
            } else {
                sendRedirect(context, uri + '/');
            }
            return;
        }
        
        //如果也不是文件
        if (!file.isFile()) {
            sendError(context, HttpResponseStatus.FORBIDDEN);
            return;
        }

        RandomAccessFile randomAccessFile = null;
        try {
            //以只读方式打开文件
            randomAccessFile = new RandomAccessFile(file, "r");
        } catch (Exception e) {
            sendError(context, HttpResponseStatus.NOT_FOUND);
            return;
        }
        long fileLenght = randomAccessFile.length();
        HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK);
        HttpHeaderUtil.setContentLength(response, fileLenght);
        setContentTypeHeader(response, file);
        
        if (HttpHeaderUtil.isKeepAlive(request)) {
            response.headers().set(HttpHeaderNames.CONNECTION,
                    HttpHeaderValues.KEEP_ALIVE);
        }
        context.write(response);
        ChannelFuture sendFuture = context.write(new ChunkedFile(randomAccessFile, 0, fileLenght, 8192),
                context.newProgressivePromise());
        sendFuture.addListener(new ChannelProgressiveFutureListener() {
            
            @Override
            public void operationComplete(ChannelProgressiveFuture future) throws Exception {
                System.out.println("Transfer complete");
            }
            
            @Override
            public void operationProgressed(ChannelProgressiveFuture future, long progress,
                    long total) throws Exception {
                if (total<0) {
                    System.out.println("Trasnsfer progress:"+progress);
                }else {
                    System.out.println("Trasnsfer progress:"+progress+"/"+total);
                }
            }
        });
        
        ChannelFuture lastContentFuture=context.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
        
        if (!HttpHeaderUtil.isKeepAlive(request)) {
            lastContentFuture.addListener(ChannelFutureListener.CLOSE);
        }
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        super.exceptionCaught(ctx, cause);
        if (ctx.channel().isActive()) {
            sendError(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 
     * sendError
     * 
     * @Description 错误相应
     * @param context
     * @param status
     * @return void
     * @see
     * @since
     */
    private void sendError(ChannelHandlerContext context, HttpResponseStatus status) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                status, Unpooled.copiedBuffer("Failure:" + status.toString() + "\r\n",
                        CharsetUtil.UTF_8));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");
        context.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private String sanitizeUrl(String uri) {
        try {
            uri = URLDecoder.decode(uri, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            try {
                uri = URLDecoder.decode(uri, "ISO-8859-1");
            } catch (UnsupportedEncodingException e1) {
                throw new Error();
            }
        }
        if (!uri.startsWith(url)) {
            return null;
        }
        if (!uri.startsWith("/")) {
            return null;
        }
        uri = uri.replace('/', File.separatorChar);
        if (uri.contains(File.separator + '.') || uri.contains('.' + File.separator)
                || uri.startsWith(".") || uri.endsWith(".")
                || INSECURE_URI.matcher(uri).matches()) {
            return null;
        }
        
        //System.getProperty("user.dir") 为用户工作目录
        return System.getProperty("user.dir")  + uri;
    }

    public void sendFileList(ChannelHandlerContext context, File dir) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");
        StringBuilder buf = new StringBuilder();
        String dirPath = dir.getPath();
        buf.append("<!DOCTYPE html>\r\n").append("<html><head><title>").append(dirPath)
                .append("目录：").append("</title></head><body>\r\n").append("<h3>")
                .append(dirPath).append(" 目录：　").append("</h3>\r\n").append("<ul>")
                .append("<li>链接：<a href=\"../\">..</a></li>\r\n");
        for (File file : dir.listFiles()) {
            if (file.isHidden() || !file.canRead()) {
                continue;
            }
            String fileName = file.getName();
            if (!ALLOWED_FILE_NAME.matcher(fileName).matches()) {
                continue;
            }
            buf.append("<li>链接：<a href=\"").append(fileName).append("\">")
                    .append(fileName).append("</a></li>\r\n");

        }

        buf.append("</ul></body></html>\r\n");

        ByteBuf byteBuf = Unpooled.copiedBuffer(buf, CharsetUtil.UTF_8);
        response.content().writeBytes(byteBuf);
        byteBuf.release();
        context.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    /**
     * 
     * sendRedirect
     * 
     * @Description 重定向
     * @param context
     * @param uri
     * @return void
     * @see
     * @since
     */
    private void sendRedirect(ChannelHandlerContext context, String uri) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                HttpResponseStatus.FOUND);
        response.headers().set(HttpHeaderNames.LOCATION, uri);
        context.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private void setContentTypeHeader(HttpResponse response, File file) {
        MimetypesFileTypeMap mimeypesFileTypeMap = new MimetypesFileTypeMap();
        response.headers().set(HttpHeaderNames.CONTENT_TYPE,
                //查看
                "text/plain; charset=UTF-8");
                
                //下载
                //mimeypesFileTypeMap.getContentType(file.getPath()));
    }
}
