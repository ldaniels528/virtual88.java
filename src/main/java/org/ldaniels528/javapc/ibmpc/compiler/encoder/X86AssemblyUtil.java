package org.ldaniels528.javapc.ibmpc.compiler.encoder;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ldaniels528.javapc.ibmpc.compiler.element.X86DataElement;
import org.ldaniels528.javapc.ibmpc.compiler.element.addressing.X86MemoryAddress;
import org.ldaniels528.javapc.ibmpc.compiler.element.addressing.X86MemoryPointer;
import org.ldaniels528.javapc.ibmpc.compiler.element.registers.X86RegisterRef;
import org.ldaniels528.javapc.ibmpc.compiler.exception.X86InvalidNumberOfParametersException;
import org.ldaniels528.javapc.ibmpc.compiler.exception.X86MalformedInstructionException;

/**
 * 80x86 Instruction Encode Utility
 * @author lawrence.daniels@gmail.com
 */
public class X86AssemblyUtil {

	/**
	 * Validates that the expected number of parameters exist
	 * @param elems the given {@link X86DataElement data elements}
	 * @param expectedCount the expected number of parameters
	 * @throws X86InvalidNumberOfParametersException
	 */
	public static void checkParameterCount( final X86DataElement[] elems, final int expectedCount ) 
	throws X86InvalidNumberOfParametersException {
		if( elems.length != expectedCount ) {
			throw new X86InvalidNumberOfParametersException( expectedCount, elems.length );
		}
	}
	
	/**
	 * Validates whether the given data element is an 8-bit register
	 * @param elem the given {@link X86DataElement data element}
	 * @return true, if the given data element is an 8-bit register
	 */
	public static X86RegisterRef getRegister( final X86DataElement elem ) {
		if( elem.isRegister() ) {
			final X86RegisterRef register = (X86RegisterRef)elem;
			return register;
		}
		
		return null;
	}
	
	/**
	 * Validates whether the given data element is a general purpose register
	 * @param elem the given {@link X86DataElement data element}
	 * @return true, if the given data element is an 8-bit register
	 */
	public static boolean isGeneralPurposeRegister( final X86DataElement elem ) {
		if( elem.isRegister() ) {
			final X86RegisterRef register = (X86RegisterRef)elem;
			return register.is8BitGeneralPurposeRegister() || register.is16BitGeneralPurposeRegister();
		}
		
		return false;
	}
	
	/**
	 * Generates the 8-bit code that specifies the appropriate
	 * source and target references.
	 * <pre>
	 * Code = mm rrr mmm
	 * 		r = register reference (e.g. "ax") [3 bits]
	 * 		m = memory reference (e.g. "[bx+si]") [5 bits]
	 * </pre>
	 * @return the code that specifies the appropriate
	 * source and target references.
	 */
	public static int generateReferenceCode( final int register, final int memref ) {
		// compute the register portion of the code (masking off unneeded bits)
		// Code = mm rrr mmm
		// 		r = register reference (e.g. "ax") [3 bits]
		// 		m = memory reference (e.g. "[bx+si]") [5 bits]
		
		// shift the register bits to the center, and AND off the rest
		final int rrr = ( register << 3 ) & 0x0038; // 38h = 00111000b 	
		
		// AND off the center bits (bits 3, 4, and 5)
		final int mmm = memref & 0xC7; // C7h = 11000111b
	
		// combine the memory reference and regitser codes 
		final int byteCode = mmm | rrr;
		
		// return the code
		return byteCode; 
	}
	
	/**
	 * Determines whether the given data element is 8-bit
	 * @param element the given {@link X86DataElement data element}
	 * @return true, if the data element is 8-bit
	 * @throws X86MalformedInstructionException 
	 */
	public static boolean is8Bit( final X86DataElement element ) 
	throws X86MalformedInstructionException {
		// is it a register?
		if( element.isRegister() ) {
			final X86RegisterRef register = (X86RegisterRef)element;
			return register.is8BitGeneralPurposeRegister();
		}
		
		// is it a memory reference?
		else if( element.isMemoryReference() ) {
			final X86MemoryAddress memoryAddress = (X86MemoryAddress)element;
			if( memoryAddress.isMemoryPointer() ) {
				final X86MemoryPointer memoryPointer = (X86MemoryPointer)memoryAddress;
				return memoryPointer.is8Bit();
			}
		}
	
		// anything else ...
		throw new X86MalformedInstructionException( "Unhandled data element '" + element + "'" );
	}

	/**
	 * Determines the appropriate element order
	 * @param elem1 {@link X86DataElement data element} #1
	 * @param elem2 {@link X86DataElement data element} #2
	 * @return the appropriate element order
	 * @throws X86MalformedInstructionException 
	 */
	public static int[] getElementOrder( final X86DataElement elem1, final X86DataElement elem2 ) 
	throws X86MalformedInstructionException {
		final int primary;
		final int seconary;
		
		// if: elem1 is a register and elem2 is a register
		if( elem1.isRegister() && elem2.isRegister() ) {
			primary = elem1.getSequence();
			seconary = elem2.getSequence();
		}		
		
		// if: elem1 is a register and elem2 is a reference ...
		else if( elem1.isRegister() && elem2.isMemoryReference() ) {
			primary = elem1.getSequence();
			seconary = elem2.getSequence();
		}
		
		// if: elem1 is a reference and elem2 is a register ...
		else if( elem1.isMemoryReference() && elem2.isRegister()  ) {
			primary = elem2.getSequence();
			seconary = elem1.getSequence();
		}
		
		else {
			throw new X86MalformedInstructionException( 
					"Unrecognized arguments (element #1 is " + elem1 + ", element #2 is " +  elem2 + ")"
			);
		}
		
		return new int[] { primary, seconary };
	}
	
	/**
	 * Builds an instruction nmemonics to instruction byte code mapping.
	 * @return the {@link Map mapping}
	 */
	public static Map<String,Integer> createInstructionMapping( final String[] INSTRUCTION_NAMES, 
															 	final int[] INSTRUCTION_CODES ) {
		final Map<String,Integer> mapping = new HashMap<String,Integer>();
		for( int n = 0; n < INSTRUCTION_NAMES.length; n++ ) {
			mapping.put( INSTRUCTION_NAMES[n], INSTRUCTION_CODES[n] );
		}
		return mapping;
	}

	/**
	 * Builds an instruction nmemonics to instruction byte code mapping.
	 * @return the {@link Map mapping}
	 */
	public static Map<String,Integer> createInstructionMapping( final List<String> INSTRUCTION_NAMES, 
																final List<Integer> INSTRUCTION_CODES ) {
		final Map<String,Integer> mapping = new HashMap<String,Integer>();
		for( int n = 0; n < INSTRUCTION_NAMES.size(); n++ ) {
			mapping.put( INSTRUCTION_NAMES.get(n), INSTRUCTION_CODES.get(n) );
		}
		return mapping;
	}
	
}
