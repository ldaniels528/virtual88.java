package ibmpc.devices.cpu.x86.opcodes.io;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.operands.Operand;
import ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;
import ibmpc.devices.cpu.x86.registers.X86Register;
import ibmpc.devices.ports.IbmPcHardwarePorts;
import ibmpc.system.IbmPcSystem;

import static ibmpc.devices.cpu.operands.Operand.*;

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
public class OUT extends AbstractOpCode {
    private final Operand accum;
    private final Operand port;

    /**
     * OUT port, accum
     *
     * @param port  the given {@link Operand port}
     * @param accum the given {@link X86Register accumulator}
     */
    public OUT(final Operand port, final X86Register accum) {
        this.port = port;
        this.accum = accum;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(final IbmPcSystem system, final Intel80x86 cpu) {
        // get the port number
        final int portNum = port.get();

        // get the data value
        final int value = accum.get();

        // output the value to the port
        final IbmPcHardwarePorts ports = system.getHardwarePorts();
        switch (accum.size()) {
            case SIZE_8BIT:
                ports.out8(portNum, value);
                break;
            case SIZE_16BIT:
                ports.out16(portNum, value);
                break;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format("OUT %s,%s", port, accum);
    }

}
