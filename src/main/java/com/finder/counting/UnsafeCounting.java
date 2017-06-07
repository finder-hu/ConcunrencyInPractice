package com.finder.counting;

/**
 * Created by Finder Hu on 2017/6/7.
 */
public class UnsafeCounting {
    private static long count = 0;

    public static long increase() {
        ++count;
        return count;
    }
}
