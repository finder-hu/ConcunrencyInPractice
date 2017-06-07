package com.finder.counting;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.*;

/**
 * Created by Finder Hu on 2017/6/7.
 * 该测试使用的方式与UnsafeCounting一样，可知SafeCounting.increase方法是线程安全的。
 */
public class SafeCountingTest {
    private final int nThreads = 10000;
    private CountDownLatch startGate = new CountDownLatch(1);
    private CountDownLatch endGate = new CountDownLatch(nThreads);
    private Set<Long> results = new HashSet<Long>();

    @Test
    public void increase() throws Exception {
        for (int i = 1; i <= nThreads; i++) {
            Thread t = new Thread() {
                @Override
                public void run() {
                    try {
                        startGate.await();
                        results.add(SafeCounting.increase());
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