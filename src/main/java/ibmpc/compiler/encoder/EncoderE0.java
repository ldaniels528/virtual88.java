package ibmpc.compiler.encoder;

import static ibmpc.compiler.element.registers.X86RegisterReferences.AL;
import static ibmpc.compiler.element.registers.X86RegisterReferences.AX;
import static ibmpc.compiler.element.registers.X86RegisterReferences.DX;
import static ibmpc.compiler.encoder.X86AssemblyUtil.checkParameterCount;

import ibmpc.exceptions.X86AssemblyException;

import java.util.Arrays;
import java.util.List;

import ibmpc.compiler.InstructionEncoder;
import ibmpc.compiler.X86Instruction;
import ibmpc.compiler.element.X86DataElement;
import ibmpc.compiler.element.registers.X86RegisterReferences;
import ibmpc.compiler.element.values.X86NumericValue;
import ibmpc.compiler.element.values.X86Value;
import ibmpc.compiler.exception.X86MalformedInstructionException;
import ibmpc.util.X86CodeBuffer;

/**
 * <pre>
 * Processes IN/OUT instructions (e.g. "in al,dx", "out dx,al")
 *	---------------------------------------------------------------------------
 *	type	bits		description 			comments
 *	---------------------------------------------------------------------------
 *	i		4	 		instruction type
 *	v		1			constant value			0=constant,1=non-constant
 *	x		1			??unknown??
 *	d		1			data direction			0=IN, 1=OUT
 *	c		1	 		memory class  			8-bit=0, 16-bit=1
 *
 *  Instruction code layout
 *  -----------------------------
 *  7654 3210 (8 bits)
 *  iiii vxdc
 *  
 * ---------------------------------------------------------------------------
 * instruction				code 	iiii v x d c 
 * ---------------------------------------------------------------------------
 * loopnz nn				E0 		1110 0 0 0 0 nn
 * loopz nn					E1 		1110 0 0 0 1 nn
 * loop nn					E2 		1110 0 0 1 0 nn
 * jcxz nn					E3 		1110 0 0 1 1 nn
 * in  al,nn				E4 		1110 0 1 0 0 nn
 * in  ax,nn				E5 		1110 0 1 0 1 nn
 * out nn,al				E6 		1110 0 1 1 0 nn
 * out nn,ax				E7 		1110 0 1 1 1 nn
 * call nnnn				E8 		1110 1 0 0 0 nnnn
 * jmp nnnn					E9 		1110 1 0 0 1 nnnn
 * jmp nnnn:nnnn			EA 		1110 1 0 1 0 nnnn nnnn
 * jmp nn					EB 		1110 1 0 1 1 nn
 * in  al,dx				EC		1110 1 1 0 0
 * in  ax,dx				ED		1110 1 1 0 1
 * out dx,al				EE		1110 1 1 1 0
 * out dx,ax				EF		1110 1 1 1 1
 * </pre>
 * @author lawrence.daniels@gmail.com
 */
public class EncoderE0 implements InstructionEncoder {
	// define the direction codes
	private static final int DIRECTION_IN	= 0;
	private static final int DIRECTION_OUT	= 1;
	
	// define the instruction codes
	private static final int IN_OUT_VAL	= 0x39; // 111001
	private static final int IN_OUT_REG	= 0x3B; // 111011
	
	// define the instruction nmemonics
	private static final List<String> INSTRUCTION_NAMES_IN_OUT = 
		Arrays.asList( new String[] { "IN", "OUT" } );
	
	// define the conditional instruction nmemonics
	private static final List<String> CONDITIONAL_ITER_NAMES = 
		Arrays.asList( new String[] {
			"LOOPNE", "LOOPNZ", "LOOPE", "LOOPZ", "LOOP", "JCXZ"
		} );
	
	// define the conditional jump codes
	private static final List<Integer> CONDITIONAL_ITER_CODES = 
		Arrays.asList( new Integer[] {
			0xE0, 0xE0, 0xE1, 0xE1, 0xE2, 0xE3
		} );

	/* (non-Javadoc)
	 * @see ibmpc.compiler.neo.encoders.InstructionEncodeHelper#encode(ibmpc.compiler.encoder.X86Encoder, ibmpc.compiler.encoder.X86Instruction)
	 */
	public boolean assemble( final X86CodeBuffer encoder, final X86Instruction instruction ) 
	throws X86AssemblyException {
		// make sure it's a handled instruction
		if( !INSTRUCTION_NAMES_IN_OUT.contains( instruction.getName() ) ) return false;
		
		// get the data elements
		final X86DataElement[] elements = instruction.getParameters();
		
		// there must be two parameters
		checkParameterCount( elements, 2 );
		
		// cache the elements
		final X86DataElement elem1 = elements[0];
		final X86DataElement elem2 = elements[1];
		
		// validate the data elements
		validate( instruction.getName(), elem1, elem2 );
		
		// cache the indicators
		final boolean valueBased = isValueBased( elem1, elem2 );
		
		// build the 8-bit instruction
		// code8: iiii vxdc
		int code8;
		
		// set the instruction code
		// code8: iiii vx..
		code8 = ( ( valueBased ? IN_OUT_VAL : IN_OUT_REG ) << 2 );
		
		// set the data direction (0=IN, 1=OUT)
		// code8: iiii vxd.
		code8 |= ( ( "IN".equals( instruction.getName() ) ? 0 : 1 ) << 1 );
		
		// set the memory class (0=8-bit, 1=16-bit)
		// code8: iiii vxdc
		code8 |= ( is8Bit( elem1, elem2 ) ? 0 : 1 );
		
		// encode the instruction
		encoder.setByte( code8 );
		
		// if value-based, add the value
		if( valueBased ) {
			encoder.setByte( getByteValue( elem1, elem2 ) );
		}
		return true;
	}
	
