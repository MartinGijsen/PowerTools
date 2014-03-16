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

package org.powertools.engine.fitnesse.sources;

import org.powertools.engine.TestRunResultPublisher;
import org.powertools.engine.fitnesse.Reference;
import org.powertools.engine.fitnesse.fixtures.DataFixture;
import org.powertools.engine.fitnesse.fixtures.InstructionFixture;
import org.powertools.engine.symbol.Scope;

import fit.Fixture;
import fit.Parse;


public class TestSourceFactory {
    public FitNesseTestSource createScenarioSource (Fixture fixture, Parse table, Scope scope, String logFilePath, TestRunResultPublisher publisher, Reference reference) {
        return new ScenarioSource (fixture, table, scope, logFilePath, publisher, reference);
    }

    public FitNesseTestSource createScenarioSource (Parse table, Scope scope, String logFilePath, TestRunResultPublisher publisher, Reference reference) {
        return new ScenarioSource (table, scope, logFilePath, publisher, reference);
    }

    public FitNesseTestSource createTestCaseSource (Fixture fixture, Parse table, Scope scope, String logFilePath, TestRunResultPublisher publisher, Reference reference) {
        return new TestCaseSource (fixture, table, scope, logFilePath, publisher, reference);
    }

    public FitNesseTestSource createTestCaseSource (Parse table, Scope scope, String logFilePath, TestRunResultPublisher publisher, Reference reference) {
        return new TestCaseSource (table, scope, logFilePath, publisher, reference);
    }

    public FitNesseTestSource createDataSource (DataFixture fixture, Parse table, Scope scope, String logFilePath, TestRunResultPublisher publisher, Reference reference) {
        return new DataSource (fixture, table, scope, logFilePath, publisher, reference);
    }

    public FitNesseTestSource createDataSource (Parse table, Scope scope, String logFilePath, TestRunResultPublisher publisher, Reference reference) {
        return new DataSource (table, scope, logFilePath, publisher, reference);
    }

    public FitNesseTestSource createDummySource (Parse table, String logFilePath, TestRunResultPublisher publisher, Reference reference) {
        return new DummySource (table, logFilePath, publisher, reference);
    }

    
    public InstructionSource createInstructionSource (InstructionFixture fixture, Parse table, Scope scope, String logFilePath, TestRunResultPublisher publisher, Reference reference) {
        return new InstructionSource (fixture, table, scope, logFilePath, publisher, reference);
    }

    public InstructionSource createInstructionSource (Parse table, Scope scope, String logFilePath, TestRunResultPublisher publisher, Reference reference) {
        return new InstructionSource (table, scope, logFilePath, publisher, reference);
    }
}
