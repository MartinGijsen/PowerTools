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

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.powerTools.engine.ExecutionException;
import org.powerTools.engine.RunTime;
import org.powerTools.web.WebLibrary.IBrowserType;
import org.powerTools.web.WebLibrary.IItemType;
import org.powerTools.web.WebLibrary.IKeyType;


final class WebDriverBrowser implements IBrowser {
	public WebDriverBrowser (RunTime runTime) {
		mRunTime = runTime;
	}


	@Override
	public boolean setDefaultTimeout (int timeout) {
		return false;
	}
	
	@Override
	public int getDefaultTimeoutAsInteger () {
		return mDefaultTimeout;
	}
	
	@Override
	public String getDefaultTimeoutAsString () {
		return Integer.toString (mDefaultTimeout);
	}
	
//	@Override
//	public void setXpathAdjusting(boolean setting) {
//
//	}

	@Override
	public boolean open (IBrowserType type, String url) {
		switch (type) {
		case cInternetExplorer:
			System.setProperty ("webdriver.ie.driver", "IEDriverServer.exe"); 
			mDriver = new InternetExplorerDriver (); 
			mDriver.get (url);
			return true;
		case cChrome:
			try {
				System.setProperty ("webdriver.chrome.driver", "chromedriver.exe"); 
				final ChromeDriverService chromeDriverService = ChromeDriverService.createDefaultService (); 
				chromeDriverService.start ();
				mDriver = new ChromeDriver (chromeDriverService); 
				mDriver.get (url);
				return true;
			} catch (IOException ioe) {
				return false;
			}
		case cFirefox:
			mDriver = new FirefoxDriver();
			mDriver.get (url);
			return true;
		default:
			mRunTime.reportError ("unknown browser code: " + type);
			return false;
		}
	}

	@Override
	public boolean maximize () {
		mDriver.manage ().window ().maximize ();
		return true;
	}

	@Override
	public boolean minimize () {
		mRunTime.reportError ("not implemented");
		return false;
	}

	@Override
	public boolean openUrl (String url) {
		mDriver.get (url);
		return true;
	}

	@Override
	public String getUrl () {
		return mDriver.getCurrentUrl ();
	}

	@Override
	public boolean refreshPage () {
		mDriver.navigate ().refresh ();
		return true;
	}

	@Override
	public String getPageTitle () {
		return mDriver.getTitle ();
	}

	@Override
	public boolean selectFrame (String name) {
		mDriver.switchTo ().frame (mDriver.findElement (By.id (name)));
		return true;
	}

	@Override
	public boolean selectFrame (IKeyType keyType, String value) {
		mDriver.switchTo ().frame (mDriver.findElement (getLocator (keyType, value)));
		return true;
	}

	@Override
	public boolean selectDefaultFrame () {
		mDriver.switchTo ().defaultContent ();
		return true;
	}

	@Override
	public boolean type (Item item, String text) {
		final WebElement element = findOneElement (item);
		return element == null ? false : type (element, text);
	}

	@Override
	public boolean type (IKeyType keyType, String value, String text) {
		final WebElement element = findOneElement (keyType, value);
		return element == null ? false : type (element, text);
	}

	@Override
	public boolean fireEvent (Item item, String event) {
		mRunTime.reportError ("not implemented");
		return false;
	}

	@Override
	public boolean itemExists (Item item) {
		return hasOneElement (getLocator (item));
	}

	@Override
	public boolean itemExists (IKeyType keyType, String value) {
		return hasOneElement (getLocator (keyType, value));
	}

	@Override
	public int countItems (Item item) {
		return findElements (getLocator (item)).size ();
	}
	
	@Override
	public boolean click (Item item) {
		final WebElement element = findOneElement (item);
		return element == null ? false : click (element);
	}

	@Override
	public boolean click (IKeyType keyType, String value) {
		final WebElement element = findOneElement (keyType, value);
		return element == null ? false : click (element);
	}

