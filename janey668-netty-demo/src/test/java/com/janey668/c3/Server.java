package com.janey668.c3;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Server {

    public static void main(String[] args) {
        new ServerBootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel channel) throws Exception {
                        channel.pipeline()
                                .addLast(new StringDecoder())
                                .addLast(new SimpleChannelInboundHandler<String>() {
                                    @Override
                                    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String msg) throws Exception {
                                        System.out.println(msg);
                                    }
                                });
                    }
                })
                .bind(8080);
    }
}
