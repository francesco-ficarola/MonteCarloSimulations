package mcsimulations.simulation;

/**
 * Description : Histogram interval
 * Date: 12/1/13
 * Time: 6:23 AM
 */
public class Interval {
    private Double end;
    private Double start;
    private int height;

    public Interval(double start, double end) {
        this.start = start;
        this.end = end;
        height = 0;
    }

    public Double getEnd() {
        return end;
    }

    public Double getStart() {
        return start;
    }

    public void incHeight() {
        height++;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public String toString() {
        return "Interval{" +
                "start=" + start +
                ", end=" + end +
                ", height=" + height +
                '}';
    }
}
