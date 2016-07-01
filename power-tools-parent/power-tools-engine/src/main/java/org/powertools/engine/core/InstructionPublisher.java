/* Copyright 2014 by Martin Gijsen (www.DeAnalist.nl)
 *
 * This file is part of the PowerTools engine.
 *
 * The PowerTools engine is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * The PowerTools engine is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with the PowerTools engine. If not, see <http://www.gnu.org/licenses/>.
 */
package org.powertools.engine.core;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import org.powertools.engine.Instruction;


final class InstructionPublisher {
    private final String mResultsDirectory;
    private final Set<String> mProcessedClasses;
    
    private PrintWriter mWriter;
    
    
    InstructionPublisher (String resultsDirectory) {
        mProcessedClasses = new HashSet<String> ();
        mResultsDirectory = resultsDirectory;
    }
    
    void publishInstructions (String className, Class<?> aClass) {
        if (!mProcessedClasses.contains (className)) {
            publishInstructions (aClass);
            mProcessedClasses.add (className);
        }
    }

    private void publishInstructions (Class<?> aClass) {
        openFile (aClass.getSimpleName ());
        for (Method method : aClass.getDeclaredMethods ()) {
            Instruction annotation = (Instruction) method.getAnnotation (Instruction.class);
            if (annotation != null) {
                writeMethodName (method.getName ());
                writeDescription (annotation.description ());
                for (String parameter : annotation.parameters ()) {
                    writeParameterDescription (parameter);
                }
                finishInstruction ();
            }
        }
        closeFile ();
    }

    private void openFile (String fileName) {
        try {
            mWriter = new PrintWriter (new FileOutputStream (mResultsDirectory + fileName + ".html"));
            mWriter.println ("<HTML><BODY>");
            mWriter.println ("<STYLE type='text/css'>");
            mWriter.println ("table { border:1px solid black; border-collapse:collapse; }");
            mWriter.println ("td { border:1px solid black; padding:3px; }");
            mWriter.println ("</STYLE>");
        } catch (FileNotFoundException fnfe) {
            // ignore
        }
    }

    private void writeMethodName (String name) {
        mWriter.println ("<TABLE>");
        mWriter.format ("<TR><TD>Name</TD><TD>%s</TD></TR>", name).println ();
    }
    
    private void writeDescription (String description) {
        mWriter.format ("<TR><TD>Description</TD><TD>%s</TD></TR>\n", description).println ();
    }
    
    private void writeParameterDescription (String description) {
        mWriter.format ("<TR><TD>Parameter</TD><TD>%s</TD></TR>\n", description).println ();
    }
    
    private void finishInstruction () {
        mWriter.println ("</TABLE>");
        mWriter.println ("<BR/>");
    }
    
    private void closeFile () {
        mWriter.println ("<BODY/><HTML/>");
        mWriter.close ();
        mWriter = null;
    }
}
