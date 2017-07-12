package org.traud.projecteuler;

import org.traud.util.misc.Timer;

import java.math.BigInteger;

/**
 * Created by traud on 4/8/2017.
 */
public class P217_BalancedNumbers {

    static int[] digits = new int[60];

    public static boolean isBalanced(long n) {
        int i = 0, j;
        while (n > 0) {
            digits[i++] = (int)(n % 10);
            n /= 10;
        }
        int length = i;
        //String s = Long.toString(n);
        i = 0;
        j = length-1;
        long s1=0,s2=0;
        do {
            s1 += digits[i];
            s2 += digits[j];
            ++i;
            --j;
        } while (i < j);
        return s1==s2;
    }

    public static long balancedCount(long digitCount) {
        if (digitCount==1)
            return 9;
        long h = digitCount/2;
        // h digit prefix, starting with a non-zero digit
        // opionall infix (doesn't matter for balanced numbers)
        // h digit suffix

        // thus
        // n(h) * infixCount * h!
        long infixCnt = ((digitCount & 1L) == 0 ? 1 : 10);
        long numCnt = n(h);
        long faculty = fac(h);
        return numCnt * infixCnt * faculty;
    }

    private static long n(long h) {
        // e.g. h==2
        // i.e. 2 digit numbers
        // 10 - 99: 100 - 10 = 90
        return (long)(9*Math.pow(10, h-1));
    }

    private static long fac(long n) {
        long f = 1;
        for (int i = 2; i <= n; ++i)
            f *= i;
        return f;
    }

