package org.ldaniels528.javapc.msdos.services;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.ldaniels528.javapc.ibmpc.devices.cpu.I8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.WordValue;
import org.ldaniels528.javapc.ibmpc.devices.cpu.opcodes.data.MOV;
import org.ldaniels528.javapc.ibmpc.devices.cpu.opcodes.stack.POP;
import org.ldaniels528.javapc.ibmpc.devices.cpu.opcodes.stack.PUSH;
import org.ldaniels528.javapc.ibmpc.devices.cpu.opcodes.system.INT;
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

    @Test
    public void testGetSystemDate() throws Exception {
        final IbmPcDisplayFrame frame = new IbmPcDisplayFrame("Test PC");
        final IbmPcSystem system = IbmPcSystemFactory.getIBMPCjr(frame);
        final I8086 cpu = system.getCPU();

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
        frame.dispose();
    }

    @Test
    public void testMsDOS_21h() throws Exception {
        final IbmPcDisplayFrame frame = new IbmPcDisplayFrame("Test PC");
        final IbmPcSystem system = IbmPcSystemFactory.getIBMPCjr(frame);
        final I8086 cpu = system.getCPU();
        final IbmPcRandomAccessMemory memory = system.getRandomAccessMemory();

        // insert a text string into memory
        final int segment = 0x13CF;
        final int offset = 0x2000;
        final String text = "Hello World$";
        memory.setBytes(segment, offset, text.getBytes(), text.length());

        // execute the 8086 instructions to print the text string
        system.execute(asList(
                new MOV(cpu.CX, new WordValue(segment)),    // mov ax, segment
                new PUSH(cpu.CX),                           // push ax
                new POP(cpu.DS),                            // pop ds
                new MOV(cpu.DX, new WordValue(offset)),     // mov dx, offset
                new MOV(cpu.AH, new WordValue(0x09)),       // mov ah, 9h
                new INT(0x21)                               // int 21h
        ));

        logger.info("The registers should match the expected values:");
        logger.info(format("CX should be %04X", segment));
        assertEquals(cpu.CX.get(), segment);
        logger.info(format("DS should be %04X", segment));
        assertEquals(cpu.DS.get(), segment);
        logger.info(format("DX should be %04X", offset));
        assertEquals(cpu.DX.get(), offset);
        logger.info(format("AH should be %04X", 0x9));
        assertEquals(cpu.AH.get(), 0x9);
        frame.dispose();
    }

}
