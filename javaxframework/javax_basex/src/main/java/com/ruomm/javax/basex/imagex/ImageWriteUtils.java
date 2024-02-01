package com.ruomm.javax.basex.imagex;

import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;
import org.w3c.dom.Element;

import javax.imageio.*;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Iterator;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright 像衍科技-idr.ai
 * @create 2022/5/30 11:00
 */
public class ImageWriteUtils {
    private final static Log log = LogFactory.getLog(ImageWriteUtils.class);
    private final static double INCH_2_CM = 2.54d;
    private final static float DEFAULT_QUALITY = 0.9f;

    /**
     * 存储图片为JPEG格式或PNG格式，默认JPEG格式
     *
     * @param rendered 已渲染的的图片
     * @param quality  图片质量
     * @param dpi      图片分辨率
     */
    public static boolean writeImage(BufferedImage rendered, float quality, int dpi, String outFilePath) {
        if (null == rendered || null == outFilePath || outFilePath.length() <= 0) {
            return false;
        }
        if (outFilePath.toLowerCase().endsWith(".png")) {
            return writePNG(rendered, quality, dpi, outFilePath);
        } else {
            return writeJPG(rendered, quality, dpi, outFilePath);
        }
    }

    /**
     * 存储图片为JPEG格式或PNG格式，默认JPEG格式
     *
     * @param rendered 已渲染的的图片
     * @param quality  图片质量
     * @param dpi      图片分辨率
     */
    public static boolean writeImage(BufferedImage rendered, float quality, int dpi, File outFile) {
        if (null == rendered || null == outFile) {
            return false;
        }
        String outFilePath = outFile.getPath();
        if (outFilePath.toLowerCase().endsWith(".png")) {
            return writePNG(rendered, quality, dpi, outFilePath);
        } else {
            return writeJPG(rendered, quality, dpi, outFilePath);
        }
    }

    /**
     * 存储图片为JPEG格式
     *
     * @param rendered 已渲染的的图片
     * @param quality  图片质量
     * @param dpi      图片分辨率
     */
    public static boolean writeJPG(BufferedImage rendered, float quality, int dpi, String outFilePath) {
        if (null == rendered || null == outFilePath || outFilePath.length() <= 0) {
            return false;
        }
        return writeJPG(rendered, quality, dpi, new File(outFilePath));
    }

    /**
     * 存储图片为JPEG格式
     *
     * @param rendered 已渲染的的图片
     * @param quality  图片质量
     * @param dpi      图片分辨率
     */
    public static boolean writeJPG(BufferedImage rendered, float quality, int dpi, File outFile) {
        if (null == rendered || null == outFile) {
            return false;
        }
        boolean result = false;
        FileImageOutputStream outputStream = null;
        try {
            ImageWriter writer = ImageIO.getImageWritersByFormatName("jpeg").next();
            ImageWriteParam writeParams = writer.getDefaultWriteParam();
            writeParams.setCompressionMode(ImageWriteParam.MODE_EXPLICIT); // Needed see javadoc
            if (quality >= 0 && quality <= 1) {

                writeParams.setCompressionQuality(quality); // Highest quality
            } else {
                writeParams.setCompressionQuality(DEFAULT_QUALITY);
            }
            IIOMetadata data = null;
            // 设置图片分分辨率
            if (dpi > 0) {
                data = writer.getDefaultImageMetadata(new ImageTypeSpecifier(rendered), writeParams);
//                setDPI(data,dpi);
                Element tree = (Element) data.getAsTree("javax_imageio_jpeg_image_1.0");
                Element jfif = (Element) tree.getElementsByTagName("app0JFIF").item(0);
                jfif.setAttribute("Xdensity", dpi + "");
                jfif.setAttribute("Ydensity", dpi + "");
                jfif.setAttribute("resUnits", "1"); // density is dots per inch
            }
            outputStream = new FileImageOutputStream(outFile);
            writer.setOutput(outputStream);
            writer.write(data, new IIOImage(rendered, null, null), writeParams);
            result = true;
        } catch (Throwable e) {
            log.error("writeJPG", e);
        } finally {
            try {
                if (null != outputStream) {
                    outputStream.close();
                }
            } catch (Exception e) {
                log.error("writeJPG", e);
            }
        }
        return result;
    }

    /**
     * 存储图片为PNG格式
     *
     * @param rendered 已渲染的的图片
     * @param quality  图片质量
     * @param dpi      图片分辨率
     */
    public static boolean writePNG(BufferedImage rendered, float quality, int dpi, String outFilePath) {
        if (null == rendered || null == outFilePath || outFilePath.length() <= 0) {
            return false;
        }
        return writePNG(rendered, quality, dpi, new File(outFilePath));
    }

    /**
     * 存储图片为PNG格式
     *
     * @param rendered 已渲染的的图片
     * @param quality  图片质量
     * @param dpi      图片分辨率
     */
    public static boolean writePNG(BufferedImage rendered, float quality, int dpi, File outFile) {
        if (null == rendered || null == outFile) {
            return false;
        }
        boolean result = false;
        FileImageOutputStream outputStream = null;
        try {
            for (Iterator<ImageWriter> iw = ImageIO.getImageWritersByFormatName("png"); iw.hasNext(); ) {
                ImageWriter writer = iw.next();
                ImageWriteParam writeParams = writer.getDefaultWriteParam();
                ImageTypeSpecifier typeSpecifier = ImageTypeSpecifier.createFromBufferedImageType(BufferedImage.TYPE_INT_RGB);
                IIOMetadata metadata = writer.getDefaultImageMetadata(typeSpecifier, writeParams);
                if (metadata.isReadOnly() || !metadata.isStandardMetadataFormatSupported()) {
                    continue;
                }
                try {
//                    if (quality >= 0 && quality <= 1) {
//                        writeParams.setCompressionQuality(quality); // Highest quality
//                    } else {
//                        writeParams.setCompressionQuality(DEFAULT_QUALITY);
//                    }
                    setDPI(metadata, dpi);
                    outputStream = new FileImageOutputStream(outFile);
                    writer.setOutput(outputStream);
                    writer.write(metadata, new IIOImage(rendered, null, metadata), writeParams);
                    result = true;
                } catch (Exception e) {
                    log.error("writeJPG", e);
                } finally {
                    try {
                        if (null != outputStream) {
                            outputStream.close();
                            outputStream = null;
                        }
                    } catch (Exception e) {
                        log.error("writePNG", e);
                    }
                }
                break;
            }
        } catch (Throwable e) {
            log.error("writeJPG", e);
        }
        return result;
    }


    private static void setDPI(IIOMetadata metadata, int dpi) throws IIOInvalidTreeException {

        // for PMG, it's dots per millimeter
        double dotsPerMilli = 1.0 * dpi / 10 / INCH_2_CM;
        IIOMetadataNode horiz = new IIOMetadataNode("HorizontalPixelSize");
        horiz.setAttribute("value", Double.toString(dotsPerMilli));

        IIOMetadataNode vert = new IIOMetadataNode("VerticalPixelSize");
        vert.setAttribute("value", Double.toString(dotsPerMilli));

        IIOMetadataNode dim = new IIOMetadataNode("Dimension");
        dim.appendChild(horiz);
        dim.appendChild(vert);

        IIOMetadataNode root = new IIOMetadataNode("javax_imageio_1.0");
        root.appendChild(dim);

        metadata.mergeTree("javax_imageio_1.0", root);
    }
}
