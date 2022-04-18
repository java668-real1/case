package com.janey668;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;

import java.io.IOException;

public class Main {

    private static final String IMAGE_MAGICK_PATH = "D:\\Program Files\\ImageMagick-7.1.0-Q16-HDRI";

    public static void main(String[] args) {
        ConvertCmd cmd = new ConvertCmd();
        cmd.setSearchPath(IMAGE_MAGICK_PATH);

        IMOperation op = new IMOperation();
        op.addImage("http://tx6.a.kwimgs.com/kimg/uhead/AB/2020/02/26/14/CjtCTWpBeU1EQXlNall4TkRBM016QmZNVGd3TWpFMU1qRTVNbDh4WDJoa09UUXhYemN5Tnc9PV9iLmpwZxCVzNcv:750x750.heif");
        op.addImage("janey668-juc-demo/file/Flower1.webp");

        try {
            cmd.run(op);
        } catch (IOException | InterruptedException | IM4JavaException e) {
            e.printStackTrace();
        }

    }
}