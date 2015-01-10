package ibmpc.devices.cpu.x86.opcodes.stack.x386;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.X86Stack;
import ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;
import ibmpc.devices.cpu.x86.opcodes.stack.PUSHF;
import ibmpc.exceptions.X86AssemblyException;

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
public class PUSHFD extends AbstractOpCode {
    private static PUSHFD instance = new PUSHFD();

    /**
     * Private constructor
     */
    private PUSHFD() {
        super();
    }

    /**
     * @return the singleton instance of this class
     */
    public static PUSHFD getInstance() {
        return instance;
    }

    /* (non-Javadoc)
     * @see ibmpc.devices.cpu.OpCode#execute(ibmpc.devices.cpu.VirtualCPU)
     */
    public void execute(final Intel80x86 cpu)
            throws X86AssemblyException {
        // get the stack instance
        final X86Stack stack = cpu.getStack();

        // get the 32-bit value
        final int value32 = cpu.FLAGS.get();

        // break the 32-bit value into two 16-bit values
        final int value16Hi = (value32 & 0xFFFF0000) >> 16;
        final int value16Lo = (value32 & 0x0000FFFF);

        // push the two 16-bit values onto the stack
        stack.pushValue(value16Lo);
        stack.pushValue(value16Hi);
    }

}