package org.ldaniels528.javapc.ibmpc.devices.cpu.opcodes.bitwise;

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
 * Roll Right (ROR) Test Suite
 * <pre>
 * 		Usage:  ROR     dest,count
 *      Modifies flags: CF OF
 *
 *         +-----------------------+        +---+
 *     +-->| 7 ----------------> 0 |---+--->| C |
 *     |   +-----------------------+   |    +---+
 *     |                               |
 *     + ------------------------------+
 *
 *      Rotates the bits in the destination to the right "count" times with
 *      all data pushed out the left side re-entering on the left.  The
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
public class RORTest {
    private final Logger logger = Logger.getLogger(getClass());

    @Test
    public void test8Bit() throws Exception {
        logger.info("An 8086 Roll Right (ROR) instruction");
        logger.info("Should correctly rotate bits from left to right");
        final IbmPcDisplayFrame frame = new IbmPcDisplayFrame("Test PC");
        final IbmPcSystem system = IbmPcSystemFactory.getIBMPCjr(frame);
        final I8086 cpu = system.getCPU();

        // set the initial value of AL
        logger.info("Given: AL = 80h");
        cpu.AL.set(0x80);

        // create a re-usable ROR instruction
        final OpCode opCode = new ROR(cpu.AL, new ByteValue(1));

        // perform the ROL 8 times
        for (int n = 1; n <= 8; n++) {
            cpu.execute(system, opCode);
            final String bits = format("%8s", Integer.toBinaryString(cpu.AL.get())).replace(' ', '0');
            logger.info(format("[%02d] %s ~> %s (FLAGS: %s)", n, opCode, bits, cpu.FLAGS));

            // validate the bits in AL and the Carry flag
            if (n == 8) {
                assertEquals(cpu.AL.get(), 0x80);
                assertEquals(cpu.FLAGS.isCF(), true);
            } else {
                assertEquals(cpu.AL.get(), 1 << (7 - n));
                assertEquals(cpu.FLAGS.isCF(), false);
            }
        }
    }

}
