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

package org.powertools.ssh;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.powertools.engine.ExecutionException;


public class Instructions {
    private final Map<String, Server> mServers;
    private final Map<String, Shell> mShells;

    
    public Instructions () {
        mServers = new HashMap<String, Server> ();
        mShells  = new HashMap<String, Shell> ();
    }


    // TODO: use PT roles
    public void registerServer (String serverName, String hostName, String userName, String password) {
        registerServer (serverName, new Server (serverName, hostName, userName, password));
    }
    
    public void registerServer (String serverName, String hostName) {
        registerServer (serverName, new Server (serverName, hostName));
    }
    
    private void registerServer (String name, Server server) {
        if (mServers.containsKey (name)) {
            throw new ExecutionException ("a server named '%s' is already registered", name);
        } else {
            mServers.put (name, server);
        }
    }
    
    public void registerUser (String serverName, String userName, String password) {
        getServer (serverName).add (new User (userName, password));
    }
    
    private Server getServer (String name) {
        Server server = mServers.get (name);
        if (server == null) {
            throw new ExecutionException ("no server named '%s' is registered", name);
        }
        return server;
    }

    
    // commands
    public String executeCommand (String command) throws IOException {
        if (mServers.size () == 1 && mShells.isEmpty ()) {
            for (Server server : mServers.values ()) {
                return server.executeCommand (command, server.getUser ());
            }
        } else if (mShells.size () == 1 && mServers.isEmpty ()) {
            for (Shell shell : mShells.values ()) {
                shell.send (command);
                return shell.getOutput ();
            }
        }
        throw new ExecutionException ("there is not 1 server registered but %d", mServers.size ());
    }
    
    public void executeCommandOnServer (String serverName, String command) {
        Server server = getServer (serverName);
        server.executeCommand (command, server.getUser ());
    }

    public String executeCommandInShell (String shellName, String command) throws IOException {
        Shell shell = getShell (shellName);
        shell.send (command);
        return shell.getOutput ();
    }


    // shells
    public void openShell (String name, String serverName) {
        if (isKnownShellName (name)) {
            throw new ExecutionException ("a shell named '%s' already exists", name);
        } else {
            mShells.put (name, getServer (serverName).openShell (name));
        }
    }

    private boolean isKnownShellName (String name) {
        return mShells.containsKey (name);
    }

    public void closeShell (String name) throws IOException {
        getShell (name).close ();
    }
    
    private Shell getShell (String name) {
        Shell shell = mShells.get (name);
        if (shell == null) {
            throw new ExecutionException ("no shell named '%s' exists", name);
        }
        return shell;
    }

    public void cleanup () {
        disconnectShells ();
    }
    
    private void disconnectShells () {
        for (Shell shell : mShells.values ()) {
            try {
                shell.close ();
            } catch (IOException ioe) {
                // ignore
            }
        }
        mShells.clear ();
    }
}
