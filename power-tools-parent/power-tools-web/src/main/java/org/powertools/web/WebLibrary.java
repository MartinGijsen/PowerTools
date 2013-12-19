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

import java.util.HashMap;
import java.util.Map;

import org.powertools.engine.InstructionSet;
import org.powertools.engine.RunTime;


public abstract class WebLibrary implements InstructionSet {
	enum IItemType {
		cButton,
		cCheckbox,
		cCombobox,
		cFrame,
		cImage,
		cLink,
		cListbox,
		cListboxItem,
		cRadioButton,
		cText
	}

	enum IBrowserType {
		cFirefox,
		cInternetExplorer,
		cChrome
	}
	
	enum IKeyType {
		cDom,
		cId,
		cIndex,
		cName,
		cText,
		cValue,
		cXpath,
		cCss,
		cTag
	}
	
	public IBrowser mBrowser;

	public final Object getTesttool () {
		return mBrowser.getTestTool ();
	}
	
	@Override
	public String getName () {
		return "web";
	}
	
	@Override
	public final void setup () {
		// empty
	}
	
	@Override
	public final void cleanup () {
		mBrowser.cleanup ();
	}
	
	
	public static IItemType getItemType (String name) {
		IItemType itemType = cItemTypesMap.get (name);
		if (itemType != null) {
			return itemType;
		} else {
			throw new IllegalArgumentException ("unknown item type: " + name);
		}
	}

	public static IBrowserType getBrowserType (String name) {
		IBrowserType browserType = cBrowserTypesMap.get (name.toLowerCase ());
		if (browserType != null) {
			return browserType;
		} else {
			throw new IllegalArgumentException ("unknown browser type: " + name);
		}
	}

	public static IKeyType getKeyType (String name) {
		IKeyType keyType = cKeyTypesMap.get (name);
		if (keyType != null) {
			return keyType;
		} else {
			throw new IllegalArgumentException ("unknown key type: " + name);
		}
	}


	// item methods
	public Item getItem (String logicalName) {
		try {
			return mItemMap.get (logicalName);
		} catch (NullPointerException npe) {
			return null;
		}
	}

	
	public final boolean MaximizeBrowser () {
		return mBrowser.maximize ();
	}
	
	public final boolean MinimizeBrowser () {
		return mBrowser.minimize ();
	}
	
	public final boolean OpenURL_ (String url) {
		return mBrowser.openUrl (completeUrl (url));
	}
	
	public final boolean RefreshPage () {
		return mBrowser.refreshPage ();
	}

	public final boolean CloseBrowser () {
		final boolean result = mBrowser.close ();
		mBrowser			 = null;
		return result;
	}

	
	// configuration instructions
	public final boolean SetShortDefaultTimeoutTo_ (int timeout) {
		return mBrowser.setShortDefaultTimeout (timeout);
	}

	public final boolean SetLongDefaultTimeoutTo_ (int timeout) {
		return mBrowser.setLongDefaultTimeout (timeout);
	}


	// item declarations
	public final boolean Name_ParentName_Type_Key_Value_ (String name, String parentName, String type, String keyTypeString, String value) {
		if ("".equals (name) || "".equals (type) || "".equals (keyTypeString) ||"".equals (value)) {
			mRunTime.reportError ("name, type, key and value must not be empty");
		} else {
			try {
				boolean parentIsOk 	= true;
				Item parentItem 	= null;
				if (!"".equals (parentName)) {
					parentItem = getItem (parentName);
					if (parentItem == null) {
						mRunTime.reportError ("unknown parent item: " + parentName);
						parentIsOk = false;
					}
				}

				Item item;
				IKeyType keyType = getKeyType (keyTypeString);
				if (keyType == IKeyType.cXpath) {
					item = new XpathItem (name, parentItem, getItemType (type), value);
				} else {
					item = new Item (name, parentItem, getItemType (type), keyType, value);
				}

				if (mItemMap.containsKey (item.mLogicalName)) {
					mRunTime.reportError ("duplicate item name: " + item.mLogicalName);
					return false;
				} else {
					mItemMap.put (item.mLogicalName, item);
					return parentIsOk;
				}
			} catch (IllegalArgumentException iae) {
				mRunTime.reportError (iae.getMessage ());
			}
		}
		return false;
	}

	public final boolean SetParameter_ForItem_To_ (int parameterNr, String itemName, String value) {
		final Item item	= findItem (itemName);
		return item != null && item.setParameterValue (parameterNr, value);
	}


