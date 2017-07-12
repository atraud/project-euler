package org.traud.projecteuler;

import java.util.Arrays;
import java.util.Date;

/**
 *
 */
public class P250_TriangleWithMinumumSum {

    private static final boolean VERBOSE = false;

    public static long triangleMinSum(int[][] tri) {
        long minSum = 0;
        int height = tri.length;
        for (int i = 0; i < height-1; ++i) {
            int width = tri[i].length;
            for (int j = 0; j < width; ++j) {
                if (VERBOSE) System.out.printf("checking (%d, %d)...\n", i, j);
                long prevSum = 0;
                for (int k = 1; k < height - i; ++k) {
                    long sum = prevSum + sumRow(tri, i+k-1, j, k); // sumTriangle(tri, i, j, k);
                    if (sum < minSum) {
                        minSum = sum;
                    }
                    prevSum = sum;
                }
            }
        }
        return minSum;
    }

    private static long sumRow(int[][] tri, int i, int j, int width) {
        long sum = 0;
        for (int jj = 0; jj < width; ++jj) {
            sum += tri[i][j+jj];
        }
        return sum;
    }

    private static long sumTriangle(final int[][] tri, final int i, final int j, final int height) {
        long sum = 0;
        if (VERBOSE) System.out.printf("summing tri at (%d,%d), h=%d\n", i, j, height);
        for (int ii = 0; ii < height; ++ii) {
            for (int jj = 0; jj < ii+1; ++jj) {
                if (VERBOSE) System.out.printf(" (%d,%d) sum = %d\n", i+ii, j+jj, sum);
                sum += tri[i + ii][j + jj];
            }
        }
        if (VERBOSE) System.out.printf("sum=%d\n", sum);
        return sum;
    }

    interface IntProducer {
        int next();
    };
    private static class LinearCongruentialGenerator implements IntProducer {

        final int[] val;
        int i;
        public LinearCongruentialGenerator() {
            val = new int[500500];
            i = 0;

            int t = 0;
            for (int k = 1; k <= 500500; ++k) {
                t =(615949  * t + 797807) & ((1<<20)-1);
                val[k-1] = t - (1 << 19);
            }
        }
        @Override
        public int next() {
            int ii = i;
            i = i+1 % val.length;
            return val[ii];
        }
    }
    private static int[][] generateTri(int height, IntProducer ip) {
        int[][] T = new int[height][];
        for (int i = 0; i < T.length; ++i) {
            T[i] = new int[i + 1];
            for (int j = 0; j < T[i].length; ++j)
                T[i][j] = ip.next();
        }

        return T;
    }

    public static void main(String[] args) {

        int[][] T = new int[][] {
                {15},
                {-14, -7},
                {20, -13, -5},
                {-3, 8, 23, -26},
                {1, -4, -5, -18, 5},
                {-16, 31, 2, 9, 28, 3},
        };

        long sum = triangleMinSum(T);
        System.out.printf("%s: minimum sum is %d\n", new Date(), sum);

        System.out.printf("%s: genearing...\n", new Date());
        T = generateTri(1000, new LinearCongruentialGenerator());
        System.out.printf("%s: done...\n", new Date());
        System.out.printf("%s\n%s\n%s\n...\n", Arrays.toString(T[0]), Arrays.toString(T[1]), Arrays.toString(T[2]));
        sum = triangleMinSum(T);
        System.out.printf("%s: minimum sum is %d\n", new Date(), sum);

    }


}
