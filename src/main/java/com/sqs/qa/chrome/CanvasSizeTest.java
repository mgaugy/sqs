/*
 * com/sqs/qa/chrome/CanvasSizeTest.java
 *
 * CanvasSizeTest object
 *
 * Copyright (c) 2017 Michael Gaugy.  All rights reserved.
 */

package com.sqs.qa.chrome;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;


/**
 * <p>Test to validate that there exists a <code>&lt;canvas&gt;</code> element
 * and it has the required dimensions.</p>
 *
 * @author Michael Gaugy (mgaugy@yahoo.com)
 */
public class CanvasSizeTest
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
	 * <p>Initialize a new {@link CanvasSizeTest} with the specified
	 * URL.</p>
	 *
	 * @param url
	 * The URL to test.
	 */
	public CanvasSizeTest( final String url ) {
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

		// Find the "large-10" <div> containers.
		final By large10By = By.className( "large-10" );
		final List<WebElement> large10WebElementList = m_webDriver.findElements( large10By );

		for( WebElement large10WebElement : large10WebElementList ) {
			final By canvasBy = By.tagName( "canvas" );
			final List<WebElement> canvasWebElementList = large10WebElement.findElements( canvasBy );
			if( canvasWebElementList.isEmpty() == false ) {
				// XXX: Assumption: Check only the first <canvas> element
				final WebElement canvasWebElement = canvasWebElementList.get( 0 );

				final String canvasWidthString = canvasWebElement.getAttribute( "width" );
				final String canvasHeightString = canvasWebElement.getAttribute( "height" );
				final int canvasWidth = Integer.parseInt( canvasWidthString );
				final int canvasHeight = Integer.parseInt( canvasHeightString );

				if( (canvasWidth < 600) || (canvasHeight < 200) ) {
					error( "Canvas does not have required dimensions (minimum 600 x 200): %d x %d",
							canvasWidth, canvasHeight );
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
		final CanvasSizeTest test =
				new CanvasSizeTest( "https://the-internet.herokuapp.com/challenging_dom" );
		(new Thread(test)).start();
	}
}
