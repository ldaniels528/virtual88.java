package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.decoder;

import org.ldaniels528.javapc.ibmpc.devices.cpu.Intel80x86;
import org.ldaniels528.javapc.ibmpc.devices.cpu.OpCode;
import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.Operand;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.bitwise.OR;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.data.MOVSX;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.math.ADD;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.stack.POP;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.stack.PUSH;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.registers.X86Register;
import org.ldaniels528.javapc.ibmpc.devices.memory.X86MemoryProxy;

import static org.ldaniels528.javapc.ibmpc.devices.cpu.operands.Operand.SIZE_16BIT;
import static org.ldaniels528.javapc.ibmpc.devices.cpu.x86.decoder.DecoderUtil.*;

/**
 * <pre>
 * Decodes instructions code between 00h and 0Fh
 * ---------------------------------------------------------------------------
 * 	type	bits	description 				comments
 * ---------------------------------------------------------------------------
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
 *  		  iiii ixsc ( 8 bits)
 *  ttrr rmmm iiii ixsc (16 bits)
 *
 * ------------------------------------------------------------------
 * instruction				code 		tt rrr mmm	iiiii x s c dddd
 * ------------------------------------------------------------------
 * add	[bx],al				0700		00 000 111 	00000 0 0 0
 * add	[bx],ax				0701		00 000 111 	00000 0 0 1
 * add	al, [bx]			0702		00 000 111	00000 0 1 0
 * add	ax, [bx]			0703		00 000 111	00000 0 1 1
 * add	al,nn				  04 nn					00000 1 0 0 nn
 * add	ax,nnnn				  05 nnnn				00000 1 0 1 nnnn
 * push es					  06					00000 1 1 0
 * pop es					  07					00000 1 1 1
 * or	[bx],al				0708		00 000 111 	00001 0 0 0
 * or	[bx],ax				0709		00 000 111 	00001 0 0 1
 * or	al, [bx]			070A		00 000 111 	00001 0 1 0
 * or	ax, [bx]			070B		00 000 111 	00001 0 1 1
 * or 	al,nn				  0C					00001 1 0 0 nn
 * or 	ax,nnnn				  0D					00001 1 0 1 nnnn
 * push cs					  0E					00001 1 1 0
 * pop cs					  0F					00001 1 1 1
 *
 * ------------------------------------------------------------------
 * instruction (386+)		code 		jjjj jjjj	iiii iiii
 * ------------------------------------------------------------------
 * seto						900F		1001 0000	0000 1111
 * setno					910F		1001 0001	0000 1111
 * setnae/setb/setc			920F		1001 0010	0000 1111
 * setae/setnb/setnc		930F		1001 0011	0000 1111
 * sete/setz				940F		1001 0100	0000 1111
 * setne/setnz				950F		1001 0101	0000 1111
 * setna/setbe				960F		1001 0110	0000 1111
 * seta/setnbe				970F		1001 0111	0000 1111
 * sets						980F		1001 1000	0000 1111
 * setns					990F		1001 1001	0000 1111
 * setp/setpe				9A0F		1001 1010	0000 1111
 * setnp/setpo				9B0F		1001 1011	0000 1111
 * setnge/setl				9C0F		1001 1100	0000 1111
 * setge/setnl				9D0F		1001 1101	0000 1111
 * setng/setle				9E0F		1001 1110	0000 1111
 * setg/setnle				9F0F		1001 1111	0000 1111
 * pop fs					A10F		1010 0001	0000 1111
 * pop gs					A90F		1010 1001	0000 1111
 * bsf ax,cx				BC0F		1011 1100	0000 1111
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 */
public class Decoder00 implements Decoder {
    // define the complex instruction code constants
    private static final int ADD_CODE = 0x00;    // 00000
    private static final int OR_CODE = 0x01; // 00001

    /**
     * {@inheritDoc}
     */
    @Override
    public OpCode decode(final Intel80x86 cpu, final X86MemoryProxy proxy) {
        // get the 8-bit instruction
        final int code8 = proxy.nextByte();

        // decode the instruction
        switch (code8) {
            // ADD AL,nn
            case 0x04:
                return new ADD(cpu.AL, nextValue8(proxy));
            // ADD AX,nnnn
            case 0x05:
                return new ADD(cpu.AX, nextValue16(proxy));
            // PUSH ES
            case 0x06:
                return new PUSH(cpu.ES);
            // POP ES
            case 0x07:
                return new POP(cpu.ES);
            // OR AL,nn
            case 0x0C:
                return new OR(cpu.AL, nextValue8(proxy));
            // OR AX,nnnn
            case 0x0D:
                return new OR(cpu.AX, nextValue16(proxy));
            // PUSH CS
            case 0x0E:
                return new PUSH(cpu.CS);
            // handle the 0Fh instruction
            case 0x0F:
                return decode0F(cpu, proxy);
            // for all others ...
            default:
                return decodeComplexCode(cpu, proxy, code8);
        }
    }

