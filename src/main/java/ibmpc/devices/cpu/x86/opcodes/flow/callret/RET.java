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
public class RET extends AbstractOpCode {
    private static RET instance = new RET();

    /**
     * Private constructor
     */
    protected RET() {
        super();
    }

    /**
     * @return the singleton instance of this class
     */
    public static RET getInstance() {
        return instance;
    }

    /* (non-Javadoc)
     * @see ibmpc.devices.cpu.OpCode#execute(ibmpc.devices.cpu.VirtualCPU)
     */
    @Override
    public void execute(final Intel80x86 cpu) throws X86AssemblyException {
        cpu.returnNear(0);
    }

    /*
     * (non-Javadoc)
     * @see ibmpc.devices.cpu.OpCode#isConditional()
     */
    @Override
    public boolean isConditional() {
        return true;
    }

}