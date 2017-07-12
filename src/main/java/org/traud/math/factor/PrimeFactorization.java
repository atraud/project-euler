package org.traud.math.factor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by traud on 4/8/2017.
 */
public class PrimeFactorization implements FactorAlgorithm {

    private final PrimeSieve sieve;
    private final int maxValue;

    public PrimeFactorization(int maxValue) {
        this.sieve = new PrimeSieve(maxValue);
        this.maxValue = maxValue;
    }

    @Override
    public long[][] factor(long n) {
        if (n < 1)
            throw new IllegalArgumentException("invalid argument. n must be positive but is " + n);
        if (n > maxValue)
            throw new IllegalArgumentException("invalid argument. n is too large. Valid values are in [1.." + maxValue + "] but n=" + n);
        if (n == 1)
            return new long[0][];

        if (Primes.isPrime(n))
            return new long[][] {{n, 1}};
        long hi = (long)(Math.sqrt(n));
        PrimeSieveIterator it = sieve.iterator();
        List<long[]> r = new ArrayList<>();
        long[] baseExp = new long[2];
        while (it.hasNext()) {
            long p = it.next();
            // no need to continue here
            if (p > hi)
                break;

            boolean hit = false;
            while ((n % p) == 0) {
                n /= p;
                baseExp[0] = p;
                baseExp[1]++;
                hit = true;
            }
            if (hit) {
                r.add(baseExp);
                baseExp = new long[2];
                if (Primes.isPrime(n)) {
                    r.add(new long[] {n,1});
                    break;
                }

                if (n == 1)
                    break;
            }
        }

        return r.toArray(new long[r.size()][]);
    }

}
