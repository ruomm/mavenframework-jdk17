/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年6月7日 上午12:00:17
 */
package com.ruomm.javax.basex.imagex;

import com.ruomm.javax.corex.FileUtils;
import com.ruomm.javax.corex.StringUtils;
import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class ImageUtil {
    private final static Log log = LogFactory.getLog(ImageUtil.class);
    public final static int TRANS_ALPHA_COLOR_OFFSET = 15;
    public final static float DW_WATER_IMAGE_ALPHA_DEFAULT_MIN = 0.2f;
    public final static float DW_WATER_IMAGE_ALPHA_DEFAULT_MAX = 1.0f;
    public final static float DW_WATER_IMAGE_ALPHA_MIN = 0.2f;
    public final static float DW_WATER_IMAGE_ALPHA_MAX = 1.0f;

    public static ImageSize getImageSize(File imageFile) {
        ImageSize imageSize = new ImageSize();
        imageSize.setWidth(-1);
        imageSize.setHeight(-1);
        if (null == imageFile || !imageFile.exists()) {
            return imageSize;
        }
        BufferedImage img = null;
        try {
            img = ImageIO.read(imageFile);
            if (null != img) {
                imageSize.setWidth(img.getWidth(null));
                imageSize.setHeight(img.getHeight(null));
            }
        } catch (Exception e) {
            imageSize.setWidth(-1);
            imageSize.setHeight(-1);
            log.error("Error:getImageSize", e);

        } finally {
            img = null;
        }
        return imageSize;
    }

    public static ImageSize getImageSize(String imageFilePath) {
        File imageFile = null;
        try {
            imageFile = new File(imageFilePath);
        } catch (Exception e) {
            // TODO: handle exception
            imageFile = null;
            log.error("Error:getImageSize", e);
        }
        return getImageSize(imageFile);
    }

    /**
     * 通过读取文件并获取其width及height的方式，来判断判断当前文件是否图片，这是一种非常简单的方式。
     *
     * @param imageFile
     * @return
     */
    public static boolean isImage(File imageFile) {
        if (null == imageFile || !imageFile.exists()) {
            return false;
        }
        Image img = null;
        try {
            img = ImageIO.read(imageFile);
            if (img == null || img.getWidth(null) <= 0 || img.getHeight(null) <= 0) {
                return false;
            }
            return true;
        } catch (Exception e) {
            log.error("Error:isImage", e);
            return false;
        } finally {
            img = null;
        }
    }

    public static boolean isImage(String imageFilePath) {
        File imageFile = null;
        try {
            imageFile = new File(imageFilePath);
        } catch (Exception e) {
            // TODO: handle exception
            imageFile = null;
            log.error("Error:isImage", e);
        }
        return isImage(imageFile);
    }

    /**
     *
     * @param imgSrcPath 来源图片路径，如：D:\\Temp\\pic_src.jpg
     * @param imgDesPath 输出图片路径，如：D:\\Temp\\pic_des.jpg，若是不填写则覆盖原来的图片
     * @return 是否转换成功
     */
    public final static boolean formatImage(String imgSrcPath, String imgDesPath) {
        return formatWaterImage(imgSrcPath, imgDesPath, null, 0, 0, 0);
    }

    /**
     *
     * @param imgSrcPath   来源图片路径，如：D:\\Temp\\pic_src.jpg
     * @param imgDesPath   输出图片路径，如：D:\\Temp\\pic_des.jpg，若是不填写则覆盖原来的图片
     * @param imgWaterPath 水印图片路径，如：D:\\Temp\\water_img.jpg，若不填写则不添加水印，只把图片复制一遍，防止图片注入非安全代码
     * @param waterX       水印图片距离目标图片右侧侧的偏移量，如果x<0, 则在最右侧
     * @param waterY       水印图片距离目标图片下侧的偏移量，如果y<0, 则在最下侧
     * @param waterAlpha   透明度(0.0 -- 1.0, 0.0为完全透明，1.0为完全不透明)
     * @return 是否转换成功
     */
    public final static boolean formatWaterImage(String imgSrcPath, String imgDesPath, String imgWaterPath, int waterX,
                                                 int waterY, float waterAlpha) {
        Image imageSrc = null;
        Image imageWater = null;
        Graphics2D g = null;
        BufferedImage bufferedImage = null;
        boolean isGraphicsDispose = false;
        try {
            File fileSrc = new File(imgSrcPath);
            File fileDes = null;
            if (StringUtils.isEmpty(imgDesPath)) {
                fileDes = fileSrc;
            } else {
                fileDes = FileUtils.createFile(imgDesPath);
            }
            if (null == fileSrc || null == fileDes || !fileSrc.exists() || !fileSrc.isFile()) {
                return false;
            }
            String ext = StringUtils.isEmpty(imgDesPath) ? imgSrcPath.substring(imgSrcPath.lastIndexOf(".") + 1)
                    : imgDesPath.substring(imgDesPath.lastIndexOf(".") + 1);
            if (StringUtils.isEmpty(ext) || ext.length() > 6) {
                return false;
            }
            imageSrc = ImageIO.read(fileSrc);
            int width = imageSrc.getWidth(null);
            int height = imageSrc.getHeight(null);
            if (null == imageSrc || width <= 0 || height <= 0) {
                return false;
            }
            bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            g = bufferedImage.createGraphics();
            g.drawImage(imageSrc, 0, 0, width, height, null);
            // 加载水印图片。
            if (!StringUtils.isEmpty(imgWaterPath)) {
                imageWater = ImageIO.read(new File(imgWaterPath));
                int width_1 = imageWater.getWidth(null);
                int height_1 = imageWater.getHeight(null);

                // 若是水印不存在则返回错误
                if (null == imageWater || width_1 <= 0 || height_1 <= 0) {
                    g.dispose();
                    isGraphicsDispose = true;
                    return false;
                }
                // 若是水印大于图片的5分之4大小则返回错误
                if (width_1 > width * 4 / 5 || height_1 > height * 4 / 5) {
                    g.dispose();
                    isGraphicsDispose = true;
                    return false;
                }
                float dwAlpha = 0.5f;
                if (waterAlpha > DW_WATER_IMAGE_ALPHA_MAX) {
                    dwAlpha = DW_WATER_IMAGE_ALPHA_DEFAULT_MAX;
                } else if (waterAlpha < DW_WATER_IMAGE_ALPHA_MIN) {
                    dwAlpha = DW_WATER_IMAGE_ALPHA_DEFAULT_MIN;
                } else {
                    dwAlpha = waterAlpha;
                }
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, dwAlpha));
                // 设置水印图片的位置(右下)。
//				int widthDiff = width - width_1;
//				int heightDiff = height - height_1;
//				int dwX = 0;
//				int dwY = 0;
//				if (waterX <= 0) {
//					dwX = widthDiff;
//				}
//				else {
//					dwX = widthDiff - waterX;
//				}
//				if (waterY <= 0) {
//					dwY = heightDiff;
//				}
//				else {
//					dwY = heightDiff - waterY;
//				}
//				if (dwX < 0) {
//					dwX = 0;
//				}
//				if (dwY < 0) {
//					dwY = 0;
//				}
                // 设置水印图片的位置(居中)。
                int widthDiff = (width - width_1) / 2;
                int heightDiff = (height - height_1) / 2;
                int dwX = widthDiff + waterX;
                int dwY = heightDiff + waterY;
                if (dwX < 0) {
                    dwX = 0;
                } else if (dwX > width - width_1) {
                    dwX = width - width_1;
                }
                if (dwY < 0) {
                    dwY = 0;
                } else if (dwY > height - height_1) {
                    dwY = height - height_1;
                }
                // 将水印图片“画”在原有的图片的制定位置。
                g.drawImage(imageWater, dwX, dwY, width_1, height_1, null);
            }
            // 关闭画笔。
            g.dispose();
            isGraphicsDispose = true;
            // 保存目标图片。
            boolean flagImageWrite = ImageIO.write(bufferedImage, ext, fileDes);
            return flagImageWrite;

        } catch (Exception e) {
            log.error("Error:formatWaterImage", e);
            return false;
        } finally {
            try {
                if (null != g && !isGraphicsDispose) {
                    g.dispose();
                }

            } catch (Exception e) {
                // TODO: handle exception
                log.error("Error:formatWaterImage", e);
            }
            g = null;
            imageSrc = null;
            imageWater = null;
            bufferedImage = null;
        }

    }

    /**
     *
     * @param imgSrcPath   来源图片路径，如：D:\\Temp\\pic_src.jpg
     * @param imgDesPath   输出图片路径，如：D:\\Temp\\pic_des.jpg，若是不填写则覆盖原来的图片
     * @param imgWaterPath 水印图片路径，如：D:\\Temp\\water_img.jpg，若不填写则不添加水印，只把图片复制一遍，防止图片注入非安全代码
     *
     * @param waterAlpha   透明度(0.0 -- 1.0, 0.0为完全透明，1.0为完全不透明)
     * @param xStep        水印图片重复绘制X轴偏移量
     * @param yStep        水印图片重复绘制Y轴偏移量
     * @param repeatCount  水印图片重复绘制次数
     * @return 是否转换成功
     */
    public final static boolean formatWaterImageRepeat(String imgSrcPath, String imgDesPath, String imgWaterPath,
                                                       float waterAlpha, int xStep, int yStep, int repeatCount) {
        Image imageSrc = null;
        Image imageWater = null;
        Graphics2D g = null;
        BufferedImage bufferedImage = null;
        boolean isGraphicsDispose = false;
        try {
            File fileSrc = new File(imgSrcPath);
            File fileDes = null;
            if (StringUtils.isEmpty(imgDesPath)) {
                fileDes = fileSrc;
            } else {
                fileDes = FileUtils.createFile(imgDesPath);
            }
            if (null == fileSrc || null == fileDes || !fileSrc.exists() || !fileSrc.isFile()) {
                return false;
            }
            String ext = StringUtils.isEmpty(imgDesPath) ? imgSrcPath.substring(imgSrcPath.lastIndexOf(".") + 1)
                    : imgDesPath.substring(imgDesPath.lastIndexOf(".") + 1);
            if (StringUtils.isEmpty(ext) || ext.length() > 6) {
                return false;
            }
            imageSrc = ImageIO.read(fileSrc);
            int width = imageSrc.getWidth(null);
            int height = imageSrc.getHeight(null);
            if (null == imageSrc || width <= 0 || height <= 0) {
                return false;
            }
            bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            g = bufferedImage.createGraphics();
            g.drawImage(imageSrc, 0, 0, width, height, null);
            // 加载水印图片。
            if (!StringUtils.isEmpty(imgWaterPath)) {
                imageWater = ImageIO.read(new File(imgWaterPath));
                int width_1 = imageWater.getWidth(null);
                int height_1 = imageWater.getHeight(null);

                // 若是水印不存在则返回错误
                if (null == imageWater || width_1 <= 0 || height_1 <= 0) {
                    g.dispose();
                    isGraphicsDispose = true;
                    return false;
                }
                // 若是水印大于图片的5分之4大小则返回错误
                if (width_1 > width * 4 / 5 || height_1 > height * 4 / 5) {
                    g.dispose();
                    isGraphicsDispose = true;
                    return false;
                }
                float dwAlpha = 0.5f;
                if (waterAlpha > DW_WATER_IMAGE_ALPHA_MAX) {
                    dwAlpha = DW_WATER_IMAGE_ALPHA_DEFAULT_MAX;
                } else if (waterAlpha < DW_WATER_IMAGE_ALPHA_MIN) {
                    dwAlpha = DW_WATER_IMAGE_ALPHA_DEFAULT_MIN;
                } else {
                    dwAlpha = waterAlpha;
                }
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, dwAlpha));
                // 设置水印图片的位置(右下)。
//				int widthDiff = width - width_1;
//				int heightDiff = height - height_1;
//				int dwX = 0;
//				int dwY = 0;
//				if (waterX <= 0) {
//					dwX = widthDiff;
//				}
//				else {
//					dwX = widthDiff - waterX;
//				}
//				if (waterY <= 0) {
//					dwY = heightDiff;
//				}
//				else {
//					dwY = heightDiff - waterY;
//				}
//				if (dwX < 0) {
//					dwX = 0;
//				}
//				if (dwY < 0) {
//					dwY = 0;
//				}
                // 设置水印图片的位置(居中)。
                int widthDiff = (width - width_1) / 2;
                int heightDiff = (height - height_1) / 2;
                int dwX = widthDiff;
                int dwY = heightDiff;
                if (dwX < 0) {
                    dwX = 0;
                } else if (dwX > width - width_1) {
                    dwX = width - width_1;
                }
                if (dwY < 0) {
                    dwY = 0;
                } else if (dwY > height - height_1) {
                    dwY = height - height_1;
                }
                // 将水印图片“画”在原有的图片的制定位置。
                g.drawImage(imageWater, dwX, dwY, width_1, height_1, null);
                if (xStep != 0 && yStep != 0) {
                    for (int i = 1; i <= repeatCount; i++) {
                        g.drawImage(imageWater, dwX + i * xStep, dwY + i * yStep, width_1, height_1, null);
                        g.drawImage(imageWater, dwX - i * xStep, dwY - i * yStep, width_1, height_1, null);
                    }
                }
            }
            // 关闭画笔。
            g.dispose();
            isGraphicsDispose = true;
            // 保存目标图片。
            boolean flagImageWrite = ImageIO.write(bufferedImage, ext, fileDes);
            return flagImageWrite;

        } catch (Exception e) {
            log.error("Error:formatWaterImageRepeat", e);
            return false;
        } finally {
            try {
                if (null != g && !isGraphicsDispose) {
                    g.dispose();
                }

            } catch (Exception e) {
                // TODO: handle exception
                log.error("Error:formatWaterImageRepeat", e);
            }
            g = null;
            imageSrc = null;
            imageWater = null;
            bufferedImage = null;
        }

    }

    /**
     *
     * @param imgSrcPath   来源图片路径，如：D:\\Temp\\pic_src.jpg
     * @param imgDesPath   输出图片路径，如：D:\\Temp\\pic_des.jpg，若是不填写则覆盖原来的图片
     * @param imgWaterPath 水印图片路径，如：D:\\Temp\\water_img.jpg，若不填写则不添加水印，只把图片复制一遍，防止图片注入非安全代码
     * @param waterX       水印图片距离目标图片右侧侧的偏移量，如果x<0, 则在最右侧
     * @param waterY       水印图片距离目标图片下侧的偏移量，如果y<0, 则在最下侧
     * @param waterAlpha   透明度(0.0 -- 1.0, 0.0为完全透明，1.0为完全不透明)
     * @param quality 图片压缩质量，浮点型 大于0小于等于1.0
     * @param density 图片分辨率
     * @return 是否转换成功
     */
    public final static boolean formatWaterImageXY(String imgSrcPath, String imgDesPath, String imgWaterPath, int waterX,
                                                   int waterY, float waterAlpha, float quality, int density) {
        Image imageSrc = null;
        Image imageWater = null;
        Graphics2D g = null;
        BufferedImage bufferedImage = null;
        boolean isGraphicsDispose = false;
        try {
            File fileSrc = new File(imgSrcPath);
            File fileDes = null;
            if (StringUtils.isEmpty(imgDesPath)) {
                fileDes = fileSrc;
            } else {
                fileDes = FileUtils.createFile(imgDesPath);
            }
            if (null == fileSrc || null == fileDes || !fileSrc.exists() || !fileSrc.isFile()) {
                return false;
            }
            String ext = StringUtils.isEmpty(imgDesPath) ? imgSrcPath.substring(imgSrcPath.lastIndexOf(".") + 1)
                    : imgDesPath.substring(imgDesPath.lastIndexOf(".") + 1);
            if (StringUtils.isEmpty(ext) || ext.length() > 6) {
                return false;
            }
            imageSrc = ImageIO.read(fileSrc);
            int width = imageSrc.getWidth(null);
            int height = imageSrc.getHeight(null);
            if (null == imageSrc || width <= 0 || height <= 0) {
                return false;
            }
            bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            g = bufferedImage.createGraphics();
            g.drawImage(imageSrc, 0, 0, width, height, null);
            // 加载水印图片。
            if (!StringUtils.isEmpty(imgWaterPath)) {
                imageWater = ImageIO.read(new File(imgWaterPath));
                int width_1 = imageWater.getWidth(null);
                int height_1 = imageWater.getHeight(null);

                // 若是水印不存在则返回错误
                if (null == imageWater || width_1 <= 0 || height_1 <= 0) {
                    g.dispose();
                    isGraphicsDispose = true;
                    return false;
                }
                // 若是水印大于图片的5分之4大小则返回错误
//                if (width_1 > width * 4 / 5 || height_1 > height * 4 / 5) {
//                    g.dispose();
//                    isGraphicsDispose = true;
//                    return false;
//                }
                float dwAlpha = 0.5f;
                if (waterAlpha > DW_WATER_IMAGE_ALPHA_MAX) {
                    dwAlpha = DW_WATER_IMAGE_ALPHA_DEFAULT_MAX;
                } else if (waterAlpha < DW_WATER_IMAGE_ALPHA_MIN) {
                    dwAlpha = DW_WATER_IMAGE_ALPHA_DEFAULT_MIN;
                } else {
                    dwAlpha = waterAlpha;
                }
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, dwAlpha));
                // 设置水印图片的位置
                int dwX = waterX;
                int dwY = waterY;
                // 将水印图片“画”在原有的图片的制定位置。
                g.drawImage(imageWater, dwX, dwY, width_1, height_1, null);
            }
            // 关闭画笔。
            g.dispose();
            isGraphicsDispose = true;
            // 保存目标图片。
            return ImageWriteUtils.writeImage(bufferedImage, quality, density, imgDesPath);

        } catch (Exception e) {
            log.error("Error:formatWaterImage", e);
            return false;
        } finally {
            try {
                if (null != g && !isGraphicsDispose) {
                    g.dispose();
                }

            } catch (Exception e) {
                // TODO: handle exception
                log.error("Error:formatWaterImage", e);
            }
            g = null;
            imageSrc = null;
            imageWater = null;
            bufferedImage = null;
        }

    }


    /**
     * 创建内容图片
     *
     * @param content 文字内容
     * @param font    字体
     * @param width   宽度
     * @param height  高度
     * @param ratio   斜角
     * @param outPath 输出路径
     */
    public static void createContentImage(String content, Font font, int width, int height, int ratio, String outPath) {
        OutputStream sos = null;
        Graphics2D g = null;
        try {
            BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            g = buffImg.createGraphics();
            // 使背景透明
            buffImg = g.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
            g.dispose();
            g = buffImg.createGraphics();
            // 消除文字
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            // 消除画图锯齿
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//			g.setColor(Color.red);
//			g.fillRect(0, 0, width, height);

            Font fontCommon = null;
            if (null == font) {
                int fontHeight = Math.round(height * 0.8f);
                fontCommon = new Font("微软雅黑", Font.BOLD + Font.ITALIC, fontHeight);

            } else {
                fontCommon = font;
            }
            g.setColor(Color.BLACK);
            g.setFont(fontCommon);
            FontMetrics fontMetrics = g.getFontMetrics();
            log.debug("Graphics2D 字体上下位置->" + fontMetrics.getAscent() + ":" + fontMetrics.getDescent());
//			int fontHeight = fontMetrics.getAscent() - fontMetrics.getDescent();
            int fontHeight = fontMetrics.getDescent() - fontMetrics.getAscent();
            log.debug("Graphics2D 字体高度->" + fontHeight);
            int strWidth = fontMetrics.stringWidth(content);
            g.drawString(content, (width - strWidth) / 2, height - (height - fontHeight) / 2);
            if (ratio != 0) {
                buffImg = modifyImageRatio(buffImg, ratio);
            }
            g.dispose();
            sos = new FileOutputStream(outPath);
            ImageIO.write(buffImg, "png", sos);
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:createContentImage", e);
        } finally {
            try {
                if (null != sos) {
                    sos.close();
                }
                sos = null;
            } catch (Exception e) {
                // TODO: handle exception
                log.error("Error:createContentImage", e);
            }
        }
    }

    /**
     *
     * @param mini  贴图
     * @param ratio 旋转角度
     * @return
     */
    public static BufferedImage modifyImageRatio(BufferedImage mini, int ratio) {
        int src_width = mini.getWidth();
        int src_height = mini.getHeight();
        // 针对图片旋转重新计算图的宽*高
        Rectangle rect_des = CalcRotatedSize(new Rectangle(new Dimension(src_width, src_height)), ratio);
        // 设置生成图片的宽*高，色彩度
        BufferedImage res = new BufferedImage(rect_des.width, rect_des.height, BufferedImage.TYPE_INT_RGB);
        // 创建画布
        Graphics2D g2 = res.createGraphics();
        res = g2.getDeviceConfiguration().createCompatibleImage(rect_des.width, rect_des.height,
                Transparency.TRANSLUCENT);
        g2 = res.createGraphics();
        // 消除文字
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        // 消除画图锯齿
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // 重新设定原点坐标
        g2.translate((rect_des.width - src_width) / 2, (rect_des.height - src_height) / 2);
        // 执行图片旋转，rotate里包含了translate，并还原了原点坐标
        g2.rotate(Math.toRadians(ratio), src_width / 2, src_height / 2);
        g2.drawImage(mini, null, null);
        g2.dispose();
        return res;
    }

    private static Rectangle CalcRotatedSize(Rectangle src, int angel) {
        if (angel >= 90) {
            if (angel / 90 % 2 == 1) {
                int temp = src.height;
                src.height = src.width;
                src.width = temp;
            }
            angel = angel % 90;
        }

        double r = Math.sqrt(src.height * src.height + src.width * src.width) / 2;
        double len = 2 * Math.sin(Math.toRadians(angel) / 2) * r;
        double angel_alpha = (Math.PI - Math.toRadians(angel)) / 2;
        double angel_dalta_width = Math.atan((double) src.height / src.width);
        double angel_dalta_height = Math.atan((double) src.width / src.height);

        int len_dalta_width = (int) (len * Math.cos(Math.PI - angel_alpha - angel_dalta_width));
        int len_dalta_height = (int) (len * Math.cos(Math.PI - angel_alpha - angel_dalta_height));
        int des_width = Math.abs(src.width) + Math.abs(len_dalta_width * 2);
        int des_height = Math.abs(src.height) + Math.abs(len_dalta_height * 2);
        return new Rectangle(new Dimension(des_width, des_height));
    }

    /**
     * 去除白色背景，变成PNG图片
     * @param src 原图片
     * @param src1 去除白色背景后的图片
     * @return
     */
    public static boolean transferAlphaWhite(String src, String src1) {
        boolean result = false;
        try {
            BufferedImage bi = ImageIO.read(new File(src));
            Image image = (Image) bi;
            ImageIcon imageIcon = new ImageIcon(image);
            BufferedImage bufferedImage = new BufferedImage(imageIcon.getIconWidth(), imageIcon.getIconHeight(),
                    BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D g2D = (Graphics2D) bufferedImage.getGraphics();
            g2D.drawImage(imageIcon.getImage(), 0, 0, imageIcon.getImageObserver());
            int alpha = 0;
            for (int j1 = bufferedImage.getMinY(); j1 < bufferedImage.getHeight(); j1++) {
                for (int j2 = bufferedImage.getMinX(); j2 < bufferedImage.getWidth(); j2++) {
                    int rgb = bufferedImage.getRGB(j2, j1);

                    int R = (rgb & 0xff0000) >> 16;
                    int G = (rgb & 0xff00) >> 8;
                    int B = (rgb & 0xff);
                    if (((255 - R) < TRANS_ALPHA_COLOR_OFFSET) && ((255 - G) < TRANS_ALPHA_COLOR_OFFSET) && ((255 - B) < TRANS_ALPHA_COLOR_OFFSET)) {
                        rgb = ((alpha + 1) << 24) | (rgb & 0x00ffffff);
                    }
                    bufferedImage.setRGB(j2, j1, rgb);
                }
            }
            g2D.drawImage(bufferedImage, 0, 0, imageIcon.getImageObserver());
            ImageIO.write(bufferedImage, "png", new File(src1));// 直接输出文件
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return result;
    }
}
