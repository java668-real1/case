package com.janey668.demo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
 
public class NioServer {
    /**端口**/
    private static final int PORT = 8081;
    /**buffer大小**/
    private static final int DEFAULT_BUFFER_SIZE = 1024;
    private final Selector selector;
    private final ByteBuffer readBuffer = ByteBuffer.allocate(NioServer.DEFAULT_BUFFER_SIZE);
    private final ByteBuffer writeBuffer = ByteBuffer.allocate(NioServer.DEFAULT_BUFFER_SIZE);
 
    private static int count = 0;
 
    public static void main(String[] args) throws IOException {
        new NioServer().start();
    }
 
    public NioServer() throws IOException {
        //创建一个服务端channel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
 
        //设置为非阻塞
        serverSocketChannel.configureBlocking(false);
 
        //获取服务器socket
        ServerSocket socket = serverSocketChannel.socket();
        //绑定ip和端口
        socket.bind(new InetSocketAddress(NioServer.PORT));
 
        //创建多路复用选择器，并保持打开状态，直到close
        selector = Selector.open();
 
        //将服务器管道注册到selector上，并监听accept事件
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("server start on port: " + NioServer.PORT);
        start();
    }
 
    public void start() throws IOException {
 
        //selector是阻塞的，直到至少有一个客户端连接。
        while (selector.select() > 0) {
            //SelectionKey是channel想Selector注册的令牌，可以通过chancel取消（不是立刻取消，会放进一个cancel list里面，下一次select时才会把它彻底删除）
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                iterator.remove();
                //当这个key的channel已经准备好接收套接字连接
                if(selectionKey.isAcceptable()) {
                    connectHandle(selectionKey);
                }
 
                //当这个key的channel已经准备好读取数据时
                if(selectionKey.isReadable()) {
                    readHandle(selectionKey);
                }
            }
        }
    }
 
    /**
     * 处理连接
     * @param selectionKey
     */
    private void connectHandle(SelectionKey selectionKey) throws IOException {
        //注意，服务端用的是ServerSocketChannel，BIO中是ServerSocket
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
        SocketChannel socketChannel = serverSocketChannel.accept();
        if(socketChannel == null) {
            return;
        }
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ|SelectionKey.OP_WRITE);
 
        System.out.println("客户端连接成功，当前总数：" + (++count));
 
        writeBuffer.clear();
        writeBuffer.put("连接成功".getBytes(StandardCharsets.UTF_8));
        writeBuffer.flip();
        socketChannel.write(writeBuffer);
    }
 
    /**
     * 读取数据
     * @param selectionKey
     */
    private void readHandle(SelectionKey selectionKey){
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        try {
            readBuffer.clear();
            int read = socketChannel.read(readBuffer);
            if(read > 0) {
                readBuffer.flip();
                String receiveData = StandardCharsets.UTF_8.decode(readBuffer).toString();
 
                System.out.println("收到客户端消息: " + receiveData);
 
                writeBuffer.clear();
                writeBuffer.put(receiveData.getBytes(StandardCharsets.UTF_8));
                writeBuffer.flip();
                socketChannel.write(writeBuffer);
            }
 
        }catch (Exception e) {
            try {
                socketChannel.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            System.out.println("客户端断开了连接~~");
            count--;
        }
    }
}