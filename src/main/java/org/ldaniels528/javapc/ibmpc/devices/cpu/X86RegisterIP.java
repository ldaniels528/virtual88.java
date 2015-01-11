package org.ldaniels528.javapc.ibmpc.devices.cpu;


/**
 * 8086 Instruction Pointer 
 * @author lawrence.daniels@gmail.com
 */
public class X86RegisterIP extends X86Register16bit {

	/**
	 * Default constructor
	 */
	public X86RegisterIP() {
		super( "IP", 0xFE );
	}

}