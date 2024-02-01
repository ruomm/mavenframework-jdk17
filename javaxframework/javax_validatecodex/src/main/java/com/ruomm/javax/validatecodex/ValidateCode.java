/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年9月23日 下午4:49:21
 */
package com.ruomm.javax.validatecodex;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

public class ValidateCode {
    public static boolean verify(String sessionCode, String code) {
        if (null == sessionCode || null == code || sessionCode.length() <= 0 || code.length() <= 0) {
            return false;
        }
        return sessionCode.equalsIgnoreCase(code);
    }

    public static boolean verify(String sessionCode, String code, boolean isTrustAll) {
        if (isTrustAll) {
            return true;
        }
        if (null == sessionCode || null == code || sessionCode.length() <= 0 || code.length() <= 0) {
            return false;
        }
        return sessionCode.equalsIgnoreCase(code);
    }

    private int width = 160;

    private int height = 40;

    private int codeCount = 4;

    private int lineCount = 150;

    private String code = null;

    private BufferedImage buffImg = null;
    private String fontVal = null;
    private boolean isRotated = true;

    //	private char[] codeSequence = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R',
//			'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
//			'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
    private char[] codeSequence = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R',
            'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    public ValidateCode() {
        createCode();
    }

    public ValidateCode(int width, int height) {
        this.width = width;
        this.height = height;
        createCode();
    }

    public ValidateCode(int width, int height, int codeCount, int lineCount) {
        this.width = width;
        this.height = height;
        this.codeCount = codeCount;
        this.lineCount = lineCount;
        createCode();
    }

    public ValidateCode(int width, int height, int codeCount, int lineCount, String fontVal) {
        this.width = width;
        this.height = height;
        this.codeCount = codeCount;
        this.lineCount = lineCount;
        this.fontVal = fontVal;
        createCode();
    }

    public ValidateCode(int width, int height, int codeCount, int lineCount, String fontVal, boolean isRotated) {
        this.width = width;
        this.height = height;
        this.codeCount = codeCount;
        this.lineCount = lineCount;
        this.fontVal = fontVal;
        this.isRotated = isRotated;
        createCode();
    }

    public void createCode() {
        int x = 0;
        int fontHeight = 0;
        int codeY = 0;
        int red = 0;
        int green = 0;
        int blue = 0;

        x = this.width / (this.codeCount + 2);
//		fontHeight = this.height - 2;
//		codeY = this.height - 4;
        fontHeight = Math.round(this.height * 0.8f);
        codeY = this.height - (this.height - fontHeight) * 6 / 10;

        this.buffImg = new BufferedImage(this.width, this.height, 1);
        Graphics2D g = this.buffImg.createGraphics();
        Random random = new Random();

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, this.width, this.height);
        ImgFontByte imgFontCommon = new ImgFontByte(fontVal);
        Font fontCommon = imgFontCommon.getFont(fontHeight);
        g.setFont(fontCommon);

        for (int i = 0; i < this.lineCount; i++) {
            int xs = random.nextInt(this.width);
            int ys = random.nextInt(this.height);
            int xe = xs + random.nextInt(this.width / 8);
            int ye = ys + random.nextInt(this.height / 8);
            red = random.nextInt(255);
            green = random.nextInt(255);
            blue = random.nextInt(255);
            g.setColor(new Color(red, green, blue));
            g.drawLine(xs, ys, xe, ye);
        }

        StringBuffer randomCode = new StringBuffer();

        for (int i = 0; i < this.codeCount; i++) {
            ImgFontByte imgFont = new ImgFontByte(fontVal);
            Font font = imgFont.getFont(fontHeight);
            if (isRotated) {
                AffineTransform affineTransform = new AffineTransform();
                int rad = random.nextInt(71) - 35;
                affineTransform.rotate(Math.toRadians(rad), 0, 0);
                Font rotatedFont = font.deriveFont(affineTransform);
                g.setFont(rotatedFont);
            }
            String strRand = String.valueOf(this.codeSequence[random.nextInt(this.codeSequence.length)]);

            red = random.nextInt(255);
            green = random.nextInt(255);
            blue = random.nextInt(255);
            g.setColor(new Color(red, green, blue));
            g.drawString(strRand, (i + 1) * x, codeY);

            randomCode.append(strRand);
        }

        this.code = randomCode.toString();
    }

    public void write(String path) throws IOException {
        OutputStream sos = new FileOutputStream(path);
        write(sos);
    }

    public void write(OutputStream sos) throws IOException {
        ImageIO.write(this.buffImg, "png", sos);
        sos.close();
    }

    public BufferedImage getBuffImg() {
        return this.buffImg;
    }

    public String getCode() {
        return this.code;
    }
}
