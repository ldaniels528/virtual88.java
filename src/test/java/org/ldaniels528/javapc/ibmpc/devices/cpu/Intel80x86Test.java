package org.ldaniels528.javapc.ibmpc.devices.cpu;

import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.WordValue;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.data.MOV;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.stack.POP;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.stack.PUSH;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.system.INT;
import org.ldaniels528.javapc.ibmpc.devices.display.IbmPcDisplayFrame;
import org.ldaniels528.javapc.ibmpc.devices.memory.IbmPcRandomAccessMemory;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystemXT;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.util.Arrays;

import static java.lang.String.format;
import static org.junit.Assert.assertEquals;

/**
 * Intel 80x86 Test Suite
 *
 * @author lawrence.daniels@gmail.com
 */
public class Intel80x86Test {
    private final Logger logger = Logger.getLogger(getClass());

    @Test
    public void testMsDOS_21h() throws Exception {
        final IbmPcDisplayFrame frame = new IbmPcDisplayFrame("Test PC");
        final IbmPcSystem system = new IbmPcSystemXT(frame);
        final Intel80x86 cpu = system.getCPU();
        final IbmPcRandomAccessMemory memory = system.getRandomAccessMemory();

        // insert a text string into memory
        final int segment = 0x13CF;
        final int offset = 0x2000;
        final String text = "Hello World$";
        memory.setBytes(segment, offset, text.getBytes(), text.length());

        // execute the 8086 instructions to print the text string
        system.execute(Arrays.asList(
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
        Thread.sleep(3000);
        frame.dispose();
    }

}
