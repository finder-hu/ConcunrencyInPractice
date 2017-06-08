package com.finder.counting;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Finder Hu on 2017/6/7.
 */
public class SafeCounting {
    private static AtomicLong count = new AtomicLong(0);

    public static Long increase() {
        return count.incrementAndGet();
    }
}
