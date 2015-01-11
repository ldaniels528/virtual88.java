package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.stack;

import org.ldaniels528.javapc.ibmpc.devices.cpu.Intel8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.X86Stack;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;
import org.ldaniels528.javapc.ibmpc.exceptions.X86AssemblyException;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

/**
 * <pre>
 * POPF/POPFD -- Pop Stack into FLAGS or EFLAGS Register
 *
 * Description
 * POPF/POPFD pops the word or doubleword on the top of the
 * stack and stores the value in the flags register. If the
 * operand-size attribute of the instruction is 16 bits,
 * then a word is popped and the value is stored in FLAGS.
 * If the operand-size attribute is 32 bits, then a doubleword
 * is popped and the value is stored in EFLAGS.
 *
 * Refer to Chapter 2 and Chapter 4 for information about the
 * FLAGS and EFLAGS registers. Note that bits 16 and 17 of EFLAGS,
 * called VM and RF, respectively, are not affected by POPF or POPFD.
 *
 * The I/O privilege level is altered only when executing at privilege level 0.
 * The interrupt flag is altered only when executing at a level at least as
 * privileged as the I/O privilege level. (Real-address mode is equivalent to
 * privilege level 0.) If a POPF instruction is executed with insufficient
 * privilege, an exception does not occur, but the privileged bits do not change.
 *
 * Opcode   Instruction   Clocks   Description
 * 9D       POPF          5        Pop top of stack FLAGS
 * 9D       POPFD         5        Pop top of stack into EFLAGS
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 */
public class POPF extends AbstractOpCode {
    private static POPF instance = new POPF();

    /**
     * Private constructor
     */
    private POPF() {
        super();
    }

    /**
     * @return the singleton instance of this class
     */
    public static POPF getInstance() {
        return instance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(IbmPcSystem system, final Intel8086 cpu) throws X86AssemblyException {
        final X86Stack stack = cpu.getStack();
        stack.pop(cpu.FLAGS);
    }

}
