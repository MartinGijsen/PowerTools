/* Copyright 2012 by Martin Gijsen (www.DeAnalist.nl)
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

package org.powertools.engine.instructions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

import org.powertools.engine.ExecutionException;
import org.powertools.engine.TestLine;


final class Client {
    private static final int ONE_SECOND   = 1000;

    private static final String SEPARATOR = "|";
    private static final String OK        = "ok";
    private static final String ERROR     = "error";

    private final String mHostName;
    private final int mPortNr;

    private Socket mSocket;
    private BufferedWriter mWriter;
    private BufferedReader mReader;

    private String mRequest;
    private String mResponse;


    Client (String hostName, int portNr) {
        mSocket   = null;
        mHostName = hostName;
        mPortNr   = portNr;
    }


    boolean isConnected () {
        return mSocket == null;
    }

    Set<String> getMethodNames () {
        return new HashSet<String> ();
    }

    boolean execute (TestLine testLine) {
        createRequest (testLine);
        sendRequest ();
        waitForResponse ();
        readResponse ();
        return processResponse ();
    }

    private void createRequest (TestLine testLine) {
        StringBuilder sb = new StringBuilder ();
        sb.append (testLine.getPart (0));
        int nrOfParts = testLine.getNrOfParts ();
        for (int partNr = 1; partNr < nrOfParts; ++partNr) {
            sb.append (SEPARATOR).append (testLine.getPart (partNr));
        }
        mRequest = sb.toString ();
    }

    private void sendRequest () {
        try {
            //System.out.println ("out: " + mRequest);
            connectIfNotConnected ();
            mWriter.write (mRequest + "\n");
            mWriter.flush ();
        } catch (IOException ioe) {
            System.out.println ("stopping");
            try {
                mReader.close ();
            } catch (IOException anotherIoe) {
                // empty
            }
        }
    }

    private void connectIfNotConnected () {
        try {
            mSocket = new Socket (mHostName, mPortNr);
            mWriter = new BufferedWriter (new OutputStreamWriter (mSocket.getOutputStream ()));
            mReader = new BufferedReader (new InputStreamReader (mSocket.getInputStream ()));
        } catch (IOException ioe) {
            throw new ExecutionException ("could not connect to remote instructions");
        }
    }

    private void waitForResponse () {
        try {
            while (!mReader.ready ()) {
                //System.out.println ("waiting");
                Thread.sleep (ONE_SECOND);
            }
        } catch (IOException ioe) {
            // empty
        } catch (InterruptedException ie) {
            // empty
        }
    }

    private void readResponse () {
        try {
            mResponse = mReader.readLine ();
            //System.out.println ("in: '" + mResponse + "'");
        } catch (IOException ioe) {
            // empty
        }
    }

    private boolean processResponse () {
        String[] parts = mResponse.split ("\\" + SEPARATOR);
        if (parts[0].equals (OK)) {
            return Boolean.parseBoolean (parts[1]);
        } else if (parts[0].equals (ERROR)) {
            throw new ExecutionException (parts[1]);
        } else {
            throw new ExecutionException ("invalid response: " + parts[0]);
        }
    }

    void disconnect () {
        try {
            mSocket.close ();
            mSocket = null;
        } catch (IOException ioe) {
            // empty
        }
    }
}
