/**
 *
 */
package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.math;

import org.ldaniels528.javapc.ibmpc.devices.cpu.I8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

/**
 * <h4>DAA</h4>
 * These instructions are used in conjunction with the add and subtract instructions to perform
 * binary-coded decimal arithmetic in packed (one BCD digit per nibble) form. For the unpacked
 * equivalents, see Section B.4.
 * <p/>
 * DAA should be used after a one-byte ADD instruction whose destination was the AL register:
 * by means of examining the value in the AL and also the auxiliary carry flag AF, it determines
 * whether either digit of the addition has overflowed, and adjusts it (and sets the carry and
 * auxiliary-carry flags) if so. You can add long BCD strings together by doing ADD/DAA on the
 * low two digits, then doing ADC/DAA on each subsequent pair of digits.
 * <p/>
 * DAS works similarly to DAA, but is for use after SUB instructions rather than ADD.
 * <p/>
 * The daa instruction is used to adjust the content of the AL register after that register is
 * used to perform the addition of two packed BCDs. The CPU uses the following logic:
 * <pre>
 *  CF_old = CF
 *  IF (al AND 0Fh > 9) or (the Auxilliary Flag is set)
 *      al = al+6
 *      CF = CF or CF_old
 *      AF set
 *  ENDIF
 *  IF (al > 99h) or (Carry Flag is set)
 *      al = al + 60h
 *      CF set
 *  ENDIF
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 * @see org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.math.DAS
 */
public class DAA extends AbstractOpCode {
    private static DAA instance = new DAA();

    /**
     * Private constructor
     */
    private DAA() {
        super();
    }

    /**
     * @return the singleton instance of this class
     */
    public static DAA getInstance() {
        return instance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(final IbmPcSystem system, final I8086 cpu) {
        // save the state of CF
        final boolean oldCF = cpu.FLAGS.isCF();

        // http://www.ray.masmcode.com/BCDdaa.html
        if (cpu.FLAGS.isAF() || (cpu.AL.get() & 0x0F) > 9) {
            cpu.AL.add(6);
            cpu.FLAGS.setCF(cpu.FLAGS.isCF() || oldCF);
            cpu.FLAGS.setAF(true);
        }

        if (cpu.FLAGS.isCF() || cpu.AL.get() > 0x99) {
            cpu.AL.add(6);
            cpu.FLAGS.setCF(true);
        }
    }

}
