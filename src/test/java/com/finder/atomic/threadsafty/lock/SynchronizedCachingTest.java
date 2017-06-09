package com.finder.atomic.threadsafty.lock;

import org.junit.Test;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.*;

/**
 * Created by Finder Hu on 2017/6/9.
 */
public class SynchronizedCachingTest {
    private final Integer nThreads = 1000;
    private ConcurrentLinkedQueue<List<BigInteger>> results = new ConcurrentLinkedQueue<>();

    @Test
    public void service() throws Exception {
        CountDownLatch startGate = new CountDownLatch(1);
        CountDownLatch endGate = new CountDownLatch(nThreads);
        SynchronizedCaching synchronizedCaching = new SynchronizedCaching();
        Runnable runnable1 = new Runnable() {
            @Override
            public void run() {
                try {
                    startGate.await();
                    synchronizedCaching.service(new BigInteger("2"), results);
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
                    synchronizedCaching.service(new BigInteger("4"), results);
                } catch (Exception e) {

                } finally {
                    endGate.countDown();
                }
            }
        };

        for (int i = 1; i <= nThreads; i++) {
            Runnable runnable = i < nThreads / 2 ? runnable1 : runnable2;
            new Thread(runnable).start();
        }
        startGate.countDown();
        endGate.await();
        for (List<BigInteger> list : results) {
            BigInteger number = list.get(0);
            BigInteger result = list.get(1);
            assertEquals(number, result.subtract(new BigInteger("-1")));
        }
    }

}