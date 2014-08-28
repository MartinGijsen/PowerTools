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

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import org.powertools.engine.ExecutionException;


class Server {
    private final String mName;
    private final String mHostName;
    private final Map<String, User> mUsers;


    Server (String name, String hostName) {
        mName     = name;
        mHostName = hostName;
        mUsers    = new HashMap<String, User> ();
    }

    Server (String name, String hostName, String userName, String password) {
        this (name, hostName);
        mUsers.put (userName, new User (userName, password));
    }

    void add (User user) {
        if (mUsers.containsKey (user.mName)) {
            throw new ExecutionException (String.format ("server %s already has a user named %s", mName, user.mName));
        } else {
            mUsers.put (user.mName, user);
        }
    }

    String getName () {
        return mName;
    }
    
    String getHostName () {
        return mHostName;
    }

    User getUser (String name) {
        return mUsers.get (name);
    }
    
    User getUser () {
        if (mUsers.size () == 1) {
            for (User user : mUsers.values ()) {
                return user;
            }
        }
        throw new ExecutionException (String.format ("server %s has not 1 user but %d", mName, mUsers.size ()));
    }
    
    
    public void uploadFile (String localPath, String remotePath, User user) {
        FileInputStream fis = null;
        try {
            Session session = new JSch ().getSession (user.mName, mHostName, 22);
            session.setPassword (user.mPassword);
            session.setConfig ("StrictHostKeyChecking", "no");
            session.connect ();

            boolean ptimestamp = true;
 
            // exec 'scp -t rfile' remotely
            String command = "scp " + (ptimestamp ? "-p" : "") + " -t " + remotePath;
            ChannelExec channel = (ChannelExec) session.openChannel ("exec");
            channel.setCommand (command);
 
            // get I/O streams for remote scp
            OutputStream out = channel.getOutputStream ();
            InputStream in   = channel.getInputStream ();
 
            channel.connect ();
 
            if (checkAck (in) != 0) {
                return;
            }
 
            File _lfile = new File (localPath);
 
            if (ptimestamp) {
                command = "T " + (_lfile.lastModified () / 1000) + " 0";
                // The access time should be sent here,
                // but it is not accessible with JavaAPI ;-<
                command += (" " + (_lfile.lastModified () / 1000) + " 0\n");
                out.write (command.getBytes ());
                out.flush ();
                if (checkAck (in) != 0) {
                    return;
                }
            }
 
            // send "C0644 filesize filename", where filename should not include '/'
            long filesize = _lfile.length ();
            command = "C0644 " + filesize + " ";
            if (localPath.lastIndexOf ('/') > 0) {
                command += localPath.substring (localPath.lastIndexOf ('/') + 1);
            } else {
                command += localPath;
            }
            command += "\n";
            out.write (command.getBytes ());
            out.flush ();
            if (checkAck (in) != 0) {
                return;
            }
 
            // send the content of local file
            fis = new FileInputStream (localPath);
            byte[] buf = new byte[1024];
            while (true) {
                int len = fis.read (buf, 0, buf.length);
                if (len <= 0) break;
                out.write (buf, 0, len);
            }
            fis.close ();
            fis = null;
            // send '\0'
            buf[0] = 0;
            out.write (buf, 0, 1);
            out.flush ();
            if (checkAck (in) != 0) {
                return;
            }
            out.close ();
 
            channel.disconnect ();
            session.disconnect ();
 
            return;
        } catch (Exception e) {
            try {
                if (fis != null)
                    fis.close ();
            } catch (Exception ee) {
                ;
            }
            throw new ExecutionException (e.getMessage (), e.getStackTrace ());
        }
    }
 
    static int checkAck (InputStream in) throws IOException {
        int b = in.read ();
        // b may be 0 for success,
        //          1 for error,
        //          2 for fatal error,
        //          -1
        if (b == 0) return b;
        if (b == -1) return b;
 
        if (b == 1 || b == 2) {
            StringBuffer sb = new StringBuffer ();
            int c;
            do {
                c = in.read ();
                sb.append ((char) c);
            } while (c != '\n');
            if (b == 1) { // error
                System.out.print (sb.toString ());
            }
            if (b == 2) { // fatal error
                System.out.print (sb.toString ());
            }
        }
        return b;
    }
    public String executeCommand (String command, User user) {
        Session session     = null;
        ChannelExec channel = null;
        try {
            session = new JSch ().getSession (user.mName, mHostName);
            channel = (ChannelExec) session.openChannel ("exec");
            channel.setCommand (command);
            channel.setPty (true);
            channel.setPtyType ("vt100");
            InputStream is  = channel.getInputStream ();
            OutputStream os = channel.getOutputStream ();
            channel.connect ();
            
            return getOutput (is);
        } catch (JSchException je) {
            throw new ExecutionException (je.getMessage (), je.getStackTrace ());
        } catch (IOException ioe) {
            throw new ExecutionException (ioe.getMessage (), ioe.getStackTrace ());
        } finally {
            if (channel != null) {
                channel.disconnect ();
            }
            if (session != null) {
                session.disconnect ();
            }
        }
    }

    private String getOutput (InputStream is) throws IOException {
        StringBuilder output = new StringBuilder ();
        byte[] tmp = new byte[1024];
        while (is.available () > 0) {
            int i = is.read (tmp, 0, 1024);
            if (i < 0) {
                break;
            } else {
                output.append (new String (tmp, 0, i));
            }
        }
        return output.toString ();
    }

    public Shell openShell (String name) {
        return new Shell (name, this).open (getUser ());
    }
}
