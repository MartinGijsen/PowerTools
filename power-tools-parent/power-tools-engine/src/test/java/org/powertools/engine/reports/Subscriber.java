/*	Copyright 2012 by Martin Gijsen (www.DeAnalist.nl)
 *
 *	This file is part of the PowerTools engine.
 *
 *	The PowerTools engine is free software: you can redistribute it and/or
 *	modify it under the terms of the GNU Affero General Public License as
 *	published by the Free Software Foundation, either version 3 of the License,
 *	or (at your option) any later version.
 *
 *	The PowerTools engine is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *	GNU Affero General Public License for more details.
 *
 *	You should have received a copy of the GNU Affero General Public License
 *	along with the PowerTools engine. If not, see <http://www.gnu.org/licenses/>.
 */

package org.powertools.engine.reports;

import java.util.Date;

import org.powertools.engine.TestLine;


final class Subscriber implements TestCaseSubscriber, TestLineSubscriber, TestResultSubscriber, ModelSubscriber {
	public enum Method {
		NONE,
		START,
		FINISH,
		PROCESS_STACK_TRACE,
		PROCESS_ERROR,
		PROCESS_WARNING,
		PROCESS_INFO,
		PROCESS_LINK,
		PROCESS_END_OF_TESTLINE,
		PROCESS_INCREASE_LEVEL,
		PROCESS_DECREASE_LEVEL,
		PROCESS_COMMENT_LINE_STRING,
		PROCESS_COMMENT_LINE_TESTLINE,
		PROCESS_TESTLINE,
		PROCESS_END_OF_SECTION,
		PROCESS_BEGIN,
		PROCESS_END,
		PROCESS_NEW_EDGE,
		PROCESS_AT_NODE,
		PROCESS_AT_EDGE,
		MORE_THAN_ONE
	}
	
	private Method mLastMethod;
	
	
	Subscriber () {
		mLastMethod = Method.NONE;
	}
	
	Method getLastMethod () {
		return mLastMethod;
	}
	
	
	@Override
	public void start (Date dateTime) {
		assignMethod (Method.START);
	}

	@Override
	public void finish (Date dateTime) {
		assignMethod (Method.FINISH);
	}

	
	@Override
	public void processStackTrace (String[] stackTraceLines) {
		assignMethod (Method.PROCESS_STACK_TRACE);
	}

	@Override
	public void processError(String message) {
		assignMethod (Method.PROCESS_ERROR);
	}

	@Override
	public void processWarning(String message) {
		assignMethod (Method.PROCESS_WARNING);
	}

	@Override
	public void processInfo(String message) {
		assignMethod (Method.PROCESS_INFO);
	}

	@Override
	public void processLink(String url) {
		assignMethod (Method.PROCESS_LINK);
	}

	@Override
	public void processEndOfTestLine() {
		assignMethod (Method.PROCESS_END_OF_TESTLINE);
	}

	@Override
	public void processIncreaseLevel() {
		assignMethod (Method.PROCESS_INCREASE_LEVEL);
	}

	@Override
	public void processDecreaseLevel() {
		assignMethod (Method.PROCESS_DECREASE_LEVEL);
	}

	@Override
	public void processCommentLine(String testLine) {
		assignMethod (Method.PROCESS_COMMENT_LINE_STRING);
	}

	@Override
	public void processCommentLine(TestLine testLine) {
		assignMethod (Method.PROCESS_COMMENT_LINE_TESTLINE);
	}

	@Override
	public void processTestLine(TestLine testLine) {
		assignMethod (Method.PROCESS_TESTLINE);
	}

	@Override
	public void processEndSection() {
		assignMethod (Method.PROCESS_END_OF_SECTION);
	}

	@Override
	public void processBegin(String id, String description) {
		assignMethod (Method.PROCESS_BEGIN);
	}

	@Override
	public void processEnd () {
		assignMethod (Method.PROCESS_END);
	}
	
	@Override
	public void processNewEdge (String sourceName, String targetName) {
		assignMethod (Method.PROCESS_NEW_EDGE);
	}
	
	@Override
	public void processAtNode (String name) {
		assignMethod (Method.PROCESS_AT_NODE);
	}
	
	@Override
	public void processAtEdge (String sourceName, String targetName) {
		assignMethod (Method.PROCESS_AT_EDGE);
	}
	
	private void assignMethod (Method method) {
		if (mLastMethod == Method.NONE) {
			mLastMethod = method;
		} else {
			mLastMethod = Method.MORE_THAN_ONE;
		}
	}
}