package org.ldaniels528.javapc.ibmpc.devices.cpu.decoders;

import org.ldaniels528.javapc.ibmpc.devices.cpu.I8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.OpCode;
import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.Operand;
import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.memory.BytePtr;
import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.memory.MemoryReference;
import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.memory.WordPtr;
import org.ldaniels528.javapc.ibmpc.devices.cpu.opcodes.addressing.LDS;
import org.ldaniels528.javapc.ibmpc.devices.cpu.opcodes.addressing.LES;
import org.ldaniels528.javapc.ibmpc.devices.cpu.opcodes.data.DB;
import org.ldaniels528.javapc.ibmpc.devices.cpu.opcodes.data.MOV;
import org.ldaniels528.javapc.ibmpc.devices.cpu.opcodes.flow.callret.RET;
import org.ldaniels528.javapc.ibmpc.devices.cpu.opcodes.flow.callret.RETF;
import org.ldaniels528.javapc.ibmpc.devices.cpu.opcodes.flow.callret.RETFn;
import org.ldaniels528.javapc.ibmpc.devices.cpu.opcodes.flow.callret.RETn;
import org.ldaniels528.javapc.ibmpc.devices.cpu.opcodes.system.INT;
import org.ldaniels528.javapc.ibmpc.devices.cpu.opcodes.system.INTO;
import org.ldaniels528.javapc.ibmpc.devices.cpu.opcodes.system.IRET;
import org.ldaniels528.javapc.ibmpc.devices.cpu.registers.X86Register;
import org.ldaniels528.javapc.ibmpc.devices.memory.X86MemoryProxy;

/**
 * <pre>
 * Decodes instruction codes between C0h and CFh
 * 	---------------------------------------------------------------------------
 * 	type	bits		description 				comments
 * 	---------------------------------------------------------------------------
 * 	t		2			register/memory type		register=11b
 * 	j		3			instruction sub-code
 * 	r		3			register/memory reference
 * 	i		6	 		instruction type
 * 	j		2			instruction sub type		00/11=N/A, 01=8-bit, 10=16-bit
 *
 *  Instruction code layout
 *  -----------------------------
 *  7654 3210 (8 bits)
 *  iiii iijj
 *
 * ---------------------------------------------------------------------------
 * instruction						code 		tt rrr mmm	iiiiii jj dddd
 * ---------------------------------------------------------------------------
 * (undefined)				 		  C0					110000 00
 * (undefined)						  C1					110000 01
 * ret nnnn							  C2 					110000 10 nnnn
 * ret								  C3	 				110000 11
 * les	ax,[bx]						07C4		00 000 111 	110001 00
 * lds	ax,[si+nn]					44C5		01 000 100 	110001 01 nn
 * mov byte ptr [bx],nn				07C6		00 000 111	110001 10 nn
 * mov word ptr [bx],nnnn			07C7		00 000 111	110001 11 nnnn
 * mov word ptr [bx+si+nnnn],nnnn	80C7		10 000 000	110001 11 nnnnnnnn
 * enter					 		  C8					110010 00
 * leave							  C9					110010 01
 * retf nnnn				 		  CA 					110010 10 nnnn
 * retf						  		  CB 					110010 11
 * int 3							  CC					110011 00
 * int nn							  CD 					110011 01 nn
 * into								  CE					110011 10
 * iret								  CF					110011 11
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 */
public class DecoderC0 implements Decoder {

    /**
     * {@inheritDoc}
     */
    @Override
    public OpCode decode(final I8086 cpu, final X86MemoryProxy proxy, DecodeProcessor processor) {
        // peek at the next byte
        final int code8 = proxy.nextByte();

        // decode the instruction
        switch (code8) {
            // undefined
            case 0xC0:
                return new DB(code8);
            // undefined
            case 0xC1:
                return new DB(code8);
            // RET count
            case 0xC2:
                return new RETn(proxy.nextWord());
            // RET
            case 0xC3:
                return RET.getInstance();
            // LES dest,src
            case 0xC4:
                return createLES(cpu, proxy, code8);
            // LDS dest,src
            case 0xC5:
                return createLDS(cpu, proxy, code8);
            // MOV byte ptr [ref],nn
            case 0xC6:
                return createMOVBytePtr(cpu, proxy, code8);
            // MOV word ptr [ref],nn
            case 0xC7:
                return createMOVWordPtr(cpu, proxy, code8);
            // RETF count
            case 0xCA:
                return new RETFn(proxy.nextWord());
            // RETF
            case 0xCB:
                return RETF.getInstance();
            // INT 3
            case 0xCC:
                return INT.BREAKPT;
            // INT nn
            case 0xCD:
                return new INT(proxy.nextByte());
            // INTO
            case 0xCE:
                return INTO.getInstance();
            // IRET
            case 0xCF:
                return IRET.getInstance(cpu);
            // unrecognized
            default:
                return new DB(code8);
        }
    }

