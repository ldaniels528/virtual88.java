package jbasic.gwbasic.functions.system;

import ibmpc.devices.memory.IbmPcRandomAccessMemory;
import ibmpc.devices.memory.MemoryObject;
import ibmpc.devices.memory.NumberMemoryObject;

import jbasic.common.tokenizer.TokenIterator;

import jbasic.common.JBasicCompiledCodeReference;
import jbasic.common.exceptions.JBasicException;
import jbasic.common.functions.JBasicFunction;
import jbasic.common.values.types.impl.JBasicTempNumber;
import jbasic.gwbasic.values.GwBasicValues;

/**
 * PEEK Function
 * <br>Syntax: PEEK(<i>offset</i>)
 * <br>Example: x = PEEK(&HB800)
 * @see jbasic.gwbasic.program.commands.io.PokeCommand
 * @author lawrence.daniels@gmail.com
 */
public class PeekFunction extends JBasicFunction {

	/**
	 * Creates an instance of this function
	 * @param it the given {@link TokenIterator token iterator}
	 * @throws JBasicException
	 */
	public PeekFunction( String name, TokenIterator it ) throws JBasicException {
		super( name, it, TYPE_NUMERIC );
	}

	/* 
	 * (non-Javadoc)
	 * @see jbasic.values.Value#getValue(jbasic.environment.JBasicEnvironment)
	 */
	public MemoryObject getValue( JBasicCompiledCodeReference program ) {
		// get the parameter values
		final MemoryObject[] objects = getParameterValues( program );
		
		// get the physical segment
		final int segment = GwBasicValues.getUnsignedInt( program.getDefaultMemorySegment() );
		
		// get the physical offset
		final int offset = GwBasicValues.getUnsignedInt( objects[0].toInteger() );
		
		// read the byte from memory at segment:offset
		final IbmPcRandomAccessMemory memory = program.getSystem().getRandomAccessMemory();
		final int value = memory.getByte( segment, offset );
		
		// return the byte
		final NumberMemoryObject number = new JBasicTempNumber( value );
		return number;
	}

}
