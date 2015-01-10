package ibmpc.compiler.encoder;

import static ibmpc.compiler.encoder.X86AssemblyUtil.checkParameterCount;

import ibmpc.exceptions.X86AssemblyException;

import java.util.Arrays;
import java.util.List;

import ibmpc.compiler.InstructionEncoder;
import ibmpc.compiler.X86Instruction;
import ibmpc.compiler.element.X86DataElement;
import ibmpc.compiler.element.values.X86ByteValue;
import ibmpc.compiler.element.values.X86NumericValue;
import ibmpc.compiler.element.values.X86Value;
import ibmpc.compiler.exception.X86InvalidNumberOfParametersException;
import ibmpc.compiler.exception.X86MalformedInstructionException;
import ibmpc.util.X86CodeBuffer;

/**
 * <pre>
 * Process return and interrupt instructions (e.g. 'RETF', 'INT &H21')
 *	---------------------------------------------------------------------------
 *	type	bits		description 			comments
 *	---------------------------------------------------------------------------
 *	i		6	 		instruction type
 *	j		2			instruction sub type	00/11=N/A, 01=8-bit, 10=16-bit
 *
 *  Instruction code layout
 *  -----------------------------
 *  7654 3210 (8 bits)
 *  iiii iijj
 *  
 * ---------------------------------------------------------------------------
 * instruction				code 		tt rrr mmm	iiiiii jj dddd
 * ---------------------------------------------------------------------------
 * (undefined)				  C0					110000 00
 * (undefined)				  C1					110000 01
 * ret nnnn					  C2 nnnn				110000 10 nnnn
 * ret						  C3	 				110000 11
 * les	ax,[bx]				07C4		00 000 111 	110001 00
 * lds	ax,[si+nn]			44C5		01 000 100 	110001 01 nn
 * mov byte ptr [bx],nn		07C6nn		00 000 111	110001 10 nn
 * mov word ptr [bx],nnnn	07C7nnnn	00 000 111	110001 11 nnnn
 * retf nnnn				  CA nnnn				110010 10 nnnn
 * retf						  CB 					110010 11
 * int 3					  CC					110011 00
 * int nn					  CD nn					110011 01 nn
 * into						  CE					110011 10
 * iret						  CF					110011 11
 * </pre>
 * @author lawrence.daniels@gmail.com
 */
public class EncoderC0 implements InstructionEncoder {
	private static final String MOV_INSTR = "MOV";
	private static final int MOV_TYPE4 = 0x0C; // 1100
	
	private static final int LEA_CODE 	= 0x23;	// 100011
	private static final int LES_CODE 	= 0x31; // 110001
	private static final int LDS_CODE 	= 0x31; // 110001
	
	// define byte value of 3
	private static final X86ByteValue THREE = new X86ByteValue( 3 );
	
	// define the instruction codes
	private static final int RET_CODE	= 0x30; // 110000b
	private static final int RETF_CODE	= 0x32; // 110010b
	private static final int INTR_CODE	= 0x33; // 110011b
	
	// define the RET/RETF instruction sub-code
	private static final int RET_SUBCODE 	= 3; // 11b
	private static final int RET_NN_SUBCODE = 2; // 10b
	
	// define the INT/INTO/IRET instruction sub-code
	private static final int INT3_SUBCODE	= 0; // 00b
	private static final int INTR_SUBCODE	= 1; // 01b
	private static final int INTO_SUBCODE	= 2; // 10b
	private static final int IRET_SUBCODE	= 3; // 11b
	
	// define the instruction codes
	private static final List<Integer> INSTRUCTION_CODES = 
		Arrays.asList( new Integer[] { 
				INTR_CODE, INTR_CODE, INTR_CODE, INTR_CODE, RET_CODE, RETF_CODE, 
				LEA_CODE, LES_CODE, LDS_CODE
		} );
	
	// define the instruction nmemonics
	private static final List<String> INSTRUCTION_NAMES = 
		Arrays.asList( new String[] { 
				"INT 3", "INT", "INTO", "IRET", "RET", "RETF",
				"LEA", "LES", "LDS"
		} );

