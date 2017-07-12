package org.traud.projecteuler;


import org.traud.math.factor.PrimeSieve;
import org.traud.math.factor.PrimeSieveIterator;
import org.traud.math.factor.Primes;

import java.util.Arrays;


/**
 * Created by traud on 4/10/2017.
 *
 *
 * A Hamming number is a positive number which has no prime factor larger than 5
 * So the first few Hamming numbers are 1, 2, 3, 4, 5, 6, 8, 9, 10, 12, 15.
 * There are 1105 Hamming numbers not exceeding 108.
 *
 * We will call a positive number a generalised Hamming number of type n, if it has no prime factor larger than n.
 * Hence the Hamming numbers are the generalised Hamming numbers of type 5.
 *
 * How many generalised Hamming numbers of type 100 are there which don't exceed 10**9?
 */
public class P204_HammingNumbers {

//    static FactorAlgorithm pf = //new PrimeFactorizationQS(7);
//                                new PrimeFactorization(100_000_000);
    static PrimeSieve ps = new PrimeSieve(100);
    static PrimeSieveIterator it = ps.iterator();

    // turns out that we actually dont need a full factorization of the
    // number to determine whether it is a generalized hamming number
    // trial division for the "first few primes" is sufficient.
    public static boolean isHammingNumberOfType2(long n, long type) {

        if (n < type)
            return true;

        it.reset();
        while ((n & 1) == 0)
            n >>= 1;

        while (n != 1 && it.hasNext()) {
            long p = it.next();
            if (p > type)
                return false;

            while ((n % p) == 0) {
                n /= p;
            }
        }
        return n < type;
    }

//    public static boolean isHammingNumberOfType(long n, long type) {
//        long[][] factor = pf.factor(n);
//
//        for (long[] p : factor) {
//            if (p[0] > type)
//                return false;
//        }
//        return true;
//    }

    public static void main(String[] args) {

        StringBuilder sb = new StringBuilder();

        int type = 5;
        long MAX = (long)1e8;

        type = 100;
        MAX = (long)1e9;

//        type = 15;
//        MAX = 20000;

        System.out.printf("Hamming numbers of type %d not exceeding %d:\n", type, MAX);
        int cnt = 0;
        int lineCnt = 0;
        if (MAX < 20000)
        for (int n = 1; n <= MAX; ++n) {
            if (isHammingNumberOfType2(n, type)) {
                sb.append(String.format("%,d", n)).append(' ');
                cnt++;
                if (sb.length()>120) {
                    if ((lineCnt % 1000)==0)
                        System.out.println(sb.toString());
                    sb.setLength(0);
                    sb.append(cnt).append(": ");
                    lineCnt++;
                }
            }
        }
        System.out.println(sb.toString());
        System.out.printf("Found %,d Hamming numbers of type %d below %d:\n", cnt, type, MAX);
        System.out.printf("------------------\n");

        int i = 0;
        long[] primes = new long[type];
        for (long n = 2; n <= type; ++n) {
            if (Primes.isPrime(n))
                primes[i++]= n;
        }
        primes = Arrays.copyOf(primes, i);
        System.out.printf("primes(type=%d): %s\n", type, Arrays.toString(primes));

        long count = 0;
        int[] exp = new int[primes.length];
        for (;;) {
            long n = pow(primes, exp);
            //System.out.printf("%s -> %d, i=%d\n", Arrays.toString(exp), n, i);
            if (n > MAX) {
                if (i == primes.length-1)
                    break;
                i = increment(exp, i+1);
            }
            else {
                count++;
                i = increment(exp);
            }
        }
        System.out.printf("found %d Hamming numbers of type %d below %d\n", count, type, MAX);

//        int type = 5;
//        double MAX = 1e8;
//        1090: 92,160,000 93,312,000 93,750,000 94,371,840 94,478,400 94,921,875 95,551,488 95,659,380 96,000,000 97,200,000 97,656,250
//        1101: 98,304,000 98,415,000 99,532,800 100,000,000
//        Found 1,105 Hamming numbers of type 5


    }

    private static long pow(long[] primes, int[] exp) {
        long n = 1;
        for (int i = 0; i < primes.length; ++i)
            n *= (long)Math.pow(primes[i], exp[i]);
        return n;
    }

    private static int increment(int[] exp) {
        return increment(exp, 0);
    }
    private static int increment(int[] exp, int i) {
        for (int j = 0; j < i; ++j)
            exp[j] = 0;
        exp[i]++;
        return i;
    }
}
