package org.traud.math;

import org.traud.math.factor.FactorAlgorithm;
import org.traud.math.factor.PrimeFactorizationFactory;
import org.traud.math.factor.Primes;

/**
 * Created by traud on 4/8/2017.
 */
public class Euler {

    static FactorAlgorithm pf = PrimeFactorizationFactory.getPrimeFactorization();//new PrimeFactorization(100_000_000);

    /**
     * returns the Euler totient for the given argument, Also called Euler's phi-function.
     * The value returned is the amount of numbers k in the range 1 <= k <= n, that are co-prime to n.
     *
     * @param n
     * @return
     */
    public static long totient(long n) {
        if (n == 1)
            return 1;

        if (n <= 3)
            return n - 1;
        if (Primes.isPrime(n))
            return n - 1;

        double fractional = 1.0;

        long[][] factor = pf.factor(n);
        for (int i = 0; i < factor.length; ++i) {
            long p = factor[i][0];
            fractional *= (1.0 - (1.0 / p));
        }
        return (long) (n * fractional);
    }


    /**
     * returns the Dedekind Psi function
     * see https://en.wikipedia.org/wiki/Dedekind_psi_function for details
     *
     * @param n
     * @return
     */
    public static long dedekindPsi(long n) {
        if (n == 1)
            return 1;

        if (n <= 3)
            return n - 1;
        if (Primes.isPrime(n))
            return n - 1;

        double fractional = 1.0;

        long[][] factor = pf.factor(n);
        for (int i = 0; i < factor.length; ++i) {
            long p = factor[i][0];
            fractional *= (1.0 + (1.0 / p));
        }
        return (long) (n * fractional);
    }




    /**
     * see https://brilliant.org/wiki/carmichaels-lambda-function/
     * https://en.wikipedia.org/wiki/Carmichael_function
     *
     * @param n
     * @return
     */
    public static long carmichaelLambda(long n) {
        if (n == 1)
            return 1;

        if (n <= 3)
            return n - 1;
        if (Primes.isPrime(n))
            return n - 1;

        double fractional = 1.0;

        long[][] factor = pf.factor(n);
        long[] l = new long[factor.length];
        for (int i = 0; i < factor.length; ++i) {
            long p = factor[i][0];
            long k = factor[i][1];

            // Caution: this may overflow easily
            long totient = NumberTheory.pow(p, k) - NumberTheory.pow(p, k-1);
            if (p == 2 && k > 2)
                l[i] = totient/2;
            else
                l[i] = totient;
        }

        return NumberTheory.lcm(l);
    }


    public static void main(String... args) {
        StringBuilder[] sb = new StringBuilder[5];
        for (int i = 0; i < 5; ++i)
            sb[i] = new StringBuilder();

        sb[0].append("n:      ");
        sb[1].append("phi:    ");
        sb[2].append("lambda: ");
        sb[3].append("        ");
        sb[4].append("psi:    ");
        for (int n = 1; n < 40; ++n) {
            sb[0].append(String.format("%3d", n));
            sb[1].append(String.format("%3d", totient(n)));
            sb[2].append(String.format("%3d", carmichaelLambda(n)));
            sb[3].append("  " + (carmichaelLambda(n) != totient(n) ? "^" : " "));
            sb[4].append(String.format("%3d", dedekindPsi(n)));

        }
        System.out.printf("%s\n%s\n%s\n%s\n%s\n", sb[0], sb[1], sb[2], sb[3], sb[4]);
    }
}
