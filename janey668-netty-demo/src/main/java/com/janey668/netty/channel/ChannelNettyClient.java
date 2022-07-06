package com.janey668.netty.channel;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;

@Slf4j
public class ChannelNettyClient {


    public static void main(String[] args) throws Exception {
        NioEventLoopGroup work = new NioEventLoopGroup();
        ChannelFuture channelFuture = new Bootstrap()
                .group(work)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new StringEncoder());
                        pipeline.addLast(new StringDecoder());
                        pipeline.addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                System.out.println(msg);
                                super.channelRead(ctx, msg);
                            }
                        });
                    }
                })
                .connect("localhost", 8080);

        Channel channel = channelFuture.sync().channel();

        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String s = scanner.nextLine();
                if ("q".equals(s)) {
                    channel.close();
                    break;
                } else {
                    channel.writeAndFlush(s);
                }

            }
        }).start();

        channel.closeFuture().addListener((future)-> {
            System.out.println("close...");
            log.debug("处理关闭之后的操作");
            work.shutdownGracefully();
        });
    }

}
