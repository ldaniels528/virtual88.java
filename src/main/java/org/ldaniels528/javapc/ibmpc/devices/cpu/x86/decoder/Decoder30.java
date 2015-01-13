package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.decoder;

import org.ldaniels528.javapc.ibmpc.devices.cpu.I8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.OpCode;
import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.Operand;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.addressing.DataSegmentOverride;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.bitwise.XOR;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.math.AAA;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.math.CMP;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.math.DAS;
import org.ldaniels528.javapc.ibmpc.devices.memory.X86MemoryProxy;

import static org.ldaniels528.javapc.ibmpc.devices.cpu.x86.decoder.DecoderUtil.*;

/**
 * <pre>
 * Decodes instruction codes between 30h and 3Fh
 * 	---------------------------------------------------------------------------
 * 	type	bits	description 				comments
 * 	---------------------------------------------------------------------------
 * 	t		2		register/reference type		see X86AddressReferenceTypes
 * 	r		3		register info				see X86RegisterReferences
 * 	m		3		memory reference info		see X86AddressReferenceTypes
 * 	i		6	 	instruction type
 * 	s		1		source/signed				register=0,reference=1 / 1='+',0='-'
 * 	c		1	 	memory class  				8-bit=0, 16-bit=1
 * 	d		16/32	offset						(optional)
 *
 *  Instruction code layout
 *  ------------------------------
 *  fedc ba98 7654 3210
 *  		  iiii iisc ( 8 bits)
 *  ttrr rmmm iiii iisc (16 bits)
 *
 * ------------------------------------------------------------------
 * instruction				code 		tt rrr mmm	iiiii v s c dddd
 * ------------------------------------------------------------------
 * xor [bx],al				0730		00 000 111	00110 0 0 0
 * xor [bx],ax				0731		00 000 111 	00110 0 0 1
 * xor al,[bx]				0732		00 000 111	00110 0 1 0
 * xor ax,[bx]				0733		00 000 111 	00110 0 1 1
 *
 * xor al,nn				  34					00110 1 0 0 nn
 * xor ax,nnnn				  35					00110 1 0 1 nnnn
 * ss:						  36					00110 1 1 0
 * aaa						  37					00110 1 1 1
 *
 * cmp [bx],al				  38					00111 0 0 0
 * cmp [bx],ax				0739		00 000 111 	00111 0 0 1
 * cmp al,[bx]				073A		00 000 111	00111 0 1 0
 * cmp ax,[bx]				073B		00 000 111 	00111 0 1 1
 *
 * cmp al,nn				  3C					00111 1 0 0 nn
 * cmp ax,nnnn				  3D					00111 1 0 1 nnnnn
 * ds:						  3E					00111 1 1 0
 * aas						  3F					00111 1 1 1
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 */
public class Decoder30 implements Decoder {
    // define the complex instruction code constants
    private static final int XOR_CODE = 0x06;    // 00110
    private static final int CMP_CODE = 0x07;    // 00111

    /**
     * {@inheritDoc}
     */
    @Override
    public OpCode decode(final I8086 cpu, final X86MemoryProxy proxy, DecodeProcessor processor) {
        // get the byte code
        final int code8 = proxy.nextByte();

        // evaluate the code
        switch (code8) {
            // XOR AL,nn
            case 0x34:
                return new XOR(cpu.AL, nextValue8(proxy));
            // XOR AX,nnnn
            case 0x35:
                return new XOR(cpu.AX, nextValue16(proxy));
            // SS:
            case 0x36:
                return new DataSegmentOverride(cpu.SS, processor.decodeNext());
            // AAA
            case 0x37:
                return AAA.getInstance();
            // CMP AL,nn
            case 0x3C:
                return new CMP(cpu.AL, nextValue8(proxy));
            // CMP AX,nnnn
            case 0x3D:
                return new CMP(cpu.AX, nextValue16(proxy));
            // DS:
            case 0x3E:
                return new DataSegmentOverride(cpu.DS, processor.decodeNext());
            // DAS
            case 0x3F:
                return DAS.getInstance();
            // for all others ...
            default:
                return decodeComplexCode(cpu, proxy, code8);
        }
    }

    /**
     * Decodes complex instruction codes between 00h and 0Fh
     *
     * @param cpu   the given {@link org.ldaniels528.javapc.ibmpc.devices.cpu.I8086 CPU} instance
     * @param proxy the given {@link X86MemoryProxy memory proxy}
     * @param code8 the given 8-bit instruction code
     * @return the resultant {@link OpCode opCode},
     * or <tt>null</tt> if not found
     */
    private OpCode decodeComplexCode(final I8086 cpu, final X86MemoryProxy proxy, final int code8) {
        // instruction code layout
        // -----------------------------
        // fedc ba98 7654 3210 (16 bits)
        // mmrr rmmm iiii ivsc

        // get the 16-bit instruction code
        final int code16 = proxy.nextWord(code8);

        // get data elements
        final Operand[] operands = lookupOperands(cpu, proxy, code16); //lookupOperands( cpu, proxy, code16, true, false );

        // identify the destination and source operands
        final Operand dest = operands[0];
        final Operand src = operands[1];

        // get the instruction code
        // code: .... .... iiii i... (mask = 0000 0000 1111 1000)
        final int insCode = (code16 & 0x00F8) >> 3;

        // evaluate the instruction
        switch (insCode) {
            case XOR_CODE:
                return new XOR(dest, src);
            case CMP_CODE:
                return new CMP(dest, src);
            default:
                throw new UnhandledByteCodeException(code16);
        }
    }

}
