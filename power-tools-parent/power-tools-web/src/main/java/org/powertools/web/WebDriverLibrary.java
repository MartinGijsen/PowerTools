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

package org.powertools.web;

import org.powertools.engine.RunTime;
import org.powertools.engine.reports.TestResultSubscriber;
import org.powertools.engine.reports.TestRunResultPublisher;


public final class WebDriverLibrary extends WebLibrary {
	public WebDriverLibrary (RunTime runTime) {
		super (runTime);
		
		TestRunResultPublisher mPublisher = TestRunResultPublisher.getInstance ();
        TestResultSubscriber subscriber = new TestResultSubscriberMakeScreenshotByError(this);
		mPublisher.subscribeToTestResults(subscriber);		
		
		runTime.addSharedObject ("WebDriverLibrary", this);
	}


	public boolean OpenBrowser_At_ (String typeString, String url) {
		return OpenBrowser_Version_At_OnGrid_(typeString, null, url, null);
	}
	
	public boolean OpenBrowser_Version_At_OnGrid_ (String typeString, String browserVersion, String url, String hubUrl) {
		if (mBrowser != null) {
			mRunTime.reportError ("browser is already open");
			return false;
		} else {
			IBrowserType browserType	= getBrowserType (typeString);
			String urlToOpen			= url.isEmpty () ? "about:blank" : completeUrl (url);			
			WebDriverBrowser myBrowser	= new WebDriverBrowser (mRunTime);
			mBrowser					= myBrowser;
			return myBrowser.open (browserType, browserVersion, urlToOpen, mRunTime.getContext ().getResultsDirectory (), hubUrl);
		}
	}
}