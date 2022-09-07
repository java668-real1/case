package com.janey668.netty.bytebuf;

import com.janey668.netty.util.ByteBufUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author Jerry.chen
 * @desc
 * @date 2022/07/08 18:20
 **/
public class UnpoolTest {

    public static void main(String[] args) {
        ByteBuf buffer1 = ByteBufAllocator.DEFAULT.buffer();
        buffer1.writeBytes("abc".getBytes());
        ByteBuf buffer2 = ByteBufAllocator.DEFAULT.buffer();
        buffer2.writeBytes("def".getBytes());
        CompositeByteBuf byteBufs = Unpooled.compositeBuffer();
        CompositeByteBuf byteBufs1 = byteBufs.addComponents(true, buffer1, buffer2);
        ByteBufUtil.log(byteBufs1);
        CompositeByteBuf byteBufs2 = ByteBufAllocator.DEFAULT.compositeBuffer();

    }
}