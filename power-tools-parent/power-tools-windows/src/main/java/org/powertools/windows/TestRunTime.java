/* Copyright 2014 by Martin Gijsen (www.DeAnalist.nl)
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

package org.powerTools.windows;

import org.powertools.engine.BusinessDayChecker;
import org.powertools.engine.Context;
import org.powertools.engine.Roles;
import org.powertools.engine.RunTime;
import org.powertools.engine.Symbol;
import org.powertools.engine.symbol.Scope;


final class TestRunTime implements RunTime {
    @Override
    public Context getContext() {
        return null;
    }

    @Override
    public boolean addSharedObject(String name, Object object) {
        return false;
    }

    @Override
    public Object getSharedObject(String name) {
        return null;
    }

    @Override
    public void reportValueError(String expression, String actualValue, String expectedValue) {
        System.out.println (String.format ("error: '%s' is not '%s'", actualValue, expectedValue));
    }

    @Override
    public void reportError(String message) {
        System.out.println ("error: " + message);
    }

    @Override
    public void reportStackTrace(Exception e) {

    }

    @Override
    public void reportWarning(String message) {

    }

    @Override
    public void reportValue(String expression, String value) {

    }

    @Override
    public void reportInfo(String message) {

    }

    @Override
    public Symbol getSymbol(String name) {
        return null;
    }

    @Override
    public void setValue(String name, String value) {

    }

    @Override
    public void copyStructure(String target, String source) {

    }

    @Override
    public void clearStructure(String name) {

    }

    public void reportLink(String url) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String evaluateExpression(String expression) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Scope getGlobalScope() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Scope getCurrentScope() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Roles getRoles() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean enterTestCase(String name, String description) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean leaveTestCase() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void setBusinessDayChecker(BusinessDayChecker checker) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
