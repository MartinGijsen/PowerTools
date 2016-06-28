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

import org.powertools.web.WebLibrary.ItemType;


final class XpathItem extends Item {
    private static final String cParameterMarker = "%";

    private final String[] mSections;
    private final String[] mParameters;


    XpathItem (String logicalName, ItemType type, String value) {
        this (logicalName, null, type, value);
    }

    XpathItem (String logicalName, Item parent, ItemType type, String xpath) {
        super (logicalName, parent, type, WebLibrary.KeyType.cXpath, xpath);
        mSections = xpath.split (cParameterMarker);
        mParameters = new String[mSections.length - 1];
    }


    @Override
    boolean resetParameter (int paramNr) {
        mParameters[paramNr] = null;
        return true;
    }

    @Override
    boolean resetParameters () {
        for (int paramNr = 0; paramNr < mParameters.length; ++paramNr) {
            mParameters[paramNr] = null;
        }
        return true;
    }

    @Override
    public boolean setParameterValue (int position, String value) {
        if (mParameters == null || position <= 0 || position > mParameters.length) {
            return false;
        } else {
            mParameters[position - 1] = value;
            return true;
        }
    }

    @Override
    public String getValue () {
        String parentXpath = "";
        if (mParent == null) {
            // TODO: check parent exists and has Xpath at creation
        } else if (mParent.mKeyType == WebLibrary.KeyType.cXpath) {
            parentXpath = mParent.getValue ();
        } else {
            return null;
        }

        final String xpath = getProcessedXpath();
        if (parentXpath == null || xpath == null) {
            return null;
        } else {
            return parentXpath + xpath;
        }
    }

    private String getProcessedXpath () {
        if (mParameters == null) {
            return mValue;
        } else {
            final StringBuilder sb = new StringBuilder ();
            for (int paramNr = 0; paramNr < mParameters.length; ++paramNr) {
                if (mParameters[paramNr] == null) {
                    return null;
                } else {
                    sb.append (mSections[paramNr]).append (mParameters[paramNr]);
                }
            }
            sb.append (mSections[mSections.length - 1]);
            return sb.toString ();
        }
    }
}
