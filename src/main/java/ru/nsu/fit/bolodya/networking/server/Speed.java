package ru.nsu.fit.bolodya.networking.server;

import java.util.*;

class Speed {

    private static final int INTERVAL_IN_MILLIS = 5000;

    private Map<Long, Long> last = new HashMap<>();
    private long startTime = -1;
    private long total;

    synchronized void put(int amount) {
        if (startTime == -1)
            startTime = System.currentTimeMillis();
        total += amount;

        long cur = System.currentTimeMillis();
        if (last.containsKey(cur))
            amount += last.get(cur);
        last.put(cur, (long) amount);
    }

    private void clear() {
        long cur = System.currentTimeMillis();
        Iterator<Map.Entry<Long, Long>> iterator = last.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Long, Long> entry = iterator.next();
            if (cur - entry.getKey() > INTERVAL_IN_MILLIS)
                iterator.remove();
        }
    }

    double getTotalSpeedInBytes() {
        long cur = System.currentTimeMillis();

        return (total * 1000.) / (cur - startTime);
    }

    synchronized double getLocalSpeedInBytes() {
        clear();

        long sum = 0;
        for (Map.Entry<Long, Long> entry : last.entrySet())
            sum += entry.getValue();

        return (sum * 1000.) / INTERVAL_IN_MILLIS;
    }
}