	/* (non-Javadoc)
	 * @see ibmpc.compiler.neo.encoders.X86EncodeHelper#encode(ibmpc.compiler.encoder.X86Encoder, ibmpc.compiler.encoder.X86Instruction)
	 */
	public boolean assemble( final X86CodeBuffer encoder, final X86Instruction instruction ) 
	throws X86AssemblyException {
		//  Instruction code layout
		//  -----------------------------
		//  7654 3210 (8 bits)
		//  iiii iijj
		 
		// make sure it's a handled instruction
		if( !INSTRUCTION_NAMES.contains( instruction.getName() ) ) return false;
		
		// determine the instruction code
		final int insCode = determineInstructionCode( instruction );
		
		// determine the instruction sub code
		final int subCode = determineInstructionSubCode( instruction, insCode );
		
		// create the 8/16-bit instruction
		int code8;
		
		// set the instruction code
		// code: iiii ii.. (6/8 bits)
		code8 = ( insCode << 2 );
		
		// set the instruction sub-code
		// code: iiii iijj (8/8 bits)
		code8 |= subCode;
		
		// encode the instruction
		encoder.setByte( code8 );
		
		// encode the interrupt number?
		if( ( insCode == INTR_CODE ) && ( subCode == INTR_SUBCODE ) ) {
			final X86DataElement[] params = instruction.getParameters();
			encoder.setByte( getNumericValue( params, true ) );
		}
		
		// encode the return number?
		else if( ( insCode == RET_CODE || insCode == RETF_CODE ) && 
				( subCode == RET_NN_SUBCODE ) ) {
			final X86DataElement[] params = instruction.getParameters();
			encoder.setWord( getNumericValue( params, false ) );
		}
		
		return true;
	}
	
	/**
	 * Determines the instruction code for the given instruction
	 * @param instruction the given {@link X86Instruction instruction}
	 * @return the instruction code
	 */
	private int determineInstructionCode( final X86Instruction instruction ) {
		// lookup the instruction code via the instruction mnemonic 
		final int index = INSTRUCTION_NAMES.indexOf( instruction.getName() );
		
		// return the instruction code
		return INSTRUCTION_CODES.get( index );
	}
	
	/**
	 * Determines the instruction sub-code for the given instruction
	 * @param instruction the given {@link X86Instruction instruction}
	 * @param insCode the given instruction code
	 * @return the instruction sub-code
	 */
	private int determineInstructionSubCode( final X86Instruction instruction, final int insCode ) 
	throws X86InvalidNumberOfParametersException {
		int subCode = 0;
		
		// get the instruction parameters
		final X86DataElement[] params = instruction.getParameters();
		
		// perform instruction code specific tests
		switch( insCode ) {
			// RET/RETF instructions
			case RET_CODE:
			case RETF_CODE:
				// can't be more than 1 parameter
				if( params.length > 1 )
					throw new X86InvalidNumberOfParametersException( 1, params.length );
				
				// set the sub-code
				subCode = ( params.length == 1 ) ? RET_NN_SUBCODE : RET_CODE;
				break;
			
			// INT/INTO/IRET
			case INTR_CODE:
				// lookup the instruction sub-code via the instruction mnemonic 
				subCode = INSTRUCTION_NAMES.indexOf( instruction.getName() );
				
				// validate the parameters
				checkParameterCount( params, ( subCode <= INTR_SUBCODE ) ? 1 : 0 );
				
				// if it's an "INT xx" instruction?
				if( subCode == INTR_SUBCODE && params[0].equals( THREE ) ) {
					subCode = INT3_SUBCODE;
				}
				break;
		}
		
		// return the sub code
		return subCode;
	}
	
	/**
	 * Retrieves the interrupt number from the given parameters
	 * @param params the given {@link X86DataElement parameters}
	 * @param is8bit indicates whether the value must be 8-bit
	 * @return the interrupt number
	 * @throws X86MalformedInstructionException
	 */
	private int getNumericValue( final X86DataElement[] params, boolean is8bit ) 
	throws X86MalformedInstructionException {
		// the data element must be a value
		final X86DataElement elem = params[0];
		if( !elem.isValue() ) {
			throw new X86MalformedInstructionException( "Numeric value expected at '" + elem + "'" );
		}
		
		// the data element must be a numeric value
		final X86Value value = (X86Value)elem;
		if( !value.isNumeric() ) {
			throw new X86MalformedInstructionException( "Numeric value expected at '" + elem + "'" );	
		}
		
		// cast to a numeric value
		final X86NumericValue numericValue = (X86NumericValue)value;
		if( is8bit && !numericValue.is8Bit() ) {
			throw new X86MalformedInstructionException( "Byte value (8-bit) expected at '" + elem + "'" );
		}
		
		// return the byte value
		return numericValue.getValue();
	}

}