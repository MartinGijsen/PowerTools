/*	Copyright 2012 by Martin Gijsen (www.DeAnalist.nl)
 *
 *	This file is part of the PowerTools engine.
 *
 *	The PowerTools engine is free software: you can redistribute it and/or
 *	modify it under the terms of the GNU Affero General Public License as
 *	published by the Free Software Foundation, either version 3 of the License,
 *	or (at your option) any later version.
 *
 *	The PowerTools engine is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *	GNU Affero General Public License for more details.
 *
 *	You should have received a copy of the GNU Affero General Public License
 *	along with the PowerTools engine. If not, see <http://www.gnu.org/licenses/>.
 */

package org.powerTools.engine.sources.model;

import org.powerTools.engine.core.RunTimeImpl;
import org.powerTools.engine.sources.TestLineImpl;
import org.powerTools.engine.sources.TestSource;
import org.powerTools.engine.symbol.Scope;


/*
 * This test source generates test lines from a model.
 * Processing starts with the start node of the model.
 * A strategy object determines the edge to the next node.
 * The strategy throws a DoneException to signal it is done.
 */
public class ModelTestSource extends TestSource {
	final Model mModel;


	public ModelTestSource (String fileName, String selector, String doneCondition, Scope scope, RunTimeImpl runTime) {
		super (scope);
		mModel = new Model (fileName, selector, doneCondition, runTime);
	}


	@Override
	public final void initialize () {
		;
	}

	@Override
	public final TestLineImpl getTestLine () {
		try {
			String instruction;
			do {
				instruction = mModel.getNextEdge ().mAction;
			} while ("".equals (instruction));
			//mTestLine.setParts (getParts (instruction));
			mTestLine.parseQuotedInstruction (instruction);
			return mTestLine;
		} catch (DoneCondition.DoneException e) {
			return null;
		}
	}

//	private List<String> getParts (String instruction) {
//		final String[] partsArray	= instruction.split ("\"");
//		final List<String> parts	= new LinkedList<String> ();		
//
//		parts.add (getInstructionName (partsArray));
//
//		for (int partNr = 1; partNr < partsArray.length; partNr += 2) {
//			parts.add (partsArray[partNr]);
//		}
//		
//		return parts;
//	}
//	
//	private String getInstructionName (String[] parts) {
//		String instructionName = parts[0];
//		for (int partNr = 2; partNr < parts.length; partNr += 2) {
//			instructionName += " _ " + parts[partNr];
//		}
//		if (parts.length % 2 == 0) {
//			instructionName += " _";
//		}
//		return instructionName;
//	}
}