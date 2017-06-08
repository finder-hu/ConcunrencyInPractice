package com.finder.counting;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.*;

/**
 * Created by Finder Hu on 2017/6/7.
 * 验证“++”操作不是线程安全的，但多个线程同时执行该操作是会出现得到相同值的情况。
 * 本测试中通过比较结果集合中的值的个数来判读是否出现重复值。
 */
public class UnsafeCountingTest {

    private final CountDownLatch startGate = new CountDownLatch(1);
    private static final int nThreads = 2000;   //当该值取2000时几乎每次都会出现竞争情况。
    private final CountDownLatch endGate = new CountDownLatch(nThreads);
    private ConcurrentSkipListSet<Long> results = new ConcurrentSkipListSet<>();

    @Test
    public void increase() throws Exception {
        for (int i = 1; i <= nThreads; i++) {
            Thread t = new Thread() {
                @Override
                public void run() {
                    try {
                        startGate.await();
                        long count = UnsafeCounting.increase();
                        results.add(count);
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
        assertNotEquals(nThreads, results.size());
    }

}