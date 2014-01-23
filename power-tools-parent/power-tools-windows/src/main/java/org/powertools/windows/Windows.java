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

package org.powerTools.windows;

import java.io.ByteArrayOutputStream;

import com.sun.jna.WString;


public class Windows {
    public static final WString LEFT_MOUSE_BUTTON      = new WString ("left");
    public static final WString MIDDLE_MOUSE_BUTTON    = new WString ("middle");
    public static final WString RIGHT_MOUSE_BUTTON     = new WString ("right");
    public static final WString PRIMARY_MOUSE_BUTTON   = new WString ("primary");
    public static final WString SECONDARY_MOUSE_BUTTON = new WString ("secondary");

    public static final int RAW         = 0;
    public static final int INTERPRETED = 1;

    public static final int HIDE        = 0;
    public static final int NORMAL      = 1;
    public static final int MINIMIZED   = 2;
    public static final int MAXIMIZED   = 3;


    private static final int MILLIS_PER_SECOND  = 1000;

    private static final int CLICK_ONCE  = 1;
    private static final int CLICK_TWICE = 2;


    private static final AutoItX mAutoIt = AutoItX.autoItX64;


    public static boolean clickControl (String windowTitle, String windowText, WString id, WString button) {
        return mAutoIt.AU3_ControlClick (new WString (windowTitle), new WString (windowText), id, button, CLICK_ONCE, -1, -1) == 1;
    }

    public static boolean doubleClickControl (String windowTitle, String windowText, WString id, WString button) {
        return mAutoIt.AU3_ControlClick (new WString (windowTitle), new WString (windowText), id, button, CLICK_TWICE, -1, -1) == 1;
    }

    public static String getControlText (String windowTitle, String windowText, WString id) {
        byte[] value = new byte[32];
        mAutoIt.AU3_ControlGetText (new WString (windowTitle), new WString (windowText), id, value, value.length);
        return byteArrayToString (value);
    }

    public static boolean sendTextToControl (String windowTitle, String windowText, WString id, String text, int mode) {
        return mAutoIt.AU3_ControlSend (new WString (windowTitle), new WString (windowText), id, new WString (text), mode) == 1;
    }

    public static boolean setControlText (String windowTitle, String windowText, WString id, String text) {
        return mAutoIt.AU3_ControlSetText (new WString (windowTitle), new WString (windowText), id, new WString (text)) == 1;
    }

    public static void sendText (String text, int mode) {
        mAutoIt.AU3_Send (new WString (text), mode);
    }

    public static boolean run (String application, String workingDirectory, int mode) {
        int pid = mAutoIt.AU3_Run (new WString (application), new WString (workingDirectory), mode);
        return pid != 0;
    }

    public static boolean closeWindow (String windowTitle, String windowText) {
        return mAutoIt.AU3_WinClose (new WString (windowTitle), new WString (windowText)) == 1;
    }

    public static int waitForWindow (String windowTitle, String windowText, int timeout) {
        return mAutoIt.AU3_WinWait (new WString (windowTitle), new WString (windowText), timeout);
    }

    public static boolean waitForWindowActive (String windowTitle, String windowText, int timeout) {
        return mAutoIt.AU3_WinWaitActive (new WString (windowTitle), new WString (windowText), timeout * MILLIS_PER_SECOND) != 0;
    }

    public static boolean waitForWindowToClose (String windowTitle, String windowText, int timeout) {
        return mAutoIt.AU3_WinWaitClose (new WString (windowTitle), new WString (windowText), timeout) == 1;
    }


    private static String byteArrayToString (byte[] aByteArray) {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream ();
        int byteCounter = 0;
        while (byteCounter < aByteArray.length && aByteArray[byteCounter] != 0) {
            baos.write (aByteArray[byteCounter++]);
        }
        return baos.toString ();
    }
}
