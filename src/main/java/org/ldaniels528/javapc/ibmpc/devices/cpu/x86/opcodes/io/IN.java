package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.io;

import org.ldaniels528.javapc.ibmpc.devices.cpu.I8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.Operand;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.AbstractDualOperandOpCode;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.registers.X86Register;
import org.ldaniels528.javapc.ibmpc.devices.ports.IbmPcHardwarePorts;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

import static org.ldaniels528.javapc.ibmpc.devices.cpu.operands.Operand.SIZE_16BIT;
import static org.ldaniels528.javapc.ibmpc.devices.cpu.operands.Operand.SIZE_8BIT;

/**
 * <pre>
 * Input Byte or Word From Port
 *
 * Usage:  IN      accum,port
 * Modifies flags: None
 *
 * A byte, word, or dword is read from "port" and placed in AL, AX or
 * EAX respectively.  If the port number is in the range of 0-255
 * it can be specified as an immediate, otherwise the port number
 * must be specified in DX.  Valid port ranges on the PC are 0-1024,
 * though values through 65535 may be specified and recognized by
 * third party vendors and PS/2's.
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 * @see OUT
 */
public class IN extends AbstractDualOperandOpCode {

    /**
     * IN accum, port (e.g. 'IN AL,DX')
     *
     * @param accum the given {@link Operand accumulator}
     * @param port  the given {@link Operand port}
     */
    public IN(final X86Register accum, final Operand port) {
        super("IN", accum, port);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(IbmPcSystem system, final I8086 cpu) {
        // get the port number
        final int portNum = src.get();

        // retrieve the information from the port
        final IbmPcHardwarePorts ports = system.getHardwarePorts();

        int value = 0;
        switch (dest.size()) {
            case SIZE_8BIT:
                value = ports.in8(portNum);
                break;
            case SIZE_16BIT:
                value = ports.in16(portNum);
                break;
        }

        // place the value into the accumulator
        dest.set(value);
    }

}