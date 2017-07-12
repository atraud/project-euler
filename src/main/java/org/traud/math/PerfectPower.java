package org.traud.math;

import java.math.BigInteger;
import java.util.Random;

/**
 * Created by traud on 4/17/2017.
 */
public class PerfectPower {

    private static final boolean DEBUG = false;

    // see http://stackoverflow.com/questions/15978781/how-to-find-integer-nth-roots
    public static BigInteger irootNewton(int k, BigInteger n) {
        if (DEBUG) System.out.printf("iroot(%d, %s)\n", k, n);
        BigInteger u = n;
        BigInteger s = n.add(BigInteger.ONE);
        BigInteger k1 = BigInteger.valueOf(k - 1);
        BigInteger kb = BigInteger.valueOf(k);
        BigInteger[] qr = null;
        while (u.compareTo(s) < 0) {
            s = u;
            BigInteger t = k1.multiply(s).add(n.divide(s.pow(k - 1)));
            qr = t.divideAndRemainder(kb);
            u = qr[0];
        }
        if (DEBUG) System.out.printf("  -> %s (remainder: %s)\n", qr[0], qr[1]);
        return s;
    }


    public static BigInteger iroot(int k, BigInteger n) {
        BigInteger hi = BigInteger.ONE;
        while (hi.pow(k).compareTo(n) < 0) {
            hi = hi.shiftLeft(1);
        }

        BigInteger lo = hi.shiftRight(1);
        while (hi.subtract(lo).compareTo(BigInteger.ONE) > 0) {
            BigInteger mid = (lo.add(hi)).shiftRight(1);
            BigInteger midToK = mid.pow(k);
            if (midToK.compareTo(n)<0)
                lo = mid;
            else if (n.compareTo(midToK)<0)
                hi = mid;
            else
                return mid;
        }
        if (hi.pow(k).equals(n))
            return hi;
        else
            return lo;
    }

    private static boolean isPerfectPower(BigInteger p) {
        return isPerfectPower(p, null);
    }

    private static boolean isPerfectPower(BigInteger p, BigInteger[] res) {
        if (p.compareTo(BigInteger.ZERO)<=0)
            throw new IllegalArgumentException("p most be positive but is " + p);

        // quick check for powers of two (p is power of two, iif p & (p-1) == 0;
        if (isPowerOfTwo(p)) {
            if (res != null) {
                res[0] = BigInteger.valueOf(2);
                res[1] = BigInteger.valueOf(p.bitLength()-1);
            }
            return true;
        }

        int bits = (int)(p.bitLength()/Math.log(3));
        if (DEBUG) System.out.printf("bits=%d\n", bits);
        for (int k=bits; k > 1; --k) {
            BigInteger r = iroot(k, p);
            if (DEBUG) System.out.printf("%d-root = %s\n", k, r);
            if (r.pow(k).equals(p)) {
                if (DEBUG) System.out.printf("%s = %s^%d\n", p, r, k);
                if (res != null) {
                    res[0] = r;
                    res[1] = BigInteger.valueOf(k);
                }
                return true;
            }
        }
        return false;
    }

    private static boolean isPowerOfTwo(BigInteger p) {
        return p.equals(BigInteger.ONE) || p.and(p.subtract(BigInteger.ONE)).equals(BigInteger.ZERO);
    }


    public static void main(String... args) {

        Random rnd = new Random(); //(12345);
        for (int i = 0; i < 10000; ++i) {
            BigInteger n = BigInteger.valueOf(341); //BigInteger.valueOf(2+rnd.nextInt(1000));
            int exp = 2 + rnd.nextInt(145);
            BigInteger p = n.pow(exp); //.subtract(BigInteger.ONE);
            boolean isPow = rnd.nextBoolean();
            if (!isPow) {
                p = p.add(BigInteger.ONE);
                // check some special cases
                if (p.equals(BigInteger.valueOf(9)))
                    isPow = true;
            }
            // System.out.printf("p = %s\n", p);
            BigInteger[] r = new BigInteger[2];
            boolean power = isPerfectPower(p, r);
            System.out.printf("%d: %s is a perfect power? %s (%s)\n", i, p, power, isPow);
            if (power && r != null) {
                System.out.printf("%s = %s^%s (%s^%s)\n", p, r[0], r[1], n, exp);
            }
            System.out.printf("-------------\n");
            if (power != isPow)
                throw new IllegalArgumentException("test failed for p=" + p + " = " + n + "^" + exp);
        }
    }

}
