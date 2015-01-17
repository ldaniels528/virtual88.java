package org.ldaniels528.javapc.ibmpc.devices.cpu.opcodes.flow.jump;

import org.ldaniels528.javapc.ibmpc.devices.cpu.opcodes.flow.AbstractForcedRedirectOpCode;
import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.Operand;

/**
 * <pre>
 * Usage:  JMP     target
 * Modifies flags: None
 *
 * Unconditionally transfers control to "label".  Jumps by default
 * are within -32768 to 32767 bytes from the instruction following
 * the jump.  NEAR and SHORT jumps cause the IP to be updated while FAR
 * jumps cause CS and IP to be updated.
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 */
public class JMP extends AbstractForcedRedirectOpCode {

    /**
     * Creates a new JMP opCode
     *
     * @param destination the given {@link Operand destination address}
     */
    public JMP(final Operand destination) {
        super(destination, false);
    }

}
