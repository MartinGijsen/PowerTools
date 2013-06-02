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

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;
import org.powerTools.engine.ExecutionException;
import org.powerTools.engine.RunTime;
import org.powerTools.web.WebLibrary.IBrowserType;
import org.powerTools.web.WebLibrary.IItemType;
import org.powerTools.web.WebLibrary.IKeyType;


class WebDriverBrowser implements IBrowser {
	private final String CHROMEDRIVER_LOG_FILENAME = "chromedriver.log";
	
	
	public WebDriverBrowser (RunTime runTime) {
		mRunTime = runTime;
	}


	@Override
	public boolean setShortDefaultTimeout (int timeout) {
		mShortDefaultTimeoutInSeconds = timeout;
		return true;
	}
	
	@Override
	public boolean setLongDefaultTimeout (int timeout) {
		mLongDefaultTimeoutInSeconds = timeout;
		return true;
	}
	
	@Override
	public int getShortDefaultTimeoutAsInteger () {
		return mShortDefaultTimeoutInSeconds;
	}
	
	@Override
	public int getLongDefaultTimeoutAsInteger () {
		return mLongDefaultTimeoutInSeconds;
	}
	
	@Override
	public String getShortDefaultTimeoutAsString () {
		return Integer.toString (mShortDefaultTimeoutInSeconds);
	}
	
	@Override
	public String getLongDefaultTimeoutAsString () {
		return Integer.toString (mLongDefaultTimeoutInSeconds);
	}
	
//	@Override
//	public void setXpathAdjusting(boolean setting) {
//
//	}

	
	public boolean open (IBrowserType type, String browserVersion, String url, String logDirectory, String hubUrl) {
		if (hubUrl == null || hubUrl.isEmpty()) {
			return open(type, url, logDirectory);
		} else {
			return openOnGrid(type, browserVersion, url, hubUrl);
		}
	}


	private boolean openOnGrid (IBrowserType type, String browserVersion, String url, String hubUrl) {
		DesiredCapabilities capability = getBrowserCapabilities (type);

		if (browserVersion != null && !browserVersion.isEmpty ()) {
			capability.setCapability(CapabilityType.VERSION, browserVersion);
		}
		
		try {
			mDriver = new RemoteWebDriver (new URL (hubUrl), capability);
			mDriver.get (url);
			mRunTime.addSharedObject ("WebDriver", mDriver);
			return true;
		} catch (MalformedURLException e) {
			mRunTime.reportError("'GridUrl' defined Url invalid: " + url + ", exception=" + e);
			return false;
		}
	}

	private DesiredCapabilities getBrowserCapabilities (IBrowserType type) {
		DesiredCapabilities capabilities;
		switch (type) {
		case cInternetExplorer:
			capabilities = DesiredCapabilities.internetExplorer ();
			break;
		case cChrome:
			capabilities = DesiredCapabilities.chrome ();
			break;
		case cFirefox:
			capabilities = DesiredCapabilities.firefox ();
			break;
		default:
			throw new ExecutionException ("unknown browser code: " + type);
		}
		return capabilities;
	}
	
	@Override
	public boolean open (IBrowserType type, String url, String logDirectory) {
		switch (type) {
		case cInternetExplorer:
			System.setProperty ("webdriver.ie.driver", "IEDriverServer.exe"); 
			mDriver = new InternetExplorerDriver (); 
			break;
		case cChrome:
			if (!startChrome (logDirectory)) {
				return false;
			}
			break;
		case cFirefox:
			mDriver = new FirefoxDriver();
			break;
		default:
			mRunTime.reportError ("unknown browser code: " + type);
			return false;
		}

		mDriver.get (url);
		mRunTime.addSharedObject ("WebDriver", mDriver);
		return true;
	}