	@Override
	public boolean clickAndWait (Item item) {
		return click (item);
	}

	@Override
	public boolean clickAndWait (Item item, int timeout) {
		// TODO: handle timeout?
		return click (item);
	}

	@Override
	public boolean clickAndWait (IKeyType keyType, String value) {
		return click (keyType, value);
	}

	@Override
	public boolean clickAndWait (IKeyType keyType, String value, int timout) {
		// TODO: handle timeout?
		return click (keyType, value);
	}

	@Override
	public boolean clickLink (Item item) {
		return click (item);
	}

	@Override
	public boolean clickLink (String text) {
		return click (By.partialLinkText (text));
	}

	@Override
	public boolean clickLink (IKeyType keyType, String value) {
		return click (getLocator (keyType, value));
	}

	@Override
	public boolean selectChoice (Item item) {
		if (item.mType != WebLibrary.IItemType.cListboxItem) {
			mRunTime.reportError ("item is not a listbox item");
			return false;
		} else {
			Select listbox = new Select (mDriver.findElement (getLocator (item.mParent)));
			switch (item.mKeyType) {
			case cIndex:
				listbox.selectByIndex (Integer.parseInt (item.mValue));
				return true;
			case cText:
				listbox.selectByVisibleText (item.mValue);
				return true;
			case cValue:
				listbox.selectByValue (item.mValue);
				return true;
			default:
				return false;
			}
		}
	}

	@Override
	public boolean selectChoice (Item selectItem, String optionText) {
		if (selectItem.mType != WebLibrary.IItemType.cListboxItem) {
			mRunTime.reportError ("item is not a listbox item");
			return false;
		} else {
			Select listbox = new Select (mDriver.findElement (getLocator (selectItem)));
			listbox.selectByVisibleText (optionText);
			return true;
		}
	}

	@Override
	public boolean mouseOver (Item item) {
		final WebElement element = findOneElement (item);
		if (element == null) {
			return false;
		} else {
			new Actions (mDriver).moveToElement (element).build ().perform ();
			return true;
		}
	}

	@Override
	public boolean waitForText (String text) {
		return waitForText (text, mDefaultTimeout);
	}

	@Override
	public boolean waitForText (String text, int timeout) {
		return waitForCondition (new TextPresentCondition (text), timeout);
	}

	@Override
	public boolean waitForItem (Item item) {
		return waitForItem (item, mDefaultTimeout);
	}

	@Override
	public boolean waitForItem (Item item, int timeout) {
		return waitForCondition (new ItemPresentCondition (item), timeout);
	}

	@Override
	public boolean waitForItemFilled (Item item) {
		return waitForItemFilled (item, mDefaultTimeout);
	}

	@Override
	public boolean waitForItemFilled (Item item, int timeout) {
		return waitForCondition (new ItemFilledCondition (item), timeout);
	}

	@Override
	public boolean checkForText (String text) {
		final WebElement element = findOneElement (WebLibrary.IKeyType.cTag, "body");
		return element == null ? false : element.getText ().contains (text);
	}

	@Override
	public String getItemText (Item item) {
		final WebElement element = findOneElement (item);
		return element == null ? null : element.getText ();
	}

	@Override
	public String getItemText (IKeyType keyType, String value) {
		final WebElement element = findOneElement (keyType, value);
		return element == null ? null : element.getText ();
	}

	@Override
	public boolean close () {
		mDriver.close ();
		mDriver.quit ();
		mDriver = null;
		return true;
	}

	public Collection<HtmlRequest> getNetworkTraffic () {
		mRunTime.reportError ("not implemented");
		return new LinkedList<HtmlRequest> ();
//		final Type collectionOfHTMLRequestsType = new TypeToken<Collection<HtmlRequest>> () {}.getType ();
//		return new Gson ().fromJson (mSelenium.captureNetworkTraffic ("json"), collectionOfHTMLRequestsType);
	}

