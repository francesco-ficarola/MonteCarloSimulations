package mcsimulations.simulation;

/**
 * Description : Exception thrown on Simulation Compute
 * Date: 11/25/13
 * Time: 12:05 PM
 */
public class ComputeException extends Exception {
    public ComputeException(String s) {
        super(s);
    }

    public ComputeException(String message, Throwable cause) {
        super(message, cause);
    }
}
