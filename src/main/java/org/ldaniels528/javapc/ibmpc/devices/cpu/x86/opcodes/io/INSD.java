package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.io;

import org.ldaniels528.javapc.ibmpc.devices.cpu.I8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

/**
 * <pre>
 * INS/INSB/INSW/INSD -- Input from Port to String
 *
 * Opcode  Instruction    Clocks         Description
 * 6C      INS r/m8,DX    15,pm=9/29**  Input byte from port DX into ES:(E)DI
 * 6D      INS r/m16,DX   15,pm=9/29**  Input word from port DX into ES:(E)DI
 * 6D      INS r/m32,DX   15,pm=9/29**  Input dword from port DX into ES:(E)DI
 * 6C      INSB           15,pm=9/29**  Input byte from port DX into ES:(E)DI
 * 6D      INSW           15,pm=9/29**  Input word from port DX into ES:(E)DI
 * 6D      INSD           15,pm=9/29**  Input dword from port DX into ES:(E)DI
 * </pre>
 * See <a href="http://pdos.csail.mit.edu/6.828/2006/readings/i386/INS.htm">INS</a>
 *
 * @author lawrence.daniels@gmail.com
 */
public class INSD extends AbstractOpCode {
    private static INSD instance = new INSD();

    /**
     * Private constructor
     */
    private INSD() {
        super();
    }

    /**
     * @return the singleton instance of this class
     */
    public static INSD getInstance() {
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
