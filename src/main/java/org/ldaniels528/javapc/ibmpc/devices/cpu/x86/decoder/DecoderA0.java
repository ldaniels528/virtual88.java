package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.decoder;

import org.ldaniels528.javapc.ibmpc.devices.cpu.Intel80x86;
import org.ldaniels528.javapc.ibmpc.devices.cpu.OpCode;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.data.MOV;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.math.TEST;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.string.*;
import org.ldaniels528.javapc.ibmpc.devices.memory.X86MemoryProxy;

import static org.ldaniels528.javapc.ibmpc.devices.cpu.x86.decoder.DecoderUtil.nextAddressByte;
import static org.ldaniels528.javapc.ibmpc.devices.cpu.x86.decoder.DecoderUtil.nextAddressWord;

/**
 * <pre>
 * Decodes instruction codes A0 thru AF.
 * 	---------------------------------------------------------------------------
 * 	type	bits	description 				comments
 * 	---------------------------------------------------------------------------
 * 	i		4	 	instruction type
 * 	j		3		instruction sub type
 * 	c		1	 	memory class  				8-bit=0, 16-bit=1
 * 	d		8/16	data value					(optional)
 *
 * ---------------------------------------------------------------------------
 * instruction				code 		iiii jjj c dddd
 * ---------------------------------------------------------------------------
 * mov al,[nnnn]			A0nnnn		1010 000 0 nnnn
 * mov ax,[nnnn]			A1nnnn		1010 000 1 nnnn
 * mov [nnnn],al			A2nnnn		1010 001 0 nnnn
 * mov [nnnn],ax			A3nnnn		1010 001 1 nnnn
 * movsb					A4			1010 010 0
 * movsw					A5			1010 010 1
 * cmpsb					A6			1010 011 0
 * cmpsw					A7			1010 011 1
 * test al,nn				A8nn		1010 100 0 nn
 * test ax,nnnn				A9nnnn		1010 100 1 nnnn
 * stosb					AA			1010 101 0
 * stosw					AB			1010 101 1
 * lodsb					AC			1010 110 0
 * lodsw					AD			1010 110 1
 * scasb					AE			1010 111 0
 * scasw					AF			1010 111 1
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 */
public class DecoderA0 implements Decoder {

    /**
     * {@inheritDoc}
     */
    @Override
    public OpCode decode(final Intel80x86 cpu, final X86MemoryProxy proxy) {
        // peek at the next word
        final int code8 = proxy.nextByte();

        // evaluate the code
        switch (code8) {
            // MOV AL,[nnnn]
            case 0xA0:
                return new MOV(cpu.AL, nextAddressByte(cpu, proxy));
            // MOV AX,[nnnn]
            case 0xA1:
                return new MOV(cpu.AX, nextAddressWord(cpu, proxy));
            // MOV [nnnn],AL
            case 0xA2:
                return new MOV(nextAddressByte(cpu, proxy), cpu.AL);
            // MOV [nnnn],AX
            case 0xA3:
                return new MOV(nextAddressWord(cpu, proxy), cpu.AX);
            // MOVSB
            case 0xA4:
                return MOVSB.getInstance();
            // MOVSW
            case 0xA5:
                return MOVSW.getInstance();
            // CMPSB
            case 0xA6:
                return CMPSB.getInstance();
            // CMPSW
            case 0xA7:
                return CMPSW.getInstance();
            // TEST AL,nn
            case 0xA8:
                return new TEST(cpu.AL, nextAddressByte(cpu, proxy));
            // TEST AX,nnnn
            case 0xA9:
                return new TEST(cpu.AX, nextAddressWord(cpu, proxy));
            // STOSB
            case 0xAA:
                return STOSB.getInstance();
            // STOSW
            case 0xAB:
                return STOSW.getInstance();
            // LODSB
            case 0xAC:
                return LODSB.getInstance();
            // LODSW
            case 0xAD:
                return LODSW.getInstance();
            // SCASB
            case 0xAE:
                return SCASB.getInstance();
            // SCASW
            case 0xAF:
                return SCASW.getInstance();
            // unrecognized
            default:
                throw new UnhandledByteCodeException(code8);
        }
    }

}
