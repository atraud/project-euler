package org.traud.projecteuler;

import org.traud.math.ContinuedFraction;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

/**
 * Created by traud on 4/13/2017.
 *
 *
 * Consider quadratic Diophantine equations of the form:
 *    x^2 – Dy^2 = 1
 *
 * For example, when D=13, the minimal solution in x is 6492 – 13×1802 = 1
 *
 * It can be assumed that there are no solutions in positive integers when D is square.
 *
 * By finding minimal solutions in x for D = {2, 3, 5, 6, 7}, we obtain the following:
 *   3^2 – 2×2^2 = 1
 *   2^2 – 3×1^2 = 1
 *   9^2 – 5×4^2 = 1
 *   5^2 – 6×2^2 = 1
 *   8^2 – 7×3^2 = 1
 *
 * Hence, by considering minimal solutions in x for D ≤ 7, the largest x is obtained when D=5.
 *
 * Find the value of D ≤ 1000 in minimal solutions of x for which the largest value of x is obtained.
 */
public class P66_DiophantineEquationSOLVED {

    static Map<Long, Long> squares = new HashMap<>();
    static {
        for (long n = 1; n <= 10_000; ++n) {
            squares.put(n * n, n);
        }
    }

    public static long[] bruteForcePellEquation(long D) {
        long[] r = new long[2];
        long x=0, y=0;

        // x^2 - D*y^2 = 1
        // y^2 = (x^2 - 1)/D
        for (x = 1; x < 1<<D; ++x) {
            long k = x*x - 1;
            Long Y;
            if ((k % D == 0) && (Y=squares.get(k/D))!=null) {
                y = Y.longValue();
                break;
            }
        }

        r[0] = x;
        r[1] = y;
        return r;
    }

    public static void main(String... args) {
        int cnt=0;
        BigInteger largestX = BigInteger.ZERO;
        long largestD = 0;
//        ContinuedFraction.DEBUG= false;
        for (long D = 1; D <= 1000; ++D) {
            if (squares.containsKey(D))
                continue;

            Timer t = new Timer();
            // long[] xy = bruteForcePellEquation(D);

            ContinuedFraction cf = ContinuedFraction.forSquareRootOf(D);
            cnt++;
            BigInteger[] xy = cf.convergents(D);
//            System.out.printf("%3d: D=%6d:  %6d² - %6dx%6d² = 1   rational: %s   (%s - %1.2fs)\n",
//                    cnt, D, xy[0], D, xy[1],
//                    Arrays.toString(xy), cf, t.getElapsedSeconds());
            System.out.printf("D=%d:  %s² - %d x %s² = 1 (%s)\n",
                    D, xy[0], D, xy[1], cf);

//            if ((cfRational[0].longValue() != xy[0] || cfRational[1].longValue() != xy[1]))
//                break;

            if (xy[0].compareTo(largestX)>0) {
                largestX = xy[0];
                largestD = D;
            }
        }
        System.out.printf("D=%d for largest x (%s)\n", largestD, largestX);
    }
}
