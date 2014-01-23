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

import com.sun.jna.Native;
import com.sun.jna.WString;


public interface AutoItX extends com.sun.jna.Library  {
    String DLL_NAME_32_BIT = "AutoItX3.dll";
    String DLL_NAME_64_BIT = "AutoItX3_x64.dll";
    AutoItX autoItX64 = (AutoItX) Native.loadLibrary ("C:\\Program Files (x86)\\AutoIt3\\AutoItX\\" + DLL_NAME_64_BIT, AutoItX.class);
//    AutoItX autoItX32 = (AutoItX) Native.loadLibrary ("C:\\Program Files (x86)\\AutoIt3\\AutoItX\\" + DLL_NAME_32_BIT, AutoItX.class);

    // Definitions
    final int AU3_INTDEFAULT = -2147483647;	// "Default" value for _some_ int parameters (largest negative number)

    // Note: buffer size for results includes terminating null

    public void AU3_Init ();
    public int  AU3_error ();

    public int  AU3_AutoItSetOption (WString szOption, int nValue);

    // clipboard
    public void AU3_ClipGet (WString szClip, int nBufSize);
    public void AU3_ClipPut (WString szClip);

    public int  AU3_ControlClick (WString szTitle, WString szText, WString szControl, WString szButton, int nNumClicks, int nX, int nY);
    public void AU3_ControlCommand (WString szTitle, WString szText, WString szControl, WString szCommand, WString szExtra, byte[] szResult, int nBufSize);
    public void AU3_ControlListView (WString szTitle, WString szText, WString szControl, WString szCommand, WString szExtra1, WString szExtra2, byte[] szResult, int nBufSize);
    public int  AU3_ControlDisable (WString szTitle, WString szText, WString szControl);
    public int  AU3_ControlEnable (WString szTitle, WString szText, WString szControl);
    public int  AU3_ControlFocus (WString szTitle, WString szText, WString szControl);
    public void AU3_ControlGetFocus (WString szTitle, WString szText, byte[] szControlWithFocus, int nBufSize);
    public void AU3_ControlGetHandleAsText (WString szTitle, WString szText, WString szControl, byte[] szRetText, int nBufSize);
    public void AU3_ControlGetText (WString szTitle, WString szText, WString szControl, byte[] szControlText, int nBufSize);
    public int  AU3_ControlHide (WString szTitle, WString szText, WString szControl);
    public int  AU3_ControlMove (WString szTitle, WString szText, WString szControl, int nX, int nY, int nWidth, int nHeight);
    public int  AU3_ControlSend (WString szTitle, WString szText, WString szControl, WString szSendText, int nMode);
    public int  AU3_ControlSetText (WString szTitle, WString szText, WString szControl, WString szControlText);
    public int  AU3_ControlShow (WString szTitle, WString szText, WString szControl);
    public void AU3_ControlTreeView (WString szTitle, WString szText, WString szControl, WString szCommand, WString szExtra1, WString szExtra2, byte[] szResult, int nBufSize);

    // mapped network drives
    public void AU3_DriveMapAdd (WString szDevice, WString szShare, int nFlags, /*[in,defaultvalue("")]*/WString szUser, /*[in,defaultvalue("")]*/WString szPwd, byte[] szResult, int nBufSize);
    public int  AU3_DriveMapDel (WString szDevice);
    public void AU3_DriveMapGet (WString szDevice, byte[] szMapping, int nBufSize);

    public int  AU3_IsAdmin ();

    // mouse
    public int  AU3_MouseClick (WString szButton, int nX, int nY, int nClicks, int nSpeed);
    public int  AU3_MouseClickDrag (WString szButton, int nX1, int nY1, int nX2, int nY2, int nSpeed);
    public void AU3_MouseDown (WString szButton);
    public int  AU3_MouseGetCursor ();
    public int  AU3_MouseMove (int nX, int nY, int nSpeed);
    public void AU3_MouseUp (WString szButton);
    public void AU3_MouseWheel (WString szDirection, int nClicks);

    public int  AU3_Opt (WString szOption, int nValue);

    public int  AU3_PixelGetColor (int nX, int nY);
    
