package mcsimulations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Description : Available probabilistic distributions
 * Date: 11/25/13
 * Time: 10:31 AM
 */
public enum Distribution {

    //ToDo (bit-man) change parameter names accordingly
    UNIFORM(2, new String[]{"parameter noname", "parameter noname"}),
    TRIANGULAR(3, new String[]{"parameter noname", "parameter noname", "parameter noname"}),
    BETA(3,
        new String[] {"parameter Optimistic Time", "parameter Pessimistic Time", "parameter Most Likely Time" }),
    GAUSSIAN(2, new String[]{"parameter noname", "parameter noname"}),
    EXPONENTIAL(1, new String[]{"parameter noname"});

    private final int numGUIParams;

    private final List<String> paramNamesGUI;

    Distribution(int numGUIParams, String[] paramNamesGUI) {
        this.numGUIParams = numGUIParams;

        this.paramNamesGUI = new ArrayList<String>(numGUIParams);
        Collections.addAll(this.paramNamesGUI,paramNamesGUI);
    }

    public int getNumGUIParams() {
        return numGUIParams;
    }

    public List<String> getParamNamesGUI() {
        return paramNamesGUI;
    }
}
