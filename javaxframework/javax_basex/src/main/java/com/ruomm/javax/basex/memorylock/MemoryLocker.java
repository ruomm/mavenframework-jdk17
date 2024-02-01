package com.ruomm.javax.basex.memorylock;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright www.ruomm.com
 * @create 2021/1/19 10:49
 */
public class MemoryLocker {
    protected Map<String, LockerItem> appLockMap = new ConcurrentHashMap<String, LockerItem>();

    /**
     * 获取内存锁
     *
     * @param lockName  内存锁名称
     * @param validTime 内存所有效时间
     * @return 获取内存锁结果
     */
    public synchronized boolean tryLocker(String lockName, long validTime) {
        if (null == lockName || lockName.length() <= 0) {
            throw new RuntimeException("内存锁名称不能为空");
        }
        //首先清理失效的内存锁
        freeAllLockerItems();
        LockerItem lockerExit = appLockMap.get(lockName);
        if (null == lockerExit) {
            // 若是没有锁加锁放入Map里面返回可以操作
            lockerExit = new LockerItem();
            lockerExit.setLock(true);
            lockerExit.setStartTime(System.currentTimeMillis());
            lockerExit.setValidTime(validTime);
            appLockMap.put(lockName, lockerExit);
            return true;
        } else if (lockerExit.isLock()) {
            // 如是有锁返回锁定，不能操作
            lockerExit.setValidTime(validTime);
            return false;
        } else {
            // 若是没有锁加锁返回可以操作
            lockerExit.setLock(true);
            lockerExit.setStartTime(System.currentTimeMillis());
            lockerExit.setValidTime(validTime);
            return true;
        }
    }

    /**
     * 释放所有的内存锁
     *
     * @return 释放所有的内存锁结果
     */
    public synchronized boolean freeAllLockers(boolean isForce) {
        if (isForce) {
            appLockMap.clear();
            return true;
        } else {
            return freeAllLockerItems();
        }
    }

    /**
     * 释放所有的内存锁
     *
     * @return 释放所有的内存锁结果
     */
    private boolean freeAllLockerItems() {
        //首先清理失效的内存锁
        List<String> removeKey = new ArrayList<>();
        Set<String> lockerKeySet = appLockMap.keySet();
        for (String key : lockerKeySet) {
            LockerItem lockerItem = appLockMap.get(key);
            if (null == lockerItem) {
                removeKey.add(key);
            } else if (lockerItem.getValidTime() <= 0) {
                //永远不失效的不清理
                continue;
            } else {
                long timeSkip = Math.abs(System.currentTimeMillis() - lockerItem.getStartTime());
                if (timeSkip > lockerItem.getValidTime()) {
                    lockerItem.setLock(false);
                    removeKey.add(key);
                }

            }
        }
        for (String key : removeKey) {
            appLockMap.remove(key);
        }
        return true;
    }

    /**
     * 释放内存锁
     *
     * @param lockName 内存锁名称
     * @return 释放内存锁结果
     */
    public synchronized boolean freeLocker(String lockName) {
        if (null == lockName || lockName.length() <= 0) {
            throw new RuntimeException("内存锁名称不能为空");
        }
        LockerItem lockerExit = appLockMap.get(lockName);
        if (null == lockerExit) {
            return true;
        } else {
            lockerExit.setLock(false);
            appLockMap.remove(lockName);
            return true;
        }
    }
}
