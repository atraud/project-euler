package org.traud.projecteuler;

import org.traud.math.factor.PrimeSieve;
import org.traud.math.factor.PrimeSieveIterator;
import org.traud.math.factor.Primes;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by traud on 4/10/2017.
 *
 * Let S = {2, 3, 5, ..., 4999} be the set of prime numbers less than 5000.
 *
 * Find the number of subsets of S, the sum of whose elements is a prime number.
 * Enter the rightmost 16 digits as your answer.
 */
public class P249_PrimeSubsetSums {

    public static void main(String[] args) {
        int N = 5000;
        PrimeSieve ps = new PrimeSieve(N);
        PrimeSieveIterator it = ps.iterator();

        StringBuilder sb = new StringBuilder();
        List<Long> S = new ArrayList<>();
        while (it.hasNext()) {
            long p = it.next();
            sb.append(p).append(' ');
            if (sb.length()>120) {
                System.out.println(sb.toString());
                sb.setLength(0);
            }
            S.add(p);
        }
        System.out.println(sb.toString());
        System.out.printf("found %d primes below %d\n", S.size(), N);

        enumPrimeSubSets(Arrays.asList(2L), S, 1, 2);
        enumPrimeSubSets(Arrays.asList(3L, 5L), S, 3, 3);
        enumPrimeSubSets(Arrays.asList(3L, 5L, 7L, 11L), S, 5, 5);
    }

    private static long enumPrimeSubSets(List<Long> given, List<Long> s, int startIdx, int setSize) {
        long totalSum = 0;
        long givenSum = 0;
        for (Long p : given) {
            givenSum += p;
        }
        for (int i = startIdx; i < s.size(); ++i) {
            long iSum = givenSum + s.get(i);
            if (Primes.isPrime(iSum))
                totalSum += iSum;
        }

        System.out.printf("total sum for sets of size %d containing %s: %,d\n", setSize, given, totalSum);
        return totalSum;
    }
}
