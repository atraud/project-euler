package org.traud.math;

import java.util.Arrays;

/**
 * Created by traud on 4/17/2017.
 */
public class ArrayUtils {
    public static long[][] clone(long[][] v) {
        long[][] r = new long[v.length][];
        for (int i = 0; i < r.length; ++i)
            r[i] = Arrays.copyOf(v[i], v[i].length);
        return r;
    }

}
