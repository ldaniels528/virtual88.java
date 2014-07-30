/**
 * 
 */
package ibmpc.util;

import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * JBasic Logger/Debugger
 * @author lawrence.daniels@gmail.com
 */
public class Logger {	
	private static final String LOGGER_CLASS_NAME = Logger.class.getName();
	public static DateFormat DATE_FORMAT	= new SimpleDateFormat( "hh:mm:ss" );
	public static boolean DEBUG				= false;
	public static boolean INFO				= true;
	public static boolean WARN				= true;
	public static boolean ERROR				= true;
	public static boolean METHODS			= false;
	public static boolean SHORT_CLASS_NAMES	= true;
	public static PrintStream out			= System.err;
	
	/**
	 * Logs the given debug statement
	 * @param format the given format text
	 * @param objs the given tokens to display
	 */
	public static void debug( final String format, final Object ... objs ) {	
		if( DEBUG ) log( format, objs );		
	}
	
	/**
	 * Logs the given debug bytes
	 * @param bytes the given bytes to display
	 */
	public static void debug( final byte[] bytes ) {
		if( DEBUG ) log( bytes );
	}
	
	/**
	 * Logs the given information statement
	 * @param format the given format text
	 * @param objs the given tokens to display
	 */
	public static void info( final String format, final Object ... objs ) {
		if( INFO ) log( format, objs );
	}
	
	/**
	 * Logs the given information bytes
	 * @param bytes the given bytes to display
	 */
	public static void info( final byte[] bytes ) {
		if( INFO ) log( bytes );
	}
	
	/**
	 * Logs the given information statement
	 * @param format the given format text
	 * @param objs the given tokens to display
	 */
	public static void warn( final String format, final Object ... objs ) {
		if( WARN ) log( format, objs );
	}
	
	/**
	 * Logs the given information bytes
	 * @param bytes the given bytes to display
	 */
	public static void warn( final byte[] bytes ) {
		if( WARN ) log( bytes );
	}
	
	/**
	 * Logs the given information statement
	 * @param format the given format text
	 * @param objs the given tokens to display
	 */
	public static void error( final String format, final Object ... objs ) {
		if( ERROR ) log( format, objs );
	}
	
	/**
	 * Logs the given information bytes
	 * @param bytes the given bytes to display
	 */
	public static void error( final byte[] bytes ) {
		if( ERROR ) log( bytes );
	}
	
	/**
	 * Writes the given bytes to the log
	 * @param bytes the given set of bytes
	 */
	private static void log( final byte[] bytes ) {
		// get the method and class names
		final String[] names = getCallReference();
		
		// get the logging time
		final String logTime = DATE_FORMAT.format( new java.util.Date() );
		
		// cache the method and class names
		final String className = names[0];
		final String methodName = names[1];

		// display the log message
		out.printf( METHODS ? "[%s] %s#%s: " : "[%s] %s: ", logTime, className, methodName );
		
		// display the bytes
		out.print( "bytes " );
		for( int n = 0; n < bytes.length; n++ ) { 
			out.printf( "%02X ", bytes[n] );
		}
		out.print( "[" + new String( bytes ) + "]\n" );
	}
	
	/**
	 * Writes the given text to the log
	 * @param format the given format text
	 * @param objs the given tokens to display
	 */
	private static void log( final String format, final Object ... objs ) {
		// get the method and class names
		final String[] names = getCallReference();
		
		// get the logging time
		final String logTime = DATE_FORMAT.format( new java.util.Date() );
		
		// cache the method and class names
		final String className = names[0];
		final String methodName = names[1];

		// display the log message
		out.printf( METHODS ? "[%s] %s#%s: " : "[%s] %s: ", logTime, className, methodName );		
		out.printf( format, objs );
	}
	
	/**
	 * @return the class name and method name of the last call
	 */
	private static String[] getCallReference() {
		// get the exception
		final Exception exc = new Exception();
		exc.fillInStackTrace();		
		
		// get the class and method names
		final StackTraceElement[] ste = exc.getStackTrace();
		
		// determine the requesting class
		int index = 0;
		while( ( index < ste.length ) && ste[index].getClassName().equals( LOGGER_CLASS_NAME ) ) {
			index++;
		}
		
		// get the class and method names
		final String className = SHORT_CLASS_NAMES 
									? toShortName( ste[ index ].getClassName() ) 
									: ste[ index ].getClassName();
									
		final String methodName = ste[ index ].getMethodName();
		
		// return the class and method name
		return new String[] { className, methodName };
	}
	
	/**
	 * Returns the short name of a class (e.g. "java.lang.String" => "String")
	 * @param className the given class name
	 * @return the short name of the given class name
	 */
	private static String toShortName( String className ) {
		final String[] sa = className.split( "[.]" );
		return sa[ sa.length - 1 ];
	}
	
}
