package com.janey668.echo;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.Scanner;

@Slf4j
public class client {

    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        ChannelFuture channelFuture = new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        ChannelPipeline pipeline = nioSocketChannel.pipeline();
                        pipeline.addLast(new SimpleChannelInboundHandler<ByteBuf>() {
                            @Override
                            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                log.info("客户端建立链接");
                                super.channelInactive(ctx);
                            }

                            @Override
                            protected void channelRead0(ChannelHandlerContext ctx, ByteBuf byteBuf) throws Exception {
                                log.info("入站处理器收到到消息：{}", byteBuf.toString(Charset.defaultCharset()));
//                                byteBuf.retain();
//                                ctx.writeAndFlush(byteBuf);

                            }
                        });
                        pipeline.addLast(new ChannelOutboundHandlerAdapter() {
                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                log.info("出站处理器收到到消息: {}", msg);
                                super.write(ctx, msg, promise);
                            }
                        });
                    }
                })
                .connect(new InetSocketAddress("localhost", 8080))
                .sync();
        Channel channel = channelFuture.channel();
        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String content = scanner.nextLine();
                if ("q".equals(content)) {
                    channel.close();
                    break;
                }
                ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
                buffer.writeBytes(content.getBytes());
                channel.writeAndFlush(buffer);
            }
        }).start();

        ChannelFuture closeFuture = channel.closeFuture();
        closeFuture.addListener((future) -> {
            log.info("客户端退出了");
            group.shutdownGracefully();
        });
    }
}
