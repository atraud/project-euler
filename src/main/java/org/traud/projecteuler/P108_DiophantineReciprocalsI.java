package org.traud.projecteuler;

import org.traud.math.factor.FactorAlgorithm;
import org.traud.math.factor.PrimeFactorizationFactory;
import org.traud.math.factor.Primes;
import org.traud.util.misc.Timer;

import java.io.IOException;
import java.util.Arrays;

/**
 *
 *



 In the following equation x, y, and n are positive integers.

 1   1   1
 - + - = -
 x   y   n

                               y + x        y + x      1
 1/x + 1/y = y/x*y + x/x*y = ---------- =  --------- = ---
                               x * y       n*(y+x)     n

 For n = 4 there are exactly three distinct solutions:

 1    1    1
 - + --- = -
 5   20    4

 1    1   1
 - + -- = -
 6   12   4

 1   1   1
 - + - = -
 8   8   4

 What is the least value of n for which the number of distinct solutions exceeds one-thousand?

 NOTE: This problem is an easier version of Problem 110; it is strongly advised that you solve this one first.

 */
public class P108_DiophantineReciprocalsI {

    public static void main(String... args) throws IOException {
        FactorAlgorithm pfqs = PrimeFactorizationFactory.getPrimeFactorization();

        Timer t = new Timer();
        long maxCount = 0;
        long maxCountN = 0;
        long[][] maxCountNFactors = null;
        for (long n = 1260; n < 1261; n += 1) {
            if (Primes.isPrime(n))
                continue;
            long max = n*5000;
            long count = 0;
            long x;
            long k=1;
            //for (x = n+1; x < max; x+=k, k<<=1) {
            for (x = n+1; x < max; x++) {
                long n_x = n*x;
                long sub = x-n;

                if (n_x % sub == 0) {
                    long y = n_x / sub;
                    // check solution
                    if (y < x)
                        break;
                    count++;
                    double floatResult = 1.0 / n;
                    double expectedResult = ((double) (x + y)) / (x * y);
//                    if (count == 1)
//                        System.out.println();
//                    System.out.printf(
//                            "%d: 1/%d + 1/%d = 1/%d (check: %f + %f = %f =?= %f)\n",
//                            count, x, y, n, 1.0/x, 1.0/y, expectedResult, floatResult);
                    if (Math.abs(floatResult - expectedResult) > 1e-6) {
                        throw new IllegalStateException("n=" + n + ": expected result doesn't equal computed one: " + expectedResult + " vs. " + floatResult);
                    }
                }
            }
            if (count > maxCount) {
                maxCount = count;
                maxCountN = n;
                maxCountNFactors = pfqs.factor(n);
            }
            System.out.printf("for n=%d, the count is %4d (n=%s, maxX=%d, maxX/n=%d, maxCount=%d, maxCountN=%d, n=%s%s)\n",
                    n, count,
                    Arrays.deepToString(pfqs.factor(n)),
                    x, x/n, maxCount, maxCountN, Arrays.deepToString(maxCountNFactors), Primes.isPrime(n) ? " p" : "");
//            System.out.printf("================\n");
            if (count > 1_000)
                break;
        }
        System.out.printf("took %1.2fs to complete\n", t.getElapsedSeconds());

//        for n=69264, the count is 203 (maxCount=608, maxCountN=55440)
//        for n=69265, the count is 14 (maxCount=608, maxCountN=55440)
//        ...
//        for n=180175, the count is    8 (maxX=1081050, maxX/n=6, sqrt(n)=424.470258, maxCount=851, maxCountN=120120)
//        for n=180176, the count is   14 (maxX=540528, maxX/n=3, sqrt(n)=424.471436, maxCount=851, maxCountN=120120)
//        for n=180177, the count is   41 (maxX=405916, maxX/n=2, sqrt(n)=424.472614, maxCount=851, maxCountN=120120)
//        for n=180178, the count is    5 (maxX=540534, maxX/n=3, sqrt(n)=424.473792, maxCount=851, maxCountN=120120)
//        for n=180180, the count is 1013 (maxX=362362, maxX/n=2, sqrt(n)=424.476148, maxCount=1013, maxCountN=180180)
//
//        Congratulations, the answer you gave to problem 108 is correct.
//
//        You are the 9609th person to have solved this problem.
//
//        Nice work, atraud, you've just advanced to Level 5 .
//        5830 members (0.84%) have made it this far.
//
//        Return to Problems page.
//        for n=180177, the count is   41 (maxX=405916, maxX/n=2, sqrt(n)=424.472614, maxCount=851, maxCountN=120120, n=[[2, 3], [3, 1], [5, 1], [7, 1], [11, 1], [13, 1]])
//        for n=180178, the count is    5 (maxX=540534, maxX/n=3, sqrt(n)=424.473792, maxCount=851, maxCountN=120120, n=[[2, 3], [3, 1], [5, 1], [7, 1], [11, 1], [13, 1]])
//        for n=180180, the count is 1013 (maxX=362362, maxX/n=2, sqrt(n)=424.476148, maxCount=1013, maxCountN=180180, n=[[2, 2], [3, 2], [5, 1], [7, 1], [11, 1], [13, 1]])
//        took 637.64s to complete.
    }
}
