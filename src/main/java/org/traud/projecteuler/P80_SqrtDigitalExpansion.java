package org.traud.projecteuler;

import org.traud.util.misc.Timer;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import static java.math.BigDecimal.ROUND_HALF_UP;

/**
 * Created by traud on 4/13/2017.
 *
 * Recovery Key: 825475-EzZE58rQsrIJoXRf24ij3yAKVFbbCw7gzF0eGDW4
 * Generated: Thu, 13 Apr 2017, 21:51.49
 *
 */
public class P80_SqrtDigitalExpansion {

    static Map<Long, Long> squares = new HashMap<>();
    static {
        for (long n = 1; n <= 10_000; ++n) {
            squares.put(n * n, n);
        }
    }

    // see http://stackoverflow.com/questions/13649703/square-root-of-bigdecimal-in-java/18322177
    public static BigDecimal sqrt(long A, final int SCALE) {
        return sqrt(BigDecimal.valueOf(A), SCALE);
    }

    public static BigDecimal sqrt(BigDecimal A, final int SCALE) {
        final BigDecimal TWO = BigDecimal.valueOf(2);
        BigDecimal x0 = new BigDecimal("0");
        BigDecimal x1 = new BigDecimal(Math.sqrt(A.doubleValue()));
        while (!x0.equals(x1)) {
            x0 = x1;
            x1 = A.divide(x0, SCALE, ROUND_HALF_UP);
            x1 = x1.add(x0);
            x1 = x1.divide(TWO, SCALE, ROUND_HALF_UP);

        }
        return x1;
    }

    static boolean ROUNDING = false;
    static long digitSum(BigDecimal d, long prec) {
        String s = d.toString();
        long sum = 0;
        int cnt = 0;
        for (int i = 0; i < s.length(); ++i) {
            if (s.charAt(i) == '.') {
                if (ROUNDING) {
                sum = 0;
                cnt = 0;
                }
                continue;
            }
            sum += s.charAt(i) - '0';
            cnt++;
            if (cnt == prec) {
                if (ROUNDING && i+1 < s.length())
                {
                    int next = s.charAt(i + 1) - '0';
                    if (next >= 5)
                        sum++;
                }
                return sum;
            }
        }
        return sum;
    }

    private static String indexString(int s, int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = s; i <= n; ++i) {
            if (i == s)
                sb.append(s);
            else if (i % 10 == 0)
                sb.append('0');
            else if (i % 10 == 5)
                sb.append('+');
            else
                sb.append('-');

        }
        return sb.toString();
    }
    public static void main(String... args) {
        int cnt=0;
        BigInteger largestX = BigInteger.ZERO;
        long largestD = 0;
//        ContinuedFraction.DEBUG= false;
        long totalDigitSum = 0;
        System.out.printf("D=%2d:  sqrt = a.%s\n", 0, indexString(2, 100));

        for (long D = 1; D <= 100; ++D) {
            if (squares.containsKey(D))
                continue;

            Timer t = new Timer();
            // long[] xy = bruteForcePellEquation(D);

            BigDecimal sq = sqrt(D, 102);
            long ds = digitSum(sq, 100);
            System.out.printf("D=%2d:  sqrt = %s, digitSum=%d\n", D, sq, ds);
            totalDigitSum += ds;
        }
        System.out.printf("total digit sum is %d\n", totalDigitSum);

//        1.414213562373095048801688724209698078569671875376948073176679737990732478462107038850387534327641573
//
//        D= 0:  sqrt = a.1---+----0----+----0----+----0----+----0----+----0----+----0----+----0----+----0----+----0----+----0
//        D= 2:  sqrt = 1.414213562373095048801688724209698078569671875376948073176679737990732478462107038850387534327641572735, digitSum=481
//        ...
//
//        D=99:  sqrt = 9.949874371066199547344798210012060051781265636768060791176046438349453927827131540126530197384871952721, digitSum=446
//        total digit sum is 40886

    }


}
