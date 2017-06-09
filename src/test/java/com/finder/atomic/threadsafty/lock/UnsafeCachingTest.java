package com.finder.atomic.threadsafty.lock;

import org.junit.Test;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.*;

/**
 * Created by Finder Hu on 2017/6/9.
 */
public class UnsafeCachingTest {
    private final ConcurrentLinkedQueue<List<Integer>> results = new ConcurrentLinkedQueue<>();
    // FIXME: 2017/6/9 为什么当线程个数超过100是就不会发生错误。并且使用maven.test运行测试用例是该用例会失败。
    private int threads = 100;

    @Test
    public void service() throws Exception {
        final UnsafeCaching unsafeCaching = new UnsafeCaching();
        final CountDownLatch startGate = new CountDownLatch(1);
        final CountDownLatch endGate = new CountDownLatch(threads);
        Runnable runnable1 = new Runnable() {
            @Override
            public void run() {
                try {
                    startGate.await();
                    unsafeCaching.service(2, results);
                } catch (Exception e) {
                } finally {
                    endGate.countDown();
                }
            }
        };
        Runnable runnable2 = new Runnable() {
            @Override
            public void run() {
                try {
                    startGate.await();
                    unsafeCaching.service(4, results);
                } catch (Exception e) {

                } finally {
                    endGate.countDown();
                }
            }
        };
        for (int i = 1; i <= threads; i++) {
            Runnable runnable = i < threads / 2 ? runnable1 : runnable2;
            Thread t = new Thread(runnable);
            t.start();
        }
        startGate.countDown();
        endGate.await();
        boolean flag = false;
        for (List<Integer> list : results) {
            Integer number = list.get(0);
            Integer result = list.get(1);
            if ((number == result - 1) || (number == result + 3)) {
//                System.out.println(number + " " + result);
                flag = true;
            }
        }
//        System.out.println(results.size());
        assertTrue(flag);
    }

}