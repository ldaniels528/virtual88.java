package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.string;

import org.ldaniels528.javapc.ibmpc.devices.cpu.Intel80x86;
import org.ldaniels528.javapc.ibmpc.devices.cpu.OpCode;
import org.ldaniels528.javapc.ibmpc.devices.cpu.X86Flags;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.registers.X86Register;
import org.ldaniels528.javapc.ibmpc.exceptions.X86AssemblyException;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

/**
 * <pre>
 * Usage:  REPE
 *         REPZ
 *  Modifies flags: None
 *
 *  Repeats execution of string instructions while CX != 0 and the Zero
 *  Flag is set.  CX is decremented and the Zero Flag tested after
 *  each string operation.   The combination of a repeat prefix and a
 *  segment override on processors other than the 386 may result in
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
public class REPZ extends AbstractOpCode {
    private final OpCode opCode;

    /**
     * Creates a new REPZ opCode
     *
     * @param opCode the given {@link OpCode opCode}
     */
    public REPZ(final OpCode opCode) {
        this.opCode = opCode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(IbmPcSystem system, final Intel80x86 cpu)
            throws X86AssemblyException {
        // cache CX and FLAGS
        final X86Register CX = cpu.CX;
        final X86Flags FLAGS = cpu.FLAGS;

        // is it a mass data instruction?
        if (opCode instanceof MassDataOpCode) {
            if ((CX.get() != 0) && !FLAGS.isZF()) {
                final MassDataOpCode massDataOpCode = (MassDataOpCode) opCode;
                massDataOpCode.executeEnMass(cpu, CX.get());
                CX.set(0);
            }
        }

        // execute normally
        else {
            // while the CX != 0 and ZF=0
            while ((CX.get() != 0) && !FLAGS.isZF()) {
                // execute the instruction
                cpu.execute(system, opCode);
                CX.add(-1);
            }
        }

        // set the Zero Flag (ZF)
        FLAGS.setZF(true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format("REPZ %s", opCode);
    }

}