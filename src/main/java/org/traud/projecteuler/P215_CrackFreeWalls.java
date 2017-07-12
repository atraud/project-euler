package org.traud.projecteuler;

import org.apache.lucene.util.mutable.MutableValueInt;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by traud on 4/15/2017.
 */
public class P215_CrackFreeWalls {

    static class GrowArray {
        int[] a;

        public GrowArray() {
        }

        public GrowArray(GrowArray a) {
            this.a = a.a == null ? null : Arrays.copyOf(a.a, a.a.length);
        }

        public void add(int n) {
            a = a == null ? new int[1] : Arrays.copyOf(a, a.length+1);
            a[a.length-1] = n;
        }

        public void removeLast() {
            a = Arrays.copyOf(a, a.length-1);
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof GrowArray))
                return false;
            GrowArray o =(GrowArray)obj;
            return this.a == o.a || Arrays.equals(this.a, o.a);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(a);
        }

        public int sum(int to) {
            int sum = 0;
            for (int i = 0; i < to; ++i) {
                sum += a[i];
            }
            return sum;
        }

        public String toString() {
            if (a == null)
                return "[]";
            else {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < a.length; ++i) {
                    int j = a[i];
                    for (int k = 0; k < j; ++k)
                        sb.append(j);
                }
                return sb.toString();
            }
        }
        public int size() {
            return a == null ? 0 : a.length;
        }

        public GrowArray copy() {
            return new GrowArray(this);
        }
    }

    static class Counter<T>  {
        private final HashMap<T, MutableValueInt> map;

        Counter()
        {
            this.map = new HashMap<T, MutableValueInt>();
        }

        public void inc(T t) {
            inc(t, 1);
        }

        public void inc(T t, int inc) {
            MutableValueInt v = map.get(t);
            if (v == null)
                map.put(t, v = new MutableValueInt());
            v.value += inc;
        }

        public int count(T t) {
            MutableValueInt v = map.get(t);
            if (v == null)
                return 0;
            return v.value;
        }

        public void addAll(Counter<T> out) {
            for (Map.Entry<T, MutableValueInt> e : out.map.entrySet()) {
                inc(e.getKey(), e.getValue().value);
            }
        }

        public String toString() {
            return map.toString();
        }
    }

    public static long countAux(GrowArray[] a, int width, int height, int y) {
        return countAux(a, width, height, y, null);
    }

    public static long countAux(GrowArray[] a, int width, int height, int y, Counter<GrowArray> out) {

        if (a == null) {
            a = new GrowArray[height];
            for (int i = 0; i < height; ++i)
                a[i] = new GrowArray();
        }

        long cnt = 0;
        int sum = a[y].sum(a[y].size());
        if (sum < width) {
            if (sum + 2 <= width) {
                a[y].add(2);
                if (y == 0 || noCrack(a, y))
                    cnt += countAux(a, width, height, y, out);
                a[y].removeLast();;
            }
            if (sum + 3 <= width) {
                a[y].add(3);
                if (y == 0 || noCrack(a, y))
                    cnt += countAux(a, width, height, y, out);
                a[y].removeLast();;
            }
        }
        else if (sum == width) {
            if (y+1 < height)
                cnt += countAux(a, width, height, y+1, out);
            else if (y == height-1) {
                //  for (int i = 0; i < height; ++i)
                //      System.out.printf("%d: %s -> %d\n", i, a[i], a[i].sum(a[i].size()));
                //  System.out.printf("\n");
                if (out != null)
                    out.inc(a[y].copy());
                cnt = 1;
            }
        }
        return cnt;
    }

    private static boolean noCrack(GrowArray[] a, int y) {
        int sumY = a[y].sum(a[y].size());
        for (int i = 1; i < a[y-1].size(); ++i)
            if (a[y-1].sum(i) == sumY)
                return false;
        return true;
    }


    public static void main(String... args) {
//        long c = primeCount(9, 3);
//        System.out.printf("c(9,3)=%d\n", c);

        int[][] wh = new int[][]{
                {32, 1},
                {32, 2},
                {32, 3},
//                {32, 4},
//                {32, 5},
//                {32, 6},
//                {32, 7},
        };

        // cnt(32,1)=3329 (x3329)
        // cnt(32,2)=37120 (x11)
        // cnt(32,3)=592050 (x15)
        // cnt(32,4)=10178548 (x17)
        // cnt(32,5)=199541122 (x19)

        long pc = 1;
        for (int[] xy : wh) {
            int w = xy[0];
            int h = xy[1];

            long cnt = countAux(null, w, h, 0);
            System.out.printf("cnt(%d,%d)=%d (x%d)\n", w, h, cnt, cnt / pc);
            pc = cnt;
        }
        System.out.printf("----------------\n");
        Counter<GrowArray> lastOut = null;
        int WIDTH=32;
        int HEIGHT=10;

        WIDTH=32;
        HEIGHT=10;

        for (int y = 1; y <= 1; ++y) {
            Counter<GrowArray> out = new Counter<>();
            long cnt = countAux(null, WIDTH, y, y - 1, out);
            System.out.printf("y=%d out.site=%d\n", y, out.map.size());
            lastOut = out;
        }

        long totalCount = 0;
        for (int y = 2; y <= HEIGHT; ++y) {
            long cnt = 0;

            Counter<GrowArray> totalOut = new Counter<>();

            for (Map.Entry<GrowArray, MutableValueInt> e : lastOut.map.entrySet()) {
                Counter<GrowArray> out = new Counter<>();
                GrowArray a = e.getKey();
                int aCnt = e.getValue().value;
                countAux(new GrowArray[]{a, new GrowArray()}, WIDTH, 2, 1, out);

                for (Map.Entry<GrowArray, MutableValueInt> e2 : out.map.entrySet()) {
                    GrowArray aChild = e2.getKey();
                    int aChildCnt = e2.getValue().value;
                    int N = aChildCnt * 1;
                    cnt += N;
                    totalOut.inc(aChild, N);

                    if (N > 1)
                    System.out.printf("%s(%d) -> %s(%d) -> %d\n", a, aCnt, aChild, aChildCnt, N);

                }
                totalOut.addAll(out);
            }
            totalCount += cnt;
            System.out.printf("y=%d -> cnt=%d / out.size=%d; totalCnt=%d\n", y, cnt, totalOut.map.size(), totalCount);
            lastOut = totalOut;
        }

        // String s1 = a1 + 31*a2;
        // String s2 = b1 + 31*b2;
        // a1 + 31*a2 = b1 + 31*b2

        // 109 83 79

    }

}
