package org.ldaniels528.javapc.ibmpc.devices.cpu.operands;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Signed Byte Value Test Suite
 *
 * @author lawrence.daniels@gmail.com
 */
public class SignedByteValueTest {

    @Test
    public void testGet() {
        final SignedByteValue operand = new SignedByteValue(0xFFFF);
        assertEquals(operand.get(), -1);
    }

}
