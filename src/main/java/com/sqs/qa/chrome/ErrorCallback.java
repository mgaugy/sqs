
package com.sqs.qa.chrome;

public interface ErrorCallback
{
	public abstract void debug( final String format, final Object... args );

	public abstract void info( final String format, final Object... args );

	public abstract void warning( final String format, final Object... args );

	public abstract void error( final String format, final Object... args );
}
