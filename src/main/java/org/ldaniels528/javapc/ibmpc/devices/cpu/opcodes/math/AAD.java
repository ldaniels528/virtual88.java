package org.ldaniels528.javapc.ibmpc.devices.cpu.opcodes.math;

import org.ldaniels528.javapc.ibmpc.devices.cpu.I8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.opcodes.AbstractOpCode;
import org.ldaniels528.javapc.ibmpc.devices.cpu.opcodes.FlagsAffected;
import org.ldaniels528.javapc.ibmpc.devices.cpu.opcodes.RegistersAffected;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

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
@FlagsAffected({"AF", "CF", "OF", "PF", "SF", "ZF"})
@RegistersAffected({"AL", "AH"})
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
    public void execute(IbmPcSystem system, final I8086 cpu) {
        cpu.AL.add(10 * cpu.AH.get());
        cpu.AH.set(0);
    }

}
