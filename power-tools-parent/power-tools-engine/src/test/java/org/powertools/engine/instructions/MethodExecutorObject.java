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

package org.powertools.engine.instructions;

import java.util.Calendar;
import java.util.Date;


final class MethodExecutorObject {
	MethodExecutorObject () {
		// nothing
	}
	
	void voidReturningMethod () {
		// nothing
	}
	
	boolean failingMethod () {
		return false;
	}
	
	boolean noParametersMethod () {
		return true;
	}
	
	boolean intParameterMethod (int i) {
		return true;
	}
	
	boolean longParameterMethod (long l) {
		return true;
	}
	
	boolean booleanParameterMethod (boolean b) {
		return true;
	}
	
	boolean floatParameterMethod (float f) {
		return true;
	}
	
	boolean doubleParameterMethod (double d) {
		return true;
	}

	boolean dateParameterMethod (Date d) {
		return true;
	}

	boolean calendarParameterMethod (Calendar c) {
		return true;
	}

	boolean objectParameterMethod (Object o) {
		return true;
	}
}
