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

import org.powertools.web.WebLibrary.BrowserType;
import org.powertools.web.WebLibrary.KeyType;


public interface IBrowser {
    boolean setShortDefaultTimeout (int timeout);
    boolean setLongDefaultTimeout (int timeout);
    int getShortDefaultTimeoutAsInteger ();
    int getLongDefaultTimeoutAsInteger ();
    String getShortDefaultTimeoutAsString ();
    String getLongDefaultTimeoutAsString ();

    boolean open (BrowserType type, String url, String logDirectory);
    boolean close ();
    boolean maximize ();
    boolean minimize ();

    boolean openUrl (String url);
    String getUrl ();
    boolean refreshPage ();

    String getPageTitle ();

    boolean selectFrame (Item item);
    boolean selectFrame (KeyType keyType, String value);
    boolean selectDefaultFrame ();

    boolean type (Item item, String text);
    boolean type (KeyType keyType, String value, String text);

    boolean fireEvent (Item item, String event);

    boolean setCheckboxValue(Item item, boolean value);
    boolean setCheckboxValue(KeyType keyType, String value, boolean checkValue);

    boolean itemExists (Item item);
    boolean itemExists (KeyType keyType, String value);
    int countItems (Item item);

    boolean click (Item item);
    boolean click (KeyType keyType, String value);
    boolean clickAndWait (Item item);
    boolean clickAndWait (Item item, int timeout);
    boolean clickAndWait (KeyType keyType, String value);
    boolean clickAndWait (KeyType keyType, String value, int timeout);

    boolean clickLink (Item item);
    boolean clickLink (String text);
    boolean clickLink (KeyType key, String value);

    boolean clickAcceptInAlert();

    boolean selectChoice (Item item);
    boolean selectChoiceByText (Item item, String text);
    boolean selectChoiceByPartialText (Item item, String text);

    boolean mouseOver (Item item);

    boolean waitUntilTextIsPresent (String text);
    boolean waitUntilTextIsPresent (String text, int timeout);
    boolean waitUntilTextIsNotPresent (String text);
    boolean waitUntilTextIsNotPresent (String text, int timeout);

    boolean waitUntilItemIsPresent (Item item);
    boolean waitUntilItemIsPresent (Item item, int timeout);
    boolean waitUntilItemIsNotPresent (Item item);
    boolean waitUntilItemIsNotPresent (Item item, int timeout);

    boolean waitUntilItemIsFilled (Item item);
    boolean waitUntilItemIsFilled (Item item, int timeout);
    boolean waitUntilItemIsEmpty (Item item);
    boolean waitUntilItemIsEmpty (Item item, int timeout);

    boolean waitUntilItemIsVisible (Item item);
    boolean waitUntilItemIsVisible (Item item, int timeout);
    boolean waitUntilItemIsNotVisible (Item item);
    boolean waitUntilItemIsNotVisible (Item item, int timeout);

    boolean waitUntilItemIsEnabled (Item item);
    boolean waitUntilItemIsEnabled (Item item, int timeout);
    boolean waitUntilItemIsDisabled (Item item);
    boolean waitUntilItemIsDisabled (Item item, int timeout);

    boolean checkForText (String text);
    String getItemText (Item item);
    String getItemText (KeyType key, String value);
    String getItemAttribute (Item item, String attributeName);
    String getItemAttribute (KeyType key, String value, String attributeName);
            
    int getCount (Item item);

    boolean makeScreenshot (String path);

    void cleanup ();

    @Deprecated
    Object getTestTool ();
}
