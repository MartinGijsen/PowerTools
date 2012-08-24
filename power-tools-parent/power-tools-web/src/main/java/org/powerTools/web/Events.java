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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.powerTools.engine.RunTime;


final class Events {
	Events (final RunTime runTime) {
		mRunTime = runTime;

		mEvents = new HashMap<String, Map<String, String>> ();
	}


	boolean addAttribute (final String eventName, final String attributeName, final String value) {
		Map<String, String> event = mEvents.get (eventName);
		if (event == null && mEventNames.contains (eventName)) {
			event = new HashMap<String, String> ();
			mEvents.put (eventName, event);
		}
		
		if (event == null) {
			mRunTime.reportError ("unsupported event");
		} else if (!mAttributesMap.containsKey (attributeName)) {
			mRunTime.reportError ("unsupported attribute");
		} else if (event.containsKey (attributeName)) {
			mRunTime.reportError ("attribute already has a value");
		} else {
			event.put (attributeName, value);
			return true;
		}
		return false;
	}

	boolean checkEvent (final Collection<HtmlRequest> requests, final String eventName) {
		final Map<String, String> event = mEvents.get (eventName);
		if (event == null) {
			mRunTime.reportError ("unknown event");
		} else if (event.isEmpty ()) {
			mRunTime.reportError ("event has no attributes");
		} else {
			return checkAttributes (requests, event);
		}
		return false;
	}


	// private members
	private final static Set<String> mEventNames			= new HashSet<String> ();
	private final static Map<String, String> mAttributesMap	= new HashMap<String, String> ();
	
	static {
		mAttributesMap.put ("prop9", "c9");
		mAttributesMap.put ("prop25", "c25");
		mAttributesMap.put ("prop26", "c26");
		mAttributesMap.put ("prop27", "c27");
		mAttributesMap.put ("prop41", "c41");
		mAttributesMap.put ("eVar1", "v1");
		mAttributesMap.put ("eVar2", "v2");
		mAttributesMap.put ("eVar3", "v3");
		mAttributesMap.put ("eVar9", "v9");
		mAttributesMap.put ("eVar13", "v13");
		mAttributesMap.put ("eVar14", "v14");
		mAttributesMap.put ("eVar25", "v25");
		mAttributesMap.put ("eVar26", "v26");
		mAttributesMap.put ("eVar27", "v27");
		mAttributesMap.put ("pev2", "pev2");
		mAttributesMap.put ("pageName", "pageName");
		mAttributesMap.put ("ch", "ch");
		mAttributesMap.put ("events", "events");

		mEventNames.add ("home page");
		mEventNames.add ("search first click");
		mEventNames.add ("search second click");
		mEventNames.add ("refreshed search second click");
		mEventNames.add ("KvK");
		mEventNames.add ("website");
		mEventNames.add ("my locations");
		mEventNames.add ("Hyves");
		mEventNames.add ("Facebook");
		mEventNames.add ("Outlook");
		mEventNames.add ("TomTom");
		mEventNames.add ("mobile phone");
		mEventNames.add ("email");
		mEventNames.add ("360 degrees photo");
		mEventNames.add ("video tour");
		mEventNames.add ("map");
		mEventNames.add ("directions");
		mEventNames.add ("photo tour");
		mEventNames.add ("profile");
		mEventNames.add ("profile website");
		mEventNames.add ("profile my locations");
		mEventNames.add ("profile TomTom");
		mEventNames.add ("profile Outlook");
		mEventNames.add ("profile email");
		mEventNames.add ("profile Facebook");
		mEventNames.add ("profile Hyves");
		mEventNames.add ("profile directions");
		mEventNames.add ("profile mobile phone");
	}


	private final RunTime mRunTime;
	private final Map<String, Map<String, String>> mEvents;


	private boolean checkAttributes (final Collection<HtmlRequest> requests, final Map<String, String> expectedAttributes) {
		final Set<String> attributeNames = expectedAttributes.keySet ();
		mRunTime.reportInfo ("looking for: " + getParameters (expectedAttributes));
		if (requests != null) {
			for (HtmlRequest request : requests) {
				final Map<String, String> attributes = request.getParameters (); 
				mRunTime.reportInfo ("checking request: " + getParameters (attributeNames, attributes));
				if (attributes != null) {
					boolean isOk = true;
					for (String name : expectedAttributes.keySet ()) {
						if (!attributeHasValue (attributes, name, expectedAttributes.get (name))) {
							isOk = false;
							break;
						}
					}
					
					if (isOk) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private boolean attributeHasValue (final Map<String, String> attributes, final String attributeName, final String expectedValue) {
		if (!attributes.containsKey (mAttributesMap.get (attributeName))) {
			mRunTime.reportInfo ("attribute " + attributeName + " is not present");
			return false;
		} else {
			final String actualValue = attributes.get (mAttributesMap.get (attributeName));
			if (expectedValue == null) {
				if (actualValue == null) {
//					mRunTime.reportInfo ("attribute " + attributeName + " = <empty>");
					return true;
				} else {
					mRunTime.reportInfo ("attribute " + attributeName + " = " + actualValue + " (expected: <empty>)");
					return false;
				}
			} else {
				if (expectedValue.equals (actualValue)) {
//					mRunTime.reportInfo ("attribute " + attributeName + " = " + actualValue);
					return true;
				} else {
					mRunTime.reportInfo ("attribute " + attributeName + " = " + actualValue + " (expected: " + expectedValue + ")");
					return false;
				}
			}
		}
	}

	private String getParameters (final Map<String, String> event) {
		final StringBuffer sb = new StringBuffer ();
		for (String attribute : event.keySet ()) {
			sb.append (attribute).append ("='").append (event.get (attribute)).append ("' ");
		}
		return sb.toString ();
	}

	private String getParameters (final Set<String> attributeNames, final Map<String, String> parameters) {
		final StringBuffer sb = new StringBuffer ();
		for (String attribute : attributeNames) {
			sb.append (attribute).append ("='").append (parameters.get (mAttributesMap.get (attribute))).append ("' ");
		}
		return sb.toString ();
	}
}