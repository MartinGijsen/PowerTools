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

package org.powertools.web;

import java.util.HashMap;
import java.util.Map;

import org.powertools.engine.InstructionSet;
import org.powertools.engine.KeywordName;
import org.powertools.engine.RunTime;


public abstract class WebLibrary implements InstructionSet {
    enum ItemType {
        cButton,
        cCheckbox,
        cCombobox,
        cFrame,
        cImage,
        cLink,
        cListbox,
        cListboxItem,
        cRadioButtonGroup,
        cRadioButton,
        cText
    }

    enum BrowserType {
        cFirefox,
        cInternetExplorer,
        cChrome
    }

    enum KeyType {
        cClass,
        cCss,
        cDom,
        cId,
        cIndex,
        cName,
        cPartialText,
        cTag,
        cText,
        cValue,
        cXpath
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
        if (mBrowser != null) {
            mBrowser.cleanup ();
        }
    }


    public static ItemType getItemType (String name) {
        ItemType itemType = cItemTypesMap.get (name);
        if (itemType != null) {
            return itemType;
        } else {
            throw new IllegalArgumentException ("unknown item type: " + name);
        }
    }

    public static BrowserType getBrowserType (String name) {
        BrowserType browserType = cBrowserTypesMap.get (name.toLowerCase ());
        if (browserType != null) {
            return browserType;
        } else {
            throw new IllegalArgumentException ("unknown browser type: " + name);
        }
    }

