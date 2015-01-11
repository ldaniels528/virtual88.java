package org.ldaniels528.javapc.jbasic.common.values.impl;


import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException;
import org.ldaniels528.javapc.jbasic.common.program.JBasicCompiledCode;
import org.ldaniels528.javapc.jbasic.common.values.Expression;

/**
 * Represents a set of expressions
 * @author lawrence.daniels@gmail.com
 */
public class SimpleExpressionSet implements Expression {
	private Collection<Expression> expressions;
	
	/**
	 * Default constructor
	 */
	public SimpleExpressionSet() {
		this.expressions = new LinkedList<Expression>();
	}
	
	/**
	 * Attaches an expression to this set
	 * @param expression the given {@link Expression expression}
	 */
	public void add( Expression expression ) {
		expressions.add( expression );
	}
	
	/* (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.values.Expression#evaluate(JBasicEnvironment)
	 */
	public boolean evaluate( JBasicCompiledCode program ) throws JBasicException {
	    for( Iterator<Expression> it = expressions.iterator(); it.hasNext(); ) {
	    		Expression expression = it.next();
	    		if( !expression.evaluate( program ) ) return false;
	    }
	    return true;
	}
	
	/* (non-javadoc)
	 * @see Object#toString() 
	 */
	public String toString() {
		return expressions.toString();
	}

}
