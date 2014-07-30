package ibmpc.devices.memory;

/**
 * X86 Memory Utility
 * @author lawrence.daniels@gmail.com
 */
public class X86MemoryUtil {
	
	/**
	 * Computes the physical address within memory based
	 * on the given segment and offset.
	 * @return the physical address
	 */
	public static int computePhysicalAddress( int segment, int offset ) {
		// compute the physical address
		final int physicalAddress = ( segment << 4 ) + offset;

		// return the physical address
		return physicalAddress;
	}
	
	/**
	 * Returns a copy of the given block with the bytes
	 * in reverse order; i.e. the first byte will be last
	 * and the last byte will be first. 
	 * @param srcBlock the given block to copy and reverse
	 * @return the "reversed" block
	 */
	public static byte[] reverseBlock( final byte[] srcBlock ) {
		// get the block size
		final int count = srcBlock.length;
		
		// create a new empty block
		final byte[] dstBlock = new byte[ count ];
		
		// copy the contents of the src block in revese
		for( int n = 0; n < count; n++ ) {
			dstBlock[(count-1)-n] = srcBlock[n];
		}
		
		// return the "reversed" block
		return dstBlock;
	}
	
	/**
	 * Returns the high portion of the given value
	 * @param value16 the given word value
	 * @return the high portion of the given value
	 */
	public static byte getHighByte( final int value16 ) {
		return (byte)( ( value16 & 0xFF00 ) >> 8 );
	}
	
	/**
	 * Returns the low portion of the given value
	 * @param value16 the given word value
	 * @return the low portion of the given value
	 */
	public static byte getLowByte( final int value16 ) {
		return (byte)( value16 & 0x00FF );
	}

}
