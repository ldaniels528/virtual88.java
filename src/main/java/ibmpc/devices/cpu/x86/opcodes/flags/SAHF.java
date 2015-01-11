package ibmpc.devices.cpu.x86.opcodes.flags;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;
import ibmpc.system.IbmPcSystem;

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
    public void execute(IbmPcSystem system, final Intel80x86 cpu) {
        cpu.FLAGS.overlay(cpu.AH.get() & 0b0111_1111);
    }

}
