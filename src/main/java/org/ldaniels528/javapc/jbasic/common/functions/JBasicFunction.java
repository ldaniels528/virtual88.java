package org.ldaniels528.javapc.jbasic.common.functions;

import org.ldaniels528.javapc.ibmpc.devices.memory.MemoryObject;

import java.util.ArrayList;
import java.util.Collection;

import org.ldaniels528.javapc.jbasic.common.JBasicCompiledCodeReference;
import org.ldaniels528.javapc.jbasic.common.exceptions.IllegalFunctionCallException;
import org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException;
import org.ldaniels528.javapc.jbasic.common.exceptions.SyntaxErrorException;
import org.ldaniels528.javapc.jbasic.common.exceptions.TypeMismatchException;
import org.ldaniels528.javapc.jbasic.common.values.Function;
import org.ldaniels528.javapc.jbasic.common.values.Value;
import org.ldaniels528.javapc.jbasic.common.values.VariableReference;
import org.ldaniels528.javapc.jbasic.gwbasic.values.GwBasicValues;

import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenIterator;

/**
 * Represents a basis for all Built-in GWBASIC Functions
 */
public abstract class JBasicFunction implements Function {
	// parameter constants
	public static final int TYPE_VAR 	 	= 0;
	public static final int TYPE_ANY 	 	= 1;
	public static final int TYPE_STRING  	= 2;
	public static final int TYPE_NUMERIC 	= 3;
	// fields
    private final int[] paramTypes; 
    private final Value[] values;
    private final String name; 

    ////////////////////////////////////////////////////////////
    //      Constructor(s)
    ////////////////////////////////////////////////////////////
    
    /**
     * Creates an instance of this {@link JBasicFunction function}
     * @param name the name of the function
     * @param it the given {@link TokenIterator iterator}
     * @param paramType the type of the expected parameter
     * @throws TypeMismatchException
     */
    protected JBasicFunction( final String name, 
    							 final TokenIterator it, 
    							 final int paramType )
    throws JBasicException {
    	this( name, it, new int[] { paramType } );
    }

    /**
     * Creates an instance of this {@link JBasicFunction function}
     * @param name the name of the function
     * @param it the given {@link TokenIterator iterator}
     * @param paramTypes the parameters types to expect
     * @throws TypeMismatchException
     */
    protected JBasicFunction( final String name, 
    							 final TokenIterator it, 
    							 final int[] paramTypes )
    throws JBasicException {
		this.name 		= name;
    	this.paramTypes = paramTypes;
    	this.values     = determineFixedValues( it, paramTypes );    		
    }
    
    /**
     * Creates an instance of this {@link JBasicFunction function}
     * @param name the name of the function
     * @param it the given {@link TokenIterator iterator}
     * @param paramTypes the parameters types to expect 
     * @param minParams the minimum number of parameters that must exist
     * @throws TypeMismatchException
     */
    protected JBasicFunction( final String name, 
    						  final TokenIterator it, 
    						  final int[] paramTypes, 
    						  final int minParams )
    throws JBasicException {
		this.name 		= name;
    	this.paramTypes = paramTypes;
    	this.values     = determineVariableValues( it, minParams, paramTypes );    		
    }

    ////////////////////////////////////////////////////////////
    //      Service Method(s)
    ////////////////////////////////////////////////////////////
    
    /* 
     * (non-javadoc)
     * @see Function#getName()
     */
    public String getName() {
    	return name;
    }
    
    /* 
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
    	return getName();
    }
    
    /**
     * Parses the given token iterator for values
     * @param it the given {@link TokenIterator token iterator}
     * @return the resultant {@link Value value}
     * @throws JBasicException
     */
    protected Value parseValues( TokenIterator it ) 
    throws JBasicException {
    	return GwBasicValues.getValueReference( it );
    }
    
