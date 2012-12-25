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

import java.util.Collection;
import java.util.regex.Pattern;

import org.powerTools.engine.RunTime;
import org.powerTools.web.WebLibrary.IBrowserType;
import org.powerTools.web.WebLibrary.IItemType;
import org.powerTools.web.WebLibrary.IKeyType;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.SeleniumException;


final class SeleniumBrowser implements IBrowser {
	public SeleniumBrowser (RunTime runTime) {
		mRunTime = runTime;
	}


	@Override
	public boolean setDefaultTimeout (int timeout) {
		if (timeout < 0) {
			mRunTime.reportError ("negative number");
		} else {
			if (timeout > 60) {
				mRunTime.reportError ("long timeout");
			}
			mDefaultTimeout = timeout;
			return true;
		}
		return false;
	}

	public boolean setServerPort (int portNr) {
		mServerPortNr = portNr;
		return true;
	}

	
	@Override
	public int getDefaultTimeoutAsInteger () {
		return mDefaultTimeout;
	}

	@Override
	public String getDefaultTimeoutAsString () {
		return Integer.toString (mDefaultTimeout);
	}

//	public void setXpathAdjusting (boolean setting) {
//		mLeaveXpathIndicesAlone = !setting;
//	}

	@Override
	public boolean open (IBrowserType type, String url) {
//		mIsInternetExplorer = false;

		switch (type) {
		case cFirefox:
			return openBrowser ("*firefox", url);
		case cInternetExplorer:
//			mIsInternetExplorer = true;
			return openBrowser ("*iexplore", url);
		case cChrome:
			return openBrowser ("*googlechrome", url);
		default:
			mRunTime.reportError ("unknown browser code: " + type);
			return false;
		}
	}

	@Override
	public boolean maximize () {
		try {
			if (browserIsOpen()) {
				mSelenium.windowMaximize ();
				return true;
			}
		} catch (SeleniumException se) {
			reportSeleniumException (se);
		}
		return false;
	}

	@Override
	public boolean minimize () {
		mRunTime.reportError ("not implemented");
		return false;
	}

	@Override
	public boolean close () {
		try {
			if (browserIsOpen()) {
				mSelenium.close ();
				mSelenium.stop (); 
//				mSelenium.shutDownSeleniumServer ();
//				if (mServer != null) {
//					try {
//						mServer.stop ();
//						mServer = null;
//					} catch (Exception e) {
//						;
//					}
//				}
				return true;
			}
		} catch (SeleniumException se) {
			reportSeleniumException (se);
		}
		return false;
	}

	@Override
	public boolean openUrl (String url) {
		try {
			if (browserIsOpen()) {
				mSelenium.open (url);
				waitForPageToLoad (getDefaultTimeoutAsString ());
				return true;
			}
		} catch (SeleniumException se) {
			reportSeleniumException (se);
		}
		return false;
	}
	
	@Override
	public String getUrl () {
		return mSelenium.getLocation();
	}
	
	@Override
	public boolean refreshPage () {
		mSelenium.refresh ();
		return true;
	}


	@Override
	public boolean selectFrame (Item item) {
		return false;
	}

	@Override
	public boolean selectFrame (IKeyType keyType, String value) {
		return false;
	}

	@Override
	public boolean selectDefaultFrame () {
		return false;
	}


	@Override
	public boolean itemExists (Item item) {
		try {
			return browserIsOpen ()
				&& mSelenium.isElementPresent (getLocator (item));
		} catch (SeleniumException se) {
			reportSeleniumException (se);
		}
		return false;
	}
	
	@Override
	public boolean itemExists (IKeyType keyType, String value) {
		try {
			return browserIsOpen ()
				&& mSelenium.isElementPresent (getLocator (WebLibrary.IItemType.cButton, keyType, value));
		} catch (SeleniumException se) {
			reportSeleniumException (se);
		}
		return false;
	}

	@Override
	public int countItems (Item item) {
		try {
			if (browserIsOpen ()) {
				mRunTime.reportError ("not implemented");
				return -1;
			}
		} catch (SeleniumException se) {
			reportSeleniumException (se);
		}
		return -1;
	}

