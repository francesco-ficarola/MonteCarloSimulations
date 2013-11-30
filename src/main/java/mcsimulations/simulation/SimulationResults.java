package mcsimulations.simulation;

import java.util.List;
import java.util.ArrayList;

class SimulationResults {
	
    private List<Double> duration = new ArrayList<Double>();
    private Double mean;
    private Double sd;
    
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
	
	
}

                           
