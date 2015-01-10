package ibmpc.util;

import org.junit.Test;

import static ibmpc.util.BitMaskGenerator.turnBitOffMask;
import static ibmpc.util.BitMaskGenerator.turnBitOnMask;
import static org.junit.Assert.assertEquals;

/**
 * Bit Mask Generator Test Suite
 *
 * @author lawrence.daniels@gmail.com
 */
public class BitMaskGeneratorTest {

    @Test
    public void testTurnBitOnMask() {
        assertEquals(turnBitOnMask(15), 0b1000_0000_0000_0000);
        assertEquals(turnBitOnMask(5), 0b0000_0000_0010_0000);
    }

    @Test
    public void testTurnBitOffMask() {
        assertEquals(turnBitOffMask(16, 5), 0b1111_1111_1101_1111);
        assertEquals(turnBitOffMask(16, 15), 0b0111_1111_1111_1111);
    }

}
