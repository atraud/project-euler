package org.traud.projecteuler;

/**
 * Created by traud on 4/10/2017.
 *
 * A k-input binary truth table is a map from k input bits (binary digits,
 * 0 [false] or 1 [true]) to 1 output bit.
 * For example, the 2-input binary truth tables for the logical AND and XOR functions are:
 *
 *
 */
public class P209_CircularLogic {

    static class TruthTable {
        private int[] s;

        TruthTable(int nInput) {
            s = new int[4];
        }

        public void next() {
            int c = 1;
            for (int i = 0; c > 0 && i < s.length; ++i) {
                s[i] += c;
                if (s[i] == 0x10000) {
                    s[i] = 0;
                }
                else {
                    c = 0;
                }
            }
        }
        public boolean hasNext() {
            return s[3] != 0xffff;
        }

        public String toString() {
            return String.format("%04x:%04x:%04x:%04x", s[3], s[2], s[1], s[0]);
        }
    }

    public static void main(String[] args) {

        TruthTable tt = new TruthTable(6);
        int i = 0;
        while (tt.hasNext()) {

            if ((i & 0xffffff) == 0)
                System.out.printf("%s\n", tt.toString());

            tt.next();
            ++i;
        }

    }
}
