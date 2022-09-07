package com.janey668.demo.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Jerry.chen
 * @version 1.0
 * @description: 用 ffmpeg 客户端命令录制
 * @date 2021/6/9 19:39
 */
@Slf4j
public class FFmpegUtil {

    private final static Integer SUCCESS = 0;
    private final static Integer MAX_VALUE = 2000;

    /**
     * 测试当前多媒体工具是否可以正常工作
     *
     * @return
     */
    public static boolean isExecutable(String ffmpegPath) {
        File ffmpegFile = new File(ffmpegPath);
        if (!ffmpegFile.exists()) {
            log.error("--- 工作状态异常，因为传入的ffmpeg可执行程序路径下的ffmpeg文件不存在！ ---");
            return false;
        }
        List<String> cmds = new ArrayList<>(1);
        cmds.add("-version");
        String ffmpegVersionStr = executeCommand(cmds);
        if (StringUtils.isBlank(ffmpegVersionStr)) {
            log.error("--- 工作状态异常，因为ffmpeg命令执行失败！ ---");
            return false;
        }
        log.info("--- 工作状态正常 ---");
        return true;
    }

    /**
     * 执行FFmpeg命令
     *
     * @param commands 要执行的FFmpeg命令
     * @return FFmpeg程序在执行命令过程中产生的各信息，执行出错时返回null
     */
    public static String executeCommand(List<String> commands) {
        if (CollectionUtils.isEmpty(commands)) {
            log.error("--- 指令执行失败，因为要执行的FFmpeg指令为空！ ---");
            return null;
        }
        LinkedList<String> ffmpegCmds = new LinkedList<>(commands);
        log.info("--- 待执行的FFmpeg指令为：--- {}" + ffmpegCmds);

        Runtime runtime = Runtime.getRuntime();
        Process ffmpeg = null;
        try {
            // 创建stopwatch并开始计时
            // 执行ffmpeg指令
            ProcessBuilder builder = new ProcessBuilder();
            builder.command(ffmpegCmds);
            builder.redirectErrorStream(false);
            ffmpeg = builder.start();
            log.info("--- 开始执行FFmpeg指令：--- 执行线程名：" + builder.toString());
            // 取出输出流和错误流的信息
            // 注意：必须要取出ffmpeg在执行命令过程中产生的输出信息，如果不取的话当输出流信息填满jvm存储输出留信息的缓冲区时，线程就回阻塞住
            PrintStream errorStream = new PrintStream(ffmpeg.getErrorStream());
            PrintStream inputStream = new PrintStream(ffmpeg.getInputStream());
            errorStream.start();
            inputStream.start();
            // 输出执行的命令信息
            String cmdStr = Arrays.toString(ffmpegCmds.toArray()).replace(",", "");
            log.info("--- 正在执行的FFmpeg指令为：--- {}", cmdStr);
            // 等待ffmpeg命令执行完
            int state = ffmpeg.waitFor();
            // 获取执行结果字符串
            String result = errorStream.stringBuffer.append(inputStream.stringBuffer).toString();
            // 防止错误信息过长导致超出数据库字段存储范围，这里将过长的错误信息只截取最后1000个字符
            if (result.length() > MAX_VALUE) {
                result = result.substring(result.length() - MAX_VALUE);
            }
            if (state != SUCCESS) {
                log.info("--- 已执行的FFmepg命令： ---" + cmdStr + " 已执行完毕,执行结果：【异常】, {}", result);
                return result;
            }
            log.info("--- FFmepg命令已执行完毕： ---" + cmdStr + " 已执行完毕,执行结果：{}", errorStream.stringBuffer.append(inputStream.stringBuffer).toString());
            return null;
        } catch (Exception e) {
            log.error("--- FFmpeg命令执行出错！ --- 出错信息： {}" + e);
            throw new RuntimeException(e.getMessage());
        } finally {
            if (null != ffmpeg) {
                ProcessKiller ffmpegKiller = new ProcessKiller(ffmpeg);
                // JVM退出时，先通过钩子关闭FFmepg进程
                runtime.addShutdownHook(ffmpegKiller);
            }
        }
    }

