package mcsimulations.test.simulation;

import jsc.distributions.Beta;
import mcsimulations.simulation.ComputeException;
import mcsimulations.simulation.BetaCalculator;
import mcsimulations.test.tools.Property;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Description : Tests for class BetaCalculator
 * Date: 11/25/13
 * Time: 1:56 PM
 */
public class BetaCalculatorTest {
    private static final int MAX_RANDOM_TESTS = Property.BC_RANDOM_MAX.getValueInt();
    private static final double MAX_TIME = 500;
    private static final int MAX_RANDOM_TESTS_DISTRIBUTION = Property.BC_RANDOM_MAX_DIST.getValueInt();
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

            Set<Double> betaValues = new HashSet<Double>();

            for( int i = 0; i < MAX_RANDOM_TESTS_DISTRIBUTION; i++) {
                final double beta = getBeta(betaCalculator);

                String newParams = ", value : " + beta +  params;
                assertFalse("Last value already contained in calculated values" + newParams,
                            betaValues.contains(beta));
                assertTrue("Random value MUST be less than or equals pessimistic :" + newParams,
                            beta <= pessimistic);
                assertTrue("Random value MUST be greater than or equals optimistic :" + newParams,
                            beta >= optimistic);

                betaValues.add(beta);
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
