package mcsimulations;

import java.util.ArrayList;
import java.util.List;

public class DataActResponse {
	private List<List<Object>> dataAct = new ArrayList<List<Object>>();
	private List<Integer> precErrors = new ArrayList<Integer>();
	private List<Integer> cyclicErrors = new ArrayList<Integer>();
	private List<Integer> paramErrors = new ArrayList<Integer>();
	private List<Integer> paramExceptions = new ArrayList<Integer>();

	public DataActResponse() {}

	/**
	 * Retrieves activities processed
	 **/
	public List<List<Object>> getDataActivities() {
		return this.dataAct;
	}

	/**
	 * Retrieves activities containing precedence errors regarding the number of priorities
	 **/
	public List<Integer> getPrecErrors() {
		return this.precErrors;
	}

	/**
	 * Retrieves activities containing cyclic path errors 
	 **/
	public List<Integer> getCyclicErrors() {
		return this.cyclicErrors;
	}

	/**
	 * Retrieves activities containing errors in distribution parameters 
	 **/
	public List<Integer> getParamErrors() {
		return this.paramErrors;
	}

	/**
	 * Retrieves activities that thrown exceptions due to the fields parameters 
	 **/
	public List<Integer> getParamExceptions() {
		return this.paramExceptions;
	}


}

