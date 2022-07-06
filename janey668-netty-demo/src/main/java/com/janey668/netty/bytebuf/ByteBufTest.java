package com.janey668.netty.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

public class ByteBufTest {

    public static void main(String[] args) {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        System.out.println(buffer);
        StringBuffer message = new StringBuffer();
        for (int i = 0; i < 300; i++) {
            message.append("a");
        }
        buffer.writeBytes(message.toString().getBytes());
        System.out.println(buffer);
    }

}
