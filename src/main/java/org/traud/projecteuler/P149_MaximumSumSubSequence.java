package org.traud.projecteuler;

import java.util.Arrays;
import java.util.Date;

/**
 *
 */
public class P149_MaximumSumSubSequence {

    private static final boolean VERBOSE = false;
    public static int[][] DIRECTIONS = {
        {1, 0},
        {0, 1},
        {1, 1},
        {1, -1}
    };

    public static long sumMaximum(int[][] M) {
        long maxSum = 0;
        int height = M.length;
        for (int i = 0; i < height; ++i) {
            int width = M[i].length;
            for (int j = 0; j < width; ++j) {
                if (VERBOSE) System.out.printf("checking (%d, %d)...\n", i, j);

                for (int l = 0; l < DIRECTIONS.length; ++l) {
                    int dx = DIRECTIONS[l][0];
                    int dy = DIRECTIONS[l][1];
                    int x = j;
                    int y = i;
                    long sum = 0;
                    int length = 0;
                    while (x >= 0 && x < width && y >= 0 && y < height) {
                        sum += M[y][x];
                        x += dx;
                        y += dy;
                        length++;
                        if (sum > maxSum) {
                            maxSum = sum;
                            if (VERBOSE) System.out.printf(
                                    "new maximum sum %2d, starting at (%d, %d), direction: (%d, %d), length: %d \n",
                                    maxSum, j, i, dx, dy, length);
                        }

                    }
                }
            }
        }
        return maxSum;
    }

    private static long sumRow(int[][] tri, int i, int j, int width) {
        long sum = 0;
        for (int jj = 0; jj < width; ++jj) {
            sum += tri[i][j+jj];
        }
        return sum;
    }


    interface IntProducer {
        int next();
    };
    private static class LaggedFibonacciGenerator implements IntProducer {

        // For 1 ≤ k ≤ 55, sk = [100003 − 200003k + 300007k3] (modulo 1000000) − 500000.
        // For 56 ≤ k ≤ 4,000,000, sk = [sk−24 + sk−55 + 1000000] (modulo 1000000) − 500000.

        final long[] val;
        int i;
        public LaggedFibonacciGenerator() {
            val = new long[57];
            i = 1;
        }
        @Override
        public int next() {
            int k = i;
            ++i;
            if (1 <= k && k <= 55) {
                long sk = ((100003L - 200003L * k + 300007L * k * k * k) % 1000000) - 500000;
                val[k] = sk;
                return (int)sk;
            }
            else {
//                System.out.printf("LFG k=%2d: %s\n", k, Arrays.toString(Arrays.copyOfRange(val,0, 56)));
                long sk24 = val[56-24];
                long sk55 = val[56-55];
                long sk = ((sk24 + sk55 + 1000000L) % 1000000) - 500000;
                val[56] = sk;
                System.arraycopy(val, 1, val, 0, val.length-1);

                return (int)sk;
            }
        }
    }
    private static int[][] generateMatrix(int size, IntProducer ip) {
        int[][] T = new int[size][size];
        for (int i = 0; i < T.length; ++i) {
            T[i] = new int[size];
            for (int j = 0; j < size; ++j)
                T[i][j] = ip.next();
        }
        return T;
    }

    public static void main(String[] args) {

        int[][] T = new int[][] {
            {-2,  5,  3,  2},
            { 9, -6,  5,  1},
            { 3,  2,  7,  3},
            {-1,  8, -4,  8},
        };

        LaggedFibonacciGenerator lfg = new LaggedFibonacciGenerator();
        for (int i = 1; i <= 103; ++i) {
            int next = lfg.next();
            if (i == 10 || i>= 95)
                System.out.printf("%3d: %8d\n", i, next);
            if (i == 10 && next != -393027)
                throw new IllegalArgumentException("expecting value -393027 for idx="+i);
            if (i == 100 && next != 86613)
                throw new IllegalArgumentException("expecting value 86613 for idx="+i);
        }
        System.out.printf("%s\n", Arrays.toString(lfg.val));

        System.out.printf("%s: sum maximum: %d\n", new Date(), sumMaximum(T));

        lfg = new LaggedFibonacciGenerator();
        T = generateMatrix(2000, lfg);

        System.out.printf("size=2000...\n");
        long l = sumMaximum(T);
        System.out.printf("%s: sum maximum: %d\n", new Date(), l);
//        Fri Jul 07 00:09:01 CEST 2017: sum maximum: 52852124

    }


}
