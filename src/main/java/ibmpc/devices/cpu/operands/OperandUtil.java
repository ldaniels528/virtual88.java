package ibmpc.devices.cpu.operands;

import ibmpc.devices.cpu.X86ExtendedFlags;

/**
 * Operand Utilities
 * @author lawrence.daniels@gmail.com
 */
public class OperandUtil {
	private static final int EQUAL		= 0;
	private static final int GREATER	= 1;
	private static final int LESSER		= -1;
	
	/**
	 * Compares the two operands
	 * @param operand0 the primary {@link Operand operand}
	 * @param operand1 the secondary {@link Operand operand}
	 * @return 0 if the operands are equal, 
	 * 		   1 if the primary operand is greater,
	 * 		  -1 if the secondary operand is  lesser
	 */
	public static int compare( final Operand operand0, final Operand operand1 ) {
		// cache the values
		final int value0 = operand0.get();
		final int value1 = operand1.get();
		
		// perform the comparison
		if( value0 < value1 ) return 1;
		else if( value0 > value1 ) return -1;
		else return 0;
	}
	
	/**
	 * Compares the two operands
	 * @param flags the given {@link X86ExtendedFlags FLAGS} instance
	 * @param value0 the given primary data
	 * @param value1 the given secondary data
	 */
	public static void compare( final X86ExtendedFlags flags, final int value0, final int value1 ) {
		final int result = compare( value0, value1 );
		switch( result ) {
			case EQUAL: flags.setZF( true ); break;
			case GREATER: flags.setCF( true ); break;
			case LESSER: flags.setCF( false ); break;
		}
	}
	
	/**
	 * Compares the two values
	 * @param value0 the given primary value
	 * @param value1 the given secondary value
	 * @return the results of the comparison
	 */
	private static int compare( final int value0, final int value1 ) {
		// perform the comparison
		if( value0 < value1 ) return GREATER;
		else if( value0 > value1 ) return LESSER;
		else return EQUAL;
	}

}
