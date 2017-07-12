package org.traud.projecteuler;

import org.apache.lucene.util.MathUtil;

import java.io.IOException;

/**
 *
 *

 A number consisting entirely of ones is called a repunit. We shall define R(k) to be a repunit of length k;
 for example, R(6) = 111111.

 Given that n is a positive integer and GCD(n, 10) = 1, it can be shown that there always exists a value,
 k, for which R(k) is divisible by n, and let A(n) be the least such value of k; for example, A(7) = 6 and A(41) = 5.

 The least value of n for which A(n) first exceeds ten is 17.

 Find the least value of n for which A(n) first exceeds one-million.

 */
public class P129_RepUnitDivisibility {


    public static long A(long n) {
        assert MathUtil.gcd(n, 10) == 1;

        // find R(k), such that R(k) is divisible by n
        // R(k) is divisble by n iff
        // there exists s with
        //    s * n = R(k)
        // or
        //    R(k) = 0 (mod n)

        long rk = 1;
        long k = 1;
        while (rk % n != 0) {
            rk = rk * 10 + 1;
            rk %= n;
            k++;
        }

        return k;
    }

    public static void main(String... args) throws IOException {
        int cnt = 0;
        long sum = 0;

        long max = 1_000_000;
        for (long n = 1_000_000; ; n += 1) {
            if (MathUtil.gcd(n, 10) != 1)
                continue;

            long a = A(n);
            if (a > max) {
                System.out.printf("n=%8d: A(n)=%8d > %8d\n", n, a, max);
                sum += n;
                if (max == 1_000_000L)
                    break;
                max *= 10;
            }
        }
    }
}
