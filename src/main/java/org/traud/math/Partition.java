package org.traud.math;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by traud on 4/8/2017.
 */
public class Partition {

    private static final boolean DEBUG = false;

    public static <T> void enumeratePartitions(Collection<T> c, int k) {

        int n = c.size();
        // see also https://en.wikipedia.org/wiki/Stirling_numbers_of_the_second_kind
        System.out.printf("enumerating all %d-partitions of %s\n", k, c);
        
        //   { n }     {n-1}   {n-1}
        //   {   } = k*{   } + {   }
        //   { k }     { k }   {k-1}

        ArrayList<List<T>> preset = new ArrayList<>();
        for (int i = 0; i < k; ++i) {
            preset.add(new ArrayList<T>());
        }
        enumeratePartitionsAux(new ArrayList<T>(c), k, preset, null);
    }

    private static <T> void enumeratePartitionsAux(List<T> c, int k, List<List<T>> presets, List<List<T>> additional) {
        int n = c.size();
        if (DEBUG) System.out.printf("aux: n=%d, k=%d, additional: %s, preset: %s\n", n, k, additional, presets);
        if (n == k) {
            if (DEBUG) System.out.printf("n==k==%d, additional: %s, preset: %s\n", k, additional, presets);
            if (additional != null) {
                for (List<T> l : additional)
                    System.out.printf("%s", l);
            }
            for (int i = 0; i < k; ++i) {
                ArrayList<T> partition = new ArrayList<>(presets.get(i));
                partition.add(c.get(i));
                System.out.printf("%s", partition);
            }
            System.out.printf("\n");
            return;
        }
        if (k == 1) {
            if (DEBUG) System.out.printf("k==%d, additional: %s, preset: %s\n", k, additional, presets);
            if (additional != null) {
                for (int i = 0; i < additional.size(); ++i) {
                    List<T> l = new ArrayList<>(presets.get(i));
                    l.addAll(additional.get(i));
                    System.out.printf("%s", l);
                }
            }
            ArrayList<T> partition = new ArrayList<>(c);
            partition.addAll(presets.get(additional == null ? 0 : additional.size()));

            System.out.printf("%s\n", partition);
            return;
        }

        T first = c.get(0);
        List<T> reduced = c.subList(1, c.size());

        for (int i = 0; i < k; ++i) {
            presets.get(i).add(first);
            enumeratePartitionsAux(reduced, k, presets, additional);
            presets.get(i).remove(presets.get(i).size()-1);

        }
        ArrayList<List<T>> additional1 = new ArrayList<>();
        if (additional != null)
            additional1.addAll(additional);
        additional1.add(Arrays.asList(first));
        enumeratePartitionsAux(reduced, k-1, presets, additional1);
    }

    static class RestrictedGrowthString {
        private final int[] a;
        private int k;

        public RestrictedGrowthString(int n) {
            this.a = new int[n];
            for (int i = 0; i < n; ++i)
                a[i] = 0;
        }

        public RestrictedGrowthString(int n, int k) {
            this.a = new int[n];
            for (int i = 0; i < n-k; ++i)
                a[i] = 0;
            for (int i = n-k; i < n; ++i)
                a[i] = i-(n-k);
            this.k = k;
        }

        public int get(int i) {
            return a[i];
        }

        public String toString() {
            StringBuilder sb = new StringBuilder(6+a.length).append("[RGS:");
            for (int i = 0; i < a.length; ++i)
                sb.append(a[i]);
            sb.append("]");
            return sb.toString();
//            return "[RGS: "+Arrays.toString(a)+"]";
        }

        public int size() {
            return a.length;
        }

        public void set(int i, int v) {
            a[i] = v;
        }

        public void increment() {

        }
        public void inc(int j) {
            a[j]++;
        }

        public boolean hasNext() {
            return false;
        }

        // 2-partitions of a 3-set
        // 001
        // 010
        // 011
        // 100
        // 101
        // 110

        // 3-partitions of a 4-set
        // 0012
        // 0102
        // 0112
        // 0120
        // 0121
        // 0122
        // x
        //

        // 00012
        // 00112
        // 00122
        // 01112


    }

    public static void gen(String s, int largest, int currentLength,
                           int desiredLength) {
        if (currentLength == desiredLength) {
            System.out.println(s);
            return;
        }
        for (int i = 0; i <= largest; i++) {
            gen(s + i, i + 1, currentLength + 1, desiredLength);
        }
    }

    public static void main(String[] args) {
        enumeratePartitions(Arrays.asList("1","2","3","4"), 3);
        enumeratePartitions(Arrays.asList("A","L","E", "X"), 2);
//
        enumeratePartitions(Arrays.asList("A","L","E","X"), 1);
        enumeratePartitions(Arrays.asList("F","R","A","N","K"), 3);
        enumeratePartitions(Arrays.asList("1","2","3","4","5"), 3);

        RestrictedGrowthString rgs = new RestrictedGrowthString(5,3);
        System.out.printf("----------------------\n");
        System.out.printf("%d: %s\n", 0, rgs);
        int index = 0;
        while (rgs.hasNext()) {
            ++index;
            rgs.increment();
            System.out.printf("%d: %s\n", index, rgs);
        }
        //gen("", 3, 0, 3);
        System.out.printf("----------------------\n");
        int n = 5;
        System.exit(0);
        RestrictedGrowthString a = new RestrictedGrowthString(n);
        RestrictedGrowthString b = new RestrictedGrowthString(n);
        for (int i = 0; i < b.size(); ++i)
            b.set(i, 1);
        int m = 1;
        for (;;) {
            m = b.get(n - 1);
            System.out.printf("a=%s\n", a);
            System.out.printf("b=%s\n", b);
            System.out.printf("m=%d\n", m);

            if (a.get(n - 1) != m) {
                // H3:
                a.inc(n - 1);
                continue;
            }


            // H4:
            int j = n - 2;
            while (j > 0 && a.get(j) == b.get(j))
                j--;

            // H5:
            if (j == 0)
                System.exit(0);
            a.inc(j);

            // H6:
            for (int i = j + 1; i < n; ++i)
                a.set(i, 0);
            m = b.get(j) + a.get(j);
            j++;
            while (j < n-1) {
                a.set(j, 0);
                b.set(j, m);
                j++;
            }
            a.set(n-1,0);
        }
    }
}
