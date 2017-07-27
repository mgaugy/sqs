/*
 * com/sqs/qa/chrome/LeftColumnSuccessButtonsTest.java
 *
 * LeftColumnSuccessButtonsTest object
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
 * <p>A test to validate that at least one of the buttons occurring has a
 * <code>'success'</code> class-name.</p>
 *
 * @author Michael Gaugy (mgaugy@yahoo.com)
 */
public class LeftColumnSuccessButtonsTest
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
	 * <p>Initialize a new {@link LeftColumnSuccessButtonsTest} with the specified
	 * URL.</p>
	 *
	 * @param url
	 * The URL to test.
	 */
	public LeftColumnSuccessButtonsTest( final String url ) {
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

		// Find the "large-2" <div> container.  It should have one or more
		// anchor child elements, each having the "button" class
		final WebElement large2WebElement = m_webDriver.findElement( By.className("large-2") );

		// Each anchor should have a "button" class
		final List<WebElement> buttonWebElementList = large2WebElement.findElements( By.className("button") );

		// Find all anchors with the "success" class
		final List<WebElement> successWebElementList = large2WebElement.findElements( By.className("success") );

		// Find elements that have both classes
		// (Use separate counter in case additional validation is necessary)
		int successWebElementCount = 0;
		if( successWebElementList.isEmpty() == false ) {
			for( WebElement buttonWebElement : buttonWebElementList ) {
				if( successWebElementList.contains(buttonWebElement) == true ) {
					info( "Success button found: %s",
							toString(buttonWebElement) );
					successWebElementCount++;
				}
			}
		}
		if( successWebElementCount == 0 ) {
			error( "0 'success' elements were found" );
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
		final LeftColumnSuccessButtonsTest test =
				new LeftColumnSuccessButtonsTest( "https://the-internet.herokuapp.com/challenging_dom" );
		(new Thread(test)).start();
	}
}
