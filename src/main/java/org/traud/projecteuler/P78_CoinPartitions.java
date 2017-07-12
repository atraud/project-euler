package org.traud.projecteuler;


import org.traud.math.factor.FactorAlgorithm;
import org.traud.math.factor.PrimeFactorizationFactory;
import org.traud.util.misc.Timer;

import java.util.Arrays;

/**
 * Created by traud on 4/10/2017.
 *
 *
 * Let p(n) represent the number of different ways in which n coins can be separated into piles.
 * For example, five coins can be separated into piles in exactly seven different ways, so p(5)=7.
 *
 * 1:      OOOOO
 * 2:     OOOO  O
 * 3:     OOO  OO
 * 4:    OOO  O  O
 * 5:    OO  OO  O
 * 6:   OO O  O   O
 * 7:  O  O  O  O  O
 *
 * Find the least value of n for which p(n) is divisible by one million.
 */
public class P78_CoinPartitions {

    private static final boolean DEBUG = false;
    private static final int N = 20000;
    private static int HITS = 0;
    private static int MISSES = 0;
    private static int[][] LUT = new int[N][];
    static {
        for (int n = 0; n < N; ++n) {
            LUT[n] = new int[n + 1];
            Arrays.fill(LUT[n], -1);
        }
    }

    // see also https://en.wikipedia.org/wiki/Partition_(number_theory)#Partition_function
    public static int partition(int n, int mod) {
        return partition(n, n, mod);
    }
    public static int partition(int n, int max, int mod) {
        //System.out.printf("partition(n=%d, max=%d)\n", n, max);
        int M = Math.min(max, n);
        if (LUT[n][M] != -1) {
            HITS++;
            return LUT[n][M];
        }

        if (n == 0) {
            HITS++;
            return LUT[n][M] = 1;
        }
        MISSES++;

        long accum = 0;
        for (int i = 1; i <= M; i++) {
            accum += partition(n-i, i, mod);
        }
        accum %= mod;

        if (DEBUG && accum % mod == 0)
            System.out.printf("p(%d,%d) mod %d is 0\n", n, max, mod);

        //
        // p(0) = 1
        // p(n) = p(n,n)
        //          min(n,m)
        //          +----
        //           \
        // p(n,m) =  /     p(n-i, i)
        //          +----
        //          i = 1


        //                             min(n,m)
        //                             +----
        //                              \
        // p(n,m) ≡ 0 (mod K)  <=>  (   )    p(n-i, i)  ) ≡ 0 (mod K)
        //                             /
        //                             +----
        //                             i = 1

        // i.e. there is a natural number s, with
        //                             min(n,m)
        //                             +----
        //                              \
        // p(n,m) = s * K      <=>  (   )    p(n-i, i)  ) = s * K
        //                             /
        //                             +----
        //                             i = 1


        return LUT[n][M] = (int)accum;
    }

    public static long pFast(int m, int mod) {
        // http://www.numericana.com/answer/numbers.htm#partitions
        int[] p = new int[m+1];
        p[0] = 1;

        for (int i = 1; i <= m; ++i) {
            int  j = 1, k = 1, s = 0;
            while (j > 0) {
                int exp = (k&1) != 0 ? (-1) : 1;
                j = i - (3 * k * k + k)/2;

                if (j >= 0) s = s - exp * p[j];
                s %= mod;

                j = i - (3 * k * k - k)/2;

                if (j >= 0) s = s - exp * p[j];
                s %= mod;
                k = k + 1;
            }
            while (s < 0)
                s += mod;
            p[i] = s % mod;
        }
        return p[m];
    }

    public static void main(String[] args) {

        Timer t = new Timer();
        int mod = 100_000; //00_000;

        // 9 coins -> 0 mod 10 partitions
        // 74 coins -> 0 mod 100 partitions
        // 449 coins -> 0 mod 1,000 partitions
        // 599 coins -> 0 mod 10,000 partitions

//        9 coins -> 0 mod 10 partitions   (9 = [[3, 2]])
//        74 coins -> 0 mod 100 partitions   (74 = [[2, 1], [37, 1]])
//        449 coins -> 0 mod 1,000 partitions   (449 = [[449, 1]])
//        599 coins -> 0 mod 10,000 partitions   (599 = [[599, 1]])

        // 1_000_000 = 2^6*5^6
        // 7995
        FactorAlgorithm pf = PrimeFactorizationFactory.getPrimeFactorization();

        mod = 1_000_000;
//        for (mod = 10; mod <= 1_000_000; mod *= 10) {
        for (int coins = 55000; ; ++coins) {
            long p = 0;//partition(coins, mod);
            long p2 = pFast(coins, mod);
            p = p2;
            System.out.printf("%d coins -> %,d mod %,d partitions (%d)\n", coins, p, mod, p2);
            if (p % mod == 0) {
                System.out.printf("%d coins -> %,d mod %,d partitions ", coins, p, mod);
                System.out.printf("  (%d = %s)\n", coins, Arrays.deepToString(pf.factor(coins)));
                break;
            }

//            if (coins > 30000)
//                break;
        }
//        }
        System.out.printf("done in %,d ms.\n", t.getElapsedMillis());
//        System.out.printf("%d hits (%d%%), %d misses (%d%%)\n",
//                HITS, 100L*HITS/(HITS+MISSES),
//                MISSES, 100L*MISSES/(HITS+MISSES));
    }
}
