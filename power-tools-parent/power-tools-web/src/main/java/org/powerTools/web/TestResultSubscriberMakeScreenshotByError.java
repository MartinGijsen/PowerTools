package org.powerTools.web;

import java.util.Date;

import org.powerTools.engine.reports.TestResultSubscriber;

public class TestResultSubscriberMakeScreenshotByError implements TestResultSubscriber {

	private WebLibrary webLibrary;
	
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
