/* Copyright 2012-2014 by Martin Gijsen (www.DeAnalist.nl)
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

package org.powertools.engine.fitnesse;

import java.util.Date;

import org.powertools.engine.fitnesse.sources.FitNesseTestSource;
import org.powertools.engine.TestResultSubscriber;


class FitNesseReporter implements TestResultSubscriber {
    protected FitNesseTestSource mSource;
    protected int mLevel;
    protected boolean mAnyErrors;


    FitNesseReporter () {
        mLevel     = 0;
        mAnyErrors = false;
    }


    public final void setSource (FitNesseTestSource source) {
        mSource = source;
    }


    @Override
    public void processStackTrace (String[] stackTraceLines) {
        mAnyErrors = true;
    }

    @Override
    public void processError (String error) {
        mAnyErrors = true;
    }

    @Override
    public void processEndOfTestLine() {
        if (mLevel == 0) {
            mSource.processFinished (mAnyErrors);
            mAnyErrors = false;
        }
    }


    @Override
    public final void processIncreaseLevel () {
        ++mLevel;
    }

    @Override
    public void processDecreaseLevel () {
        if (--mLevel == 0) {
            mSource.processFinished (mAnyErrors);
            mAnyErrors = false;
        }
    }

    // ignored events
    @Override
    public final void start (Date dateTime) {
        // ignored
    }
    @Override
    public final void finish (Date dateTime) {
        // ignored
    }

    @Override
    public final void processInfo (String message) {
        // ignored
    }

    @Override
    public final void processLink (String url) {
        // ignored
    }

    @Override
    public final void processWarning (String warning) {
        // ignored
    }
}
