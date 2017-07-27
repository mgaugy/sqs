/*
 * com/sqs/qa/chrome/CanvasScreenshotTest.java
 *
 * CanvasScreenshotTest object
 *
 * Copyright (c) 2017 Michael Gaugy.  All rights reserved.
 */

package com.sqs.qa.chrome;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import com.google.common.io.Files;


/**
 * <p>Test to validate that a <code>&lt;canvas&gt;</code> element exists, and
 * compares a screenshot of it to a reference screenshot.</p>
 *
 * @author Michael Gaugy (mgaugy@yahoo.com)
 */
public class CanvasScreenshotTest
extends ChromeDriverTest
{
	//
	// Instance fields
	//

	/**
	 * <p>The web driver to use.</p>
	 */
	private final WebDriver m_webDriver;

	/**
	 * <p>The URL to be tested.</p>
	 */
	private final String m_url;


	//
	// Constructors
	//

	/**
	 * <p>Initialize a new {@link CanvasScreenshotTest} with the specified
	 * URL.</p>
	 *
	 * @param url
	 * The URL to test.
	 */
	public CanvasScreenshotTest( final String url ) {
		super();

		if( url == null ) {
			throw( new NullPointerException("'url' is null!") );
		} else if( url.length() == 0 ) {
			throw( new IllegalArgumentException("'url' has zero length") );
		}

		m_webDriver = new ChromeDriver();
		m_url = url;
	}


	//
	// Instance methods
	//

	@Override
	public void run() {
		// Fetch the page
		m_webDriver.get( m_url );

		// Zoom out
		final WebElement htmlWebElement = m_webDriver.findElement( By.tagName("html") );
		htmlWebElement.sendKeys( Keys.chord(Keys.CONTROL, Keys.SUBTRACT) );

		// Find the "large-10" <div> containers.
		final By large10By = By.className( "large-10" );
		final List<WebElement> large10WebElementList = m_webDriver.findElements( large10By );

		for( WebElement large10WebElement : large10WebElementList ) {
			final By canvasBy = By.tagName( "canvas" );
			final List<WebElement> canvasWebElementList = large10WebElement.findElements( canvasBy );
			if( canvasWebElementList.isEmpty() == false ) {
				// XXX: ASSUME: Check only the first <canvas> element
				try {
					final WebElement canvasWebElement = canvasWebElementList.get( 0 );
					captureScreenshot( m_webDriver, canvasWebElement, "selenium-canvas.png", true );
					final File testFile = new File( System.getProperty("user.home") + "/Downloads/selenium-canvas.png" );
					final File referenceFile = new File( System.getProperty("user.home") + "/Downloads/selenium-canvas-original.png" );
					final boolean result = Files.equal( testFile, referenceFile );
					if( result == false ) {
						error( "Canvas elements differ.  Please check image files." );
					}
				} catch( IOException ioException ) {
					ioException.printStackTrace();
				}
			} else {
				error( "No <canvas> element found to test!" );
			}
		}

		// All done.
		m_webDriver.close();
	}


	//
	// Class methods
	//

	/**
	 * <p>Stand-alone entry point.</p>
	 *
	 * @param args
	 * Command-line arguments.
	 */
	public static final void main( String[] args ) {
		final CanvasScreenshotTest test =
				new CanvasScreenshotTest( "https://the-internet.herokuapp.com/challenging_dom" );
		(new Thread(test)).start();
	}
}
