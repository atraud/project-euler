package org.traud.projecteuler;

import org.traud.util.misc.Timer;

import java.util.HashMap;
import java.util.Map;

import static org.traud.math.ContinuedFraction.DEBUG;

/**
 * Created by traud on 4/13/2017.
 */
public class P86_CuboidRoute {

    static Map<Long, Long> squares = new HashMap<>();
    static {
        for (long n = 1; n <= 10_000; ++n) {
            squares.put(n * n, n);
        }
    }

    static double eps = 1e-5;
    static int iterationCount = 20;

    interface Function {
        double apply(double x);
    }

    public static double derivative(Function f, double x, int n) {
        if (n == 0)
            return f.apply(x);

        if (n == 1) {
            double f1 = f.apply(x+eps);
            double f0 = f.apply(x);

            return (f1-f0)/eps;
        }

        double f1 = derivative(f, x+eps, n-1);
        double f0 = derivative(f, x, n-1);
        return (f1-f0)/eps;
    }

    public static double newtonIteration(Function f, double xn) {
        double xn2 = xn - derivative(f, xn, 1) / derivative(f, xn, 2);
        return xn2;
    }

    public static double minimalIntegerLength(final int a, final int b, final int c) {
        Function f = new Function() {
            @Override
            public double apply(double x) {
                double s = x * a;
                double l1 = Math.sqrt(c*c + (a-s)*(a-s));
                double l2 = Math.sqrt(b*b + s*s);
                return l1+l2;
            }
        };
        double x = 0.5;
        for (int i = 0; i < iterationCount; ++i) {
            x = newtonIteration(f, x);

            if (x < 0.0)
                x = 0.0;
            else if (x > 1.0)
                x = 1.0;
            //System.out.printf("%d: x=%1.8f -> f=%1.8f\n", i, x, f.apply(x));
        }
        double dist = f.apply(x);
//        if (isInteger(dist)) {
//            double s = x * a;
//            System.out.printf("(A,B,C)=(%d,%d,%d), x=%1.12f -> d=%1.12f  (s=%f,  d=sqrt(%f)+sqrt(%f))\n", a, b, c, x, dist, s,
//                    c*c + (a-s)*(a-s), b*b+s*s);
//        }
        return dist;
    }

    private static boolean isInteger(double x) {
        double frac = x - Math.floor(x);
        return (frac < 1e-5);
    }

    private static double distanceOnCubeSurface(int a, int b, int c) {


        //  +-------------+
        //  |            /| c
        //  |       a   / |
        //  +----------*--+
        //  |         /s  |
        //  | ........    | b
        //  |/            |
        //  +-------------+
        //
        //
        double l = Double.MAX_VALUE;
        double l1 = minimalIntegerLength(a, b, c);  l = Math.min(l, l1);
        double l2 = minimalIntegerLength(a, c, b);  l = Math.min(l, l2);
        double l3 = minimalIntegerLength(b, a, c);  l = Math.min(l, l3);
        double l4 = minimalIntegerLength(b, c, a);  l = Math.min(l, l4);
        double l5 = minimalIntegerLength(c, a, b);  l = Math.min(l, l5);
        double l6 = minimalIntegerLength(c, b, a);  l = Math.min(l, l6);

        return l;
    }


    private static boolean shortestRouteHasIntegerLength(int a, int b, int c) {
        // determine sqaure of shortest path
        long p1 = (a+c)*(a+c)+b*b;
        long p2 = (a+b)*(a+b)+c*c;
        long p3 = (b+c)*(b+c)+a*a;

        if (p1 < p2 && p1 < p3) {
            return isPythagoreanTripleBase(a+c,b);
        }

        if (p2 < p1 && p2 < p3) {
            return isPythagoreanTripleBase(a + b, c);
        }

        return isPythagoreanTripleBase(b+c,a);
    }

    private static boolean isPythagoreanTripleBase(long a, long b) {
        long c2 = a*a + b*b;
        boolean isSquare = squares.containsKey(c2);
        if (DEBUG && isSquare)
            System.out.printf("isPythagoreanTripleBase(%d,%d): %d²+%d²=%d = %d²\n", a,b, a, b, c2, squares.get(c2));
        return isSquare;
    }


    public static void main(String... args) {
        double D = distanceOnCubeSurface(3,5,6);
        D = distanceOnCubeSurface(5,3,6);
        System.out.printf("D=%1.8f  frac(D)=%f\n", D, D-Math.floor(D));

        for (int M=1815; M <= 2000; ++M) {
            long cnt = 0;
            long cnt2 = 0;
            Timer t = new Timer();
            for (int A = 1; A <= M; ++A) {
                for (int B = 1; B <= A; ++B) {
                    for (int C = 1; C <= B; ++C) {
                        if (shortestRouteHasIntegerLength(A,B,C))
                            cnt2++;

//                        D = distanceOnCubeSurface(A, B, C);
//                        double frac = D - Math.floor(D);
//                        if (frac < 1e-5) {
//                            //System.out.printf("A=%d, B=%d, C=%d -> D=%f\n", A, B, C, D);
//                            cnt++;
//                        }
//                        else if (shortestRouteHasIntegerLength(A,B,C)) {
//                            System.out.printf("A=%d, B=%d, C=%d -> D=%f but pyth. tripel\n", A, B, C, D);
//                            DEBUG=true;
//                            shortestRouteHasIntegerLength(A,B,C);
//                            DEBUG=false;
//
//                        }
                    }
                }
            }
            System.out.printf("M=%d -> cnt=%d, cnt2=%d (%1.2fs)\n", M, cnt, cnt2, t.getElapsedSeconds());
            if (cnt2>1_000_000)
                break;
        }

//        M=500 -> cnt=64210
//        M=501 -> cnt=64378
//        M=502 -> cnt=64378
//        M=503 -> cnt=64378
//        M=504 -> cnt=66404
    }



}
