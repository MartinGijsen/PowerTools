/* Copyright 2012-2014 by Martin Gijsen (www.DeAnalist.nl)
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

package org.powertools.engine.fitnesse.sources;

import fit.Fixture;
import fit.Parse;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.powertools.engine.TestRunResultPublisher;
import org.powertools.engine.fitnesse.Reference;
import org.powertools.engine.sources.Procedure;
import org.powertools.engine.sources.TestLineImpl;
import org.powertools.engine.symbol.Scope;


public final class InstructionSource extends FitNesseTestSource {
    private final List<String> mParameterNames;

    private Procedure mProcedure;


    InstructionSource (Fixture fixture, Parse table, Scope scope, String logFilePath, TestRunResultPublisher publisher, Reference reference) {
        super (fixture, table, scope, logFilePath, publisher, reference);
        mParameterNames = new ArrayList<String> ();
    }

    InstructionSource (Parse table, Scope scope, String logFilePath, TestRunResultPublisher publisher, Reference reference) {
        super (table, scope, logFilePath, publisher, reference);
        mParameterNames = new ArrayList<String> ();
    }


    @Override
    public void initialize () {
        processFixtureLine ();
        processHeaderLine ();
    }

    private void processHeaderLine () {
        mProcedure = null;
        mRow       = mRow.more;
        if (mRow != null) {
            Parse currentCell      = mRow.parts;
            String instructionName = currentCell.text ();

            int position = 0;
            while ((currentCell = currentCell.more) != null) {
                final String text = currentCell.text();
                if (text.isEmpty ()) {
                    mTestLine.setParts (readSentence (mRow.parts));
                    mPublisher.publishTestLine (mTestLine);
                    mPublisher.publishError ("empty cells not allowed");
                    mPublisher.publishEndOfTestLine ();
                    break;
                } else if (++position % 2 != 0) {
                    mParameterNames.add (text);
                    instructionName += " _";
                } else {
                    instructionName += " " + text;
                }
            }

            linkToLogFile (mRow.parts);
            mTestLine.setParts (readSentence (mRow.parts));
            mPublisher.publishTestLine (mTestLine);

            mProcedure = new Procedure (instructionName);

            for (String parameterName : mParameterNames) {
                boolean isOutput         = (parameterName.startsWith (OUTPUT_PARAMETER_PREFIX));
                String realParameterName = (isOutput ? parameterName.substring (OUTPUT_PARAMETER_PREFIX.length ()).trim () : parameterName);
                mProcedure.addParameter (realParameterName, isOutput);
            }
        }
    }

    public Procedure getProcedure () {
        return mProcedure;
    }

    @Override
    public TestLineImpl getTestLine () {
//        List<List<String>> table = new LinkedList<List<String>> ();
//        if (mRow != null) {
//            // Do not return instructions for execution.
//            // Add them to the procedure instead, unevaluated.
//            // They are executed when the procedure is called.
//            while ((mRow = mRow.more) != null) {
//                table.add (mRow.parts.parts);
//                processSentence (readSentence (mRow.parts));
//            }
//        }
//        mProcedure.addTable (table);

        // indicate there are no instructions to execute from this source
        return null;
    }

    public boolean addScenario (Parse table) {
        String tableType = table.parts.parts.text ().toLowerCase ();
//        if (type.equals ("instruction")) {
//            mRow = table.parts.more.more;
//        } else if (type.equals ("scenario")) {
//            mRow = table.parts.more;
//        } else {
//            throw new ExecutionException ("invalid table type for inside instruction: " + type);
//        }
        if (tableType.equals ("scenario")) {
            mRow = table.parts;
            processFixtureLine ();
        }

        List<List<String>> instructions = new LinkedList<List<String>> ();
        if (mRow != null) {
            while ((mRow = mRow.more) != null) {
                List<String> instruction = readSentence (mRow.parts);
                instructions.add (instruction);
                processSentence (instruction);
            }
        }

        if (mProcedure != null) {
            mProcedure.addTable (instructions);
        }
        if (!tableType.equals ("instruction")) {
            mPublisher.publishEndOfSection ();
        }
        
        return !instructions.isEmpty ();
    }

    private void processSentence (List<String> parts) {
        if (!parts.isEmpty ()) {
            linkToLogFile (mRow.parts);
            mTestLine.setParts (parts);
            mPublisher.publishTestLine (mTestLine);
        }
    }

    public void addData (Parse table) {
        mRow = table.parts;
        processFixtureLine ();
        String instructionName = getDataInstructionName ();
        List<List<String>> instructions = new LinkedList<List<String>> ();
        if (mRow != null) {
            while ((mRow = mRow.more) != null) {
                List<String> instruction = readData (instructionName);
                instructions.add (instruction);
                processData (instruction);
            }
        }

        mProcedure.addTable (instructions);
        mPublisher.publishEndOfSection ();
    }
    
    private String getDataInstructionName () {
        mRow = mRow.more;
        if (mRow != null) {
            fillDataTestLine ();
            mPublisher.publishTestLine (mTestLine);

            int nrOfParts                   = mTestLine.getNrOfParts ();
            StringBuilder instructionNameSb = new StringBuilder ();
            for (int partNr = 1; partNr < nrOfParts; ++partNr) {
                String part = mTestLine.getPart (partNr);
                if (part.isEmpty ()) {
                    mPublisher.publishError ("empty column name(s)");
                    break;
                } else {
                    for (String word : part.split (" +")) {
                        instructionNameSb.append (word).append (" ");
                    }
                    instructionNameSb.append ("_ ");
                }
            }
            mPublisher.publishEndOfTestLine ();

            return instructionNameSb.toString ().trim ();
        }
        return null;
    }

    private void fillDataTestLine () {
        List<String> parts = new ArrayList<String> ();
        parts.add ("");
//        Parse currentCell = mRow.parts;
        for (Parse currentCell = mRow.parts; currentCell != null; currentCell = currentCell.more) {
//        do {
            parts.add (currentCell.text ());
//            currentCell = currentCell.more;
//        } while (currentCell != null);
        }
        mTestLine.setParts (parts);
    }

    private List<String> readData (String instructionName) {
        List<String> parts = new ArrayList<String> ();
        parts.add (instructionName);
        for (Parse currentCell = mRow.parts; currentCell != null; currentCell = currentCell.more) {
            parts.add (currentCell.text ());
        }
        mTestLine.setParts (parts);
        return parts;
    }

    private void processData (List<String> parts) {
        if (!parts.isEmpty ()) {
            linkToLogFile (mRow.parts);
            mTestLine.setParts (parts);
            mPublisher.publishTestLine (mTestLine);
        }
    }
}
