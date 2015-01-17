package org.ldaniels528.javapc.ibmpc.devices.cpu.registers;

import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.Operand;
import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.WordValue;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.ldaniels528.javapc.ibmpc.devices.cpu.registers.X86Flags;

import static java.lang.String.format;
import static org.junit.Assert.*;

/**
 * 8086 Flags (FLAGS) Register Test Suite
 *
 * @author lawrence.daniels@gmail.com
 */
public class X86FlagsTest {
    private final Logger logger = Logger.getLogger(getClass());

    @Test
    public void testCarryFlag() {
        final X86Flags flags = new X86Flags();
        flags.setCF(false);
        logger.info("Given a set of nominal flags:");
        logger.info(format("\tFlags: %s", flags));
        assertFalse(flags.isCF());

        logger.info("When setting CF = 1");
        flags.setCF(true);
        logger.info(format("\tFlags: %s", flags));
        assertTrue(flags.isCF());
    }

    @Test
    public void testOverflowFlag() {
        final X86Flags flags = new X86Flags();
        flags.setOF(false);
        logger.info("Given a set of nominal flags:");
        logger.info(format("\tFlags: %s", flags));
        assertFalse(flags.isOF());

        logger.info("When setting OF = 1");
        flags.setOF(true);
        logger.info(format("\tFlags: %s", flags));
        assertTrue(flags.isOF());
    }

    @Test
    public void testSignFlag() {
        final X86Flags flags = new X86Flags();
        flags.setSF(false);
        logger.info("Given a set of nominal flags:");
        logger.info(format("\tFlags: %s", flags));
        assertFalse(flags.isSF());

        logger.info("When setting SF = 1");
        flags.setSF(true);
        logger.info(format("\tFlags: %s", flags));
        assertTrue(flags.isSF());
    }

    @Test
    public void testTrapFlag() {
        final X86Flags flags = new X86Flags();
        flags.setTF(false);
        logger.info("Given a set of nominal flags:");
        logger.info(format("\tFlags: %s", flags));
        assertFalse(flags.isTF());

        logger.info("When setting TF = 1");
        flags.setTF(true);
        logger.info(format("\tFlags: %s", flags));
        assertTrue(flags.isTF());
    }

    @Test
    public void testZeroFlag() {
        final X86Flags flags = new X86Flags();
        flags.setZF(false);
        logger.info("Given a set of nominal flags:");
        logger.info(format("\tFlags: %s", flags));
        assertFalse(flags.isZF());

        logger.info("When setting ZF = 1");
        flags.setZF(true);
        logger.info(format("\tFlags: %s", flags));
        assertTrue(flags.isZF());
    }

    @Test
    public void testUpdateADD() {
        final Operand dst = new WordValue(0x1000);
        final Operand src = new WordValue(0x1321);
        logger.info(format("Given two values: %s and %s", dst, src));

        logger.info("And a set of nominal flags:");
        final X86Flags flags = new X86Flags();
        flags.setZF(true).setCF(false);
        logger.info(format("\tFlags: %s", flags));

        logger.info("When adding the values:");
        final int result = flags.updateADD(dst, src);

        logger.info(format("\tThe result is %04X", result));
        assertEquals(result, 0x2321);
        logger.info("\tThe Zero flag should be set (ZR)");
        assertFalse(flags.isZF());
        logger.info(format("\tFlags: %s", flags));
        assertTrue(flags.isCF());
    }

    @Test
    public void testUpdateAND() {
        final Operand dst = new WordValue(0x1110);
        final Operand src = new WordValue(0x1321);
        logger.info(format("Given two values: %s and %s", dst, src));

        logger.info("And a set of nominal flags:");
        final X86Flags flags = new X86Flags();
        flags.setZF(true);
        logger.info(format("\tFlags: %s", flags));

        logger.info("When ANDing the values:");
        final int result = flags.updateAND(dst, src);

        logger.info(format("\tThe result is %04X", result));
        assertEquals(result, 0x1100);
        logger.info("\tThe Zero flag should be set (ZR)");
        logger.info(format("\tFlags: %s", flags));
        assertFalse(flags.isZF());
    }

    @Test
    public void testUpdateCMPEqual() {
        final Operand dst = new WordValue(0x4321);
        final Operand src = new WordValue(0x4321);
        logger.info(format("Given two values: %s and %s", dst, src));

        logger.info("And a set of nominal flags:");
        final X86Flags flags = new X86Flags();
        flags.setZF(false);
        logger.info(format("\tFlags: %s", flags));

        logger.info("When comparing the values:");
        flags.updateSUB(dst, src);

        logger.info("\tThe Zero flag should be set (ZR)");
        logger.info(format("\tFlags: %s", flags));
        assertTrue(flags.isZF());
    }

    @Test
    public void testUpdateCMPGreaterThan() {
        final Operand dst = new WordValue(0x4321);
        final Operand src = new WordValue(0x1234);
        logger.info(format("Given two values: %s and %s", dst, src));

        logger.info("And a set of nominal flags:");
        final X86Flags flags = new X86Flags();
        flags.setOF(false).setSF(false).setZF(true);
        logger.info(format("\tFlags: %s", flags));

        logger.info("When comparing the values:");
        flags.updateSUB(dst, src);

        logger.info(format("\tFlags: %s", flags));
        logger.info("\tZF should equal false");
        assertFalse(flags.isZF());
        logger.info("\tSF should equal OF");
        assertEquals(flags.isSF(), flags.isOF());
    }

    @Test
    public void testUpdateCMPLessThan() {
        final X86Flags flags = new X86Flags();
        final Operand dst = new WordValue(0x1234);
        final Operand src = new WordValue(0x4321);
        flags.updateSUB(dst, src);

        endValues(dst, src, flags, "comparing");
        logger.info("\tThe Carry and Overflow flags should NOT be equal\n");
        assertFalse(flags.isZF());
//        assertNotEquals(flags.isSF(), flags.isOF());
    }

    private void startValues(final Operand dst, final Operand src, final X86Flags flags, final String verb) {
        logger.info(format("Given two values: %s and %s", dst, src));
        logger.info("And a set of nominal flags:");
        logger.info(format("\tFlags: %s", flags));
        logger.info(format("When %s the values:", verb));
        logger.info(format("\tFlags: %s", flags));
    }

    private void endValues(final Operand dst, final Operand src, final X86Flags flags, final String verb) {
        logger.info(format("Given two values: %s and %s", dst, src));
        logger.info("And a set of nominal flags:");
        logger.info(format("\tFlags: %s", flags));
        logger.info(format("When %s the values:", verb));
        logger.info(format("\tFlags: %s", flags));
    }

}
