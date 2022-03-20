package com.janey668.c2;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

import static com.janey668.netty.util.ByteBufferUtil.debugAll;

@Slf4j
public class Server3 {

    public static void main(String[] args) throws IOException {
        // 創建 ServerSocketChannel
        ServerSocketChannel ssc = ServerSocketChannel.open();
        // 綁定端口
        ssc.bind(new InetSocketAddress(8080));
        // 設置非阻塞模式
        ssc.configureBlocking(false);
        // 創建selector
        Selector selector = Selector.open();
        // 為 ServerSocketChannel 注冊 selector
        ssc.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            selector.select();
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = keys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                iterator.remove();
                if (selectionKey.isAcceptable()) {
                    ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
                    // 必须处理
                    SocketChannel sc = serverSocketChannel.accept();
                    sc.configureBlocking(false);
                    sc.register(selector, SelectionKey.OP_READ);
                    log.debug("连接已建立: {}", sc);
                } else if (selectionKey.isReadable()) {
                    SocketChannel sc = (SocketChannel) selectionKey.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(128);
                    int read = sc.read(buffer);
                    if(read == -1) {
                        selectionKey.cancel();
                        sc.close();
                    } else {
                        buffer.flip();
                        debugAll(buffer);
                    }
                }
            }
        }

    }

}
