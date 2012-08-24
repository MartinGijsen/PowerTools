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

package org.powerTools.engine;


/**
 * The ExecutionException is used to report fatal instruction execution errors.
 * They are normally caught and reported by the Engine.
 * A stack trace is only rarely relevant and specified explicitly if it is.
 */
public final class ExecutionException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	private final boolean mHasStackTrace;

	
	public ExecutionException (String errorMessage) {
		super (errorMessage);
		mHasStackTrace = false;
	}

	public ExecutionException (String errorMessage, StackTraceElement[] stackTrace) {
		super (errorMessage);
		setStackTrace (stackTrace);
		mHasStackTrace = true;
	}
	
	
	public boolean hasStackTrace () {
		return mHasStackTrace;
	}
}