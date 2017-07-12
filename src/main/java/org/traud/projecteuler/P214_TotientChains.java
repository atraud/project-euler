package org.traud.projecteuler;

import org.traud.math.Euler;
import org.traud.math.factor.PrimeSieve;
import org.traud.math.factor.PrimeSieveIterator;
import org.traud.util.misc.Timer;

import java.math.BigInteger;

/**
 * Created by traud on 4/8/2017.
 *
 *
 * Let φ be Euler's totient function, i.e. for a natural number n, φ(n) is the number of k, 1 ≤ k ≤ n,
 * for which gcd(k,n) = 1.
 *
 * By iterating φ, each positive integer generates a decreasing chain of numbers ending in 1.
 * E.g. if we start with 5 the sequence 5,4,2,1 is generated.
 * Here is a listing of all chains with length 4:
 *
 *  5,4,2,1
 *  7,6,2,1
 *  8,4,2,1
 *  9,6,2,1
 *  10,4,2,1
 *  12,4,2,1
 *  14,6,2,1
 *  18,6,2,1
 *
 *  Only two of these chains start with a prime, their sum is 12.
 *
 *  What is the sum of all primes less than 40,000,000 which generate a chain of length 25?
 *
 */
public class P214_TotientChains {

    private static int generateChain(int chainLength, StringBuilder sb, long p) {
        long q = p;
        sb.setLength(0);
        sb.append(String.format("%,d",p));
        int len = 1;
        while (q != 1) {
            q = Euler.totient(q);
            sb.append("  ").append(String.format("%,d",q));
            len++;

            // not interested in longer chains anyway
            if (len > chainLength) {
                sb.append("...");
                break;
            }
        }
        sb.append(" -> ").append(len);
        return len;
    }


    public static void main(String[] args) {
        int N = 40_000_000;
        int desiredChainLength = 25;

//        N = 19;
//        chainLength = 4;
        PrimeSieve pf = new PrimeSieve(N+100);
        PrimeSieveIterator it = pf.iterator();

        Timer timer = new Timer();
        StringBuilder sb = new StringBuilder(200);
        BigInteger sumb = BigInteger.valueOf(0);
        long sum = 0;
        long cnt = 0;

        while (it.hasNext()) {
            long p = it.next();
            if (p >= N)
                break;

            int len = generateChain(desiredChainLength, sb, p);

            if (len == desiredChainLength) {
                sum += p;
                sumb = sumb.add(BigInteger.valueOf(p));

                if ((cnt & 0xfff)==0 || p>=39_999_000)
                    System.out.println("s="+sum+"  " + sb.toString());

                cnt++;
            }
        } // while (it.hasNext())

        System.out.println("----");
        System.out.println("s="+sum+"  " + sb.toString());

        System.out.printf("finished in %1.2fs.\n", timer.getElapsedMillis()/1000.0);
        System.out.printf("sum of primes (%d) that generate totient chains of length %,d is: %d\n", cnt, desiredChainLength, sum);
        System.out.printf("sum of primes (%d) that generate totient chains of length %,d is: %s\n", cnt, desiredChainLength, sumb);

        // sum of primes that generate totient chains of length 25 is: 1,839,540,823,783
        //

        // finished in 91.21s.
        // sum of primes (57009) that generate totient chains of length 25 is: 1839540823783
        // sum of primes (57009) that generate totient chains of length 25 is: 1839540823783
    }

}
