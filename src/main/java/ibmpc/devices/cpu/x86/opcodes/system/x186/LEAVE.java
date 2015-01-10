package ibmpc.devices.cpu.x86.opcodes.system.x186;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;

/**
 * LEAVE -- High Level Procedure Exit
 * <pre>
 * Description
 * LEAVE reverses the actions of the ENTER instruction.
 * By copying the frame pointer to the stack pointer,
 * LEAVE releases the stack space used by a procedure
 * for its local variables. The old frame pointer is
 * popped into BP or EBP, restoring the caller's frame.
 * A subsequent RET instruction removes any arguments
 * pushed onto the stack of the exiting procedure.
 *
 * Flags Affected
 * None
 *
 * Opcode  Instruction  Clocks  Description
 * C9      LEAVE        4       Set SP to BP, then pop BP
 * C9      LEAVE        4       Set ESP to EBP, then pop EBP
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 * @see ENTER
 */
public class LEAVE extends AbstractOpCode {
    private static LEAVE instance = new LEAVE();

    /**
     * Private constructor
     */
    private LEAVE() {
        super();
    }

    /**
     * @return the singleton instance of this class
     */
    public static LEAVE getInstance() {
        return instance;
    }

    /*
     * (non-Javadoc)
     * @see ibmpc.devices.cpu.OpCode#execute(ibmpc.devices.cpu.Intel80x86)
     */
    public void execute(final Intel80x86 cpu) {
        // for 386+
        if (cpu.isVirtualMode()) {
            cpu.ESP.set(cpu.EBP.get());
            cpu.getStack().pop(cpu.EBP);
        }

        // 286 or older ...
        else {
            cpu.SP.set(cpu.BP.get());
            cpu.getStack().pop(cpu.BP);
        }
    }

}
