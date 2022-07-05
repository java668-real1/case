package com.janey668.netty.eventloop;

import io.netty.channel.DefaultEventLoopGroup;

/**
 * @author Jerry.chen
 * @desc
 * @date 2022/07/05 16:07
 **/
public class EventLoopTest {

    public static void main(String[] args) {
        DefaultEventLoopGroup group = new DefaultEventLoopGroup(2);
        System.out.println(group.next());
        System.out.println(group.next());
        System.out.println(group.next());
    }
}