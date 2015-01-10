package ibmpc.devices.cpu.x86.decoder;

import static ibmpc.devices.cpu.x86.decoder.DecoderUtil.lookupRegister;
import static ibmpc.devices.cpu.x86.decoder.DecoderUtil.lookupSecondaryOperand;
import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.OpCode;
import ibmpc.devices.cpu.operands.Operand;
import ibmpc.devices.cpu.x86.opcodes.addressing.x286.ARPL;
import ibmpc.devices.cpu.x86.opcodes.data.DB;
import ibmpc.devices.cpu.x86.opcodes.io.INSB;
import ibmpc.devices.cpu.x86.opcodes.io.INSD;
import ibmpc.devices.cpu.x86.opcodes.io.INSW;
import ibmpc.devices.cpu.x86.opcodes.io.OUTSB;
import ibmpc.devices.cpu.x86.opcodes.io.OUTSD;
import ibmpc.devices.cpu.x86.opcodes.io.OUTSW;
import ibmpc.devices.cpu.x86.opcodes.math.x186.BOUND;
import ibmpc.devices.cpu.x86.opcodes.stack.x186.POPA;
import ibmpc.devices.cpu.x86.opcodes.stack.x186.PUSHA;
import ibmpc.devices.cpu.x86.opcodes.stack.x386.POPAD;
import ibmpc.devices.cpu.x86.opcodes.stack.x386.PUSHAD;
import ibmpc.devices.cpu.x86.registers.X86Register;
import ibmpc.devices.memory.X86MemoryProxy;

/**
 * Decodes instruction codes between 60h and 6Fh
 * <pre>
 *	---------------------------------------------------------------------------
 *	type	bits		description 			comments
 *	---------------------------------------------------------------------------
 *	t		2			element code
 *	r		3			register code
 *	m		3			reference code
 *	i		7	 		instruction type
 *	j		1			instruction sub type
 *
 *  Instruction code layout
 *  -----------------------------
 *  fedc ba98 7654 3210 (8/16 bits)
 *  ttrr rmmm iiii iiij
 *  
 * ---------------------------------------------------------------------------
 * instruction				code 	tt rrr mmm	iiiiiii j 
 * ---------------------------------------------------------------------------
 * pusha/pushad				  60				0110000 0
 * popa/popad				  61				0110000 1
 * 
 * arpl ax,cx				C163	11 000 001 	0110001 1
 * arpl cx,ax				C863	11 001 000 	0110001 1
 * 
 * insb						  6C				0110110 0
 * insw | insd				  6D				0110110 1
 * outsb					  6E				0110111 0
 * outsw | outsd			  6F				0110111 1
 * </pre>
 * See <a href="http://pdos.csail.mit.edu/6.828/2006/readings/i386/OUTS.htm">OUT instruction</a>
 * @author lawrence.daniels@gmail.com
 */
public class Decoder60 implements Decoder {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OpCode decode( final Intel80x86 cpu, final X86MemoryProxy proxy ) {
		// get the 8-bit instruction code
		final int code8 = proxy.nextByte();
		
		// is the processor in Real mode?
		final boolean realMode = cpu.FLAGS.isVM();
		
		// decode the instruction
		switch( code8 ) {
			case 0x60:	return realMode ? new PUSHAD() : new PUSHA();
			case 0x61:	return realMode ? new POPAD()  : new POPA();
			case 0x62:	return createBOUND( cpu, proxy, code8 );
			case 0x63:	return createARPL( cpu, proxy, code8 );
			case 0x6C:	return INSB.getInstance();
			case 0x6D:	return realMode ? INSD.getInstance()  : INSW.getInstance(); 
			case 0x6E:	return OUTSB.getInstance();
			case 0x6F:	return realMode ? OUTSD.getInstance() : OUTSW.getInstance();  
			default:	return new DB( code8 );
		}
	}
	
	/**
	 * Creates a new BOUND instruction
	 * @param cpu the given {@link Intel80x86 CPU}
	 * @param proxy the given {@link X86MemoryProxy proxy}
	 * @param code8 the given 8-bit instruction code
	 * @return a new {@link OpCode BOUND} instruction
	 */
	private OpCode createBOUND( final Intel80x86 cpu, final X86MemoryProxy proxy, final int code8 ) {
		// build the 16-bit code
		final int code16 = proxy.nextWord( code8 );
		
		// lookup the primary and secondary operands
		final X86Register primary = lookupRegister( cpu, proxy, code16 );
		final Operand secondary = lookupSecondaryOperand( cpu, proxy, code16 );
		
		// return the instruction
		return new BOUND( primary, secondary );
	}
	
	/**
	 * Creates a new ARPL instruction
	 * @param cpu the given {@link Intel80x86 CPU}
	 * @param proxy the given {@link X86MemoryProxy proxy}
	 * @param code8 the given 8-bit instruction code
	 * @return a new {@link OpCode ARPL} instruction
	 */
	private OpCode createARPL( final Intel80x86 cpu, final X86MemoryProxy proxy, final int code8 ) {
		// build the 16-bit code
		final int code16 = proxy.nextWord( code8 );
		
		// lookup the primary and secondary operands
		final X86Register primary = lookupRegister( cpu, proxy, code16 );
		final Operand secondary = lookupSecondaryOperand( cpu, proxy, code16 );
		
		// return the instruction
		return new ARPL( primary, secondary );
	}

}
