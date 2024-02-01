/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年6月11日 下午1:15:31
 */
package com.ruomm.javax.basex.imagex;

public class ImageSize {
    private int width;
    private int height;

    public ImageSize() {
    }

    public ImageSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isImage() {
        if (width <= 0 || height <= 0) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ImageSize{" +
                "width=" + width +
                ", height=" + height +
                '}';
    }
}
