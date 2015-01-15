package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.registers;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.ldaniels528.javapc.ibmpc.devices.cpu.X86RegisterSet;

import static org.ldaniels528.javapc.util.BitMaskGenerator.*;
import static java.lang.String.format;
import static org.junit.Assert.assertEquals;

/**
 * i8086 16-bit Register Test Suite
 *
 * @author lawrence.daniels@gmail.com
 */
public class X86Register16bitTest {
    private final Logger logger = Logger.getLogger(getClass());

    @Test
    public void testSI_ADD() {
        final X86RegisterSet rs = new X86RegisterSet();
        final int value = 0x1234;
        final int modifier = 5;
        final int expectedValue = value + modifier;
        rs.SI.set(value);

        logger.info(format("When SI = %04Xh and increased by %d", value, modifier));
        rs.SI.add(modifier);
        logger.info(format("SI should be %04X", expectedValue));
        assertEquals(rs.SI.get(), expectedValue);
    }

    @Test
    public void testSI_AND() {
        final X86RegisterSet rs = new X86RegisterSet();
        final int value = 0xABCD;
        final int modifier = 0xF00F;
        final int expectedValue = value & modifier;
        rs.SI.set(value);

        logger.info(format("When SI = %04Xh and AND'ed with %04X", value, modifier));
        rs.SI.and(modifier);
        logger.info(format("SI should be %04X", expectedValue));
        assertEquals(rs.SI.get(), expectedValue);
    }

    @Test
    public void testSI_OR() {
        final X86RegisterSet rs = new X86RegisterSet();
        final int value = 0x1234;
        final int modifier = 0xF00F;
        final int expectedValue = value | modifier;
        rs.SI.set(value);

        logger.info(format("When SI = %04Xh and OR'ed with %04X", value, modifier));
        rs.SI.or(modifier);
        logger.info(format("SI should be %04X", expectedValue));
        assertEquals(rs.SI.get(), expectedValue);
    }

    @Test
    public void testSI_SHL() {
        final X86RegisterSet rs = new X86RegisterSet();
        final int value = 0x1234;
        final int modifier = 4;
        final int expectedValue = value << modifier;
        rs.SI.set(value);

        logger.info(format("When SI = %04X and is shifted left by %d bits", value, modifier));
        rs.SI.lshift(modifier);
        logger.info(format("SI should be %04X", expectedValue));
        assertEquals(rs.SI.get(), expectedValue);
    }

    @Test
    public void testSI_SHR() {
        final X86RegisterSet rs = new X86RegisterSet();
        final int value = 0x1234;
        final int modifier = 4;
        final int expectedValue = value >> modifier;
        rs.SI.set(value);

        logger.info(format("When SI = %04X and is shifted right by %d bits", value, modifier));
        rs.SI.rshift(modifier);
        logger.info(format("SI should be %04X", expectedValue));
        assertEquals(rs.SI.get(), expectedValue);
    }

    @Test
    public void testSI_SetBit() {
        final X86RegisterSet rs = new X86RegisterSet();
        final int value = 0x1234;
        final int modifier = 15;
        final int expectedValue = value | turnBitOnMask(modifier);
        rs.SI.set(value);

        logger.info(format("When SI = %04Xh and %dth bit is set", value, modifier + 1));
        rs.SI.setBit(modifier, true);
        logger.info(format("SI should be %04X", expectedValue));
        assertEquals(rs.SI.get(), expectedValue);
    }

}
