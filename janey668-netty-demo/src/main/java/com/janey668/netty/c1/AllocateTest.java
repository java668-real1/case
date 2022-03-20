package com.janey668.netty.c1;

import java.nio.ByteBuffer;

public class AllocateTest {

    public static void main(String[] args) {
        ByteBuffer allocate = ByteBuffer.allocate(16);
        ByteBuffer allocateDirect = ByteBuffer.allocateDirect(16);
        System.out.println(allocate);
        System.out.println(allocateDirect);

    }
}