	private boolean startChrome (String logDirectory) {
		try {
			System.setProperty ("webdriver.chrome.driver", "chromedriver.exe"); 
			ChromeDriverService service = new ChromeDriverService.Builder ()
											.usingDriverExecutable (new File ("chromedriver.exe"))
											.usingAnyFreePort ()
											.withLogFile (new File (logDirectory + CHROMEDRIVER_LOG_FILENAME))
											.build ();
			service.start ();
			mDriver = new ChromeDriver (service); 
			mRunTime.reportLink (CHROMEDRIVER_LOG_FILENAME);
			return true;
		} catch (IOException ioe) {
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
	public boolean selectFrame (Item item) {
		mDriver.switchTo ().frame (mDriver.findElement (getLocator (item)));
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
		return type (waitForUniqueElement (getLocator (item)), text);
	}

	@Override
	public boolean type (IKeyType keyType, String value, String text) {
		return type (waitForUniqueElement (getLocator (keyType, value)), text);
	}

	@Override
	public boolean fireEvent (Item item, String event) {
		mRunTime.reportError ("not implemented");
		return false;
	}

	@Override
	public boolean setCheckboxValue(Item item, boolean value) {
		return setCheckboxValue (waitForUniqueElement (getLocator (item)), value);
	}
	
	@Override
	public boolean setCheckboxValue(IKeyType keyType, String value, boolean checkValue) {
		return setCheckboxValue (waitForUniqueElement (getLocator (keyType, value)), checkValue);
	}	
	
	@Override
	public boolean itemExists (Item item) {
		return getUniqueElement (getLocator (item)) != null;
	}

	@Override
	public boolean itemExists (IKeyType keyType, String value) {
		return getUniqueElement (getLocator (keyType, value)) != null;
	}

	public boolean itemVisible (Item item) {
		return getUniqueElement (getLocator (item)).isDisplayed ();
	}

	public boolean itemEnabled (Item item) {
		return getUniqueElement (getLocator (item)).isEnabled ();
	}

	@Override
	public int countItems (Item item) {
		return mDriver.findElements (getLocator (item)).size ();
	}
	
	@Override
	public boolean click (Item item) {
		return click (waitForUniqueElement (getLocator (item)));
	}

	@Override
	public boolean click (IKeyType keyType, String value) {
		return click (waitForUniqueElement (getLocator (keyType, value)));
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
	public boolean clickAcceptInAlert() {
		mDriver.switchTo().alert().accept();
		return true;	
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
	public boolean selectChoiceByText (Item selectItem, String optionText) {
		if (selectItem.mType != WebLibrary.IItemType.cListbox) {
			mRunTime.reportError ("item is not a listbox");
			return false;
		} else {
			Select listbox = new Select (mDriver.findElement (getLocator (selectItem)));
			listbox.selectByVisibleText (optionText);
			return true;
		}
	}

	@Override
	public boolean selectChoiceByPartialText (Item selectItem, String optionText) {
		if (selectItem.mType != WebLibrary.IItemType.cListbox) {
			mRunTime.reportError ("item is not a listbox");
			return false;
		} else {
			Select listbox				= new Select (mDriver.findElement (getLocator (selectItem)));
			List<WebElement> choices	= listbox.getOptions ();
			WebElement theOne			= null;
			for (WebElement option : choices) {
				if (!option.getText ().contains (optionText)) {
					;
				} else if (theOne == null) {
					theOne = option;
				} else {
					mRunTime.reportError (String.format ("partial text '%s' is not unique (part of '%s' and '%s')", optionText, theOne.getText (), option.getText ()));
					return false;
				}
			}
			theOne.click ();
			return true;
		}
	}
	
	@Override
	public boolean mouseOver (Item item) {
		final WebElement element = waitForUniqueElement (getLocator (item));
		new Actions (mDriver).moveToElement (element).build ().perform ();
		return true;
	}

	@Override
	public boolean waitUntilTextIsPresent (String text) {
		return waitUntilTextIsPresent (text, mShortDefaultTimeoutInSeconds);
	}

	@Override
	public boolean waitUntilTextIsPresent (String text, int timeout) {
		return waitForCondition (new TextPresentCondition (text), timeout);
	}

	@Override
	public boolean waitUntilTextIsNotPresent (String text) {
		return waitUntilTextIsNotPresent (text, mShortDefaultTimeoutInSeconds);
	}

	@Override
	public boolean waitUntilTextIsNotPresent (String text, int timeout) {
		return waitForCondition (new TextNotPresentCondition (text), timeout);
	}

	@Override
	public boolean waitUntilItemIsPresent (Item item) {
		return waitUntilItemIsPresent (item, mShortDefaultTimeoutInSeconds);
	}

	@Override
	public boolean waitUntilItemIsPresent (Item item, int timeout) {
		return waitForCondition (new ItemPresentCondition (getLocator (item)), timeout);
	}

	@Override
	public boolean waitUntilItemIsNotPresent (Item item) {
		return waitUntilItemIsNotPresent (item, mShortDefaultTimeoutInSeconds);
	}

	@Override
	public boolean waitUntilItemIsNotPresent (Item item, int timeout) {
		return waitForCondition (new ItemNotPresentCondition (getLocator (item)), timeout);
	}

	@Override
	public boolean waitUntilItemIsFilled (Item item) {
		return waitUntilItemIsFilled (item, mShortDefaultTimeoutInSeconds);
	}

	@Override
	public boolean waitUntilItemIsFilled (Item item, int timeout) {
		return waitForCondition (new ItemNotEmptyCondition (getLocator (item)), timeout);
	}

	@Override
	public boolean waitUntilItemIsEmpty (Item item) {
		return waitUntilItemIsEmpty (item, mShortDefaultTimeoutInSeconds);
	}

	@Override
	public boolean waitUntilItemIsEmpty (Item item, int timeout) {
		return waitForCondition (new ItemEmptyCondition (getLocator (item)), timeout);
	}

	@Override
	public boolean waitUntilItemIsVisible (Item item) {
		return waitUntilItemIsVisible (item, mShortDefaultTimeoutInSeconds);
	}

	@Override
	public boolean waitUntilItemIsVisible (Item item, int timeout) {
		return waitForCondition (new ItemVisibleCondition (getLocator (item)), timeout);
	}
	
	@Override
	public boolean waitUntilItemIsNotVisible (Item item) {
		return waitUntilItemIsNotVisible (item, mShortDefaultTimeoutInSeconds);
	}

	@Override
	public boolean waitUntilItemIsNotVisible (Item item, int timeout) {
		return waitForCondition (new ItemNotVisibleCondition (getLocator (item)), timeout);
	}
	
	@Override
	public boolean waitUntilItemIsEnabled (Item item) {
		return waitUntilItemIsEnabled (item, mShortDefaultTimeoutInSeconds);
	}

	@Override
	public boolean waitUntilItemIsEnabled (Item item, int timeout) {
		return waitForCondition (new ItemEnabledCondition (getLocator (item)), timeout);
	}

	@Override
	public boolean waitUntilItemIsDisabled (Item item) {
		return waitUntilItemIsDisabled (item, mShortDefaultTimeoutInSeconds);
	}

	@Override
	public boolean waitUntilItemIsDisabled (Item item, int timeout) {
		return waitForCondition (new ItemDisabledCondition (getLocator (item)), timeout);
	}

	@Override
	public boolean checkForText (String text) {
		final WebElement element = getUniqueElement (getLocator (WebLibrary.IKeyType.cTag, "body"));
		return element == null ? false : element.getText ().contains (text);
	}

	@Override
	public String getItemText (Item item) {
		return waitForUniqueElement (getLocator (item)).getText ();
	}

	@Override
	public String getItemText (IKeyType keyType, String value) {
		return waitForUniqueElement (getLocator (keyType, value)).getText ();
	}

	@Override
	public boolean close () {
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


	@Override
	public void cleanup () {
		if (mDriver != null) {
			close ();
		}
	}

	
	@Override
	public boolean makeScreenshot (String path) {
		try {
			TakesScreenshot augmentedDriver	= (TakesScreenshot) new Augmenter ().augment (mDriver);
			File screenshot					= augmentedDriver.getScreenshotAs (OutputType.FILE);
			Files.copy (Paths.get (screenshot.getPath ()), Paths.get (path));
			return true;
		} catch (IOException ioe) {
			throw new ExecutionException ("could not copy screenshot file");
		}
	}


	// protected members
	protected final static int cOneSecondTimeout = 1000;

	protected final RunTime mRunTime;

	protected int mShortDefaultTimeoutInSeconds	= 10;
	protected int mLongDefaultTimeoutInSeconds	= 30;
	protected WebDriver mDriver;


	// private members
//	private WebElement findOneElement (Item item) {
//		return findOneElement (getLocator (item));
//	}
//
//	private WebElement findOneElement (IKeyType keyType, String value) {
//		return findOneElement (getLocator (keyType, value));
//	}
//
//	private WebElement findOneElement (By locator) {
//		final List<WebElement> list = mDriver.findElements (locator);
//		switch (list.size ()) {
//		case 0:
//			mRunTime.reportError ("no matching item found");
//			return null;
//		case 1:
//			return list.get (0);
//		default:
//			mRunTime.reportError ("multiple matching items found");
//			return null;	
//		}
//	}

	private WebElement getUniqueElement (By locator) {
		final List<WebElement> list = mDriver.findElements (locator);
		switch (list.size ()) {
		case 0:
			throw new ExecutionException ("no matching item found");
		case 1:
			return list.get (0);
		default:
			throw new ExecutionException ("multiple matching items found");
		}
	}

	private WebElement waitForUniqueElement (By locator) {
		return waitForUniqueElement (locator, mShortDefaultTimeoutInSeconds);
	}
	
	private WebElement waitForUniqueElement (By locator, int timeout) {
		ItemPresentCondition condition = new ItemPresentCondition (locator);
		if (waitForCondition (condition, timeout)) {
			return condition.mElement;
		} else {
			throw new ExecutionException ("timeout expired");
		}
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
	
	private boolean setCheckboxValue(WebElement element, boolean value) {
		boolean current = element.isSelected();
		if (current != value) {
			element.click();
		}
		return true;
	}
	
	
	private boolean click (WebElement element) {
		element.click ();
		return true;
	}

	private boolean click (By locator) {
		getUniqueElement (locator).click ();
		return true;
	}

	
	private boolean waitForCondition (Condition condition, int timeout) {
		for (int nrOfSeconds = 0; nrOfSeconds < timeout; ++nrOfSeconds) {
			// TODO find better place for handling the exceptions
			try {
				if (condition.isSatisfied ()) {
					return true;
				}
				Thread.sleep (cOneSecondTimeout);
			} catch (InterruptedException ie) {
				;
			} catch (StaleElementReferenceException e) {
				// This error occurs occasionally in IE when using SeleniumGrid
			} catch (NoSuchElementException e) {
				// This error occurs occasionally in IE when using SeleniumGrid
			} catch (ExecutionException e) {
				// This error occurs occasionally in Firefox. Sometimes the body tag cannot be found, due of Animation by Javascript 
			}
		}
		return false;
	}	
	
	
	private interface Condition {
		boolean isSatisfied ();
	}
	
	private final class TextPresentCondition implements Condition {
		TextPresentCondition (String text) {
			mText = text;
		}
		
		@Override
		public boolean isSatisfied () {
			return checkForText (mText);
		}
		
		private String mText;
	}

	private final class TextNotPresentCondition implements Condition {
		TextNotPresentCondition (String text) {
			mText = text;
		}
		
		@Override
		public boolean isSatisfied () {
			return !checkForText (mText);
		}
		
		private String mText;
	}

	private abstract class ItemCondition implements Condition {
		protected ItemCondition (By locator) {
			mLocator = locator;
		}
		
		protected By mLocator;
	}
	
	private final class ItemNotEmptyCondition extends ItemCondition {
		ItemNotEmptyCondition (By locator) {
			super (locator);
		}
		
		@Override
		public boolean isSatisfied () {
			return !getUniqueElement (mLocator).getText ().isEmpty ();
		}
	}

	private final class ItemEmptyCondition extends ItemCondition {
		ItemEmptyCondition (By locator) {
			super (locator);
		}
		
		@Override
		public boolean isSatisfied () {
			return getUniqueElement (mLocator).getText ().isEmpty ();
		}
	}

	private final class ItemPresentCondition extends ItemCondition {
		ItemPresentCondition (By locator) {
			super (locator);
		}
		
		@Override
		public boolean isSatisfied () {
			List<WebElement> list = mDriver.findElements (mLocator);
			if (list.size () == 1) {
				mElement = list.get (0);
				return true;
			} else {
				return false;
			}
		}
		
		WebElement mElement;
	}

	private final class ItemNotPresentCondition extends ItemCondition {
		ItemNotPresentCondition (By locator) {
			super (locator);
		}
		
		@Override
		public boolean isSatisfied () {
			return mDriver.findElements (mLocator).size () == 0;
		}
	}

	private final class ItemVisibleCondition extends ItemCondition {
		ItemVisibleCondition (By locator) {
			super (locator);
		}
		
		@Override
		public boolean isSatisfied () {
			return getUniqueElement (mLocator).isDisplayed ();
		}
	}

	private final class ItemNotVisibleCondition extends ItemCondition {
		ItemNotVisibleCondition (By locator) {
			super (locator);
		}
		
		@Override
		public boolean isSatisfied () {
			return !getUniqueElement (mLocator).isDisplayed ();
		}
	}

	private final class ItemEnabledCondition extends ItemCondition {
		ItemEnabledCondition (By locator) {
			super (locator);
		}
		
		@Override
		public boolean isSatisfied () {
			return getUniqueElement (mLocator).isEnabled ();
		}
	}

	private final class ItemDisabledCondition extends ItemCondition {
		ItemDisabledCondition (By locator) {
			super (locator);
		}
		
		@Override
		public boolean isSatisfied () {
			return !getUniqueElement (mLocator).isEnabled ();
		}
	}

	@Override
	public int getCount (Item item) {
		final By locator = getLocator (item);
		final List<WebElement> list = mDriver.findElements(locator);
		mRunTime.reportInfo ("the count is : " + list.size());
		return list.size();
	}
	
	@Override
	public Object getTestTool () {
		return mDriver;
	}
}