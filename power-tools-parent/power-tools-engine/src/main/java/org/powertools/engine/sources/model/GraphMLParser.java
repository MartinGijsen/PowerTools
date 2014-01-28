/* Copyright 2013-2014 by Martin Gijsen (www.DeAnalist.nl)
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

package org.powertools.engine.sources.model;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import org.powertools.engine.ExecutionException;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;


final class GraphMLParser extends DefaultHandler {
    private static final String NODE_KEY_TYPE               = "node";
    private static final String EDGE_KEY_TYPE               = "edge";

    private static final String CONDITION_KEY_NAME          = "Condition";
    private static final String ACTION_KEY_NAME             = "Action";
    private static final String DESCRIPTION_KEY_NAME        = "description";

    private static final String KEY_NAME_ATTRIBUTE_NAME     = "attr.name";
    private static final String KEY_DOMAIN_ATTRIBUTE_NAME   = "for";
    private static final String KEY_ID_ATTRIBUTE_NAME       = "id";

    // node
    private static final String NODE_NAME_ATTRIBUTE_NAME    = "id";

    // edge
    private static final String EDGE_SOURCE_ATTRIBUTE_NAME  = "source";
    private static final String EDGE_TARGET_ATTRIBUTE_NAME  = "target";
//  private static final String NODE_DESCRIPTION_KEY_NAME   = "d5";
//  private static final String EDGE_CONDITION_KEY_NAME     = "d8";
//  private static final String EDGE_ACTION_KEY_NAME        = "d9";

    private DirectedGraphImpl mGraph;
    private Stack<Element> mElementStack;

    private String mNodeDescriptionKeyId;
    private String mNodeActionKeyId;
    private String mEdgeDescriptionKeyId;
    private String mEdgeConditionKeyId;
    private String mEdgeActionKeyId;

    private Node mLastNode;
    private Edge mLastEdge;
    private String mKeyName;

    private static final Set<String> knownElements = new HashSet<String> ();
    static {
        knownElements.add ("graphml");
        knownElements.add ("graph");
        knownElements.add ("ShapeNode");
        knownElements.add ("Geometry");
        knownElements.add ("Fill");
        knownElements.add ("BorderStyle");
        knownElements.add ("SmartNodeLabelModel");
        knownElements.add ("ModeParameter");
        knownElements.add ("PolyLineEdge");
        knownElements.add ("Path");
        knownElements.add ("default");
        knownElements.add ("NodeLabel");
        knownElements.add ("EdgeLabel");
        knownElements.add ("Arrows");
        knownElements.add ("BendStyle");
        knownElements.add ("Resources");
        knownElements.add ("PreferredPlacementDescriptor");
        knownElements.add ("ModelParameter");
        knownElements.add ("LineStyle");
        knownElements.add ("SmartNodeLabelModelParameter");
        knownElements.add ("SmartEdgeLabelModelParameter");
        knownElements.add ("Shape");
        knownElements.add ("Point");
        knownElements.add ("SmartEdgeLabelModel");
        knownElements.add ("LabelModel");
    }

    private class Element {
        String mText;

        Element () {
            mText = "";
        }
    }


    GraphMLParser () {
        super ();
    }

    void parse (DirectedGraphImpl graph, String path, String fileName) throws IOException, SAXException {
        mGraph        = graph;
        mElementStack = new Stack<Element> ();

        XMLReader reader = XMLReaderFactory.createXMLReader ();
        reader.setContentHandler (this);
        String fullPath = path + "/" + fileName;
        if (!fullPath.endsWith (DirectedGraphImpl.FILE_EXTENSION)) {
            fullPath += DirectedGraphImpl.FILE_EXTENSION;
        }
        reader.parse (new InputSource (new FileReader (fullPath)));
    }


    @Override
    public void startElement (String uri, String name, String qName, Attributes attributes) {
        mElementStack.push (new Element ());
        if ("key".equals (name)) {
            handleKey (attributes);
        } else if ("node".equals (name)) {
            String nodeName = attributes.getValue (NODE_NAME_ATTRIBUTE_NAME);
            mLastNode       = mGraph.addNode (nodeName);
        } else if ("edge".equals (name)) {
            String sourceName = attributes.getValue (EDGE_SOURCE_ATTRIBUTE_NAME);
            String targetName = attributes.getValue (EDGE_TARGET_ATTRIBUTE_NAME);
            mLastEdge         = mGraph.addEdge (sourceName, targetName);
        } else if ("data".equals (name)) {
            mKeyName = attributes.getValue ("key");
        } else if (!knownElements.contains (name)) {
            throw new ExecutionException ("unsupported element: " + qName);
        }
    }

    private void handleKey (Attributes attributes) {
        String keyName = attributes.getValue (KEY_NAME_ATTRIBUTE_NAME);
        String keyType = attributes.getValue (KEY_DOMAIN_ATTRIBUTE_NAME);
        String keyId   = attributes.getValue (KEY_ID_ATTRIBUTE_NAME);
        if (keyName == null) {
            // ignore
        } else if (keyType.equals (NODE_KEY_TYPE)) {
            handleNodeKey (keyName, keyId);
        } else if (keyType.equals (EDGE_KEY_TYPE)) {
            handleEdgeKey (keyName, keyId);
        }
    }

    private void handleNodeKey (String keyName, String keyId) {
        if (keyName.equals (DESCRIPTION_KEY_NAME)) {
            mNodeDescriptionKeyId = keyId;
        } else if (keyName.equalsIgnoreCase (ACTION_KEY_NAME)) {
            mNodeActionKeyId = keyId;
        } else {
            // error
        }
    }

    private void handleEdgeKey (String keyName, String keyId) {
        if (keyName.equals (DESCRIPTION_KEY_NAME)) {
            mEdgeDescriptionKeyId = keyId;
        } else if (keyName.equalsIgnoreCase (ACTION_KEY_NAME)) {
            mEdgeActionKeyId = keyId;
        } else if (keyName.equalsIgnoreCase (CONDITION_KEY_NAME)) {
            mEdgeConditionKeyId = keyId;
        } else {
            // error
        }
    }

    @Override
    public void characters (char[] chars, int start, int length) {
        mElementStack.peek ().mText += new String (chars, start, length);
    }

    @Override
    public void endElement (String uri, String name, String qName) {
        String text = mElementStack.peek ().mText.trim ();
        if ("data".equals (name)) {
            if (mNodeActionKeyId != null && mNodeActionKeyId.equals (mKeyName)) {
                mLastNode.mAction = text;
            } else if (mEdgeConditionKeyId != null && mEdgeConditionKeyId.equals (mKeyName)) {
                mLastEdge.mCondition = text;
            } else if (mEdgeActionKeyId != null && mEdgeActionKeyId.equals (mKeyName)) {
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
