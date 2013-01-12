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

package org.powerTools.engine.sources.model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;

import java.util.Stack;

import org.xml.sax.XMLReader;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.SAXException;


final class GraphMLParser extends DefaultHandler {
	private final static String NODE_NAME_ATTRIBUTE_NAME	= "id";
	private final static String EDGE_SOURCE_ATTRIBUTE_NAME	= "source";
	private final static String EDGE_TARGET_ATTRIBUTE_NAME	= "target";
	private final static String NODE_DESCRIPTION_KEY_NAME	= "d5";
	private final static String EDGE_CONDITION_KEY_NAME		= "d8";
	private final static String EDGE_ACTION_KEY_NAME		= "d9";

	private final DirectedGraph mGraph;
	private final Stack<Element> mElementStack;

	private Node mLastNode;
	private Edge mLastEdge;
	private String mKeyName;


	private class Element {
		String mText;
		
		Element () {
			mText = "";
		}
	}
	

	GraphMLParser () throws SAXException {
		super ();
		mGraph			= new DirectedGraph ();
		mElementStack	= new Stack<Element> ();
	}

	DirectedGraph parse (String filename) throws FileNotFoundException, IOException, SAXException {
		final XMLReader reader = XMLReaderFactory.createXMLReader ();
		reader.setContentHandler (this);
		reader.parse (new InputSource (new FileReader (filename)));
		return mGraph;
	}


	@Override
	public void startElement (String uri, String name, String qName, Attributes attributes) {
		mElementStack.push (new Element ());
		if ("node".equals (name)) {
			String nodeName	= attributes.getValue (NODE_NAME_ATTRIBUTE_NAME);
			mLastNode		= mGraph.addNode (nodeName);
		} else if ("edge".equals (name)) {
			String sourceName	= attributes.getValue (EDGE_SOURCE_ATTRIBUTE_NAME);
			String targetName	= attributes.getValue (EDGE_TARGET_ATTRIBUTE_NAME);
			mLastEdge			= mGraph.addEdge (sourceName, targetName);
		} else if ("data".equals (name)) {
			mKeyName = attributes.getValue ("key");
		} else if ("graphml".equals (name)
				|| "graph".equals (name)
				|| "ShapeNode".equals (name)
				|| "Geometry".equals (name)
				|| "Fill".equals (name)
				|| "BorderStyle".equals (name)
				|| "SmartNodeLabelModel".equals (name)
				|| "ModeParameter".equals (name)
				|| "PolyLineEdge".equals (name)
				|| "Path".equals (name)
				|| "key".equals (name)
				|| "default".equals (name)
				|| "NodeLabel".equals (name)
				|| "EdgeLabel".equals (name)
				|| "Arrows".equals (name)
				|| "BendStyle".equals (name)
				|| "Resources".equals (name)
				|| "PreferredPlacementDescriptor".equals (name)
				|| "ModelParameter".equals (name)
				|| "LineStyle".equals (name)
				|| "SmartNodeLabelModelParameter".equals (name)
				|| "SmartEdgeLabelModelParameter".equals (name)
				|| "Shape".equals (name)
				|| "SmartEdgeLabelModel".equals (name)
				|| "LabelModel".equals (name)) {
			;
		} else {
			System.out.println ("Start element: " + qName);
		}
	}

	@Override
	public void characters (char chars[], int start, int length) {
		mElementStack.peek ().mText += new String (chars, start, length);
	}

	@Override
	public void endElement (String uri, String name, String qName) {
		final String text = mElementStack.peek ().mText.trim ();
		if ("data".equals (name)) {
			if (NODE_DESCRIPTION_KEY_NAME.equals (mKeyName)) {
				mLastNode.mAction = text;
			} else if (EDGE_CONDITION_KEY_NAME.equals (mKeyName)) {
				mLastEdge.mCondition = text;
			} else if (EDGE_ACTION_KEY_NAME.equals (mKeyName)) {
				mLastEdge.mAction = text;
			}
			mKeyName = null;
		} else if ("NodeLabel".equals (name)) {
			if (!"".equals (text)) {
				mLastNode.mLabel = text;
			}
		} else if ("EdgeLabel".equals (name)) {
			if (!"".equals (text)) {
				mLastEdge.mLabel = text;
			}
		}
		mElementStack.pop ();
	}
}