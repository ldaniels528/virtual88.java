package ibmpc.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * Bit Mask Generator Test Suite
 *
 * @author lawrence.daniels@gmail.com
 */
public class BitMaskGeneratorTest {

    @Test
    public void turnBitOnMask() {
        Assert.assertEquals(BitMaskGenerator.turnBitOnMask(15), 0b1000_0000_0000_0000);
        Assert.assertEquals(BitMaskGenerator.turnBitOnMask(5), 0b0000_0000_0010_0000);
    }

    @Test
    public void turnBitOffMask() {
        Assert.assertEquals(BitMaskGenerator.turnBitOffMask(16, 5), 0b1111_1111_1101_1111);
        Assert.assertEquals(BitMaskGenerator.turnBitOffMask(16, 15), 0b0111_1111_1111_1111);
    }

}