	@Override
	public String getPageTitle () {
		try {
			if (browserIsOpen()) {
				return mSelenium.getTitle ();
			}
		} catch (SeleniumException se) {
			reportSeleniumException (se);
		}
		return null;
	}
	
	@Override
	public boolean type (Item item, String text) {
		return type (item.mType, item.mKeyType, item.getValue(), text);
	}

	@Override
	public boolean type (IKeyType keyType, String value, String text) {
		return type (WebLibrary.IItemType.cText, keyType, value, text);
	}

	@Override
	public boolean fireEvent (Item item, String event) {
		try {
			if (browserIsOpen()) {
				mSelenium.fireEvent (getLocator (item), event);
				return true;
			}
		} catch (SeleniumException se) {
			reportSeleniumException (se);
		}
		return false;
	}
	
	@Override
	public boolean click (Item item) {
		switch (item.mType) {
		case cLink :
			if (browserIsOpen()) {
				return clickIfPresent (getLocator (WebLibrary.IItemType.cLink, item.mKeyType, item.getValue ()));
			}
			break;
		case cButton :
		case cCheckbox :
		case cImage :
		case cText :
		case cRadioButton :
			if (browserIsOpen()) {
				return clickIfPresent (getLocator (item.mKeyType, item.getValue ()));
			}
		default :
			mRunTime.reportError ("unsupported item type");
		}
		return false;
	}

	@Override
	public boolean click (IKeyType keyType, String value) {
		try {
			switch (keyType) {
			case cId:
			case cName:
			case cXpath:
			case cValue:
			case cText:
				if (browserIsOpen()) {
					return clickIfPresent (getLocator (keyType, value));
//					mSelenium.click (getLocator (WebLibrary.IItemType.cButtonType, keyType, value));
//					return true;
				}
			default:
				mRunTime.reportError ("unsupported key type");
			}
		} catch (SeleniumException se) {
			reportSeleniumException (se);
		}
		return false;
	}

	@Override
	public boolean clickAndWait (Item item) {
		switch (item.mType) {
		case cButton :
		case cRadioButton :
		case cImage :
			return clickAndWait (item.mKeyType, item.getValue ());
		case cLink :
			return clickLink (item);
		default :
			mRunTime.reportError ("type " + item.mType);
			return false;
		}
	}

	@Override
	public boolean clickAndWait (Item item, int timeout) {
		return false;
	}

	@Override
	public boolean clickAndWait (IKeyType keyType, String value) {
		try {
			switch (keyType) {
			case cId:
			case cName:
			case cXpath:
			case cValue:
				if (browserIsOpen () && clickIfPresent (getLocator (keyType, value))) {
					waitForPageToLoad (getDefaultTimeoutAsString ());
					return true;
				}
				break;
			default:
				mRunTime.reportError ("unsupported key type");
			}
		} catch (SeleniumException se) {
			reportSeleniumException (se);
		}
		return false;
	}

	@Override
	public boolean clickAndWait (IKeyType keyType, String value, int timeout) {
		try {
			switch (keyType) {
			case cId:
			case cName:
			case cXpath:
			case cValue:
				if (browserIsOpen () && clickIfPresent (getLocator (keyType, value))) {
					waitForPageToLoad (Integer.toString (timeout));
					return true;
				}
				break;
			default:
				mRunTime.reportError ("unsupported key type");
			}
		} catch (SeleniumException se) {
			reportSeleniumException (se);
		}
		return false;
	}

	@Override
	public boolean clickLink (Item item) {
		try {
			switch (item.mKeyType) {
			case cId:
			case cText:
			case cXpath:
				if (browserIsOpen () && clickIfPresent (getLocator (item))) {
					waitForPageToLoad (getDefaultTimeoutAsString ());
					return true;
				}
				break;
			default:
				mRunTime.reportError ("unsupported key type");
			}
		} catch (SeleniumException se) {
			reportSeleniumException (se);
		}
		return false;
	}

	@Override
	public boolean clickLink (String text) {
		try {
			if (browserIsOpen ()) {
				//mSelenium.click ("link=" + text);
				mSelenium.click ("xpath=//a[contains(text()," + quoteText (text) + ")]");
				waitForPageToLoad (getDefaultTimeoutAsString ());
				return true;
			}
		} catch (SeleniumException se) {
			reportSeleniumException (se);
		}
		return false;
	}

