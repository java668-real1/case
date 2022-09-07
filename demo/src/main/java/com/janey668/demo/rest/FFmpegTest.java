package com.janey668.demo.rest;

import com.janey668.demo.util.FFmpegUtil;
import org.apache.commons.lang3.time.DurationFormatUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jerry.chen
 * @desc
 * @date 2022/07/29 14:08
 **/
public class FFmpegTest {
    public static String ffmpegPath = "D:\\develop\\ffmpeg-4.3.1-win64-static\\bin\\ffmpeg.exe";
    public static String videoPath = "D:\\develop\\ffmpeg-4.3.1-win64-static\\bin\\dna\\6.mp4";
    public static String savePath = "D:\\develop\\ffmpeg-4.3.1-win64-static\\bin\\21\\66.mp4";

    public static void main(String[] args) {
        long l = System.currentTimeMillis();
        int videoTime = FFmpegUtil.getVideoTime(ffmpegPath, videoPath);
        long end = System.currentTimeMillis();
        System.out.println(end - l);
        System.out.println(videoTime);
        Map<String, String> scope = getScope(videoTime);
        FFmpegUtil.doVideoIntercept(ffmpegPath, videoPath, savePath, scope.get("s"), scope.get("l"));
    }

    public static Map getScope(int time) {
        int length = time / 2;
        int start = time / 2 - length / 2;
        int end = time / 2 + length / 2;
        String s = DurationFormatUtils.formatDuration(start * 1000L, "HH:mm:ss");
        String e = DurationFormatUtils.formatDuration(end * 1000L, "HH:mm:ss");
        String l = DurationFormatUtils.formatDuration(length * 1000L, "HH:mm:ss");
        Map<String, String> result = new HashMap<>(2);
        result.put("s", s);
        result.put("l", l);
        System.out.println(s + "-" + l);
        return result;
    }

}