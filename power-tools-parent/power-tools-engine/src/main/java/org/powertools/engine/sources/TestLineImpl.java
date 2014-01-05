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

import org.powertools.engine.TestLine;


public final class TestLineImpl implements TestLine {
    private int mNrOfParts;
    private String[] mParts;
    private String[] mOriginalParts;


    public TestLineImpl () {
        mNrOfParts = 0;
    }

    public TestLineImpl (String[] parts) {
        mParts         = parts.clone ();
        mOriginalParts = parts.clone ();
        mNrOfParts     = parts.length;
    }


    public void setParts (List<String> list) {
        mNrOfParts = list.size ();
        if (mNrOfParts != 0) {
            mParts         = new String[mNrOfParts];
            mOriginalParts = new String[mNrOfParts];

            int partNr = 0;
            for (String part : list) {
                setPart (partNr++, part);
            }
        }
    }

    public void createParts (int nrOfParts) {
        mNrOfParts     = nrOfParts;
        mParts         = new String[nrOfParts];
        mOriginalParts = new String[nrOfParts];
        for (int partNr = 0; partNr < nrOfParts; ++partNr) {
            mParts[partNr] = "";
        }
    }

    public void setPart (int partNr, String value) {
        mParts[partNr] = value;
    }

    public void setEvaluatedPart (int partNr, String value) {
        mOriginalParts[partNr] = mParts[partNr];
        mParts[partNr]         = value;
    }

    @Override
    public int getNrOfParts () {
        return mNrOfParts;
    }

    @Override
    public String getPart (int partNr) {
        return (partNr >= mNrOfParts) ? "" : mParts[partNr];
    }

    @Override
    public String getOriginalPart (int partNr) {
        return (partNr >= mNrOfParts) ? null : mOriginalParts[partNr];
    }

    @Override
    public boolean isEmpty () {
        for (int partNr = 0; partNr < mNrOfParts; ++partNr) {
            if (!mParts[partNr].isEmpty ()) {
                return false;
            }
        }
        return true;
    }
}
