package ibmpc.compiler.element.addressing;

import static ibmpc.devices.cpu.operands.memory.MemoryReference.*;
import static ibmpc.compiler.element.registers.X86RegisterReferences.isRegister16G;
import static ibmpc.util.X86CompilerUtil.getConfiguredTokenizer;
import static ibmpc.util.X86CompilerUtil.isNumber;
import static ibmpc.util.X86CompilerUtil.mandateToken;
import static ibmpc.util.X86CompilerUtil.noMoreTokens;
import ibmpc.exceptions.X86AssemblyException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ibmpc.compiler.element.values.X86ByteValue;
import ibmpc.compiler.element.values.X86WordValue;
import ibmpc.compiler.exception.X86MalformedInstructionException;
import jbasic.gwbasic.values.GwBasicValues;

import com.ldaniels528.tokenizer.TokenIterator;
import com.ldaniels528.tokenizer.Tokenizer;
import com.ldaniels528.tokenizer.TokenizerContext;

/**
 * 80x86 "Referenced" Address Parser (e.g. "[BX+SI]")
 * @author lawrence.daniels@gmail.com
*/
public class X86ReferencedAddressParser {
	private static final Tokenizer tokenizer = getConfiguredTokenizer();
	private static final Map<String,Integer> INDICES = getReferences();
	
	/**
	 * Indicates whether the given value "appears" to be a memory reference
	 * @param value the given value
	 * @return true, if the given value "appears" to be a memory reference
	 */
	public static boolean isReference( final String value ) {
		return value.startsWith( "[" ) && value.endsWith( "]" );
	}
	
	/**
	 * Parses the given string into a referenced address
	 * @param address the given string representation of the referenced address
	 * @return the {@link X86ReferencedAddressParser referenced address}
	 * @throws X86AssemblyException 
	 */
	public static X86ReferencedAddress parseReference( final String address ) 
	throws X86AssemblyException {
		String registerName1 = null;
		String registerName2 = null;
		String value = null;
		
		// create a container for incoming paramters
		final List<String> params = getParameters( address );		
		
		// analyze the parameters
		for( String param : params ) {
			// is the parameter a general purpose register?
			if( isRegister16G( param ) ) {
				if( registerName1 == null ) registerName1 = param;
				else if( registerName2 == null ) registerName2 = param;
				else throw new X86AssemblyException( "Unexpected argument '" + param + "'" );
			}
			// is the parameter a number?
			else if( isNumber( param ) ) {
				if( value != null )
					throw new X86AssemblyException( "Unexpected argument '" + param + "'" );
				value = param;
			}
			// not sure what it is
			else throw new X86AssemblyException( "Invalid argument '" + param + "'" );
		}
		
		// now we potentially have upto two registers and a number
		final int referenceType = getReferenceType( registerName1, registerName2, value );
		if( referenceType == -1 ) 
			throw new X86MalformedInstructionException( "Malformed reference '" + address + "'" );
		
		// return the "referenced" address
		switch( referenceType ) {
			case REF_CONST_BP_DI: 		return X86ReferencedAddress.createBP_DI();
			case REF_CONST_BP_DI_NN:	return X86ReferencedAddress.createBP_DI_NN( new X86ByteValue( value ) );
			case REF_CONST_BP_NN:		return X86ReferencedAddress.createBP_NN( new X86ByteValue( value ) );
			case REF_CONST_BP_NNNN:		return X86ReferencedAddress.createBP_NNNN( new X86WordValue( value ) );
			case REF_CONST_BP_SI:		return X86ReferencedAddress.createBP_SI();
			case REF_CONST_BP_SI_NN:	return X86ReferencedAddress.createBP_SI_NN( new X86ByteValue( value ) );
			case REF_CONST_BX:			return X86ReferencedAddress.createBX();
			case REF_CONST_BX_DI:		return X86ReferencedAddress.createBX_DI();
			case REF_CONST_BX_DI_NN:	return X86ReferencedAddress.createBX_DI_NN( new X86ByteValue( value ) );
			case REF_CONST_BX_NN:		return X86ReferencedAddress.createBX_NN( new X86ByteValue( value ) );
			case REF_CONST_BX_NNNN:		return X86ReferencedAddress.createBX_NNNN( new X86WordValue( value ) );
			case REF_CONST_BX_SI:		return X86ReferencedAddress.createBX_SI();
			case REF_CONST_BX_SI_NN:	return X86ReferencedAddress.createBX_SI_NN( new X86ByteValue( value ) );
			case REF_CONST_DI:			return X86ReferencedAddress.createDI();
			case REF_CONST_DI_NN:		return X86ReferencedAddress.createDI_NN( new X86ByteValue( value ) );
			case REF_CONST_DI_NNNN:		return X86ReferencedAddress.createDI_NNNN( new X86WordValue( value ) );
			case REF_CONST_NNNN:		return X86ReferencedAddress.createNNNN( new X86WordValue( value ) );
			case REF_CONST_SI:			return X86ReferencedAddress.createSI();
			case REF_CONST_SI_NN:		return X86ReferencedAddress.createSI_NN( new X86ByteValue( value ) );
			case REF_CONST_SI_NNNN:		return X86ReferencedAddress.createSI_NNNN( new X86WordValue( value ) );
			default:
				throw new IllegalStateException( "Unhandled reference type (type = " + referenceType + ")" );
		}
	}

