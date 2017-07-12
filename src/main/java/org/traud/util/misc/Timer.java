package org.traud.util.misc;

import java.util.Date;

/**
 * Created by traud on 4/8/2017.
 */
public class Timer {
    private static final long NANO_TO_MILLIS = 1_000_000;
    private long t0;
    private long t0Nanos;

    public Timer() {
        reset();
    }
    public long getElapsedMillis() {
        return (System.currentTimeMillis() - t0);
    }

    public long getElapsedNanos() {
        return System.nanoTime() - t0Nanos;
    }


    public void reset() {
        this.t0 = System.currentTimeMillis();
        this.t0Nanos = System.nanoTime();
    }

    public double getElapsedSeconds() {
        return getElapsedMillis()/1000.0;
    }

    public String eta(double perc) {
        long ms = getElapsedMillis();
        double f = 100 / perc;
        long eta = t0 + (long)(f * ms);
        return new Date(eta).toString();
    }
}
