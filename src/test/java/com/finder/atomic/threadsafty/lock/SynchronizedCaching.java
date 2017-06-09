package com.finder.atomic.threadsafty.lock;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Finder Hu on 2017/6/9.
 */
public class SynchronizedCaching {
    private AtomicReference<BigInteger> lastNumber = new AtomicReference<>();
    private AtomicReference<BigInteger> lastResult = new AtomicReference<>();

    public void service(BigInteger number, ConcurrentLinkedQueue<List<BigInteger>> results) {
        if (number.equals(lastNumber.get())) {
            synchronized (this) {
                List<BigInteger> list = new ArrayList<BigInteger>() {{
                    this.add(lastNumber.get());
                    this.add(lastResult.get());
                }};
                results.add(list);
            }
        } else {
            BigInteger result = calculate(number);
            List<BigInteger> list = new ArrayList<BigInteger>() {{
                this.add(number);
                this.add(result);
            }};
            results.add(list);
            synchronized (this) {
                lastNumber.set(number);
                lastResult.set(result);
            }
        }
    }

    private BigInteger calculate(BigInteger number) {
        return number.subtract(new BigInteger("1"));
    }
}
