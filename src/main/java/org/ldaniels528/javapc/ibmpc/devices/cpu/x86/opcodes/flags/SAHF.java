package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.flags;

import org.ldaniels528.javapc.ibmpc.devices.cpu.I8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

/**
 * SAHF - Store AH Register into FLAGS
 * <pre>
 *  Usage:  SAHF
 *  Modifies flags: AF CF PF SF ZF
 *
 *  Transfers bits 0-7 of AH into the Flags Register.  This includes
 *  AF, CF, PF, SF and ZF.
 *
 *  Clocks                 Size
 *  Operands         808x  286   386   486          Bytes
 *
 *  none              4     2     3     2             1
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 */
public class SAHF extends AbstractOpCode {
    private static SAHF instance = new SAHF();

    /**
     * Private constructor
     */
    private SAHF() {
        super();
    }

    /**
     * @return the singleton instance of this class
     */
    public static SAHF getInstance() {
        return instance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(IbmPcSystem system, final I8086 cpu) {
        cpu.FLAGS.overlay(cpu.AH.get() & 0b0111_1111);
    }

}
