package org.ldaniels528.javapc.ibmpc.devices.cpu.decoders;

/**
 * Unhandled Byte Code Exception
 * @author lawrence.daniels@gmail.com
 */
@SuppressWarnings("serial")
public class UnhandledByteCodeException extends RuntimeException {

	/**
	 * Creates a new Unhandled Byte Code Exception
	 * @param byteCode the given byte code
	 */
	public UnhandledByteCodeException( final int byteCode ) {
		super( String.format( ( byteCode < 256 ) 
						? "Unhandled byte code %02X"
						: "Unhandled byte code %04X", 
								byteCode ) );
	}

}
