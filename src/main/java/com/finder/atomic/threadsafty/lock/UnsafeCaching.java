package com.finder.atomic.threadsafty.lock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Finder Hu on 2017/6/9.
 */
public class UnsafeCaching {
    private final AtomicReference<Integer> lastNumber = new AtomicReference<>();
    private final AtomicReference<Integer> result = new AtomicReference<>();

    public UnsafeCaching() {
        lastNumber.set(new Integer(0));
        result.set(new Integer(0));
    }

    public void service(Integer number, ConcurrentLinkedQueue<List<Integer>> results) {
        if (lastNumber.get().equals(number)) {
            List list = new ArrayList() {{
                this.add(lastNumber.get());
                this.add(result.get());
            }};
            results.add(list);
        } else {
            Integer tempresult = calculate(number);
            List list = new ArrayList() {{
                this.add(number);
                this.add(tempresult);
            }};
            results.add(list);
            lastNumber.set(number);
            result.set(tempresult);
        }
    }

    private Integer calculate(Integer number) {
        return number - 1;
    }
}
