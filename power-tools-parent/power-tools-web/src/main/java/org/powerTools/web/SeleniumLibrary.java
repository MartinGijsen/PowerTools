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

package org.powerTools.web;

import org.openqa.selenium.server.RemoteControlConfiguration;
import org.openqa.selenium.server.SeleniumServer;
import org.powerTools.engine.RunTime;


public final class SeleniumLibrary extends WebLibrary {
	public SeleniumLibrary (final RunTime runTime) {
		super (runTime);
//		mEvents = new Events (runTime);
		runTime.addSharedObject ("SeleniumLibrary", this);
	}


	// browser related instructions
	public final boolean SetSeleniumServerPortTo_ (final int portNr) {
		return ((SeleniumBrowser) mBrowser).setServerPort (portNr);
	}

	public boolean StartSeleniumServer () {
		try {
			final RemoteControlConfiguration config = new RemoteControlConfiguration();
			config.setPort (4444);
			config.reuseBrowserSessions();
			SeleniumServer mServer = new SeleniumServer (config);
			mServer.start ();
			return true;
		} catch (Exception e) {
			mRunTime.reportError (e.getMessage ());
			return false;
		}
	}

	public final boolean OpenBrowser_At_ (final String typeString, final String url) {
		if (mBrowser != null) {
			mRunTime.reportError ("browser is already open");
			return false;
		} else {
			mBrowser = new SeleniumBrowser (mRunTime);
			return mBrowser.open (getBrowserType (typeString), url);
		}
	}


//	public boolean EnableXpathAdjusting () {
//		mBrowser.setXpathAdjusting(true);
//		return true;
//	}
//
//	public boolean DisableXpathAdjusting () {
//		mBrowser.setXpathAdjusting(false);
//		return true;
//	}

//	public boolean SetXpathAdjusting (final String setting) {
//		if (setting.equals("on")) {
//			return EnableXpathAdjusting ();
//		} else if (setting.equals("off")) {
//			return DisableXpathAdjusting ();
//		} else {
//			return false;
//		}
//	}


//	public boolean ClearEvents () {
//		mBrowser.clearNetworkTraffic ();
//		mRequests = null;
//		return true;
//	}
//
//	public boolean SelectEvents () {
//		mRequests = mBrowser.getNetworkTraffic ();
//		final Iterator<HtmlRequest> iter = mRequests.iterator ();
//		while (iter.hasNext ()) {
//			if (!iter.next ().url.startsWith ("http://stat.detelefoongids.nl")) {
//				iter.remove ();
//			}
//		}
//		return true;
//	}
//
//	public boolean Event_Attribute_Value_ (final String eventName, final String attributeName, final String value) {
//		return mEvents.addAttribute (eventName, attributeName, value);
//	}
//	
//	public boolean CheckForEvent_ (final String eventName) {
//		return mEvents.checkEvent (mRequests, eventName);
//	}
//
//
//	// private members
//	private final Events mEvents;
//
//	private Collection<HtmlRequest> mRequests;
}