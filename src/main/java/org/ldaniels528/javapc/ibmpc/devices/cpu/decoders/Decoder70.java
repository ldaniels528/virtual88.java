package org.ldaniels528.javapc.ibmpc.devices.cpu.decoders;

import org.ldaniels528.javapc.ibmpc.devices.cpu.I8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.OpCode;
import org.ldaniels528.javapc.ibmpc.devices.cpu.opcodes.flow.jump.*;
import org.ldaniels528.javapc.ibmpc.devices.memory.X86MemoryProxy;

/**
 * <pre>
 * Decodes instruction codes between 70h and 7Fh
 * 	---------------------------------------------------------------------------
 * 	type	size		description 			comments
 * 	---------------------------------------------------------------------------
 * 	i		4-bit 		instruction type  		inc=01000,dec=01001
 * 	z		4-bit		unknown					ax=000b, cx=001b
 *
 *  Instruction code layout
 *  -----------------------
 *  7654 3210 (8 bits)
 *  iiii irrr
 *
 * ---------------------------------------------------------------------------
 * instruction			code 	iiii zzzz
 * ---------------------------------------------------------------------------
 * jo  nn				70		0111 0000
 * jno nn				71		0111 0001
 * jc  nn				72		0111 0010
 * jnc nn				73		0111 0011
 * jz  nn				74		0111 0100
 * jnz nn				75		0111 0101
 * jbe nn				76		0111 0110
 * ja  nn				77		0111 0111
 * js  nn				78		0111 1000
 * jns nn				79		0111 1001
 * jpe nn				7A		0111 1010
 * jpo nn				7B		0111 1011
 * jl  nn				7C		0111 1100
 * jge nn				7D		0111 1101
 * jle nn				7E		0111 1110
 * jg  nn				7F		0111 1111
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 */
public class Decoder70 implements Decoder {

    /**
     * {@inheritDoc}
     */
    @Override
    public OpCode decode(final I8086 cpu, final X86MemoryProxy proxy, DecodeProcessor processor) {
        // get the 8-bit instruction code
        final int code8 = proxy.nextByte();

        // evaluate the instruction
        switch (code8) {
            case 0x70:
                return new JO(DecoderUtil.nextAddressShort(proxy));
            case 0x71:
                return new JNO(DecoderUtil.nextAddressShort(proxy));
            case 0x72:
                return new JC(DecoderUtil.nextAddressShort(proxy));
            case 0x73:
                return new JNC(DecoderUtil.nextAddressShort(proxy));
            case 0x74:
                return new JZ(DecoderUtil.nextAddressShort(proxy));
            case 0x75:
                return new JNZ(DecoderUtil.nextAddressShort(proxy));
            case 0x76:
                return new JBE(DecoderUtil.nextAddressShort(proxy));
            case 0x77:
                return new JA(DecoderUtil.nextAddressShort(proxy));
            case 0x78:
                return new JS(DecoderUtil.nextAddressShort(proxy));
            case 0x79:
                return new JNS(DecoderUtil.nextAddressShort(proxy));
            case 0x7A:
                return new JPE(DecoderUtil.nextAddressShort(proxy));
            case 0x7B:
                return new JPO(DecoderUtil.nextAddressShort(proxy));
            case 0x7C:
                return new JL(DecoderUtil.nextAddressShort(proxy));
            case 0x7D:
                return new JGE(DecoderUtil.nextAddressShort(proxy));
            case 0x7E:
                return new JLE(DecoderUtil.nextAddressShort(proxy));
            case 0x7F:
                return new JG(DecoderUtil.nextAddressShort(proxy));
            default:
                throw new UnhandledByteCodeException(code8);
        }
    }

}
