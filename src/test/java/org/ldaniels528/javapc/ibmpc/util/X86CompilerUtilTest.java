package org.ldaniels528.javapc.ibmpc.util;

import org.junit.Test;

import static org.ldaniels528.javapc.ibmpc.util.X86CompilerUtil.getMemoryOffset;
import static org.ldaniels528.javapc.ibmpc.util.X86CompilerUtil.isNumber;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * 8086 Compiler Utilities Test Suite
 *
 * @author lawrence.daniels@gmail.com
 */
public class X86CompilerUtilTest {

    @Test
    public void testIsNumber() {
        assertTrue(isNumber("1234"));
        assertTrue(isNumber("&O1234"));
        assertTrue(isNumber("&H1234"));
    }

    @Test
    public void testGetMemoryOffset() throws Exception {
        assertEquals(getMemoryOffset("[&H1234]"), 0x1234);
    }

}
