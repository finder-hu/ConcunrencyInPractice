package com.finder.threadsafty;

import org.junit.Test;
import org.omg.IOP.ENCODING_CDR_ENCAPS;

import java.util.Date;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.*;

/**
 * Created by Finder Hu on 2017/6/7.
 * 验证延迟初始化存在的竞争条件，具体方式为：多个线程同时调用
 * LazyInitRace.getInstance方法，打印得到的date的值，结果发现
 * 所有的打印结果中出现了两个值，说明存在竞争条件。
 */
public class LazyInitRaceTest {

    private int nThreads = 20;
    private final CountDownLatch startGate = new CountDownLatch(1);
    private final CountDownLatch endGate = new CountDownLatch(nThreads);

    @Test
    public void getInstance() throws Exception {
        final Runnable task = new Runnable() {
            public void run() {
                Date date = LazyInitRace.getInstance();
                System.out.println(date.getTime());
            }
        };
        for (int i = 1; i <= nThreads; i++) {
            Thread workder = new Thread() {
                @Override
                public void run() {
                    try {
                        startGate.await();
                        try {
                            task.run();
                        } finally {
                            endGate.countDown();
                        }
                    } catch (InterruptedException ignored) {

                    }
                }
            };
            workder.start();
        }

        startGate.countDown();
        endGate.await();
    }

}