/*
 * com/sqs/qa/chrome/ChromeDriverTestRunner.java
 *
 * ChromeDriverTestRunner object
 *
 * Copyright (c) 2017 Michael Gaugy.  All rights reserved.
 */

package com.sqs.qa.chrome;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;


/**
 * <p>Test harness for executing test cases.</p>
 *
 * @author Michael Gaugy (mgaugy@yahoo.com)
 */
public final class ChromeDriverTestRunner
implements Runnable, ErrorCallback
{
	//
	// Constants
	//

	/**
	 * <p>The list of tests to execute.</p>
	 */
	private static final Class<?>[] TEST_CLASSES = {
		LeftColumnButtonWidthTest.class,
		LeftColumnButtonStringCharactersTest.class,
		LeftColumnButtonDuplicatedStringTest.class,
		LeftColumnAlertButtonsTest.class,
		LeftColumnSuccessButtonsTest.class,

		TableColumnCountTest.class,
		TableColumn3TextTest.class,
		TableActionLinksTest.class,

		CanvasSizeTest.class,
		CanvasScreenshotTest.class,
	};


	//
	// Instance fields
	//

	/**
	 * <p>The URL for which to perform the tests.</p>
	 */
	private final String m_url;

	private int m_errorCount;

	private boolean m_allowDebug;


	//
	// Constructors
	//

	/**
	 * <p>Initialize a new {@link ChromeDriverTestRunner} with the specified
	 * URL.</p>
	 *
	 * @param url
	 * The URL for which to perform the tests.
	 */
	private ChromeDriverTestRunner( final String url  ) {
		super();
		m_allowDebug = true;
		m_url = url;
	}


	//
	// Instance methos
	//

	private static final String SEPARATOR_LINE =
			"--------------------------------------------------------------------------------";
	/**
	 * <p>Execute all the test cases.</p>
	 */
	@Override
	public void run() {
		for( Class<?> testClass : TEST_CLASSES ) {
			System.out.println( "\n" + SEPARATOR_LINE );
			try {
				final Constructor<?> constructor = testClass.getConstructor( String.class );
				final ChromeDriverTest testInstance = (ChromeDriverTest) constructor.newInstance( m_url );
				testInstance.setErrorCallback( this );
				final Thread thread = new Thread( testInstance );
				thread.start();
				thread.join();
			} catch( NoSuchMethodException noSuchMethodException ) {
				noSuchMethodException.printStackTrace();
			} catch( SecurityException securityException ) {
				securityException.printStackTrace();
			} catch( InstantiationException instantiationException ) {
				instantiationException.printStackTrace();
			} catch( IllegalAccessException illegalAccessException ) {
				illegalAccessException.printStackTrace();
			} catch( IllegalArgumentException illegalArgumentException ) {
				illegalArgumentException.printStackTrace();
			} catch( InvocationTargetException invocationTargetException ) {
				invocationTargetException.printStackTrace();
			} catch( InterruptedException interruptedException ) {
				// Ignored
			}
		}

		System.out.println( SEPARATOR_LINE );
		System.out.format( "\n%d tests, %d errors\n", TEST_CLASSES.length, m_errorCount );
		System.out.println( SEPARATOR_LINE );
	}

	/**
	 * <p>Issue a debug message.</p>
	 *
	 * @param format
	 * The format string.
	 * @param args
	 * Arguments
	 */
	@Override
	public final void debug( final String format, final Object... args ) {
		if( m_allowDebug == true ) {
			String className = getCallstackClassName( 3 );
			System.out.format( className + ": DEBUG: " + format + "\n", args );
		}
	}

	/**
	 * <p>Issue an informative message.</p>
	 *
	 * @param format
	 * The format string.
	 * @param args
	 * Arguments
	 */
	@Override
	public final void info( final String format, final Object... args ) {
		String className = getCallstackClassName( 3 );
		System.out.format( className + ": INFO: " + format + "\n", args );
	}

	/**
	 * <p>Issue a warning message.</p>
	 *
	 * @param format
	 * The format string.
	 * @param args
	 * Arguments
	 */
	@Override
	public final void warning( final String format, final Object... args ) {
		String className = getCallstackClassName( 3 );
		System.out.format( className + ": WARNING: " + format + "\n", args );
	}

	/**
	 * <p>Issue an error message.</p>
	 *
	 * @param format
	 * The format string.
	 * @param args
	 * Arguments
	 */
	@Override
	public final void error( final String format, final Object... args ) {
		String className = getCallstackClassName( 3 );
		System.out.format( className + ": ERROR: " + format + "\n", args );
		m_errorCount++;
	}


	//
	// Class methods
	//

	/**
	 * <p>Main entry point.</p>
	 *
	 * @param args
	 * Command-line arguments.
	 */
	public static final void main( String[] args ) {
		final String url = "https://the-internet.herokuapp.com/challenging_dom";
		final ChromeDriverTestRunner testRunner = new ChromeDriverTestRunner( url );
		final Thread testRunnerThread = new Thread( testRunner );
		testRunnerThread.start();
	}

	/**
	 * <p>Utility method to identify the class name of a caller.</p>
	 *
	 * @param depth
	 * The callstack depth from which to retrieve the class name.
	 * @return
	 * Returns the full class name.
	 */
	private static final String getCallstackClassName( int depth ) {
		String result = null;
		try {
			throw( new Exception() );
		} catch( Exception exception ) {
			StackTraceElement[] stackTrace = exception.getStackTrace();
			result = stackTrace[depth].getClassName();
		}
		return( result );
	}
}