	// operations on frames
	public final boolean SelectFrame_ (String itemName) {
		final Item item	= findItem (itemName);
		if (item != null) {
			return mBrowser.selectFrame (item);
		} else {
			return false;
		}
	}
	
	public final boolean SelectFrameWhere_Is_ (String key, String value) {
		try {
			return mBrowser.selectFrame (getKeyType (key), value);
		} catch (IllegalArgumentException iae) {
			mRunTime.reportError (iae.getMessage ());
			return false;
		}
	}
	
	public final boolean SelectDefaultFrame () {
		return mBrowser.selectDefaultFrame ();
	}
	
	
	// operations on items
	public final boolean CheckPageTitleIs_ (String expectedTitle) {
		final String actualTitle = mBrowser.getPageTitle ();
		if (actualTitle.equals (expectedTitle)) {
			return true;
		} else {
			mRunTime.reportValueError ("page title", actualTitle, expectedTitle);
			return false;
		}
	}

	public final boolean CheckPageTitleContains_ (String expectedTitle) {
		final String actualTitle = mBrowser.getPageTitle ();
		if (actualTitle.indexOf (expectedTitle) >= 0) {
			return true;
		} else {
			mRunTime.reportError ("page title is '" + actualTitle + "'");
			return false;
		}
	}

	public final boolean CheckForText_ (String text) {
		return mBrowser.checkForText (text);
	}

	public final boolean CheckItem_IsEmpty (String itemName) {
		final Item item	= findItem (itemName);
		if (item != null) {
			final String actualText = mBrowser.getItemText (item);
			if (actualText == null) {
				//mPublisher.publishError ("item not found on this page or not unique");
			} else if (!actualText.isEmpty ()) {
				mRunTime.reportValueError ("item " + itemName, actualText, "<empty>");
			} else {
				return true;
			}
		}
		return false;
	}
	
	public final boolean CheckItem_IsNotEmpty (String itemName) {
		final Item item	= findItem (itemName);
		if (item != null) {
			final String actualText = mBrowser.getItemText (item);
			if (actualText == null) {
				//mPublisher.publishError ("item not found on this page or not unique");
			} else if (actualText.isEmpty ()) {
				mRunTime.reportError ("item " + itemName + " is empty");
			} else {
				return true;
			}
		}
		return false;
	}
	
	public final boolean CheckTextOfItem_Is_ (String itemName, String expectedText) {
		final Item item	= findItem (itemName);
		if (item != null) {
			final String actualText = mBrowser.getItemText (item);
			if (actualText == null) {
				//mPublisher.publishError ("item not found on this page or not unique");
			} else if (!actualText.equals (expectedText)) {
				mRunTime.reportValueError ("item " + itemName, actualText, expectedText);
			} else {
				return true;
			}
		}
		return false;
	}
	
	public final boolean CheckTextOfItem_IsNot_ (String itemName, String unexpectedText) {
		final Item item	= findItem (itemName);
		if (item != null) {
			final String actualText = mBrowser.getItemText (item);
			if (actualText == null) {
				//mPublisher.publishError ("item not found on this page or not unique");
			} else if (actualText.equals (unexpectedText)) {
				mRunTime.reportError ("actual text is '" + actualText + "'");
			} else {
				return true;
			}
		}
		return false;
	}
	
	public final boolean CheckTextOfItem_Contains_ (String itemName, String expectedText) {
		final Item item	= findItem (itemName);
		if (item != null) {
			final String actualText = mBrowser.getItemText (item);
			if (actualText == null) {
				//mPublisher.publishError ("item not found on this page or not unique");
			} else if (actualText.indexOf (expectedText) < 0) {
				mRunTime.reportError ("expected text '" + expectedText + "' not found in actual text '" + actualText + "'");
			} else {
				return true;
			}
		}
		return false;
	}
	
	public final boolean CheckTextOfItem_DoesNotContain_ (String itemName, String unexpectedText) {
		final Item item	= findItem (itemName);
		if (item != null) {
			final String actualText = mBrowser.getItemText (item);
			if (actualText == null) {
				//mPublisher.publishError ("item not found on this page or not unique");
			} else if (actualText.indexOf (unexpectedText) >= 0) {
				mRunTime.reportError ("unexpected text '" + unexpectedText + "' found in actual text '" + actualText + "'");
			} else {
				return true;
			}
		}
		return false;
	}
	
