/*	Copyright 2012 by Martin Gijsen (www.DeAnalist.nl)
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

package org.powerTools.engine.core;

import org.powerTools.engine.ExecutionException;
import org.powerTools.engine.Roles;
import org.powerTools.engine.RunTime;
import org.powerTools.engine.Symbol;
import org.powerTools.engine.symbol.Scope;


public final class RolesImpl implements Roles {
	private static final String SYMBOL_NAME			= "roles";
	private static final String DOMAIN_FIELD_NAME	= "domain";
	private static final String USERNAME_FIELD_NAME	= "username";
	private static final String PASSWORD_FIELD_NAME	= "password";

	private final RunTime mRunTime;

	private Symbol mSymbol;


	RolesImpl (RunTime runTime) {
		 mRunTime = runTime;
	}

	@Override
	public void addRole (String role, String domain, String username, String password) {
		checkAndAddRole (SYMBOL_NAME + "." + role + ".", role, domain, username, password);
	}

	@Override
	public void addRole (String system, String role, String domain, String username, String password) {
		if (system.isEmpty ()) {
			addRole (role, domain, username, password);
		} else {
			checkAndAddRole (SYMBOL_NAME + "." + system + "." + role + ".", role, domain, username, password);
		}
	}

	private void checkAndAddRole (String prefix, String role, String domain, String username, String password) {
		checkValues (role, username, password);
		getRolesSymbol ();
		mSymbol.setValue (prefix + DOMAIN_FIELD_NAME, domain);
		mSymbol.setValue (prefix + USERNAME_FIELD_NAME, username);
		mSymbol.setValue (prefix + PASSWORD_FIELD_NAME, password);
	}

	private void checkValues (String role, String username, String password) {
		if (role.isEmpty ()) {
			throw new ExecutionException ("role name is empty");
		} else if (username.isEmpty ()) {
			throw new ExecutionException ("user name is empty");
		} else if (password.isEmpty ()) {
			mRunTime.reportWarning ("password is empty");
		}
	}
	
	@Override
	public String getDomain (String role) {
		return getValue (SYMBOL_NAME + "." + role + "." + DOMAIN_FIELD_NAME);
	}
	
	@Override
	public String getDomain (String system, String role) {
		return getValue (SYMBOL_NAME + "." + system + "." + role + "." + DOMAIN_FIELD_NAME);
	}
	
	@Override
	public String getUsername (String role) {
		return getValue (SYMBOL_NAME + "." + role + "." + USERNAME_FIELD_NAME);
	}

	@Override
	public String getUsername (String system, String role) {
		return getValue (SYMBOL_NAME + "." + system + "." + role + "." + USERNAME_FIELD_NAME);
	}
	
	@Override
	public String getPassword (String role) {
		return getValue (SYMBOL_NAME + "." + role + "." + PASSWORD_FIELD_NAME);
	}

	@Override
	public String getPassword (String system, String role) {
		return getValue (SYMBOL_NAME + "." + system + "." + role + "." + PASSWORD_FIELD_NAME);
	}

	private String getValue (String role) {
		getRolesSymbol ();
		return mSymbol.getValue (SYMBOL_NAME + "." + role + "." + DOMAIN_FIELD_NAME);
	}

	private void getRolesSymbol () {
		try {
			if (mSymbol == null) {
				mSymbol = mRunTime.getSymbol (SYMBOL_NAME);
			}
		} catch (ExecutionException ee) {
			mSymbol = Scope.getGlobalScope ().createStructure (SYMBOL_NAME);
		}
	}
}