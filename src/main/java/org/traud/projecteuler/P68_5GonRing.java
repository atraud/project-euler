package org.traud.projecteuler;

import org.traud.math.Permutation;

import java.io.IOException;
import java.util.*;

/**
 * Created by traud on 10/1/2017.
 */
public class P68_5GonRing {

    //          0
    //
    //            [1]       3
    //
    //      [8]         [2]
    //
    //   9     [6]   [4]    5
    //
    //            7
    private static boolean isValidSolution5Gon(int[] next, int total) {
        //
        int s1 = next[0] + next[1] + next[2];
        int s2 = next[3] + next[2] + next[4];
        int s3 = next[5] + next[4] + next[6];
        int s4 = next[7] + next[6] + next[8];
        int s5 = next[9] + next[8] + next[1];

        return     s1 == total
                && s2 == total
                && s3 == total
                && s4 == total
                && s5 == total;
    }

    private static boolean isValidSolution3Gon(int[] next, int total) {
        //
        int s1 = next[0] + next[1] + next[2];
        int s2 = next[3] + next[2] + next[4];
        int s3 = next[5] + next[4] + next[1];
        return s1 == total && s2 == total && s3 == total;
    }

    private static String solString3Gon(int[] next) {
        //
        int[][] a  = new int[][] {
            {next[0], next[1], next[2]},
            {next[3], next[2], next[4]},
            {next[5], next[4], next[1]},
        };

        int n = 3;
        int i = 0;
        for (int j = 0; j < n; ++j) {
            if (a[j][0] < a[i][0])
                i = j;
        }

        return String.format("%d,%d,%d;%d,%d,%d;%d,%d,%d",
            a[      i][0], a[      i][1], a[      i][2],
            a[(i+1)%n][0], a[(i+1)%n][1], a[(i+1)%n][2],
            a[(i+2)%n][0], a[(i+2)%n][1], a[(i+2)%n][2]
        );
    }

    private static String solString5Gon(int[] next) {
        //
        int[][] a  = new int[][] {
                {next[0], next[1], next[2]},
                {next[3], next[2], next[4]},
                {next[5], next[4], next[6]},
                {next[7], next[6], next[8]},
                {next[9], next[8], next[1]},
        };

        int n = 5;
        int i = 0;
        for (int j = 0; j < n; ++j) {
            if (a[j][0] < a[i][0])
                i = j;
        }

        return String.format("%d,%d,%d;%d,%d,%d;%d,%d,%d;%d,%d,%d;%d,%d,%d",
                a[      i][0], a[      i][1], a[      i][2],
                a[(i+1)%n][0], a[(i+1)%n][1], a[(i+1)%n][2],
                a[(i+2)%n][0], a[(i+2)%n][1], a[(i+2)%n][2],
                a[(i+3)%n][0], a[(i+3)%n][1], a[(i+3)%n][2],
                a[(i+4)%n][0], a[(i+4)%n][1], a[(i+4)%n][2]
            );
    }

    private static long findSolution3Gon(int total) {
        long max = 0;
        Permutation p = new Permutation(6);
        Set<String> solutions = new HashSet<>();
        while (p.hasNext()) {
            int[] q = p.next();
            int[] next = Arrays.copyOf(q, q.length);
            for (int i = 0; i < next.length; ++i)
                next[i]++;
            if (isValidSolution3Gon(next, total)) {
                String s = solString3Gon(next);
                if (solutions.add(s)) {
                    System.out.printf("%s\n", s);
                    long val = Long.parseLong(s.replaceAll(",", "").replaceAll(";", ""));
                    if (val > max)
                        max = val;
                }
            }
        }
        System.out.printf("-----------\n");
        return max;
    }


    private static long findSolution5Gon(int total) {
        long max = 0;
        Permutation p = new Permutation(10);
        Set<String> solutions = new HashSet<>();
        while (p.hasNext()) {
            int[] q = p.next();
            int[] next = Arrays.copyOf(q, q.length);
            for (int i = 0; i < next.length; ++i)
                next[i]++;

            if (isValidSolution5Gon(next, total)) {
                String s = solString5Gon(next);
                if (solutions.add(s)) {
                    String valStr = s.replaceAll(",", "").replaceAll(";", "");
                    long val = Long.parseLong(valStr);
                    System.out.printf("%s\t%,d\n", s, val);
                    if (valStr.length() == 16 && val > max)
                        max = val;

                }
            }
        }
        System.out.printf("-----------\n");
        return max;
    }

    public static void main(String[] args) throws IOException {

        // https://projecteuler.net/problem=68

        //        Congratulations, the answer you gave to problem 68 is correct.
        //
        // maximum solution is 6531031914842725
        //        Answer:
        //        6531031914842725
        //        Completed on Tue, 3 Oct 2017, 23:00

        long max = 0;
        for (int total = 9; total < 13; ++total)
            max = Math.max(max, findSolution3Gon(total));
        System.out.printf("maximum solution is %d\n", max);
        System.out.printf("===========\n");
        max = 0;
        for (int total = 9; total < 19; ++total)
            max = Math.max(max, findSolution5Gon(total));
        System.out.printf("maximum solution is %d\n", max);

    }



}
