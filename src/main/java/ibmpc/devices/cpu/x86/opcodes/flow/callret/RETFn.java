package ibmpc.devices.cpu.x86.opcodes.flow.callret;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;
import ibmpc.exceptions.X86AssemblyException;

/**
 * <pre>
 * Usage:  RET
 * 		   RET     nBytes
 *         RETF
 *         RETF    nBytes
 *  Modifies flags: None
 *
 *  Transfers control from a procedure back to the instruction address
 *  saved on the stack.  "n bytes" is an optional number of bytes to
 *  release.  Far returns pop the IP followed by the CS, while near
 *  returns pop only the IP register.
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 * @see RET
 * @see RETn
 * @see RETF
 * @see RETFn
 */
public class RETFn extends AbstractOpCode {
    private final int count;

    /**
     * Creates a new RET instruction
     *
     * @param count the number of elements to POP off the stack
     */
    public RETFn(final int count) {
        this.count = count;
    }

    /* (non-Javadoc)
     * @see ibmpc.devices.cpu.OpCode#execute(ibmpc.devices.cpu.VirtualCPU)
     */
    public void execute(final Intel80x86 cpu)
            throws X86AssemblyException {
        cpu.returnFar(count);
    }

    /*
     * (non-Javadoc)
     * @see ibmpc.devices.cpu.OpCode#isConditional()
     */
    @Override
    public boolean isConditional() {
        return true;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("RETF %04X", count);
    }

}