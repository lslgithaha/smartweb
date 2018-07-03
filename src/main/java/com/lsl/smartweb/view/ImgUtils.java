package com.lsl.smartweb.view;

import net.coobird.thumbnailator.Thumbnails;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Create by LSL on 2018\7\3 0003
 * 描述：
 * 版本：1.0.0
 */
public class ImgUtils {

    /**
     * 方法名: ImgUtils.imgSizetoOut
     * 作者: LSL
     * 创建时间: 16:22 2018\7\3 0003
     * 描述: 处理图片并输出到流指定size
     * 参数: [filePath, size, outputStream]
     * 返回: void
     */
    public static void imgSizetoOut(String filePath, String size, OutputStream outputStream) throws IOException {
        String[]  xes = size.split("x");
        int width = Integer.valueOf(xes[0]);
        int height = Integer.valueOf(xes[1]);
        Thumbnails.of(filePath).size(width,height).toOutputStream(outputStream);
        outputStream.flush();
        outputStream.close();
    }
    /**
     * 方法名: ImgUtils.imgSizetoOut
     * 作者: LSL
     * 创建时间: 16:22 2018\7\3 0003
     * 描述: 处理图片并输出到流
     * 参数: [filePath, size, outputStream]
     * 返回: void
     */
    public static void imgToOut(String filePath, OutputStream outputStream) throws IOException {
        Thumbnails.of(filePath).scale(1).toOutputStream(outputStream);
        outputStream.flush();
        outputStream.close();
    }

}
