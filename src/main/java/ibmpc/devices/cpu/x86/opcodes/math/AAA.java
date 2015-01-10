package ibmpc.devices.cpu.x86.opcodes.math;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.X86ExtendedFlags;
import ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;

/**
 * Ascii Adjust for Addition (AAA)
 * <pre>
 * Usage:  AAA
 * Modifies flags: AF CF (OF,PF,SF,ZF undefined)
 *
 * 	Changes contents of AL to valid unpacked decimal.
 * 	The high order nibble is zeroed.
 *
 * 	if low nibble of AL > 9 or AF = 1
 * 	then:
 * 		AL = AL + 6
 * 		AH = AH + 1
 * 		AF = 1
 * 		CF = 1
 * 	else:
 * 		AF = 0
 * 		CF = 0
 *
 * See <a href="http://www.emu8086.com/assembly_language_tutorial_assembler_reference/8086_instruction_set.html">AAA</a>
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 */
public class AAA extends AbstractOpCode {
    private static AAA instance = new AAA();

    /**
     * Private constructor
     */
    private AAA() {
        super();
    }

    /**
     * @return the singleton instance of this class
     */
    public static AAA getInstance() {
        return instance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(final Intel80x86 cpu) {
        // cache the Flags
        final X86ExtendedFlags FLAGS = cpu.FLAGS;

        // get the low nibble of AL (mask = 0000 1111)
        final int lowNibble = (cpu.AL.get() & 0x0F);

		/*
		 	if low nibble of AL > 9 or AF = 1 
		 	then:
				AL = AL + 6
				AH = AH + 1
				AF = 1
				CF = 1
			else:
				AF = 0 and CF = 0
		 */
        final boolean nibbleGreaterThan9 = (lowNibble > 9) || FLAGS.isAF();
        if (nibbleGreaterThan9) {
            cpu.AL.add(6);
            cpu.AH.add(1);
        }

        // set the flags
        FLAGS.setAF(nibbleGreaterThan9);
        FLAGS.setCF(nibbleGreaterThan9);
    }

}