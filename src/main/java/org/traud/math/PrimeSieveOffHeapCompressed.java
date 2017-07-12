package org.traud.math;

import org.traud.util.misc.Timer;
import org.traud.math.factor.PrimeSieveIterator;
import org.traud.math.factor.Primes;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.util.Date;

/**
 * Created by traud on 4/8/2017.
 *
 * Iterator-like implementation of the sieve of Erathosthenes
 */
public class PrimeSieveOffHeapCompressed {

    private static final boolean DEBUG = false;
    private final long maxN;

    // convention: composite.isComposite(i) is true, if the number represented by index i is composite (i.e. was striked)
    // only odd numbers are represented, i.e. 2 is handled separately
    private final RandomAccessFileBits composite;
    private final RandomAccessFile raf;

    public PrimeSieveOffHeapCompressed(long maxN) throws IOException {
        this(maxN, "composite-bits_001.bin");
    }
    public PrimeSieveOffHeapCompressed(long maxN, String offHeapFilename) throws IOException {
        if (maxN < 10)
            throw new IllegalArgumentException("maxN must be at least 10");
        this.maxN = maxN;
        this.raf = new RandomAccessFile(offHeapFilename, "rw");
        this.composite = new RandomAccessFileBits(num2Index((maxN) | 1), raf, false);

        sieve();
    }

    public PrimeSieveOffHeapCompressed(String offHeapFilename) throws IOException {
        this.raf = new RandomAccessFile(offHeapFilename, "r");
        this.maxN = index2Num(raf.length()*8L);
        this.composite = new RandomAccessFileBits(num2Index((maxN) | 1), raf, true);
    }

    private void sieve() {
        for (long i = 3; i <= maxN; ++i) {

            if (isComposite(i)) {
                if (DEBUG) System.out.printf("sieve: skipping multiples %,d as they have been striked already\n", i);
                continue;
            }

            if (DEBUG) System.out.printf("sieve: striking multiples of %,d: ", i);
            long step = 2 * i;
            for (long j = 3*i; j <= maxN; j += step) {
                if (j < 0)
                    break;  // avoid overflow: TODO: how can this be done better?
                setComposite(j);
                if (DEBUG) System.out.printf("%,d, ", j);
            }
            if (DEBUG) System.out.printf("\n");
        }

        long lowK = maxN + 1;
        long hiK = index2Num(composite.size());
        for (long k = lowK; k < hiK; ++k)
            setComposite(k);
    }

    private void setComposite(long j) {
        if ((j & 1) == 0)
            return;
        if ((j % 3) == 0)   // divisible by 3
            return;

        long bitIndex = num2Index(j);
        composite.set(bitIndex);
    }

    private boolean isComposite(long i) {
        if (i == 2 || i == 3)
            return false;

        if ((i & 1) == 0)   // even -> anyway composite;
            return true;

        if ((i % 3) == 0)   // divisble by three -> anyway composite;
            return true;

        return composite.get(num2Index(i));
    }

    private static long num2Index(long n) {

        // num:  0  1  2  3  4  5  6  7  8  9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31 32 33
        // idx:  -  0  -  -  -  1  -  2  -  -  -  3  -  4  -  -  -  5  -  6  -  -  -  7  -  8  -  -  -  9  - 10  -  -

        // 3,2,3,2,3,2
        // 2*((n-1)/6)+(n-5)/2
        if ((n & 1) == 0)
            throw new IllegalArgumentException("only odd arguments allowed, but is " + n);

        if ((n % 3) == 0)   // divisble by three -> anyway composite;
            throw new IllegalArgumentException("n="+n+" is divisible by 3.");

        if (n > 16L*Integer.MAX_VALUE)
            throw new IllegalArgumentException(
                    String.format("too large argument for n. Max supported value is %,d.", 2L*Integer.MAX_VALUE));
        return 2*((n-1)/6)+(((n-1)%6)/4);
    }

    private static long index2Num(long idx) {
        // num:  0  1  2  3  4  5  6  7  8  9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31 32 33
        // idx:  -  0  -  -  -  1  -  2  -  -  -  3  -  4  -  -  -  5  -  6  -  -  -  7  -  8  -  -  -  9  - 10  -  -
        return 1+idx*3 + (idx & 1);
    }

    public PrimeSieveIterator iterator() {
        return new PrimeSieveIteratorOHC();
    }

