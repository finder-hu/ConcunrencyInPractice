package com.finder.atomic.threadsafty;

import java.util.Date;

/**
 * Created by Finder Hu on 2017/6/7.
 */
public class LazyInitRace {
    private static Date instance = null;

    static public Date getInstance() {
        if (instance == null) {
            instance = new Date();
        }
        return instance;
    }
}
