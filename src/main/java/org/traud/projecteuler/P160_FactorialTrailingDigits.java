package org.traud.projecteuler;

import java.math.BigInteger;
import java.util.Date;
import java.util.Random;

/**
 * Created by traud on 7/4/2017.
 */
public class P160_FactorialTrailingDigits {


    private static long factorialTrailingDigits(long t) {
        BigInteger f = BigInteger.ONE;
        BigInteger MILLION= BigInteger.valueOf(1_000_000);
        BigInteger HUNDRED_K = BigInteger.valueOf(100_000);

        for (long k = 1; k <= t; ++k) {
            f = f.multiply(BigInteger.valueOf(k));
            while (f.mod(BigInteger.TEN).equals(BigInteger.ZERO))
                f = f.divide(BigInteger.TEN);

            if (f.compareTo(MILLION) >= 0)
                f = f.mod(MILLION);
        }
        return f.mod(HUNDRED_K).longValue();
    }

    private static long factorialTrailingDigitsPrimitive(long t) {
        long f = 1;
        long start = System.currentTimeMillis();
        long mask = 16*16777216L - 1;
        for (long k = 1; k <= t; ++k) {
            f *= k;
            while ((f % 10) == 0)
                f /= 10;

            if (f >= 10_000_000_000L)
                f %= 10_000_000_000L;

            if ((k & mask) == 0) {
                double percent = 100.0 * k / t;
                long elapsed = System.currentTimeMillis() - start;
                long eta = start + (long)(elapsed * 100 / percent);
                System.out.printf("%s: factorialTrailingDigitsPrimitive(%,d): k=%,d (%1.1f%%), eta: %s\n", new Date(), t, k, percent,
                        new Date(eta));
            }
        }
        return f % 100_000;
    }

    private static BigInteger factorial(long t) {
        BigInteger f = BigInteger.ONE;
        for (long k = 1; k <= t; ++k) {
            f = f.multiply(BigInteger.valueOf(k));
        }
        return f;
    }

    private static void test(long t) {
        long d1 = factorialTrailingDigits(t);
        long d2 = factorialTrailingDigitsPrimitive(t);
        BigInteger factorial = factorial(t);
        String fs = factorial.toString();

        d1 = d2;
        int p = fs.length()-1;
        while (p >= 0 && fs.charAt(p) == '0') {
            p--;
        }
        String suffix="";
        if (p >= 0) {
            suffix = fs.substring(Math.max(0, p-4), p+1);
        }

        System.out.printf("%s: %d! = %s (%d / %d) - %s\n", new Date(),
                t, fs,
                d1, d2, suffix);
        if (d1 != d2)
            throw new IllegalStateException(d1 + " != " + d2);
        if (Integer.parseInt(suffix) != d1)
            throw new IllegalStateException(t + "!: " + suffix + " != " + d1 + " (d1)");
        if (Integer.parseInt(suffix) != d2)
            throw new IllegalStateException(t + "!: " + suffix + " != " + d2 + " (d2)");
    }


    //
//    9: 36288
//            10: 36288
//            20: 17664
//            11: 99168
//            12: 90016
//            14: 82912
//            1000000: 4544
//            1000001: 4544
//            1000002: 9088
//            1000003: 27264
//            1000000000000: 30976

    public static void main(String... args) {
        long[] test;

        for (long t = 1; t <= 500; ++t) {
            test(t);
        }
        Random rnd = new Random();
//         test with a few larger numbers
        for (int i = 1; i <= 20; ++i) {
            test(rnd.nextInt(100_000));
        }

        System.exit(0);
        long val = 1_000_000_000_000L;
        System.out.printf("prime factors of %,d!: %s", val,
                P231_PrimeFactBinomialCoeff.primeExponentString(
                        P231_PrimeFactBinomialCoeff.getPrimeFactorsOfFactorial(val)
                )
        );
        System.exit(0);
        test = new long[] {
                9, 10, 20, 11, 12, 14, 1_000_000, 1_000_001, 1_000_002, 1_000_003,
                1_000_000_000_000L
        };
        long m = Long.MAX_VALUE;
        for (long t : test) {
            System.out.printf("%s: %,d: %d\n", new Date(), t, factorialTrailingDigitsPrimitive(t));
        }
//        prime factors of 1,000,000,000,000!: 2^999999999987·3^499999999988·5^249999999997·7^166666666660·11^99999999994·13^83333333328·17^62499999996·19^55555555551·23^45454545450·29^35714285710·31^33333333330... (1270607 factor(s))

//        Wed Jul 05 22:49:12 CEST 2017: 1,000,002: 25088
//        Wed Jul 05 22:49:12 CEST 2017: 1,000,003: 75264
//        Wed Jul 05 22:49:13 CEST 2017: factorialTrailingDigitsPrimitive(1,000,000,000,000): k=268,435,456 (0.0%), eta: Thu Jul 06 00:23:30 CEST 2017
//        Wed Jul 05 22:49:15 CEST 2017: factorialTrailingDigitsPrimitive(1,000,000,000,000): k=536,870,912 (0.1%), eta: Thu Jul 06 00:23:25 CEST 2017
//        Wed Jul 05 22:49:16 CEST 2017: factorialTrailingDigitsPrimitive(1,000,000,000,000): k=805,306,368 (0.1%), eta: Thu Jul 06 00:23:21 CEST 2017
//
//        Thu Jul 06 00:24:05 CEST 2017: factorialTrailingDigitsPrimitive(1,000,000,000,000): k=586,799,906,816 (58.7%), eta: Thu Jul 06 01:30:53 CEST 2017
//        Thu Jul 06 00:24:07 CEST 2017: factorialTrailingDigitsPrimitive(1,000,000,000,000): k=587,068,342,272 (58.7%), eta: Thu Jul 06 01:30:53 CEST 2017
//        ...
//        Thu Jul 06 01:29:03 CEST 2017: factorialTrailingDigitsPrimitive(1,000,000,000,000): k=999,653,638,144 (100.0%), eta: Thu Jul 06 01:29:06 CEST 2017
//        Thu Jul 06 01:29:05 CEST 2017: factorialTrailingDigitsPrimitive(1,000,000,000,000): k=999,922,073,600 (100.0%), eta: Thu Jul 06 01:29:06 CEST 2017
//        Wed Jul 05 22:49:12 CEST 2017: 1,000,000,000,000: 58624

    }


}
