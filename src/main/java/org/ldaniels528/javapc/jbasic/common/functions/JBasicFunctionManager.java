/**
 * 
 */
package org.ldaniels528.javapc.jbasic.common.functions;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenIterator;

import org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException;
import org.ldaniels528.javapc.jbasic.common.values.Function;

/**
 * JBasic Function Manager
 * @author lawrence.daniels@gmail.com
 */
public abstract class JBasicFunctionManager {
	private final Map<String,Class> functionClasses;
	private final Map<String,Constructor> functionConstructors;
	
	/**
	 * Default Constructor
	 */
	public JBasicFunctionManager() {
		this.functionClasses 	 	= getFunctionClasses();
		this.functionConstructors = new HashMap<String,Constructor>();
	}
	
	/**
	 * Indicates whether the function exists
	 * @param funcName the name of the function
	 * @return true, if the function exists
	 */
	public boolean functionExists( String funcName ) {
		return functionClasses.containsKey( funcName );
	}
	
	/**
	 * Creates a GWBASIC function call
	 * @param funcName the name of the function
	 * @param it the given {@link TokenIterator token iterator}
	 * @return a {@link Function function} instance
	 * @throws JBasicException
	 */
	public Function callFunction( String funcName, TokenIterator it ) 
	throws JBasicException {	  	  
		// lookup the function's constructor
		final Constructor constructor = getFunctionConstructor( funcName );

		// create the function arguments
		final Object[] args = new Object[] { funcName, it };
		
		// create an instance of the function
		final Function function;		
		try {
			function = (Function)constructor.newInstance( args );
		} catch ( Exception e) {
			if( e.getCause() instanceof JBasicException ) {
				throw (JBasicException)e.getCause();
			}
			throw new JBasicException( e );
		}
		
		// return the function
		return function;
	}
	
	/** 
	 * Retrieves the constructor for the specified function
	 * @param funcName the name of the specified function
	 * @return the {@link Constructor constructor} for the specified function
	 * @throws JBasicException
	 */
	private Constructor getFunctionConstructor( String funcName ) 
	throws JBasicException {
		// if the constructor already exists, return the cached version
		if( functionConstructors.containsKey( funcName ) ) {
			return (Constructor)functionConstructors.get( funcName );
		}

		// define the constructor arguments
		final Class[] args = new Class[] { String.class, TokenIterator.class };
		
		// lookup the function class
		final Class funcClass = functionClasses.get( funcName );
		
		// get the function constructor
		Constructor constructor = null;
		try {
			constructor = funcClass.getConstructor( args );
		} 
		catch( Exception e)  {
			throw new JBasicException( e );
		}
		
		// remember this constructor
		functionConstructors.put( funcName, constructor );
		
		// return the constructor
		return constructor;
	}
	
	/**
	 * Builds a mapping of function names to function classes
	 * @return the mapping of function names to function classes
	 */
	protected abstract Map<String,Class> getFunctionClasses();
	
}
