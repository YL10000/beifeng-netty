package com.beifeng.hadoop.netty.httpFile;

import java.net.InetAddress;
import java.net.UnknownHostException;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class HttpFileServer {
    
    private static final String DEFAULT_URL="/src/main/java/com/beifeng/hadoop/netty";
    
    public static void main(String[] args) throws Exception {
       int port=18080;
       new HttpFileServer().run(port, DEFAULT_URL);
    }
    
    public void run(final int port,final String url) throws Exception {
        EventLoopGroup bossGroup=new NioEventLoopGroup();
        EventLoopGroup workGroup=new NioEventLoopGroup();
        try {
            String ip = InetAddress.getLocalHost().getHostAddress();
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workGroup).channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel channel)
                                throws Exception {
                            
                            //消息解码器
                            channel.pipeline().addLast("http-decoder",new HttpRequestDecoder());
                            
                            /**
                             * 将多个消息转为单一的FullHttpRequest或FullHttpResponse
                             * http解码器在每个http消息中会生成多个消息对象，
                             *  httpRequest/httpRespone、httpContent、LastHttpContent
                             */
                            channel.pipeline().addLast("http-aggregator",new HttpObjectAggregator(65536));
                            
                            //消息编码器
                            channel.pipeline().addLast("http-encoder",new HttpResponseEncoder());
                            
                            /**
                             * 作用：支持异步发送大的码流，但不占用太多的内存，防止发生java内存溢出错误
                             */
                            channel.pipeline().addLast("http-chunked",new ChunkedWriteHandler());
                            channel.pipeline().addLast("fileServerHandler",new HttpFileServerHandler(url));
                        }
                    });
            ChannelFuture future = bootstrap.bind(ip, port).sync();
            System.out.println("http 文件服务器启动，网址为：http://" + ip + ":" + port + url);
            future.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
        
    }
    

}
