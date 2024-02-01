package com.ruomm.javax.basex;

import com.ruomm.javax.basex.memorylock.MemoryLocker;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright www.ruomm.com
 * @create 2021/1/19 11:28
 */
public class MemoryLockerTest {

    public static void main(String[] args) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                MemoryLocker memoryLocker = new MemoryLocker();
                System.out.println("tryLocker" + memoryLocker.tryLocker("你好", 3000));
                boolean isOk = false;
                while (true) {
                    try {
                        sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    isOk = memoryLocker.tryLocker("你好", 3000);
                    System.out.println("tryLocker" + isOk);
                    if (isOk) {
                        break;
                    }
                }

                System.out.println("freeLocker" + memoryLocker.freeLocker("你好"));
                System.out.println("tryLocker" + memoryLocker.tryLocker("你好", 1000000));
                System.out.println("tryLocker" + memoryLocker.tryLocker("你好2", 1000000));
            }
        }.start();

    }
}