	@Override
	public boolean clickLink (IKeyType keyType, String value) {
		try {
			if (browserIsOpen()) {
				mSelenium.click (getLocator (WebLibrary.IItemType.cButton, keyType, value));
				waitForPageToLoad (getDefaultTimeoutAsString ());
				return true;
			}
		} catch (SeleniumException se) {
			reportSeleniumException (se);
		}
		return false;
	}

	@Override
	public boolean selectChoice (Item item) {
		try {
			if (!browserIsOpen()) {
				;
			} else if (item.mType == WebLibrary.IItemType.cListboxItem) {
				mSelenium.select (getLocator (item.mParent), getLocator (item));
				return true;
			} else if (item.mType == WebLibrary.IItemType.cCheckbox) {
				mSelenium.check (getLocator (item));
				return true;
			} else {
				mRunTime.reportError ("item is not a listbox item");
			}
		} catch (SeleniumException se) {
			reportSeleniumException (se);
		}
		return false;
	}
	
	@Override
	public boolean selectChoiceByText (Item item, String text) {
		try {
			if (browserIsOpen()) {
				mSelenium.select (getLocator (item), "label=" + text);
				return true;
			}
		} catch (SeleniumException se) {
			reportSeleniumException (se);
		}
		return false;
	}

	@Override
	public boolean selectChoiceByPartialText (Item item, String text) {
		mRunTime.reportError ("not implemented");
		return false;
	}

	@Override
	public boolean mouseOver (Item item) {
		try {
			if (browserIsOpen()) {
				mSelenium.mouseMove (getLocator (item));
				return true;
			}
		} catch (SeleniumException se) {
			reportSeleniumException (se);
		}
		return false;
	}
	
	@Override
	public boolean waitForText (String text) {
		return waitForText (text, mDefaultTimeout);
	}

	@Override
	public boolean waitForText (String text, int timeout) {
		if (browserIsOpen()) {
			for (int nrOfSeconds = 0; nrOfSeconds < timeout; nrOfSeconds++) {
				try {
					if (mSelenium.isTextPresent (text)) {
						return true;
					}
					Thread.sleep (cOneSecondTimeout);
				} catch (InterruptedException ie) {
					;
				} catch (SeleniumException se) {
					reportSeleniumException (se);
				} catch (Exception e) {
					mRunTime.reportError ("exception: " + e.getMessage ());
				}
			}
		}
		return false;
	}

	@Override
	public boolean waitForItem (Item item) {
		return waitForItem (item, mDefaultTimeout);
	}
	
	@Override
	public boolean waitForItem (Item item, int timeout) {
		if (browserIsOpen()) {
			for (int nrOfSeconds = 0; nrOfSeconds < timeout; nrOfSeconds++) {
				try {
					if (mSelenium.isElementPresent (getLocator (item))) {
						return true;
					}
					Thread.sleep (cOneSecondTimeout);
				} catch (InterruptedException ie) {
					;
				} catch (SeleniumException se) {
					reportSeleniumException (se);
				} catch (Exception e) {
					mRunTime.reportError ("exception: " + e.getMessage ());
				}
			}
		}
		return false;
	}

	@Override
	public boolean waitForItemFilled (Item item) {
		return waitForItemFilled (item, mDefaultTimeout);
	}
	
	@Override
	public boolean waitForItemFilled (Item item, int timeout) {
		if (browserIsOpen()) {
			for (int nrOfSeconds = 0; nrOfSeconds < timeout; ++nrOfSeconds) {
				try {
					// TODO remove isElementPresent?
					final String locator = getLocator (item);
					if (mSelenium.isElementPresent (locator) && !mSelenium.getText (locator).isEmpty ()) {
						return true;
					}
					Thread.sleep (cOneSecondTimeout);
				} catch (InterruptedException ie) {
					;
				} catch (SeleniumException se) {
					reportSeleniumException (se);
				} catch (Exception e) {
					mRunTime.reportError ("exception: " + e.getMessage ());
				}
			}
		}
		return false;
	}

	@Override
	public boolean waitForItemVisible (Item item) {
		mRunTime.reportError ("not implemented");
		return false;
	}

