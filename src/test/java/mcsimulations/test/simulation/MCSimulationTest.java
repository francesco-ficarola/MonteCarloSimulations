package mcsimulations.test.simulation;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

import mcsimulations.simulation.*;
import mcsimulations.test.tools.ActivitiesBuilder;
import mcsimulations.Distribution;

public class MCSimulationTest {

	private static boolean NON_VERBOSE = false;
	private static int IS_ACYCLIC = 1;
	private static int REPETITIONS_DEFAULT = 100;
	
	@Test
	public void test1activity() throws Exception {
		List<List<Object>> activities = new ActivitiesBuilder().newActivity().build();

		MCSimulation mcs = new MCSimulation(NON_VERBOSE, REPETITIONS_DEFAULT, activities);

		assertEquals( IS_ACYCLIC, mcs.topologicalSort() );
		mcs.makeInNodes();
		
		SimulationResults results = mcs.results();

		assertEquals(1, results.getCPN().length);
		assertEquals(0, results.getCPN()[0]);
		assertTrue( results.getSD() * 6 + results.getMean() >  results.getMaxDuration() );

	}

	@Test
	public void test2activities() throws Exception {
		List<List<Object>> activities = 
			new ActivitiesBuilder()
				.newActivity()
				.newActivity().addPrecedent(1)
				.build();

		MCSimulation mcs = new MCSimulation(NON_VERBOSE, REPETITIONS_DEFAULT, activities);

		assertEquals( IS_ACYCLIC, mcs.topologicalSort() );
		mcs.makeInNodes();

		SimulationResults results = mcs.results();

		assertEquals(2, results.getCPN().length);
		assertEquals(REPETITIONS_DEFAULT, results.getCPN()[0]);
		assertEquals(REPETITIONS_DEFAULT, results.getCPN()[1]);
		assertTrue( results.getSD() * 6 + results.getMean() >  results.getMaxDuration() );

	}
	
	@Test
	public void test3activities() throws Exception {
		
		int[] gaussParams = new  int[]{ 100, 1 }; // mean, std dev
		
		List<List<Object>> activities = 
			new ActivitiesBuilder()
				.newActivity()
				.newActivity().addPrecedent(1)
				.newActivity().addPrecedent(1).setDist(Distribution.GAUSSIAN, gaussParams)
				.build();

		assertEquals("Gaussian", (String) activities.get(2).get(ActivitiesBuilder.ACTIVITY_DIST_NAME) );
		for( int i = 0; i < gaussParams.length; i ++) {
			assertEquals("Wrong parameter #" + i, gaussParams[i], ((int []) activities.get(2).get(ActivitiesBuilder.ACTIVITY_DIST_PARAMS))[i]);
		}
		
		MCSimulation mcs = new MCSimulation(NON_VERBOSE, REPETITIONS_DEFAULT, activities);

		assertEquals( IS_ACYCLIC, mcs.topologicalSort() );
		mcs.makeInNodes();

		SimulationResults results = mcs.results();

		assertEquals(3, results.getCPN().length);
		assertEquals(REPETITIONS_DEFAULT, results.getCPN()[0]);
		assertEquals(0, results.getCPN()[1]);
		assertEquals(REPETITIONS_DEFAULT, results.getCPN()[2]);
		assertTrue( results.getSD() * 6 + results.getMean() >  results.getMaxDuration() );

	}
}