    public static String doLiveRecord(String ffmpegPath, String liveUrl, String savePath, String recordTime) {
        createFolder(savePath);
        // 构建命令
        List<String> command = new ArrayList();
        command.add(ffmpegPath);
        // 这个选项会将输入的读取速度降低到输入的本地帧速率。它对于实时输出(例如直播流)很有用。
//        command.add("-re");
        //时间
        command.add("-t");
        command.add(recordTime);
        command.add("-y");
        // 超时时间 10s 单位：微妙
        command.add("-timeout");
        command.add("10000000");
        // 使用持久连接如果设置为1，则默认值为0
        command.add("-multiple_requests");
        command.add("1");
        // 如果设置了eof，那么eof将被视为一个错误并导致重新连接，这对于实时/无限流非常有用。
//        command.add("-reconnect_at_eof");
//        command.add("1");
        // 如果设置，则即使流/不可查找的流也将在出现错误时重新连接。
//        command.add("-reconnect_streamed");
//        command.add("1");
        // 如果连接期间出现 TCP/TLS 错误，则自动重新连接。
//        command.add("-reconnect_on_network_error");
//        command.add("1");
        // 以秒为单位设置放弃重新连接的最大延迟
//        command.add("-reconnect_delay_max");
//        command.add("1");
        command.add("-i");
        command.add(liveUrl);
        //添加timebase fps等配置
        command.add("-r");
        command.add("25000/1001");
        command.add("-video_track_timescale");
        command.add("25k");
        //添加转成h264 录制ts的时候需要限制
        command.add("-c:v");
        command.add("libx264");
        //添加转成aac 录制ts的时候需要限制
        command.add("-c:a");
        command.add("aac");
        // -preset的参数主要调节编码速度和质量的平衡，有ultrafast、superfast、veryfast、faster、fast、medium、slow、slower、veryslow、placebo这10个选项，从快到慢
        command.add("-preset");
        command.add("ultrafast");
        // 设置缓冲区大小，太小会爆缓存，并且引起丢帧，而且容易引起传输卡死，太大没有明显影响
        command.add("-rtbufsize");
        command.add("3500k");
        // 码率
        command.add("-b:v");
        command.add("2134k");
        // Too many packets buffered for output stream 0:1.
        // [libmp3lame @ 0x562ac0d0ad00] 4 frames left in the queue on closing
        // Conversion failed! 异常处理：视频数据有问题，导致视频处理过快，容器封装时队列溢出。
        command.add("-max_muxing_queue_size");
        command.add("1024");
        command.add(savePath);

        //ffmpeg -i http://ivi.bupt.edu.cn/hls/cctv5phd.m3u8 -c:a copy -c:v copy d:\20180706-cctv5+live.mp4
//        command.add(ffmpegPath);
//        command.add("-t");
//        command.add(recordTime);
//        command.add("-i");
//        command.add(liveUrl);
//        command.add("-preset");
//        command.add("ultrafast");
//        command.add("-c:a");
//        command.add("copy");
//        command.add("-c:v");
//        command.add("copy");
//        command.add(savePath);
        String executeResult = executeCommand(command);
        return executeResult;
    }

    public static String doLiveRecordAndBitrate(String ffmpegPath, String liveUrl, String savePath, String recordTime,int bitrate) {
        createFolder(savePath);
        // 构建命令
        List<String> command = new ArrayList();
        command.add(ffmpegPath);
        //时间
        command.add("-t");
        command.add(recordTime);
        command.add("-y");
        // 超时时间 10s 单位：微妙
        command.add("-timeout");
        command.add("10000000");
        // 使用持久连接如果设置为1，则默认值为0
        command.add("-multiple_requests");
        command.add("1");
        command.add("-i");
        command.add(liveUrl);
        //添加timebase fps等配置
        command.add("-r");
        command.add("25000/1001");
        command.add("-video_track_timescale");
        command.add("25k");
        //添加转成h264 录制ts的时候需要限制
        command.add("-vcodec");
        command.add("h264");
        //添加转成aac 录制ts的时候需要限制
        command.add("-c:a");
        command.add("aac");
        // -preset的参数主要调节编码速度和质量的平衡，有ultrafast、superfast、veryfast、faster、fast、medium、slow、slower、veryslow、placebo这10个选项，从快到慢
        command.add("-preset");
        if(bitrate<=400){
            command.add("superfast");
        }else{
            command.add("ultrafast");
        }
        // 设置缓冲区大小，太小会爆缓存，并且引起丢帧，而且容易引起传输卡死，太大没有明显影响
        command.add("-rtbufsize");
        command.add("3500k");
        // 码率
        command.add("-b:v");
        command.add(bitrate+"k");
        // Too many packets buffered for output stream 0:1.
        // [libmp3lame @ 0x562ac0d0ad00] 4 frames left in the queue on closing
        // Conversion failed! 异常处理：视频数据有问题，导致视频处理过快，容器封装时队列溢出。
        command.add("-max_muxing_queue_size");
        command.add("1024");
        command.add(savePath);
        String executeResult = executeCommand(command);
        return executeResult;
    }

