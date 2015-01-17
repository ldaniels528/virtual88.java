package org.ldaniels528.javapc.ibmpc.devices.cpu.opcodes.flags;

import org.ldaniels528.javapc.ibmpc.devices.cpu.I8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.opcodes.AbstractOpCode;
import org.ldaniels528.javapc.ibmpc.devices.cpu.opcodes.FlagsAffected;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

/**
 * <pre>
 * Complement Carry Flag
 *
 * Usage:  CMC
 * Modifies flags: CF
 *
 * Toggles the Carry Flag from 0 to 1 or 1 to 0.
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 */
@FlagsAffected({"CF"})
public class CMC extends AbstractOpCode {
    private static final CMC instance = new CMC();

    /**
     * Private constructor
     */
    private CMC() {
        super();
    }

    /**
     * @return the singleton instance of this class
     */
    public static CMC getInstance() {
        return instance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(IbmPcSystem system, final I8086 cpu) {
        cpu.FLAGS.setCF(!cpu.FLAGS.isCF());
    }

}
