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

import org.powerTools.web.WebLibrary.IBrowserType;
import org.powerTools.web.WebLibrary.IKeyType;


public interface IBrowser {
	interface IMessages {
		String cNoSuchElementErrorMsg		= "no element found named ";
		String cMultipleElementsErrorMsg	= "multiple elements found named ";
	}
	
	boolean setDefaultTimeout (int timeout);
	int getDefaultTimeoutAsInteger ();
	String getDefaultTimeoutAsString ();
	
	boolean open (IBrowserType type, String url);
	boolean close ();
	boolean maximize ();
	boolean minimize ();
	
	boolean openUrl (String url);	
	String getUrl ();
	boolean refreshPage ();

	String getPageTitle ();	

	boolean selectFrame (String name);
	boolean selectFrame (IKeyType keyType, String value);
	boolean selectDefaultFrame ();
	
	boolean type (Item item, String text);
	boolean type (IKeyType keyType, String value, String text);

	boolean fireEvent (Item item, String event);

	boolean itemExists (Item item);
	boolean itemExists (IKeyType keyType, String value);
	int countItems (Item item);
	
	boolean click (Item item);
	boolean click (IKeyType keyType, String value);
	boolean clickAndWait (Item item);
	boolean clickAndWait (Item item, int timeout);
	boolean clickAndWait (IKeyType keyType, String value);
	boolean clickAndWait (IKeyType keyType, String value, int timeout);

	boolean clickLink (Item item);
	boolean clickLink (String text);
	boolean clickLink (IKeyType key, String value);

	boolean selectChoice (Item item);
	boolean selectChoice (Item item, String text);
	
	boolean mouseOver (Item item);
	
	boolean waitForText (String text);
	boolean waitForText (String text, int timeout);

	boolean waitForItem (Item item);
	boolean waitForItem (Item item, int timeout);

	boolean waitForItemFilled (Item item);
	boolean waitForItemFilled (Item item, int timeout);

	boolean checkForText (String text);
	String getItemText (Item item);
	String getItemText (IKeyType key, String value);

	Collection<HtmlRequest> getNetworkTraffic ();
	void clearNetworkTraffic ();

	int getCount(Item item);
}