    /**
     * Returns the values contained by this function after validating
     * each values' parameter type.
     * @param program the currently running {@link JBasicCompiledCodeReference program}
     * @return the array {@link MemoryObject values} contained by this function
     */
    protected MemoryObject[] getParameterValues( JBasicCompiledCodeReference program ) {
		// can't have more values than parameter types
		if( values.length > paramTypes.length )
			throw new IllegalFunctionCallException();
	
		// create an array of objects to return
		final MemoryObject[] objects = new MemoryObject[ values.length ];
	
		// check each value
		for( int i = 0; i < values.length; i++ ) {
			final Value value = values[i];
			
			// get the object for the current value
			objects[i] = value.getValue( program ); 
			
			// cache the object
			final MemoryObject object = objects[i];
				
			// validate the object's type
			switch( paramTypes[i] ) {    				
				case TYPE_ANY: break;
				
				case TYPE_VAR: if( !( value instanceof VariableReference ) )
								throw new TypeMismatchException( object );
							  break;
				
				case TYPE_NUMERIC: if( !object.isNumeric() )
									throw new TypeMismatchException( object );
								   break;
								   
				case TYPE_STRING: if( !object.isString() )
									throw new TypeMismatchException( object );
					   			 break;
			}
		}
		
		return objects;
    }
    
    ////////////////////////////////////////////////////////////
    //      Internal Service Method(s)
    ////////////////////////////////////////////////////////////
    
    /**
     * Retrieves a fixed number of values based on the given parameter types
     * @param it the given {@link TokenIterator token iterator}
     * @param paramTypes the given parameter types
     */
    private Value[] determineFixedValues( final TokenIterator it, 
    									    final int[] paramTypes ) 
    throws JBasicException {
    		// create a fixed number of values
    		Value[] values = new Value[paramTypes.length];

    		// must have the opening parenthesis
        GwBasicValues.mandateToken( it, "(" );
        
		// parse the parameters
        for( int index = 0; index < paramTypes.length; index++ ) {
        		values[index] = parseValues( it );
        		if( index+1 < paramTypes.length ) GwBasicValues.mandateToken( it, "," );
        }
        
		// must have the closing parenthesis
        GwBasicValues.mandateToken( it, ")" );
        
        // return the values
        return values;
    }
    
    /**
     * Retrieves a fixed number of values based on the given parameter types
     * @param it the given {@link TokenIterator token iterator}
     * @param minParams the minimum number of parameters required
     * @param paramTypes the given parameter types
     */
    private Value[] determineVariableValues( final TokenIterator it, 
    										   final int minParams, 
    										   final int[] paramTypes ) 
    throws JBasicException {
		// create a container for a dynamic number of values
		Collection<Value> values = new ArrayList<Value>( paramTypes.length );
	
		// if parameters were specified
		if( it.hasNext() ) {
        // expect an opening parenthesis
    		GwBasicValues.mandateToken( it, "(" );      
            		
    		// iterate the tokens until the parameters have been collected
    		boolean complete = false;
    		while( it.hasNext() && !complete ) {
    			// if there are more values then parameter types, error...
    			if( values.size() > paramTypes.length )
    				throw new SyntaxErrorException();
    			
      	    // get the next value
        		values.add( GwBasicValues.getValueReference( it ) );   
	          
	  	    // check before each value to make sure there's a comma (except the first one)
	  	    if( it.hasNext() ) {
	  	    		// get the next element
	  	    		final String nextToken = it.next();
	  	    		
	  	    		// is it an argument separator?
	  	    		if( nextToken.equals( "," ) ) {
	  	    			// do nothing
	  	    		}
	  	    		
	  	    		// is it the end of the function call?
	  	    		else if( nextToken.equals( ")" ) ) {
	  	    			complete = true;
	  	    		}
	  	    		
	  	    		// unknown parameter/delimiter
	  	    		else throw new SyntaxErrorException(); 
  	    		}
        }
		}
    
		// insure the minimum number of parameters was reached
		if( ( values.size() < minParams ) )
			throw new SyntaxErrorException();
		
		// return the values
		return values.toArray( new Value[ values.size() ] );
    }
    
}
