package org.ldaniels528.javapc.ibmpc.util;

/**
 * This class generates bit masks for turning bits on or off
 *
 * @author lawrence.daniels@gmail.com
 */
public class BitMaskGenerator {

    /**
     * Indicates whether the bit is set for the given value and specified bit number
     *
     * @param value the given value
     * @param nth   the specified bit number (starts at zero)
     * @return true, if the nth bit is set.
     */
    public static boolean isBitSet(final int value, final int nth) {
        return (value & turnBitOnMask(nth)) > 0;
    }

    /**
     * Generates a masking for retrieving (ANDing) the status
     * of the given bit number
     *
     * @param nth the given bit number
     * @return the generated AND mask
     */
    public static int turnBitOnMask(final int nth) {
        return (1 << nth);
    }

    /**
     * Generates a masking for retrieving (ORing) the status
     * of the given bit number
     *
     * @param size      the size (in bits) of the value being operated on
     * @param nth the given bit number
     * @return the generated OR mask
     */
    public static int turnBitOffMask(final int size, final int nth) {
        return ((1 << size) - 1) - (1 << nth);
    }

}
