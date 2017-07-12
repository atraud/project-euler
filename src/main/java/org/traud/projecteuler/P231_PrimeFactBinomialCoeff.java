package org.traud.projecteuler;

import org.traud.math.PrimeSieveOffHeapCompressed;
import org.traud.math.factor.FactorAlgorithm;
import org.traud.math.factor.PrimeFactorizationFactory;
import org.traud.math.factor.PrimeSieveIterator;


import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by traud on 7/4/2017.
 */
public class P231_PrimeFactBinomialCoeff {
    FactorAlgorithm pf = PrimeFactorizationFactory.getPrimeFactorization();

    public static BigInteger binomialCoefficient(long n, long k) {
        if (n == k || k == 0)
            return BigInteger.ONE;
        if (n == k+1 || k == 1)
            return BigInteger.valueOf(n);

        BigInteger p = BigInteger.valueOf(n);
        BigInteger q = BigInteger.ONE;

        BigInteger Z = BigInteger.ONE;
        BigInteger N = BigInteger.ONE;

        for (int i = 1; i <= k; ++i) {
//            System.out.printf("  p=%s, q=%s\n", p, q);
            Z = Z.multiply(p);
            N = N.multiply(q);

            p = p.subtract(BigInteger.ONE);
            q = q.add(BigInteger.ONE);

            Z = Z.divide(BigInteger.valueOf(i));
            N = N.divide(BigInteger.valueOf(i));
        }
        return Z.divide(N);
    }

//    public static List<Long> binomialCoefficientPrimeFactors(long n, long k) {
//        if (n == k || k == 0)
//            return Arrays.asList(Long.valueOf(1));
//        if (n == k+1 || k == 1)
//            return asList(new PrimeFactorizationQS().factor(n));
//
//        ArrayList<Long> r = new ArrayList<>();
//        BigInteger p = BigInteger.valueOf(n);
//        BigInteger q = BigInteger.ONE;
//
//        BigInteger Z = BigInteger.ONE;
//        BigInteger N = BigInteger.ONE;
//
//        for (int i = 1; i <= k; ++i) {
//            Z = Z.multiply(p);
//            N = N.multiply(q);
//
//            if (p.remainder(q).equals(BigInteger.ZERO)) {
//                r.addAll(asList(new PrimeFactorizationQS().factor(p.divide(q).longValue())));
//            }
//
//            p = p.subtract(BigInteger.ONE);
//            q = q.add(BigInteger.ONE);
//
//            Z = Z.divide(BigInteger.valueOf(i));
//            N = N.divide(BigInteger.valueOf(i));
//        }
//        return asList(new PrimeFactorizationQS().factor(Z.divide(N).longValue()));
//    }

    private static List<Long> asList(long[][] factor) {
        List<Long> r = new ArrayList<Long>();
        for (int i = 0; i < factor.length; ++i) {
            for (int j = 0; j < factor[i][1]; ++j)
                r.add(factor[i][0]);
        }
        return r;
    }

