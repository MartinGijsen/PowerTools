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

import com.sun.jna.Native;
import com.sun.jna.WString;


interface AutoItX extends com.sun.jna.Library {
    String AUTO_IT_BIN_PATH = "C:\\Program Files (x86)\\AutoIt3\\AutoItX\\";
    String DLL_NAME_64_BIT  = "AutoItX3_x64.dll";
    String DLL_NAME_32_BIT  = "AutoItX3.dll";
    String DLL_NAME         = DLL_NAME_64_BIT;
    AutoItX INSTANCE        = (AutoItX) Native.loadLibrary (AUTO_IT_BIN_PATH + DLL_NAME, AutoItX.class);


    int AU3_INTDEFAULT = -2147483647; // "Default" value for _some_ int parameters (largest negative number)

    // Note: buffer size for results includes terminating null

    void AU3_Init ();
    int  AU3_error ();

    int  AU3_AutoItSetOption (WString option, int value);

    // clipboard
    void AU3_ClipGet (WString clip, int bufferSize);
    void AU3_ClipPut (WString clip);

    // controls
    int  AU3_ControlClick (WString title, WString text, WString control, WString button, int numClicks, int x, int y);
    void AU3_ControlCommand (WString title, WString text, WString control, WString command, WString extra, byte[] result, int bufferSize);
    void AU3_ControlListView (WString title, WString text, WString control, WString command, WString extra1, WString extra2, byte[] szResult, int bufferSize);
    int  AU3_ControlDisable (WString title, WString text, WString control);
    int  AU3_ControlEnable (WString title, WString text, WString control);
    int  AU3_ControlFocus (WString title, WString text, WString control);
    void AU3_ControlGetFocus (WString title, WString text, byte[] controlWithFocus, int bufferSize);
    void AU3_ControlGetHandleAsText (WString title, WString text, WString control, byte[] result, int bufferSize);
    void AU3_ControlGetText (WString title, WString text, WString control, byte[] controlText, int bufferSize);
    int  AU3_ControlHide (WString title, WString text, WString control);
    int  AU3_ControlMove (WString title, WString text, WString control, int x, int y, int nWidth, int nHeight);
    int  AU3_ControlSend (WString title, WString text, WString control, WString szSendText, int nMode);
    int  AU3_ControlSetText (WString title, WString text, WString control, WString controlText);
    int  AU3_ControlShow (WString title, WString text, WString control);
    void AU3_ControlTreeView (WString title, WString text, WString control, WString command, WString extra1, WString extra2, byte[] result, int bufferSize);

    // mapped network drives
    void AU3_DriveMapAdd (WString device, WString share, int flags, WString user, WString password, byte[] result, int bufferSize);
    int  AU3_DriveMapDel (WString device);
    void AU3_DriveMapGet (WString device, byte[] mapping, int bufferSize);

    int  AU3_IsAdmin ();

    // mouse
    int  AU3_MouseClick (WString button, int x, int y, int nClicks, int speed);
    int  AU3_MouseClickDrag (WString button, int x1, int y1, int x2, int y2, int speed);
    void AU3_MouseDown (WString button);
    int  AU3_MouseGetCursor ();
    int  AU3_MouseMove (int x, int y, int speed);
    void AU3_MouseUp (WString button);
    void AU3_MouseWheel (WString direction, int nClicks);

    int  AU3_Opt (WString option, int value);

    int  AU3_PixelGetColor (int x, int y);
    
    // processes
    int  AU3_ProcessClose (WString process);
    int  AU3_ProcessExists (WString process);
    int  AU3_ProcessSetPriority (WString process, int priority);
    int  AU3_ProcessWait (WString process, int timeout);
    int  AU3_ProcessWaitClose (WString process, int timeout);

    int  AU3_Run (WString program, WString dir, int showFlag);
    int  AU3_RunWait (WString program, WString dir, int showFlag);
    int  AU3_RunAs (WString user, WString domain, WString password, int logoflag, WString program, WString dir, int showFlag);
    int  AU3_RunAsWait (WString user, WString domain, WString password, int logoflag, WString program, WString dir, int showFlag);

    void AU3_Send (WString szSendText, int mode);
    int  AU3_Shutdown (int flags);
    void AU3_Sleep (int nrOfMillis);
    int  AU3_StatusbarGetText (WString title, WString text, int nPart, byte[] szStatusText, int bufferSize);

    void AU3_ToolTip (WString szTip, int x, int y);

    // windows
    int  AU3_WinActivate (WString title, WString text);
    int  AU3_WinActive (WString title, WString text);
    int  AU3_WinClose (WString title, WString text);
    int  AU3_WinExists (WString title, WString text);
    void AU3_WinGetClassList (WString title, WString text, byte[] result, int bufferSize);
    int  AU3_WinGetState (WString title, WString text);
    void AU3_WinGetText (WString title, WString text, byte[] result, int bufferSize);
    void AU3_WinGetTitle (WString title, WString text, byte[] result, int bufferSize);
    int  AU3_WinKill (WString title, WString text);
    int  AU3_WinMenuSelectItem (WString title, WString text, WString item1, WString item2, WString item3, WString item4, WString item5, WString item6, WString item7, WString item8);
    void AU3_WinMinimizeAll ();
    void AU3_WinMinimizeAllUndo ();
    int  AU3_WinMove (WString title, WString text, int x, int y, int width, int height);
    int  AU3_WinSetOnTop (WString title, WString text, int flag);
    int  AU3_WinSetState (WString title, WString text, int flags);
    int  AU3_WinSetTitle (WString title, WString text, WString newTitle);
    int  AU3_WinSetTrans (WString title, WString text, int trans);
    int  AU3_WinWait (WString title, WString text, int timeout);
    int  AU3_WinWaitActive (WString title, WString text, int timeout);
    int  AU3_WinWaitClose (WString title, WString text, int timeout);
    int  AU3_WinWaitNotActive (WString title, WString text, int timeout);

