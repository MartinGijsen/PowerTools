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

package org.powertools.engine.core;

import java.util.HashMap;
import java.util.Map;
import org.powertools.engine.BusinessDayChecker;
import org.powertools.engine.Context;
import org.powertools.engine.Currencies;
import org.powertools.engine.Functions;
import org.powertools.engine.Roles;
import org.powertools.engine.RunTime;
import org.powertools.engine.Scope;
import org.powertools.engine.Symbol;
import org.powertools.engine.TestRunResultPublisher;
import org.powertools.engine.core.runtime.Factory;
import org.powertools.engine.core.runtime.TestSourceStack;
import org.powertools.engine.expression.ExpressionEvaluator;
import org.powertools.engine.ProcedureRunner;
import org.powertools.engine.expression.EvaluatedExpression;
import org.powertools.engine.instructions.ParameterConvertors;
import org.powertools.engine.reports.TestRunResultPublisherImpl;
import org.powertools.engine.sources.TestLineImpl;
import org.powertools.engine.sources.TestSource;
import org.powertools.engine.symbol.Util;
import org.powertools.engine.util.PowerToolsParser;


/**
 * The runtime provides a full API for instructions (and engine logic) to access
 * 1) the test source stack, 2) reporting, 3) roles and 4) shared objects.
 * <BR/><BR/>
 * The test source stack is used for creating and finding symbols.
 * <BR/><BR/>
 * Reporting concerns errors, warnings and info messages.
 * <BR/><BR/>
 * Roles represent combinations of user names and passwords in a test environment.
 * <BR/><BR/>
 * Shared objects are used to exchange information between instruction sets.
 */
public final class RunTimeImpl implements RunTime, ProcedureRunner {
    final TestSourceStack mSourceStack;

    private final Context mContext;
    private final ExpressionEvaluator mEvaluator;
    private final TestRunResultPublisher mPublisher;
    private final Map<String, Object> mSharedObjects;
    private final Roles mRoles;
    private final Currencies mCurrencies;
    private final Functions mFunctions;
    private final PowerToolsParser mPowerToolsParser;
    private final ParameterConvertors mParameterConvertors;


    public RunTimeImpl (Context context) {
        mContext             = context;
        mSourceStack         = Factory.createTestSourceStack ();
        mPublisher           = new TestRunResultPublisherImpl ();
        mSharedObjects       = new HashMap<String, Object> ();
        mRoles               = Factory.createRoles (this);
        mCurrencies          = Factory.createCurrencies ();
        mFunctions           = Factory.createFunctions ();
        mEvaluator           = new ExpressionEvaluator (mFunctions);
        mPowerToolsParser    = new PowerToolsParser ();
        mParameterConvertors = new ParameterConvertors (this);
    }


    @Override
    public Context getContext () {
        return mContext;
    }


    @Override
    public void enterTestCase (String name, String description) {
        mSourceStack.createAndPushTestCase (name, description);
        mPublisher.publishTestCaseBegin (name, description);
    }

    @Override
    public void leaveTestCase () {
        mSourceStack.popTestCase ();
        mPublisher.publishTestCaseEnd ();
    }

    @Override
    public void abortTestCase () {
        mSourceStack.abortTestCase ();
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
    public void reportLink (String url) {
        mPublisher.publishLink (url);
    }

    @Override
    public TestRunResultPublisher getPublisher () {
        return mPublisher;
    }


    @Override
    public Scope getGlobalScope () {
        return mSourceStack.getGlobalScope ();
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
        Symbol sourceSymbol = mSourceStack.getCurrentScope ().getSymbol (source);
        Symbol targetSymbol = mSourceStack.getCurrentScope ().getSymbol (target);
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

    public void invokeSource (TestSource source) {
        mSourceStack.initAndPush (source);
    }

    @Override
    public EvaluatedExpression evaluateExpression (String expression) {
        return mEvaluator.evaluate (expression, getCurrentScope ());
    }
     
    void evaluateExpressions (TestLineImpl testLine) {
        Scope scope   = getCurrentScope ();
        int nrOfParts = testLine.getNrOfParts ();
        for (int partNr = 0; partNr < nrOfParts; ++partNr) {
            final String part = testLine.getPart (partNr);
            if (part.startsWith ("?")) {
                EvaluatedExpression result = mEvaluator.evaluate (part, scope);
                testLine.setEvaluatedPart (partNr, result.getSymbolValues (), result.getValue ());
            }
        }
    }

    @Override
    public Roles getRoles () {
        return mRoles;
    }

    @Override
    public Functions getFunctions () {
        return mFunctions;
    }

    @Override
    public void setBusinessDayChecker (BusinessDayChecker checker) {
        ExpressionEvaluator.setBusinessDayChecker (checker);
    }

    
    @Override
    public Currencies getCurrencies () {
        return mCurrencies;
    }

    @Override
    public PowerToolsParser getPowerToolsParser() {
        return mPowerToolsParser;
    }
    
    public void setDefaultDateFormat (String format) {
        mPowerToolsParser.setDefaultDateFormat (format);
    }
    
    public ParameterConvertors getParameterConvertors () {
        return mParameterConvertors;
    }
}
