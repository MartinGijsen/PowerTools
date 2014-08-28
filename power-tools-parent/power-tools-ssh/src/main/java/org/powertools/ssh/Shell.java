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

import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.powertools.engine.ExecutionException;


public class Shell {
    private final static int SECONDS = 1000;

    private final String mName;
    private final Server mServer;
    
    private Session mSession;
    private ChannelShell mChannel;
    private InputStream mIs;
    private OutputStream mOs;
    private String mOutput;
    
    
    Shell (String name, Server server) {
        mName   = name;
        mServer = server;
        mOutput   = "";
    }
    
    String getName () {
        return mName;
    }
    
    Shell open (User user) {
        try {
            mSession = new JSch ().getSession (user.mName, mServer.getHostName ());
            mSession.setPassword (user.mPassword);
            mSession.setConfig ("StrictHostKeyChecking", "no");
            mChannel.connect ();
            
            mChannel = (ChannelShell) mSession.openChannel ("shell");
            mChannel.setPty (true);
            mChannel.setPtyType ("vt100");
            mIs = mChannel.getInputStream ();
            mOs = mChannel.getOutputStream ();
            mChannel.connect ();
            return this;
        } catch (JSchException je) {
            throw newException (je);
        } catch (IOException ioe) {
            throw newException (ioe);
        }
    }
    
    void send (String text) throws IOException {
        mOutput = "";
        write (mOs, text);
    }

    boolean expect (String text, int timeout) throws IOException {
        int waitingTime = 0;
        while (!mChannel.isClosed () && waitingTime < timeout) {
            collectOutput ();
            wait (1, SECONDS);
            ++waitingTime;
            if (mOutput.contains (text)) {
                return true;
            }
        }
        return false;
    }
    
    String getOutput () throws IOException {
        collectOutput ();
        return mOutput;
    }
    
    private void collectOutput () throws IOException {
        byte[] tmp = new byte[1024];
        while (mIs.available () > 0) {
            int i = mIs.read (tmp, 0, 1024);
            if (i < 0) {
                break;
            } else {
                mOutput += new String (tmp, 0, i);
            }
        }
    }

    private void write (OutputStream os, String text) throws IOException {
        os.write ((text + "\n").getBytes ());
        os.flush ();
    }

    private void wait (int count, int unit) {
        try {
            Thread.sleep (count * unit);
        } catch (InterruptedException ie) {
            // ignore
        }
    }

//    String executeCommand (String command) {
//        try {
//            mOutput = "";
//            mOs.write ((command + "\n").getBytes ());
//            mOs.flush ();
//            return "";// ................
//        } catch (IOException ioe) {
//            throw newException (ioe);
//        }
//    }
    
    void close () throws IOException {
        mIs.close ();
        mOs.close ();
        mChannel.disconnect ();
        mChannel = null;
        mSession.disconnect ();
        mSession = null;
    }
    
    private ExecutionException newException (Exception e) {
        return new ExecutionException (e.getMessage (), e.getStackTrace ());
    }
}
