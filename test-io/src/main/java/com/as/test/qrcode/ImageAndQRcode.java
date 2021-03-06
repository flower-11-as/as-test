package com.as.test.qrcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

/**
 * @author as
 * @desc
 * @date 2018/12/24
 */
public class ImageAndQRcode {

    private static final int BLACK = 0xFF000000;
    private static final int WHITE = 0xFFFFFFFF;

    private ImageAndQRcode() {
    }


    private static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
            }
        }

        return image;
    }


    private static void writeToFile(BitMatrix matrix, String format, File file) throws IOException {
        BufferedImage image = toBufferedImage(matrix);
        if (!ImageIO.write(image, format, file)) {
            throw new IOException("Could not write an image of format " + format + " to " + file);
        }
    }

    /**
     * @param file      源文件(图片)
     * @param waterFile 水印文件(图片)
     * @param x         距离右下角的X偏移量
     * @param y         距离右下角的Y偏移量
     * @param alpha     透明度, 选择值从0.0~1.0: 完全透明~完全不透明
     * @return BufferedImage
     * @throws IOException
     * @Title: 构造图片
     * @Description: 生成水印并返回java.awt.image.BufferedImage
     */
    private static BufferedImage watermark(File file, File waterFile, String content, int x, int y, float alpha) throws IOException {

        // 获取底图
        BufferedImage buffImg = ImageIO.read(file);
        // 获取层图
        BufferedImage waterImg = ImageIO.read(waterFile);
        // 创建Graphics2D对象，用在底图对象上绘图
        Graphics2D g2d = buffImg.createGraphics();
        int waterImgWidth = waterImg.getWidth();// 获取层图的宽度
        int waterImgHeight = waterImg.getHeight();// 获取层图的高度

        // 在图形和图像中实现混合和透明效果
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
        // 绘制
        g2d.drawImage(waterImg, x, y, waterImgWidth, waterImgHeight, null);
        g2d.setColor(Color.white);
        g2d.setFont(new Font("宋体", Font.BOLD, 18));
        g2d.drawString(content, 170, 555);
        g2d.dispose();// 释放图形上下文使用的系统资源
        return buffImg;
    }

    /**
     * 输出水印图片
     *
     * @param buffImg  图像加水印之后的BufferedImage对象
     * @param savePath 图像加水印之后的保存路径
     */
    private void generateWaterFile(BufferedImage buffImg, String savePath) {
        int temp = savePath.lastIndexOf(".") + 1;
        try {
            ImageIO.write(buffImg, savePath.substring(temp), new File(savePath));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * @param text           二维码内容
     * @param sourceFilePath 底层图片路径
     * @param waterFilePath  二维码路径
     * @param saveFilePath   合成图片路径
     * @throws Exception
     */
    public static void addImageQRcode(String text, String sourceFilePath,
                                      String waterFilePath, String saveFilePath, String content) throws Exception {
        Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, 180, 180, hints);

        // 生成二维码
        File outputFile = new File(waterFilePath);
        ImageAndQRcode.writeToFile(bitMatrix, "png", outputFile);
        ImageAndQRcode newImageUtils = new ImageAndQRcode();

        content = StringUtils.isBlank(content) ? "" : content;
        while (content.length() < 10) {
            if (content.length() % 2 == 0) {
                content = " " + content;
            } else {
                content = content + " ";
            }
        }

        // 构建叠加层
        BufferedImage buffImg = ImageAndQRcode.watermark(new File(sourceFilePath), new File(waterFilePath), content, 135, 565, 1.0f);

        // 输出水印图片
        newImageUtils.generateWaterFile(buffImg, saveFilePath);
    }

    public static void main(String[] args) throws Exception {
//        addImageQRcode("http://www.baidu.com", "C:\\Users\\as\\Desktop\\background.png",
//                "C:\\Users\\as\\Desktop\\test.png", "C:\\Users\\as\\Desktop\\merge.png",
//                "阿水");
//        System.out.println(ImageAndQRcode.class.getResource("/"));
//        String content = "阿水";
//        content = new String(content.getBytes("utf-8"), StandardCharsets.UTF_8);
//        System.out.println(content);
        String a = (1 / 100f) + "";
        System.out.println(a);
        System.out.println(1 / 100f);
    }
}