	/**
	 * Retrieves the byte value from either element #1 or element #2
	 * @param elem1 the first {@link X86DataElement data element}
	 * @param elem2 the second {@link X86DataElement data element}
	 * @return the byte value
	 */
	private int getByteValue( final X86DataElement elem1, final X86DataElement elem2 ) {
		return elem1.isValue() ? getByteValue( elem1 ) : getByteValue( elem2 );
	}
	
	/**
	 * Retrieves the byte value from the given data element
	 * @param elem the given {@link X86DataElement data element}
	 * @return the byte value
	 */
	private int getByteValue( final X86DataElement elem ) {
		final X86NumericValue value = (X86NumericValue)elem;
		return value.getValue();
	}
	
	/**
	 * Indicates whether the given instruction code
	 * is a conditional jump instruction
	 * @param code8 the given 8-bit instruction code 
	 * @return true, of whether the given instruction code
	 * is a conditional jump instruction
	 */
	private boolean isConditionIeration( final int code8 ) {
		return CONDITIONAL_ITER_CODES.contains( code8 );
	}
	
	/**
	 * Indicates whether one of the given elements references a value
	 * @param elem1 the first {@link X86DataElement data element}
	 * @param elem2 the second {@link X86DataElement data element}
	 * @return true, if either elem1 or elem2 references a value
	 */
	private boolean isValueBased( final X86DataElement elem1, final X86DataElement elem2 ) {
		return elem1.isValue() || elem2.isValue();
	}
	
	/**
	 * Indicates whether one of the given elements is register AL.
	 * @param elem1 the first {@link X86DataElement data element}
	 * @param elem2 the second {@link X86DataElement data element}
	 * @return true, if either elem1 or elem2 is register {@link X86RegisterReferences#AL AL}
	 */
	private boolean is8Bit( final X86DataElement elem1, final X86DataElement elem2 ) {
		return AL.equals( elem1 ) || AL.equals( elem2 );
	}
	
	/**
	 * Indicates whether the given data element is a numeric value
	 * @param elem the given {@link X86DataElement data element}
	 * @return true, if the given data element is a numeric value
	 * @throws X86MalformedInstructionException
	 */
	private boolean isNumericValue( final X86DataElement elem ) 
	throws X86MalformedInstructionException {
		return elem.isValue() && ((X86Value)elem).isNumeric();
	}
	
	/**
	 * Validates the given data elements
	 * @param instructionName the given instruction's nmemonic
	 * @param elem1 the first {@link X86DataElement data element}
	 * @param elem2 the second {@link X86DataElement data element}
	 * @throws X86MalformedInstructionException
	 */
	private void validate( final String instructionName, 
						   final X86DataElement elem1, 
						   final X86DataElement elem2 ) 
	throws X86MalformedInstructionException {
		// IN instruction?
		if( "IN".equals( instructionName ) ) {
			// syntax: in  ax|al,dx|nn
			// elem1 must be AX or AL
			validateAXorAL( elem1 );
			
			// elem2 must be DX or a numeric value
			validateDXorValue( elem2 );
		}
		
		// OUT instruction
		else {
			// syntax: out dx|nn,ax|al
			// elem1 must be DX or a numeric value
			validateDXorValue( elem1 );
			
			// elem2 must be AX or AL
			validateAXorAL( elem2 );
		}
	}
	
	/**
	 * Validates that the given data element is either AX or AL
	 * @param elem the given {@link X86DataElement data element}
	 * @throws X86MalformedInstructionException
	 */
	private void validateAXorAL( final X86DataElement elem ) 
	throws X86MalformedInstructionException {
		if( !AX.equals( elem ) && !AL.equals( elem ) ) {
			throw new X86MalformedInstructionException( "Register AX or AL expected near '" + elem + "'" );
		}
	}
	
	/**
	 * Validates that the given data element is either AX or AL
	 * @param elem
	 * @throws X86MalformedInstructionException
	 */
	private void validateDXorValue( final X86DataElement elem ) 
	throws X86MalformedInstructionException {
		if( !DX.equals( elem ) && !isNumericValue( elem ) ) {
			throw new X86MalformedInstructionException( "Register DX or an integer value expected near '" + elem + "'" );
		}
	}

}
