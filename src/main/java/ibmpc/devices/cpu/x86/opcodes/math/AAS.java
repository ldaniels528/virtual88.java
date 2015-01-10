package ibmpc.devices.cpu.x86.opcodes.math;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.X86ExtendedFlags;
import ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;

/**
 * <pre>
 * Ascii Adjust for Subtraction
 *
 * Usage:  AAS
 * Modifies flags: AF CF (OF,PF,SF,ZF undefined)
 *
 * 	Corrects result of a previous unpacked decimal subtraction in AL.
 * 	High order nibble is zeroed.
 *
 * 	if low nibble of AL > 9 or AF = 1
 * 	then:
 * 		AL = AL - 6
 * 		AH = AH - 1
 * 		AF = 1
 * 		CF = 1
 * 	else:
 * 		AF = 0
 * 		CF = 0
 *
 * See <a href="http://www.emu8086.com/assembly_language_tutorial_assembler_reference/8086_instruction_set.html">AAS</a>
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 */
public class AAS extends AbstractOpCode {
    private static AAS instance = new AAS();

    /**
     * Private constructor
     */
    private AAS() {
        super();
    }

    /**
     * @return the singleton instance of this class
     */
    public static AAS getInstance() {
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
				AL = AL - 6
				AH = AH - 1
				AF = 1
				CF = 1
			else:
				AF = 0
				CF = 0
		 */
        final boolean nibbleGreaterThan9 = (lowNibble > 9) || FLAGS.isAF();
        if (nibbleGreaterThan9) {
            cpu.AL.add(-6);
            cpu.AH.add(-1);
        }

        // set the flags
        FLAGS.setAF(nibbleGreaterThan9);
        FLAGS.setCF(nibbleGreaterThan9);
    }

}
