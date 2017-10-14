package org.traud.projecteuler;

import org.apache.lucene.util.MathUtil;
import org.traud.math.factor.Primes;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * A number consisting entirely of ones is called a repunit. We shall define R(k) to be a repunit of length k.
 *
 * For example, R(10) = 1111111111 = 11×41×271×9091, and the sum of these prime factors is 9414.
 *
 * Find the sum of the first forty prime factors of R(109).
 */
public class P133_RepUnitNonFactors {
//        // https://math.stackexchange.com/questions/620226/repunit-prime-factors/620237

    public static boolean primeDividesRepUnit(long k, long p) {
        if (p == 2)
            return false;

        if (p == 3)
            return k % 3 == 0;


        if (MathUtil.gcd(k, p-1) == 1)
            return false;

//        System.out.printf("  primeDividesRepUnit(%d, %d)\n", k, p);
        long mp = BigInteger.TEN.modPow(BigInteger.valueOf(k), BigInteger.valueOf(p)).longValue();
        return 1 == mp;
    }


    public static void main(String... args) throws IOException {
        long sum = 0;
        for (int n = 2; n < 100; ++n) {
            if (!Primes.isPrime(n))
                continue;

            int p = n;
            BigInteger p_1 = BigInteger.valueOf(p).subtract(BigInteger.ONE);
            boolean found = false;
            for (int k = 2; k < 12; ++k) {
                BigInteger pow10 = BigInteger.TEN.pow(k);
//                BigInteger gcd = pow10.gcd(p_1);
//                if (gcd.longValue() != 1) {
//                    found = true;
//                    System.out.printf(" - %d: %d divides R(10^%d) - gcd(%s, %s)=%s\n", p, p, k,
//                            pow10, p_1, gcd);
//                    break;
//                }
                if (primeDividesRepUnit(pow10.longValue(), p)) {
                    found = true;
//                    System.out.printf(" - %d: %d divides R(10^%d)\n", p, p, k);
                    break;
                }
            }
            if (found)
                continue;

            System.out.printf("%d\n", p);

            sum += p;
        }

        System.out.printf("sum of primes less than 100 that cannot be factors of rep units: %d\n", sum);
    }
}
