package org.traud.projecteuler;

import java.math.BigInteger;

/**
 * Created by traud on 4/14/2017.
 */
public class P592_FactorialDigits2 {
    public static BigInteger fac(long n) {
        BigInteger f = BigInteger.ONE;
        for (long k = 1; k <= n; ++k) {
            f = f.multiply(BigInteger.valueOf(k));
        }
        return f;
    }

    public static BigInteger facMod(BigInteger n, long mod) {
        // assume mod to be 2^k - 1 for some k
        BigInteger f = BigInteger.ONE;
        BigInteger FIFTEEN = BigInteger.valueOf(0xf);
        BigInteger SIXTEEN = BigInteger.valueOf(16);
        BigInteger m = BigInteger.valueOf(mod);
        for (BigInteger k = BigInteger.ONE; k.compareTo(n) <= 0; k=k.add(BigInteger.ONE)) {
            f = f.multiply(k);
            while (f.and(FIFTEEN).equals(BigInteger.ZERO))
                f = f.shiftRight(4);

            f = f.and(m);
        }
//        while (f.mod(SIXTEEN).equals(BigInteger.ZERO))
//            f = f.divide(SIXTEEN);

        return f;
    }

    public static void main(String... args) {
//        BigInteger f20 = fac(20);
//        System.out.printf("%d! = %s (%s)", 20, f20, f20.toString(16));
//        BigInteger fac2 = facMod(f20, 0xffff_ffff_ffffL);
//        System.out.printf("%d! = %s\n", f20, fac2.toString(16).toUpperCase());

        for (int N = 10; N < 25; ++N) {
            BigInteger fac = fac(N);
            BigInteger fac2 = facMod(BigInteger.valueOf(N), 0xffff_ffff_ffffL);
            System.out.printf("%d! = %s (%s)\n", N, fac.toString(16), fac2.toString(16));
        }
    }
}