	/**
	 * Returns the index of this address (e.g. "[BX+SI]" = 0)
	 * @return the index of this address 
	 */
	private static int getReferenceType( final String register1, final String register2, final String value ) {
		final String template = getTemplateString( register1, register2, value );
		return INDICES.containsKey( template ) ? INDICES.get( template ) : -1;
	}	
	
	/**
	 * @return the template the this address should be matched against
	 */
	private static String getTemplateString( final String register1, final String register2, final String value ) {
		final StringBuilder sb = new StringBuilder( 12 );
		sb.append( '[' );
		sb.append( register1 );
		if( register2 != null ) {
			sb.append( '+' );
			sb.append( register2 );
		}
		if( value != null ) {
			sb.append( '+' );
			sb.append( is16BitValue( value ) ? "####" : "##" );
		}
		sb.append( ']' );
		return sb.toString();
	}
	
	/**
	 * Indicates whether the given value is a 16-bit value
	 * @param value the given string that represents the numeric value
	 * @return true, if the given value is a 16-bit value
	 */
	private static boolean is16BitValue( final String value ) {
		final int intValue = GwBasicValues.parseIntegerString( value );
		return intValue >= 0x100;
	}
	
	/** 
	 * Builds a mapping of register references to machine code operands
	 * @return a mapping of register references to machine code operands
	 */
	private static final Map<String,Integer> getReferences() {
		final Map<String,Integer> mapping = new HashMap<String,Integer>();
		mapping.put( "[BX+SI]",		REF_CONST_BX_SI );
		mapping.put( "[BX+DI]",		REF_CONST_BX_DI );
		mapping.put( "[BP+SI]",		REF_CONST_BP_SI );
		mapping.put( "[BP+DI]",		REF_CONST_BP_DI );
		mapping.put( "[SI]", 		REF_CONST_SI);
		mapping.put( "[DI]", 		REF_CONST_DI );
		mapping.put( "[##]", 		REF_CONST_NNNN );
		mapping.put( "[####]", 		REF_CONST_NNNN );		
		mapping.put( "[BX]", 		REF_CONST_BX );
		mapping.put( "[BX+SI+##]",	REF_CONST_BX_SI_NN );
		mapping.put( "[BX+DI+##]",	REF_CONST_BX_DI_NN );
		mapping.put( "[BP+SI+##]",	REF_CONST_BP_SI_NN );
		mapping.put( "[BP+DI+##]",	REF_CONST_BP_DI_NN );
		mapping.put( "[SI+##]", 	REF_CONST_SI_NN );
		mapping.put( "[DI+##]", 	REF_CONST_DI_NN );
		mapping.put( "[BP+##]", 	REF_CONST_BP_NN );
		mapping.put( "[BX+##]", 	REF_CONST_BX_NN );
		mapping.put( "[SI+####]", 	REF_CONST_SI_NNNN );
		mapping.put( "[DI+####]", 	REF_CONST_DI_NNNN );
		mapping.put( "[BP+####]", 	REF_CONST_BP_NNNN );
		mapping.put( "[BX+####]", 	REF_CONST_BX_NNNN );
		return mapping;
	}
	
	/**
	 * Retrieves the paramters from the given address string
	 * @param address the given address string
	 * @return the extracted {@link List list} of parameters
	 * @throws X86AssemblyException
	 */
	private static List<String> getParameters( String address ) 
	throws X86AssemblyException {			
		// create a container for incoming paramters
		final List<String> params = new ArrayList<String>( 3 );
		
		// get the iteration of tokens
		final TokenizerContext context = tokenizer.parse( address );
		final TokenIterator it = tokenizer.nextTokens( context );
		
		// must start with '['
		mandateToken( it, "[" );
		
		// collect tokens until ']' is reached
		String token;
		while( it.hasNext() && !( token = it.next() ).equals( "]" ) ) {				
			// get the parameter
			params.add( token );
			
			// if there is another parameter ...
			if( it.hasNext() ) {
				if( !"]".equals( it.peekAtNext() ) && !"+".equals( it.peekAtNext() ) ) 
					throw new X86AssemblyException( "Symbol ']' expected" );
				else 
					it.next();
			}
		}
		
		// there should be no more tokens
		noMoreTokens( it );
		
		// should've gotten no more than 3 parameters
		if( params.isEmpty() || params.size() > 3 )
			throw new X86AssemblyException( "Invalid number of parameters" );
		
		// return the parameters
		return params;
	}
	
}
