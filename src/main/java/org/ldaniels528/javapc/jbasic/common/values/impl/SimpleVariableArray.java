package org.ldaniels528.javapc.jbasic.common.values.impl;

import org.ldaniels528.javapc.ibmpc.devices.memory.MemoryManager;
import org.ldaniels528.javapc.ibmpc.devices.memory.MemoryObject;

import java.util.HashMap;
import java.util.Map;

import org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException;
import org.ldaniels528.javapc.jbasic.common.exceptions.SubscriptOutOfRangeException;
import org.ldaniels528.javapc.jbasic.common.program.JBasicCompiledCode;
import org.ldaniels528.javapc.jbasic.common.values.Variable;
import org.ldaniels528.javapc.jbasic.common.values.VariableArray;

/**
 * Represents a BASICA/GWBASIC/QBASIC Variable Array
 */
public class SimpleVariableArray implements VariableArray {
	private final Map<Integer,SimpleVariable> variables;
	private final JBasicCompiledCode compiledCode;
	private final MemoryManager memoryManager;		
	private final String name;
	private final int offset;
	private final int length;
	private final int size;
	  
	//////////////////////////////////////////////////////
	//    Constructor(s)
	//////////////////////////////////////////////////////

	/**
	 * Creates an instance of this {@link VariableArray array}
	 * @param program the given {@link MemoryManager memory manager}
	 * @param name the name of this array
	 * @param length the size of this array
	 * @param offset the offset of the memory block allocated to this array
	 * @param size the size of the allocated memory block
	 */
	public SimpleVariableArray( JBasicCompiledCode compiledCode, 
							   String name, 
							   int length, 
							   int offset, 
							   int size ) {
		this.compiledCode	= compiledCode;
		this.memoryManager	= compiledCode.getMemoryManager();
		this.name      		= name;		  		  
		this.length	   		= length;
		this.offset			= offset;
		this.size			= size;
		this.variables		= new HashMap<Integer,SimpleVariable>( length );
	}

	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.base.values.ReferencedEntity#getName()
	 */
	public String getName() {
		return name;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.base.values.VariableArray#length()
	 */
	public int length() {
		return length;
	}
	  
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.base.values.VariableArray#destroy()
	 */
	public void destroy() {
		memoryManager.deallocate( offset, size );
	}
	  
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.values.VariableArray#getValue(int, org.ldaniels528.javapc.jbasic.environment.JBasicEnvironment)
	 */
	public MemoryObject getValue( int index, JBasicCompiledCode program )
	throws JBasicException {
		// lookup the variable
		final Variable variable = getElement( index );
		
		// get the variable's content
		final MemoryObject object = variable.getValue( program );
		  
		// return the content
		return object;
	}
	  
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.values.VariableArray#setValue(int,org.ldaniels528.javapc.jbasic.values.types.JBasicObject)
	 */
	public void setValue( int index, MemoryObject value ) throws JBasicException {
		// lookup the variable
		final Variable variable = getElement( index );
		  
		// set the variable
		variable.setValue( value );
	}
	  
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.jbasic.base.values.VariableArray#getElement(int)
	 */
	public Variable getElement( int index ) 
	throws SubscriptOutOfRangeException {
		// check the array bounds
	    if( index < 1 || index > length )
	    		throw new SubscriptOutOfRangeException( name, index );
	    
	    // has the variable already been initialized?
	    if( variables.containsKey( index ) ) return variables.get( index );
	    else {
		    // get the index pointer
		    final int pointer = 2 * ( index - 1 ) + offset;
		    
		    // create a memory reference
		    final MemoryObject memoryObject = compiledCode.wrapObject( name, pointer );
		    
		    // create a variable reference
		    final SimpleVariable variable = new SimpleVariable( name, memoryObject );
		    
		    // add the variable to the map
		    variables.put( index, variable );
		    
		    // return the variable at the given index
		    return variable;
	    }
	}

	/**
	 * @return a string representation of this array
	 */
	public String toString() {
		return new StringBuilder( name )
				.append( '(' )
				.append( length )
				.append( ')' )
				.toString();
	}
	  
}
