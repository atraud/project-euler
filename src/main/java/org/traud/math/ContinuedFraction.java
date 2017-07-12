package org.traud.math;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by traud on 4/13/2017.
 */
public class ContinuedFraction {


    public static boolean DEBUG = false;
    private final long[] a;
    private int startPeriod;

    private ContinuedFraction(long[] a) {
        this.a = a;
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < a.length; ++i) {
            sb.append(a[i]);
            if (i == 0)
                sb.append(";");
            else if (i == a.length-1)
                sb.append("]");
            else
                sb.append(",");
        }
        sb.append("(sp: "+startPeriod+", len:"+a.length+")");
        return sb.toString();
    }

    // https://en.wikipedia.org/wiki/Methods_of_computing_square_roots#Continued_fraction_expansion
    public static ContinuedFraction forSquareRootOf(long S) {
        long m0 = 0;
        long d0 = 1;
        long s0 = (long) Math.sqrt(S);
        long a0 = s0;
        if (a0*a0 == S)
            throw new IllegalArgumentException("S="+S+" is a square!");

        List<long[]> seq = new ArrayList<>();
        seq.add(new long[] {m0, d0, a0});

        //System.out.printf("sqrt(%d): i=%6d, m=%6d, d=%6d, a=%6d\n", S, seq.size()-1, m0, d0, a0);
        // start of period
        int s = 0;

        for (;;) {
            long m1 = d0*a0 - m0;
            long d1 = (S - m1*m1)/d0;
            long a1 = (s0 + m1)/d1;
            if ((s=contains(seq, m1, d1, a1)) != -1) {
//                if (seq.size()%2 == 0)
//                    seq.add(new long[] {m1, d1, a1});

                break;
            }

            seq.add(new long[] {m1, d1, a1});

            d0 = d1;
            m0 = m1;
            a0 = a1;
            //System.out.printf("sqrt(%d): i=%6d, m=%6d, d=%6d, a=%6d\n", S, seq.size()-1, m0, d0, a0);

        }
        long[] a = new long[seq.size()];
        for (int i = 0; i < a.length; ++i)
            a[i] = seq.get(i)[2];
        ContinuedFraction cf = new ContinuedFraction(a);
        cf.startPeriod = s;
        return cf;
    }

    private static BigInteger lcm(BigInteger a, BigInteger b) {
        BigInteger gcd = a.gcd(b);
        return a.divide(gcd).multiply(b);
    }

    public BigInteger[] asRational() {
        //        Example: The contiuned fraction of sqrt(23) is
        //
        //        4 + 1/1+ 1/3+ 1/1+ 1/8+ ...
        //
        //        which repeats after a cycle of length 4. The first convergent fraction excluding the last term (8) is
        //
        //        4 + 1/(1 + 1/(3 + 1/1)) = 24/5.
        //
        //        Thus, (24, 5) is the fundamental solution of X2 - 23Y2 = 1.

//        BigInteger p = BigInteger.ZERO;
//        BigInteger q = BigInteger.ONE;
//
//        for (int i = a.length-2; i >= 0; --i) {
//            BigInteger b = BigInteger.valueOf(a[i]);
//
//            // compute b + p/q
//            // i.e.
//            //    b*q + p
//            //   ---------
//            //       q
//            BigInteger p1 = b.multiply(q).add(p);
//            if (DEBUG) System.out.printf("p=%s,  q=%s,  b=%s -> p1=%s\n", p, q, b, p1);
//            BigInteger gcd = p1.gcd(q);
//            BigInteger q1 = q.divide(gcd);
//            p1 = p1.divide(gcd);
//
//            p = q1;
//            q = p1;
//        }
//        return new BigInteger[] {q,p};

        BigInteger hn2 = BigInteger.ZERO;
        BigInteger kn2 = BigInteger.ONE;

        BigInteger hn1 = BigInteger.ONE;
        BigInteger kn1 = BigInteger.ZERO;

        int m = (a.length == 2) ? a.length : a.length - 1;
        for (int n = 0; n < m; ++n) {
            BigInteger hn = hn1.multiply(BigInteger.valueOf(a[n])).add(hn2);
            BigInteger kn = kn1.multiply(BigInteger.valueOf(a[n])).add(kn2);

            System.out.printf("h[%d]=%s,  k[%d]=%s\n", n, hn, n, kn);

            hn2 = hn1;
            kn2 = kn1;

            hn1 = hn;
            kn1 = kn;
        }
        return new BigInteger[] {hn1,kn1};
    }


    /**
     * returns the convergents for sqrt(S)
     * @param S
     * @return
     */
    public BigInteger[] convergents(long S) {
        BigInteger hn2 = BigInteger.ZERO;
        BigInteger kn2 = BigInteger.ONE;

        BigInteger hn1 = BigInteger.ONE;
        BigInteger kn1 = BigInteger.ZERO;

        int m = a.length; //(a.length == 2) ? a.length : a.length - 1;
        int n = 0;
        for (;;) {
            BigInteger hn = hn1.multiply(BigInteger.valueOf(a[n])).add(hn2);
            BigInteger kn = kn1.multiply(BigInteger.valueOf(a[n])).add(kn2);

            if (DEBUG) System.out.printf("h[%d]=%s,  k[%d]=%s\n", n, hn, n, kn);

            hn2 = hn1;
            kn2 = kn1;

            hn1 = hn;
            kn1 = kn;

            // verify solution
            // x^2 - Dy^2 = 1
            BigInteger x2 = hn.multiply(hn);
            BigInteger y2 = kn.multiply(kn);
            BigInteger v = x2.subtract(y2.multiply(BigInteger.valueOf(S)));
            if (v.equals(BigInteger.ONE))
                break;

            ++n;
            if (n == m) {
                n = startPeriod;
            }
        }
        return new BigInteger[] {hn1,kn1};
    }

    private static int contains(List<long[]> seq, long m1, long d1, long a1) {
        for (int i = 1; i < seq.size(); ++i) {
            long[] e = seq.get(i);
            if (e[0] == m1 && e[1] == d1 && e[2] == a1)
                return i;
        }
        return -1;
    }


    public static void main(String... args) {
        ContinuedFraction cf2 = ContinuedFraction.forSquareRootOf(2);
        System.out.printf("sqrt(2) = %s. as rational=%s\n", cf2, Arrays.toString(cf2.asRational()));

        ContinuedFraction cf114 = ContinuedFraction.forSquareRootOf(114);
        System.out.printf("sqrt(114) = %s\n", cf114);

        ContinuedFraction cf313 = ContinuedFraction.forSquareRootOf(313);
        System.out.printf("sqrt(313) = %s / %s\n", cf313, Arrays.toString(cf313.asRational()));

        long[] test = new long[] {7, 13, 23, 61, 62, 60, 59, 58,313,2};
        for (long S: test) {
            ContinuedFraction cf0 = ContinuedFraction.forSquareRootOf(S);
            System.out.printf("sqrt(%d) = %s. as rational = %s\n", S, cf0, Arrays.toString(cf0.asRational()));

        }

    }

}

