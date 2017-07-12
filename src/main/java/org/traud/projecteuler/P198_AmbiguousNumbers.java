package org.traud.projecteuler;

import org.apache.lucene.util.MathUtil;
import org.traud.util.misc.Timer;

/**
 * Created by traud on 4/8/2017.
 */
public class P198_AmbiguousNumbers {

    public static boolean isAmbiguous(long p, long q) {
        long s = MathUtil.gcd(p, q);
        if (s != 1) {
            System.out.printf("%d/%d -> ", p, q);
            p /= s;
            q /= s;

            System.out.printf("%d/%d\n", p, q);
        }

        // p/q is ambigious if it is of the form
        // 1/2 * (a/b + c/d)
        // or
        //   a*d + c*b
        //   ---------
        //     2*b*d
        return true;
    }

    public static void main(String[] args) {
        long[][] test = new long[][] {
                {9, 40},
                {6+7, 2*6*7},
                {6+8, 2*6*8},
                {7+9, 2*7*9},
                {1, 2},
        };
        for (long[] t : test) {
            long p = t[0];
            long q = t[1];
            System.out.printf("isAmbiguous(%d/%d): %s\n", p,q,isAmbiguous(p,q));
        }

        Timer t = new Timer();
        for (long q = 101; q < 1e8; ++q) {
            long maxP = q / 100;
//            for (long p = 1; p < maxP; ++p) {
//
//            }
        }
        System.out.printf("enumerated fractions in %1.2fs\n", t.getElapsedSeconds());
    }
}
