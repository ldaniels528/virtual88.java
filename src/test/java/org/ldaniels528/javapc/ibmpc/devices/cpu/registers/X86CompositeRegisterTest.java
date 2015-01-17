package org.ldaniels528.javapc.ibmpc.devices.cpu.registers;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.ldaniels528.javapc.ibmpc.devices.cpu.registers.X86RegisterSet;

import static java.lang.String.format;
import static org.junit.Assert.assertEquals;

/**
 * i8086 Composite Register Test Suite
 *
 * @author lawrence.daniels@gmail.com
 */
public class X86CompositeRegisterTest {
    private final Logger logger = Logger.getLogger(getClass());

    @Test
    public void testAX() {
        final X86RegisterSet rs = new X86RegisterSet();
        rs.AX.set(0xABCD);

        logger.info("When AX = 0xABCD");
        logger.info(format("AH should be 0xAB"));
        assertEquals(rs.AH.get(), 0xAB);
        logger.info(format("AL should be 0xCD"));
        assertEquals(rs.AL.get(), 0xCD);
    }

    @Test
    public void testAX_ADD() {
        final X86RegisterSet rs = new X86RegisterSet();
        final int value = 0x1234;
        final int delta = 5;
        rs.AX.set(value);

        logger.info(format("When AX = %04Xh and %d is added", value, delta));
        rs.AX.add(delta);
        logger.info(format("AH should be 12h"));
        assertEquals(rs.AH.get(), 0x12);
        logger.info(format("AL should be 39h"));
        assertEquals(rs.AL.get(), 0x39);
    }

    @Test
    public void testAX_AND() {
        final X86RegisterSet rs = new X86RegisterSet();
        rs.AX.set(0xABCD);

        logger.info("When AX is AND'ed with 0xF00F");
        rs.AX.and(0xF00F);
        logger.info(format("AH should be 0xA0"));
        assertEquals(rs.AH.get(), 0xA0);
        logger.info(format("AL should be 0x0D"));
        assertEquals(rs.AL.get(), 0x0D);
    }

    @Test
    public void testAX_OR() {
        final X86RegisterSet rs = new X86RegisterSet();
        rs.AX.set(0xABCD);

        // OR test
        logger.info("When AX is OR'ed with 0xF00F");
        rs.AX.or(0x0BC0);
        logger.info(format("AH should be 0xAB"));
        assertEquals(rs.AH.get(), 0xAB);
        logger.info(format("AL should be 0xCD"));
        assertEquals(rs.AL.get(), 0xCD);
    }

    @Test
    public void testAX_SHL() {
        final X86RegisterSet rs = new X86RegisterSet();
        final int value = 0x1234;
        rs.AX.set(value);

        logger.info(format("When AX = %04Xh and is shifted left by 4 bits", value));
        rs.AX.lshift(4);
        logger.info(format("AH should be 23h"));
        assertEquals(rs.AH.get(), 0x23);
        logger.info(format("AL should be 40h"));
        assertEquals(rs.AL.get(), 0x40);
    }

    @Test
    public void testAX_SHR() {
        final X86RegisterSet rs = new X86RegisterSet();
        final int value = 0x1234;
        rs.AX.set(value);

        logger.info(format("When AX = %04Xh and is shifted right by 4 bits", value));
        rs.AX.rshift(4);
        logger.info(format("AH should be 01h"));
        assertEquals(rs.AH.get(), 0x01);
        logger.info(format("AL should be 23h"));
        assertEquals(rs.AL.get(), 0x23);
    }

    @Test
    public void testAX_SetBit() {
        final X86RegisterSet rs = new X86RegisterSet();
        final int value = 0x1234;
        rs.AX.set(value);

        logger.info(format("When AX = %04Xh and 16th bit is set", value));
        rs.AX.setBit(15, true);
        logger.info(format("AX = %04X", rs.AX.get()));
        logger.info(format("AH should be 92h"));
        assertEquals(rs.AH.get(), 0x92);
        logger.info(format("AL should be 34h"));
        assertEquals(rs.AL.get(), 0x34);
    }

}
