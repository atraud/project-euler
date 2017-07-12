package org.traud.projecteuler;

import org.traud.math.*;
import org.traud.util.misc.Timer;

import java.io.IOException;
import java.util.Date;

import org.traud.math.factor.PrimeSieveIterator;

/**
 * Created by traud on 4/11/2017.
 *
 * Considering 4-digit primes containing repeated digits it is clear that they cannot all be the same: 1111 is divisible by 11, 2222 is divisible by 22, and so on. But there are nine 4-digit primes containing three ones:

 1117, 1151, 1171, 1181, 1511, 1811, 2111, 4111, 8111

 We shall say that M(n, d) represents the maximum number of repeated digits for an n-digit prime where d is the repeated digit, N(n, d) represents the number of such primes, and S(n, d) represents the sum of these primes.

 So M(4, 1) = 3 is the maximum number of repeated digits for a 4-digit prime where one is the repeated digit, there are N(4, 1) = 9 such primes, and the sum of these primes is S(4, 1) = 22275. It turns out that for d = 0, it is only possible to have M(4, 0) = 2 repeated digits, but there are N(4, 0) = 13 such cases.

 In the same way we obtain the following results for 4-digit primes.
 */
public class P111_PrimesWithRunsSOLVED {

    private static long hi(int n) {
        return (long)Math.pow(10, n);
    }

    private static long low(int n) {
        return (long)Math.pow(10, n-1);
    }

    private static final void toStringRev(long l, final StringBuilder sb) {
        sb.setLength(0);

        if (l == 0) {
            sb.append('0');
            return;
        }

        while (l > 0) {
            int digit = (int)(l % 10);
            sb.append((char)('0' + digit));
            l /= 10;
        }
    }

    private static final int count(final StringBuilder sb, final int digit) {
        final char c = (char)('0' + digit);
        final int n = sb.length();
        int cnt = 0;
        for (int i = 0; i < n; ++i) {
            if (sb.charAt(i) == c)
                cnt++;
        }
        return cnt;
    }


    public static long[][] M(final int n) throws IOException {
        final long hi = hi(n);
        final long lo = low(n);

        StringBuilder sb = new StringBuilder(20);

        PrimeSieveOffHeapCompressed ps = null;
        PrimeSieveIterator it = null;
        long[][] r = new long[10][3];
        // 10^10 = 10_000_000_000
        Timer t = new Timer();

        String offHeapFilename = "ro-composite-zip-10B.bin";
        System.out.printf("%s: loading previously storage prime sieve from %s...\n", new Date(), offHeapFilename);
        ps = new PrimeSieveOffHeapCompressed(offHeapFilename);
        it = ps.iterator();
        System.out.printf("%s: done. Covers numbers up to \n", new Date(), ps.maxValue());


        int j = 0;
        long p = lo+1;
        //for (long k = lo+1; k < hi; k += inc[j++ & 03]) {
        while (it.hasNext()) {

            if (j > 0 && (j & 0xffffff) == 0) {
                double perc = (p - lo) * 100D / (hi - lo);
                System.out.printf("%,d of %,d (%1.1f%%) - %1.2fs so far (ETA: %s).\n", p, hi, perc,
                        t.getElapsedSeconds(), t.eta(perc));
            }
            ++j;
            p = it.next();
            if (p < lo)
                continue;

            if (p >= hi)
                break;

            long k = p;

            toStringRev(p, sb);
            for (int d = 0; d < 10; ++d) {
                int cnt = count(sb, d);
                if (cnt > r[d][0]) {
                    r[d][1] = 0;
                    r[d][0] = cnt;
                    r[d][2] = 0;
                }

                // new long[] {maxCnt, primeCnt, sum};
                if (cnt == r[d][0]) {
                    r[d][2] += k;
                    r[d][1]++;
                }
            }
        }
        return r;
    }


    public static void main(String... args) throws IOException {
        int n = 10;
        long totalSum = 0;
        long[][] m = M(n);
        for (int d = 0; d < 10; ++d) {
            System.out.printf("n=%d, d=%d\tM=%d\tN=%2d\tS=%6d\n", n, d, m[d][0], m[d][1], m[d][2]);
            totalSum += m[d][2];
        }
        System.out.printf("total sum: %d\n", totalSum);

//        ...
//        9,566,541,749 of 10,000,000,000 (95.2%) - 98.94s so far (ETA: Wed Apr 12 14:53:06 CEST 2017).
//        9,952,394,911 of 10,000,000,000 (99.5%) - 103.02s so far (ETA: Wed Apr 12 14:53:06 CEST 2017).
//        n=10, d=0	M=8	N= 8	S=38000000042
//        n=10, d=1	M=9	N=11	S=12882626601
//        n=10, d=2	M=8	N=39	S=97447914665
//        n=10, d=3	M=9	N= 7	S=23234122821
//        n=10, d=4	M=9	N= 1	S=4444444447
//        n=10, d=5	M=9	N= 1	S=5555555557
//        n=10, d=6	M=9	N= 1	S=6666666661
//        n=10, d=7	M=9	N= 9	S=59950904793
//        n=10, d=8	M=8	N=32	S=285769942206
//        n=10, d=9	M=9	N= 8	S=78455389922
//        total sum: 612407567715

    }
}
