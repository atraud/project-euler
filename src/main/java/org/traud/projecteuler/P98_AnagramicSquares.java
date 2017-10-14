package org.traud.projecteuler;

import org.traud.math.Choice;
import org.traud.math.GenericPermutation;
import org.traud.math.Permutation;
import org.traud.math.StringPermutation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by traud on 10/1/2017.
 */
public class P98_AnagramicSquares {
    private static String stripQutes(String wo) {
        if (wo.startsWith("\""))
            wo = wo.substring(1);
        if (wo.endsWith("\""))
            wo = wo.substring(0, wo.length()-1);
        return wo;
    }

    private static String chars(String w) {
        w = w.toUpperCase();
        char[] c = w.toCharArray();
        char[] r = new char[c.length];
        int cnt = 0;
        for (int i = 0; i < c.length; ++i) {
            boolean found = false;
            char ch = c[i];
            for (int j = 0; j < cnt; ++j) {
                if (r[j] == ch)
                    found = true;
            }
            if (!found)
                r[cnt++] = ch;

        }
        return new String(Arrays.copyOfRange(r, 0, cnt));
    }


    public static String key(String w) {
        char[] chars = w.toCharArray();
        Arrays.sort(chars);
        return new String(chars);
    }

    public static long maxSquareCharSubstitution(String orgChars, List<String> words) {
        long maxValue = 0;
        if (orgChars.length() > 10){
            System.out.printf("skipping '%s' as there are more than 10 unique characters (%s).\n", orgChars, words);
            return 0;
        }
        StringPermutation perm = new StringPermutation(orgChars);
        while (perm.hasNext()) {
            String chars = perm.next();

            Choice c = new Choice(10, orgChars.length());

            while (c.hasNext()) {
                c.next();
                int[] choice = c.getChoice();

                boolean allSquares = true;
                for (String w : words) {
                    long value = getValue(w, chars, choice);
                    if (choice[chars.indexOf(w.charAt(0))] == 0) {
                        allSquares = false;
                    }
                    if (!isSquare(value))
                        allSquares = false;
                    //System.out.printf("%s -> %d\n", w, value);
                }

                if (allSquares) {
                    for (int i = 0; i < chars.length(); ++i)
                        System.out.printf("%s%s: %d", i>0?", " : "", chars.charAt(i) + "", choice[i]);
                    System.out.printf("\n");

                    for (String w : words) {
                        if (choice[chars.indexOf(w.charAt(0))] == 0) {
                            System.out.printf("skipping '%s' as leading char has value 0\n", w);
                            continue;
                        }

                        long value = getValue(w, chars, choice);
                        System.out.printf("%s -> %d (%d²)\n", w, value, (int) Math.sqrt(value));
                        if (value > maxValue)
                            maxValue = value;
                    }
                    System.out.printf("-----------\n");
                }
            }
        }
        return maxValue;
    }

    private static long getValue(String w, String chars, int[] choice) {
        long sum = 0;
        if (chars.length() != choice.length)
            throw new IllegalArgumentException("invalid lengths: chars '" + chars + "', choice: " + Arrays.toString(choice));
        for (int i = 0; i < w.length(); ++i) {
            char ch = w.charAt(i);
            int j = chars.indexOf(ch);
            if (j < 0)
                throw new IllegalArgumentException("chars '" + chars + "' do not fit for word '" + w + "'");
            sum *= 10;
            sum += choice[j];
        }
        return sum;
    }

    public static boolean isSquare(long l) {
        long sqrt = (long)Math.sqrt(l);
        return (sqrt*sqrt == l);
    }

    public static void main(String[] args) throws IOException {

        //        Congratulations, the answer you gave to problem 98 is correct.
        //
        //        You are the 8525th person to have solved this problem.
        //        2017-10-03 06:49
        //        ABDOR: [BOARD, BROAD] (ABDOR)
        //        maximum value: 18769 (18,769) = 137.00²

        BufferedReader rd = new BufferedReader(new FileReader("p098_words.txt"));
        String line;

        Set<String> words = new HashSet<>();
        while ((line = rd.readLine()) != null) {
            System.out.printf("%s\n", line);
            String[] w = line.split(",");
            for (String wo : w) {
                words.add(stripQutes(wo));
            }
        }
        rd.close();
        System.out.printf("%,d words\n", words.size());

        Map<String, List<String>> anagrams = new HashMap<>();
        for (String w : words) {
            String k = key(w);

            List<String> a = anagrams.get(k);
            if (a == null)
                anagrams.put(k, a = new ArrayList<String>());
            a.add(w);
        }
        System.out.printf("--------------\n");
        int idx = 0;
        long maxSq = 0;
        String maxAna = null;
        for (String w : anagrams.keySet()) {
            if (anagrams.get(w).size()>1) {
                idx++;
                System.out.printf("%d %s: %s (%s)\n", idx, w, anagrams.get(w), chars(w));

                long l = maxSquareCharSubstitution(chars(w), anagrams.get(w));
                if (l > maxSq) {
                    maxSq = l;
                    maxAna = w;
                }
            }
        }
        System.out.printf("--------------\n");
        System.out.printf("%s: %s (%s)\n", maxAna, anagrams.get(maxAna), chars(maxAna));
        System.out.printf("maximum value: %d (%,d) = %1.2f²\n", maxSq, maxSq, Math.sqrt(maxSq));
//        long l = maxSquareCharSubstitution(chars(maxAna), anagrams.get(maxAna));
        System.out.printf("--------------\n");



    }


}
