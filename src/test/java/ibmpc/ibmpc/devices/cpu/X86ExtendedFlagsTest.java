package ibmpc.ibmpc.devices.cpu;

import ibmpc.devices.cpu.X86ExtendedFlags;
import org.apache.log4j.Logger;
import org.junit.Test;

import static java.lang.String.format;
import static org.junit.Assert.*;

/**
 * 80x86 Extended Flags (EFLAGS) Register Test Suite
 *
 * @author lawrence.daniels@gmail.com
 */
public class X86ExtendedFlagsTest {
    private final Logger logger = Logger.getLogger(getClass());

    @Test
    public void testCompareEqual() {
        final X86ExtendedFlags flags = new X86ExtendedFlags();
        final int value0 = 4321;
        final int value1 = 4321;
        flags.compare(value0, value1);

        compareValues(value0, value1, flags);
        logger.info("\tThe Zero flag should be set (ZR)\n");
        assertTrue(flags.isZF());
    }

    @Test
    public void testCompareGreaterThan() {
        final X86ExtendedFlags flags = new X86ExtendedFlags();
        final int value0 = 4321;
        final int value1 = 1234;
        flags.compare(value0, value1);

        compareValues(value0, value1, flags);
        logger.info("\tThe Carry flag should be set (CY)\n");
        assertFalse(flags.isZF());
        assertEquals(flags.isSF(), flags.isOF());
    }

    @Test
    public void testCompareLessThan() {
        final X86ExtendedFlags flags = new X86ExtendedFlags();
        final int value0 = 1234;
        final int value1 = 4321;
        flags.compare(value0, value1);

        compareValues(value0, value1, flags);
        logger.info("\tThe Carry flag should NOT be equal to the Overflow flag\n");
        assertNotEquals(flags.isSF(), flags.isOF());
    }

    private void compareValues(final int value0, final int value1, final X86ExtendedFlags flags) {
        logger.info(format("Given two values: %d and %d", value0, value1));
        logger.info("When comparing the values:");
        logger.info(format("\tFlags: %s", flags));
    }

}
