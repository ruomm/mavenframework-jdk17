package com.ruomm.javax.basex.memorylock;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright www.ruomm.com
 * @create 2021/1/19 10:47
 */
public class LockerItem {
    private String name;
    private long validTime;
    private long startTime;
    private boolean lock;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getValidTime() {
        return validTime;
    }

    public void setValidTime(long validTime) {
        this.validTime = validTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public boolean isLock() {
        return lock;
    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }

    @Override
    public String toString() {
        return "MemoryLocker{" +
                "name='" + name + '\'' +
                ", validTime=" + validTime +
                ", startTime=" + startTime +
                ", lock=" + lock +
                '}';
    }
}