    public static String mp4ConvertTs(String ffmpegPath, String filePath, String savePath) {
        log.info("mp4ConvertTs------{}", savePath);
        List<String> command = new ArrayList();
        command.add(ffmpegPath);
        command.add("-i");
        command.add(filePath);
        command.add("-c");
        command.add("copy");
        command.add("-bsf:v");
        command.add("h264_mp4toannexb");
        command.add("-f");
        command.add("mpegts");
        command.add(savePath);
        String executeResult = executeCommand(command);
        return executeResult;
    }

    public static String doMergeByTs(String ffmpegPath, List<String> fileTsUrl, String savePath) {
        log.info("doMergeByTs------{}", savePath);
        createFolder(savePath);
        //拼接concat
        StringBuffer concatStbr = new StringBuffer();
        concatStbr.append("concat:");
        if(fileTsUrl.size() == 0 ){
            return "";
        }
        for (String fileUrl : fileTsUrl) {
            //判断文件是否存在，存在则放入
            File file = new File(fileUrl);
            if(file.exists()){
                concatStbr.append(fileUrl).append("|");
            }
        }
        concatStbr.setLength(concatStbr.length()-1);

        // 构建命令
        List<String> command = new ArrayList();
        command.add(ffmpegPath);
        command.add("-y");
        command.add("-i");
        command.add(concatStbr.toString());
        command.add("-c");
        command.add("copy");
        command.add("-bsf:a");
        command.add("aac_adtstoasc");
        command.add("-movflags");
        command.add("+faststart");
        command.add(savePath);
        String executeResult = executeCommand(command);
        if (StringUtils.isNotBlank(executeResult)) {
            throw new RuntimeException(executeResult);
        }
        return executeResult;
    }

    public static void doVideoSynthesis(String ffmpegPath, String filePath, String savePath) {
        log.info("savePath------{}", savePath);
        createFolder(savePath);
        // 构建命令
        List<String> command = new ArrayList();
        command.add(ffmpegPath);
        command.add("-f");
        command.add("concat");
        command.add("-safe");
        command.add("0");
        command.add("-y");
        command.add("-i");
        command.add(filePath);
        command.add("-c");
        command.add("copy");
        command.add(savePath);
        String executeResult = executeCommand(command);
        if (StringUtils.isNotBlank(executeResult)) {
            throw new RuntimeException(executeResult);
        }
    }

    public static void doVideoIntercept(String ffmpegPath, String liveUrl, String savePath, String startTime, String durationTime) {
        log.info("savePath------{}", savePath);
        createFolder(savePath);
        // 构建命令
        List<String> command = new ArrayList();
        command.add(ffmpegPath);
        command.add("-ss");
        command.add(startTime);
        command.add("-t");
        command.add(durationTime);
        command.add("-y");
        command.add("-i");
        command.add(liveUrl);
        command.add("-c:v");
        command.add("libx264");
        command.add("-c:a");
        command.add("aac");
        command.add("-strict");
        command.add("experimental");
        command.add("-b:a");
        command.add("98k");
        command.add(savePath);
        String executeResult = executeCommand(command);
        if (StringUtils.isNotBlank(executeResult)) {
            throw new RuntimeException(executeResult);
        }
    }


