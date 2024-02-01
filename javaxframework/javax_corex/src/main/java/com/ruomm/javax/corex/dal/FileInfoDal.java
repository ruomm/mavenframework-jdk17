package com.ruomm.javax.corex.dal;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright 像衍科技-idr.ai
 * @create 2023/5/27 0:09
 */
public class FileInfoDal {
    // 全路径
    private String pathFull;
    // 相对路径
    private String pathLite;
    // 是否文件夹
    private boolean directory;

    private String sha;

    public String getPathFull() {
        return pathFull;
    }

    public void setPathFull(String pathFull) {
        this.pathFull = pathFull;
    }

    public String getPathLite() {
        return pathLite;
    }

    public void setPathLite(String pathLite) {
        this.pathLite = pathLite;
    }

    public boolean isDirectory() {
        return directory;
    }

    public void setDirectory(boolean directory) {
        this.directory = directory;
    }

    public String getSha() {
        return sha;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }

    @Override
    public String toString() {
        return "FileInfoDal{" +
                "pathFull='" + pathFull + '\'' +
                ", pathLite='" + pathLite + '\'' +
                ", directory=" + directory +
                ", sha='" + sha + '\'' +
                '}';
    }
}
