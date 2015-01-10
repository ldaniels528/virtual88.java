package ibmpc.devices.cpu.operands;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Double Word Value Test Suite
 *
 * @author lawrence.daniels@gmail.com
 */
public class DoubleWordValueTest {

    @Test
    public void testGet() {
        final DoubleWordValue operand = new DoubleWordValue(0xACBCD);
        assertEquals(operand.get(), 0xACBCD);
    }

}
