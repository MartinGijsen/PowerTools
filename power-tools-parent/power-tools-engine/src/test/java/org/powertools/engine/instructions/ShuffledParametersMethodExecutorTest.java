/* Copyright 2014 by Martin Gijsen (www.DeAnalist.nl)
 *
 * This file is part of the PowerTools.
 *
 * The PowerTools are free software: you can redistribute them and/or
 * modify them under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * The PowerTools are distributed in the hope that they will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with the PowerTools. If not, see <http://www.gnu.org/licenses/>.
 */

package org.powertools.engine.instructions;

import java.lang.reflect.Method;
import static org.junit.Assert.fail;
import org.junit.Test;
import org.powertools.engine.ExecutionException;
import org.powertools.engine.ParameterOrder;


public class ShuffledParametersMethodExecutorTest {
    private final InstructionSetImpl mInstructionSet = new InstructionSetImpl ();

    @Test
    public void testShuffledParametersMethodExecutor_tooFew () throws NoSuchMethodException {
        try {
            Method method                             = mInstructionSet.getClass ().getMethod ("Too_Few_", new Class<?>[] { String.class, String.class });
            ShuffledParametersMethodExecutor executor = new ShuffledParametersMethodExecutor (mInstructionSet, method, null);
            executor.execute (null);
            fail ("no exception");
        } catch (ExecutionException ee) {
            // ok
        }
    }

    @Test
    public void testShuffledParametersMethodExecutor_tooMany () throws NoSuchMethodException {
        try {
            Method method                             = mInstructionSet.getClass ().getMethod ("Too_Many_", new Class<?>[] { String.class, String.class });
            ShuffledParametersMethodExecutor executor = new ShuffledParametersMethodExecutor (mInstructionSet, method, null);
            executor.execute (null);
            fail ("no exception");
        } catch (ExecutionException ee) {
            // ok
        }
    }

    @Test
    public void testShuffledParametersMethodExecutor_repeatedParameter () throws NoSuchMethodException {
        try {
            Method method                             = mInstructionSet.getClass ().getMethod ("Repeated_Parameter_", new Class<?>[] { String.class, String.class });
            ShuffledParametersMethodExecutor executor = new ShuffledParametersMethodExecutor (mInstructionSet, method, null);
            executor.execute (null);
            fail ("no exception");
        } catch (ExecutionException ee) {
            // ok
        }
    }

//    @Test
//    public void testGetArguments () throws NoSuchMethodException {
//        Method method                             = mInstructionSet.getClass ().getMethod ("Instruction_Name_", new Class<?>[] { String.class, String.class });
//        ShuffledParametersMethodExecutor executor = new ShuffledParametersMethodExecutor (mInstructionSet, method, new ParameterConvertors (null, null));
//        TestLine testLine                         = new TestLineImpl (new String[] { "", "a", "b" });
//        executor.getArguments (testLine);
//        assertEquals ("b", executor.mArguments[0]);
//        assertEquals ("a", executor.mArguments[1]);
//    }

    private class InstructionSetImpl {
        @ParameterOrder ({ 2, 1 })
        public void Instruction_Name_ (String x, String y) {
            // ignore
        }

        @ParameterOrder ({ 1 })
        public void Too_Few_ (String x, String y) {
            // ignore
        }

        @ParameterOrder ({ 1, 3, 2 })
        public void Too_Many_ (String x, String y) {
            // ignore
        }

        @ParameterOrder ({ 1, 1 })
        public void Repeated_Parameter_ (String x, String y) {
            // ignore
        }
    }
}