	public void clearNetworkTraffic () {
		// TODO: reset without getting all that traffic?
		getNetworkTraffic ();
	}

	
	// private members
	private final static int cOneSecondTimeout	= 1000;

	private final RunTime mRunTime;

	private int mDefaultTimeout = 30000;
	private WebDriver mDriver;


	private WebElement findOneElement (Item item) {
		final By locator = getLocator (item);
		return locator == null ? null : findOneElement (locator);
	}

	private WebElement findOneElement (IKeyType keyType, String value) {
		final By locator = getLocator (keyType, value);
		return locator == null ? null : findOneElement (locator);
	}

	private WebElement findOneElement (By locator) {
		final List<WebElement> list = mDriver.findElements (locator);
		switch (list.size ()) {
		case 0:
			mRunTime.reportError ("no matching item found");
			return null;
		case 1:
			return list.get (0);
		default:
			mRunTime.reportError ("multiple matching items found");
			return null;	
		}
	}

	private boolean hasOneElement (By locator) {
		//mRunTime.reportInfo ("the size of the locator is  " + mDriver.findElements (locator).size ());
		return mDriver.findElements (locator).size () == 1;
	}

	private List<WebElement> findElements (By locator) {
		return mDriver.findElements (locator);
	}

	private By getLocator (Item item) {
		if (item.mType == IItemType.cLink && item.mKeyType == IKeyType.cText) {
			final By locator = By.linkText (item.mValue);
			mRunTime.reportInfo ("locator: " + locator.toString ());
			return locator;
		} else {
			return getLocator (item.mKeyType, item.getValue ());
		}
	}

	private By getLocator (IKeyType keyType, String value) {
		By locator;
		switch (keyType) {
		case cId :
			locator = By.id (value);
			break;
		case cName :
			locator = By.name (value);
			break;
		case cXpath :
			locator = By.xpath (value);
			break;
		case cCss :
			locator = By.cssSelector (value);
			break;
		case cTag :
			locator = By.tagName (value);
			break;
		default:
			throw new ExecutionException ("invalid key");
		}
		mRunTime.reportInfo ("locator: " + locator.toString ());
		return locator;
	}

	private boolean type (WebElement element, String text) {
		element.clear ();
		element.sendKeys (text);
		return true;
	}
	
	private boolean click (WebElement element) {
		element.click ();
		return true;
	}

	private boolean click (By locator) {
		final WebElement element = findOneElement (locator);
		if (element != null) {
			element.click ();
			return true;
		} else {
			return false;
		}
	}
	
	private boolean waitForCondition (ICondition condition, int timeout) {
		for (int nrOfSeconds = 0; nrOfSeconds < timeout; ++nrOfSeconds) {
			try {
				if (condition.isSatisfied ()) {
					return true;
				}
				Thread.sleep (cOneSecondTimeout);
			} catch (InterruptedException ie) {
				;
			}
		}
		return false;
	}
	
	
	private interface ICondition {
		boolean isSatisfied ();
	}
	
	private final class ItemFilledCondition implements ICondition {
		ItemFilledCondition (Item item) {
			mItem = item;
		}
		
		public boolean isSatisfied () {
			final WebElement element = findOneElement (mItem);
			return element != null && !element.getText ().isEmpty ();
		}
		
		private Item mItem;
	}

	private final class ItemPresentCondition implements ICondition {
		ItemPresentCondition (Item item) {
			mItem = item;
		}
		
		public boolean isSatisfied () {
			return itemExists (mItem);
		}
		
		private Item mItem;
	}

	private final class TextPresentCondition implements ICondition {
		TextPresentCondition (String text) {
			mText = text;
		}
		
		public boolean isSatisfied () {
			return checkForText (mText);
		}
		
		private String mText;
	}

	@Override
	public int getCount (Item item) {
		final By locator = getLocator (item);
		final List<WebElement> list = mDriver.findElements(locator);
		mRunTime.reportInfo ("the count is : " + list.size());
		return list.size();
	}
}