package com.janey668.echo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

@Slf4j
public class Server {

    public static void main(String[] args) throws InterruptedException {
        ChannelFuture channelFuture = new ServerBootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        ChannelPipeline pipeline = nioSocketChannel.pipeline();
                        pipeline.addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                if (msg instanceof ByteBuf) {
                                    log.info("接受客户端发来的消息: {}", ((ByteBuf) msg).toString(Charset.defaultCharset()));
                                    ctx.writeAndFlush(msg);
                                }
                                super.channelRead(ctx, msg);
                            }

                            @Override
                            public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                                ChannelFuture closeFuture = ctx.channel().close();
                                closeFuture.sync();
                                log.info("客户端链接异常关闭 Channel 成功");
                                super.exceptionCaught(ctx, cause);
                            }
                        });
                    }
                })
                .bind(new InetSocketAddress(8080));
        Channel channel = channelFuture.sync().channel();
        log.info("服务器启动成功");

    }

}
