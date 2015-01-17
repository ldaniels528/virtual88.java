package org.ldaniels528.javapc.ibmpc.devices.cpu.decoders;

import org.ldaniels528.javapc.ibmpc.devices.cpu.I8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.OpCode;
import org.ldaniels528.javapc.ibmpc.devices.cpu.opcodes.stack.POP;
import org.ldaniels528.javapc.ibmpc.devices.cpu.opcodes.stack.PUSH;
import org.ldaniels528.javapc.ibmpc.devices.memory.X86MemoryProxy;

/**
 * <pre>
 * Processes instruction codes between 50h and 5Fh
 * 	---------------------------------------------------------------------------
 * 	type	size		description 			comments
 * 	---------------------------------------------------------------------------
 * 	i		5-bit 		instruction type  		push=01010, pop=01011
 * 	r		3-bit		register index			ax=000
 *
 *  Instruction code layout
 *  -----------------------
 *  7654 3210 (8 bits)
 *  iiii irrr
 *
 * ---------------------------------------------------------------------------
 * instruction			code 	iiiii rrr
 * ---------------------------------------------------------------------------
 * push ax				50		01010 000
 * push cx				51		01010 001
 * push dx				52		01010 010
 * push bx				53		01010 011
 * push sp				54		01010 100
 * push bp				55		01010 101
 * push si				56		01010 110
 * push di				57		01010 111
 * pop ax				58		01011 000
 * pop cx				59		01011 001
 * pop dx				5A		01011 010
 * pop bx				5B		01011 011
 * pop sp				5C		01011 100
 * pop bp				5D		01011 101
 * pop si				5E		01011 110
 * pop di				5F		01011 111
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 */
public class Decoder50 implements Decoder {

    /**
     * {@inheritDoc}
     */
    @Override
    public OpCode decode(final I8086 cpu, X86MemoryProxy proxy, DecodeProcessor processor) {
        // get the next byte code
        final int code8 = proxy.nextByte();

        // interpret the instruction
        switch (code8) {
            // PUSH codes
            case 0x50:
                return new PUSH(cpu.AX);
            case 0x51:
                return new PUSH(cpu.CX);
            case 0x52:
                return new PUSH(cpu.DX);
            case 0x53:
                return new PUSH(cpu.BX);
            case 0x54:
                return new PUSH(cpu.SP);
            case 0x55:
                return new PUSH(cpu.BP);
            case 0x56:
                return new PUSH(cpu.SI);
            case 0x57:
                return new PUSH(cpu.DI);

            // POP codes
            case 0x58:
                return new POP(cpu.AX);
            case 0x59:
                return new POP(cpu.CX);
            case 0x5A:
                return new POP(cpu.DX);
            case 0x5B:
                return new POP(cpu.BX);
            case 0x5C:
                return new POP(cpu.SP);
            case 0x5D:
                return new POP(cpu.BP);
            case 0x5E:
                return new POP(cpu.SI);
            case 0x5F:
                return new POP(cpu.DI);

            // unrecognized
            default:
                throw new UnhandledByteCodeException(code8);
        }
    }

}
