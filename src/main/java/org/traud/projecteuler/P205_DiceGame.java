package org.traud.projecteuler;

import org.traud.util.misc.Timer;


/**
 * Created by traud on 4/10/2017.
 *
 * Peter has nine four-sided (pyramidal) dice, each with faces numbered 1, 2, 3, 4.
 * Colin has six six-sided (cubic) dice, each with faces numbered 1, 2, 3, 4, 5, 6.
 *
 * Peter and Colin roll their dice and compare totals: the highest total wins. The result is a draw if the totals are equal.
 *
 * What is the probability that Pyramidal Pete beats Cubic Colin? Give your answer rounded to seven decimal places
 * in the form
 * 0.abcdefg
 *
 */
public class P205_DiceGame {

    public static double expectedValue(int sides, int count) {
        double e = 0;
        double prob = 1.0 / sides;
        for (int s = 1; s <= sides; ++s)
            e += prob * s;
        e *= count;
        return e;
    }

    static class Number {
        private final int base;
        private final int[] d;

        public Number(int width, int base) {
            this.d = new int[width];
            this.base = base;
            d[0] = -1;
        }

        public Number next() {
            int c = 1;
            for (int i = 0; c != 0 && i < d.length; ++i) {
                d[i] += c;
                if (d[i] == base)
                    d[i] = 0;
                else
                    c = 0;
            }
            return this;
        }

        public String toString() {
            StringBuilder sb=new StringBuilder(d.length);
            for (int i = d.length-1; i>=0; --i)
                sb.append(d[i]);
            return sb.toString();
        }

        public boolean hasNext() {
            for (int i = 0; i < d.length; ++i) {
                if (d[i] != base-1)
                    return true;
            }
            return false;
        }

        public int sum() {
            int s = 0;
            for (int i = 0; i < d.length; ++i)
                s+= d[i];
            return s;
        }
    }

    public static void solveComplicated() {
        Number pyramid = new Number(9,4);
        int cnt = 0;
        Timer t = new Timer();

        long pyrWins = 0;
        long diceWins = 0;
        long draws = 0;
        long total = 0;

        while (pyramid.hasNext()) {
            Number nextPyr = pyramid.next();
            //System.out.printf("%s\n", nextPyr);
            int sumPyr = nextPyr.sum();

            Number dice = new Number(6,6);
            while (dice.hasNext()) {
                Number nextDice = dice.next();
                //System.out.printf("%s\n", nextDice);

                int sumDice = nextDice.sum();

                if (sumDice == sumPyr)
                    draws++;
                else if (sumDice < sumPyr)
                    pyrWins++;
                else
                    diceWins++;

                total++;
            }

            cnt++;
            if ((cnt & 0xfff) == 0)
            {
                System.out.printf("%s : %s\ttotal: %,d, dice: %,d, pyr: %,d, draw: %,d\n",
                        nextPyr, dice, total, pyrWins, diceWins, draws);
            }
        }
        System.out.printf("finished in %1.2fs.\n", t.getElapsedSeconds());
        System.out.printf("%d total combinations, pyr: %d, draw: %d\n", total, pyrWins, diceWins, draws);
        System.out.printf("probability that Pyramidal Pete wins: %1.7f\n", ((double)pyrWins)/total);
        System.out.printf("probability that Pyramidal Pete wins: %s\n", "0.abcdefg");
    }


    private static long sum(long code, int base) {
        long sum = 0;
        while (code > 0) {
            sum += 1 + code % base;
            code /= base;
        }
        return sum;
    }

    public static void main(String[] args) {
        System.out.printf("E[4,4]=%1.8f\n", expectedValue(4, 9));
        System.out.printf("E[6,6]=%1.8f\n", expectedValue(6, 6));

        // P("Pete wins") = P(E[4,4] > E[6,6])


        int cnt = 0;
        Timer t = new Timer();

        long pyrWins = 0;
        long diceWins = 0;
        long draws = 0;
        long total = 0;

        long hiPyr = 4L * 4L * 4L * 4L * 4L * 4L * 4L * 4L * 4L;
        long hiDice = 6L * 6L * 6L * 6L * 6L * 6L;
        System.out.printf("%06x =?= %06x\n", 0x7ffff, hiPyr);
        for (long dice = 0; dice < hiDice; ++dice) {
            long sumDice = sum(dice, 6);

            for (long pyramid = 0; pyramid < hiPyr; ++pyramid) {
                //System.out.printf("%s\n", nextPyr);
                long sumPyr = sum(pyramid, 4);

                if (sumDice == sumPyr)
                    draws++;
                else if (sumDice < sumPyr)
                    pyrWins++;
                else
                    diceWins++;

                total++;
            }

            cnt++;
            if ((cnt & 0xfff) == 0)
            {
                System.out.printf("%d%% done. total: %,d, dice: %,d, pyr: %,d, draw: %,d\n",
                        dice*100/ (hiDice), total, pyrWins, diceWins, draws);
            }
        }
        System.out.printf("finished in %1.2fs.\n", t.getElapsedSeconds());
        System.out.printf("%d total combinations, pyr: %d, draw: %d\n", total, pyrWins, diceWins, draws);
        System.out.printf("probability that Pyramidal Pete wins: %1.7f\n", ((double)pyrWins)/total);
        System.out.printf("probability that Pyramidal Pete wins: %s\n", "0.abcdefg");
    }

}
