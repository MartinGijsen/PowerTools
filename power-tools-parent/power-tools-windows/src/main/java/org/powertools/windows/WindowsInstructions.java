/* Copyright 2014 by Martin Gijsen (www.DeAnalist.nl)
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

package org.powertools.windows;

import java.util.HashMap;
import java.util.Map;

import org.powertools.engine.ExecutionException;
import org.powertools.engine.KeywordName;
import org.powertools.engine.RunTime;


public final class WindowsInstructions {
    private final RunTime mRunTime;
    private final Map<String, Application> mApplications;
    private final Map<String, Window> mWindows;
    private final Map<String, Control> mControls;
    private final Windows mWinApi;


    public WindowsInstructions (RunTime runTime) {
        mRunTime      = runTime;
        mApplications = new HashMap<String, Application> ();
        mWindows      = new HashMap<String, Window> ();
        mControls     = new HashMap<String, Control> ();
        mWinApi       = new Windows ();
    }


    @KeywordName ("DeclareApplication")
    public boolean DeclareApplication_As_Title_Text_ (String name, String command, String windowName) {
        if (mApplications.containsKey (name)) {
            throw new ExecutionException (String.format ("application '%s' has already been defined", name));
        } else {
            mApplications.put (name, new Application (name, command, windowName));
            return true;
        }
    }

    public boolean DeclareWindow (String name, String windowTitle, String windowText) {
        if (mWindows.containsKey (name)) {
            throw new ExecutionException (String.format ("window '%s' has already been defined", name));
        } else {
            mWindows.put (name, new Window (name, windowTitle, windowText));
            return true;
        }
    }

    @KeywordName ("StartApplication")
    public boolean StartApplication_Within_ (String name, int timeout) {
        final Application application = getApplication (name);
        final Window window           = getWindow (application.mWindowName);
        if (!mWinApi.run (application.mCommand, "", Windows.NORMAL)) {
            throw new ExecutionException (String.format ("could not start '%s'", application.mCommand));
        } else if (!mWinApi.waitForWindowActive (window.mTitle, window.mText, timeout)) {
            throw new ExecutionException ("window was not active in time");
        } else {
            return true;
        }
    }

    public boolean CloseWindow (String name, int timeout) {
        final Window window = getWindow (name);
        if (!mWinApi.closeWindow (window.mTitle, window.mText)) {
            throw new ExecutionException (String.format ("could not close '%s'", window.mName));
        } else if (!mWinApi.waitForWindowToClose (window.mTitle, window.mText, timeout)) {
            throw new ExecutionException ("window did not close in time");
        } else {
            return true;
        }
    }

    public boolean DeclareControl (String name, String windowName, int id) {
        if (mControls.containsKey (name)) {
            throw new ExecutionException (String.format ("control '%s' has already been defined", name));
        } else if (!mWindows.containsKey (windowName)) {
            throw new ExecutionException (String.format ("window '%s' is unknown", windowName));
        } else {
            mControls.put (name, new Control (name, mWindows.get (windowName), id));
            return true;
        }
    }

    public boolean ClickControl (String name) {
        final Control control = getControl (name);
        if (!mWinApi.clickControl (control.mWindow.mTitle, control.mWindow.mText, control.mId, Windows.PRIMARY_MOUSE_BUTTON)) {
            throw new ExecutionException (String.format ("could not click item '%s' (title: %s, text: %s, ID: %s)", control.mName, control.mWindow.mTitle, control.mWindow.mText, control.mId));
        } else {
            return true;
        }
    }

    public boolean CheckControlText (String name, String expectedText) {
        Control control   = getControl (name);
        String actualText = mWinApi.getControlText (control.mWindow.mTitle, control.mWindow.mText, control.mId);
        if (actualText.equals (expectedText)) {
            return true;
        } else {
            mRunTime.reportValueError ("name", actualText, expectedText);
            return false;
        }
    }

    public boolean Type (String text) {
        mWinApi.sendText (text, Windows.RAW);
        return true;
    }

    private Application getApplication (String name) {
        if (mApplications.containsKey (name)) {
            return mApplications.get (name);
        } else {
            throw new ExecutionException (String.format ("application '%s' is unknown", name));
        }
    }

    private Window getWindow (String name) {
        if (mWindows.containsKey (name)) {
            return mWindows.get (name);
        } else {
            throw new ExecutionException (String.format ("window '%s' is unknown", name));
        }
    }

    private Control getControl (String name) {
        if (mControls.containsKey (name)) {
            return mControls.get (name);
        } else {
            throw new ExecutionException (String.format ("control '%s' is unknown", name));
        }
    }
}
