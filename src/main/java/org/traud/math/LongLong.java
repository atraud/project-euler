package org.traud.math;

import java.util.Arrays;

/**
 * Created by traud on 4/11/2017.
 *
 * represents a 128 bit integer (=8x32 bit int)
 */
public class LongLong {
    public int[] a;

    public LongLong() {
        a = new int[8];
    }

    public LongLong(long n) {
        a = new int[8];
        a[0] = (int)(n & 0xffff_ffffL);
        a[1] = (int)(n >> 32);
        if (n < 0)
            Arrays.fill(a, 2, a.length, -1);
    }

    public void add(LongLong b) {
        long c = 0;
        int i = 0;
        while (i < a.length) {
            long sum = (a[i] & 0xffff_ffffL) + (b.a[i] & 0xffff_ffffL) + c;
            if (sum >= 0x1_0000_0000L) {
                a[i] = (int)(sum & 0xffff_ffff);
                c = (sum >>> 32) & 0xffff_ffff;
            }
            else {
                a[i] = (int)sum;
                c = 0;
            }
            ++i;
        }
    }
    public void increment() {
        long c = 1;
        int i = 0;
        while (i < a.length && c!=0) {
            long inc = a[i] + c;
            if (inc == 0) {
                a[i] = 0;
            }
            else {
                a[i] = (int)inc;
                c = 0;
            }
            ++i;
        }
    }
    public String toHexString() {
        StringBuilder sb = new StringBuilder(128/4+7);
        for (int i = a.length-1; i>= 0; --i) {
            sb.append(String.format("%08x", a[i]));
//            if (i > 0)
//                sb.append('');
        }
        return sb.toString();
    }

    public String toString() {
        return toHexString();
    }

    public static void main(String... args) {
        LongLong l = new LongLong(0x7fff_ffff_ffff_ffffL);
        LongLong b = new LongLong(0x7fff_ffff_ffff_ffffL);
        for (int i = 0; i < 10; ++i) {
            System.out.printf("%d: %s\n", i, l);
//            l.increment();
            l.add(b);
        }
    }
}
