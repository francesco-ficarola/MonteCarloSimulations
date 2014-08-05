package mcsimulations.test.tools;

import java.util.*;
import mcsimulations.Distribution;

public class ActivitiesBuilder {

	public static int GAUSS_MEAN_DEFAULT = 10;
	public static int GAUSS_STDDEV_DEFAULT = 1;

	public static int ACTIVITY_ID = 0;
	public static int ACTIVITY_DESCRIPTION = 1;
	public static int ACTIVITY_PRECEDENTS = 2;
	public static int ACTIVITY_DIST_NAME = 3;
	public static int ACTIVITY_DIST_PARAMS = 4;

	public static int NO_PRECEDENTS = -1;

	private int iActivity = -1;
	private ArrayList<Object> currActivity;
	private List<List<Object>> dataActArray = new ArrayList<List<Object>>();

	public ActivitiesBuilder() {};


	public ActivitiesBuilder newActivity() {
		iActivity++;
		currActivity = new ArrayList<Object>();
		dataActArray.add( currActivity );

		// 0: Activity id
		// 1: Description
		// 2: Precedences
		// 3: Distribution
		// 4: Parameters

		currActivity.add( iActivity );
		currActivity.add( "Activity " + iActivity );

		// No preceding activities
		int[] preced = new int[1];
		preced[0] = NO_PRECEDENTS;
		currActivity.add( preced );

		// Defaults to Gaussian distribution, mean = 10 and std dev = 1
		currActivity.add( "Gaussian" );
		int[] paramArray = new int[2];
		paramArray[0] = GAUSS_MEAN_DEFAULT;
		paramArray[1] = GAUSS_STDDEV_DEFAULT;
		currActivity.add( paramArray );

		return this;
	}


	public ActivitiesBuilder addPrecedent(int p) {

		int[] preced = (int[]) currActivity.get(ACTIVITY_PRECEDENTS);

		if ( preced[0] == NO_PRECEDENTS ) {
			preced[0] = p;
		} else {
			int[] newPrec = new int[preced.length + 1];
			System.arraycopy( preced, 0, newPrec, 0, preced.length );
			newPrec[preced.length] = p;
			currActivity.set(ACTIVITY_PRECEDENTS, newPrec);
		}

		return this;
	}

	public ActivitiesBuilder setDist(Distribution dist, int[] params ) {

		currActivity.set(ACTIVITY_DIST_NAME, dist.getName());
		currActivity.set(ACTIVITY_DIST_PARAMS, params );
		return this;
	}

	public List<List<Object>> build() {
		return dataActArray;
	};

}
