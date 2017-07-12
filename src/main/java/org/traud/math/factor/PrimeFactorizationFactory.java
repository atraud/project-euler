package org.traud.math.factor;

/**
 * Created by traud on 7/12/2017.
 */
public class PrimeFactorizationFactory {
    public static FactorAlgorithm getPrimeFactorization() {
        return new PrimeFactorization(Integer.MAX_VALUE);
    }


    public static FactorAlgorithm getPrimeFactorization(int maxValue) {
        return new PrimeFactorization(maxValue);
    }
}