    public static KeyType getKeyType (String name) {
        KeyType keyType = cKeyTypesMap.get (name);
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

    @KeywordName ("OpenURL")
    public final boolean OpenURL_ (String url) {
        return mBrowser.openUrl (completeUrl (url));
    }

    public final boolean RefreshPage () {
        return mBrowser.refreshPage ();
    }

    public final boolean CloseBrowser () {
        final boolean result = mBrowser.close ();
        mBrowser             = null;
        return result;
    }


    // configuration instructions
    @KeywordName ("SetShortDefaultTimeout")
    public final boolean SetShortDefaultTimeoutTo_ (int timeout) {
        return mBrowser.setShortDefaultTimeout (timeout);
    }

    @KeywordName ("SetLongDefaultTimeout")
    public final boolean SetLongDefaultTimeoutTo_ (int timeout) {
        return mBrowser.setLongDefaultTimeout (timeout);
    }


    // item declarations
    @KeywordName ("DeclareItem")
    public final boolean Name_ParentName_Type_Key_Value_ (String name, String parentName, String type, String keyTypeString, String value) {
        if (parametersValid (name, type, keyTypeString, value)) {
            try {
                Item parentItem = ("".equals (parentName) ? null : findItem (parentName));
                Item item       = createItem (name, parentItem, type, keyTypeString, value);
                if (mItemMap.containsKey (item.mLogicalName)) {
                    mRunTime.reportError ("duplicate item name: " + item.mLogicalName);
                } else {
                    mItemMap.put (item.mLogicalName, item);
                }
            } catch (IllegalArgumentException iae) {
                mRunTime.reportError (iae.getMessage ());
            }
        }
        return true;
    }

    private boolean parametersValid (String name, String type, String keyTypeString, String value) {
        if ("".equals (name) || "".equals (type) || "".equals (keyTypeString) || "".equals (value)) {
            mRunTime.reportError ("name, type, key and value must not be empty");
            return false;
        } else {
            return true;
        }
    }

    private Item createItem (String name, Item parentItem, String type, String keyTypeString, String value) {
        KeyType keyType = getKeyType (keyTypeString);
        if (keyType == KeyType.cXpath) {
            return new XpathItem (name, parentItem, getItemType (type), value);
        } else {
            return new Item (name, parentItem, getItemType (type), keyType, value);
        }
    }

    @KeywordName ("SetItemParameter")
    public final boolean SetParameter_ForItem_To_ (int parameterNr, String itemName, String value) {
        final Item item = findItem (itemName);
        return item != null && item.setParameterValue (parameterNr, value);
    }


    // operations on frames
    @KeywordName ("SelectFrame")
    public final boolean SelectFrame_ (String itemName) {
        final Item item = findItem (itemName);
        if (item != null) {
            return mBrowser.selectFrame (item);
        } else {
            return false;
        }
    }

    @KeywordName ("SelectFrameByKeyValue")
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
    @KeywordName ("CheckPageTitle")
    public final boolean CheckPageTitleIs_ (String expectedTitle) {
        final String actualTitle = mBrowser.getPageTitle ();
        if (actualTitle.equals (expectedTitle)) {
            return true;
        } else {
            mRunTime.reportValueError ("page title", actualTitle, expectedTitle);
            return false;
        }
    }

    @KeywordName ("CheckPartialPageTitle")
    public final boolean CheckPageTitleContains_ (String expectedTitle) {
        final String actualTitle = mBrowser.getPageTitle ();
        if (actualTitle.indexOf (expectedTitle) >= 0) {
            return true;
        } else {
            mRunTime.reportError ("page title is '" + actualTitle + "'");
            return false;
        }
    }

    @KeywordName ("CheckForText")
    public final boolean CheckForText_ (String text) {
        return mBrowser.checkForText (text);
    }

    @KeywordName ("CheckItemIsEmpty")
    public final boolean CheckItem_IsEmpty (String itemName) {
        final Item item = findItem (itemName);
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

    @KeywordName ("CheckItemIsNotEmpty")
    public final boolean CheckItem_IsNotEmpty (String itemName) {
        final Item item = findItem (itemName);
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

    @KeywordName ("CheckItemTextIs")
    public final boolean CheckTextOfItem_Is_ (String itemName, String expectedText) {
        final Item item = findItem (itemName);
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

    @KeywordName ("CheckItemTextIsNot")
    public final boolean CheckTextOfItem_IsNot_ (String itemName, String unexpectedText) {
        final Item item = findItem (itemName);
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

    @KeywordName ("CheckItemTextContains")
    public final boolean CheckTextOfItem_Contains_ (String itemName, String expectedText) {
        final Item item = findItem (itemName);
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

    @KeywordName ("CheckItemTextContainsNot")
    public final boolean CheckTextOfItem_DoesNotContain_ (String itemName, String unexpectedText) {
        final Item item = findItem (itemName);
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

    @KeywordName ("SelectLink")
    public final boolean SelectLink_ (String itemName) {
        final Item item = findItem (itemName);
        return item != null && mBrowser.clickLink (item);
    }

    @KeywordName ("SelectLinkByText")
    public final boolean SelectLinkByText_ (String text) {
        return mBrowser.clickLink (text);
    }

    @KeywordName ("SelectLinkByKeyValue")
    public final boolean SelectLinkWhere_Is_ (String keyTypeString, String value) {
        try {
            return mBrowser.clickLink (getKeyType (keyTypeString), value);
        } catch (IllegalArgumentException iae) {
            mRunTime.reportError (iae.getMessage ());
            return false;
        }
    }

    @KeywordName ("SelectOption")
    public final boolean SelectOption_ (String itemName) {
        final Item item = findItem (itemName);
        return item != null && mBrowser.selectChoice (item);
    }

    @KeywordName ("SelectOptionByText")
    public final boolean SelectOptionIn_ByText_ (String selectItemName, String text) {
        final Item item = findItem (selectItemName);
        return item != null && mBrowser.selectChoiceByText (item, text);
    }

    @KeywordName ("SelectOptionByPartialText")
    public final boolean SelectOptionIn_ByPartialText_ (String selectItemName, String text) {
        final Item item = findItem (selectItemName);
        return item != null && mBrowser.selectChoiceByPartialText (item, text);
    }

    @KeywordName ("ClickItem")
    public final boolean ClickItem_ (String itemName) {
        final Item item = findItem (itemName);
        return item != null && mBrowser.click (item);
    }

    @KeywordName ("ClickItemByKeyValue")
    public final boolean ClickItemWhere_Is_ (String keyTypeString, String value) {
        try {
            return mBrowser.click (getKeyType (keyTypeString), value);
        } catch (IllegalArgumentException iae) {
            mRunTime.reportError (iae.getMessage ());
            return false;
        }
    }

    @KeywordName ("ClickItemAndWait")
    public final boolean ClickItem_AndWait (String itemName) {
        final Item item = findItem (itemName);
        return item != null && mBrowser.clickAndWait (item);
    }

    @KeywordName ("ClickItemByKeyValueAndWait")
    public final boolean ClickItemWhere_Is_AndWait (String keyTypeString, String value) {
        try {
            return mBrowser.clickAndWait (getKeyType (keyTypeString), value);
        } catch (IllegalArgumentException iae) {
            mRunTime.reportError (iae.getMessage ());
            return false;
        }
    }


    @KeywordName ("ClickAcceptInAlert")
    public boolean ClickAcceptInAlert() {
        return mBrowser.clickAcceptInAlert ();
    }

    @KeywordName ("ClickDismissInAlert")
    public boolean ClickDismissInAlert() {
        return mBrowser.clickDismissInAlert ();
    }


    @KeywordName ("MoveMouseOver")
    public final boolean MoveMouseOver_ (String itemName) {
        final Item item = findItem (itemName);
        return item != null && mBrowser.mouseOver (item);
    }

    @KeywordName ("TypeIntoItem")
    //@ParameterOrder ({ 2, 1 })
    public final boolean Type_IntoItem_ (String text, String itemName) {
        final Item item = findItem (itemName);
        return item != null && mBrowser.type (item, text);
    }

    @KeywordName ("TypeIntoItemByKeyValue")
    public final boolean Type_IntoItemWhere_Is_ (String text, String keyTypeString, String value) {
        try {
            return mBrowser.type (getKeyType (keyTypeString), value, text);
        } catch (IllegalArgumentException iae) {
            mRunTime.reportError (iae.getMessage ());
            return false;
        }
    }


    public boolean Set_IntoCheckbox_(String text, String itemName) {
        final Item item = findItem (itemName);
        boolean value = makeBoolean (text);
        return mBrowser.setCheckboxValue (item, value);
    }

    public boolean Set_IntoCheckboxWhere_Is_(String text, String keyTypeString, String value) {
        try {
            boolean checkValue = makeBoolean(text);
            return mBrowser.setCheckboxValue(getKeyType(keyTypeString), value, checkValue);
        } catch (IllegalArgumentException iae) {
            mRunTime.reportError(iae.getMessage());
            return false;
        }
    }

    public boolean CheckItem_IsSelected (String itemName) {
        final Item item = findItem (itemName);
        return item != null && mBrowser.isSelected (item);
    }

    public boolean CheckItem_IsNotSelected (String itemName) {
        final Item item = findItem (itemName);
        return item != null && !mBrowser.isSelected (item);
    }

    @KeywordName ("WaitForText")
    public final boolean WaitForText_ (String text) {
        return WaitUntilText_IsPresent (text);
    }

    @KeywordName ("WaitUntilTextIsPresent")
    public final boolean WaitUntilText_IsPresent (String text) {
        return mBrowser.waitUntilTextIsPresent (text);
    }

    @KeywordName ("WaitUntilTextIsNotPresent")
    public final boolean WaitUntilText_IsNotPresent (String text) {
        return mBrowser.waitUntilTextIsNotPresent (text);
    }

    @KeywordName ("WaitForItem")
    public final boolean WaitForItem_ (String itemName) {
        return WaitUntilItem_IsPresent (itemName);
    }

    @KeywordName ("WaitUntilItemIsPresent")
    public final boolean WaitUntilItem_IsPresent (String itemName) {
        final Item item = findItem (itemName);
        return item != null && mBrowser.waitUntilItemIsPresent (item);
    }

    @KeywordName ("WaitUntilItemIsNotPresent")
    public final boolean WaitUntilItem_IsNotPresent (String itemName) {
        final Item item = findItem (itemName);
        return item != null && mBrowser.waitUntilItemIsNotPresent (item);
    }

    public final boolean WaitForItem_Filled (String itemName) {
        return WaitUntilItem_IsFilled (itemName);
    }

    // TODO: remove in 2015
    @Deprecated
    @KeywordName ("WaitUntilItemIsFilled")
    public final boolean WaitUntilItem_IsFilled (String itemName) {
        return WaitUntilItem_IsNotEmpty (itemName);
    }

    @KeywordName ("WaitUntilItemIsNotEmpty")
    public final boolean WaitUntilItem_IsNotEmpty (String itemName) {
        final Item item = findItem (itemName);
        return item != null && mBrowser.waitUntilItemIsNotEmpty (item);
    }

    @KeywordName ("WaitUntilItemIsEmpty")
    public final boolean WaitUntilItem_IsEmpty (String itemName) {
        final Item item = findItem (itemName);
        return item != null && mBrowser.waitUntilItemIsEmpty (item);
    }

    public final boolean WaitForItem_Visible (String itemName) {
        return WaitUntilItem_IsVisible (itemName);
    }

    @KeywordName ("WaitUntilItemIsVisible")
    public final boolean WaitUntilItem_IsVisible (String itemName) {
        final Item item = findItem (itemName);
        return item != null && mBrowser.waitUntilItemIsVisible (item);
    }

    @KeywordName ("WaitUntilItemIsNotVisible")
    public final boolean WaitUntilItem_IsNotVisible (String itemName) {
        final Item item = findItem (itemName);
        return item != null && mBrowser.waitUntilItemIsNotVisible (item);
    }

    @KeywordName ("WaitUntilItemIsEnabled")
    public final boolean WaitUntilItem_IsEnabled (String itemName) {
        final Item item = findItem (itemName);
        return item != null && mBrowser.waitUntilItemIsEnabled (item);
    }

    @KeywordName ("WaitUntilItemIsDisabled")
    public final boolean WaitUntilItem_IsDisabled (String itemName) {
        final Item item = findItem (itemName);
        return item != null && mBrowser.waitUntilItemIsDisabled (item);
    }

    @KeywordName ("CheckItemIsPresent")
    public final boolean CheckItem_IsPresent (String itemName) {
        final Item item = findItem (itemName);
        return item != null && mBrowser.itemIsPresent (item);
    }

    @KeywordName ("CheckItemIsNotPresent")
    public final boolean CheckItem_IsNotPresent (String itemName) {
        final Item item = findItem (itemName);
        return item != null && !mBrowser.itemIsPresent (item);
    }

    @KeywordName ("CheckItemIsVisible")
    public final boolean CheckItem_IsVisible (String itemName) {
        final Item item = findItem (itemName);
        return item != null && mBrowser.itemIsVisible (item);
    }

    @KeywordName ("CheckItemIsNotVisible")
    public final boolean CheckItem_IsNotVisible (String itemName) {
        final Item item = findItem (itemName);
        return item != null && !mBrowser.itemIsVisible (item);
    }

    @KeywordName ("CheckItemIsEnabled")
    public final boolean CheckItem_IsEnabled (String itemName) {
        final Item item = findItem (itemName);
        return item != null && mBrowser.itemIsEnabled (item);
    }

    @KeywordName ("CheckItemIsDisabled")
    public final boolean CheckItem_IsDisabled (String itemName) {
        final Item item = findItem (itemName);
        return item != null && !mBrowser.itemIsEnabled (item);
    }


    public final boolean CheckThat_Items_ArePresent (String itemName, int count) {
        final Item item = findItem (itemName);
        return item != null && mBrowser.getCount (item) >= count;
    }

    public final boolean CheckAtLeast_Items_ArePresent (int expectedNrOfItems, String itemName) {
        final Item item = findItem (itemName);
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

    public final void MakeScreenshot () {
        String filename = "screenshot" + ++mLastScreenshotNr + ".png";
        String path = mRunTime.getContext ().getResultsDirectory () + filename;
        if (mBrowser != null && mBrowser.makeScreenshot (path)) {
            mRunTime.reportLink (filename);
        }
    }

    
    @KeywordName ("SaveItemText")
    public final boolean SaveItem_In_ (String itemName, String variableName) {
        final Item item = findItem (itemName);
        if (item != null) {
            mRunTime.setValue (variableName, mBrowser.getItemText (item));
            return true;
        } else {
            return false;
        }
    }

    @KeywordName ("StoreItemAttribute")
    public final boolean PutAttribute_ForItem_In_ (String attributeName, String itemName, String variableName) {
        final Item item = findItem (itemName);
        if (item != null) {
            mRunTime.setValue (variableName, mBrowser.getItemAttribute (item, attributeName));
            return true;
        } else {
            return false;
        }
    }


    // protected members

    // names for item types
    protected interface ItemTypeName {
        String cButton           = "button";
        String cCheckbox         = "checkbox";
        String cCombobox         = "combobox";
        String cFrame            = "frame";
        String cImage            = "image";
        String cLink             = "link";
        String cListbox          = "listbox";
        String cListboxItem      = "listbox item";
        String cRadioButtonGroup = "radio button group";
        String cRadioButton      = "radio button";
        String cText             = "text input";
    }

    // names for key types
    protected interface KeyTypeName {
        String cClass        = "class";
        String cCss          = "css";
        String cDom          = "dom";
        String cId           = "id";
        String cIndex        = "index";
        String cName         = "name";
        String cPartialText  = "partial text";
        String cTag          = "tag";
        String cText         = "text";
        String cValue        = "value";
        String cXpath        = "xpath";
    }

    // names for browsers
    protected interface BrowserName {
        String cFirefox          = "firefox";
        String cIe               = "ie";
        String cInternetExplorer = "internet explorer";
        String cChrome           = "chrome";
    }

    protected final RunTime mRunTime;

    protected int mLastScreenshotNr;


    protected WebLibrary (RunTime runTime) {
        mRunTime          = runTime;
        mLastScreenshotNr = 0;
        mItemMap          = new HashMap<String, Item> ();
        //mBrowserMap     = new HashMap<String, IBrowser> ();
    }


    protected final String completeUrl (String url) {
        return url.startsWith ("http") ? url : "http:" + url;
    }

    protected Item findItem (String name) {
        Item item = getItem (name);
        if (item == null) {
            mRunTime.reportError ("no item declared named '" + name + "'");
        }
        return item;
    }


    // private members
    private static final Map<String, ItemType> cItemTypesMap;
    private static final Map<String, BrowserType> cBrowserTypesMap;
    private static final Map<String, KeyType> cKeyTypesMap;

    private final Map<String, Item> mItemMap;


    private boolean makeBoolean (String text) {
        if ("true".equalsIgnoreCase (text) || "on".equalsIgnoreCase (text)) {
            return true;
        } else if ("false".equalsIgnoreCase (text) || "off".equalsIgnoreCase (text)) {
            return false;
        }
        throw new IllegalArgumentException ("Invalid boolean value: " + text + " valid values: true/false/on/off");
    }

    //private final Map<String, IBrowser> mBrowserMap;

    static {
        cItemTypesMap = new HashMap<String, ItemType> ();
        cItemTypesMap.put (ItemTypeName.cButton,      ItemType.cButton);
        cItemTypesMap.put (ItemTypeName.cCheckbox,    ItemType.cCheckbox);
        cItemTypesMap.put (ItemTypeName.cCombobox,    ItemType.cCombobox);
        cItemTypesMap.put (ItemTypeName.cFrame,       ItemType.cFrame);
        cItemTypesMap.put (ItemTypeName.cImage,       ItemType.cImage);
        cItemTypesMap.put (ItemTypeName.cLink,        ItemType.cLink);
        cItemTypesMap.put (ItemTypeName.cListbox,     ItemType.cListbox);
        cItemTypesMap.put (ItemTypeName.cListboxItem, ItemType.cListboxItem);
        cItemTypesMap.put (ItemTypeName.cRadioButton, ItemType.cRadioButton);
        cItemTypesMap.put (ItemTypeName.cText,        ItemType.cText);

        cBrowserTypesMap = new HashMap<String, BrowserType> ();
        cBrowserTypesMap.put (BrowserName.cFirefox,          BrowserType.cFirefox);
        cBrowserTypesMap.put (BrowserName.cIe,               BrowserType.cInternetExplorer);
        cBrowserTypesMap.put (BrowserName.cInternetExplorer, BrowserType.cInternetExplorer);
        cBrowserTypesMap.put (BrowserName.cChrome,           BrowserType.cChrome);

        cKeyTypesMap = new HashMap<String, KeyType> ();
        cKeyTypesMap.put (KeyTypeName.cClass,       KeyType.cClass);
        cKeyTypesMap.put (KeyTypeName.cCss,         KeyType.cCss);
        cKeyTypesMap.put (KeyTypeName.cDom,         KeyType.cDom);
        cKeyTypesMap.put (KeyTypeName.cId,          KeyType.cId);
        cKeyTypesMap.put (KeyTypeName.cIndex,       KeyType.cIndex);
        cKeyTypesMap.put (KeyTypeName.cName,        KeyType.cName);
        cKeyTypesMap.put (KeyTypeName.cPartialText, KeyType.cPartialText);
        cKeyTypesMap.put (KeyTypeName.cText,        KeyType.cText);
        cKeyTypesMap.put (KeyTypeName.cValue,       KeyType.cValue);
        cKeyTypesMap.put (KeyTypeName.cXpath,       KeyType.cXpath);
        cKeyTypesMap.put (KeyTypeName.cTag,         KeyType.cTag);
    }
}