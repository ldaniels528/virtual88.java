package ibmpc.devices.cpu.x86.opcodes.flow.callret;

import ibmpc.devices.cpu.operands.Operand;
import ibmpc.devices.cpu.x86.opcodes.flow.AbstractForcedRedirectOpCode;

/**
 * <pre>
 * Usage:  CALL    destination
 * Modifies flags: None
 *
 * Pushes Instruction Pointer (and Code Segment for far calls) onto
 * stack and loads Instruction Pointer with the address of proc-name.
 * Code continues with execution at CS:IP.
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 */
public class CALL extends AbstractForcedRedirectOpCode {

    /**
     * Creates a new CALL opCode
     *
     * @param destination the given {@link Operand destination address}
     */
    public CALL(final Operand destination) {
        super(destination, true);
    }

}
