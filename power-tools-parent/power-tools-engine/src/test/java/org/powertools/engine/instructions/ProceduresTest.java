/*	Copyright 2013 by Martin Gijsen (www.DeAnalist.nl)
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

import org.junit.Test;
import static org.junit.Assert.*;
import org.powertools.engine.sources.Procedure;


public class ProceduresTest {
	@Test
	public void testGetName () {
		Procedures procedures = new Procedures (null, null);
		procedures.setup ();
		procedures.cleanup ();
		assertEquals ("something that will not match an instruction set name", procedures.getName ());
	}

	@Test
	public void testAddGetExecutor () {
		Procedures procedures = new Procedures (null, null);
		String PROCEDURE_NAME = "procedure name";
		Procedure procedure   = new Procedure (PROCEDURE_NAME);
		procedures.add (procedure);
		assertNotNull (procedures.getExecutor (PROCEDURE_NAME));
	}

	@Test
	public void testGetExecutorUnknownProcedure () {
		Procedures procedures = new Procedures (null, null);
		String PROCEDURE_NAME = "unknown procedure";
		assertNull (procedures.getExecutor (PROCEDURE_NAME));
	}
}