    /* methods using handles and less basic types
    int  AU3_ControlClickByHandle (HWND hWnd, HWND hCtrl, WString button, int nNumClicks, int x, int y);
    void AU3_ControlCommandByHandle(HWND hWnd, HWND hCtrl, WString command, WString extra, byte[] szResult, int bufferSize);
    void AU3_ControlListViewByHandle(HWND hWnd, HWND hCtrl, WString command, WString extra1, WString extra2, byte[] szResult, int bufferSize);
    int  AU3_ControlDisableByHandle(HWND hWnd, HWND hCtrl);
    int  AU3_ControlEnableByHandle(HWND hWnd, HWND hCtrl);
    int  AU3_ControlFocusByHandle(HWND hWnd, HWND hCtrl);
    void AU3_ControlGetFocusByHandle(HWND hWnd, byte[] controlWithFocus, int bufferSize);
    HWND AU3_ControlGetHandle(HWND hWnd, WString control);
    int  AU3_ControlGetPos (WString title, WString text, WString control, LPRECT lpRect);
    int  AU3_ControlGetPosByHandle(HWND hWnd, HWND hCtrl, LPRECT lpRect);
    void AU3_ControlGetTextByHandle(HWND hWnd, HWND hCtrl, byte[] controlText, int bufferSize);
    int  AU3_ControlHideByHandle(HWND hWnd, HWND hCtrl);
    int  AU3_ControlMoveByHandle(HWND hWnd, HWND hCtrl, int x, int y, int nWidth, int nHeight);
    int  AU3_ControlSendByHandle(HWND hWnd, HWND hCtrl, WString szSendText, int nMode);
    int  AU3_ControlSetTextByHandle(HWND hWnd, HWND hCtrl, WString controlText);
    int  AU3_ControlShowByHandle(HWND hWnd, HWND hCtrl);
    void AU3_ControlTreeViewByHandle(HWND hWnd, HWND hCtrl, WString command, WString extra1, WString extra2, byte[] szResult, int bufferSize);

    void AU3_MouseGetPos (LPPOINT lpPoint);

    int  AU3_PixelChecksum (LPRECT lpRect, int nStep);
    void AU3_PixelSearch (LPRECT lpRect, int nCol, int nVar, int nStep, LPPOINT pPointResult);

    int  AU3_StatusbarGetTextByHandle(HWND hWnd, int nPart, byte[] szStatusText, int bufferSize);

    int  AU3_WinActivateByHandle (HWND hWnd);
    int  AU3_WinActiveByHandle (HWND hWnd);
    int  AU3_WinCloseByHandle (HWND hWnd);
    int  AU3_WinExistsByHandle (HWND hWnd);
    int  AU3_WinGetCaretPos (LPPOINT lpPoint);
    void AU3_WinGetClassListByHandle (HWND hWnd, byte[] result, int bufferSize);
    int  AU3_WinGetClientSize (WString title, WString text, LPRECT lpRect);
    int  AU3_WinGetClientSizeByHandle (HWND hWnd, LPRECT lpRect);
    HWND AU3_WinGetHandle (WString title, WString text);
    void AU3_WinGetHandleAsText (WString title, WString text, byte[] result, int bufferSize);
    int  AU3_WinGetPos (WString title, WString text, LPRECT lpRect);
    int  AU3_WinGetPosByHandle (HWND hWnd, LPRECT lpRect);
    DWORD AU3_WinGetProcess (WString title, WString text);
    DWORD AU3_WinGetProcessByHandle (HWND hWnd);
    int  AU3_WinGetStateByHandle (HWND hWnd);
    void AU3_WinGetTextByHandle (HWND hWnd, byte[] result, int bufferSize);
    void AU3_WinGetTitleByHandle (HWND hWnd, byte[] result, int bufferSize);
    int  AU3_WinKillByHandle (HWND hWnd);
    int  AU3_WinMenuSelectItemByHandle (HWND hWnd, WString item1, WString item2, WString item3, WString item4, WString item5, WString item6, WString item7, WString item8);
    int  AU3_WinMoveByHandle (HWND hWnd, int x, int y, int nWidth, int nHeight);
    int  AU3_WinSetOnTopByHandle (HWND hWnd, int flag);
    int  AU3_WinSetStateByHandle (HWND hWnd, int flags);
    int  AU3_WinSetTitleByHandle (HWND hWnd, WString szNewTitle);
    int  AU3_WinSetTransByHandle (HWND hWnd, int nTrans);
    int  AU3_WinWaitByHandle (HWND hWnd, int timeout);
    int  AU3_WinWaitActiveByHandle (HWND hWnd, int timeout);
    int  AU3_WinWaitCloseByHandle (HWND hWnd, int timeout);
    int  AU3_WinWaitNotActiveByHandle (HWND hWnd, int timeout);
    */
}
