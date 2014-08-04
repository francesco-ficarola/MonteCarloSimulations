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
    UNIFORM(2, new String[]{"parameter noname", "parameter noname"}, "Uniform"),
    TRIANGULAR(3, new String[]{"parameter noname", "parameter noname", "parameter noname"}, "Triangular"),
    BETA(2, new String[] {"parameter p", "parameter q" }, "Beta"),
    GAUSSIAN(2, new String[]{"parameter noname", "parameter noname"}, "Gaussian"),
    EXPONENTIAL(1, new String[]{"parameter noname"}, "Exponential");

    private final int numGUIParams;
    private final List<String> paramNamesGUI;
    private final String name;

    Distribution(int numGUIParams, String[] paramNamesGUI, String name) {
        this.numGUIParams = numGUIParams;

        this.paramNamesGUI = new ArrayList<String>(numGUIParams);
        Collections.addAll(this.paramNamesGUI,paramNamesGUI);
        
        this.name = name;
    }

    public int getNumGUIParams() {
        return numGUIParams;
    }

    public List<String> getParamNamesGUI() {
        return paramNamesGUI;
    }
    
    public String getName() {
    	return this.name;
    }
}
