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

package org.powerTools.engine.sources;

import org.powerTools.engine.ExecutionException;
import org.powerTools.engine.reports.TestRunResultPublisher;
import org.powerTools.engine.symbol.Scope;


/*
 * A TestSource contains instructions and provides them one by one.
 */
public abstract class TestSource {
	protected static final String OUTPUT_PARAMETER_PREFIX = "out ";

	protected final TestRunResultPublisher	mPublisher;
	protected final Scope					mScope;
	
	protected TestLineImpl mTestLine;

	
	protected TestSource (Scope scope) {
		mScope		= scope; 
		mPublisher 	= TestRunResultPublisher.getInstance ();
		mTestLine 	= new TestLineImpl ();
	}

	
	public abstract void initialize ();
	public abstract TestLineImpl getTestLine ();
	
	
	public final Scope getScope () {
		return mScope;
	}

	public TestSource create (String sourceName) {
		throw new ExecutionException ("operation not supported for this test source");
	}
}