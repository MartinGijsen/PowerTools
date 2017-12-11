package org.powertools.demo;

import java.time.LocalDate;
import java.time.LocalTime;
import org.powertools.engine.RunTime;


public class DemoInstructions {
    private final RunTime _runTime;
    
    public DemoInstructions(RunTime runTime) {
        _runTime = runTime;
    }

    public void throwAnException () {
        String s = null;
        s.isEmpty ();
    }
    
    public void logByte_ (byte b) {
        _runTime.reportInfo (String.format ("received argument '%d' as a byte", b));
    }

    public void logShort_ (short s) {
        _runTime.reportInfo (String.format ("received argument '%d' as a short", s));
    }

    public void logInt_ (int i) {
        _runTime.reportInfo (String.format ("received argument '%d' as an integer", i));
    }

    public void logLong_ (long l) {
        _runTime.reportInfo (String.format ("received argument '%d' as a long", l));
    }

    public void logFloat_ (float f) {
        _runTime.reportInfo (String.format ("received argument '%f' as a float", f));
    }

    public void logDouble_ (double d) {
        _runTime.reportInfo (String.format ("received argument '%f' as a double", d));
    }

    public void logBoolean_ (boolean b) {
        _runTime.reportInfo (String.format ("received argument '%b' as a boolean", b));
    }

    public void logChar_ (char c) {
        _runTime.reportInfo (String.format ("received argument '%c' as a char", c));
    }

    public void logString_ (String s) {
        _runTime.reportInfo (String.format ("received argument '%s' as a string", s));
    }
    
    public void logDate_ (LocalDate date) {
        _runTime.reportInfo (String.format ("received argument '%s' as a date", date));
    }
    
    public void logTime_ (LocalTime time) {
        _runTime.reportInfo (String.format ("received argument '%s' as a time", time));
    }
}
