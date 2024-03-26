package com.cx.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@ChannelHandler.Sharable
@Component
public class NettyServer {
    @Autowired
    private ServerHandle serverHandle;

    @Resource
    private ServerHandle01 serverHandle01;


    public void run() throws InterruptedException {

        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {

            ServerBootstrap bootstrap = new ServerBootstrap();

            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.config().setRecvByteBufAllocator(new FixedRecvByteBufAllocator(65535));  //设置接受最大字节
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast("decoder", new StringDecoder());//加入解码器
                            pipeline.addLast("encoder", new StringEncoder());//加入编码器

                            pipeline.addLast(serverHandle);//自定义
                        }
                    });

            ServerBootstrap bootstrap01 = new ServerBootstrap();
            bootstrap01.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new HttpServerCodec());//http协议编码器
                            pipeline.addLast(new ChunkedWriteHandler());//以块的形式来写
                            pipeline.addLast(new HttpObjectAggregator(1024 * 1024 * 1024));//获取请求的最大的长度
                            pipeline.addLast(new WebSocketServerProtocolHandler("/chat"));//适配 ws://127.0.0.1:6001/chat
                            pipeline.addLast(serverHandle01);//自定义

                        }
                    });

            ChannelFuture channelFuture = bootstrap.bind(8089).sync();
            ChannelFuture channelFuture01 = bootstrap01.bind(8099).sync();
            channelFuture01.channel().closeFuture().sync();
            channelFuture.channel().closeFuture().sync();

        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
