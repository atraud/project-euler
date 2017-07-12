package org.traud.math.factor;

import java.util.BitSet;

/**
 * Created by traud on 4/8/2017.
 *
 * Iterator-like implementation of the sieve of Erathosthenes
 */
public class PrimeSieve {

    private static final boolean DEBUG = false;
    private final long maxN;
    private final long base;
    private long primeCount;
    // convention: composite.isComposite(i) is true, if the number represented by index i is composite (i.e. was striked)
    // only odd numbers are represented, i.e. 2 is handled separately
    private final BitSet composite;

    public PrimeSieve(long maxN) {
        this(0, maxN);
    }


    public PrimeSieve(long from, long maxN) {
        if (maxN < 10)
            throw new IllegalArgumentException("maxN must be at least 10");
        if ((from & 1) == 1)
            throw new IllegalArgumentException("from must be even but is " + from);
        this.maxN = maxN;
        this.base = from;
        this.composite = new BitSet(num2Index((maxN-from) | 1));

        sieve();
    }

    private void sieve() {
        for (long i = 3; i <= maxN; ++i) {

            if (base==0 && isComposite(i)) {
                if (DEBUG) System.out.printf("sieve: skipping multiples %,d as they have been striked already\n", i);
                continue;
            }

            if (DEBUG) System.out.printf("sieve: striking multiples of %,d: ", i);
            for (long j = 3*i; j <= maxN; j += 2*i) {
                if (j < 0)
                    break;  // avoid overflow: TODO: how can this be done better?
                setComposite(j);
                if (DEBUG) System.out.printf("%,d, ", j);
            }
            if (DEBUG) System.out.printf("\n");
        }
        primeCount = 0;
        for (long i = 2; i <= maxN; ++i)
            if (!isComposite(i))
                primeCount++;
    }

    private void setComposite(long j) {
        if ((j & 1) == 0 || j < base)
            return;
        int bitIndex = num2Index(j);
        composite.set(bitIndex);
    }

    private boolean isComposite(long i) {
        if (i <= 2 )
            return false;
        if ((i & 1) == 0)   // even -> anyway composite;
            return true;
        return composite.get(num2Index(i));
    }

    private int num2Index(long n) {
        n -= base;
        if ((n & 1) == 0)
            throw new IllegalArgumentException("only odd arguments allowed, but is " + n);

        if (n > 2L*Integer.MAX_VALUE)
            throw new IllegalArgumentException(
                    String.format("too large argument for n. Max supported value is %,d.", 2L*Integer.MAX_VALUE));
        return (int)(n >> 1);
    }

    private long index2Num(int idx) {
        return ((idx << 1) | 1) + base;
    }

    public PrimeSieveIterator iterator() {
        return new PrimeSieveIteratorStd();
    }

    public long primeCount() {
        return primeCount;
    }

    public class PrimeSieveIteratorStd implements PrimeSieveIterator {

        private int idx;

        public PrimeSieveIteratorStd() {
            reset();
        }

        public void reset() {
            this.idx = -1;
        }

        public long getPreviousPrime(long p) {
            if (base != 0)
                throw new IllegalArgumentException("getNextPrime() not support for non-zero base values");

            if (p == 3)
                return 2;

            if (p > maxN || p < 3)
                throw new IllegalArgumentException("argument ("+p+") too large or invalid. Max value allowed is " + maxN);
            int idx = num2Index((int)(p-1 | 1));
            int prevIdx = composite.previousClearBit(idx - 1);

            if (prevIdx == -1 || prevIdx == 0)
                return -1;

            return index2Num(prevIdx);
        }

        public long getNextPrime(long p) {
            if (base != 0)
                throw new IllegalArgumentException("getNextPrime() not support for non-zero base values");

            if (p > maxN)
                throw new IllegalArgumentException("argument ("+p+") too large or invalid. Max value allowed is " + maxN);
            int idx = num2Index((int)(p-1 | 1));
            int nextIdx = composite.nextClearBit(idx + 1);

            if (nextIdx == composite.size())
                return -1;

            return index2Num(nextIdx);
        }


        public boolean hasNext() {
            if (idx == -1)
                return true;

            if (idx == Integer.MAX_VALUE)
                return false;

            return true;
        }

        public long next() {
            if (idx == -1) {
                if (base == 0) {
                    idx = 1;
                    return 2;
                }
                idx = composite.nextClearBit(0);
            }

            int curr = idx;
            int next = composite.nextClearBit(idx+1);
            if (next >= composite.size())
                idx = Integer.MAX_VALUE;
            else
                idx = next;
            long value = index2Num(curr);
            long nextValue = index2Num(next);
            if (nextValue >= maxN)
                idx = Integer.MAX_VALUE;
            return value;
        }
    }
}
