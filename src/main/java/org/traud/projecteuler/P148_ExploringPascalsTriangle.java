package org.traud.projecteuler;

import java.util.*;

/**
 *
 */
public class P148_ExploringPascalsTriangle {

    private static final boolean VERBOSE = false;

    public static int[] getPascalTriangleRowMod7(int[] prevRow) {
        if (prevRow == null || prevRow.length == 0)
            return new int[] { 1 };

        int N = prevRow.length + 1;
        int[] row = new int[N];
        row[0] = row[N-1] = 1;
        for (int i = 1; i < row.length-1; ++i) {
            int sum = prevRow[i - 1] + prevRow[i];
            if (sum >= 7)
                sum -= 7;
            row[i] = sum;
        }
        return row;
    }

    private static String rowString(int[] row) {
        return row == null ? "null" : Arrays.toString(row).replaceAll(", ", "").substring(1).substring(0, row.length);
    }

    private static long getDivisibleCountSlow(int m) {
        String mBase7 = Integer.toString(m, 7);

        int cnt = 0;
        for (int n = 0; n <= m; ++n) {
            String nBase7 = Integer.toString(n, 7);
            while (nBase7.length() < mBase7.length())
                nBase7 = "0" + nBase7;

            for (int i = 0; i < nBase7.length(); ++i) {
                if (nBase7.charAt(i) > mBase7.charAt(i)) {
                    cnt++;
                    break;
                }
            }
        }
        return cnt;
    }


    private static long getDivisibleCount(int m) {
        String base7 = Integer.toString(m, 7);
        if (base7.length() < 2)
            return 0;

        long sum = 1;
        int smallestDigit = 7;
        int firstDigit = base7.charAt(0) - '0';
        for (int i = base7.length()-1; i > 0; --i) {
            int digit = base7.charAt(i) - '0';

            if (digit < smallestDigit)
                smallestDigit = digit;
        }


        return firstDigit * (6 - smallestDigit) * (int)Math.pow(7, base7.length()-2);
    }


    static class ExponentInfo implements Comparable<ExponentInfo> {
        int digit;
        int exponent;

        public ExponentInfo(int digit, int exponent) {
            this.digit = digit;
            this.exponent = exponent;
        }

        @Override
        public int compareTo(ExponentInfo o) {
            return -Integer.compare(digit, o.digit);
        }

        public int headRoom() {
            return 6-digit;
        }
    }
    private static long getDivisibleCount2(int m) {
        String base7 = Integer.toString(m, 7);
        if (base7.length() < 2)
            return 0;

        int exp = 0;
        List<ExponentInfo> ei = new ArrayList<>();
        int powSum = 1;
        for (int i = base7.length()-1; i > 0; --i) {
            ei.add(new ExponentInfo(base7.charAt(i) - '0', exp));
            exp++;
            if (i > 1)
                powSum *= 7;
        }
        Collections.sort(ei);
        ExponentInfo last = null;
        long sum = 0;
        System.out.printf("m=%d (%s)\n", m, base7);

        for (ExponentInfo e : ei) {
            System.out.printf("  Exp: %d, digit: %d, head room: %d\n", e.exponent, e.digit, e.headRoom());

            int headRoom = last != null ? e.headRoom() - last.headRoom() : e.headRoom();
            int entries = headRoom * powSum;

            sum += entries;
        }

        int firstDigit = base7.charAt(0) - '0';

        return firstDigit * sum;
    }

    public static void main(String[] args) {

        int[] row = null;
        long totalNonDivisibleCount = 0;
        long totalNonDivisibleCount2 = 0;
        long totalCount = 0;
        long lastPrint = 0;
        int COUNT_ROWS = 200;//1_000_000_000;

        // Idea:
        // Does Lucas's theorem help? See https://en.wikipedia.org/wiki/Lucas%27s_theorem
        //
        // >>
        //   A binomial coefficient ( m over n ) is divisible by a prime p if and only if at least one of the base p
        //   digits of n is greater than the corresponding digit of m.
        // <<

        for (int m = 0; m <= COUNT_ROWS; ++m) {

            String base7 = Integer.toString(m, 7);
            long divisble2 = getDivisibleCountSlow(m);
            row = COUNT_ROWS < 1000 ? getPascalTriangleRowMod7(row) : null;
            long divisibleBySeven = 0;
            if (row != null)
            for (long val : row) {
                if (val == 0)
                    divisibleBySeven++;
                totalCount++;
            }
            totalNonDivisibleCount += m - divisibleBySeven;
            totalNonDivisibleCount2 += m - divisble2;

            if (m <= 100 || System.currentTimeMillis() - lastPrint > 2000) {
                System.out.printf("%s: %2d: %3d divisible by 7 (%3d? / %3d?)\ttotal: %,d\tbase7: %s\t(%4.2f%% so far)\t%s\n",
                        new Date(), m,
                        divisibleBySeven,
                        divisble2, getDivisibleCount(m),
                        totalNonDivisibleCount,
                        base7,
                        m*100D/COUNT_ROWS,
                        m <= 100 ? rowString(row) : "");
                lastPrint = System.currentTimeMillis();
            }
//            if (divisble2 != divisibleBySeven)
//                throw new IllegalArgumentException("m=" + m + ":  " + divisble2 + " != " + divisibleBySeven+ "  m(base7)=" + Integer.toString(m, 7));
        }
        System.out.printf("total count: %,d out of %,d not divisible by 7.\n", totalNonDivisibleCount, totalCount);
        System.out.printf("total count: %,d out of %,d not divisible by 7.\n", totalNonDivisibleCount2, totalCount);

//        Random rnd = new Random(12345);
//        for (int i = 0; i < 100; ++i) {
//            int m = new int[] {77, 53, 77}[i % 2]; //7+rnd.nextInt(49);
//
//            long d1 = getDivisibleCountSlow(m);
//            long d3 = getDivisibleCount(m);
//            long d2 = getDivisibleCount2(m);
//            if (d1 != d2)
//                throw new IllegalArgumentException("m=" + m + " ("+Integer.toString(m,7)+"):  expected " + d1 + " != " + d2 + " actual m(base7)=" + Integer.toString(m, 7));
//            System.out.println("OK: m=" + m + ":  " + d1 + " == " + d2 + "  m(base7)=" + Integer.toString(m, 7));
//        }


        int[] test = new int[] {77, 53,
            Integer.parseInt("102",7),
            Integer.parseInt("1031",7),
            Integer.parseInt("2031",7),
        };
        for (int m : test) {
            String m7 = Integer.toString(m, 7);
            int cnt = 0;
            P215_CrackFreeWalls.Counter<Character> ctr = new P215_CrackFreeWalls.Counter<>();

            for (int n = 0; n <= m; ++n) {
                String n7 = Integer.toString(n, 7);
                while (n7.length() < m7.length()) n7 = "0" + n7;
                String gChar = charGreater(m7, n7);
                System.out.printf("%s %s  ", n7, gChar);
                if (!".".equals(gChar))
                    cnt++;
                ctr.inc(Character.valueOf(gChar.charAt(0)));
                if (n7.endsWith("6")) {
                    System.out.printf("\tcnt=%s\n", ctr.toString());
                    cnt = 0;
                }
            }
            System.out.printf("\n\n");
        }
    }

    private static String charGreater(String m7, String n7) {
        for (int i = 0; i < m7.length(); ++i)
        {
            if (n7.charAt(i) > m7.charAt(i))
                return ((char)(i+'u'))+"";
        }
        return ".";
    }


}