    /**
     * returns the exponent of p in the prime factorization of n!
     * Computation is done according to the theorem of Legendre.
     *
     * @param n number for which faculties prime factorization to find an exponent
     * @param p prime factor
     * @return
     */
    private static long getPrimeExponentOfFaculty(long n, long p) {
        // see also http://matheplanet.com/default3.html?call=article.php?sid=168&ref=https%3A%2F%2Fduckduckgo.com%2F
        //
        long ppow = p;
        long sum = 0;
        while (ppow <= n) {
            sum += n / ppow;
            ppow *= p;
        }
        return sum;
    }

//    private static PrimeSieve ps = new PrimeSieve(20_000_000);
    private static PrimeSieveOffHeapCompressed ps;
    static {
        try {
            ps = new PrimeSieveOffHeapCompressed("ro-composite-zip-10B.bin");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<long[]> getPrimeFactorsOfFactorial(long n) {

        PrimeSieveIterator i = ps.iterator();
        List<long[]> r = new ArrayList<>();
        while (i.hasNext()) {
            long p = i.next();
            if (p>2_000_000_000)
                break;

            if (p > n) {
                break;
            }
            long e = getPrimeExponentOfFaculty(n, p);
            if (e > 0) {
//                System.out.printf("pf(%d): e(%d) = %d\n", n, p, e);
                r.add(new long[] {p, e});
            }
        }
        return r;
    }

    public static void test(int n, int k) {
        System.out.printf("computing (%d over %d)...\n", n, k);
        BigInteger b = binomialCoefficient(n, k);
        System.out.printf("result: %s\n", b);
    }

    public static String primeExponentString(List<long[]> primeFactorsOfFaculty) {
        StringBuilder sb = new StringBuilder();
        for (long[] l : primeFactorsOfFaculty) {
            if (sb.length()>0)
                sb.append("\u00B7");
//            if (sb.length()>0)
//                sb.append("\u00D7");
            sb.append(l[0]);
            long exp = l[1];
            if (exp > 0)
                sb.append("^").append(exp);
            if (sb.length() > 150) {
                sb.append("... (").append(primeFactorsOfFaculty.size()).append(" factor(s))");
                break;
            }
        }
        return sb.toString();
    }

    public static long sumOfPrimeFactors(long n, long k) {
        System.out.printf("sumOfPrimeFactors(%d over %d):\n", n, k);
        long sum = 0;
        List<long[]> nFac = getPrimeFactorsOfFactorial(n);
        List<long[]> kFac = getPrimeFactorsOfFactorial(k);
        List<long[]> n_kFac = getPrimeFactorsOfFactorial(n-k);

        List<long[]> denomitator = multiplyPrimeFactorization(kFac, n_kFac);
        System.out.printf("z = %s\n", primeExponentString(nFac));
        System.out.printf("a = %s\n", primeExponentString(kFac));
        System.out.printf("b = %s\n", primeExponentString(n_kFac));
        System.out.printf("c = %s\n", primeExponentString(denomitator));


        List<long[]> res = cancelPrimeFactorization(nFac, denomitator);
        System.out.printf("r = %s (%s)\n", primeExponentString(res), evaluate(res));
        sum = 0;
        for (long[] baseExp : res) {
            long b = baseExp[0];
            long e = baseExp[1];
            sum += b * e;
        }
        return sum;
    }

    private static BigInteger evaluate(List<long[]> res) {
        BigInteger prod = BigInteger.ONE;
        if (res.size()>1000)
            return BigInteger.ONE.negate();

        for (long[] baseExp : res) {
            long b = baseExp[0];
            long e = baseExp[1];
            prod = prod.multiply(BigInteger.valueOf(b).pow((int)e));
        }
        return prod;
    }

    private static List<long[]> cancelPrimeFactorization(List<long[]> a, List<long[]> b) {
        List<long[]> r = new ArrayList<>();
        int i = 0, j = 0;
        while (i < a.size() || j < b.size()) {
            if (i < a.size() && j < b.size()) {
                long ba = a.get(i)[0];
                long bb = b.get(i)[0];

                long ea = a.get(i)[1];
                long eb = b.get(i)[1];

                if (ba == bb) {
                    r.add(new long[]{ba, ea - eb});
                    ++i;
                    ++j;
                }
                else if (ba < bb) {
                    r.add(new long[]{ba, ea});
                    ++i;
                }
                else if (bb < ba) {
                    r.add(new long[]{bb, eb});
                    ++j;
                }
            }
            else if (i < a.size()) {
                r.add(a.get(i++));
            }
            else {
                throw new IllegalArgumentException("can't cancel out " + b.get(j++)[0]);
            }
        }
        return r;
    }


    private static List<long[]> multiplyPrimeFactorization(List<long[]> a, List<long[]> b) {
        List<long[]> r = new ArrayList<>();
        int i = 0, j = 0;
        while (i < a.size() || j < b.size()) {
            if (i < a.size() && j < b.size()) {
                long ba = a.get(i)[0];
                long bb = b.get(i)[0];

                long ea = a.get(i)[1];
                long eb = b.get(i)[1];

                if (ba == bb) {
                    r.add(new long[]{ba, ea + eb});
                    ++i;
                    ++j;
                }
                else if (ba < bb) {
                    r.add(new long[]{ba, ea});
                    ++i;
                }
                else if (bb < ba) {
                    r.add(new long[]{bb, eb});
                    ++j;
                }
            }
            else if (i < a.size()) {
                r.add(a.get(i++));
            }
            else {
                // j< b.size()
                r.add(b.get(j++));
            }
        }
        return r;
    }

    public static void main(String... args) {
        long n = 10, k;
//        for (k = 0; k <= n; ++k) {
//            System.out.printf("(%d over %d) = %s -> %s\n", n, k,
//                    binomialCoefficient(n, k), binomialCoefficientPrimeFactors(n, k));
//        }

        // test(20_000_000, 15_000_000);
        System.out.printf("pf(10!) = %s\n", primeExponentString(getPrimeFactorsOfFactorial(10)));
        System.out.printf("pf(27!) = %s\n", primeExponentString(getPrimeFactorsOfFactorial(27)));

//        System.out.printf("pf(10!) = %s\n", primeExponentString(getPrimeFactorsOfFactorial(10)));
        System.out.printf("pf(20_000_000!) = %s\n", primeExponentString(getPrimeFactorsOfFactorial(20_000_000)));
        System.out.printf("pf(15_000_000!) = %s\n", primeExponentString(getPrimeFactorsOfFactorial(15_000_000)));
        System.out.printf("pf( 5_000_000!) = %s\n", primeExponentString(getPrimeFactorsOfFactorial( 5_000_000)));

        long[][] test = new long[][] {
                {10, 3},
                {20_000_000, 15_000_000},
        };
        for (long[] nk: test) {
            n = nk[0];
            k = nk[1];
            System.out.printf("sum of prime factors of (%d over %d) = %d\n", n, k, sumOfPrimeFactors(n, k));
        }
    }


}
