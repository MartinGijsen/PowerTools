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

package org.powerTools.engine.core;

import org.powerTools.engine.Context;
import org.powerTools.engine.reports.ReportFactory;
import org.powerTools.engine.sources.TestSourceFactory;
import org.powerTools.engine.symbol.Scope;


/**
 * The model based engine obtains instructions from a model.
 * It reports to an HTML log.
 */
public class ModelBasedEngine extends Engine {
	public static void main (String[] args) {
		switch (args.length) {
		case 2:
			new ModelBasedEngine (args[0]).run (args[1]);
			break;
		case 4:
			new ModelBasedEngine (args[0]).run (args[1], args[2], args[3]);
			break;
		default:
			System.err.println ("Please specify a directory, model name, selection strategy and stop condition");
		}
	}


	public ModelBasedEngine (String resultsDirectory) {
		this (new RunTimeImpl (new Context (resultsDirectory)));
	}

	public ModelBasedEngine (RunTimeImpl runTime) {
		super (runTime);

		if (!ReportFactory.createKeywordsHtmlLog (runTime.getContext ())) {
			mPublisher.publishError ("could not open HTML log");
		}

		BuiltinInstructions.register (runTime, mInstructions);
	}

	@Override
	public final void run (String fileName) {
		run (fileName, "random", "all edges");
	}

	public final void run (String fileName, String selector, String condition) {
		run (TestSourceFactory.createModelTestSource (fileName, selector, condition, Scope.getGlobalScope (), mRunTime));
	}
}