	@Override
	public boolean waitForItemVisible (Item item, int timeout) {
		mRunTime.reportError ("not implemented");
		return false;
	}
	
	@Override
	public boolean checkForText (String text) {
		try {
			if (browserIsOpen()) {
				return mSelenium.isTextPresent (text);
			}
		} catch (SeleniumException se) {
			reportSeleniumException (se);
		} catch (Exception e) {
			mRunTime.reportError ("exception: " + e.getMessage ());
		}
		return false;
	}

	@Override
	public String getItemText (Item item) {
		try {
			if (browserIsOpen()) {
				if (isAttribute (item.mKeyType, item.getValue ())) {
					return mSelenium.getAttribute (getLocator (item));
				} else {
					return mSelenium.getText (getLocator (item));
				}
			}
		} catch (SeleniumException se) {
			reportSeleniumException (se);
		}
		return null;
	}

	@Override
	public String getItemText (IKeyType keyType, String value) {
		try {
			if (browserIsOpen()) {
				final String locator = getLocator (WebLibrary.IItemType.cText, keyType, value);
				if (isAttribute (keyType, value)) {
					return mSelenium.getAttribute (locator);
				} else {
					return mSelenium.getText (locator);
				}
			}
		} catch (SeleniumException se) {
			reportSeleniumException (se);
		}
		return null;
	}

	@Override
	public Collection<HtmlRequest> getNetworkTraffic () {
		return null;
//		final Type collectionOfHTMLRequestsType = new TypeToken<Collection<HtmlRequest>> () {}.getType ();
//		return new Gson ().fromJson (mSelenium.captureNetworkTraffic ("json"), collectionOfHTMLRequestsType);
	}

	@Override
	public void clearNetworkTraffic () {
		// TODO: reset without getting all that traffic?
		getNetworkTraffic ();
	}

	
	// private members
	private final static int cDefaultPortNr			= 4444;
	private final static int cOneSecondTimeout		= 1000;
	private final static Pattern mAttributePattern	= Pattern.compile (".*/@\\w+");
	
	private final RunTime mRunTime;

	private int mDefaultTimeout = 30000;
	private int mServerPortNr	= cDefaultPortNr;
//	private SeleniumServer mServer;
	private Selenium mSelenium;
//	private boolean mLeaveXpathIndicesAlone = false;
//	private boolean mIsInternetExplorer = false;


