/*
 * com/sqs/qa/chrome/TableActionLinksTest.java
 *
 * TableActionLinksTest object
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
 * <p>Test to validate that each of the rows in the table has links in the
 * "Action" column.</p>
 *
 * @author Michael Gaugy (mgaugy@yahoo.com)
 */
public class TableActionLinksTest
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
	 * <p>Initialize a new {@link TableActionLinksTest} with the specified
	 * URL.</p>
	 *
	 * @param url
	 * The URL to test.
	 */
	public TableActionLinksTest( final String url ) {
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

				// Find which column index has the "Action" column header
				int actionColumnIndex = -1;
				// XXX: Assumption: Only one header row
				final List<WebElement> thWebElementList = tableWebElement.findElements( By.tagName("th") );
				for( int thIndex = 0; thIndex < thWebElementList.size(); thIndex++ ) { 
					final WebElement thWebElement = thWebElementList.get( thIndex );
					final String thText = thWebElement.getText();
					if( thText.equals("Action") == true ) {
						// We found a match
						// XXX: Assumption: Only take the first column named "Action"
						actionColumnIndex = thIndex;
						break;
					}
				}

				if( actionColumnIndex >= 0 ) {
					final WebElement tbodyWebElement = tableWebElement.findElement( By.tagName("tbody") );

					final List<WebElement> trWebElementList = tbodyWebElement.findElements( By.tagName("tr") );

					final int trWebElementCount = trWebElementList.size();
					for( int trWebElementIndex = 0; trWebElementIndex < trWebElementCount; trWebElementIndex++ ) {
						final WebElement trWebElement = trWebElementList.get( trWebElementIndex );
						final List<WebElement> tdWebElementList = trWebElement.findElements( By.tagName("td") );
						// Find the n-th <td>
						final List<WebElement> aWebElementList = tdWebElementList.get(actionColumnIndex).findElements( By.tagName("a") );
						// XXX: Assumption: There are only two anchor tags in this cell
						if( aWebElementList.size() != 2 ) {
							error( "Row %d does not have required 2 <a> links for action column %d",
									trWebElementIndex, actionColumnIndex );
						}
					}
				} else {
					error( "Could not find an \"Action\" column header" );
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
		final TableActionLinksTest test =
				new TableActionLinksTest( "https://the-internet.herokuapp.com/challenging_dom" );
		(new Thread(test)).start();
	}
}
