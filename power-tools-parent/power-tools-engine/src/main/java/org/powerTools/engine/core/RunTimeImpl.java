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

import java.util.HashMap;
import java.util.Map;

import org.powerTools.engine.Context;
import org.powerTools.engine.Symbol;
import org.powerTools.engine.RunTime;
import org.powerTools.engine.instructions.ProcedureRunner;
import org.powerTools.engine.reports.TestRunResultPublisher;
import org.powerTools.engine.sources.TestSource;
import org.powerTools.engine.symbol.Scope;
import org.powerTools.engine.symbol.StringSequence;
import org.powerTools.engine.symbol.Util;


/**
 * The runtime provides a full API for instructions (and engine logic) to
 * 1) the test source stack, 2) reporting and 3) shared objects.
 * <BR/><BR/>
 * The test source stack is used for creating and for finding symbols.
 * <BR/><BR/>
 * Reporting concerns errors, warnings and info messages.
 * <BR/><BR/>
 * Shared objects are used to exchange information between instruction sets.
 */
public class RunTimeImpl implements RunTime, ProcedureRunner {
	final TestSourceStack mSourceStack;

	private final Context mContext;
	private final TestRunResultPublisher mPublisher;
	private final Map<String, Object> mSharedObjects;

	
	public RunTimeImpl (Context context) {
		mSourceStack	= new TestSourceStack ();
		mContext		= context;
		mPublisher 		= TestRunResultPublisher.getInstance ();
		mSharedObjects	= new HashMap<String, Object> ();
	}


	@Override
	public Context getContext () {
		return mContext;
	}
	
	@Override
	public boolean addSharedObject (String name, Object object) {
		if (mSharedObjects.containsKey (name)) {
			return false;
		} else {
			mSharedObjects.put (name, object);
			return true;
		}
	}

	@Override
	public Object getSharedObject (String name) {
		if (mSharedObjects.containsKey (name)) {
			return mSharedObjects.get (name);
		} else {
			return null;
		}
	}


	@Override
	public void reportValueError (String expression, String actualValue, String expectedValue) {
		mPublisher.publishValueError (expression, actualValue, expectedValue);
	}

	@Override
	public void reportError (String message) {
		mPublisher.publishError (message);
	}

	@Override
	public void reportStackTrace (Exception e) {
		mPublisher.publishStackTrace (e);
	}

	@Override
	public void reportWarning (String message) {
		mPublisher.publishWarning (message);
	}

	@Override
	public void reportValue(String expression, String value) {
		mPublisher.publishValue (expression, value);
	}
	
	@Override
	public void reportInfo (String message) {
		mPublisher.publishInfo (message);
	}

	
	@Override
	public Symbol createLocalConstant (String name, String value) {
		return mSourceStack.getCurrentScope ().createConstant (name, value);
	}

	@Override
	public Symbol createLocalVariable (String name, String value) {
		return mSourceStack.getCurrentScope ().createVariable (name, value);
	}

	@Override
	public Symbol createLocalStructure (String name) {
		return mSourceStack.getCurrentScope ().createStructure (name);
	}

	@Override
	public Symbol createLocalNumberSequence (String name, int value) {
		return mSourceStack.getCurrentScope ().createNumberSequence (name, value);
	}

	@Override
	public StringSequence createLocalStringSequence (String name) {
		return mSourceStack.getCurrentScope ().createStringSequence (name);
	}


	@Override
	public Symbol getSymbol (String name) {
		return mSourceStack.getCurrentScope ().getSymbol (name);
	}

	@Override
	public void setValue (String name, String value) {
		mSourceStack.getCurrentScope ().getSymbol (name).setValue (value);
	}
	
	@Override
	public void copyStructure (String source, String target) {
		final Symbol sourceSymbol = mSourceStack.getCurrentScope ().getSymbol (source);
		final Symbol targetSymbol = mSourceStack.getCurrentScope ().getSymbol (target);
		Util.copy (sourceSymbol, targetSymbol, source.split (Symbol.PERIOD), target);
	}

	@Override
	public void clearStructure (String name) {
		mSourceStack.getCurrentScope ().getSymbol (name).clear (name);
	}


	@Override
	public Scope getCurrentScope () {
		return mSourceStack.getCurrentScope ();
	}
	
	public void run (TestSource source) {
		mSourceStack.initAndPush (source);
	}
}