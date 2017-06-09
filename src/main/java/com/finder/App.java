package com.finder;


import com.finder.atomic.threadsafty.LazyInitRace;

import java.util.Date;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        Thread t = new Thread() {
            @Override
            public void run() {
                System.out.println("thread");
            }
        };
        t.start();
//        Integer a = 0;
//        Date date = LazyInitRace.getInstance();
//        System.out.println(date.toString());
    }
}
