package org.traud.projecteuler;

import org.traud.math.PrimeSieveOffHeapCompressed;
import org.traud.math.factor.PrimeSieveIterator;
import org.traud.math.factor.Primes;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by traud on 4/12/2017.
 *
 *

 There are some prime values, p, for which there exists a positive integer, n, such that the expression n3 + n2p is a perfect cube.

 For example, when p = 19, 83 + 82×19 = 123.

 What is perhaps most surprising is that for each prime with this property the value of n is unique, and there are only four such primes below one-hundred.

 How many primes below one million have this remarkable property?

 */
public class P131_PrimeCubePartnership {

    private static final long MAX = 1_000_000L;
    private static final long C_MAX = 10_000L;
    private static final long N_MAX = 1_000_000L;

    static Map<Long, Long> CUBES = new HashMap<>();
    static {
        for (long n = 1; n < MAX; ++n) {
            CUBES.put(n*n*n, n);
        }
    }


    private static int primeCubePartners() {
        int cnt = 0;
        for (long n = 1; n <= N_MAX; ++n) {
            long n2=n*n;
            long n3=n2*n;
            for (long c = n+1; c <= N_MAX; ++c) {
                long c3 = c*c*c;
                assert c3>=c : "c**3 ("+c+") must me larger than c="+c;
                assert c3>=c*c;
                long s = (c3-n3);
                if (s % n2 == 0) {
                    s = s/n2;

                    if (Primes.isPrime(s) && s < MAX) {
                        System.out.printf("p=%d, n=%d\n", s, n);
                        cnt++;
                    }
                    else {
                        //System.out.printf("s=%d is not prime (n=%d)\n", s, n);
                    }
                }
                else {
                    //System.out.printf("s=%d is not divisible by n*n=%d\n", s, n);
                }
            }
        }
        return cnt;
    }
    // check whether there is an n, such that n**3 + (n**2)*p is a perfect cube
    // n**3 + (n**2)*p == (n**2)*(n+p) == m**3
    // i.e.
    //      (n**2)*(n+p) == m**3
    //
    // what is a cube?
    //   c = m**3, i.e. the prime factorization of c has exponents all divisible by 3
    //
    // m = p1^k1 * p2^k2 * ... pn1^kn1
    //
    // c = (p1^k1 * p2^k2 * ... pn1^kn1)^3
    // or
    // c = p1^3k1 * p2^3k2 * ... pn1^3kn1
    //
    // p1^3k1 * p2^3k2 * ... pn1^3kn1 = (n**2)*(n+p)
    //
    // p1^3k1 * p2^3k2 * ... pn1^3kn1     n**3
    // ------------------------------  =  ---- + (n**2)
    //               p                      p

    public static boolean hasCubePartner(long p, int idx) {
        // assumes p to be prime

        // Hypothesis:
        // n is bounded by p x some ratio.

        // The ratio has been determined via some search over the first
        // found cube partners

//        idx=	1	p=	7	n=	1	ratio=	0.142857143
//        idx=	2	p=	19	n=	8	ratio=	0.421052632
//        idx=	3	p=	37	n=	27	ratio=	0.72972973
//        idx=	4	p=	61	n=	64	ratio=	1.049180328
//        idx=	5	p=	127	n=	216	ratio=	1.700787402
//        ...
//        idx=	32	p=	13267	n=	287496	ratio=	21.67000829
//        idx=	33	p=	13669	n=	300763	ratio=	22.00329212
//        idx=	34	p=	16651	n=	405224	ratio=	24.33631614
//        idx=	35	p=	19441	n=	512000	ratio=	26.33609382
//        idx=	36	p=	19927	n=	531441	ratio=	26.66939329


        long ratio = Math.max(13, idx-6);
        long maxN = p * ratio;
        for (long n = 1; n <= maxN; ++n) {
            long n2 = n * n;
            long n3 = n2 * n;
            long s = n3 + n2 * p;
            if (CUBES.containsKey(s)) {
                System.out.printf("%d: %d = %d**3+%d*%d**2 = %d = %d**3 (maxRatio=%d, actRatio=%1.2f)\n",
                        idx+1, p, n,p,n,s,CUBES.get(s),
                        ratio, ((double)n)/p);
                return true;
            }
        }

        return false;
    }

