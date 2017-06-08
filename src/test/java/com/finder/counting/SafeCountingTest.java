package com.finder.counting;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.*;

/**
 * Created by Finder Hu on 2017/6/7.
 * 该测试使用的方式与UnsafeCounting一样，可知SafeCounting.increase方法是线程安全的。
 */
public class SafeCountingTest {
    private final int nThreads = 3000;
    private CountDownLatch startGate = new CountDownLatch(1);
    private CountDownLatch endGate = new CountDownLatch(nThreads);
    private final ConcurrentSkipListSet<Long> results = new ConcurrentSkipListSet<Long>();

    @Test
    public void increase() throws Exception {
        final Runnable task = new Runnable() {
            public void run() {
                Long increase = SafeCounting.increase();
                results.add(increase);
            }
        };
        for (int i = 1; i <= nThreads; i++) {
            Thread t = new Thread() {
                @Override
                public void run() {
                    try {
                        startGate.await();
                        task.run();
                    } catch (Exception e) {
                    } finally {
                        endGate.countDown();
                    }
                }
            };
            t.start();
        }

        startGate.countDown();
        endGate.await();
        assertEquals(nThreads, results.size());
    }


}