    public static void main(String[] args) {
        Timer t = new Timer();
        long sum = 0;
        int exp=12;
        int cnt = 0;
        long sumPerOrderOfMagnitude = 0;
        long lastSumPerOrderOfMagnitude = 1;
        long mod = BigInteger.valueOf(3).pow(15).longValue();
        System.out.printf("%s\n", BigInteger.valueOf(3).pow(15));
        System.out.printf("%s\n", BigInteger.valueOf(2).pow(30));

        for (long i = 1; i < Math.pow(10, exp); ++i) {
            if (isBalanced(i)) {
                cnt++;
                //System.out.printf("%d: %d\n", cnt, i);
                sum += i;
                sumPerOrderOfMagnitude = (sumPerOrderOfMagnitude + i) % mod;
            }
            if (Long.toString(i+1).length() > Long.toString(i).length() ) {
                long divider = sumPerOrderOfMagnitude / lastSumPerOrderOfMagnitude;
                long remainder = sumPerOrderOfMagnitude % lastSumPerOrderOfMagnitude;
                System.out.printf("sum for values <%,d: %s (in base 3: %s: factors: %,d, %,d)\n", i+1, sumPerOrderOfMagnitude,
                        Long.toString(sumPerOrderOfMagnitude,3 ), divider, remainder);
                lastSumPerOrderOfMagnitude = sumPerOrderOfMagnitude;
                //sumPerOrderOfMagnitude = BigInteger.ZERO;
                cnt = 0;
            }


        }

//        long sum2 = 0;
//        for (int i = 1; i <= exp; ++i) {
//            long bc = balancedCount(i);
//            System.out.printf("balanced primeCount for %d digit numbers: %d\n", i, bc);
//            sum2 += bc;
//        }
        System.out.printf("sum is %d, time: %1.2fs.\n", sum, t.getElapsedSeconds());
//        System.out.printf("sum2 is %d\n", sum2);
        System.out.printf("mod is %s\n", mod);


//        "C:\Program Files\Java\jdk1.8.0_121\bin\java" -Didea.launcher.port=7532 "-Didea.launcher.bin.path=C:\Program Files (x86)\JetBrains\IntelliJ IDEA Community Edition 2016.3.3\bin" -Dfile.encoding=UTF-8 -classpath "C:\Program Files\Java\jdk1.8.0_121\jre\lib\charsets.jar;C:\Program Files\Java\jdk1.8.0_121\jre\lib\deploy.jar;C:\Program Files\Java\jdk1.8.0_121\jre\lib\ext\access-bridge-64.jar;C:\Program Files\Java\jdk1.8.0_121\jre\lib\ext\cldrdata.jar;C:\Program Files\Java\jdk1.8.0_121\jre\lib\ext\dnsns.jar;C:\Program Files\Java\jdk1.8.0_121\jre\lib\ext\jaccess.jar;C:\Program Files\Java\jdk1.8.0_121\jre\lib\ext\jfxrt.jar;C:\Program Files\Java\jdk1.8.0_121\jre\lib\ext\localedata.jar;C:\Program Files\Java\jdk1.8.0_121\jre\lib\ext\nashorn.jar;C:\Program Files\Java\jdk1.8.0_121\jre\lib\ext\sunec.jar;C:\Program Files\Java\jdk1.8.0_121\jre\lib\ext\sunjce_provider.jar;C:\Program Files\Java\jdk1.8.0_121\jre\lib\ext\sunmscapi.jar;C:\Program Files\Java\jdk1.8.0_121\jre\lib\ext\sunpkcs11.jar;C:\Program Files\Java\jdk1.8.0_121\jre\lib\ext\zipfs.jar;C:\Program Files\Java\jdk1.8.0_121\jre\lib\javaws.jar;C:\Program Files\Java\jdk1.8.0_121\jre\lib\jce.jar;C:\Program Files\Java\jdk1.8.0_121\jre\lib\jfr.jar;C:\Program Files\Java\jdk1.8.0_121\jre\lib\jfxswt.jar;C:\Program Files\Java\jdk1.8.0_121\jre\lib\jsse.jar;C:\Program Files\Java\jdk1.8.0_121\jre\lib\management-agent.jar;C:\Program Files\Java\jdk1.8.0_121\jre\lib\plugin.jar;C:\Program Files\Java\jdk1.8.0_121\jre\lib\resources.jar;C:\Program Files\Java\jdk1.8.0_121\jre\lib\rt.jar;C:\Users\traud\git\private\MathPlayground\target\classes;C:\Users\traud\.m2\repository\org\apache\lucene\lucene-core\5.3.1\lucene-core-5.3.1.jar;C:\Users\traud\.m2\repository\log4j\log4j\1.2.16\log4j-1.2.16.jar;C:\Users\traud\.m2\repository\junit\junit\4.10\junit-4.10.jar;C:\Users\traud\.m2\repository\org\hamcrest\hamcrest-core\1.1\hamcrest-core-1.1.jar;C:\Users\traud\.m2\repository\org\apache\commons\commons-math\2.1\commons-math-2.1.jar;C:\Program Files (x86)\JetBrains\IntelliJ IDEA Community Edition 2016.3.3\lib\idea_rt.jar" com.intellij.rt.execution.application.AppMain org.traud.projecteuler.P217_BalancedNumbers
//        sum for values <10: 45 (in base 3: 1200: factors: [45, 0])
//        sum for values <100: 540 (in base 3: 202000: factors: [12, 0])
//        sum for values <1,000: 50040 (in base 3: 2112122100: factors: [92, 360])
//        sum for values <10,000: 3364890 (in base 3: 20022221202120: factors: [67, 12210])
//        sum for values <100,000: 334795890 (in base 3: 22222101121210: factors: [99, 1671780])
//        sum for values <1,000,000: 27671338200 (in base 3: 110111121220210: factors: [82, 218075220])
//        sum for values <10,000,000: 2761284321300 (in base 3: 122121100001210: factors: [99, 21821839500])
//        sum for values <100,000,000: 241066406350560 (in base 3: 100020121222010: factors: [87, 834670397460])
//        sum for values <1,000,000,000: 24071550717893160 (in base 3: 120201210220110: factors: [99, 205976489187720])
//        sum for values <10,000,000,000: 2164109364980520560 (in base 3: 202021021021222: factors: [89, 21741351088029320])
//
//        Process finished with exit code 1

//
//        14348907
//        1073741824
//        sum for values <10: 45 (in base 3: 1200: factors: 45, 0)
//        sum for values <100: 540 (in base 3: 202000: factors: 12, 0)
//        sum for values <1,000: 50040 (in base 3: 2112122100: factors: 92, 360)
//        sum for values <10,000: 3364890 (in base 3: 20022221202120: factors: 67, 12,210)
//        sum for values <100,000: 4771029 (in base 3: 22222101121210: factors: 1, 1,406,139)
//        sum for values <1,000,000: 6645504 (in base 3: 110111121220210: factors: 1, 1,874,475)
//        sum for values <10,000,000: 9356034 (in base 3: 122121100001210: factors: 1, 2,710,530)
//        sum for values <100,000,000: 4913436 (in base 3: 100020121222010: factors: 0, 4,913,436)
//        sum for values <1,000,000,000: 8361561 (in base 3: 120201210220110: factors: 1, 3,448,125)
//        sum for values <10,000,000,000: 10771919 (in base 3: 202021021021222: factors: 1, 2,410,358)
//        sum for values <100,000,000,000: 5248588 (in base 3: 100212122201011: factors: 0, 5,248,588)
    }
}
