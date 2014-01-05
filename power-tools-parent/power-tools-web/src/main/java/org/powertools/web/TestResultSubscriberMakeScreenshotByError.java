/* Copyright 2013 by Martin Gijsen (www.DeAnalist.nl)
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

import java.util.Date;

import org.powertools.engine.reports.TestResultSubscriber;


public class TestResultSubscriberMakeScreenshotByError implements TestResultSubscriber {
    private final WebLibrary webLibrary;

    public TestResultSubscriberMakeScreenshotByError(WebLibrary webLibrary) {
        this.webLibrary = webLibrary;
    }

    @Override
    public void start(Date dateTime) {
    }

    @Override
    public void finish(Date dateTime) {
    }

    @Override
    public void processStackTrace(String[] stackTraceLines) {
    }

    @Override
    public void processError(String message) {
        webLibrary.MakeScreenshot();
    }

    @Override
    public void processWarning(String message) {
    }

    @Override
    public void processInfo(String message) {
    }

    @Override
    public void processLink(String message) {
    }

    @Override
    public void processEndOfTestLine() {
    }

    @Override
    public void processIncreaseLevel() {
    }

    @Override
    public void processDecreaseLevel() {
    }
}
