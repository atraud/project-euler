package org.traud.math;

import java.math.BigInteger;
import java.util.Arrays;

/**
 * Created by traud on 4/17/2017.
 */
public class NumberTheory {

    private static final boolean DEBUG = false;

    public static long lcm(long a, long b) {
        if (a == 0 || b == 0)
            return 0;
        // this may overflow!
        a = Math.abs(a);
        b = Math.abs(b);
        return a / gcd(a, b) * b;
    }

    public static long lcm(long... a) {
        long l = a[0];
        for (int i = 1; i < a.length; ++i)
            l = lcm(l, a[i]);

        return l;
    }


    public static long gcd(long... a) {
        long g = a[0];
        for (int i = 1; i < a.length; ++i)
            g = gcd(g, a[i]);

        return g;
    }

    public static long gcd(long a, long b) {
        a = Math.abs(a);
        b = Math.abs(b);
        if(a == 0L) {
            return b;
        } else if(b == 0L) {
            return a;
        } else {
            int commonTrailingZeros = Long.numberOfTrailingZeros(a | b);
            a >>>= Long.numberOfTrailingZeros(a);

            while(true) {
                b >>>= Long.numberOfTrailingZeros(b);
                if(a == b) {
                    break;
                }

                if(a > b || a == -9223372036854775808L) {
                    long tmp = a;
                    a = b;
                    b = tmp;
                }

                if(a == 1L) {
                    break;
                }

                b -= a;
            }

            return a << commonTrailingZeros;
        }
    }


    public static long pow(long base, long exp) {
        long p = 1;
        for (int i = 0; i < exp; ++i) {
            p *= base;
        }
        return p;
    }

    // http://www.inf.fh-flensburg.de/lang/krypto/algo/chinese-remainder.htm

    /**
     * solves the simultaneous congruences of the form
     * x eq a[0] (mod m[0])
     * x eq a[1] (mod m[1])
     * ...
     * x eq a[n-1] (mod m[n-1])
     *
     * @param a the remainders
     * @param m the moduli
     * @return
     */
    public static long[] chineseRemainderTheorem(long[] a, long[]m) {
        if (a == null || m == null)
            throw new IllegalArgumentException("arguments must not be null. a=" + a + ", m=" + m);
        if (a.length != m.length)
            throw new IllegalArgumentException("arguments must have same size a.length=" + a.length + ", m.length=" + m.length);

        // check that all moduli are co-prime
        for (int i = 0; i < m.length-1; ++i) {
            for (int j = i+1; j < m.length; ++j) {
                long gcd = gcd(m[i], m[j]);
                if (a[i] % gcd != a[j] % gcd)
                    throw new IllegalArgumentException(
                            String.format("moduli arguments must all be co-prime. m[%d]=%d and m[%d]=%d have gcd=%d",
                                    i, m[i], j, m[j], gcd));
            }
        }
        System.out.printf("chineseRemainderTheorem(a=%s, m=%s):\n", Arrays.toString(a), Arrays.toString(m));
        long M = lcm(m);
        System.out.printf("  M=%d\n", M);
        long[] rs = new long[3];
        long x0 = 0;
        for (int i = 0; i < m.length; ++i) {
            long Mi = M / m[i];

            extendedEuclid3(m[i], Mi, rs);
            long ei = rs[2] * Mi;
            System.out.printf("  Mi=%d, ei=%d\n", M, ei);
            x0 += a[i] * ei;
        }

        System.out.printf("  (x0, M)=(%d, %d)\n", x0, M);
        long x = x0, k = 0;
        x = x % M;
        if (x < 0)
            x += M;
        return new long[] {x, M};
    }

    /**
     * extended euclid algorithm
     *
     * @param a
     * @param b
     * @param auv, upon completion, fills the provided array with the values {a, u, v},
     *             such that a = gcm(a,b), u and v, such that
     *             gcm(a,b) = u*a + v*b
     */
    public static void extendedEuclid3(long a, long b, long[] auv) {
        long u=1, v=0, s=0, t=1;
        if (DEBUG) System.out.printf("exendedEuclid3(%d, %d):\n", a, b);

        while (b!=0) {
            long q = a/b;
            if (DEBUG) System.out.printf(" q=%4d  a=%4d   b=%4d  u=%4d   s=%4d   v=%4d   t=%4d\n", q, a, b, u, s, v, t);
            long a0 = a,b0 = b,u0 = u,v0 = v,s0 = s,t0 = t;
            //a, b = b, a - q * b
            a = b0;
            b = a0 - q * b0;

            // u, s = s, u - q * s
            u = s0;
            s = u0 - q * s0;

            //v, t = t, v - q * t
            v = t0;
            t = v0 - q * t0;
        }
        if (DEBUG) System.out.printf(" q=%4d  a=%4d   b=%4d  u=%4d   s=%4d   v=%4d   t=%4d\n", -1, a, b, u, s, v, t);

        auv[0] = a;
        auv[1] = u;
        auv[2] = v;
        //return a, u, v
    }

    public static void main(String... args) {
        long[][] test = new long[][] {
                {-2,3},
                {-2,-3},
                {2,3},
                {4,5},
                {4,12},
                {6,7},
                {8,9},
                {2,3,4,5,7},
                {144,160,175},
                {160,144,175},
                {36,12,144},
        };

        for (long[] t : test) {
            long lcm=0;
            if (t.length == 2)
                lcm = lcm(t[0], t[1]);
            else
                lcm = lcm(t);
            System.out.printf("lcm(%s) = %d, gcd(%s) = %d\n", Arrays.toString(t), lcm, Arrays.toString(t), gcd(t));
        }

        long[] dst = new long[3];
        int p = 11;
        int q = 17;
        long phi = (p-1)*(q-1);
        int a = 3;
        extendedEuclid3(a, phi, dst);
        System.out.printf("%s\n", Arrays.toString(dst));
        long inv = dst[1];
        if (inv < 0)
            inv += phi;
        if (dst[0] != 1)
            inv = -1;
        System.out.printf("inverse of 3 mod %d is %d\n", phi, inv);

        BigInteger inverse = BigInteger.valueOf(3).modInverse(BigInteger.valueOf(phi));
        System.out.printf("inverse of 3 mod %d is %d\n", phi, inverse);

        long[] x = chineseRemainderTheorem(new long[]{2, 8}, new long[]{7, 10});
        System.out.printf("x=%s\n", Arrays.toString(x));

        x = chineseRemainderTheorem(new long[]{2, 3, 2}, new long[]{3, 4, 5});
        System.out.printf("x=%s\n", Arrays.toString(x));
    }
}
