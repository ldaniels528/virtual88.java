package org.ldaniels528.javapc.ibmpc.devices.cpu.opcodes.data;

import org.ldaniels528.javapc.ibmpc.devices.cpu.I8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.opcodes.AbstractOpCode;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

/**
 * <pre>
 * Usage:  XLAT    translation-table
 *         XLATB   (masm 5.x)
 *  Modifies flags: None
 *
 *  Replaces the byte in AL with byte from a user table addressed by
 *  BX.  The original value of AL is the index into the translate table.
 *  The best way to describe this is MOV AL,[BX+AL]
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 */
public class XLAT extends AbstractOpCode {
    private static XLAT instance = new XLAT();

    /**
     * Private constructor
     */
    private XLAT() {
        super();
    }

    /**
     * @return the singleton instance of this class
     */
    public static XLAT getInstance() {
        return instance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(IbmPcSystem system, final I8086 cpu) {
        // compute the offset
        final int offset = cpu.BX.get() + cpu.AL.get();

        // get the byte value from the translation table
        final int value = cpu.getByte(offset);

        // put the value into AL
        cpu.AL.set(value);
    }

}