    /**
     * Decode the 80386SX/DX 0F00-0FFF instructions
     *
     * @param cpu   the given {@link Intel80x86 CPU} instance
     * @param proxy the given {@link X86MemoryProxy memory proxy}
     * @return the resultant {@link OpCode opCode}
     * See <a href="http://pdos.csail.mit.edu/6.828/2006/readings/i386/SETcc.htm">SETxx</a>
     */
    private OpCode decode0F(final Intel80x86 cpu, final X86MemoryProxy proxy) {
        // get the 8-bit code
        final int subCode = proxy.nextByte();
        switch (subCode) {
            // POP FS
            case 0xA1:
                return new POP(cpu.FS);
            // POP GS
            case 0xA9:
                return new POP(cpu.GS);
            // MOVSX r16/32,r/m8
            case 0xBE:
                return decodeMOVSX(cpu, proxy, MEM_CLASS_8BIT);
            // MOVSX r32,r/m16
            case 0xBF:
                return decodeMOVSX(cpu, proxy, MEM_CLASS_16BIT);
            // POP CS
            default:
                proxy.moveOffset(-1);
                return new POP(cpu.CS);
        }
    }

    /**
     * Bit Test
     * <pre>
     * -------------------------------
     * instruction	code	iii jj kkk
     * -------------------------------
     * BT			A3		101 00 011
     * BTS 			AB		101 01 011
     * BTR			B3		101 10 011
     * BT?			BA		101 11 010
     * BTC			BB		101 11 011
     * </pre>
     */

    /**
     * Decodes complex instruction codes between 00h and 0Fh
     *
     * @param cpu   the given {@link Intel80x86 CPU} instance
     * @param proxy the given {@link X86MemoryProxy memory proxy}
     * @param code8 the given 8-bit instruction code
     * @return the resultant {@link OpCode opCode}
     */
    private OpCode decodeComplexCode(final Intel80x86 cpu,
                                     final X86MemoryProxy proxy,
                                     final int code8) {
        // instruction code layout
        // -----------------------------
        // fedc ba98 7654 3210 (16 bits)
        // ttrr rmmm iiii ixsc

        // get the 16-bit instruction code
        final int code16 = proxy.nextWord(code8);

        // get data elements
        final Operand[] operands = lookupOperands(cpu, proxy, code16, true, false);

        // identify the destination and source operands
        final Operand dest = operands[0];
        final Operand src = operands[1];

        // get the instruction code
        // code: .... .... iiii i... (mask = 0000 0000 1111 1000)
        final int insCode = (code16 & 0b0000_0000_1111_1000) >> 3;

        // evaluate the instruction
        switch (insCode) {
            case ADD_CODE:
                return new ADD(dest, src);
            case OR_CODE:
                return new OR(dest, src);
            default:
                throw new UnhandledByteCodeException(code16);
        }
    }

    /**
     * MOVSX r16/32,r/m8 | MOVSX r32,r/m16
     *
     * @param cpu         the given {@link Intel80x86 CPU} instance
     * @param proxy       the given {@link X86MemoryProxy memory proxy}
     * @param srcMemClass the memory class of the source operand
     *                    (see {@link DecoderUtil#MEM_CLASS_8BIT}
     *                    and {@link DecoderUtil#MEM_CLASS_16BIT})
     * @return the resultant {@link MOVSX opCode}
     */
    private MOVSX decodeMOVSX(final Intel80x86 cpu, final X86MemoryProxy proxy, final int srcMemClass) {
        // get the operands
        final Operand[] operands = decodeOperands(cpu, proxy, srcMemClass);

        // return opcode
        return new MOVSX(operands[0], operands[1]);
    }

    /**
     * Decodes a 16-bit register
     *
     * @param cpu   the given {@link Intel80x86 CPU} instance
     * @param proxy the given {@link X86MemoryProxy memory proxy}
     * @return the resultant {@link X86Register register}
     */
    private X86Register decodeRegister16(final Intel80x86 cpu, final X86MemoryProxy proxy) {
        // get the destination
        final int regID = proxy.nextByte() & 0b0111; // 0111
        return lookupRegister(cpu, proxy, regID);
    }

    /**
     * Decode the operands
     *
     * @param cpu         the given {@link Intel80x86 CPU} instance
     * @param proxy       the given {@link X86MemoryProxy memory proxy}
     * @param srcMemClass the source memory class
     * @return the resultant {@link Operand operands}
     */
    private Operand[] decodeOperands(final Intel80x86 cpu,
                                     final X86MemoryProxy proxy,
                                     final int srcMemClass) {
        // get the register/reference information
        final int code = proxy.nextByte();

        // get the register code
        // code: ..rr r... (mask = 0011 1000)
        final int regCode = (code & 0b0011_1000) >> 3;

        // get the element and reference codes
        // code: tt.. .mmm (mask = 1100 0111)
        final int elemCode = (code & 0b1100_0111) >> 6;
        final int refCode = (code & 0b0111);

        // get the destination operand
        final X86Register dest = getRegister(cpu, regCode, SIZE_16BIT);

        // get the source operand
        final Operand src = lookupOperand(cpu, proxy, elemCode, refCode, srcMemClass);

        // return the operands
        return new Operand[]{dest, src};
    }

}
