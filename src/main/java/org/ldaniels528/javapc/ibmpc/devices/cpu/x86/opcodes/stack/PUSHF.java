package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.stack;

import org.ldaniels528.javapc.ibmpc.devices.cpu.I8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.X86Stack;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.StackModifyingOpCode;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.flags.AbstractFlagUpdateOpCode;
import org.ldaniels528.javapc.ibmpc.exceptions.X86AssemblyException;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

/**
 * <pre>
 *  Usage:  PUSHF
 *          PUSHFD  (386+)
 *  Modifies flags: None
 *
 *  Transfers the Flags Register onto the stack.  PUSHF saves a 16 bit
 *  value while PUSHFD saves a 32 bit value.
 *
 *                           Clocks                 Size
 *  Operands         808x  286   386   486          Bytes
 *
 *  none            10/14   3     4     4             1
 *  none  (PM)        -     -     4     3             1
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 * @see PUSHF
 */
public class PUSHF extends AbstractFlagUpdateOpCode implements StackModifyingOpCode {
    private static final PUSHF instance = new PUSHF();

    /**
     * Private constructor
     */
    private PUSHF() {
        super();
    }

    /**
     * @return the singleton instance of this class
     */
    public static PUSHF getInstance() {
        return instance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(IbmPcSystem system, final I8086 cpu) throws X86AssemblyException {
        final X86Stack stack = cpu.getStack();
        stack.push(cpu.FLAGS);
    }

}
