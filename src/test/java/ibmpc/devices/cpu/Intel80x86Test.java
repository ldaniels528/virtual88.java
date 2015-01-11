package ibmpc.devices.cpu;

import ibmpc.devices.cpu.operands.WordValue;
import ibmpc.devices.cpu.x86.opcodes.data.MOV;
import ibmpc.devices.cpu.x86.opcodes.stack.POP;
import ibmpc.devices.cpu.x86.opcodes.stack.PUSH;
import ibmpc.devices.cpu.x86.opcodes.system.INT;
import ibmpc.devices.display.IbmPcDisplayFrame;
import ibmpc.devices.memory.IbmPcRandomAccessMemory;
import ibmpc.system.IbmPcSystem;
import ibmpc.system.IbmPcSystemXT;
import org.apache.log4j.Logger;
import org.junit.Test;

import static java.lang.String.format;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Intel 80x86 Test Suite
 *
 * @author lawrence.daniels@gmail.com
 */
public class Intel80x86Test {
    private final Logger logger = Logger.getLogger(getClass());

    @Test
    public void test() throws Exception {
        final IbmPcDisplayFrame frame = new IbmPcDisplayFrame("Test PC");
        final IbmPcSystem system = new IbmPcSystemXT(frame);
        final Intel80x86 cpu = system.getCPU();

        final List<OpCode> opCodes = Arrays.asList(
                new MOV(cpu.AX, new WordValue(0xABCD))
        );

        for (final OpCode opCode : opCodes) {
            cpu.execute(system, opCode);
        }

        assertEquals(cpu.AL.get(), 0xCD);
        assertEquals(cpu.AH.get(), 0xAB);

        logger.info(cpu.FLAGS);
        frame.dispose();
    }

    @Test
    public void testMsDOS_21h() throws Exception {
        final IbmPcDisplayFrame frame = new IbmPcDisplayFrame("Test PC");
        final IbmPcSystem system = new IbmPcSystemXT(frame);
        final Intel80x86 cpu = system.getCPU();
        final IbmPcRandomAccessMemory memory = system.getRandomAccessMemory();

        // place a text string to print
        final int segment = 0x13CF;
        final int offset = 0x2000;
        final String text = "Hello World$";
        memory.setBytes(segment, offset, text.getBytes(), text.length());

        // create the instructions to execute
        final List<OpCode> opCodes = Arrays.asList(
                new MOV(cpu.CX, new WordValue(segment)),    // mov ax, segment
                new PUSH(cpu.CX),                           // push ax
                new POP(cpu.DS),                            // pop ds
                new MOV(cpu.DX, new WordValue(offset)),     // mov dx, offset
                new MOV(cpu.AH, new WordValue(0x09)),       // mov ah, 9h
                new INT(0x21)                               // int 21h
        );

        for (final OpCode opCode : opCodes) {
            cpu.execute(system, opCode);
        }

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
    }

}
