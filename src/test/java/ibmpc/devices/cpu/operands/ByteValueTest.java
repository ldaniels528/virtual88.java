package ibmpc.devices.cpu.operands;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Byte Value Test Suite
 *
 * @author lawrence.daniels@gmail.com
 */
public class ByteValueTest {

    @Test
    public void testGet() {
        final ByteValue operand = new ByteValue(0xFFFF);
        assertEquals(operand.get(), 255);
    }

}
