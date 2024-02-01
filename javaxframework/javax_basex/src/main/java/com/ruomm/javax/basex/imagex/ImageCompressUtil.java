package com.ruomm.javax.basex.imagex;

import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright www.ruomm.com
 * @create 2021/3/19 10:08
 */
public class ImageCompressUtil {
    private final static Log log = LogFactory.getLog(ImageCompressUtil.class);

    /**
     * 等比例压缩算法：
     * 算法思想：根据压缩基数和压缩比来压缩原图，生产一张图片效果最接近原图的缩略图
     *
     * @param srcURL      原图地址
     * @param deskURL     缩略图地址
     * @param maxSize     图片宽度和高度最大值
     * @param maxFileSize 目标图片最大值
     * @param density     图片分辨率
     */
    public static boolean compressImageMaxFileSize(String srcURL, String deskURL, int maxSize, long maxFileSize, int density) {
        if (maxFileSize <= 0) {
            return compressImage(srcURL, deskURL, maxSize, 0.8f, density);
        }
        boolean result = false;
        for (int i = 0; i <= 12; i++) {
            float quality = 1.0f - 0.05f * i;
            boolean tmpResult = compressImage(srcURL, deskURL, maxSize, quality, density);
            if (!tmpResult) {
                result = false;
                break;
            }
            try {
                File file = new File(deskURL);
                long fileSize = file.length();
                log.info("compressImageMaxFileSize->" + "设定的最大文件大小:" + maxFileSize + ",本次压缩后的文件大小:" + fileSize);
                if (fileSize <= maxFileSize) {
                    result = true;
                    break;
                } else {
                    file.delete();
                }
            } catch (Exception e) {
                log.error("compressImage", e);
                result = false;
                break;
            }
        }
        return result;
    }

    /**
     * 等比例压缩算法：
     * 算法思想：根据压缩基数和压缩比来压缩原图，生产一张图片效果最接近原图的缩略图
     *
     * @param srcURL  原图地址
     * @param deskURL 缩略图地址
     * @param maxSize 图片宽度和高度最大值
     * @param quality 图片质量
     * @param density 图片分辨率
     */
    public static boolean compressImage(String srcURL, String deskURL, int maxSize, float quality, int density) {
        boolean result = false;
        try {
            File srcFile = new File(srcURL);
            Image src = ImageIO.read(srcFile);
            int srcHeight = src.getHeight(null);
            int srcWidth = src.getWidth(null);
            if (srcHeight <= 0 || srcWidth <= 0) {
                return false;
            }
            int deskHeight = 0;// 缩略图高
            int deskWidth = 0;// 缩略图宽
            if (maxSize <= 0) {
                deskHeight = srcHeight;
                deskWidth = srcWidth;
            } else if (srcWidth <= maxSize && srcHeight <= maxSize) {
                deskHeight = srcHeight;
                deskWidth = srcWidth;
            } else if (srcHeight > srcWidth) {
                deskHeight = maxSize;
                deskWidth = srcWidth * maxSize / srcHeight;
            } else {
                deskWidth = maxSize;
                deskHeight = srcHeight * maxSize / srcWidth;
            }
            log.info("compressImage->" + "压缩图片宽度:" + deskWidth + ",高度:" + deskHeight + ",质量:" + quality + ",格式:jpg(ARGB)");
            BufferedImage tag = new BufferedImage(deskWidth, deskHeight, BufferedImage.TYPE_INT_RGB);
            tag.getGraphics().drawImage(src, 0, 0, deskWidth, deskHeight, null); //绘制缩小后的图
            return ImageWriteUtils.writeImage(tag, quality, density, deskURL);
        } catch (FileNotFoundException e) {
            log.error("compressImage", e);
        } catch (IOException e) {
            log.error("compressImage", e);
        } catch (Exception e) {
            log.error("compressImage", e);
        }
        return result;
    }

    /**
     * 图片自定义压缩算法：
     * 算法思想：根据压缩基数和压缩比来压缩原图，生产一张图片效果最接近原图的缩略图
     *
     * @param srcURL  原图地址
     * @param deskURL 缩略图地址
     * @param scaleX  图片宽度,若是宽度和高度都大于0则是自定义压缩，否则是等比压缩
     * @param scaleY  图片高度，若是宽度和高度都大于0则是自定义压缩，否则是等比压缩
     * @param quality 图片质量
     * @param density 图片分辨率
     */
    public static boolean compressImage(String srcURL, String deskURL, int scaleX, int scaleY, float quality, int density) {
        boolean result = false;
        try {
            File srcFile = new File(srcURL);
            Image src = ImageIO.read(srcFile);
            int srcHeight = src.getHeight(null);
            int srcWidth = src.getWidth(null);
            if (srcHeight <= 0 || srcWidth <= 0) {
                return false;
            }
            int deskHeight = 0;// 缩略图高
            int deskWidth = 0;// 缩略图宽
            if (scaleX <= 0 && scaleY <= 0) {
                deskWidth = srcWidth;
                deskHeight = srcHeight;
            } else if (scaleX > 0 && scaleY > 0) {
                deskWidth = scaleX;
                deskHeight = scaleY;
            } else {
                int maxSize = scaleX > 0 ? scaleX : scaleY;
                if (maxSize <= 0) {
                    deskWidth = srcWidth;
                    deskHeight = srcHeight;
                } else if (srcWidth <= maxSize && srcHeight <= maxSize) {
                    deskWidth = srcWidth;
                    deskHeight = srcHeight;
                } else if (srcHeight > srcWidth) {
                    deskHeight = maxSize;
                    deskWidth = srcWidth * maxSize / srcHeight;
                } else {
                    deskWidth = maxSize;
                    deskHeight = srcHeight * maxSize / srcWidth;
                }
            }


            log.info("compressImage->" + "压缩图片宽度:" + deskWidth + ",高度:" + deskHeight + ",质量:" + quality + ",格式:jpg(ARGB)");
            BufferedImage tag = new BufferedImage(deskWidth, deskHeight, BufferedImage.TYPE_INT_RGB);
            tag.getGraphics().drawImage(src, 0, 0, deskWidth, deskHeight, null); //绘制缩小后的图
            return ImageWriteUtils.writeImage(tag, quality, density, deskURL);
        } catch (FileNotFoundException e) {
            log.error("compressImage", e);
        } catch (IOException e) {
            log.error("compressImage", e);
        } catch (Exception e) {
            log.error("compressImage", e);
        }
        return result;
    }

}
