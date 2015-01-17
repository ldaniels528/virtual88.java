package org.ldaniels528.javapc.msdos.services;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ldaniels528.javapc.ibmpc.devices.cpu.I8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.opcodes.data.MOV;
import org.ldaniels528.javapc.ibmpc.devices.cpu.opcodes.system.INT;
import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.WordValue;
import org.ldaniels528.javapc.ibmpc.devices.display.IbmPcDisplayFrame;
import org.ldaniels528.javapc.ibmpc.devices.memory.IbmPcRandomAccessMemory;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystemFactory;

import java.util.Calendar;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

/**
 * MS-DOS System Services Test Suite
 *
 * @author lawrence.daniels@gmail.com
 */
public class MsDosSystemServicesTest {
    private final Logger logger = Logger.getLogger(getClass());
    private final IbmPcDisplayFrame frame = new IbmPcDisplayFrame("Test PC");
    private final IbmPcSystem system = IbmPcSystemFactory.getIBMPCjr(frame);
    private final I8086 cpu = system.getCPU();
    private final IbmPcRandomAccessMemory memory = system.getRandomAccessMemory();
    private final int segment = 0x13F0;

    @Before
    public void setup() {
        cpu.CS.set(segment);
        cpu.DS.set(segment);
        cpu.ES.set(segment);
        cpu.SS.set(segment);
        cpu.SP.set(0xFFFE);
    }

    @After
    public void tearDown() {
        frame.dispose();
    }

    @Test
    public void testGetSystemDate() throws Exception {
        // execute the 8086 instructions to print the text string
        system.execute(asList(
                new MOV(cpu.AH, new WordValue(0x2A)),       // mov ah, 2ah
                new INT(0x21)                               // int 21h
        ));

        // gather the date components from the MS-DOS call
        final int year = cpu.CX.get();
        final int month = cpu.DH.get();
        final int day = cpu.DL.get();
        final int dayOfWeek = cpu.AL.get();

        // gather the date components from the Java Calendar
        final Calendar cal = Calendar.getInstance();
        final int expectedYear = cal.get(Calendar.YEAR);
        final int expectedMonth = cal.get(Calendar.MONTH);
        final int expectedDay = cal.get(Calendar.DAY_OF_MONTH);
        final int expectedDayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

        // validate the date components
        logger.info(format("The year should be %d", expectedYear));
        assertEquals(expectedYear, year);
        logger.info(format("The month should be %d", expectedMonth));
        assertEquals(expectedMonth, month);
        logger.info(format("The day (of the month) should be %d", expectedDay));
        assertEquals(expectedDay, day);
        logger.info(format("The day (of the week) should be %d", expectedDayOfWeek));
        assertEquals(expectedDayOfWeek, dayOfWeek);
    }

    @Test
    public void testPrintString() throws Exception {
        // insert a text string into memory
        final int offset = 0x2000;
        final String text = "Hello World$";
        memory.setBytes(segment, offset, text.getBytes(), text.length());

        // execute the 8086 instructions to print the text string
        system.execute(asList(
                new MOV(cpu.DX, new WordValue(offset)),     // mov dx, offset
                new MOV(cpu.AH, new WordValue(0x09)),       // mov ah, 9h
                new INT(0x21)                               // int 21h
        ));

        logger.info("The registers should match the expected values:");
        logger.info(format("DX should be %04X", offset));
        assertEquals(cpu.DX.get(), offset);
        logger.info(format("AH should be %04X", 0x9));
        assertEquals(cpu.AH.get(), 0x9);

        logger.info("The bytes in memory should match the text string:");
        final byte[] bytes = memory.getBytes(0xB800, 0, 2 * text.length() - 1);
        logger.info(format("bytes = %s", new String(bytes)));
    }

    @Test
    public void testReadString() throws Exception {
        // insert a text string into memory
        final int offset = 0x2000;

        // execute the 8086 instructions to print the text string
        system.execute(asList(
                new MOV(cpu.DX, new WordValue(offset)),     // mov dx, offset
                new MOV(cpu.AH, new WordValue(0x0A)),       // mov ah, 0ah
                new INT(0x21)                               // int 21h
        ));

        final int maxLength = memory.getByte(segment, offset);
        final int length = memory.getByte(segment, offset + 1);
        final String text = new String(memory.getBytes(segment, offset + 2, length));

        logger.info("The data string should match the expected values:");

        final int expectedMaxLength = 255;
        logger.info(format("maximum length should be %d", expectedMaxLength));
        assertEquals(maxLength, expectedMaxLength);

        final int expectedLength = 12;
        logger.info(format("actual length should be %d", expectedLength));
        assertEquals(length, expectedLength);

        final String expectedText = "Hello World\n";
        logger.info(format("the text string should be '%s<CR>'", expectedText.trim()));
        assertEquals(text, expectedText);
    }

}
