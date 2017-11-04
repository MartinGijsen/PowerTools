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

package org.powertools.engine.sources;

import java.util.List;
import java.util.Map;

import org.powertools.engine.TestLine;


public final class TestLineImpl implements TestLine {
    private static class Part {
        String              mOriginalValue;
        Map<String, String> mSymbolValues;
        String              mValue;
        
        Part (String value) {
            mOriginalValue = null;
            mValue         = value;
        }
    }
    
    private int    mNrOfParts;
    private Part[] mParts;


    public TestLineImpl () {
        mNrOfParts = 0;
    }

    public TestLineImpl (String[] parts) {
        mNrOfParts     = parts.length;
        mParts = new Part[mNrOfParts];
        for (int partNr = 0; partNr < mNrOfParts; ++partNr) {
            mParts[partNr] = new Part (parts[partNr]);
        }
    }


    public void setParts (List<String> list) {
        mNrOfParts = list.size ();
        if (mNrOfParts != 0) {
            mParts = new Part[mNrOfParts];
            int partNr = 0;
            for (String part : list) {
                mParts[partNr++] = new Part (part);
            }
        }
    }

    public void createParts (int nrOfParts) {
        mNrOfParts = nrOfParts;
        mParts     = new Part[nrOfParts];
        for (int partNr = 0; partNr < nrOfParts; ++partNr) {
            mParts[partNr] = new Part ("");
        }
    }

    public void setPart (int partNr, String value) {
        mParts[partNr].mOriginalValue = null;
        mParts[partNr].mValue         = value;
    }

    public void setEvaluatedPart (int partNr, Map<String, String> symbolValues, String value) {
        Part part           = mParts[partNr];
        part.mOriginalValue = part.mValue;
        part.mSymbolValues  = symbolValues;
        part.mValue         = value;
    }

    @Override
    public int getNrOfParts () {
        return mNrOfParts;
    }

    @Override
    public String getOriginalPart (int partNr) {
        return (partNr >= mNrOfParts) ? null : mParts[partNr].mOriginalValue;
    }

    @Override
    public Map<String, String> getSymbolValues (int partNr) {
        return (partNr >= mNrOfParts) ? null : mParts[partNr].mSymbolValues;
    }

    @Override
    public String getPart (int partNr) {
        return (partNr >= mNrOfParts) ? "" : mParts[partNr].mValue;
    }

    @Override
    public boolean isEmpty () {
        for (int partNr = 0; partNr < mNrOfParts; ++partNr) {
            if (!mParts[partNr].mValue.isEmpty ()) {
                return false;
            }
        }
        return true;
    }
}
