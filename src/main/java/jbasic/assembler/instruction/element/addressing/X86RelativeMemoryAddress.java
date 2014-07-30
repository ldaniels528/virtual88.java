package jbasic.assembler.instruction.element.addressing;

import static ibmpc.devices.cpu.operands.memory.MemoryReference.*;
import static ibmpc.devices.memory.X86MemoryUtil.getHighByte;
import static ibmpc.devices.memory.X86MemoryUtil.getLowByte;
import jbasic.assembler.instruction.element.registers.X86RegisterRef;
import ibmpc.devices.cpu.Intel80x86;

/**
 * Represents a 80x86 relative memory address referenced
 * by an offset within the "current" (either {@link Intel80x86#CS CS} for code
 * or {@link Intel80x86#DS DS} for data) memory segment.
 * @author lawrence.daniels
 */
public class X86RelativeMemoryAddress extends X86MemoryAddress {
	private final int offset;
	
	
	/**
	 * Creates a new offset memory address instance
	 * @param offset the given offset portion of the memory address
	 */
	public X86RelativeMemoryAddress( final int offset ) {
		this.offset = offset;
	}
	
	/* (non-Javadoc)
	 * @see ibmpc.machinecode.encoder.X86DataElement#getSequence()
	 */
	public int getSequence() {
		return REF_CONST_NNNN;
	}

	/**
	 * @return the offset
	 */
	public int getOffset() {
		return offset;
	}

	/* (non-Javadoc)
	 * @see ibmpc.assembly.encoder.memoryaddress.X86MemoryAddress#getByteCode()
	 */
	public byte[] getByteCode() {
		final byte offHi = getHighByte( offset );
		final byte offLo = getLowByte( offset );
		return new byte[] { offLo, offHi };
	}
	
	/* 
	 * (non-Javadoc)
	 * @see ibmpc.machinecode.encoder.addressing.X86MemoryAddress#getByteCode(ibmpc.machinecode.encoder.registers.X86RegisterRef)
	 */
	public byte[] getByteCode( X86RegisterRef reg ) {
		return getByteCode();
	}

}
