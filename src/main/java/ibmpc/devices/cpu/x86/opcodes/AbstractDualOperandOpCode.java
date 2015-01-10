package ibmpc.devices.cpu.x86.opcodes;

import ibmpc.devices.cpu.operands.Operand;
import ibmpc.devices.cpu.operands.memory.MemoryPointer;

/**
 * Represents a Dual Operand opCode (e.g. 'MOV AX,CS')
 * @author ldaniels
 */
public abstract class AbstractDualOperandOpCode extends AbstractOpCode {
	protected final String name;
	protected final Operand dest;
	protected final Operand src;
	
	/**
	 * Creates a new dual operand opCode
	 * @param name the name of the instruction
	 * @param dst the given destination {@link Operand operand}
	 * @param src the given source {@link Operand operand}
	 */
	public AbstractDualOperandOpCode( final String name, final Operand dst, final Operand src ) {
		this.name	= name;
		this.dest 	= dst;
		this.src	= src;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		// if the source is a memory pointer, point to it's memory reference
		final Object srcObj = ( src instanceof MemoryPointer )
							? ((MemoryPointer)src).getMemoryReference()
							: src;
		
		// convert the instruction into a string			
		return String.format( "%s %s,%s", name, dest, srcObj );
	}

}
