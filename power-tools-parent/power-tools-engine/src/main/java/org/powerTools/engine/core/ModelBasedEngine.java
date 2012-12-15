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
import org.powerTools.engine.sources.model.EdgeSelectionStrategyFactory;
import org.powerTools.engine.sources.model.ModelTestSource;
import org.powerTools.engine.symbol.Scope;


/**
 * The model based engine obtains instructions from a model.
 * It reports to an HTML log.
 */
public class ModelBasedEngine extends Engine {
	public static void main (String[] args) {
		if (args.length != 2) {
			System.err.println ("Please specify a directory and a file");
		} else {
			new ModelBasedEngine (args[0]).run (args[1]);
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
		run (new ModelTestSource (fileName, EdgeSelectionStrategyFactory.createRandomEdgeSelector (), Scope.getGlobalScope ()));
	}
}