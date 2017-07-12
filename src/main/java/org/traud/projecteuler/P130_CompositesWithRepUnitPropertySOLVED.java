package org.traud.projecteuler;

import org.apache.lucene.util.MathUtil;
import org.traud.math.factor.Primes;

import java.io.IOException;


/**
 *
 *

 A number consisting entirely of ones is called a repunit. We shall define R(k) to be a repunit of length k; for example, R(6) = 111111.

 Given that n is a positive integer and GCD(n, 10) = 1, it can be shown that there always exists a value, k, for which R(k) is divisible by n, and let A(n) be the least such value of k; for example, A(7) = 6 and A(41) = 5.

 You are given that for all primes, p > 5, that p − 1 is divisible by A(p). For example, when p = 41, A(41) = 5, and 40 is divisible by 5.

 However, there are rare composite values for which this is also true; the first five examples being 91, 259, 451, 481, and 703.

 Find the sum of the first twenty-five composite values of n for which
 GCD(n, 10) = 1 and n − 1 is divisible by A(n).

 */
public class P130_CompositesWithRepUnitPropertySOLVED {


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

//        long[] testValues = new long[] {7,41,91,259,451};
//        for (long n : testValues) {
//            System.out.printf("     A(%d)=%d\n", n, A(n));
//        }

        for (long n = 2; cnt < 25; n += 1) {
            if (MathUtil.gcd(n, 10) != 1)
                continue;

            // only consider composite numbers!
            if (Primes.isPrime(n)) {
                //System.out.printf("     %d is prime A(%d)=%d. skipping\n", n, n, A(n));
                continue;
            }

            long a = A(n);
            long remainder = (n - 1) % a;
            if (remainder == 0) {
                cnt++;
                System.out.printf("%d:  n=%3d\tA(n)=%3d,  (n-1)%%A(n)=%3d\n", cnt, n, a, remainder);
                sum += n;

            }
        }
        System.out.printf("total sum is %d\n", sum);
    }
}
