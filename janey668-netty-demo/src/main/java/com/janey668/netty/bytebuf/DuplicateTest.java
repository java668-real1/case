package com.janey668.netty.bytebuf;

import com.janey668.netty.util.ByteBufUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.nio.charset.Charset;

public class DuplicateTest {

    public static void main(String[] args) {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        buffer.writeCharSequence("abcdefghijk", Charset.defaultCharset());
        ByteBufUtil.log(buffer);
        ByteBuf duplicate = buffer.duplicate();
        ByteBufUtil.log(duplicate);
        duplicate.writeBytes(new byte[] {1});
        ByteBufUtil.log(duplicate);
    }

}
