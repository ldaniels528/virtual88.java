package org.ldaniels528.javapc.ibmpc.devices.cpu.opcodes.flow.callret;

import org.ldaniels528.javapc.ibmpc.devices.cpu.opcodes.RegistersAffected;
import org.ldaniels528.javapc.ibmpc.devices.cpu.opcodes.flow.AbstractForcedRedirectOpCode;
import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.Operand;

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
@RegistersAffected({"SP"})
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
