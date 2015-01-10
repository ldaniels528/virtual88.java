package ibmpc.devices.cpu.x86.opcodes.math;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;

/**
 * <pre>
 * Ascii Adjust for Division
 *
 * Usage:  AAD
 *  Modifies flags: SF ZF PF (AF,CF,OF undefined)
 *
 *  Used before dividing unpacked decimal numbers.   Multiplies AH by
 *  10 and the adds result into AL.  Sets AH to zero.  This instruction
 *  is also known to have an undocumented behavior.
 *
 *  AL = ( 10 * AH ) + AL
 *  AH = 0
 *
 * See <a href="http://www.emu8086.com/assembly_language_tutorial_assembler_reference/8086_instruction_set.html">AAD</a>
 *  </pre>
 *
 * @author lawrence.daniels@gmail.com
 */
public class AAD extends AbstractOpCode {
    private static AAD instance = new AAD();

    /**
     * Private constructor
     */
    private AAD() {
        super();
    }

    /**
     * @return the singleton instance of this class
     */
    public static AAD getInstance() {
        return instance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(final Intel80x86 cpu) {
        cpu.AL.add(10 * cpu.AH.get());
        cpu.AH.set(0);
    }

}
