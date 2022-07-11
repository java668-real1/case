package com.janey668.netty.bytebuf;

import com.janey668.netty.util.ByteBufUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.handler.codec.FixedLengthFrameDecoder;

public class SliceTest {

    public static void main(String[] args) {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        buffer.writeBytes(new byte[] {1, 2, 3, 4, 51, 6, 'A', 'B', 'C', 'D'});
        ByteBufUtil.log(buffer);
        ByteBuf b1 = buffer.slice(0, 5);
        ByteBufUtil.log(b1);
        ByteBuf b2 = buffer.slice(5, 5);
        ByteBufUtil.log(b2);
        byte b = b2.readByte();
        System.out.println(b);
        ByteBuf b3 = buffer.slice();
        ByteBufUtil.log(b3);
        b3.writeBytes(new byte[] {1});
        ByteBufUtil.log(b3);

    }

}