	public final boolean SelectLink_ (String itemName) {
		final Item item = findItem (itemName);
		return item != null && mBrowser.clickLink (item);
	}
	
	public final boolean SelectLinkByText_ (String text) {
		return mBrowser.clickLink (text);
	}

	public final boolean SelectLinkWhere_Is_ (String keyTypeString, String value) {
		try {
			return mBrowser.clickLink (getKeyType (keyTypeString), value);
		} catch (IllegalArgumentException iae) {
			mRunTime.reportError (iae.getMessage ());
			return false;
		}
	}
	
	public final boolean SelectOption_ (String itemName) {
		final Item item = findItem (itemName);
		return item != null && mBrowser.selectChoice (item);
	}

	public final boolean SelectOptionIn_ByText_ (String selectItemName, String text) {
		final Item item = findItem (selectItemName);
		return item != null && mBrowser.selectChoiceByText (item, text);
	}

	public final boolean SelectOptionIn_ByPartialText_ (String selectItemName, String text) {
		final Item item = findItem (selectItemName);
		return item != null && mBrowser.selectChoiceByPartialText (item, text);
	}

	public final boolean ClickItem_ (String itemName) {
		final Item item = findItem (itemName);
		return item != null && mBrowser.click (item);
	}

	public final boolean ClickItemWhere_Is_ (String keyTypeString, String value) {
		try {
			return mBrowser.click (getKeyType (keyTypeString), value);
		} catch (IllegalArgumentException iae) {
			mRunTime.reportError (iae.getMessage ());
			return false;
		}
	}
	
	public final boolean ClickItem_AndWait (String itemName) {
		final Item item = findItem (itemName);
		return item != null && mBrowser.clickAndWait (item);
	}

	public final boolean ClickItemWhere_Is_AndWait (String keyTypeString, String value) {
		try {
			return mBrowser.clickAndWait (getKeyType (keyTypeString), value);
		} catch (IllegalArgumentException iae) {
			mRunTime.reportError (iae.getMessage ());
			return false;
		}
	}
	

	public boolean ClickAcceptInAlert()	{
        return mBrowser.clickAcceptInAlert();
	}
	
	
	public final boolean MoveMouseOver_ (String itemName) {
		final Item item = findItem (itemName);
		return item != null && mBrowser.mouseOver (item);
	}
	
	public final boolean Type_IntoItem_ (String text, String itemName) {
		final Item item	= findItem (itemName);
		return item != null && mBrowser.type (item, text);
	}

	public final boolean Type_IntoItemWhere_Is_ (String text, String keyTypeString, String value) {
		try {
			return mBrowser.type (getKeyType (keyTypeString), value, text);
		} catch (IllegalArgumentException iae) {
			mRunTime.reportError (iae.getMessage ());
			return false;
		}
	}
	
	
	public boolean Set_IntoCheckbox_(String text, String itemName) {
		final Item item = findItem(itemName);
		boolean value = makeBoolean(text);
		return mBrowser.setCheckboxValue(item, value);
	}

	public boolean Set_IntoCheckboxWhere_Is_(String text, String keyTypeString,	String value) {
		try {
			boolean checkValue = makeBoolean(text);
			return mBrowser.setCheckboxValue(getKeyType(keyTypeString), value, checkValue);
		} catch (IllegalArgumentException iae) {
			mRunTime.reportError(iae.getMessage());
			return false;
		}
	}

	public final boolean WaitForText_ (String text) {
		return WaitUntilText_IsPresent (text);
	}

	public final boolean WaitUntilText_IsPresent (String text) {
		return mBrowser.waitUntilTextIsPresent (text);
	}

	public final boolean WaitUntilText_IsNotPresent (String text) {
		return mBrowser.waitUntilTextIsNotPresent (text);
	}

	public final boolean WaitForItem_ (String itemName) {
		return WaitUntilItem_IsPresent (itemName);
	}
	
	public final boolean WaitUntilItem_IsPresent (String itemName) {
		final Item item	= findItem (itemName);
		return item != null && mBrowser.waitUntilItemIsPresent (item);
	}
	
	public final boolean WaitUntilItem_IsNotPresent (String itemName) {
		final Item item	= findItem (itemName);
		return item != null && mBrowser.waitUntilItemIsNotPresent (item);
	}
	
