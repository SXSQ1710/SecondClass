package com.SecondClass.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.extra.qrcode.QrCodeUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @title: QrCodeUtils
 * @Author SXSQ
 * @Description //TODO
 * @Date 2022/10/31 23:23
 **/

public class QrCodeUtils {
    public static String createQRCode(String content){
        // buffer 二维码生成转base64 三个参数 第一个参数是二维码的内容 第二个参数是图片宽度 第三个参数是图片高度
        BufferedImage image = QrCodeUtil.generate(content, 200, 200);
        //输出流
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String base64 ="data:image/png;base64,"+ Base64.encode(stream.toByteArray());
        return base64;
    }
}
