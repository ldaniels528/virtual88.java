package jbasic.common.values.impl;


import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import jbasic.common.exceptions.JBasicException;
import jbasic.common.program.JBasicCompiledCode;
import jbasic.common.values.Expression;

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
	 * @see jbasic.values.Expression#evaluate(JBasicEnvironment)
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