	private boolean openBrowser (String browserCode, String url) {
		try {
//			final RemoteControlConfiguration config = new RemoteControlConfiguration();
//			config.setPort (portNr);
//			config.reuseBrowserSessions();
//			final SeleniumServer server = new SeleniumServer (config);
//			server.start ();

			//mSelenium = new DefaultSelenium ("localhost", portNr, browserCode, url);
			mSelenium = new DefaultSelenium ("localhost", mServerPortNr, browserCode, url) {
				public void open (String url) {
					commandProcessor.doCommand ("open", new String[] { url, "true" } );
				}
			};
			//mSelenium.start("--disable-web-security");
			//mSelenium.start ("commandLineFlags=--disable-web-security");
			mSelenium.start ("captureNetworkTraffic=true");
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private boolean browserIsOpen () {
		final boolean isOpen = (mSelenium != null);
		if (!isOpen) {
			mRunTime.reportError ("browser not opened");
		}
		return isOpen;
	}
	
	private void reportSeleniumException (SeleniumException se) {
		mRunTime.reportError (se.getMessage ());
	}
	
	
	private String getLocator (Item item) {
		return getLocator (item.mType, item.mKeyType, item.getValue ());
	}

	private String getLocator (IItemType type, IKeyType keyType, String value) {
		if (value == null) {
			return null;
		}
		
		switch (keyType) {
		case cDom:
			return "dom=" + value;
		case cId:
			return value;
		case cIndex:
			return "index=" + value;
		case cName:
			return "name=" + value;
		case cValue:
			return "value=" + value;
		case cText:
			if (type == WebLibrary.IItemType.cLink) {
				return "xpath=//a[contains(text()," + quoteText (value) + ")]";
//			} else if (item.mType == WebLibrary.cButtonType) {
//				return "value=" + item.mValue;
			} else if (type == WebLibrary.IItemType.cListboxItem) {
				return "label=" + value;
			} else {
				// TODO are quotes needed here?
				return "text=" + quoteText (value) + "";
			}
		case cXpath:
			return "xpath=" + value;
			//return "xpath=" + getXpath (value);
		default:
			return "unsupported key";
		}
	}

	private String getLocator (IKeyType keyType, String value) {
		if (value == null) {
			return null;
		}
		
		switch (keyType) {
		case cDom:
			return "dom=" + value;
		case cId:
			return value;
		case cIndex:
			return "index=" + value;
		case cName:
			return "name=" + value;
		case cValue:
			return "value=" + value;
		case cText:
			return "text=" + quoteText (value) + "";
		case cXpath:
			return "xpath=" + value;
			//return "xpath=" + getXpath (value);
		default:
			return "unsupported key";
		}
	}

	private String quoteText (String text) {
		return text.contains ("'") ? "\"" + text + "\"" : "'" + text + "'" ;
	}


	private boolean clickIfPresent (String locator) {
//		mRunTime.reportInfo("locator: " + locator);
//		mRunTime.reportInfo("present: " + mSelenium.isElementPresent (locator));

		if (locator == null) {
			;
		} else if (mSelenium.isElementPresent (locator)) {
			mSelenium.click (locator);
			return true;
		} else {
			mRunTime.reportError ("no element found for locator " + locator);
		}
		return false;
	}

	private boolean type (IItemType type, IKeyType keyType, String value, String text) {
		if (value == null) {
			return false;
		}
		
		switch (type) {
		case cCombobox :
		case cListbox :
		case cText :
			try {
				mSelenium.type (getLocator (type, keyType, value), text);
				return true;
			} catch (SeleniumException se) {
				mRunTime.reportError (se.getMessage ());
			} catch (NullPointerException npe) {
				mRunTime.reportError ("browser not opened");
			}
			break;
		default :
			mRunTime.reportError ("typing not supported in this kind of item");
		}
		return false;
	}

	private boolean isAttribute (IKeyType keyType, String value) {
		return keyType == WebLibrary.IKeyType.cXpath && mAttributePattern.matcher (value).matches();
	}
	
//	private String getXpath (String normalExpression) {
//		if (!mIsInternetExplorer || mLeaveXpathIndicesAlone) {
//			// all browsers except IE follow the standard and start indices at one
//			mRunTime.reportInfo ("getXpath ok");
//			return normalExpression;
//		} else {
//			mRunTime.reportInfo ("getXpath: " + normalExpression);
//
//			// for IE, indices start at zero and must be decreased by one
//			final StringBuffer sb = new StringBuffer();
//			final CharacterIterator iter = new StringCharacterIterator (normalExpression);
//			int currentChar = iter.first();
//			do {
//				if (currentChar != '[') {
//					sb.append(currentChar);
//					currentChar = iter.next();
//				} else {
//					currentChar = iter.next();
//					if (!Character.isDigit(currentChar)) {
//						sb.append(currentChar);
//						currentChar = iter.next();
//					} else {
//						int number = currentChar - '0';
//						currentChar = iter.next();
//						while (Character.isDigit(currentChar)) {
//							number *= 10;
//							number += currentChar - '0';
//							currentChar = iter.next();
//						}
//						sb.append (number - 1);
//					}
//				}
//			} while (currentChar != CharacterIterator.DONE);
//
//			mRunTime.reportInfo (sb.toString ());
//
//			return sb.toString ();
//		}
//	}
	
	private void waitForPageToLoad (String timeout) {
		try {
			mSelenium.waitForPageToLoad (timeout);
		} catch (Exception e) {
			mRunTime.reportWarning ("waitForPageToLoad exception: " + e.getMessage ());
			try {
				Thread.sleep (2000);
			} catch (InterruptedException ie) {
				;
			}
		}
	}
	//Added by Poonam
	public int getCount(Item item) {
		/*if (browserIsOpen()) {
			return mSelenium.getXpathCount(item).intValue();
		} else {
			return 0;
		}*/
		return 0;
	}

	
	@Override
	public Object getTestTool () {
		return mSelenium;
	}
}