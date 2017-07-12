package org.traud.math.factor;

/**
 * Created by traud on 7/6/2017.
 */
public interface PrimeSieveIterator {
    public void reset();

    public long getNextPrime(long p);

    public long getPreviousPrime(long p);

    public boolean hasNext();

    public long next();

}
