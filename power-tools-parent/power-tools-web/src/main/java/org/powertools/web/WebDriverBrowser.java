/* Copyright 2012-2014 by Martin Gijsen (www.DeAnalist.nl)
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

package org.powertools.web;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import org.apache.commons.io.FileUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
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
import org.powertools.engine.ExecutionException;
import org.powertools.engine.RunTime;
import org.powertools.web.WebLibrary.BrowserType;
import org.powertools.web.WebLibrary.ItemType;
import org.powertools.web.WebLibrary.KeyType;


class WebDriverBrowser implements IBrowser {
    private static final String CHROMEDRIVER_LOG_FILENAME = "chromedriver.log";


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

    public boolean open (BrowserType type, String browserVersion, String url, String logDirectory, String hubUrl) {
        if (hubUrl == null || hubUrl.isEmpty()) {
            webBrowserRunsOnGrid = false;
            return open(type, url, logDirectory);
        } else {
            webBrowserRunsOnGrid = true;
            return openOnGrid(type, browserVersion, url, hubUrl);
        }
    }


    private boolean openOnGrid (BrowserType type, String browserVersion, String url, String hubUrl) {
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

    private DesiredCapabilities getBrowserCapabilities (BrowserType type) {
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
    public boolean open (BrowserType type, String url, String logDirectory) {
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
    public boolean selectFrame (KeyType keyType, String value) {
        mDriver.switchTo ().frame (mDriver.findElement (getLocator (keyType, value)));
        return true;
    }

    @Override
    public boolean selectDefaultFrame () {
        mDriver.switchTo ().defaultContent ();
        return true;
    }

    @Override
    public boolean type (Item item, final String text) {
        return type (getLocator (item), text);
    }

    @Override
    public boolean type (KeyType keyType, String value, String text) {
        return type (getLocator (keyType, value), text);
    }

    @Override
    public boolean fireEvent (Item item, String event) {
        mRunTime.reportError ("not implemented");
        return false;
    }

    @Override
    public boolean setCheckboxValue(Item item, boolean value) {
        return setCheckboxValue (getLocator (item), value);
    }

    @Override
    public boolean setCheckboxValue(KeyType keyType, String keyValue, boolean value) {
        return setCheckboxValue (getLocator (keyType, keyValue), value);
    }

    private boolean setCheckboxValue (By locator, final boolean value) {
        return executeCommandWhenElementAvailable (locator, mShortDefaultTimeoutInSeconds, new WebCommand() {

            @Override
            public boolean execute (WebElement element) {
                boolean current = element.isSelected ();
                if (current != value) {
                    element.click ();
                }
                return true;
            }
        });
    }

    @Override
    public boolean isSelected (Item item) {
        return isSelected (item.mKeyType, item.mValue);
    }
    
    @Override
    public boolean isSelected (KeyType keyType, String value) {
        return isSelected (getLocator (keyType, value));
    }

    private boolean isSelected (By locator) {
        IsSelectedWebCommand command = new IsSelectedWebCommand ();
        executeCommandWhenElementAvailable (locator, mShortDefaultTimeoutInSeconds, command);
        return command.result;
    }

    private class IsSelectedWebCommand implements WebCommand {
        boolean result;

        @Override
        public boolean execute (WebElement element) {
            result = element.isSelected ();
            return true;
        }
    }

    @Override
    public boolean itemIsPresent (Item item) {
        return getUniqueElement (getLocator (item)) != null;
    }

    @Override
    public boolean itemIsVisible (Item item) {
        return getUniqueElement (getLocator (item)).isDisplayed ();
    }

    @Override
    public boolean itemIsEnabled (Item item) {
        return getUniqueElement (getLocator (item)).isEnabled ();
    }

    @Override
    public boolean itemIsEmpty (Item item) {
        return getUniqueElement (getLocator (item)).getText ().isEmpty ();
    }

    @Override
    public int countItems (Item item) {
        return mDriver.findElements (getLocator (item)).size ();
    }

    @Override
    public boolean click (Item item) {
        return click (getLocator (item));
    }

    @Override
    public boolean click (KeyType keyType, String value) {
        return click (getLocator (keyType, value));
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
    public boolean clickAndWait (KeyType keyType, String value) {
        return click (keyType, value);
    }

    @Override
    public boolean clickAndWait (KeyType keyType, String value, int timout) {
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
    public boolean clickLink (KeyType keyType, String value) {
        return click (getLocator (keyType, value));
    }

    @Override
    public boolean clickAcceptInAlert () {
        try {
            mDriver.switchTo ().alert ().accept ();
            return true;
        } catch (NoAlertPresentException nape) {
            mRunTime.reportError ("no alert found");
            return false;
        }
    }

    @Override
    public boolean clickDismissInAlert () {
        try {
            mDriver.switchTo ().alert ().dismiss ();
            return true;
        } catch (NoAlertPresentException nape) {
            mRunTime.reportError ("no alert found");
            return false;
        }
    }


    @Override
    public boolean selectChoice (Item item) {
        if (item.mType != WebLibrary.ItemType.cListboxItem) {
            mRunTime.reportError ("item is not a listbox item");
            return false;
        } else if (item.mKeyType == KeyType.cPartialText) {
            return selectChoiceByPartialText (item.mParent, item.mValue);
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
                mRunTime.reportError ("unsupported item key: " + item.mKeyType.toString ());
                return false;
            }
        }
    }

    @Override
    public boolean selectChoiceByText (Item selectItem, String optionText) {
        if (selectItem.mType != WebLibrary.ItemType.cListbox) {
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
        if (selectItem.mType != WebLibrary.ItemType.cListbox) {
            mRunTime.reportError ("item is not a listbox");
            return false;
        } else {
            Select listbox           = new Select (mDriver.findElement (getLocator (selectItem)));
            List<WebElement> choices = listbox.getOptions ();
            WebElement theOne        = null;
            for (WebElement option : choices) {
                if (!option.getText ().contains (optionText)) {
                    // ignore: option does not contain the desired text
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
        return mouseOver (getLocator (item));
    }

    @Override
    public boolean waitUntilTextIsPresent (String text) {
        return waitUntilTextIsPresent (text, mShortDefaultTimeoutInSeconds);
    }

    @Override
    public boolean waitUntilTextIsPresent (String text, int timeout) {
        return waitUntilTextIsPresent (getLocator (WebLibrary.KeyType.cTag, "body"), text, timeout);
    }

    @Override
    public boolean waitUntilTextIsNotPresent (String text) {
        return waitUntilTextIsNotPresent (text, mShortDefaultTimeoutInSeconds);
    }

    @Override
    public boolean waitUntilTextIsNotPresent (String text, int timeout) {
        return waitUntilTextIsNotPresent (getLocator (WebLibrary.KeyType.cTag, "body"), text, timeout);
    }

    @Override
    public boolean waitUntilItemIsPresent (Item item) {
        return waitUntilItemIsPresent (getLocator (item), mShortDefaultTimeoutInSeconds);
    }

    @Override
    public boolean waitUntilItemIsPresent (Item item, int timeout) {
        return waitUntilItemIsPresent (getLocator (item), timeout);
    }

    @Override
    public boolean waitUntilItemIsNotPresent (Item item) {
        return waitUntilItemIsNotPresent (getLocator (item), mShortDefaultTimeoutInSeconds);
    }

    @Override
    public boolean waitUntilItemIsNotPresent (Item item, int timeout) {
        return waitUntilItemIsNotPresent (getLocator (item), timeout);
    }

    @Override
    public boolean waitUntilItemIsNotEmpty (Item item) {
        return waitUntilItemIsNotEmpty (getLocator (item), mShortDefaultTimeoutInSeconds);
    }

    @Override
    public boolean waitUntilItemIsNotEmpty (Item item, int timeout) {
        return waitUntilItemIsNotEmpty (getLocator (item), timeout);
    }

    @Override
    public boolean waitUntilItemIsEmpty (Item item) {
        return waitUntilItemIsEmpty (item, mShortDefaultTimeoutInSeconds);
    }

    @Override
    public boolean waitUntilItemIsEmpty (Item item, int timeout) {
        return waitUntilItemIsEmpty (getLocator (item), timeout);
    }

    @Override
    public boolean waitUntilItemIsVisible (Item item) {
        return waitUntilItemIsVisible (getLocator (item), mShortDefaultTimeoutInSeconds);
    }

    @Override
    public boolean waitUntilItemIsVisible (Item item, int timeout) {
        return waitUntilItemIsVisible (getLocator (item), timeout);
    }

    @Override
    public boolean waitUntilItemIsNotVisible (Item item) {
        return waitUntilItemIsNotVisible (getLocator (item), mShortDefaultTimeoutInSeconds);
    }

    @Override
    public boolean waitUntilItemIsNotVisible (Item item, int timeout) {
        return waitUntilItemIsNotVisible (getLocator (item), timeout);
    }

    @Override
    public boolean waitUntilItemIsEnabled (Item item) {
        return waitUntilItemIsEnabled (getLocator (item), mShortDefaultTimeoutInSeconds);
    }

    @Override
    public boolean waitUntilItemIsEnabled (Item item, int timeout) {
        return waitUntilItemIsEnabled (getLocator (item), timeout);
    }

    @Override
    public boolean waitUntilItemIsDisabled (Item item) {
        return waitUntilItemIsDisabled (getLocator (item), mShortDefaultTimeoutInSeconds);
    }

    @Override
    public boolean waitUntilItemIsDisabled (Item item, int timeout) {
        return waitUntilItemIsDisabled (getLocator (item), timeout);
    }

    @Deprecated
    @Override
    public boolean checkForText (String text) {
        return textIsPresent (text);
    }

    @Override
    public boolean textIsPresent (String text) {
        final WebElement element = getUniqueElement (getLocator (WebLibrary.KeyType.cTag, "body"));
        return element == null ? false : element.getText ().contains (text);
    }

    @Override
    public String getItemText (Item item) {
        return getItemText (getLocator (item));
    }

    @Override
    public String getItemText (KeyType keyType, String value) {
        return getItemText (getLocator (keyType, value));
    }

    @Override
    public String getItemAttribute (Item item, String attributeName) {
        return getItemAttribute (getLocator (item), attributeName);
    }

    @Override
    public String getItemAttribute (KeyType keyType, String value, String attributeName) {
        return getItemAttribute (getLocator (keyType, value), attributeName);
    }

    @Override
    public boolean close () {
        mDriver.quit ();
        mDriver = null;
        return true;
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
            WebDriver driver    = webBrowserRunsOnGrid ? new Augmenter ().augment (mDriver) : mDriver;
            File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs (OutputType.FILE);
            FileUtils.copyFile (screenshotFile, new File (path));
            return true;
        } catch (IOException ioe) {
            mRunTime.reportStackTrace (ioe);
            throw new ExecutionException ("Could not copy screenshot file");
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


    // protected members
    protected static final int cOneSecondTimeout = 1000;

    protected final RunTime mRunTime;

    protected int mShortDefaultTimeoutInSeconds = 10;
    protected int mLongDefaultTimeoutInSeconds  = 30;
    protected WebDriver mDriver;

    protected boolean webBrowserRunsOnGrid;


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

    private By getLocator (Item item) {
        if (item.mType != ItemType.cLink) {
            // continue
        } else if (item.mKeyType == KeyType.cText) {
            By locator = By.linkText (item.mValue);
            mRunTime.reportInfo ("locator: " + locator.toString ());
            return locator;
        } else if (item.mKeyType == KeyType.cPartialText) {
            By locator = By.partialLinkText (item.mValue);
            mRunTime.reportInfo ("locator: " + locator.toString ());
            return locator;
        }
        return getLocator (item.mKeyType, item.getValue ());
    }

    private By getLocator (KeyType keyType, String value) {
        By locator;
        switch (keyType) {
        case cClass :
            locator = By.className (value);
            break;
        case cCss :
            locator = By.cssSelector (value);
            break;
        case cId :
            locator = By.id (value);
            break;
        case cName :
            locator = By.name (value);
            break;
        case cTag :
            locator = By.tagName (value);
            break;
        case cXpath :
            locator = By.xpath (value);
            break;
        default:
            throw new ExecutionException ("invalid key: " + keyType);
        }
        mRunTime.reportInfo ("locator: " + locator.toString ());
        return locator;
    }

    private boolean click (By locator) {
        return executeCommandWhenElementAvailable (locator, mShortDefaultTimeoutInSeconds, new WebCommand() {

            @Override
            public boolean execute (WebElement element) {
                element.click ();
                return true;
            }
        });
    }

    private boolean type (By locator, final String text) {
        return executeCommandWhenElementAvailable (locator, mShortDefaultTimeoutInSeconds, new WebCommand() {

            @Override
            public boolean execute (WebElement element) {
                element.clear ();
                element.sendKeys (text);
                return true;
            }
        });
    }

    private boolean waitUntilItemIsPresent(By locator, int timeout) {
        return executeCommandWhenElementAvailable (locator, timeout, new WebCommand() {

            @Override
            public boolean execute (WebElement element) {
                return true;
            }
        });
    }


    private boolean waitUntilTextIsPresent (By locator, final String text, int timeout) {
        return executeCommandWhenElementAvailable (locator, timeout, new WebCommand() {

            @Override
            public boolean execute (WebElement element) {
                return element.getText ().contains (text);
            }
        });
    }

    private boolean waitUntilTextIsNotPresent (By locator, final String text, int timeout) {
        return executeCommandWhenElementAvailable (locator, timeout, new WebCommand() {

            @Override
            public boolean execute (WebElement element) {
                return !element.getText ().contains (text);
            }
        });
    }

    private boolean waitUntilItemIsNotEmpty (By locator, int timeout) {
        return executeCommandWhenElementAvailable (locator, timeout, new WebCommand() {

            @Override
            public boolean execute (WebElement element) {
                return !element.getText ().isEmpty ();
            }
        });
    }

    private boolean waitUntilItemIsEmpty(By locator, int timeout) {
        return executeCommandWhenElementAvailable (locator, timeout, new WebCommand() {

            @Override
            public boolean execute (WebElement element) {
                return element.getText ().isEmpty ();
            }
        });
    }

    private boolean waitUntilItemIsVisible(By locator, int timeout) {
        return executeCommandWhenElementAvailable (locator, timeout, new WebCommand() {

            @Override
            public boolean execute (WebElement element) {
                return element.isDisplayed ();
            }
        });
    }

    private boolean waitUntilItemIsNotVisible(By locator, int timeout) {
        return executeCommandWhenElementAvailable (locator, timeout, new WebCommand() {

            @Override
            public boolean execute (WebElement element) {
                return !element.isDisplayed ();
            }
        });
    }

    private boolean waitUntilItemIsEnabled(By locator, int timeout) {
        return executeCommandWhenElementAvailable (locator, timeout, new WebCommand() {

            @Override
            public boolean execute (WebElement element) {
                return element.isEnabled ();
            }
        });
    }

    private boolean waitUntilItemIsDisabled(By locator, int timeout) {
        return executeCommandWhenElementAvailable (locator, timeout, new WebCommand() {

            @Override
            public boolean execute (WebElement element) {
                return !element.isEnabled ();
            }
        });
    }

    private boolean mouseOver(By locator) {
        return executeCommandWhenElementAvailable (locator, mShortDefaultTimeoutInSeconds, new WebCommand() {

            @Override
            public boolean execute (WebElement element) {
                new Actions (mDriver).moveToElement (element).build ().perform ();
                return true;
            }
        });
    }

    private String getItemText(By locator) {
        GetItemTextCommand command = new GetItemTextCommand();

        boolean succes = executeCommandWhenElementAvailable (locator, mShortDefaultTimeoutInSeconds, command);
        if (!succes) {
            throw new ExecutionException("error during getItemText");
        }

        return command.result;
    }

    private class GetItemTextCommand implements WebCommand {
        String result = null;

        @Override
        public boolean execute (WebElement element) {
            if (element.getTagName ().equalsIgnoreCase ("SELECT")) {
                result = getSelectedOptionText (element);
            } else {
                result = element.getText ();
            }
            mRunTime.reportInfo ("getItemText result: " + result);
            result = (result == null)? result: result.trim ();
            return true;
        }
        
        private String getSelectedOptionText (WebElement listbox) {
            List<WebElement> options = listbox.findElements (By.tagName ("option"));
            for (WebElement option : options) {
                if (option.isSelected ()) {
                    return option.getText ();
                }
            }
            return "";
        }
    }

    private String getItemAttribute (By locator, String attributeName) {
        GetItemAttributeCommand command = new GetItemAttributeCommand (attributeName);

        boolean succes = executeCommandWhenElementAvailable (locator, mShortDefaultTimeoutInSeconds, command);
        if (!succes) {
            throw new ExecutionException("error during getItemAttribute");
        }

        return command.result;
    }

    private class GetItemAttributeCommand implements WebCommand {
        final String mAttributeName;
        String result = null;
        
        GetItemAttributeCommand (String attributeName) {
            mAttributeName = attributeName;
        }

        @Override
        public boolean execute (WebElement element) {
            result = element.getAttribute (mAttributeName);
            mRunTime.reportInfo ("getItemAttribute result: " + result);
            result = (result == null ? result :  result.trim ());
            return true;
        }
    }

    private boolean executeCommandWhenElementAvailable (By locator, int timeoutInSec, WebCommand cmd) {
        for (int nrOfSeconds = 0; nrOfSeconds < timeoutInSec; ++nrOfSeconds) {
            try {
                WebElement element = getUniqueElement(locator);
                if (cmd.execute (element)) {
                    return true;
                }
            } catch (WebDriverException wde) {
                // This error occurs occasionally in IE when using SeleniumGrid
            } catch (ExecutionException ee) {
                System.out.println (ee.getMessage ());
                // This error occurs when the element is not yet available 
            }
            sleep (cOneSecondTimeout);
        }
        return false;
    }

    private boolean waitUntilItemIsNotPresent (By locator, int timeout) {
        for (int nrOfSeconds = 0; nrOfSeconds < timeout; ++nrOfSeconds) {
            try {
                if (mDriver.findElements (locator).isEmpty ()) {
                    return true;
                }
            } catch (WebDriverException e) {
                // This error occurs occasionally in IE when using SeleniumGrid
            }
            sleep (cOneSecondTimeout);
        }
        return false;
    }

    private void sleep (long ms) {
        try {
            Thread.sleep (ms);
        } catch (InterruptedException ie) {
            // ignore
        }
    }

    private interface WebCommand {
        boolean execute(WebElement element);
    }
}
