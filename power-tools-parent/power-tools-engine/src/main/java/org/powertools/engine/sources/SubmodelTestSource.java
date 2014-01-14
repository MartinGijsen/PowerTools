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

package org.powertools.engine.sources;

import org.powertools.engine.RunTime;
import org.powertools.engine.instructions.ProcedureRunner;
import org.powertools.engine.sources.model.Model;


/*
 * This test source generates test lines from a submodel.
 * A submodel has no implicit loop from end to start state.
 * It simply runs from the root state to a final state.
 * It can invoke submodels.
 */
final class SubmodelTestSource extends ModelTestSource {
    private final Model mParentModel;

    SubmodelTestSource (String fileName, Model parent, RunTime runTime, ProcedureRunner runner) {
        super (fileName, new Model (parent), runTime, runner);
        mParentModel = new Model (parent);
    }


    @Override
    public void initialize () {
        mModel.initialize (mFileName);
    }
}