    /**
     * 获取视频总时间
     */
    public static int getVideoTime(String ffmpegPath, String liveUrl) {
        List<String> command = new ArrayList<>();
        command.add(ffmpegPath);
        command.add("-i");
        command.add(liveUrl);
        command.add("2>&1");
        command.add("|");
        command.add("grep");
        command.add("'Duration'");
        command.add("|");
        command.add("cut");
        command.add("-d");
        command.add("' '");
        command.add("-f");
        command.add("4");
        command.add("|");
        command.add("sed");
        command.add("s/,//");
        try {
            ProcessBuilder builder = new ProcessBuilder();
            builder.command(command);
            Process p = builder.start();

            //从输入流中读取视频信息
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line = "";
            while ((line = br.readLine()) != null) {
                stringBuilder.append(line);
            }
            br.close();

            //从视频信息中解析时长
            String regexDuration = "Duration: (.*?), start: (.*?), bitrate: (\\d*) kb\\/s";
            Pattern pattern = Pattern.compile(regexDuration);
            Matcher m = pattern.matcher(stringBuilder.toString());
            if (m.find()) {
                int time = getTimelen(m.group(1));
                log.info("解析到的视频时长：" + time + "s");
                return time;
            }
        } catch (Exception e) {
            log.error("--- FFmpeg命令执行出错！ --- 出错信息： {}", e);
        }
        return 0;
    }

    public static void createFolder(String filepath) {
        //如果文件夹不存在则创建
        File file = new File(filepath);
        File fileParent = file.getParentFile();
        if (!fileParent.exists()) {
            fileParent.mkdirs();
        }
    }

    /**
     * 在程序退出前结束已有的FFmpeg进程
     */
    private static class ProcessKiller extends Thread {
        private Process process;

        public ProcessKiller(Process process) {
            this.process = process;
        }

        @Override
        public void run() {
            this.process.destroy();
            log.info("--- 已销毁FFmpeg进程 --- 进程名： " + process.toString());
        }
    }


    /**
     * 用于取出ffmpeg线程执行过程中产生的各种输出和错误流的信息
     */
    static class PrintStream extends Thread {
        InputStream inputStream = null;
        BufferedReader bufferedReader = null;
        StringBuffer stringBuffer = new StringBuffer();

        public PrintStream(InputStream inputStream) {
            this.inputStream = inputStream;
        }

        @Override
        public void run() {
            try {
                if (null == inputStream) {
                    log.error("--- 读取输出流出错！因为当前输出流为空！---");
                }
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    log.debug(line);
                    stringBuffer.append(line + "*****");
                }
            } catch (Exception e) {
                log.error("--- 读取输入流出错了！--- 错误信息：" + e.getMessage());
            } finally {
                try {
                    if (null != bufferedReader) {
                        bufferedReader.close();
                    }
                    if (null != inputStream) {
                        inputStream.close();
                    }
                } catch (IOException e) {
                    log.error("--- 调用PrintStream读取输出流后，关闭流时出错！---");
                }
            }
        }
    }

    private static int getTimelen(String timelen) {
        int min = 0;
        String strs[] = timelen.split(":");
        if (strs[0].compareTo("0") > 0) {
            // 秒
            min += Integer.valueOf(strs[0]) * 60 * 60;
        }
        if (strs[1].compareTo("0") > 0) {
            min += Integer.valueOf(strs[1]) * 60;
        }
        if (strs[2].compareTo("0") > 0) {
            min += Math.round(Float.valueOf(strs[2]));
        }
        return min;
    }

    public static void createListFile(String filePath, String data) {
        createFolder(filePath);
        FileOutputStream outputStream = null;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            }
            outputStream = new FileOutputStream(file, true);//形参里面可追加true参数，表示在原有文件末尾追加信息
            outputStream.write(data.getBytes("utf-8"));
        } catch (IOException e) {
            e.printStackTrace();
            log.error("--- 创建ListFile文件出错！ --- 出错信息： {}" + e);
            throw new RuntimeException(e.getMessage());
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 功能描述
     *
     * @param file
     * @return void
     * @Author sean
     * @Description 删除文件夹
     * @Date 2:53 下午 2021/6/18
     **/
    public static void deleteAll(File file) {
        if (file.isFile() || file.list().length == 0) {
            file.delete();
        } else {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                deleteAll(files[i]);
                files[i].delete();
            }
            //如果文件本身就是目录 ，就要删除目录
            if (file.exists()) {
                file.delete();
            }
        }
    }







}