package ibmpc.devices.cpu.x86.opcodes.io;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.operands.Operand;
import ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;
import ibmpc.devices.cpu.x86.registers.X86Register;
import ibmpc.devices.ports.IbmPcHardwarePorts;

import static ibmpc.devices.cpu.operands.Operand.*;

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
public class IN extends AbstractOpCode {
    private final X86Register accum;
    private final Operand port;

    /**
     * IN accum, port (e.g. 'IN AL,DX')
     *
     * @param accum the given {@link Operand accumulator}
     * @param port  the given {@link Operand port}
     */
    public IN(final X86Register accum, final Operand port) {
        this.accum = accum;
        this.port = port;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(final Intel80x86 cpu) {
        // get the port number
        final int portNum = port.get();

        // retrieve the information from the port
        final IbmPcHardwarePorts ports = cpu.getHardwarePorts();

        int value = 0;
        switch (accum.size()) {
            case SIZE_8BIT:
                value = ports.in8(portNum);
                break;
            case SIZE_16BIT:
                value = ports.in16(portNum);
                break;
            case SIZE_32BIT:
                value = ports.in32(portNum);
                break;
        }

        // place the value into the accumulator
        accum.set(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format("IN %s,%s", accum, port);
    }

}