package ibmpc.devices.cpu.x86.opcodes.string;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.OpCode;
import ibmpc.devices.cpu.X86ExtendedFlags;
import ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;
import ibmpc.devices.cpu.x86.registers.X86Register;
import ibmpc.exceptions.X86AssemblyException;

/**
 * <pre>
 * Usage:  REPNE
 *         REPNZ
 *  Modifies flags: None
 *
 *  Repeats execution of string instructions while CX != 0 and the Zero
 *  Flag is clear.   CX is decremented and the Zero Flag tested after
 *  each string operation.   The combination of a repeat prefix and a
 *  segment override on ors other than the 386 may result in
 *  errors if an interrupt occurs before CX=0.
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 * @see CMPSB
 * @see CMPSW
 * @see LODSB
 * @see LODSW
 * @see SCASB
 * @see SCASW
 * @see STOSB
 * @see STOSW
 */
public class REPNZ extends AbstractOpCode {
    private final OpCode opCode;

    /**
     * Creates a new REPNZ opCode
     *
     * @param opCode the given {@link OpCode opCode}
     */
    public REPNZ(final OpCode opCode) {
        this.opCode = opCode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(final Intel80x86 cpu) throws X86AssemblyException {
        // cache CX and FLAGS
        final X86Register CX = cpu.CX;
        final X86ExtendedFlags FLAGS = cpu.FLAGS;

        // while the CX != 0 and ZF=1
        while ((CX.get() != 0) && FLAGS.isZF()) {
            // execute the instruction
            cpu.execute(opCode);
            CX.add(-1);
        }
    }

    /**
     * {@inheritDoc}
     */
    public String toString() {
        return String.format("REPNZ %s", opCode);
    }

}