package ibmpc.util;

/**
 * This class generates bit masks for turning bits on or off
 * @author lawrence.daniels@gmail.com
 */
public class BitMaskGenerator {
	
	/**
	 * Generates a masking for retrieving (ANDing) the status
	 * of the given bit number
	 * @param bitNumber the given bit number
	 * @return the generated AND mask
	 */
	public static int turnBitOnMask( final int bitNumber ) {
		return ( 1 << bitNumber );
	}
	
	/**
	 * Generates a masking for retrieving (ORing) the status
	 * of the given bit number
	 * @param size the size (in bits) of the value being operated on
	 * @param bitNumber the given bit number
	 * @return the generated OR mask
	 */
	public static int turnBitOffMask( final int size, final int bitNumber ) {
		return ( ( 1 << size ) - 1 ) - ( 1 << bitNumber );
	}

}
