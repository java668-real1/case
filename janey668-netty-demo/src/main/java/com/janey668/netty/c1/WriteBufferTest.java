package com.janey668.netty.c1;

import com.janey668.netty.util.ByteBufferUtil;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class WriteBufferTest {

    public static void main(String[] args) throws IOException {
//        FileChannel channel = new FileInputStream("janey668-netty-demo/data.txt").getChannel();
        ByteBuffer allocate = ByteBuffer.allocate(16);
//        channel.read(allocate);
        allocate.put("你好".getBytes());
        ByteBufferUtil.debugAll(allocate);
        allocate.flip();
        System.out.println((char) allocate.get());

    }



}
