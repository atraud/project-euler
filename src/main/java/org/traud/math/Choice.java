package org.traud.math;

import java.util.Arrays;

/**
 * Created by traud on 4/13/2017.
 */
public class Choice {
    private int[] comb;
    private final int k;
    private final int n;
    private boolean hasNext;
    public Choice(int n, int k) {
        // see https://compprog.wordpress.com/2007/10/17/generating-combinations-1/
        this.k = k;
        this.n = n;

        if (k < 0 || n < 0)
            throw new IllegalArgumentException("k="+k+" and n=" + n + " must be non-negative!");
        if (k > n)
            throw new IllegalArgumentException("k="+k+" must be <= n=" + n);
        // TODO: check validity of n and k
        hasNext = true;
    }


    /*
    next_comb(int comb[], int k, int n)
        Generates the next combination of n elements as k after comb

    comb => the previous combination ( use (0, 1, 2, ..., k) for first)
    k => the size of the subsets to generate
    n => the size of the original set

    Returns: 1 if a valid combination was found
        0, otherwise
    */
    public static boolean next_comb(int comb[], int k, int n) {
        int i = k - 1;
        ++comb[i];
        while ((i >= 0) && (comb[i] >= n - k + 1 + i)) {
            --i;
            if (i >= 0)
                ++comb[i];
        }

        if (comb[0] > n - k)  /* Combination (n-k, n-k+1, ..., n) reached */
            return false;     /* No more combinations can be generated */

        /* comb now looks like (..., x, n, n, n, ..., n).
        Turn it into (..., x, x + 1, x + 2, ...) */
        for (i = i + 1; i < k; ++i)
            comb[i] = comb[i - 1] + 1;

        return true;
    }

    public boolean hasNext() {
        return hasNext;

    }
    public void next() {
        if (comb == null) {
            this.comb = new int[k];
            for (int i = 0; i < k; ++i)
                comb[i] = i;
            hasNext = n != k;
        }
        else {
            hasNext = next_comb(comb, k, n);
        }
    }

    public int[] getChoice() {
        return comb;
    }

    public static void main(String... args) {
        Choice c = new Choice(100, 4);
        long cnt = 0;
        while (c.hasNext()) {
            c.next();
            int[] p = c.getChoice();
            if ((cnt & 0xffff) == 0)
            System.out.printf("%,d: %s\n", cnt, Arrays.toString(p));
            cnt++;
        }
        System.out.printf("%,d: %s\n", cnt, Arrays.toString(c.getChoice()));

    }

}
