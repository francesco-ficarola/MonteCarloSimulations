package mcsimulations.simulation;

import java.util.List;
import java.util.ArrayList;

public class SimulationResults {
	
    private List<Double> duration = new ArrayList<Double>();
    private Double mean;
    private Double sd;
    private Double maxDuration;
    private int[] cpn;
    
	public void addDuration(double d) {
	    duration.add(d);
	}

	public int getNumDurations() {
	    return duration.size();
	}
	
	public List<Double> getDurations() {
	    return duration;
	}
	
	public Double getMean() {
	    return mean;
	}
	
	public void setMean( Double mean ) {
	    this.mean = mean;
	}
	public Double getSD() {
	    return sd;
	}
	
	public void setSD( Double sd ) {
	    this.sd = sd;
	}

    public Histogram getHistogram(int intervals) {
        return new Histogram(getDurations(), intervals);
    }
    
    public void setMaxDuration( double max ) {
    	this.maxDuration = max;
    }
    
    public Double getMaxDuration() {
    	return this.maxDuration;
    }
    
    public int[] getCPN() {
    	return this.cpn;
    }

    public void setCPN( int[] cpn ) {
    	this.cpn = cpn;
    }
	
	
}

                           
