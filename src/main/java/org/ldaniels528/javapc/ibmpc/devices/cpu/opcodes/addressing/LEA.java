package org.ldaniels528.javapc.ibmpc.devices.cpu.opcodes.addressing;

import org.ldaniels528.javapc.ibmpc.devices.cpu.I8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.opcodes.AbstractDualOperandOpCode;
import org.ldaniels528.javapc.ibmpc.devices.cpu.opcodes.RegistersAffected;
import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.OperandHelper;
import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.memory.MemoryReference;
import org.ldaniels528.javapc.ibmpc.devices.cpu.registers.X86Register;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

/**
 * Load Effective Address
 * <pre>
 * Syntax:	lea	reg16, memref
 * reg16: 16-bit register
 * memref: An effective memory address (e.g., [bx+2])
 * Action: reg16 = address offset of memref
 * Flags Affected: None
 * Notes: This instruction is used to easily calculate the address of data in memory. It does not actually access memory.
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 */
@RegistersAffected({"CS"})
public class LEA extends AbstractDualOperandOpCode {

    /**
     * LEA dest, src (e.g. 'LEA AX,[BX+SI]')
     *
     * @param dest the given {@link X86Register destination}
     * @param src  the given {@link MemoryReference source}
     */
    public LEA(final X86Register dest, final MemoryReference src) {
        super("LEA", dest, OperandHelper.getEffectiveAddress(src, dest.size()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(final IbmPcSystem system, final I8086 cpu) {
        // set the DS-override register
        cpu.setOverrideRegister(cpu.CS);

        // populate the destination with the source offset
        dest.set(src.get());

        // reset the DS override
        cpu.resetOverrideRegister();
    }

}
