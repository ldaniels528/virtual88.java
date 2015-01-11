package org.ldaniels528.javapc.util;

/**
 * Byte Conversion
 *
 * @author lawrence.daniels@gmail.com
 */
public class ByteConversion {

    public static int convertToInt(final byte b) {
        return (b < 0) ? b + 256 : b;
    }

}
