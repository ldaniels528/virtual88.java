package org.ldaniels528.javapc.ibmpc.devices.cpu.opcodes.string;

import org.ldaniels528.javapc.ibmpc.devices.cpu.I8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.opcodes.AbstractOpCode;
import org.ldaniels528.javapc.ibmpc.devices.cpu.opcodes.FlagsAffected;
import org.ldaniels528.javapc.ibmpc.devices.cpu.opcodes.RegistersAffected;
import org.ldaniels528.javapc.ibmpc.devices.memory.IbmPcRandomAccessMemory;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

/**
 * Scan String Byte
 * <pre>
 * Usage:  SCAS    string
 *         SCASB
 *         SCASW
 *         SCASD   (386+)
 * Modifies flags: AF CF OF PF SF ZF
 *
 * Compares value at ES:DI (even if operand is specified) from the
 * accumulator and sets the flags similar to a subtraction.  DI is
 * incremented/decremented based on the instruction format (or
 * operand size) and the state of the Direction Flag.  Use with REP
 * prefixes.
 * </pre>
 */
@FlagsAffected({"AF", "CF", "OF", "PF", "SF", "ZF"})
@RegistersAffected({"ES", "DI"})
public class SCASB extends AbstractOpCode {
    private static final SCASB instance = new SCASB();

    /**
     * Private constructor
     */
    private SCASB() {
        super();
    }

    /**
     * @return the singleton instance of this class
     */
    public static SCASB getInstance() {
        return instance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(IbmPcSystem system, final I8086 cpu) {
        // get the memory instance
        final IbmPcRandomAccessMemory memory = cpu.getRandomAccessMemory();

        // compare the byte at ES:[DI] to AL
        cpu.FLAGS.updateSUB(memory.getByte(cpu.ES, cpu.DI), cpu.AL);

        // setup increment/decrement value
        final int delta = cpu.FLAGS.isDF() ? -1 : 1;

        // increment/decrement pointer
        cpu.DI.add(delta);
    }

}