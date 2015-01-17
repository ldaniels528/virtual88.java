/**
 *
 */
package org.ldaniels528.javapc.ibmpc.devices.cpu.opcodes.io;

import org.ldaniels528.javapc.ibmpc.devices.cpu.I8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.opcodes.AbstractOpCode;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

/**
 * <pre>
 * OUTS/OUTSB/OUTSW/OUTSD -- Output String to Port (386+)
 *
 * Opcode   Instruction     Clocks          Description
 * ------   ------------    ------------    ---------------------------------
 * 6E       OUTS DX,r/m8    14,pm=8/28**   	Output byte [(E)SI] to port in DX
 * 6F       OUTS DX,r/m16   14,pm=8/28**   	Output word [(E)SI] to port in DX
 * 6F       OUTS DX,r/m32   14,pm=8/28**   	Output dword [(E)SI] to port in DX
 * 6E       OUTSB           14,pm=8/28**   	Output byte DS:[(E)SI] to port in DX
 * 6F       OUTSW           14,pm=8/28**   	Output word DS:[(E)SI] to port in DX
 * 6F       OUTSD           14,pm=8/28**   	Output dword DS:[(E)SI] to port in DX
 * </pre>
 * See <a href="http://pdos.csail.mit.edu/6.828/2006/readings/i386/OUTS.htm">OUTS</a>
 *
 * @author lawrence.daniels@gmail.com
 */
public class OUTSD extends AbstractOpCode {
    private static OUTSD instance = new OUTSD();

    /**
     * Private constructor
     */
    private OUTSD() {
        super();
    }

    /**
     * @return the singleton instance of this class
     */
    public static OUTSD getInstance() {
        return instance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(IbmPcSystem system, final I8086 cpu) {
        // TODO finish this instruction
    }

}
