package org.traud.projecteuler;

import org.traud.math.factor.FactorAlgorithm;
import org.traud.math.factor.PrimeFactorizationFactory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by traud on 4/16/2017.
 */
public class P221_AlexandrianIntegers {
    private static FactorAlgorithm pqs = PrimeFactorizationFactory.getPrimeFactorization();

    static class LimitedString {
        private final long[] a;
        private final long[] limit;

        public LimitedString(int n) {
            this.a = new long[n];
            a[0]=-1;
            this.limit = new long[n];
        }

        public void setLimit(int index, long limit) {
            this.limit[index] = limit;
        }

        public long getValue(int index) {
            return a[index];
        }

        public boolean hasNext() {
            for (int i = 0; i < a.length; ++i)
                if (a[i] < limit[i])
                    return true;
            return false;
        }

        public void next() {
            int i = 0;
            int c = 1;
            while (c != 0 && i < a.length) {
                if (a[i] < limit[i]) {
                    a[i] += c;
                    c = 0;
                }
                else {
                    a[i] = 0;
                    ++i;
                }
            }
        }

        public String toString() {
            return "LS" + Arrays.toString(a);
        }
    }
    public static boolean isAlexandrian(long A) {
        if (A <= 1)
            return false;

        long[][] factor = pqs.factor(A);
        LimitedString ls = new LimitedString(factor.length);
        for (int i = 0; i < factor.length; ++i)
            ls.setLimit(i, factor[i][1]);
        System.out.printf("A=%d -> %s\n", A, Arrays.deepToString(factor));
        while (ls.hasNext()) {
            ls.next();
            System.out.printf("  %s\n", ls);
        }
        return false;
    }

    public static void main(String... args) {

        //  1      1     1     1     q + p     1      r*(q+p) + pq       rp + rq + pq
        // --- =  --- + --- + --- = ------- + --- =  ---------------- = ---------------
        //  A      p     q     r      pq       r           pqr                pqr

        Set<Long> As = new HashSet<>();
        int N = 100;
        for (long p = -N; p <= N; ++p) {
            for (long q = -N; q <= p; ++q) {
                for (long r = -N; r <= q; ++r) {
                    if (r==0 || p==0 || q==0)
                        continue;

                    long nom = r*p*q;
                    long den = r*p + r*q + p*q;

                    if (den != 0 && nom % den==0 && (nom^den)>=0) {
                        long A = nom/den;

                        if (A==nom) {
                            As.add(A);
                            System.out.printf("A=%d (p=%d, q=%d, r=%d)\n", A, p, q, r);
                        }
                    }
                }
            }
        }
        Long[] aa = As.toArray(new Long[As.size()]);
        Arrays.sort(aa);
        System.out.printf("============\n");
        System.out.printf("A(%d)=%s\n", aa.length, Arrays.toString(aa));

        for (long a = 1; a < 100; ++a) {
            if (isAlexandrian(a))
                System.out.printf("A=%dn", a);
        }
    }
}
