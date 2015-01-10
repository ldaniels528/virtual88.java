/**
 *
 */
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
public class RETF extends AbstractOpCode {
    private static RETF instance = new RETF();

    /**
     * Private constructor
     */
    protected RETF() {
        super();
    }

    /**
     * @return the singleton instance of this class
     */
    public static RETF getInstance() {
        return instance;
    }

    /* (non-Javadoc)
     * @see ibmpc.devices.cpu.OpCode#execute(ibmpc.devices.cpu.VirtualCPU)
     */
    @Override
    public void execute(final Intel80x86 cpu) throws X86AssemblyException {
        cpu.returnFar(0);
    }

    /*
     * (non-Javadoc)
     * @see ibmpc.devices.cpu.OpCode#isConditional()
     */
    public boolean isConditional() {
        return true;
    }

}