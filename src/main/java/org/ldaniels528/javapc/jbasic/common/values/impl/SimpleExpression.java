package org.ldaniels528.javapc.jbasic.common.values.impl;

import org.ldaniels528.javapc.ibmpc.devices.memory.MemoryObject;
import org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException;
import org.ldaniels528.javapc.jbasic.common.program.JBasicCompiledCode;
import org.ldaniels528.javapc.jbasic.common.values.Expression;
import org.ldaniels528.javapc.jbasic.common.values.Value;

/**
 * Represents a BASICA/GWBASIC/QBASIC Expression
 * @author lawrence.daniels@gmail.com
 */
public class SimpleExpression implements Expression { 
	private String comparator;
	private Value value1;
	private Value value2;

	/**
	 * Creates an instance of this expression
	 * @param value1 the left value (L-value) of this expression
	 * @param comparator the comparator of this expression
	 * @param value2 the right value (R-value) of this expression
	 */
	public SimpleExpression( Value value1, String comparator, Value value2 ) {
		this.value1 		= value1;
		this.comparator	= comparator;
		this.value2 		= value2;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.values.Expression#evaluate(JBasicEnvironment)
	 */
	@SuppressWarnings("unchecked")
	public boolean evaluate( JBasicCompiledCode program ) 
	throws JBasicException {		
		// get the typed values
	    final MemoryObject tv1 = value1.getValue( program );
	    final MemoryObject tv2 = value2.getValue( program );

	    // perform the comparision
	    int result = tv1.compareTo( tv2 );

	    // equals?
	    if( comparator.equals( "=" ) ) return result == 0;
	    // greater than
	    else if( comparator.equals( ">" ) ) return result > 0;
	    // greater or equal than
	    else if( comparator.equals( ">=" ) ) return result >= 0;
	    // less than
	    else if( comparator.equals( "<" ) ) return result < 0;
	    // less than or equal to
	    else if( comparator.equals( "<=" ) ) return result <= 0;
	    // not equal to
	    else if( comparator.equals( "<>" ) ) return result != 0;
	    // invalid comparator
	    else throw new JBasicException( comparator );
	}
	
	/*
	 * (non-javadoc)
	 * @see Object#toString() 
	 */
	public String toString() {
		return value1.toString() + ' ' + comparator + ' ' + value2;
	}

}
