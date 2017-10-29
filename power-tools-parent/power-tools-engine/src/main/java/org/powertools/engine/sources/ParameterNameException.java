package org.powertools.engine.sources;


// TODO: have one exception for non-fatal and one for fatal?
public class ParameterNameException extends Exception {
    public ParameterNameException (String message, String param1) {
        super (String.format (message, param1));
    }
}