    // processes
    public int  AU3_ProcessClose (WString szProcess);
    public int  AU3_ProcessExists (WString szProcess);
    public int  AU3_ProcessSetPriority (WString szProcess, int nPriority);
    public int  AU3_ProcessWait (WString szProcess, int nTimeout);
    public int  AU3_ProcessWaitClose (WString szProcess, int nTimeout);

    public int  AU3_Run (WString szProgram, WString szDir, int nShowFlag);
    public int  AU3_RunWait (WString szProgram, WString szDir, int nShowFlag);
    public int  AU3_RunAs (WString szUser, WString szDomain, WString szPassword, int nLogonFlag, WString szProgram, /*[in,defaultvalue("")]*/WString szDir, int nShowFlag);
    public int  AU3_RunAsWait (WString szUser, WString szDomain, WString szPassword, int nLogonFlag, WString szProgram, /*[in,defaultvalue("")]*/WString szDir, int nShowFlag);

    public void AU3_Send (WString szSendText, int nMode);
    public int  AU3_Shutdown (int nFlags);
    public void AU3_Sleep (int nMilliseconds);
    public int  AU3_StatusbarGetText (WString szTitle, WString szText, /*[in,defaultvalue(1)]*/int nPart, byte[] szStatusText, int nBufSize);

    public void AU3_ToolTip (WString szTip, int nX, int nY);

    public int  AU3_WinActivate (WString szTitle, WString szText);
    public int  AU3_WinActive (WString szTitle, WString szText);
    public int  AU3_WinClose (WString szTitle, WString szText);
    public int  AU3_WinExists (WString szTitle, WString szText);
    public void AU3_WinGetClassList (WString szTitle, WString szText, byte[] szRetText, int nBufSize);
    public int  AU3_WinGetState (WString szTitle, WString szText);
    public void AU3_WinGetText (WString szTitle, WString szText, byte[] szRetText, int nBufSize);
    public void AU3_WinGetTitle (WString szTitle, WString szText, byte[] szRetText, int nBufSize);
    public int  AU3_WinKill (WString szTitle, WString szText);
    public int  AU3_WinMenuSelectItem (WString szTitle, WString szText, WString szItem1, WString szItem2, WString szItem3, WString szItem4, WString szItem5, WString szItem6, WString szItem7, WString szItem8);
    public void AU3_WinMinimizeAll ();
    public void AU3_WinMinimizeAllUndo ();
    public int  AU3_WinMove (WString szTitle, WString szText, int nX, int nY, int nWidth, int nHeight);
    public int  AU3_WinSetOnTop (WString szTitle, WString szText, int nFlag);
    public int  AU3_WinSetState (WString szTitle, WString szText, int nFlags);
    public int  AU3_WinSetTitle (WString szTitle, WString szText, WString szNewTitle);
    public int  AU3_WinSetTrans (WString szTitle, WString szText, int nTrans);
    public int  AU3_WinWait (WString szTitle, WString szText, int nTimeout);
    public int  AU3_WinWaitActive (WString szTitle, WString szText, int nTimeout);
    public int  AU3_WinWaitClose (WString szTitle, WString szText, int nTimeout);
    public int  AU3_WinWaitNotActive (WString szTitle, WString szText, int nTimeout);

