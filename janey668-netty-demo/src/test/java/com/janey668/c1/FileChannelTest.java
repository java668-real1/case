package com.janey668.c1;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicInteger;

public class FileChannelTest {

    @Test
    public void testTransferTo() {
        String FROM = "data.txt";
        String TO = "to.txt";
        long start = System.nanoTime();
        try (FileChannel from = new FileInputStream(FROM).getChannel();
             FileChannel to = new FileOutputStream(TO).getChannel();
        ) {
            from.transferTo(0, from.size(), to);
        } catch (IOException e) {
            e.printStackTrace();
        }
        long end = System.nanoTime();
        System.out.println("transferTo 用时：" + (end - start) / 1000_000.0);
    }


    @Test
    public void testTransferTo1() {
        try (
                FileChannel from = new FileInputStream("data.txt").getChannel();
                FileChannel to = new FileOutputStream("to.txt").getChannel();
        ) {
            // 效率高，底层会利用操作系统的零拷贝进行优化
            long size = from.size();
            // left 变量代表还剩余多少字节
            for (long left = size; left > 0; ) {
                System.out.println("position:" + (size - left) + " left:" + left);
                left -= from.transferTo((size - left), left, to);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testPath() {
        Path source = Paths.get("data.txt"); // 相对路径 使用 user.dir 环境变量来定位 1.txt

        boolean exists = Files.exists(source);
        System.out.println(exists);


//        Path source = Paths.get("d:\\1.txt"); // 绝对路径 代表了  d:\1.txt
//
//        Path source = Paths.get("d:/1.txt"); // 绝对路径 同样代表了  d:\1.txt
//
//        Path projects = Paths.get("d:\\data", "projects"); // 代表了  d:\data\projects
    }

    @Test
    public void testFiles() throws IOException {
        Path path = Paths.get("src/d1/d2");
        Files.createDirectory(path);
    }

    @Test
    public void testFiles1() throws IOException {
        Path path = Paths.get("test/d1/d2");
        Files.createDirectories(path);
    }

    @Test
    public void testCopy() throws IOException {
        Path source = Paths.get("data.txt");
        Path target = Paths.get("target.txt");

        Files.copy(source, target);
    }
    @Test
    public void testCopy1() throws IOException {
        Path source = Paths.get("data.txt");
        Path target = Paths.get("target.txt");

        Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
    }


    @Test
    public void testMove() throws IOException {
        Path source = Paths.get("data.txt");
        Path target = Paths.get("testMove.txt");

        Files.move(source, target, StandardCopyOption.ATOMIC_MOVE);
    }


    @Test
    public void tesDelete() throws IOException {
        Path target = Paths.get("d1");
        Files.delete(target);
    }


    @Test
    public void testWalkFileTree() throws IOException {
        Path path = Paths.get("C:\\Program Files\\Java\\jdk1.8.0_181");
        AtomicInteger dirCount = new AtomicInteger();
        AtomicInteger fileCount = new AtomicInteger();
        Files.walkFileTree(path, new SimpleFileVisitor<Path>(){
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
                    throws IOException {
                System.out.println(dir);
                dirCount.incrementAndGet();
                return super.preVisitDirectory(dir, attrs);
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                    throws IOException {
                System.out.println(file);
                fileCount.incrementAndGet();
                return super.visitFile(file, attrs);
            }
        });
        System.out.println(dirCount); // 133
        System.out.println(fileCount); // 1479
    }


    @Test
    public void testWalkFileTree1() throws IOException {

        Path path = Paths.get("C:\\Program Files\\Java\\jdk1.8.0_181");
        AtomicInteger fileCount = new AtomicInteger();
        Files.walkFileTree(path, new SimpleFileVisitor<Path>(){
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                    throws IOException {
                if (file.toFile().getName().endsWith(".jar")) {
                    fileCount.incrementAndGet();
                }
                return super.visitFile(file, attrs);
            }
        });
        System.out.println(fileCount); // 724
    }

}
