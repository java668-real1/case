package com.janey668.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

/**
 *
 */
public class NioClient {
 
    private static final int PORT = 8081;
    private static final int DEFAULT_BUFFER_SIZE = 1024;
    private final Selector selector;
    private final ByteBuffer readBuffer = ByteBuffer.allocate(NioClient.DEFAULT_BUFFER_SIZE);
    private final ByteBuffer writeBuffer = ByteBuffer.allocate(NioClient.DEFAULT_BUFFER_SIZE);
 
    public static void main(String[] args) throws IOException {
        NioClient nioClient = new NioClient();
 
        //终端监听用户输入
        new Thread(nioClient::terminal).start();
 
        //这个方法是阻塞的，要放在最后
        nioClient.start();
    }
 
    public NioClient() throws IOException {
        selector = Selector.open();
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress(InetAddress.getLocalHost(), NioClient.PORT));
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
    }
 
    public void start() throws IOException {
        while (selector.select() > 0) {
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                //拿到selectionKey后要删除，否则会重复处理
                iterator.remove();
                if(selectionKey.isReadable()) {
                    handleRead(selectionKey);
                }
 
                //只要连接成功，selectionKey.isWritable()一直为true
                if(selectionKey.isWritable()) {
                    handleWrite(selectionKey);
                }
            }
        }
    }
 
    /**
     * 监听写操作
     */
    private void handleWrite(SelectionKey selectionKey) throws IOException {
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
 
        // writeBuffer有数据就直接写入，因为另开了线程监听用户读取，所以要上锁
        synchronized (writeBuffer) {
            writeBuffer.flip();
            while (writeBuffer.hasRemaining()) {
                socketChannel.write(writeBuffer);
            }
            writeBuffer.compact();
        }
 
    }
 
    /**
     * 监听读操作
     */
    private void handleRead(SelectionKey selectionKey) throws IOException {
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        readBuffer.clear();
        socketChannel.read(readBuffer);
 
        readBuffer.flip();
        String res = StandardCharsets.UTF_8.decode(readBuffer).toString();
        System.out.println("收到服务器发来的消息： " + res);
        readBuffer.clear();
    }
 
    /**
     * 监听终端的输入
     */
    private void terminal() {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try {
            String msg;
            while ((msg = bufferedReader.readLine()) != null) {
                synchronized (writeBuffer) {
                    writeBuffer.put((msg + "\r\n").getBytes(StandardCharsets.UTF_8));
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
 
    }
 
}