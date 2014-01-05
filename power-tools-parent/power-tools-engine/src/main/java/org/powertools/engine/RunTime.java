/* Copyright 2012 by Martin Gijsen (www.DeAnalist.nl)
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

package org.powertools.engine;

import org.powertools.engine.symbol.Scope;


/**
 * The runtime provides all engine functionality that an instruction may need.
 * It supports reporting errors and other execution information and
 * getting the local and global scope for getting and setting symbols.
 * <BR/>
 * It enables entering and leaving a test case.
 * <BR/>
 * It also allows an instruction (set) to create or get a shared object,
 * so data or logic can be shared between instruction sets.
 */
public interface RunTime {
    /**
     * Returns the Context that describes the context in which a test runs,
     * including the start time, the results directory and the log file path.
     * @return	the context for the test run
     */
    Context getContext ();


    /**
     * Reports an incorrect value error message to the log (and any interested report(s))
     * @param	expression	the expression being considered
     * @param	actualValue	the actual value
     * @param	expectedValue	the expected value
     */
    void reportValueError (String expression, String actualValue, String expectedValue);

    /**
     * Reports an error message to the log (and any interested report(s))
     * @param	message	the message to report
     */
    void reportError (String message);

    /**
     * Reports an exception stack trace to the log (and any interested report(s))
     * @param	e	the exception to report the stack trace of
     */
    void reportStackTrace (Exception e);

    /**
     * Reports an warning message to the log (and any interested report(s))
     * @param	message	the message to report
     */
    void reportWarning (String message);

    /**
     * Reports a value to the log (and any interested report(s))
     * @param	expression	the expression being considered
     * @param	value	the value of the expression
     */
    void reportValue (String expression, String value);

    /**
     * Reports an info message to the log (and any interested report(s))
     * @param	message	the message to report
     */
    void reportInfo (String message);

    /**
     * Reports a URL in an info message to the log (and any interested report(s))
     * so that it can be rendered in a clickable way
     * @param	url	the URL to report
     */
    void reportLink (String url);


    /**
     * Returns the global scope for getting and setting global symbols
     * @return	the global scope
     */
    Scope getGlobalScope ();

    /**
     * Returns the current local scope for getting and setting symbols
     * @return	the current local scope
     */
    Scope getCurrentScope ();


    /**
     * Returns the first symbol found with the specified name,
     * starting to look in the local scope
     * @param	name	the symbol name
     * @return	the first symbol with the specified name
     */
    Symbol getSymbol (String name);

    /**
     * Sets the value of a symbol to the specified value
     * @param	name	the name of the symbol to set
     * @param	value	the value to set the symbol to
     */
    void setValue (String name, String value);

    /**
     * Copies all fields of a (sub)structure to another (sub)structure
     * @param	target	the name of the (sub)structure to set
     * @param	source	the name of the (sub)structure to copy from
     */
    void copyStructure (String target, String source);

    /**
     * Clears a (sub)structure of all fields
     * @param	name	the name of the (sub)structure to clear
     */
    void clearStructure (String name);


    /**
     * Returns the roles object that contains registered user names and passwords
     * @return	the roles object
     */
    Roles getRoles ();


    /**
     * Enters a test case, creating a new scope for variables.
     * @param	name	the (unique) name of the test case
     * @param	description	a description of the test case
     * @return	true
     */
    boolean enterTestCase (String name, String description);

    /**
     * Leaves the test case and deletes its scope and the variables in it
     * @return	false if not in a test case, true otherwise
     */
    boolean leaveTestCase ();


    /**
     * Makes an object available to other instruction sets
     * @param	name	the name of the object
     * @param	object	the object to share
     * @return	false if an object of the specified name already exists, true otherwise
     */
    boolean addSharedObject (String name, Object object);

    /**
     * Returns an object available to other instruction sets
     * @param	name	the name of the object to get
     * @return	the specified shared object
     */
    Object getSharedObject (String name);

    /**
     * Sets the object that determines which are business days for
     * date expressions with business days (e.g. 'today + 1 business day')
     * @param	checker	the business day checker to use
     */
    void setBusinessDayChecker (BusinessDayChecker checker);
}
