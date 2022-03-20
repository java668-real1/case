package com.janey668.c2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

import static com.janey668.netty.util.ByteBufferUtil.debugAll;

public class Clinet {

    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();
        SocketChannel sc = SocketChannel.open();
        sc.connect(new InetSocketAddress("localhost", 8080));
        sc.write(Charset.defaultCharset().encode("hello!"));
        System.out.println("SocketChannel end");
//        sc.configureBlocking(false);
//        SelectionKey selectionKey = sc.register(selector, 0);
//        selectionKey.interestOps(SelectionKey.OP_READ);
//        while (true) {
//            selector.select();
//            Set<SelectionKey> selectionKeys = selector.selectedKeys();
//            Iterator<SelectionKey> iterator = selectionKeys.iterator();
//            while(iterator.hasNext()) {
//                SelectionKey next = iterator.next();
//                if (next.isReadable()) {
//                    SocketChannel c = (SocketChannel) next.channel();
//                    ByteBuffer buffer = ByteBuffer.allocate(16);
//                    c.read(buffer);
//                    buffer.flip();
//                    debugAll(buffer);
//                }
//            }
//
//        }
    }
}
