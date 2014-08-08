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

package org.powertools.engine.core.runtime;

import org.powertools.engine.Currencies;
import org.powertools.engine.Functions;
import org.powertools.engine.Roles;
import org.powertools.engine.RunTime;


public class Factory {
    private Factory () {
        // not used
    }
    
    public static Currencies createCurrencies () {
        return new CurrenciesImpl ();
    }

    public static Roles createRoles (RunTime runTime) {
        return new RolesImpl (runTime);
    }
    
    public static TestSourceStackImpl createTestSourceStack () {
        return new TestSourceStackImpl ();
    }
    
    public static Functions createFunctions () {
        return new FunctionsImpl ();
    }
}
