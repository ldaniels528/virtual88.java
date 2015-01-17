package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.bitwise;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.ldaniels528.javapc.ibmpc.devices.cpu.I8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.OpCode;
import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.ByteValue;
import org.ldaniels528.javapc.ibmpc.devices.display.IbmPcDisplayFrame;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystemFactory;

import static java.lang.String.format;
import static org.junit.Assert.assertEquals;

/**
 * Roll Left (ROL) Test Suite
 * <pre>
 * 		Usage:  ROL     dest,count
 *      Modifies flags: CF OF
 *
 * +---+       +-----------------------+
 * | C |<---+--| 7 <---------------- 0 |<--+
 * +---+    |  +-----------------------+   |
 *          |                              |
 *          +------------------------------+
 *
 *      Rotates the bits in the destination to the left "count" times with
 *      all data pushed out the left side re-entering on the right.  The
 *      Carry Flag will contain the value of the last bit rotated out.
 *
 *                                Clocks                 Size
 *      Operands         808x  286   386   486          Bytes
 *
 *      reg,1             2     2     3     3             2
 *      mem,1           15+EA   7     7     4            2-4  (W88=23+EA)
 *      reg,CL           8+4n  5+n    3     3             2
 *      mem,CL        20+EA+4n 8+n    7     4            2-4  (W88=28+EA+4n)
 *      reg,immed8        -    5+n    3     2             3
 *      mem,immed8        -    8+n    7     4            3-5
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 */
public class ROLTest {
    private final Logger logger = Logger.getLogger(getClass());

    @Test
    public void test8Bit() throws Exception {
        logger.info("An 8086 Roll Left (ROL) instruction");
        logger.info("Should correctly rotate bits from right to left");
        final IbmPcDisplayFrame frame = new IbmPcDisplayFrame("Test PC");
        final IbmPcSystem system = IbmPcSystemFactory.getIBMPCjr(frame);
        final I8086 cpu = system.getCPU();

        // set the initial value of AL
        logger.info("Given: AL = 1");
        cpu.AL.set(1);

        // create a re-usable ROL instruction
        final OpCode opCode = new ROL(cpu.AL, new ByteValue(1));

        // perform the ROL 8 times
        for (int n = 1; n <= 8; n++) {
            cpu.execute(system, opCode);
            final String bits = format("%8s", Integer.toBinaryString(cpu.AL.get())).replace(' ', '0');
            logger.info(format("[%02d] %s ~> %s (FLAGS: %s)", n, opCode, bits, cpu.FLAGS));

            // validate the bits in AL and the Carry flag
            if (n == 8) {
                assertEquals(cpu.AL.get(), 1);
                assertEquals(cpu.FLAGS.isCF(), true);
            } else {
                assertEquals(cpu.AL.get(), 1 << n);
                assertEquals(cpu.FLAGS.isCF(), false);
            }
        }
    }

}
