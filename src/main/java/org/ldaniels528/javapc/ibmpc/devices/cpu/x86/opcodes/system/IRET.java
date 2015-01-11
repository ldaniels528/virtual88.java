package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.system;

import org.ldaniels528.javapc.ibmpc.devices.cpu.Intel80x86;
import org.ldaniels528.javapc.ibmpc.devices.cpu.X86Stack;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;
import org.ldaniels528.javapc.ibmpc.exceptions.X86AssemblyException;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

/**
 * <pre>
 * Interrupt Return
 *
 * Usage:  IRET
 * Modifies flags: AF CF DF IF PF SF TF ZF
 *
 * Returns control to point of interruption by popping IP, CS
 * and then the Flags from the stack and continues execution at
 * this location.  CPU exception interrupts will return to the
 * instruction that cause the exception because the CS:IP placed
 * on the stack during the interrupt is the address of the offending
 * instruction.
 * </pre>
 */
public class IRET extends AbstractOpCode {
    private static IRET instance;
    private final X86Stack stack;

    /**
     * Private constructor
     */
    private IRET(final Intel80x86 cpu) {
        this.stack = cpu.getStack();
    }

    /**
     * Returns the singleton instance of this class
     *
     * @param cpu the given {@link Intel80x86 Intel 80x86 CPU}
     * @return the singleton instance of this class
     */
    public static IRET getInstance(final Intel80x86 cpu) {
        if (instance == null) {
            instance = new IRET(cpu);
        }
        return instance;
    }

    /* (non-Javadoc)
     * @see org.ldaniels528.javapc.ibmpc.devices.cpu.OpCode#execute(org.ldaniels528.javapc.ibmpc.devices.cpu.VirtualCPU)
     */
    public void execute(IbmPcSystem system, final Intel80x86 cpu)
            throws X86AssemblyException {
        // pop the registers
        stack.pop(cpu.IP);
        stack.pop(cpu.CS);
        stack.pop(cpu.FLAGS);
    }

}
