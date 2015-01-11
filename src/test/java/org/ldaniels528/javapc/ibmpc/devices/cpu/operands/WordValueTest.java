package org.ldaniels528.javapc.ibmpc.devices.cpu.operands;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Byte Value Test Suite
 *
 * @author lawrence.daniels@gmail.com
 */
public class WordValueTest {

    @Test
    public void testGet() {
        final WordValue operand = new WordValue(0xFFFF);
        assertEquals(operand.get(), 0xFFFF);
    }

}
