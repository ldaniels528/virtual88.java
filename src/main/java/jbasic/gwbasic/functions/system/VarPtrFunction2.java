package jbasic.gwbasic.functions.system;

import ibmpc.devices.memory.MemoryObject;
import ibmpc.devices.memory.StringMemoryObject;
import jbasic.common.JBasicCompiledCodeReference;
import jbasic.common.exceptions.JBasicException;
import jbasic.common.exceptions.NeverShouldHappenException;
import jbasic.common.functions.JBasicFunction;
import jbasic.common.values.Value;
import jbasic.common.values.types.impl.JBasicNumber;
import jbasic.common.values.types.impl.JBasicTempString;
import jbasic.gwbasic.values.GwBasicValues;

import com.ldaniels528.tokenizer.TokenIterator;

/**
 * VARPTR$ Function
 * <br>Purpose: To return a character form of the offset of a variable in memory.
 * <br>Syntax: VARPTR$(<i>variable</i>)
 * @author lawrence.daniels@gmail.com
 */
public class VarPtrFunction2 extends JBasicFunction {
	
    /**
     * Creates an instance of this opCode
     * @param it the given {@link TokenIterator token iterator}
     * @throws jbasic.common.exceptions.TypeMismatchException
     */
    public VarPtrFunction2( String name, TokenIterator it ) 
    throws JBasicException {
    		super( name, it, TYPE_VAR ); 
    }

    /* 
     * (non-Javadoc)
     * @see jbasic.values.Value#getValue(jbasic.environment.JBasicEnvironment)
     */
    public MemoryObject getValue( JBasicCompiledCodeReference program ) {
		// get the parameter values
		final MemoryObject[] objects = getParameterValues( program );
		
		// identify the object we're using
		final MemoryObject object = objects[0];
		
		// create a string representing return value
		final StringBuffer buffer = new StringBuffer( 3 );
		buffer.append( getDataType( object ) );
		buffer.append( getLeastSignificantByte( object ) );
		buffer.append( getMostSignificantByte( object ) );
		
		// return the square root
		final StringMemoryObject string = new JBasicTempString();
		string.setString( buffer.toString() );
		return string;
    }
    
    /* 
     * (non-Javadoc)
     * @see jbasic.gwbasic.values.functions.GwBasicInternalFunction#lookupValue(jbasic.tokenizer.TokenIterator)
     */
    protected Value parseValues( TokenIterator it ) 
    throws JBasicException {
    		return GwBasicValues.getVariableReference( it );
    }
    
    /**
     * Returns the data type of the given object
     * @param object the given {@link MemoryObject object}
     * @return '2'=integer, '3'=string, '4'=single precision, '8'=double precision 
     * @throws NeverShouldHappenException 
     */
    private char getDataType( final MemoryObject object ) 
    throws NeverShouldHappenException {
    		if( object.isString() ) return (char)3;
    		else {
    			JBasicNumber number = (JBasicNumber)object;
    			if( number.isInteger() ) return (char)2;
    			else if( number.isSinglePrecision() ) return (char)4;
    			else if( number.isDoublePrecision() ) return (char)8;
    			else throw new NeverShouldHappenException();
    		}
    }
    
    /**
     * Returns the least significant byte of the object
     * @param object the given {@link MemoryObject object}
     * @return the least significant byte of the object
     */
    private char getLeastSignificantByte( final MemoryObject object ) {
		final String string = object.toString();
		final char digit = string.charAt( 0 );		
		return digit;
    }
    
    /**
     * Returns the most significant byte of the object
     * @param object the given {@link MemoryObject object}
     * @return the most significant byte of the object
     */
    private char getMostSignificantByte( final MemoryObject object ) {
    		final String string = object.toString();
    		final char digit = string.charAt( 0 );
    		return digit;
    }

}