    /**
     * Creates a new 'LDS <i>dest<i>,<i>src<i>' instruction
     *
     * @param cpu   the given {@link org.ldaniels528.javapc.ibmpc.devices.cpu.I8086 CPU} instance
     * @param proxy the given {@link X86MemoryProxy memory proxy}
     * @param code8 the 8-bit instruction identifier
     * @return an {@link LDS LDS} opCode
     */
    private LDS createLDS(final I8086 cpu, final X86MemoryProxy proxy, final int code8) {
        // code: tt rrr mmm	iiiiii jj dddd
        final int code16 = proxy.nextWord(code8);

        // get the composite reference code
        // code: tt.. .mmm .... .... (mask = 1100 0111 0000 0000)
        final int compCode = (code16 & 0xC700) >> 8;

        // lookup the register
        final X86Register register = DecoderUtil.lookupRegister(cpu, proxy, code16);

        // lookup the operands
        final MemoryReference memref = DecoderUtil.lookupReferencedAddress(cpu, proxy, compCode);

        // return the opCode
        return new LDS(register, memref);
    }

    /**
     * Creates a new 'LES <i>dest<i>,<i>src<i>' instruction
     *
     * @param cpu   the given {@link org.ldaniels528.javapc.ibmpc.devices.cpu.I8086 CPU} instance
     * @param proxy the given {@link X86MemoryProxy memory proxy}
     * @param code8 the 8-bit instruction identifier
     * @return an {@link LES LES} opCode
     */
    private LES createLES(final I8086 cpu, final X86MemoryProxy proxy, final int code8) {
        // code: tt rrr mmm	iiiiii jj dddd
        final int code16 = proxy.nextWord(code8);

        // get the composite reference code
        // code: tt.. .mmm .... .... (mask = 1100 0111 0000 0000)
        final int compCode = (code16 & 0xC700) >> 8;

        // lookup the register
        final X86Register register = DecoderUtil.lookupRegister(cpu, proxy, code16);

        // lookup the operands
        final MemoryReference memref = DecoderUtil.lookupReferencedAddress(cpu, proxy, compCode);

        // return the opCode
        return new LES(register, memref);
    }

    /**
     * Creates a new 'MOV BYTE PTR <i>dest<i>,<i>nn</i>' instruction
     *
     * @param cpu   the given {@link org.ldaniels528.javapc.ibmpc.devices.cpu.I8086 CPU} instance
     * @param proxy the given {@link X86MemoryProxy memory proxy}
     * @param code8 the 8-bit instruction identifier
     * @return an {@link MOV MOV} opCode
     */
    private MOV createMOVBytePtr(final I8086 cpu, final X86MemoryProxy proxy, final int code8) {
        // code: tt rrr mmm	iiiiii jj dddd
        final int code16 = proxy.nextWord(code8);

        // extract the composite code
        // code: tt.. .mmm .... .... (mask = 1100 0111 0000 0000)
        final int compCode = (code16 & 0xC700) >> 8;

        // lookup the operands
        final MemoryReference memref = DecoderUtil.lookupReferencedAddress(cpu, proxy, compCode);

        // get the 16-bit value
        final Operand value = DecoderUtil.nextValue8(proxy);

        // return the opCode
        return new MOV(new BytePtr(memref), value);
    }

    /**
     * Creates a new 'MOV WORD PTR <i>dest<i>,<i>nnnn</i>' instruction
     *
     * @param cpu   the given {@link org.ldaniels528.javapc.ibmpc.devices.cpu.I8086 CPU} instance
     * @param proxy the given {@link X86MemoryProxy memory proxy}
     * @param code8 the 8-bit instruction identifier
     * @return an {@link MOV MOV} opCode
     */
    private MOV createMOVWordPtr(final I8086 cpu, final X86MemoryProxy proxy, final int code8) {
        // code: tt rrr mmm	iiiiii jj dddd
        final int code16 = proxy.nextWord(code8);

        // extract the composite code
        // code: tt.. .mmm .... .... (mask = 1100 0111 0000 0000)
        final int compCode = (code16 & 0xC700) >> 8;

        // lookup the operands
        final MemoryReference memref = DecoderUtil.lookupReferencedAddress(cpu, proxy, compCode);

        // get the 16-bit value
        final Operand value = DecoderUtil.nextValue16(proxy);

        // return the opCode
        return new MOV(new WordPtr(memref), value);
    }

}
