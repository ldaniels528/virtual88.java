package ibmpc.instruction.element.addressing;

import static ibmpc.util.X86CompilerUtil.isNumber;

/**
 * 80x86 Memory Address Parser
 * @author lawrence.daniels@gmail.com
 */
public class X86MemoryAddressParser {

	/**
	 * Indicates whether the given string represents a "referenced" address (e.g. '[BX+SI+45]').
	 * @return true, if the given string represents a "referenced" address.
	 */
	public static boolean isReferencedAddress( final String identifier ) {
		return identifier.startsWith( "[" ) && identifier.endsWith( "]" )
					&& !isNumber( identifier.substring( 1, identifier.length() - 1 ) );
	}

	/**
	 * Indicates whether the given string represents a direct memory address (e.g. '[2345]').
	 * @return true, if the given string represents a direct memory address.
	 */
	public static boolean isDirectAddress( final String identifier ) {
		return ( identifier.startsWith( "[" ) && identifier.endsWith( "]" )  
				&& isNumber( identifier.substring( 1, identifier.length() - 1 ) ) );
	}

}
