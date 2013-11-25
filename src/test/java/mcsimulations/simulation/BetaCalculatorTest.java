package mcsimulations.simulation;

import jsc.distributions.Beta;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Description : Tests for class BetaCalculator
 * Date: 11/25/13
 * Time: 1:56 PM
 */
public class BetaCalculatorTest {
    private static final int MAX_RANDOM_TESTS = 1000;
    private static final double MAX_TIME = 500;
    private static final int MAX_RANDOM_TESTS_INNER = 100;
    private static final int MOST_LIKELY_MINIMUM = 3;
    private static final double MOST_LIKELY_MAXIMUM = MAX_TIME - 10;

    @Test
    public void testRandom() throws ComputeException {

        for (int i = 0; i < MAX_RANDOM_TESTS; i++) {
            int mostLikely = (int) (Math.random() * MAX_TIME);

            while ( ! (mostLikely >= MOST_LIKELY_MINIMUM && mostLikely <= MOST_LIKELY_MAXIMUM) )
                mostLikely = (int) (Math.random() * MAX_TIME);

            int pessimistic;
            while ((pessimistic = (int) (Math.random() * MAX_TIME)) <= mostLikely);
            int optimistic;
            while ((optimistic = (int) (Math.random() * MAX_TIME)) >= mostLikely) ;

            testGeneric(optimistic, pessimistic, mostLikely);
        }
    }


    private void testGeneric(int optimistic, int pessimistic, int mostLikely) throws ComputeException {

        String params = ", Optimistic : " + optimistic +
                ", Pessimistic : " + pessimistic +
                ", Most Likely : " + mostLikely;
        try {
            BetaCalculator betaCalculator = new BetaCalculator(optimistic, pessimistic, mostLikely);

            assertTrue("Alpha must be greater than zero :" + betaCalculator.getAlpha() + params,
                    betaCalculator.getAlpha() > 0);
            assertTrue("Beta must be greater than zero :" + betaCalculator.getBeta() + params,
                    betaCalculator.getBeta() > 0);

            for( int i = 0; i < MAX_RANDOM_TESTS_INNER; i++) {
                final double beta = getBeta(betaCalculator);
                String newParams = ", value : " + beta +  params;
                assertTrue("Random value MUST be less than or equals pessimistic :" + newParams,
                            beta <= pessimistic);
                assertTrue("Random value MUST be greater than or equals optimistic :" + newParams,
                            beta >= optimistic);
            }

        } catch (ComputeException e) {
            throw new ComputeException(e.getMessage() + params);
        }
    }

    private double getBeta(BetaCalculator betaCalculator) {
        Beta beta = new Beta(betaCalculator.getAlpha(),
                betaCalculator.getBeta());
        return betaCalculator.scaleValue(beta.random());
    }
}
