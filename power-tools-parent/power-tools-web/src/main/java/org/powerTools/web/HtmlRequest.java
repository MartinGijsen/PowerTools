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

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;


public final class HtmlRequest {
	class Header {
		String name;
		String value;

		Header (String name, String value) {
			this.name  = name;
			this.value = value;
		}
	}

	int statusCode;
	String method;
	String url;
	int bytes;
	String start;
	String end;
	int timeInMillis;
	List<Header> requestHeaders;
	
	
	Map<String, String> getParameters () {
		try {
			final String query	= new URL (this.url).getQuery ();
			if (query != null) {
				final Map<String, String> parameters = new HashMap<String, String> (); 
				final StringTokenizer pairs          = new StringTokenizer (query, "&");
				while (pairs.hasMoreTokens ()) {
					final StringTokenizer parts = new StringTokenizer (pairs.nextToken (), "=");
					final String name			= URLDecoder.decode (parts.nextToken (), "UTF-8");
					if (parts.hasMoreTokens ()) {
						//final String value = URLDecoder.decode (parts.nextToken (), "UTF-8");
						//System.out.println (name + "=" + value);
						//parameters.put (name, value);
						parameters.put (name, URLDecoder.decode (parts.nextToken (), "UTF-8"));
					} else {
						//System.out.println (name);
						parameters.put (name, null);
					}
				}
				//System.out.println ();
				return parameters;
			}
		} catch (MalformedURLException mue) {
			;
		} catch (UnsupportedEncodingException uee) {
			;
		}
		return null;
	}
}