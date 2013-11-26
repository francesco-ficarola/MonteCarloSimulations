package mcsimulations.simulation;

/**
 * Description : Beta distribution calculations
 * Date: 11/25/13
 * Time: 12:13 PM
 */
public class BetaCalculator {

    private static final int LAMBDA = 4;
    private final int optimistic;
    private final int pessimistic;
    private final int mostLikely;
    private Double mean;
    private Double variance;
    private Double commonFactor;
    private Double alpha;
    private Double k1;
    private Double beta;

    /***
     * Calculates Beta distribution parameters based on evaluated times
     * {@link https://en.wikipedia.org/wiki/Beta_distribution#Related_distributions}
     * @param optimistic the minimum possible time required to accomplish a task
     * @param pessimistic the maximum possible time required to accomplish a task
     * @param mostLikely  the best estimated of the time required to accomplish a task
     * @throws ComputeException
     */
    public BetaCalculator(int optimistic, int pessimistic, int mostLikely) throws ComputeException {
        this.optimistic = optimistic;
        this.pessimistic = pessimistic;
        this.mostLikely = mostLikely;

        if ( optimistic > mostLikely)
            throw new ComputeException("Optimistic time MUST BE lower or equal than Most Likely time");

        if ( pessimistic < mostLikely)
            throw new ComputeException("Pessimistic time MUST BE greater or equal than Most Likely time");

    }

    public double getAlpha() {
        double num = mostLikely - optimistic;
        double den = pessimistic - optimistic;
        return 1 + LAMBDA * (num / den);
    }

    public double getBeta() {
        double num = pessimistic - mostLikely;
        double den = pessimistic - optimistic;
        return 1 + LAMBDA * (num / den);
    }

    public double scaleValue(double betaValue) {
        double slope =  pessimistic - optimistic;
        return optimistic + (betaValue * slope);
    }

}
