/**
 * @copyright www.ruomm.com
 * @author 牛牛-wanruome@163.com
 * @create 2020年9月2日 下午4:10:57
 */
package com.ruomm.javax.propertiesx.loader;

class PropertyLoaderFileBean {

    private String loadFile;
    private String path;
    private String fileName;
    private String fileNameWithoutExtension;
    private String fileExtension;

    public String getLoadFile() {
        return loadFile;
    }

    public void setLoadFile(String loadFile) {
        this.loadFile = loadFile;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileNameWithoutExtension() {
        return fileNameWithoutExtension;
    }

    public void setFileNameWithoutExtension(String fileNameWithoutExtension) {
        this.fileNameWithoutExtension = fileNameWithoutExtension;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    @Override
    public String toString() {
        return "LoaderFileBean [loadFile=" + loadFile + ", path=" + path + ", fileName=" + fileName
                + ", fileNameWithoutExtension=" + fileNameWithoutExtension + ", fileExtension=" + fileExtension + "]";
    }

}
