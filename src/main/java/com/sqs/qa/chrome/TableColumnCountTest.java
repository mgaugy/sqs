/*
 * com/sqs/qa/chrome/TableColumnCountTest.java
 *
 * TableColumnCountTest object
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
 * <p>Test to validate that the table contains the correct number of columns.</p>
 *
 * @author Michael Gaugy (mgaugy@yahoo.com)
 */
public class TableColumnCountTest
extends ChromeDriverTest
{
	//
	// Constants
	//

	private static final int COLUMN_COUNT = 8;


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
	 * <p>Initialize a new {@link TableColumnCountTest} with the specified
	 * URL.</p>
	 *
	 * @param url
	 * The URL to test.
	 */
	public TableColumnCountTest( final String url ) {
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
		final List<WebElement> large10WebElementList = m_webDriver.findElements( By.className("large-10") );

		for( WebElement large10WebElement : large10WebElementList ) {
			final List<WebElement> tableWebElementList = large10WebElement.findElements( By.tagName("table") );
			if( tableWebElementList.isEmpty() == false ) {
				// XXX: Assumption: Check only the first table
				final WebElement tableWebElement = tableWebElementList.get( 0 );
				final WebElement theadWebElement = tableWebElement.findElement( By.tagName("thead") );
				final List<WebElement> thWebElementList = theadWebElement.findElements( By.tagName("th") );
				if( thWebElementList.size() != COLUMN_COUNT ) {
					error( "Table header does not contain required number of columns (%d): %d",
							COLUMN_COUNT, thWebElementList.size() );
				}
				
				final WebElement tbodyWebElement = tableWebElement.findElement( By.tagName("tbody") );
				final List<WebElement> trWebElementList = tbodyWebElement.findElements( By.tagName("tr") );
				final int trWebElementCount = trWebElementList.size();
				for( int trWebElementIndex = 0; trWebElementIndex < trWebElementCount; trWebElementIndex++ ) {
					final WebElement trWebElement = trWebElementList.get( trWebElementIndex );
					final List<WebElement> tdWebElementList = trWebElement.findElements( By.tagName("td") );
					if( tdWebElementList.size() != COLUMN_COUNT ) {
						error( "Row %d does not contain required number of columns (%d): %d",
								trWebElementIndex, COLUMN_COUNT, thWebElementList.size() );
					}
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
		final TableColumnCountTest test =
				new TableColumnCountTest( "https://the-internet.herokuapp.com/challenging_dom" );
		(new Thread(test)).start();
	}
}
