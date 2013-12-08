package org.powertools.engine.reports;

import java.util.Date;

import org.powertools.engine.TestLine;


final class Subscriber implements TestCaseSubscriber, TestLineSubscriber, TestResultSubscriber {
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
		PROCESS_END
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
		mLastMethod = Method.START;
	}

	@Override
	public void finish (Date dateTime) {
		mLastMethod = Method.FINISH;
	}

	
	@Override
	public void processStackTrace (String[] stackTraceLines) {
		mLastMethod = Method.PROCESS_STACK_TRACE;
	}

	@Override
	public void processError(String message) {
		mLastMethod = Method.PROCESS_ERROR;
	}

	@Override
	public void processWarning(String message) {
		mLastMethod = Method.PROCESS_WARNING;
	}

	@Override
	public void processInfo(String message) {
		mLastMethod = Method.PROCESS_INFO;
	}

	@Override
	public void processLink(String url) {
		mLastMethod = Method.PROCESS_LINK;
	}

	@Override
	public void processEndOfTestLine() {
		mLastMethod = Method.PROCESS_END_OF_TESTLINE;
	}

	@Override
	public void processIncreaseLevel() {
		mLastMethod = Method.PROCESS_INCREASE_LEVEL;
	}

	@Override
	public void processDecreaseLevel() {
		mLastMethod = Method.PROCESS_DECREASE_LEVEL;
	}

	@Override
	public void processCommentLine(String testLine) {
		mLastMethod = Method.PROCESS_COMMENT_LINE_STRING;
	}

	@Override
	public void processCommentLine(TestLine testLine) {
		mLastMethod = Method.PROCESS_COMMENT_LINE_TESTLINE;
	}

	@Override
	public void processTestLine(TestLine testLine) {
		mLastMethod = Method.PROCESS_TESTLINE;
	}

	@Override
	public void processEndSection() {
		mLastMethod = Method.PROCESS_END_OF_SECTION;
	}

	@Override
	public void processBegin(String id, String description) {
		mLastMethod = Method.PROCESS_BEGIN;
	}

	@Override
	public boolean processEnd () {
		mLastMethod = Method.PROCESS_END;
		return true;
	}
}