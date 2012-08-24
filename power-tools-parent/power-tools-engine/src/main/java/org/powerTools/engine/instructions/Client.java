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

package org.powerTools.engine.instructions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Set;

import org.powerTools.engine.ExecutionException;
import org.powerTools.engine.TestLine;


final class Client {
	private static final int ONE_SECOND = 1000;
	
	private static final String SEPARATOR	= "|";
	private static final String OK			= "ok";
	private static final String ERROR		= "error";
	
	private final String mHostName;
	private final int mPortNr;
	
	private BufferedWriter mWriter;
	private BufferedReader mReader;
	private boolean mIsConnected;
	
	private String mRequest;
	private String mResponse;

	
	Client (String hostName, int portNr) {
		mIsConnected	= false;
		mHostName		= hostName;
		mPortNr			= portNr;
	}

	
	boolean isConnected () {
		return mIsConnected;
	}

	Set<String> getMethodNames () {
		
		return null;
	}

	boolean execute (TestLine testLine) {
		createRequest (testLine);
		sendRequest ();
		waitForResponse ();
		readResponse ();
		return processResponse ();
	}
	
	private void createRequest (TestLine testLine) {
		final StringBuffer sb = new StringBuffer ();
		sb.append (testLine.getPart (0));
		final int nrOfParts = testLine.getNrOfParts ();
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
				;
			}
		}
	}

	private void connectIfNotConnected () {
		try {
			final Socket socket	= new Socket (mHostName, mPortNr);
			mWriter				= new BufferedWriter (new OutputStreamWriter (socket.getOutputStream ()));
			mReader				= new BufferedReader (new InputStreamReader (socket.getInputStream ()));
			mIsConnected		= true;
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
			;
		} catch (InterruptedException ie) {
			;
		}
	}
	
	private void readResponse () {
		try {
			mResponse = mReader.readLine ();
			//System.out.println ("in: '" + mResponse + "'");
		} catch (IOException ioe) {
			;
		}
	}
	
	private boolean processResponse () {
		final String[] parts = mResponse.split ("\\" + SEPARATOR);
		if (parts[0].equals (OK)) {
			return Boolean.parseBoolean (parts[1]);
		} else if (parts[0].equals (ERROR)) {
			throw new ExecutionException (parts[1]);
		} else {
			throw new ExecutionException ("invalid response: " + parts[0]);
		}
	}
}