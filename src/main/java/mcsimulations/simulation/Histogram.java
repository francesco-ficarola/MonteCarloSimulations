package mcsimulations.simulation;

import java.util.*;

/**
 * Description : Histogram creator
 * Date: 12/1/13
 * Time: 6:02 AM
 */
public class Histogram {
    private final List<Double> durations;
    private final int numIntervals;
    private List<Interval> intervals;

    public Histogram(List<Double> durations, int numIntervals) {
        this.durations = durations;
        this.numIntervals = numIntervals;
    }

    public int getNumIntervals() {
        return numIntervals;
    }

    public Interval getInterval(int interval) {
        return getIntervals().get(interval);
    }


    private List<Interval> getIntervals() {
        if (intervals == null)
            intervals = calcIntervals();

        return intervals;
    }

    private List<Interval> calcIntervals() {
        List<Interval> intervals = new ArrayList<Interval>(numIntervals);

        // Gets collection max an min
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        for (double n : durations) {
            max = Math.max(max, n);
            min = Math.min(min, n);
        }

        initIntervals(intervals, min, max);

        for (Double d : durations) {
            boolean found = false;
            final Iterator<Interval> iterator = intervals.iterator();
            while (iterator.hasNext() && !found) {
                final Interval interval = iterator.next();
                final int startCmp = interval.getStart().compareTo(d);
                final int endCmp = interval.getEnd().compareTo(d);
                if ((startCmp == 0 || startCmp < 0) && (endCmp == 0 || endCmp > 0)) {
                    found = true;
                    interval.incHeight();
                }
            }

            if (!found)
                throw new RuntimeException("Histogram interval not found for value " + d);
        }


        return intervals;
    }

    private void initIntervals(List<Interval> intervals, double min, double max) {
        double intervalStart = min;
        double intervalEnd;
        double width = (max - min) / numIntervals;
        for (int i = 0; i < numIntervals; i++) {
            intervalEnd = getIntervalEnd(max, intervalStart, width, i);
            intervals.add(new Interval(intervalStart, intervalEnd));
            intervalStart = intervalEnd;
        }
    }

    private double getIntervalEnd(double max, double intervalStart, double width, int i) {
        if (i != (numIntervals - 1))
            return intervalStart + width;
        else
        // Because of rounding errors adding n widths to interval start might not cover all durations range
            return max;

    }

}
