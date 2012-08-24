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

import org.powerTools.engine.RunTime;


public final class WebDriverKeywords {
	private WebDriverLibrary mInstructions;
	
	public WebDriverKeywords (RunTime runTime) {
		mInstructions = new WebDriverLibrary (runTime);
		runTime.addSharedObject ("WebDriverKeywords", this);
	}
	
	
	public boolean OpenBrowser (String typeString, String url) {
		return mInstructions.OpenBrowser_At_ (typeString, url);
	}

	public boolean MaximizeBrowser () {
		return mInstructions.MaximizeBrowser ();
	}
	
	public boolean MinimizeBrowser () {
		return mInstructions.MinimizeBrowser ();
	}
	
	public boolean OpenURL (String url) {
		return mInstructions.OpenURL_ (url);
	}

	public boolean RefreshPage () {
		return mInstructions.RefreshPage ();
	}

	public boolean CloseBrowser () {
		return mInstructions.CloseBrowser ();
	}

	public boolean SetDefaultTimeout (int timeout) {
		return mInstructions.SetDefaultTimeoutTo_ (timeout);
	}

	public boolean DeclareItem (String name, String parentName, String type, String keyTypeString, String value) {
		return mInstructions.Name_ParentName_Type_Key_Value_ (name, parentName, type, keyTypeString, value);
	}

	public boolean SetItemParameter (String itemName, int parameterNr, String value) {
		return mInstructions.SetParameter_For_To_ (parameterNr, itemName, value);
	}

	public boolean SelectFrame (String name) {
		return mInstructions.SelectFrame (name);
	}

//	public boolean CheckPageTitleContains (String expectedTitle) {
//		return mInstructions.CheckPageTitleContains_ (expectedTitle);
//	}

	public boolean SelectDefaultFrame () {
		return mInstructions.SelectDefaultFrame ();
	}

	public boolean CheckPageTitleContains (String expectedTitle) {
		return mInstructions.CheckPageTitleContains_ (expectedTitle);
	}

	public boolean CheckPageTitle (String expectedTitle) {
		return mInstructions.CheckPageTitleIs_ (expectedTitle);
	}

	public boolean CheckForText (String text) {
		return mInstructions.CheckForText_ (text);
	}

	public boolean CheckItemTextContains (String itemName, String expectedText) {
		return mInstructions.CheckTextOf_Contains_ (itemName, expectedText);
	}
	
	public boolean CheckItemText (String itemName, String expectedText) {
		return mInstructions.CheckTextOf_Is_ (itemName, expectedText);
	}
	
	public boolean SelectLink (String itemName) {
		return mInstructions.SelectLink_ (itemName);
	}
	
	public boolean SelectLinkByText (String text) {
		return mInstructions.SelectLinkByText_ (text);
	}

	public boolean SelectLinkByKeyValue (String keyTypeString, String value) {
		return mInstructions.SelectLinkWhere_Is_ (keyTypeString, value);
	}
	
	public boolean SelectOption (String itemName) {
		return mInstructions.SelectOption_ (itemName);
	}

	public boolean SelectIn_OptionWithText (String selectItemName, String text) {
		return mInstructions.SelectOption_ (selectItemName, text);
	}

	public boolean ClickItem (String itemName) {
		return mInstructions.Click_ (itemName);
	}

	public boolean ClickItemByKeyValue (String keyTypeString, String value) {
		return mInstructions.ClickItemWhere_Is_ (keyTypeString, value);
	}
	
	public boolean ClickAndWait (String itemName) {
		return mInstructions.Click_AndWait (itemName);
	}

	public boolean ClickItemByKeyValueAndWait (String keyTypeString, String value) {
		return mInstructions.ClickItemWhere_Is_AndWait (keyTypeString, value);
	}
	
//	public boolean MoveMouseOver (String itemName) {
//		return mInstructions.MoveMouseOver_ (itemName);
//	}
	
	public boolean TypeIntoItem (String itemName, String text) {
		return mInstructions.Type_Into_ (text, itemName);
	}

	public boolean TypeByKeyValue (String text, String keyTypeString, String value) {
		return mInstructions.Type_IntoItemWhere_Is_ (text, keyTypeString, value);
	}

	public boolean WaitForText (String text) {
		return mInstructions.WaitForText_ (text);
	}

	public boolean WaitForItem (String itemName) {
		return mInstructions.WaitForItem_ (itemName);
	}
	
	public boolean WaitForItemFilled (String itemName) {
		return mInstructions.WaitForItem_Filled (itemName);
	}
	
	public boolean CheckItemIsPresent (String itemName) {
		return mInstructions.CheckItem_IsPresent (itemName);
	}
	
	public boolean CheckItemIsNotPresent (String itemName) {
		return mInstructions.CheckItem_IsNotPresent (itemName);
	}
	
//	public boolean ClearEvents () {
//		return mInstructions.ClearEvents ();
//	}
//
//	public boolean DeclareEvent (String eventName, String attributeName, String value) {
//		return mInstructions.Event_Attribute_Value_ (eventName, attributeName, value);
//	}
//	
//	public boolean SelectEvents () {
//		return mInstructions.SelectEvents ();
//	}
//
//	public boolean CheckForEvent (String eventName) {
//		return mInstructions.CheckForEvent_ (eventName);
//	}
}