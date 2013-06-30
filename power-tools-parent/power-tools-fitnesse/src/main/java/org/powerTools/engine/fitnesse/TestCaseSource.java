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

package org.powerTools.engine.fitnesse;

import fit.Fixture;
import fit.Parse;


final class TestCaseSource extends ScenarioSource {
	private String[] mArgs;
	
	TestCaseSource (Fixture fixture, Parse table, String logFilePath) {
		super (fixture, table, logFilePath);
		mArgs = fixture.getArgs ();
	}


	@Override
	public void initialize () {
		String id			= mArgs.length >= 1 ? mArgs[0] : "";
		String description	= mArgs.length >= 2 ? mArgs[1] : "";
		
		copyFixtureLine ();
		linkToLogFile (mRow.parts);
		mPublisher.publishTestLine (mTestLine);
		if (mArgs.length > 2) {
			mPublisher.publishError ("too many arguments (test case name and description expected)");
		}
		mPublisher.publishEndOfTestLine ();
		mPublisher.publishTestCaseBegin (id, description);
	}

	private void copyFixtureLine () {
		int nrOfArgs = mArgs.length;
		mTestLine.createParts (nrOfArgs + 1);
		mTestLine.setPart (0, mRow.parts.text ());
		for (int argNr = 0; argNr < nrOfArgs; ++argNr) {
			mTestLine.setPart (argNr + 1, mArgs[argNr]);
		}
	}
	
	@Override
	public void cleanup () {
		mPublisher.publishTestCaseEnd ();
	}
}