	public final boolean WaitForItem_Filled (String itemName) {
		return WaitUntilItem_IsFilled (itemName);
	}
	
	public final boolean WaitUntilItem_IsFilled (String itemName) {
		final Item item	= findItem (itemName);
		return item != null && mBrowser.waitUntilItemIsFilled (item);
	}
	
	public final boolean WaitUntilItem_IsEmpty (String itemName) {
		final Item item	= findItem (itemName);
		return item != null && mBrowser.waitUntilItemIsEmpty (item);
	}
	
	public final boolean WaitForItem_Visible (String itemName) {
		return WaitUntilItem_IsVisible (itemName);
	}
	
	public final boolean WaitUntilItem_IsVisible (String itemName) {
		final Item item	= findItem (itemName);
		return item != null && mBrowser.waitUntilItemIsVisible (item);
	}
	
	public final boolean WaitUntilItem_IsNotVisible (String itemName) {
		final Item item	= findItem (itemName);
		return item != null && mBrowser.waitUntilItemIsNotVisible (item);
	}
	
	public final boolean WaitUntilItem_IsEnabled (String itemName) {
		final Item item	= findItem (itemName);
		return item != null && mBrowser.waitUntilItemIsEnabled (item);
	}
	
	public final boolean WaitUntilItem_IsDisabled (String itemName) {
		final Item item	= findItem (itemName);
		return item != null && mBrowser.waitUntilItemIsDisabled (item);
	}
	
	public final boolean CheckItem_IsPresent (String itemName) {
		final Item item	= findItem (itemName);
		return item != null && mBrowser.itemExists (item);
	}
	
	public final boolean CheckItem_IsNotPresent (String itemName) {
		final Item item	= findItem (itemName);
		return item != null && !mBrowser.itemExists (item);
	}

	
	public final boolean CheckThat_Items_ArePresent (String itemName, int count) {
		final Item item = findItem (itemName);
		return item != null && mBrowser.getCount (item) >= count;
	}

	public final boolean CheckAtLeast_Items_ArePresent (int expectedNrOfItems, String itemName) {
		final Item item	= findItem (itemName);
		if (item != null) {
			final int actualNrOfItems = mBrowser.countItems (item);
			if (actualNrOfItems >= expectedNrOfItems) {
				return true;
			} else {
				mRunTime.reportError ("number of items: " + actualNrOfItems);
				return false;
			}
		} else {
			return false;
		}
	}
	
	public final boolean MakeScreenshot () {
		String filename = "screenshot" + ++mLastScreenshotNr + ".png";
		String path	= mRunTime.getContext ().getResultsDirectory () + filename;
		boolean ok	= mBrowser.makeScreenshot (path);
		if (ok) {
			mRunTime.reportLink (filename);
		}
		return ok;
	}
	
/*
	public final boolean ClearEvents () {
		mBrowser.clearNetworkTraffic ();
		mRequests = null;
		return true;
	}

	public final boolean SelectEvents () {
		mRequests = mBrowser.getNetworkTraffic ();
		final Iterator<HtmlRequest> iter = mRequests.iterator ();
		while (iter.hasNext ()) {
			if (!iter.next ().url.startsWith ("http://stat.detelefoongids.nl")) {
				iter.remove ();
			}
		}
		return true;
	}

	public final boolean Event_Attribute_Value_ (String eventName, String attributeName, String value) {
		return mEvents.addAttribute (eventName, attributeName, value);
	}
	
	public final boolean CheckForEvent_ (String eventName) {
		return mEvents.checkEvent (mRequests, eventName);
	}
*/
	public final boolean PutItem_In_ (String itemName, String variableName) {
		final Item item = findItem (itemName);
		if (item != null) {
			mRunTime.setValue (variableName, mBrowser.getItemText (item));
			return true;
		} else {
			return false;
		}
	}


	// protected members

	// names for item types
	protected interface IItemTypeName {
		String cButton		= "button";
		String cCheckbox	= "checkbox";
		String cCombobox	= "combobox";
		String cFrame		= "frame";
		String cImage		= "image";
		String cLink		= "link";
		String cListbox		= "listbox";
		String cListboxItem	= "listbox item";
		String cRadioButton	= "radio button";
		String cText		= "text input";
	}

