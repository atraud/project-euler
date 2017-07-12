package org.traud.math;

import org.apache.lucene.util.MathUtil;
import org.traud.math.factor.FactorAlgorithm;
import org.traud.math.factor.PrimeFactorizationFactory;
import org.traud.util.misc.Timer;
import org.traud.math.factor.Primes;

import java.math.BigInteger;
import java.util.*;

/**
 * Created by traud on 4/8/2017.
 */
public class PollardRhoLong {
    private final static BigInteger ZERO = new BigInteger("0");
    private final static BigInteger ONE  = new BigInteger("1");
    private final static BigInteger TWO  = new BigInteger("2");
    private final static Random random = new Random(12345678);
    private static final boolean DEBUG = false;

    public static long rho(long N) {
        long divisor;
        long mask = Long.highestOneBit(N)-1;
        long c  = random.nextLong() & mask;    // todo bit length
        long x  = random.nextLong() & mask;
        long xx = x;

        // check divisibility by 2
        if ((N % 2) == 0) return 2;

        do {
            x  = (((x * x) % N) + c) % N;
            xx = (((xx * xx) % N) + c) % N;
            xx = (((xx * xx) % N) + c) % N;
            divisor = MathUtil.gcd(x - xx, N);
        } while(divisor == 1 || divisor == N);

        return divisor;
    }

    public static long[][] factor(long N) {
        if (DEBUG) System.out.printf("factor %,d:\n", N);
        if (N <= 0)
            throw new IllegalArgumentException("argument must be positive but is " + N);
        if (N == 1) return new long[0][];
        ArrayList<Long> res = new ArrayList<Long>();

        N = handleBaseCases(N, res);
        factorAux(N, res);
        Collections.sort(res);

        List<long[]> r2 = new ArrayList<>();
        for (int i = 0; i < res.size(); ++i) {
            long base = res.get(i);
            long[] be = new long[] {base, 1};
            while (i+1 < res.size() && res.get(i+1) == base) {
                ++i;
                be[1]++;
            }
            r2.add(be);
        }
        long[][] r3 = r2.toArray(new long[r2.size()][]);
        if (DEBUG) System.out.printf("%d -> %s -> %s\n", N, res, Arrays.deepToString(r3));
        return r3;
    }

    static final long[] smallPrimeFactors = new long[] {
            2, 3, 5, 7, 11, 13, 17, 19, 23, 29
    };
    private static long handleBaseCases(long n, ArrayList<Long> res) {
//        System.out.printf("handleBaseCases(%d)\n", n);
        for (long p : smallPrimeFactors) {
            while ((n % p) == 0) {
//                System.out.printf("handleBaseCases: factor %d -> %d\n", p, n);
                res.add(p);
                n /= p;
            }
        }
        return n;
    }

    public static void factorAux(long N, ArrayList<Long> res) {
        if (N == 1) return;
        if (Primes.isPrime(N)) {
            if (DEBUG) System.out.printf("%s\n",N);
            res.add(N);
            return;
        }
        long divisor = rho(N);
        if (divisor != 1 && divisor != N) {
            factorAux(divisor, res);
            factorAux(N / divisor, res);
        }
        else {
            res.add(N);
        }
    }


    public static void main(String[] args) {
        args = new String[]{"39999856"};
        long N = 65537*123557L*97*65537*7*7;
        Random rnd = new Random();
        N = BigInteger.probablePrime(30, rnd).longValue() * BigInteger.probablePrime(30, rnd).longValue();
        org.traud.util.misc.Timer t = new Timer();
        long start = 9223372036854775754L; //9223372036854775777L;
        long num = 1000;
        List<Long> largePrimes = new ArrayList<>();
        FactorAlgorithm pfSimple = PrimeFactorizationFactory.getPrimeFactorization();

        for (N = start; N >= start-num; --N) {
//        for (N = 1_000_000_000_000L; N >= 1_000_000_000_000L-1_000_000; --N) {

            System.out.printf("%,d -> \n", N);
            System.out.flush();

//            t.reset();
//            long[][] factor = factor(N);
//            System.out.printf("  rho: %s (%1.2fs)\n", Arrays.deepToString(factor), t.getElapsedSeconds());
//            System.out.flush();


            t.reset();
            Primes.USE_JAVA_BIGINTEGER_PRIME_CHECK=true;
            long[][] factor2 = pfSimple.factor(N);
            System.out.printf("  sim: %s (%1.2fs)\n", Arrays.deepToString(factor2), t.getElapsedSeconds());
            System.out.flush();

            for (long[] p : factor2)
                if (p[0] > Integer.MAX_VALUE)
                    largePrimes.add(p[0]);
//            t.reset();
//            Primes.USE_JAVA_BIGINTEGER_PRIME_CHECK=false;
//            long[][] factor3 = PrimeFactorizationSimple.factor(N);
//            System.out.printf("  sim: %s (%1.2fs)\n", Arrays.deepToString(factor3), t.getElapsedSeconds());
//            System.out.flush();
//            if (!Arrays.deepEquals(factor2, factor3)) {
//                System.err.println("  NOT EQUAL!!!");
//                System.err.flush();
//            }

        }

        // 9223372036854775807 -> [[7, 2], [73, 1], [127, 1], [337, 1], [92737, 1], [649657, 1]] (0.02s)
        // 9223372036854775806 -> [[2, 1], [3, 1], [715827883, 1], [2147483647, 1]] (18.08s)
        // 9223372036854775805 -> [[5, 1], [23, 1], [53301701, 1], [1504703107, 1]] (55.83s)
        System.out.printf("large primes:\n");
        for (Long p : largePrimes)
            System.out.printf("%dL,\n", p);
    }


}
