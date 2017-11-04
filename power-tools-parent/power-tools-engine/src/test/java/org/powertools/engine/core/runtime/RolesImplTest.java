/*	Copyright 2013-2014 by Martin Gijsen (www.DeAnalist.nl)
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

package org.powertools.engine.core.runtime;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.powertools.engine.BusinessDayChecker;
import org.powertools.engine.Context;
import org.powertools.engine.ExecutionException;
import org.powertools.engine.Roles;
import org.powertools.engine.RunTime;
import org.powertools.engine.Symbol;
import org.powertools.engine.TestRunResultPublisher;
import org.powertools.engine.Currencies;
import org.powertools.engine.Functions;
import org.powertools.engine.Scope;
import org.powertools.engine.expression.EvaluatedExpression;
import org.powertools.engine.symbol.ScopeImpl;
import org.powertools.engine.util.PowerToolsParser;


public class RolesImplTest {
    private final String SYSTEM_NAME = "systemName";
    private final String ROLE_NAME   = "roleName";
    private final String DOMAIN_NAME = "domain name";
    private final String USER_NAME   = "user name";
    private final String PASSWORD    = "password";

    private final RunTime mRunTime   = new RunTimeImpl ();

    @Test
    public void testAddRoleWithInvalidData () {
        RolesImpl roles = new RolesImpl (mRunTime);
        try {
            roles.addRole ("", DOMAIN_NAME, USER_NAME, PASSWORD);
            fail ("no exception");
        } catch (ExecutionException ee) {
            // ok
        }
        try {
            roles.addRole (ROLE_NAME, DOMAIN_NAME, "", PASSWORD);
            fail ("no exception");
        } catch (ExecutionException ee) {
            // ok
        }
    }

    @Test
    public void testAddRoleWithoutSystem () {
        RolesImpl roles = new RolesImpl (mRunTime);
        roles.addRole (ROLE_NAME, DOMAIN_NAME, USER_NAME, PASSWORD);
        assertEquals (DOMAIN_NAME, roles.getDomain (ROLE_NAME));
        assertEquals (USER_NAME, roles.getUsername (ROLE_NAME));
        assertEquals (PASSWORD, roles.getPassword (ROLE_NAME));
    }

    @Test
    public void testAddRoleWithSystem () {
        RolesImpl roles = new RolesImpl (mRunTime);
        roles.addRole (SYSTEM_NAME, ROLE_NAME, DOMAIN_NAME, USER_NAME, PASSWORD);
        assertEquals (DOMAIN_NAME, roles.getDomain (SYSTEM_NAME, ROLE_NAME));
        assertEquals (USER_NAME, roles.getUsername (SYSTEM_NAME, ROLE_NAME));
        assertEquals (PASSWORD, roles.getPassword (SYSTEM_NAME, ROLE_NAME));
    }

    @Test
    public void testAddRoleWithEmptySystem () {
        RolesImpl roles = new RolesImpl (mRunTime);
        roles.addRole ("", ROLE_NAME, DOMAIN_NAME, USER_NAME, PASSWORD);
        assertEquals (DOMAIN_NAME, roles.getDomain (ROLE_NAME));
        assertEquals (USER_NAME, roles.getUsername (ROLE_NAME));
        assertEquals (PASSWORD, roles.getPassword (ROLE_NAME));
    }


    private class RunTimeImpl implements RunTime {
        private final Scope mGlobalScope = new ScopeImpl (null);

        public Scope getGlobalScope () {
            return mGlobalScope;
        }

        public Symbol getSymbol (String name) {
            throw new ExecutionException ("symbol not found");
        }


        public Context getContext() {
            throw new UnsupportedOperationException ("Not supported yet.");
        }

        public void reportValueError(String expression, String actualValue, String expectedValue) {
            throw new UnsupportedOperationException ("Not supported yet.");
        }

        public void reportError(String message) {
            throw new UnsupportedOperationException ("Not supported yet.");
        }

        public void reportStackTrace(Exception e) {
            throw new UnsupportedOperationException ("Not supported yet.");
        }

        public void reportWarning(String message) {
            throw new UnsupportedOperationException ("Not supported yet.");
        }

        public void reportValue(String expression, String value) {
            throw new UnsupportedOperationException ("Not supported yet.");
        }

        public void reportInfo(String message) {
            throw new UnsupportedOperationException ("Not supported yet.");
        }

        public void reportLink(String url) {
            throw new UnsupportedOperationException ("Not supported yet.");
        }

        public Scope getCurrentScope() {
            throw new UnsupportedOperationException ("Not supported yet.");
        }

        public void setValue(String name, String value) {
            throw new UnsupportedOperationException ("Not supported yet.");
        }

        public void copyStructure(String target, String source) {
            throw new UnsupportedOperationException ("Not supported yet.");
        }

        public void clearStructure(String name) {
            throw new UnsupportedOperationException ("Not supported yet.");
        }

        public Roles getRoles() {
            throw new UnsupportedOperationException ("Not supported yet.");
        }

        public void enterTestCase(String name, String description) {
            throw new UnsupportedOperationException ("Not supported yet.");
        }

        public void leaveTestCase() {
            throw new UnsupportedOperationException ("Not supported yet.");
        }

        public boolean addSharedObject(String name, Object object) {
            throw new UnsupportedOperationException ("Not supported yet.");
        }

        public Object getSharedObject(String name) {
            throw new UnsupportedOperationException ("Not supported yet.");
        }

        public void setBusinessDayChecker(BusinessDayChecker checker) {
            throw new UnsupportedOperationException ("Not supported yet.");
        }

        public EvaluatedExpression evaluateExpression(String expression) {
            throw new UnsupportedOperationException ("Not supported yet.");
        }

        public TestRunResultPublisher getPublisher() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void addCurrency (String name, int nrOfDecimals) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    
        public void addCurrencyAlias (String alias, String name) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void removeCurrency (String name) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public Currencies getCurrencies () {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public Functions getFunctions() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public PowerToolsParser getPowerToolsParser () {
            throw new UnsupportedOperationException ("Not supported yet.");
        }

        public void abortTestCase () {
            throw new UnsupportedOperationException ("Not supported yet.");
        }
    }
}