	// names for key types
	protected interface IKeyTypeName {
		String cDom		= "dom";
		String cId		= "id";
		String cIndex	= "index";
		String cName	= "name";
		String cText	= "text";
		String cValue	= "value";
		String cXpath	= "xpath";
		String cCss		= "css";
		String cTag		= "tag";
	}
	
	// names for browsers
	protected interface IBrowserName {
		String cFirefox				= "firefox";
		String cIe					= "ie";
		String cInternetExplorer	= "internet explorer";
		String cChrome				= "chrome";
	}

	protected final RunTime mRunTime;
	//protected final Events mEvents;
	//protected final Collection<HtmlRequest> mRequests;

	protected int mLastScreenshotNr;


	protected WebLibrary (RunTime runTime) {
		mRunTime			= runTime;
		mLastScreenshotNr	= 0;
		mItemMap			= new HashMap<String, Item> ();
		//mBrowserMap		= new HashMap<String, IBrowser> ();
		//mEvents			= new Events (runTime);
	}

	
	protected final String completeUrl (String url) {
		return url.startsWith ("http") ? url : "http:" + url;
	}

	protected Item findItem (String name) {
		final Item item = getItem (name);
		if (item == null) {
			mRunTime.reportError ("no item declared named '" + name + "'");
		}
		return item;
	}

	
	// private members
	private static final Map<String, IItemType> cItemTypesMap;
	private static final Map<String, IBrowserType> cBrowserTypesMap;
	private static final Map<String, IKeyType> cKeyTypesMap;

	private final Map<String, Item> mItemMap;
	
	
	private boolean makeBoolean(String text) {
		if ("true".equalsIgnoreCase (text) || "on".equalsIgnoreCase (text)) {
			return true;
		}
		if ("false".equalsIgnoreCase (text) || "off".equalsIgnoreCase (text)) {
			return false;
		}
		String msg = "Invalid value: " + text + " valid values: true/false/on/off";
		throw new IllegalArgumentException(msg);
	}

	//private final Map<String, IBrowser> mBrowserMap;

	static {
		cItemTypesMap = new HashMap<String, IItemType> ();
		cItemTypesMap.put (IItemTypeName.cButton,		IItemType.cButton);
		cItemTypesMap.put (IItemTypeName.cCheckbox,		IItemType.cCheckbox);
		cItemTypesMap.put (IItemTypeName.cCombobox,		IItemType.cCombobox);
		cItemTypesMap.put (IItemTypeName.cFrame,		IItemType.cFrame);
		cItemTypesMap.put (IItemTypeName.cImage,		IItemType.cImage);
		cItemTypesMap.put (IItemTypeName.cLink,			IItemType.cLink);
		cItemTypesMap.put (IItemTypeName.cListbox,		IItemType.cListbox);
		cItemTypesMap.put (IItemTypeName.cListboxItem,	IItemType.cListboxItem);
		cItemTypesMap.put (IItemTypeName.cRadioButton,	IItemType.cRadioButton);
		cItemTypesMap.put (IItemTypeName.cText,			IItemType.cText);
		
		cBrowserTypesMap = new HashMap<String, IBrowserType> ();
		cBrowserTypesMap.put (IBrowserName.cFirefox,			IBrowserType.cFirefox);
		cBrowserTypesMap.put (IBrowserName.cIe,					IBrowserType.cInternetExplorer);
		cBrowserTypesMap.put (IBrowserName.cInternetExplorer,	IBrowserType.cInternetExplorer);
		cBrowserTypesMap.put (IBrowserName.cChrome,				IBrowserType.cChrome);

		cKeyTypesMap = new HashMap<String, IKeyType> ();
		cKeyTypesMap.put (IKeyTypeName.cDom,	IKeyType.cDom);
		cKeyTypesMap.put (IKeyTypeName.cId,		IKeyType.cId);
		cKeyTypesMap.put (IKeyTypeName.cIndex,	IKeyType.cIndex);
		cKeyTypesMap.put (IKeyTypeName.cName,	IKeyType.cName);
		cKeyTypesMap.put (IKeyTypeName.cText,	IKeyType.cText);
		cKeyTypesMap.put (IKeyTypeName.cValue,	IKeyType.cValue);
		cKeyTypesMap.put (IKeyTypeName.cXpath,	IKeyType.cXpath);
		cKeyTypesMap.put (IKeyTypeName.cCss,	IKeyType.cCss);
		cKeyTypesMap.put (IKeyTypeName.cTag,	IKeyType.cTag);
	}
}