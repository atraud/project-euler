package org.traud.math;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by traud on 4/17/2017.
 */
public class LinAlgebra {

    public static boolean DEBUG = false;

    // returns the multiplicative inverse of a modulo p
    public static long inverse(long a, long p) {
        if (a < 0) {
            a %= p;
            if (a < 0)
                a += p;
        }
        long[] auv = new long[3];
        NumberTheory.extendedEuclid3(a, p, auv);
        long ainv = auv[1];

        ainv = ainv % p;
        if (ainv < 0)
            ainv += p;

        return ainv;
    }

    public static long[] GaussSolve(long[][] A, long[] b, long p) {
        // defensive copy
        int n = A.length;
        int m = A[0].length;
        assert b == null || m == b.length;
        assert n == m;
        int i;
        A = ArrayUtils.clone(A);
        b = b == null ? new long[m] : Arrays.copyOf(b, b.length);

        List<int[]> columnSwaps = new ArrayList<>();
        if (DEBUG) System.out.printf("GaussSolve(%d x %d | %d), p=%d\n", n,m,m,p);
        // A is an n x m matrix (i.e. n rows, m columns)
        // b is an m-entry column vector
        // p is assumed to be prime, all calculations are done modulo p

        for (i = 0; i < n; ++i) {
            if (A[i][i] == 0) {
                // find non-zero element in rows > i and swap with row i
                boolean found = false;
                for (int j = i+1; j < m; ++j) {
                    if (A[j][i] != 0) {
                        swapRow(A, b, i, j);
                        if (DEBUG) System.out.printf("swap(%d, %d)\n", i, j);
                        if (DEBUG) print(A, b);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    // and now?
                    int j = findNonZeroInRow(A, i);
                    if (j != -1) {
                        swapCol(A, i, j);
                        if (DEBUG) System.out.printf("swapCol(%d, %d)\n", i, j);
                        if (DEBUG) print(A, b);
                        columnSwaps.add(new int[] {i,j});
                        found = true;
                    }
                }
                if (!found)
                    break;
            }

            if (A[i][i] != 1) {
                // compute inverse of A[i][i];
                long aInv = inverse(A[i][i], p);
                if (DEBUG) System.out.printf("A[%d][%d] = %d, inverse is %d\n", i,i,A[i][i], aInv);
                mulRow(A, b, i, aInv, p);
                if (DEBUG) System.out.printf("A[%d] *= %d\n", i, aInv);
                if (DEBUG) print(A, b);
            }
            for (int j = i+1; j < n; ++j) {
                if (A[j][i] != 0) {
                    // optimization: we can start row ops in column
                    if (DEBUG) System.out.printf("A[%d] = A[%d] + %d*A[%d]\n", j, j, -A[j][i], i);
                    mulRow(A, b, j, i, -A[j][i], p);
                    if (DEBUG) print(A, b);
                }
            }
        }

        if (DEBUG) System.out.printf("reverse...i=%d\n", i);
        for (--i; i >= 0; --i) {
            for (int j = i-1; j >= 0; --j) {
                if (DEBUG) System.out.printf("A[%d] = A[%d] + %d * A[%d]\n", j, j, -A[i][i], i);
                mulRow(A, b, j, i, -A[j][i], p);
                if (DEBUG) print(A, b);

            }
        }
        return b;
    }

    private static void swapCol(long[][] A, int i, int j) {
        for (int k = 0; k < A.length; ++k) {
            long t = A[k][i];
            A[k][i] = A[j][j];
            A[k][j] = t;
        }
    }

    private static int findNonZeroInRow(long[][] A, int i) {
        for (int j = i+1; j < A[i].length; ++j)
            if (A[i][j] != 0)
                return j;
        return -1;
    }

    public static void print(long[][] A) {
        print(A, null);
    }

    public static void print(long[][] A, long[] b) {
        long max = 0;
        String fmt = "%3d";

        for (int i = 0; i < A.length; ++i) {
            for (int j = 0; j < A[i].length; ++j) {
                System.out.printf(fmt, A[i][j]);
            }
            if (b != null) {
                System.out.printf(" |");
                System.out.printf(fmt, b[i]);
            }
            System.out.printf("\n");
        }
    }

    public static void printVector(long[]... v) {
        long max = 0;
        String fmt = "%3d";

        for (int j = 0; j < v[0].length; ++j) {
            for (int i = 0; i < v.length; ++i) {
                if (i > 0) {
                    System.out.printf(" |");
                }
                System.out.printf(fmt, v[i][j]);
            }
            System.out.printf("\n");
        }
    }

    public static void printBinary(long[][] A) {
        printBinary(A, null);
    }

    public static void printBinary(long[][] A, long[] b) {
        for (int i = 0; i < A.length; ++i) {
            for (int j = 0; j < A[i].length; ++j) {
                System.out.print(A[i][j]);
            }
            if (b != null) {
                System.out.print("|");
                System.out.print(b[i]);
            }
            System.out.println();
        }
    }
    /**
     * for every column entry (specified by column index k) in row i, the
     *    a[i][k] = a[i][k] * x mod p
     * @param A matrix
     * @param i target row
     * @param x factor
     */
    private static void mulRow(long[][] A, long[] b, int i, long x, long p) {
        for (int k = 0; k < A[i].length; ++k) {
            A[i][k] = (A[i][k]*x) % p;
            if (A[i][k] < 0) A[i][k] += p;
        }
        if (b != null) {
            b[i] = (b[i] * x) % p;
            if (b[i] < 0) b[i] += p;
        }
    }

    /**
     * for every column entry (specified by column index k) in row i, the
     *    a[i][k] = a[i][k] + a[j][k]*x
     * @param A matrix
     * @param i target row
     * @param j source row
     * @param x factor
     */
    private static void mulRow(long[][] A, long[] b, int i, int j, long x, long p) {
        assert A[i].length == A[j].length;

        for (int k = 0; k < A[i].length; ++k) {
            A[i][k] = (A[i][k] + A[j][k]*x) % p;
            if (A[i][k] < 0) A[i][k] += p;
        }
        if (b != null) {
            b[i] = (b[i] + b[j] * x) % p;
            if (b[i] < 0) b[i] += p;
        }
    }

    private static void swapRow(long[][] A, long[] b, int i, int j) {
        assert A[i].length == A[j].length;

        for (int k = 0; k < A[i].length; ++k) {
            long t = A[i][k];
            A[i][k] = A[j][k];
            A[j][k] = t;
        }
        if (b != null) {
            long t = b[i];
            b[i] = b[j];
            b[j] = t;
        }

    }


    private static long[] matrixVectorMul(long[][] A, long[] x, long p) {
        int n = A.length;
        int m = A[0].length;
        assert m == x.length;
        assert n == m;
        // A: n x m (i.e. n rows, m columsn)
        // x: m rows
        // result:

        long[] b = new long[n];
        for (int i = 0; i < A.length; ++i) {
            for (int j = 0; j < A[i].length; ++j) {
                b[i] += (A[i][j] * x[j]) % p;
                b[i] %= p;
                if (b[i] < 0) b[i] += p;
            }
        }

        return b;
    }


    public static void main(String... args) {
        Random rnd = new Random();
        long a = rnd.nextInt(500);
        long p = BigInteger.probablePrime(10, rnd).longValue();
        long inv = inverse(a, p);
        System.out.printf("%d^(-1) mod %d = %d (%d*%d=%d mod %d)\n", a, p, inv, a, inv, a*inv % p, p);

        long[]b;
        long[][]A;

//        b = new long[]{1,2,4};
//        A = new long[][] {
//            {1, 0, 0},
//            {0, 1, 0},
//            {1, 0, 1},
//        };
//        System.out.printf("A=\n");
//        print(A, b);
//        GaussSolve(A, null, 19);

        System.out.printf("-------------\n");
        b = new long[] {8,-11,3};
        A = new long[][] {
            { 2, 1,-1},
            {-3,-1, 2},
            {-2, 1, 2},
        };
//        b = new long[] {0,0};
//        A = new long[][] {
//                {2, 3, 5},
//                {-4, 2, 3},
//        };

        DEBUG=true;
        System.out.printf("A=\n");
        print(A, null);

        long[] x = GaussSolve(A, null, 19);
        System.out.printf("x:\n");
        printVector(x);
        long[] Ax = matrixVectorMul(A, x, 19);
        System.out.printf("Ax | b: \n");
        printVector(Ax, b);
    }



}
