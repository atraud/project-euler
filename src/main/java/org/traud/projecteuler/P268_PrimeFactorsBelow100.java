package org.traud.projecteuler;

import org.traud.math.factor.PrimeSieve;
import org.traud.math.factor.PrimeSieveIterator;

import org.traud.math.Choice;
import org.traud.util.misc.Timer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by traud on 4/10/2017.
 *
 *
 * It can be verified that there are 23 positive integers less than 1000 that are divisible by at least four distinct
 * primes less than 100.
 *
 * Find how many positive integers less than 10^16 are divisible by at least four distinct primes less than 100.
 */
public class P268_PrimeFactorsBelow100 {

    public static void main(String[] args) {

        PrimeSieve ps = new PrimeSieve(100);
        PrimeSieveIterator i = ps.iterator();
        int cnt = 0;
        List<Long> primeFactorList = new ArrayList<>();
        while (i.hasNext()) {
            long p = i.next();
            System.out.printf("%d: %d\n",cnt, p);
            primeFactorList.add(p);
            cnt++;
        }
        long[] primeFactors = new long[primeFactorList.size()];
        for (int j= 0; j < primeFactorList.size(); ++j)
            primeFactors[j] = primeFactorList.get(j);

        Choice c = new Choice(primeFactors.length-1, 4);
        long MAX = 1000; //10_000_000_000_000_000L;
        long pfCount = 0;
        long totalSum = 0;
        while (c.hasNext()) {
            c.next();
            int[] choice = c.getChoice();

            long factor = 1;
            for (int j = 0; j < choice.length; ++j)
                factor *= primeFactors[choice[j]];

            if (factor <= MAX) {
                long factorCount = MAX / factor;
                System.out.printf("%,d: prime factor: %s -> %d, there are %d numbers below %,d (10^%d) with this factor\n",
                        pfCount, Arrays.toString(choice), factor, factorCount, MAX, (long) Math.log10(MAX));
                pfCount++;

                totalSum += factorCount;
            }
        }
        System.out.printf("total sum: %d\n", totalSum);
        Timer t = new Timer();
        System.out.printf("counting to %,d...\n", MAX);
        for (long l = 1; l < MAX; ++l)
        {

        }
        System.out.printf("counting to %,d took %1.2fs\n", MAX, t.getElapsedSeconds());

    }
}