    /* methods using handles and less basic types
    public int  AU3_ControlClickByHandle (HWND hWnd, HWND hCtrl, WString szButton, int nNumClicks, int nX, int nY);
    public void AU3_ControlCommandByHandle(HWND hWnd, HWND hCtrl, WString szCommand, WString szExtra, byte[] szResult, int nBufSize);
    public void AU3_ControlListViewByHandle(HWND hWnd, HWND hCtrl, WString szCommand, WString szExtra1, WString szExtra2, byte[] szResult, int nBufSize);
    public int  AU3_ControlDisableByHandle(HWND hWnd, HWND hCtrl);
    public int  AU3_ControlEnableByHandle(HWND hWnd, HWND hCtrl);
    public int  AU3_ControlFocusByHandle(HWND hWnd, HWND hCtrl);
    public void AU3_ControlGetFocusByHandle(HWND hWnd, byte[] szControlWithFocus, int nBufSize);
    public HWND AU3_ControlGetHandle(HWND hWnd, WString szControl);
    public int  AU3_ControlGetPos (WString szTitle, WString szText, WString szControl, LPRECT lpRect);
    public int  AU3_ControlGetPosByHandle(HWND hWnd, HWND hCtrl, LPRECT lpRect);
    public void AU3_ControlGetTextByHandle(HWND hWnd, HWND hCtrl, byte[] szControlText, int nBufSize);
    public int  AU3_ControlHideByHandle(HWND hWnd, HWND hCtrl);
    public int  AU3_ControlMoveByHandle(HWND hWnd, HWND hCtrl, int nX, int nY, int nWidth, int nHeight);
    public int  AU3_ControlSendByHandle(HWND hWnd, HWND hCtrl, WString szSendText, int nMode);
    public int  AU3_ControlSetTextByHandle(HWND hWnd, HWND hCtrl, WString szControlText);
    public int  AU3_ControlShowByHandle(HWND hWnd, HWND hCtrl);
    public void AU3_ControlTreeViewByHandle(HWND hWnd, HWND hCtrl, WString szCommand, WString szExtra1, WString szExtra2, byte[] szResult, int nBufSize);

    public void AU3_MouseGetPos (LPPOINT lpPoint);

    public int  AU3_PixelChecksum (LPRECT lpRect, int nStep);
    public void AU3_PixelSearch (LPRECT lpRect, int nCol, int nVar, int nStep, LPPOINT pPointResult);

    public int  AU3_StatusbarGetTextByHandle(HWND hWnd, int nPart, byte[] szStatusText, int nBufSize);

    public int  AU3_WinActivateByHandle (HWND hWnd);
    public int  AU3_WinActiveByHandle (HWND hWnd);
    public int  AU3_WinCloseByHandle (HWND hWnd);
    public int  AU3_WinExistsByHandle (HWND hWnd);
    public int  AU3_WinGetCaretPos (LPPOINT lpPoint);
    public void AU3_WinGetClassListByHandle (HWND hWnd, byte[] szRetText, int nBufSize);
    public int  AU3_WinGetClientSize (WString szTitle, WString szText, LPRECT lpRect);
    public int  AU3_WinGetClientSizeByHandle (HWND hWnd, LPRECT lpRect);
    public HWND AU3_WinGetHandle (WString szTitle, WString szText);
    public void AU3_WinGetHandleAsText (WString szTitle, WString szText, byte[] szRetText, int nBufSize);
    public int  AU3_WinGetPos (WString szTitle, WString szText, LPRECT lpRect);
    public int  AU3_WinGetPosByHandle (HWND hWnd, LPRECT lpRect);
    public DWORD AU3_WinGetProcess (WString szTitle, WString szText);
    public DWORD AU3_WinGetProcessByHandle (HWND hWnd);
    public int  AU3_WinGetStateByHandle (HWND hWnd);
    public void AU3_WinGetTextByHandle (HWND hWnd, byte[] szRetText, int nBufSize);
    public void AU3_WinGetTitleByHandle (HWND hWnd, byte[] szRetText, int nBufSize);
    public int  AU3_WinKillByHandle (HWND hWnd);
    public int  AU3_WinMenuSelectItemByHandle (HWND hWnd, WString szItem1, WString szItem2, WString szItem3, WString szItem4, WString szItem5, WString szItem6, WString szItem7, WString szItem8);
    public int  AU3_WinMoveByHandle (HWND hWnd, int nX, int nY, int nWidth, int nHeight);
    public int  AU3_WinSetOnTopByHandle (HWND hWnd, int nFlag);
    public int  AU3_WinSetStateByHandle (HWND hWnd, int nFlags);
    public int  AU3_WinSetTitleByHandle (HWND hWnd, WString szNewTitle);
    public int  AU3_WinSetTransByHandle (HWND hWnd, int nTrans);
    public int  AU3_WinWaitByHandle (HWND hWnd, int nTimeout);
    public int  AU3_WinWaitActiveByHandle (HWND hWnd, int nTimeout);
    public int  AU3_WinWaitCloseByHandle (HWND hWnd, int nTimeout);
    public int  AU3_WinWaitNotActiveByHandle (HWND hWnd, int nTimeout);
    */
}
