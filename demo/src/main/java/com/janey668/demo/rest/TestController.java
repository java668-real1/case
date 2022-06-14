package com.janey668.demo.rest;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

@RestController
public class TestController {

    private static final String IMAGE_MAGICK_PATH = "D:\\Program Files\\ImageMagick-7.1.0-Q16-HDRI";
    /**
     * 回显图片
     *
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping("show.jpg")
    public void show(String heif, HttpServletRequest request, HttpServletResponse response) throws IOException {
        //获取传入参数
        String attachId = request.getParameter("attachId");

        // 下载图片
        //查询文件详情
        String path = downImg(heif);
        String trans = trans(path);
        File file1 = new File(path);
        File file = new File(trans);
        if (!file.exists()) {
            return;
        }
        FileInputStream inputStream = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        inputStream.read(data);
        inputStream.close();
        response.setContentType("image/png");
        OutputStream os = response.getOutputStream();
        os.write(data);
        os.flush();
        os.close();
        file1.delete();
        file.delete();
    }

    public String downImg(String heif) {
        //http连接
        HttpURLConnection conn = null;
        //文件输入流
        InputStream inputStream = null;
        //缓存字节流
        BufferedInputStream bs = null;
        //文件输出流
        FileOutputStream out = null;
        try {
            File file_name = new File("D:\\work\\project\\github\\case\\demo\\file");
            if (!file_name.isDirectory() && !file_name.exists()) {
                file_name.mkdirs();
            }
            String path = file_name + "\\" + UUID.randomUUID(); //+ ".heif";
            out = new FileOutputStream(path);
            //建立连接
            URL httpurl = new URL(heif);
            conn = (HttpURLConnection) httpurl.openConnection();
            //提交表单
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setDoOutput(false);
            //post方式不能使用缓存
            conn.setUseCaches(false);
            //连接指定资源
            conn.connect();

            System.out.println("===" + conn.getContentType());

            //获取网络输入流
            inputStream = conn.getInputStream();
            bs = new BufferedInputStream(inputStream);
            //创建一个1k的数据缓冲
            byte b[] = new byte[1024];
            //读取到的数据长度
            int len = 0;
            //开始读取
            while ((len = bs.read(b)) != -1) {
                out.write(b, 0, len);
            }
            System.out.println("下载完成");
            return path;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (bs != null) {
                    bs.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return "";
    }

    public String trans(String path) {
        ConvertCmd cmd = new ConvertCmd();
        cmd.setSearchPath(IMAGE_MAGICK_PATH);
        long start = System.currentTimeMillis();
        System.out.println(start);
        IMOperation op = new IMOperation();
//        op.addImage("http://tx6.a.kwimgs.com/kimg/uhead/AB/2020/02/26/14/CjtCTWpBeU1EQXlNall4TkRBM016QmZNVGd3TWpFMU1qRTVNbDh4WDJoa09UUXhYemN5Tnc9PV9iLmpwZxCVzNcv:750x750.heif");
        op.addImage(path);
        String target = "D:\\work\\project\\github\\case\\demo\\file\\" + UUID.randomUUID() + ".png";
        op.addImage(target);
        try {
            cmd.run(op);
        } catch (IOException | InterruptedException | IM4JavaException e) {
            e.printStackTrace();
        }
        System.out.println(System.currentTimeMillis() - start);
        return target;
    }

}
