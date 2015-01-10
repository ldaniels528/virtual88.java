package jbasic.gwbasic.values;

import org.junit.Test;

import static jbasic.gwbasic.values.GwBasicValues.*;
import static org.junit.Assert.*;

/**
 * GWBASIC Values Test Suite
 *
 * @author lawrence.daniels@gmail.com
 */
public class GwBasicValuesTest {

    @Test
    public void testIsDecimalConstant() {
        assertTrue(isDecimalConstant("1234.11"));
        assertFalse(isDecimalConstant("&H1234"));
    }

    @Test
    public void testIsHexadecimalConstant() {
        assertTrue(isHexadecimalConstant("&H1234"));
        assertFalse(isHexadecimalConstant("&O1234"));
    }

    @Test
    public void testIsIntegerConstant() {
        assertTrue(isIntegerConstant("1234"));
    }

    @Test
    public void testIsNumericConstant() throws Exception {
        assertTrue(isNumericConstant("9876"));
        assertTrue(isNumericConstant("1234.11"));
        assertTrue(isNumericConstant("&H1234"));
        assertTrue(isNumericConstant("&O1234"));
        assertFalse(isNumericConstant("\"Hello World\""));
    }

    @Test
    public void testIsOctalConstant() {
        assertTrue(isOctalConstant("&O1234"));
    }

    @Test
    public void testIsStringConstant() {
        assertTrue(isStringConstant("\"Hello World\""));
        assertFalse(isStringConstant("&O1234"));
    }

    @Test
    public void testParseDecimalString() {
        assertEquals(parseDecimalString("182.17"), 182.17d, 0d);
    }

    @Test
    public void testParseHexadecimalString() {
        assertEquals(parseHexadecimalString("&HABCD"), 0xABCD);
    }

    @Test
    public void testParseIntegerString() throws Exception {
        assertEquals(parseIntegerString("123456"), 123456);
    }

    @Test
    public void testParseOctalString() throws Exception {
        assertEquals(parseOctalString("&O123456"), 0123456);
    }

    @Test
    public void testParseNumericString() throws Exception {
        assertEquals(parseNumericString("1234"), 1234d, 0d);
    }

    @Test
    public void testToUnsignedInteger() {
        assertEquals(toUnsignedInteger(-1234), 64302);
    }

}
