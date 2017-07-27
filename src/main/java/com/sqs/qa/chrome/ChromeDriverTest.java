/*
 * com/sqs/qa/chrome/ChromeDriverTest.java
 *
 * ChromeDriverTest object
 *
 * Copyright (c) 2017 Michael Gaugy.  All rights reserved.
 */

package com.sqs.qa.chrome;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


/**
 * <p>Base class for Chrome-based tests.</p>
 *
 * @author Michael Gaugy (mgaugy@yahoo.com)
 */
public abstract class ChromeDriverTest
implements Runnable
{
	//
	// Class fields
	//

	/**
	 * <p>Flag to indicate that we have previously set the corresponding system
	 * property.</p>
	 */
	private static boolean ms_driverInitialised;


	//
	// Instance fields
	//

	private ErrorCallback m_errorCallback;


	//
	// Constructors
	//

	/**
	 * <p>Initialize a new {@link ChromeDriverTest} with default values.</p>
	 */
	public ChromeDriverTest() {
		super();

		if( ms_driverInitialised == false ) {
			final String executablePath =
					System.getProperty("user.home")
					+ "/selenium/chromedriver/chromedriver";
			System.setProperty( "webdriver.chrome.driver", executablePath );
			ms_driverInitialised = true;
		}
	}


	//
	// Instance methods
	//

	/**
	 * <p>Register callback object for messages.</p>
	 *
	 * @param callback
	 * The callback object.
	 */
	public final void setErrorCallback( final ErrorCallback callback ) {
		m_errorCallback = callback;
	}

	/**
	 * <p>Issue a debug message.</p>
	 *
	 * @param format
	 * The format string.
	 * @param args
	 * Arguments
	 */
	public final void debug( final String format, final Object... args ) {
		m_errorCallback.debug( format, args );
	}

	/**
	 * <p>Issue an informative message.</p>
	 *
	 * @param format
	 * The format string.
	 * @param args
	 * Arguments
	 */
	public final void info( final String format, final Object... args ) {
		m_errorCallback.info( format, args );
	}

	/**
	 * <p>Issue an warning message.</p>
	 *
	 * @param format
	 * The format string.
	 * @param args
	 * Arguments
	 */
	public final void warning( final String format, final Object... args ) {
		m_errorCallback.warning( format, args );
	}

	/**
	 * <p>Issue an error message.</p>
	 *
	 * @param format
	 * The format string.
	 * @param args
	 * Arguments
	 */
	public final void error( final String format, final Object... args ) {
		m_errorCallback.error( format, args );
	}


	//
	// Class fields
	//

	/**
	 * <p>Utility method to pretty-print a {@link WebElement}.</p>
	 *
	 * <p>This generates a string of the form:</p>
	 *
	 * <blockquote><pre>'&lt' <i>tag-name</i> ['#' <i>id</i>] ['.' <i>class-name</i> ['.' <i>class-name</i>] ...] '>'</pre>
	 * </blockquote>
	 *
	 * @param element
	 * The elment to print.
	 * @return
	 * Returns the formatted element.
	 */
	public static final String toString( final WebElement element ) {
		final StringBuilder sb = new StringBuilder();
		sb.append( "<" );
		sb.append( element.getTagName() );
		final String idAttribute = element.getAttribute( "id" );
		if( idAttribute != null ) {
			sb.append( "#" + idAttribute );
		}
		final String classNameAttribute = element.getAttribute( "class" );
		if( classNameAttribute != null ) {
			String[] classNames = classNameAttribute.split( " " );
			for( String className : classNames ) {
				sb.append( "." + className );
			}
		}
		sb.append( "/>" );
		final String result = sb.toString();
		return( result );
	}

	/**
	 * <p>Capture a screenshot of the specified element, scrolling to it as
	 * necessary.</p>
	 *
	 * @param driver
	 * The web driver.
	 * @param element
	 * The element to screenshot.
	 * @param filename
	 * The output filename.
	 * @param replaceFile
	 * Specifies whether to replace an existing file (<code>true</code>) or
	 * leave it untouched (<code>false</code>).
	 *
	 * @throws IOException
	 * This exception is thrown if an I/O error occurs.
	 */
	public static void captureScreenshot( final WebDriver driver, final WebElement element,
			final String filename, final boolean replaceFile )
	throws IOException {
		// Check first for an existing file
		final String destinationPath = System.getProperty("user.home") + "/Downloads/" + filename;
		final File destinationFile = new File( destinationPath );
		if( (destinationFile.exists() == true)
				&& (replaceFile == false) ) {
			final String msg = String.format( "Cannot replace existing file: %s", destinationFile );
			throw( new IOException(msg) );
		}

		// Scroll to the element
		final JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
		javascriptExecutor.executeScript( "arguments[0].scrollIntoView(true);", element );
		javascriptExecutor.executeScript( "document.body.style.zoom='50%';", element );	// Does not work for IE

		// Wait a short amount of time to complete the scroll before taking the screenshot
		try {
			Thread.sleep(500);
		} catch( InterruptedException interruptedException ) {
			// Ignore
		}

		final Point location = element.getLocation();
		final Dimension size = element.getSize();

		final TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
		final File screenshotFile = takesScreenshot.getScreenshotAs( OutputType.FILE );
		final BufferedImage fullBufferedImage = ImageIO.read( screenshotFile );

		final BufferedImage croppedBufferedImage =
				fullBufferedImage.getSubimage( location.x, location.y, size.width, size.height );
		ImageIO.write( croppedBufferedImage, "png", screenshotFile );

		if( (destinationFile.exists() == true)
				&& (replaceFile == true) ) {
			FileUtils.deleteQuietly( destinationFile );
		}

		FileUtils.copyFile( screenshotFile, destinationFile );
	}
}
