package mcsimulations.test.tools;

/**
 * Description : Testing properties
 * Date: 11/26/13
 * Time: 2:06 PM
 */
public enum Property {
    BC_RANDOM_MAX("mcs.tests.bc.random-max", "1000"),
    BC_RANDOM_MAX_DIST("mcs.tests.bc.random-max-dist", "100");

    private final String value;

    Property(String property, String defaultValue) {
        value = System.getProperty(property, defaultValue);
    }

    public String getValue() {
        return value;
    }

    public int getValueInt() {
        return Integer.parseInt(value);
    }
}
