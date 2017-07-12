package org.traud.projecteuler;

import org.traud.util.misc.Timer;

import java.math.BigInteger;

/**
 * Created by traud on 4/11/2017.
 *
 *

 In the hexadecimal number system numbers are represented using 16 different digits:
 0,1,2,3,4,5,6,7,8,9,A,B,C,D,E,F

 The hexadecimal number AF when written in the decimal number system equals 10x16+15=175.

 In the 3-digit hexadecimal numbers 10A, 1A0, A10, and A01 the digits 0,1 and A are all present.
 Like numbers written in base ten we write hexadecimal numbers without leading zeroes.

 How many hexadecimal numbers containing at most sixteen hexadecimal digits exist with all of the digits 0,1, and A present at least once?
 Give your answer as a hexadecimal number.

 (A,B,C,D,E and F in upper case, without any leading or trailing code that marks the number as hexadecimal and without leading zeroes , e.g. 1A3F and not: 1a3f and not 0x1a3f and not $1A3F and not #1A3F and not 0000001A3F)

 */
public class P162_HexadecimalNumbers {

    public static long numberCount(int digits) {
        if (digits < 3)
            return 0;

//        if (digits == 3)
//            return 4;

        // more digits

        // permutations
        // A01 A10 10A 1A0 01A 0A1  (01A and 0A1 cannot appear as first digits)

        // P = {A01, A10, 10A, 1A0}
        // Q = {01A, 0A1}
        long P = 4;
        long Q = 2;
        long x = 16;
        long y = 15;
        // cases for 4 digits:
        // yPPP, PxPP, PPxP PPPx
        // yQQQ
//        if (digits == 4)
//            return y*P + 3*x*P + y*(Q-2);       // -2 as there are two possible overlaps of Q and P

        // total numbers with n digits:
        // 15 * 16^(n-1)
        long totalCnt = 15 * BigInteger.valueOf(16).pow(digits-1).longValue();

        // digits without 'a', '1' or '0'
        // 14 * 15^(n-1)
        long sub1 = 14 * BigInteger.valueOf(15).pow(digits-1).longValue();

        // digits without combination of 'a'/'1', 'a'/'0', '1'/'0'
        // 14 * 15^(n-1)
        long sub2 = 13 * BigInteger.valueOf(14).pow(digits-1).longValue();


        return -1; //totalCnt - 3*sub1 - 3*sub2;
    }


    /**
     * returns the number of hexadecimal numbers with n digits and
     * the specified hexadecimal digit.
     *
     * @param n
     * @param digit
     * @return
     */
    public static long numberWithDigitCount(int n, int digit) {
        if (n == 1)
            return 1;

        if (digit != 0) {
            long totalCnt = 15*BigInteger.valueOf(16).pow(n-1).longValue();
            long noCnt = 14*BigInteger.valueOf(15).pow(n-1).longValue();
            return totalCnt - noCnt;
        }
        else {
            long totalCnt = 15*BigInteger.valueOf(16).pow(n-1).longValue();
            long noCnt = 15*BigInteger.valueOf(15).pow(n-1).longValue();
            return totalCnt - noCnt;
        }
    }

    private static boolean contains(StringBuilder sb, char a) {
        for (int i = 0; i < sb.length(); ++i)
            if (sb.charAt(i) == a)
                return true;
        return false;
    }

    static final char[] hexChars = "0123456789abcdef".toCharArray();

    private static void toHexStringRev(long l, StringBuilder sb) {
        sb.setLength(0);

        if (l == 0) {
            sb.append('0');
            return;
        }

        while (l > 0) {
            sb.append(hexChars[(int)(l & 0x0f)]);
            l >>>= 4;
        }
    }

    public static void main(String[] args) {

        Timer t = new Timer();
        long lastDigitCount = 1;
        StringBuilder sb = new StringBuilder();
        int digits = 2;
        int cntA01 = 0;
        int cntA= 0,cnt0=0,cnt1=0,cntA0=0,cntA1=0,cnt10=0;
        int total = 0;
        for (long l = 0x10; l <= 0x1_000_000; ++l) {
            toHexStringRev(l, sb);
            if (sb.length() != digits) {
                System.out.printf("%d digits: cnt = %8d (=%8d?), total=%8d, A=%8d, 0=%8d, 1=%8d  " +
                                "(%7d, %7d, %7d), A/0=%8d, A/1=%8d, 1/0=%8d\n",
                        digits, cntA01, numberCount(digits), total, cntA, cnt0, cnt1,
                        numberWithDigitCount(digits, 0xa), numberWithDigitCount(digits, 0), numberWithDigitCount(digits, 1),
                        cntA0, cntA1, cnt10);
                cntA01 = cntA = cnt0 = cnt1 = 0;
                cntA0 = cntA1 = cnt10 = 0;
                total = 0;

//                System.out.printf("%d digits: cnt = %d (=%d?), total=%d, 'A'=%d, '0'=%d, '1'=%d (%d, %d, %d)\n",
//                        digits, cnt, numberCount(digits), total, cntA, cnt0, cnt1,
//                        numberWithDigitCount(digits, 0xa), numberWithDigitCount(digits, 0), numberWithDigitCount(digits, 1));
                digits = sb.length();

            }
            if (contains(sb, 'a') && contains(sb, '0') && contains(sb, '1')) {
                cntA01++;
//                    if ((cnt & 0xfffff)==0)
//                        if (digits == 4)
//                        System.out.printf("%d: %s\n", cnt, hex);
            }
            if (contains(sb, 'a'))
                cntA++;
            if (contains(sb, '0'))
                cnt0++;
            if (contains(sb, '1'))
                cnt1++;
            if (contains(sb, 'a') && contains(sb, '0'))
                cntA0++;
            if (contains(sb, 'a') && contains(sb, '1'))
                cntA1++;
            if (contains(sb, '1') && contains(sb, '0'))
                cnt10++;
            total++;
        }
//        System.out.printf("%d digi<ts: cnt = %d (=%d?), *%d + %d, %1.2fs so far\n",
//                digits, cnt, numberCount(digits), cnt/lastDigitCount, cnt%lastDigitCount, t.getElapsedSeconds());

//        4 digits: cnt = 4 (=252?), *4 + 0, 0.00s so far
//        5 digits: cnt = 262 (=-1?), *65 + 2, 0.02s so far
//        6 digits: cnt = 10190 (=-1?), *38 + 234, 0.05s so far
//        7 digits: cnt = 309680 (=-1?), *30 + 3980, 0.58s so far
//        8 digits: cnt = 8133504 (=-1?), *26 + 81824, 8.39s so far

    }


}
