/*
 * com/sqs/qa/chrome/LeftColumnButtonStringCharactersTest.java
 *
 * LeftColumnButtonStringCharactersTest object
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
 * <p>A test to validate that all the buttons occurring in the left column
 * do not have invalid characters in their names.</p>
 *
 * @author Michael Gaugy (mgaugy@yahoo.com)
 */
public class LeftColumnButtonStringCharactersTest
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
	 * <p>Initialize a new {@link LeftColumnButtonStringCharactersTest} with the specified
	 * URL.</p>
	 *
	 * @param url
	 * The URL to test.
	 */
	public LeftColumnButtonStringCharactersTest( final String url ) {
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

		// Cycle through and test the string characters
		for( WebElement buttonWebElement : buttonWebElementList ) {
			final String webElementText = buttonWebElement.getText();
			debug( "Button: '%s' with text '%s'", toString(buttonWebElement), webElementText );
			char[] webElementChars = webElementText.toCharArray();
			for( char ch : webElementChars ) {
				if( Character.isISOControl(ch) == true ) {
					error( "Invalid character(s) found in element %s",
							toString(buttonWebElement) );
					break;
				}
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
		final LeftColumnButtonStringCharactersTest test =
				new LeftColumnButtonStringCharactersTest( "https://the-internet.herokuapp.com/challenging_dom" );
		(new Thread(test)).start();
	}
}
