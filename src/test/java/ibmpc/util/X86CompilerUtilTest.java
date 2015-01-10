package ibmpc.util;

import org.junit.Test;

import static ibmpc.util.X86CompilerUtil.getMemoryOffset;
import static ibmpc.util.X86CompilerUtil.isNumber;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * 80x86 Compiler Utilities Test Suite
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
