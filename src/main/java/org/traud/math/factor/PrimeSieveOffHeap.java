package org.traud.math.factor;

import org.traud.math.RandomAccessFileBits;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by traud on 4/8/2017.
 *
 * Iterator-like implementation of the sieve of Erathosthenes
 */
public class PrimeSieveOffHeap {

    private static final boolean DEBUG = false;
    private final long maxN;

    // convention: composite.isComposite(i) is true, if the number represented by index i is composite (i.e. was striked)
    // only odd numbers are represented, i.e. 2 is handled separately
    private final RandomAccessFileBits composite;
    private final RandomAccessFile raf;

    public PrimeSieveOffHeap(long maxN) throws IOException {
        this(maxN, "composite-bits_001.bin");
    }
    public PrimeSieveOffHeap(long maxN, String offHeapFilename) throws IOException {
        if (maxN < 10)
            throw new IllegalArgumentException("maxN must be at least 10");
        this.maxN = maxN;
        this.raf = new RandomAccessFile(offHeapFilename, "rw");
        this.composite = new RandomAccessFileBits(num2Index((maxN) | 1), raf, false);

        sieve();
    }

    public PrimeSieveOffHeap(String offHeapFilename) throws IOException {
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
    }

    private void setComposite(long j) {
        if ((j & 1) == 0)
            return;
        long bitIndex = num2Index(j);
        composite.set(bitIndex);
    }

    private boolean isComposite(long i) {
        if ((i & 1) == 0)   // even -> anyway composite;
            return true;
        return composite.get(num2Index(i));
    }

    private long num2Index(long n) {
        if ((n & 1) == 0)
            throw new IllegalArgumentException("only odd arguments allowed, but is " + n);

        if (n > 16L*Integer.MAX_VALUE)
            throw new IllegalArgumentException(
                    String.format("too large argument for n. Max supported value is %,d.", 2L*Integer.MAX_VALUE));
        return (n >> 1);
    }

    private long index2Num(long idx) {
        return ((idx << 1) | 1);
    }

    public PrimeSieveIterator iterator() {
        return new PrimeSieveIteratorOH();
    }

    public class PrimeSieveIteratorOH implements PrimeSieveIterator {

        private long idx;

        public PrimeSieveIteratorOH() {
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
}
