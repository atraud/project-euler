package org.traud.projecteuler;

import org.apache.commons.math.fraction.BigFraction;

import java.util.ArrayList;

/**
 * Created by traud on 7/3/2017.
 */
public class P152_WritingOneHalf {

    private static BigFraction HALF = new BigFraction (1, 2);

    private static void writeOneHalf(int low, int high) {
        BigFraction sum = new BigFraction(0);

        for (int q = low; q <= high; ++q) {
            writeOneHalfAux(sum, new ArrayList<Integer>(), q+1, high);
        }
    }

    private static void writeOneHalfAux(BigFraction sum, ArrayList<Integer> sq, int low, int high) {

        for (int q = low; q <= high; ++q) {
            BigFraction sum1 = sum.add(new BigFraction(1, low*low));

            if (sum1.compareTo(HALF) > 0)
                continue;


            ArrayList<Integer> sq1 = new ArrayList<>(sq);
            sq1.add(q);

            if (sum1.compareTo(HALF) == 0) {
                System.out.printf("1/2: %s\n", sq1);
            }
            else {
//                System.out.printf("- %s\n", sq1);
            }
            writeOneHalfAux(sum1, sq1, q+1, high);

        }
    }

    public static void main(String... args) {
        writeOneHalf(1, 45);
    }
}
