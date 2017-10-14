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
public class P132_LargeRepUnitFactors {

    public static boolean primeDividesRepUnit(long k, long p) {
        if (MathUtil.gcd(k, p-1) == 1)
            return false;

        return 1 == BigInteger.TEN.modPow(BigInteger.valueOf(k), BigInteger.valueOf(p)).longValue();
    }

    public static List<Long> repUnitFactors(long n) {
        // R(n) = (10^n - 1) / 9
        // p | R(n) <=>  (10^n - 1) / 9 = p * s
        //          <=>  (10^n - 1) = p *9s

        List<Long> r = new ArrayList<>();
        if (primeDividesRepUnit(n, 2))
            r.add(2L);


        if (n % 3 == 0) {
            r.add(3L);
            System.out.printf("   - %d: %d\n", r.size(), 3);

        }

        if (n % 2 == 0) {
            r.add(11L);
            System.out.printf("   - %d: %d\n", r.size(), 11);
        }

        long n2 = n/2;
        for (long p = 5; Math.log10(p) <= n2; p += 2) {
            if (Primes.isPrime(p) && primeDividesRepUnit(n, p))
                if (!r.contains(p)) {
                    r.add(p);
                    System.out.printf("   - %d: %d\n", r.size(), p);
                }

            if (r.size() == 40)
                break;
        }

//        BigInteger R = repunit(n);
//        // https://math.stackexchange.com/questions/620226/repunit-prime-factors/620237
//
//        for (int i = 0; i < r.size(); ++i) {
//            long p = r.get(i);
//            long q = R.divide(BigInteger.valueOf(p)).longValue();
//            if (!r.contains(q) && Primes.isPrime(q))
//                r.add(q);
//        }

        return r;
    }

    public static List<Long> repUnitFactorsDumb(long n) {
        // R(n) = (10^n - 1) / 9
        // p | R(n) <=>  (10^n - 1) / 9 = p * s
        //          <=>  (10^n - 1) = p * 9s
        BigInteger R = repunit(n);
        List<Long> r = new ArrayList<>();

        long MAX = R.shiftRight(R.bitCount()/2).longValue();
        for (long p = 3; p <= MAX; p += 2) {
            if (Primes.isPrime(p) && R.mod(BigInteger.valueOf(p)).equals(BigInteger.ZERO))
                r.add(p);
        }
        return r;
    }

    private static BigInteger repunit(long n) {
        BigInteger R = BigInteger.ZERO;
        while (n > 0) {
            R = R.multiply(BigInteger.TEN).add(BigInteger.ONE);
            n--;
        }
        return R;
    }

    public static void main(String... args) throws IOException {
        for (int n = 3; n <= 15; ++n) {
            System.out.printf("R(%d) =\n", n);
            System.out.printf("  %s\n", repUnitFactors(n));
//            System.out.printf("  %s\n", repUnitFactorsDumb(n));
        }

        long n = (long)Math.pow(10, 9);
        System.out.printf("R(%d) =\n", n);
        List<Long> factors = repUnitFactors(n);
        System.out.printf("  %s\n", factors);
        long sum = factors.stream().mapToLong(Long::longValue).sum();
        System.out.printf("sum of rep unit factos: %d\n", sum);
    }
}
