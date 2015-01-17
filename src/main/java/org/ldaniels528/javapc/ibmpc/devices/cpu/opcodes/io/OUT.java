package org.ldaniels528.javapc.ibmpc.devices.cpu.opcodes.io;

import org.ldaniels528.javapc.ibmpc.devices.cpu.I8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.Operand;
import org.ldaniels528.javapc.ibmpc.devices.cpu.opcodes.AbstractDualOperandOpCode;
import org.ldaniels528.javapc.ibmpc.devices.cpu.registers.X86Register;
import org.ldaniels528.javapc.ibmpc.devices.ports.IbmPcHardwarePorts;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

import static org.ldaniels528.javapc.ibmpc.devices.cpu.operands.Operand.SIZE_16BIT;
import static org.ldaniels528.javapc.ibmpc.devices.cpu.operands.Operand.SIZE_8BIT;

/**
 * <pre>
 * Output Data to Port
 *
 * Usage:  OUT     port,accum
 * Modifies flags: None
 *
 * Transfers byte in AL, word in AX, or dword in EAX to the specified
 * hardware port address.  If the port number is in the range of 0-255
 * it can be specified as an immediate.  If greater than 255 then the
 * port number must be specified in DX.  Since the PC only decodes 10
 * bits of the port address, values over 1023 can only be decoded by
 * third party vendor equipment and also map to the port range 0-1023.
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 * @see IN
 */
public class OUT extends AbstractDualOperandOpCode {

    /**
     * OUT port, accum
     *
     * @param port  the given {@link Operand port}
     * @param accum the given {@link X86Register accumulator}
     */
    public OUT(final Operand port, final X86Register accum) {
        super("OUT", port, accum);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(final IbmPcSystem system, final I8086 cpu) {
        // get the port number
        final int portNum = dest.get();

        // get the data value
        final int value = src.get();

        // output the value to the port
        final IbmPcHardwarePorts ports = system.getHardwarePorts();
        switch (src.size()) {
            case SIZE_8BIT:
                ports.out8(portNum, value);
                break;
            case SIZE_16BIT:
                ports.out16(portNum, value);
                break;
        }
    }

}
