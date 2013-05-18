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
import org.powerTools.engine.InstructionSet;

import org.openqa.selenium.WebDriver;


public final class WebDriverKeywords implements InstructionSet {
	private WebDriverLibrary mInstructions;
	
	public WebDriverKeywords (RunTime runTime) {
		mInstructions = new WebDriverLibrary (runTime);
		runTime.addSharedObject ("WebDriverKeywords", this);
	}
	
	
	@Override
	public String getName () {
		return mInstructions.getName ();
	}

	@Override
	public void setup () {
		mInstructions.setup ();
	}

	@Override
	public void cleanup () {
		mInstructions.cleanup ();
	}

	public Object getTesttool () {
		return mInstructions.getTesttool ();
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

	public boolean SetShortDefaultTimeout (int timeout) {
		return mInstructions.SetShortDefaultTimeoutTo_ (timeout);
	}

	public boolean SetLongDefaultTimeout (int timeout) {
		return mInstructions.SetLongDefaultTimeoutTo_ (timeout);
	}

	public boolean DeclareItem (String name, String parentName, String type, String keyTypeString, String value) {
		return mInstructions.Name_ParentName_Type_Key_Value_ (name, parentName, type, keyTypeString, value);
	}

	public boolean SetItemParameter (String itemName, int parameterNr, String value) {
		return mInstructions.SetParameter_ForItem_To_ (parameterNr, itemName, value);
	}

	public boolean SelectFrame (String name) {
		return mInstructions.SelectFrame_ (name);
	}

	public boolean SelectFrameByKeyValue (String key, String value) {
		return mInstructions.SelectFrameWhere_Is_ (key, value);
	}

	public boolean SelectDefaultFrame () {
		return mInstructions.SelectDefaultFrame ();
	}

	public boolean CheckPageTitle (String expectedTitle) {
		return mInstructions.CheckPageTitleIs_ (expectedTitle);
	}

	public boolean CheckPageTitleContains (String expectedTitle) {
		return mInstructions.CheckPageTitleContains_ (expectedTitle);
	}

	public boolean CheckForText (String text) {
		return mInstructions.CheckForText_ (text);
	}

	public boolean CheckItemIsEmpty (String itemName) {
		return mInstructions.CheckItem_IsEmpty (itemName);
	}
	
	public boolean CheckItemIsNotEmpty (String itemName) {
		return mInstructions.CheckItem_IsNotEmpty (itemName);
	}
	
	public boolean CheckItemText (String itemName, String expectedText) {
		return mInstructions.CheckTextOfItem_Is_ (itemName, expectedText);
	}
	
	public boolean CheckPartialItemText (String itemName, String expectedText) {
		return mInstructions.CheckTextOfItem_Contains_ (itemName, expectedText);
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

	public boolean SelectOptionByText (String selectItemName, String text) {
		return mInstructions.SelectOptionIn_ByText_ (selectItemName, text);
	}

	public boolean ClickItem (String itemName) {
		return mInstructions.ClickItem_ (itemName);
	}

	public boolean ClickItemByKeyValue (String keyTypeString, String value) {
		return mInstructions.ClickItemWhere_Is_ (keyTypeString, value);
	}
	
	public boolean ClickAndWait (String itemName) {
		return mInstructions.ClickItem_AndWait (itemName);
	}

	public boolean ClickItemByKeyValueAndWait (String keyTypeString, String value) {
		return mInstructions.ClickItemWhere_Is_AndWait (keyTypeString, value);
	}
	
//	public boolean MoveMouseOver (String itemName) {
//		return mInstructions.MoveMouseOver_ (itemName);
//	}
	
	public boolean TypeIntoItem (String itemName, String text) {
		return mInstructions.Type_IntoItem_ (text, itemName);
	}

	public boolean TypeIntoItemByKeyValue (String text, String keyTypeString, String value) {
		return mInstructions.Type_IntoItemWhere_Is_ (text, keyTypeString, value);
	}

	public boolean WaitForText (String text) {
		return WaitUntilTextIsPresent (text);
	}

	public boolean WaitUntilTextIsPresent (String text) {
		return mInstructions.WaitUntilText_IsPresent (text);
	}

	public boolean WaitUntilTextIsNotPresent (String text) {
		return mInstructions.WaitUntilText_IsNotPresent (text);
	}

	public boolean WaitForItem (String itemName) {
		return WaitUntilItemIsPresent (itemName);
	}
	
	public boolean WaitUntilItemIsPresent (String itemName) {
		return mInstructions.WaitUntilItem_IsPresent (itemName);
	}
	
	public boolean WaitUntilItemIsNotPresent (String itemName) {
		return mInstructions.WaitUntilItem_IsNotPresent (itemName);
	}
	
	public boolean WaitForItemFilled (String itemName) {
		return WaitUntilItemIsFilled (itemName);
	}
	
	public boolean WaitUntilItemIsFilled (String itemName) {
		return mInstructions.WaitUntilItem_IsFilled (itemName);
	}
	
	public boolean WaitUntilItemIsEmpty (String itemName) {
		return mInstructions.WaitUntilItem_IsEmpty (itemName);
	}
	
	public boolean WaitForItemVisible (String itemName) {
		return WaitUntilItemIsVisible (itemName);
	}
	
	public boolean WaitUntilItemIsVisible (String itemName) {
		return mInstructions.WaitUntilItem_IsVisible (itemName);
	}
	
	public boolean WaitUntilItemIsNotVisible (String itemName) {
		return mInstructions.WaitUntilItem_IsNotVisible (itemName);
	}
	
	public boolean WaitUntilItemIsEnabled (String itemName) {
		return mInstructions.WaitUntilItem_IsEnabled (itemName);
	}
	
	public boolean WaitUntilItemIsDisabled (String itemName) {
		return mInstructions.WaitUntilItem_IsDisabled (itemName);
	}
	
	public boolean CheckItemIsPresent (String itemName) {
		return mInstructions.CheckItem_IsPresent (itemName);
	}
	
	public boolean CheckItemIsNotPresent (String itemName) {
		return mInstructions.CheckItem_IsNotPresent (itemName);
	}
}