    public long maxValue() {
        return maxN;
    }

    public class PrimeSieveIteratorOHC implements PrimeSieveIterator {

        private long idx;

        public PrimeSieveIteratorOHC() {
            reset();
        }

        public void reset() {
            this.idx = -1;
        }

        public long getPreviousPrime(long p) {
            if (p == 3)
                return 2;

            if (p > maxN || p < 3)
                throw new IllegalArgumentException("argument ("+p+") too large or invalid. Max value allowed is " + maxN);
            long idx = num2Index((int)(p-1 | 1));
            long prevIdx = composite.previousClearBit(idx - 1);

            if (prevIdx == -1 || prevIdx == 0)
                return -1;

            return index2Num(prevIdx);
        }

        public long getNextPrime(long p) {
            if (p > maxN)
                throw new IllegalArgumentException("argument ("+p+") too large or invalid. Max value allowed is " + maxN);
            long idx = num2Index((int)(p-1 | 1));
            long nextIdx = composite.nextClearBit(idx + 1);

            if (nextIdx == composite.size())
                return -1;

            return index2Num(nextIdx);
        }


        public boolean hasNext() {
            if (idx == -1)
                return true;

            if (idx == Long.MAX_VALUE)
                return false;

            return true;
        }

        public long next() {
            if (idx == -1) {
                idx = 1;
                return 2;
            }

            long curr = idx;
            long next = composite.nextClearBit(idx+1);
            if (next >= composite.size())
                idx = Long.MAX_VALUE;
            else
                idx = next;
            long value = index2Num(curr);
            long nextValue = index2Num(next);
            if (nextValue >= maxN)
                idx = Long.MAX_VALUE;
            return value;
        }
    }


    public void close() throws IOException {
        composite.close();
        raf.close();
    }


    private static String abbreviate(long maxN) {
        if (maxN >= 1_000_000_000_000L)
            return (maxN / 1_000_000_000_000L) + "T";
        if (maxN >= 1_000_000_000L)
            return (maxN / 1_000_000_000L) + "B";
        if (maxN >= 1_000_000L)
            return (maxN / 1_000_000L) + "M";
        if (maxN >= 1_000L)
            return (maxN / 1_000L) + "K";
        return Long.toString(maxN);
    }

    public static void main(String... args) throws IOException {
        for (int i = 0; i < 33; ++i) {
            if (i%2!=0 && i%3!=0)
                System.out.printf("%2d -> %2s  %2s\n", i, num2Index(i), index2Num(num2Index(i)));
            else
                System.out.printf("%2d -> %2s  %2s\n", i, "-", "-");
        }

        boolean DO_SIEVE = false;

        PrimeSieveOffHeapCompressed ps;
        long maxN = 10_000_000_000L;
        String offHeapFilename = "ro-composite-zip-"+abbreviate(maxN)+".bin";

        if (DO_SIEVE) {
            Timer t = new Timer();
            System.out.printf("%s: sieving first %s numbers to %s...\n", new Date(), abbreviate(maxN), offHeapFilename);
            ps = new PrimeSieveOffHeapCompressed(maxN, offHeapFilename);
            ps.close();
            System.out.printf("%s: done in %1.2fs.\n", new Date(), t.getElapsedSeconds());
        }
        System.out.printf("%s: loading prime sieve from %s...\n", new Date(), offHeapFilename);

        ps = new PrimeSieveOffHeapCompressed(offHeapFilename);
        PrimeSieveIterator it = ps.iterator();
        System.out.printf("%s: done... starting iteration...\n", new Date());

        long min = 100;
        long lo = maxN - 200;
        long p=0;
        Timer t = new Timer();
        while (it.hasNext()) {
            p = it.next();
            if (p > min && p < lo)
                continue;
            System.out.printf("%s: %,d \t %,d \t %s \t %s\n", new Date(), p, p, Primes.isPrime(p), BigInteger.valueOf(p).isProbablePrime(30));
        }
        System.out.printf("%s: iterating primes in below %s took %1.2fs\n", new Date(), abbreviate(maxN), t.getElapsedSeconds());
        for (long k = 9_999_999_967L; k <= 10_000_000_010L; ++k)
            if (Primes.isPrime(k))
                System.out.printf("%s: %,d is also prime", new Date(), k);
    }

}
