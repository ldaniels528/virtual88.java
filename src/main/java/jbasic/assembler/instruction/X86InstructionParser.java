/**
 * 
 */
package jbasic.assembler.instruction;

import static jbasic.assembler.instruction.element.addressing.X86ReferencedAddressParser.isReference;
import static jbasic.assembler.instruction.element.addressing.X86ReferencedAddressParser.parseReference;
import static jbasic.assembler.instruction.element.registers.X86RegisterReferences.isRegister;
import static jbasic.assembler.instruction.element.registers.X86RegisterReferences.lookupRegister;
import static jbasic.assembler.instruction.element.values.X86Value.isNumeric;
import static jbasic.assembler.instruction.element.values.X86Value.isString;
import static jbasic.assembler.instruction.element.values.X86Value.parseValue;
import static jbasic.assembler.util.X86CompilerUtil.mandateElement;
import static jbasic.assembler.util.X86CompilerUtil.nextElement;
import ibmpc.exceptions.X86AssemblyException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jbasic.assembler.instruction.element.X86DataElement;
import jbasic.assembler.instruction.exception.X86MalformedInstructionException;

import com.ldaniels528.tokenizer.TokenIterator;

/**
 * Represents a 80x86 assembly language instruction parser
 * @author lawrence.daniels@gmail.com
 */
public class X86InstructionParser {
	private static final List<String> QUANTIFIERS = Arrays.asList( 
			new String[] { "byte ptr", "word ptr", "word", "dword", "qword" }
		);
	
	/**
	 * Parses the given assembly code string into an x86 instruction object
	 * @param it the {@link TokenIterator tokenizer} containing the assembly code string
	 * @return an {@link X86Instruction x86 instruction} object
	 * @throws X86AssemblyException 
	 */
	public static X86Instruction parse( final TokenIterator it ) 
	throws X86AssemblyException {
		// get the instruction's name
		final String name = nextElement( it );
		
		// if there are no other elements, just return
		if( !it.hasNext() ) {
			return X86Instruction.create( name );
		}
		
		// create a container for the parameters
		final List<X86DataElement> params = new ArrayList<X86DataElement>( 3 );
		
		// gather the parameters
		while( it.hasNext() ) {
			// get the next data element from the iterator
			params.add( nextDataElement( it ) );
			
			// is there another element
			if( it.hasNext() ) {
				mandateElement( it, "," );
			}
		}
		
		// return a new instruction with the list of paramters
		return X86Instruction.create( name, params );
	}
	
	/**
	 * Converts the given token iterator to a data element.
	 * @param it the given {@link TokenIterator token iterator}
	 * @return the corresponding {@link X86DataElement data element}
	 * @throws X86AssemblyException
	 */
	private static X86DataElement nextDataElement( final TokenIterator it ) 
	throws X86AssemblyException {
		// get the next token
		final String token = nextElement( it );
		
		// is it a quantifier (e.g. 'byte ptr')?
		if( QUANTIFIERS.contains( token ) ) {
			throw new X86MalformedInstructionException( "'" + token + "' is not yet implemented" );
		}
		
		// is it a numeric or string value?
		else if( isNumeric( token ) || isString( token ) ) {
			 return parseValue( token );
		}
		
		// is it a register?
		else if( isRegister( token ) ) {
			return lookupRegister( token );
		}
		
		// is it a memory reference?
		else if( isReference( token ) ) {
			return parseReference( token );
		}
		
		// unrecognized
		else {
			throw new X86MalformedInstructionException( "unrecognized element '" + token + "'" );
		}
	}

}
