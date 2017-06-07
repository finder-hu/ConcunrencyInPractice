package com.finder;

import com.finder.threadsafty.LazyInitRace;

import java.util.Date;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        Integer a = 0;
        Date date = LazyInitRace.getInstance();
        System.out.println(date.toString());
    }
}
