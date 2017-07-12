package org.traud.math.factor;

import java.math.BigInteger;

/**
 * Created by traud on 4/8/2017.
 */
public class Primes {

    public static boolean USE_JAVA_BIGINTEGER_PRIME_CHECK = false;

    public static boolean isPrimeSlow(long n) {
        // TODO: replace with something faster for type long,
        // e.g. Rabin-Miller primality test
        BigInteger bigInteger = BigInteger.valueOf(n);
        return bigInteger.isProbablePrime(100);
    }


    // see https://de.wikipedia.org/wiki/Miller-Rabin-Test
    private static long[][] as = {
        {1_373_653, 2, 3},
        {9_080_191, 31, 73},
        {4_759_123_141L, 2, 7, 61},
        {2_152_302_898_747L, 2, 3, 5, 7, 11},
        {3_474_749_660_383L, 2, 3, 5, 7, 11, 13},
        {341_550_071_728_321L, 2, 3, 5, 7, 11, 13, 17},
        {3_825_123_056_546_413_051L, 2, 3, 5, 7, 11, 13, 17, 19, 23},
        {Long.MAX_VALUE, 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37},
        //{318_665_857_834_031_151_167_461L, 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37},
    };

    private static long[] getWhitnessAs(long n) {
        for (long[] a : as) {
            if (n < a[0])
                return a;
        }
        return null;
    }

    private static long modpow(long x, long c, long m) {
        if (x >= Integer.MAX_VALUE || c >= Integer.MAX_VALUE || m >= Integer.MAX_VALUE)
            return BigInteger.valueOf(x).modPow(BigInteger.valueOf(c), BigInteger.valueOf(m)).longValue();

        long result = 1;
        long aktpot = x;
        while (c > 0) {
            if (c % 2 == 1) {
                result = (result * aktpot) % m;
                assert result > 0 : "x="+x+",c="+c+",m="+m;
            }
            aktpot = (aktpot * aktpot) % m;
            assert result > 0;
            c /= 2;
        }
        return result;
    }
    static final BigInteger TWO = BigInteger.valueOf(2);

    // see http://codereview.stackexchange.com/questions/24625/miller-rabin-prime-test-speed-is-the-main-goal
    private static boolean millerRabin(long n) {
        long s = 0;
        long d = n - 1;
        while (d % 2 == 0) {
            s++;
            d /= 2;
        }
        BigInteger BiN = null;

        long[] as = getWhitnessAs(n);
        outer:
        for (int i = 1; i < as.length; ++i) {
            long a = as[i];
            long x = modpow(a, d, n);
            if (x != 1 && x != n - 1) {
                for (long r = 1; r < s; r++) {
//                    long y = (x*x)%n;
//                    if ((y ^ x) < 0) {
//                        if (BiN == null)
//                            BiN = BigInteger.valueOf(n);
//                        y = BigInteger.valueOf(x).modPow(TWO, BiN).longValue();
//                    }
//                    x = y;
                    if (x < Integer.MAX_VALUE) {
                        x = (x * x) % n;
                        assert x > 0 : "n="+n+",a="+a;
                    }
                    else {
                        if (BiN == null)
                            BiN = BigInteger.valueOf(n);
                        x = BigInteger.valueOf(x).modPow(TWO, BiN).longValue();
                    }

                    if (x == 1) {
                        return false;
                    }
                    if (x == n - 1) {
                        continue outer;
                    }
                }
                return false;
            }
        }
        return true;
    }


    public static boolean isPrime(long num) {
        if (num <= 1) {
            return false;
        } else if (num <= 3) {
            return true;
        } else if (num % 2 == 0) {
            return false;
        } else if (num % 3 == 0) {
            return false;
        } else {
            if (USE_JAVA_BIGINTEGER_PRIME_CHECK)
                return isPrimeSlow(num);
            return millerRabin(num);
        }
    }


    public static void main(String[] args) {
        long[] test = new long[] {
                1537228672809129301L,
                11*19,
                127,
                2305843009213693951L,
                1030686124187L,
                12586817029L,
                9902437L,
                29775769179641L,
                641387128649L,
                133978850655919L,
                3074457345618258599L,
                79511827903920481L
        };

        for (long n : test) {
            System.out.printf("%,d: %s\n", n, millerRabin(n));
        }
    }
}
