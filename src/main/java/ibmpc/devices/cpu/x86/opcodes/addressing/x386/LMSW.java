package ibmpc.devices.cpu.x86.opcodes.addressing.x386;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.operands.Operand;
import ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;
import ibmpc.exceptions.X86AssemblyException;

/**
 * <pre>
 * LMSW - Load Machine Status Word (286+ privileged)
 *
 *  Usage:  LMSW    src
 *  Modifies flags: None
 *
 * Overview
 *
 * Loads the Machine Status Word (MSW) from data found at "src"
 *
 * Description
 *
 * LMSW loads the machine status word (part of CR0) from the source operand.
 * This instruction can be used to switch to Protected Mode; if so, it must be
 * followed by an intrasegment jump to flush the instruction queue. LMSW will
 * not switch back to Real Address Mode.
 *
 * LMSW is used only in operating system software. It is not used in
 * application programs.
 *
 * Opcode      Instruction      Clocks   Description
 * 0F  01 /6   LMSW r/m16       10/13    Load r/m16 in machine status word
 *
 *                           Clocks                 Size
 * 	Operands         808x  286   386   486          Bytes
 * 	reg16             -     3     10    13            3
 *  mem16             -     6     13    13            5
 * </pre>
 * See <a href="http://pdos.csail.mit.edu/6.828/2006/readings/i386/LMSW.htm">LMSW</a>
 *
 * @author lawrence.daniels@gmail.com
 */
public class LMSW extends AbstractOpCode {
    private Operand src;

    /**
     * Creates a new LMSW instruction
     *
     * @param src the given source
     */
    public LMSW(final Operand src) {
        this.src = src;
    }

    /* (non-Javadoc)
     * @see ibmpc.devices.cpu.OpCode#execute(ibmpc.devices.cpu.Intel80x86)
     */
    public void execute(final Intel80x86 cpu)
            throws X86AssemblyException {
        cpu.MSW.overlay(src.get());
    }

    /*
     * (non-Javadoc)
     * @see ibmpc.devices.cpu.x86.opcodes.AbstractOpCode#toString()
     */
    public String toString() {
        return String.format("LMSW %s", src);
    }

}
