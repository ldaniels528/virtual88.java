package org.ldaniels528.javapc.ibmpc.devices.cpu.opcodes.math;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.ldaniels528.javapc.ibmpc.devices.cpu.I8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.OpCode;
import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.SignedByteValue;
import org.ldaniels528.javapc.ibmpc.devices.display.IbmPcDisplayFrame;
import org.ldaniels528.javapc.ibmpc.exceptions.X86AssemblyException;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystemFactory;

/**
 * <pre>
 * Syntax: ADD dest, src
 * dest: register or memory
 * src: register, memory, or immediate
 * Action: dest = dest + src
 *
 * Flags Affected: OF, SF, ZF, AF, PF, CF
 * Notes: Works for both signed and unsigned numbers.
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 */
public class ADDTest {
    private final Logger logger = Logger.getLogger(getClass());

    @Test
    public void testSignedAdd() throws X86AssemblyException {
        final IbmPcDisplayFrame frame = new IbmPcDisplayFrame("Test PC");
        final IbmPcSystem system = IbmPcSystemFactory.getIBMPCjr(frame);
        final I8086 cpu = system.getCPU();

        cpu.AX.set(0xFFFE);
        final OpCode opCode = new ADD(cpu.AX, new SignedByteValue(22));
        cpu.execute(system, opCode);

        logger.info(cpu.toString());

    }

}
