package org.traud.projecteuler;

import org.traud.math.factor.FactorAlgorithm;
import org.traud.math.factor.PrimeFactorizationFactory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by traud on 4/10/2017.
 *
 * It can be seen that the first eight rows of Pascal's triangle contain twelve distinct numbers:
 *  1, 2, 3, 4, 5, 6, 7, 10, 15, 20, 21 and 35.
 *
 * A positive integer n is called squarefree if no square of a prime divides n. Of the twelve distinct numbers in the
 * first eight rows of Pascal's triangle, all except 4 and 20 are squarefree. The sum of the distinct squarefree numbers
 * in the first eight rows is 105.
 *
 * Find the sum of the distinct squarefree numbers in the first 51 rows of Pascal's triangle.
 */
public class P203_SquarefreeBinomialCoefficients {

    static int N = 51;
    static long[][] P = new long[N+1][];

    static FactorAlgorithm pf = PrimeFactorizationFactory.getPrimeFactorization();

    public static boolean isSquareFree(long n) {
        long[][] p = pf.factor(n);
        for (long[] q : p)
            if (q[1] > 1)
                return false;

        return true;
    }
    public static void main(String[] args) {
        P[1] = new long[] {1};

        System.out.printf("%d: %s\n", 1, Arrays.toString(P[1]));
        Set<Long> seen = new HashSet<>();
        long sumDistinctSquareFree = 0;
        for (int r = 2; r <= N; ++r) {
            P[r] = new long[r];
            for (int i = 0; i < r; ++i) {
                long p;
                if (i == 0 || i == r-1)
                    p=P[r][i] = 1;
                else {
                    p=P[r][i] = P[r - 1][i - 1] + P[r - 1][i];
                    assert P[r][i]>0;
                    assert P[r][i]>P[r - 1][i - 1];
                    assert P[r][i]>P[r - 1][i];
                }
                if (seen.add(p) && isSquareFree(p)) {
                    //System.out.printf("square free: %d\n", p);
                    sumDistinctSquareFree += p;
                }
            }
            System.out.printf("%d: %s\n", r, Arrays.toString(P[r]));
        }
        System.out.printf("sum of distinct square free numbers: %d\n", sumDistinctSquareFree);

        // Found correct solution:
        //        51: [1, 50, 1225, 19600, 230300, 2118760, 15890700, 99884400, 536878650, 2505433700, 10272278170, 37353738800, 121399651100, 354860518600, 937845656300, 2250829575120, 4923689695575, 9847379391150, 18053528883775, 30405943383200, 47129212243960, 67327446062800, 88749815264600, 108043253365600, 121548660036300, 126410606437752, 121548660036300, 108043253365600, 88749815264600, 67327446062800, 47129212243960, 30405943383200, 18053528883775, 9847379391150, 4923689695575, 2250829575120, 937845656300, 354860518600, 121399651100, 37353738800, 10272278170, 2505433700, 536878650, 99884400, 15890700, 2118760, 230300, 19600, 1225, 50, 1]
            //        sum of distinct square free numbers: 34029210557338

    }
}