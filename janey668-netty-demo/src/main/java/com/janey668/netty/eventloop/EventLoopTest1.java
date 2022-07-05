package com.janey668.netty.eventloop;

import io.netty.channel.DefaultEventLoopGroup;
import io.netty.util.concurrent.EventExecutor;

/**
 * @author Jerry.chen
 * @desc
 * @date 2022/07/05 16:07
 **/
public class EventLoopTest1 {

    public static void main(String[] args) {
        DefaultEventLoopGroup group = new DefaultEventLoopGroup(2);
        for (EventExecutor loop : group) {
            System.out.println(loop);
        }
    }
}