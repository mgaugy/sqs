/*
 * com/sqs/qa/chrome/LeftColumnButtonWidthTest.java
 *
 * LeftColumnButtonWidthTest object
 *
 * Copyright (c) 2017 Michael Gaugy.  All rights reserved.
 */

package com.sqs.qa.chrome;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;


/**
 * <p>A test to validate that all the buttons occurring in the left column
 * are consistent and have the same visual widths.</p>
 *
 * @author Michael Gaugy (mgaugy@yahoo.com)
 */
public class LeftColumnButtonWidthTest
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
	 * <p>Initialize a new {@link LeftColumnButtonWidthTest} with the specified
	 * URL.</p>
	 *
	 * @param url
	 * The URL to test.
	 */
	public LeftColumnButtonWidthTest( final String url ) {
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

		int minimumWidth = 1000;
		int maximumWidth = 0;
		int narrowestCount = 0;
		int widestCount = 0;
		WebElement narrowestWebElement = null;
		WebElement widestWebElement = null;

		// Cycle through and match the sizes
		for( WebElement buttonWebElement : buttonWebElementList ) {
			final Dimension size = buttonWebElement.getSize();
			if( minimumWidth > size.width ) {
				narrowestCount = 1;
				narrowestWebElement = buttonWebElement;
				minimumWidth = size.width;
			} else if( minimumWidth == size.width ) {
				narrowestCount++;
			}
			if( maximumWidth < size.width ) {
				widestCount = 1;
				widestWebElement = buttonWebElement;
				maximumWidth = size.width;
			} else if( maximumWidth == size.width ) {
				widestCount++;
			}
		}

		// Compare the sizes and determine whether this test passes or fails
		if( minimumWidth != maximumWidth ) {
			if( narrowestCount >= widestCount ) {
				error( "%d element(s) are wider than the others: %s",
						widestCount, toString(widestWebElement) );
			} else {
				error( "%d element(s) are narrower than the others: %s",
						narrowestCount, toString(narrowestWebElement) );
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
		final LeftColumnButtonWidthTest test =
				new LeftColumnButtonWidthTest( "https://the-internet.herokuapp.com/challenging_dom" );
		(new Thread(test)).start();
	}
}