    public static void main(String... args) throws IOException {
//        int cnt = primeCubePartners();
//        System.out.printf("found %d prime/cube partners below %,d (n_max=%,d)\n", cnt, MAX, N_MAX);
//        System.exit(0);

        PrimeSieveOffHeapCompressed ps = new PrimeSieveOffHeapCompressed("ro-composite-zip-10M.bin");
        PrimeSieveIterator iterator = ps.iterator();

//        long MAX = 1_000_000;
//        int cnt = 0;
//        while (iterator.hasNext()) {
//            long p = iterator.next();
//            if (p > MAX)
//                break;
//
//            if (hasCubePartner(p, cnt)) {
//                //System.out.printf("p=%d\n", p);
//                cnt++;
//            }
//        }
//        ps.close();
//        System.out.printf("found %d primes below %d with a 'cube partner'\n", cnt, MAX);

        //http://www.mathblog.dk/project-euler-131-primes-perfect-cube/
//        If we start out with the equation
//
//\displaystyle n^3 n^2p = k^3
//
//        where k is a perfect cube and p is prime, then we can rewrite this as
//
//\displaystyle n^3 n^2p = k^3 \Leftrightarrow
//
//\displaystyle n^3 (\frac{p}{n} 1) = k^3 \Leftrightarrow
//
//\displaystyle n^3 \frac{p n}{n} = k^3 \Leftrightarrow
//
//\displaystyle n \sqrt[3]{\frac{p n}{n}} = k\Leftrightarrow
//
//\displaystyle n\frac{\sqrt[3]{p n}}{\sqrt[3]{n}} = k
//
//        From this is follows that if k is an integer then both n and n+p needs to be perfect cubes as well.
//
//                Since both n and n + p are cubes we can denote them n = x^3 and p n=y^3  where x, y \in \mathbb{Z} (meaning they are integers). Then it follows from rearranging and inserting into the second equation that p = y^3 - n = y^3 - x^3 . So p is the difference of two cubes.  This difference can be written as
//
//        p =y^3 - x^3 = (y-x)(y^2 yx x^2)
//
//        Since we have the factor y-x in from of the larger equation, it is obvious that p must be divisible by y-x and therefore p can only be prime if y-x = 1. So we have that p must be the difference of two consecutive cubes. Therefore all we need to check in order to solve this problem is if
//
//        p = (i 1)^3 - i^3
//
//        is prime for any number below 1.000.000. For i = 577 we get that p = (i 1)^3 - i^3 = 578^3 - 577^3 = 1.000.519.
//
//                So we only need to check 577 values of i, which is pretty quickly done with the code
        int result = 0;
        for (int i = 1; i < 577; i++) {
            if (Primes.isPrime((i + 1) * (i + 1) * (i + 1) - i * i * i))
                result++;
        }
        System.out.printf("result is %d\n", result);

        //        p=7, n=1: n**3+(n**2)*p = 8 = 2**3 (n=[])
//        p=19, n=8: n**3+(n**2)*p = 1728 = 12**3 (n=[[2, 3]])
//        p=37, n=27: n**3+(n**2)*p = 46656 = 36**3 (n=[[3, 3]])
//        p=61, n=64: n**3+(n**2)*p = 512000 = 80**3 (n=[[2, 6]])
//        p=127, n=216: n**3+(n**2)*p = 16003008 = 252**3 (n=[[2, 3], [3, 3]])
//        p=271, n=729: n**3+(n**2)*p = 531441000 = 810**3 (n=[[3, 6]])
//        p=331, n=1000: n**3+(n**2)*p = 1331000000 = 1100**3 (n=[[2, 3], [5, 3]])
//        p=397, n=1331: n**3+(n**2)*p = 3061257408 = 1452**3 (n=[[11, 3]])
//        p=547, n=2197: n**3+(n**2)*p = 13244763896 = 2366**3 (n=[[13, 3]])
//        p=631, n=2744: n**3+(n**2)*p = 25412184000 = 2940**3 (n=[[2, 3], [7, 3]])
//        p=919, n=4913: n**3+(n**2)*p = 140770302408 = 5202**3 (n=[[17, 3]])
//        there are 11 primes below 1000 with a 'cube partner'

//
//        p=7, n=1
//        p=19, n=8
//        p=37, n=27
//        p=61, n=64
//        p=127, n=216
//        p=271, n=729
//        p=331, n=1000
//        p=397, n=1331
//        p=547, n=2197
//        p=631, n=2744
//        p=919, n=4913
//        found 11 prime/cube partners below 1,000,000 (c_max=10,000)

//        p=3571, n=39304
//        p=4219, n=50653
//        p=4447, n=54872
//        p=5167, n=68921
//        p=5419, n=74088
//        p=6211, n=91125
//        found 24 prime/cube partners below 1,000,000 (c_max=100,000)

    }

}
