package org.ldaniels528.javapc.ibmpc.devices.cpu.decoders;

import org.ldaniels528.javapc.ibmpc.devices.cpu.I8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.OpCode;
import org.ldaniels528.javapc.ibmpc.devices.cpu.opcodes.math.DEC;
import org.ldaniels528.javapc.ibmpc.devices.cpu.opcodes.math.INC;
import org.ldaniels528.javapc.ibmpc.devices.memory.X86MemoryProxy;

/**
 * <pre>
 * Decodes instruction codes between 40h and 4Fh 
 *	---------------------------------------------------------------------------
 *	type	size		description 			comments
 *	---------------------------------------------------------------------------
 *	i		5-bit 		instruction type  		inc=01000,dec=01001
 *	r		3-bit		register index			ax=000b, cx=001b
 * 
 *  Instruction code layout
 *  -----------------------
 *  7654 3210 (8 bits)
 *  iiii irrr 
 *  
 * ---------------------------------------------------------------------------
 * instruction			code 	iiiii rrr 
 * ---------------------------------------------------------------------------
 * inc ax				40		01000 000
 * inc cx				41		01000 001
 * inc dx				42		01000 010
 * inc bx				43		01000 011
 * inc sp				44		01000 100
 * inc bp				45		01000 101
 * inc si				46		01000 110
 * inc di				47		01000 111
 * dec ax				48		01001 000
 * dec cx				49		01001 001
 * dec dx				4A		01001 010
 * dec bx				4B		01001 011
 * dec sp				4C		01001 100
 * dec bp				4D		01001 101
 * dec si				4E		01001 110
 * dec di				4F		01001 111
 * </pre>
 * @author lawrence.daniels@gmail.com
 */
public class Decoder40 implements Decoder {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OpCode decode(final I8086 cpu, X86MemoryProxy proxy, DecodeProcessor processor) {
		// get the next byte code
		final int code8 = proxy.nextByte();
		
		// interpret the instruction
		switch( code8 ) {
			// INC codes
			case 0x40: return new INC( cpu.AX ); 
			case 0x41: return new INC( cpu.CX ); 
			case 0x42: return new INC( cpu.DX ); 
			case 0x43: return new INC( cpu.BX ); 
			case 0x44: return new INC( cpu.SP ); 
			case 0x45: return new INC( cpu.BP ); 
			case 0x46: return new INC( cpu.SI ); 
			case 0x47: return new INC( cpu.DI ); 
			
			// DEC codes
			case 0x48: return new DEC( cpu.AX ); 
			case 0x49: return new DEC( cpu.CX ); 
			case 0x4A: return new DEC( cpu.DX ); 
			case 0x4B: return new DEC( cpu.BX ); 
			case 0x4C: return new DEC( cpu.SP ); 
			case 0x4D: return new DEC( cpu.BP ); 
			case 0x4E: return new DEC( cpu.SI ); 
			case 0x4F: return new DEC( cpu.DI ); 
			
			// unrecognized
			default: 	throw new UnhandledByteCodeException( code8 );
		}
	}

}
