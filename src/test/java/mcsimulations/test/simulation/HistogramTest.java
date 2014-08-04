package mcsimulations.test.simulation;

import mcsimulations.simulation.Histogram;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Description : Tests histogram
 * Date: 12/1/13
 * Time: 6:54 AM
 */
public class HistogramTest {

    @Test
    public void test1() {
        final int INTERVALS = 2;
        List<Double> durations = new ArrayList<Double>(10);
        durations.add( (double) 1);
        durations.add((double) 2);
        durations.add((double) 3);
        durations.add((double) 4);
        durations.add((double) 5);
        durations.add((double) 6);
        durations.add((double) 7);
        durations.add((double) 8);
        durations.add((double) 9);
        durations.add((double) 10);
        final Histogram histogram = new Histogram(durations, INTERVALS);
        assertEquals( "Wrong height", 5, histogram.getInterval(0).getHeight() );
        assertEquals( "Wrong height", 5, histogram.getInterval(1).getHeight() );
    }

    @Test
    public void test2() {
        final int INTERVALS = 2;
        List<Double> durations = new ArrayList<Double>(10);
        durations.add( (double) 1);
        durations.add((double) 2);
        durations.add((double) 3);
        durations.add((double) 4);
        durations.add((double) 5);
        durations.add((double) 4);
        durations.add((double) 7);
        durations.add((double) 8);
        durations.add((double) 9);
        durations.add((double) 10);
        final Histogram histogram = new Histogram(durations, INTERVALS);
        assertEquals( "Wrong height", 6, histogram.getInterval(0).getHeight() );
        assertEquals( "Wrong height", 4, histogram.getInterval(1).getHeight() );
    }



    @Test
    public void test3() {
        final int INTERVALS = 10;
        List<Double> durations = new ArrayList<Double>(10);
        durations.add( (double) 1);
        durations.add((double) 2);
        durations.add((double) 3);
        durations.add((double) 4);
        durations.add((double) 5);
        durations.add((double) 6);
        durations.add((double) 7);
        durations.add((double) 8);
        durations.add((double) 9);
        durations.add((double) 10);
        final Histogram histogram = new Histogram(durations, INTERVALS);
        for( int i = 0; i < INTERVALS; i++)
            assertEquals( "Wrong height for interval " + i, 1, histogram.getInterval(0).getHeight() );
    }
}
