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

package org.powertools.engine;


public abstract class Function {
    private final String mName;
    private final int mNrOfParams;

    public Function (String name, int nrOfParameters) {
        mName       = name;
        mNrOfParams = nrOfParameters;
    }

    public final String getName () {
        return mName;
    }
    
    public final int getNrOfParameters () {
        return mNrOfParams;
    }

    public final String checkNrOfArgsAndExecute (String[] args) {
        if (args.length != mNrOfParams) {
            throw new ExecutionException (String.format ("function '%s' needs %d arguments, got %d", getName (), mNrOfParams, args.length));
        }
        return execute (args);
    }
    
    public abstract String execute (String[] args);
}
