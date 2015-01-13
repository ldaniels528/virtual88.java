package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.decoder;

import org.ldaniels528.javapc.ibmpc.devices.cpu.I8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.OpCode;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.bitwise.NOP;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.data.CBW;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.data.CWD;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.data.XCHG;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.flags.LAHF;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.flags.SAHF;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.flow.callret.CALL;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.stack.POPF;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.stack.PUSHF;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.system.WAIT;
import org.ldaniels528.javapc.ibmpc.devices.memory.X86MemoryProxy;

import static org.ldaniels528.javapc.ibmpc.devices.cpu.x86.decoder.DecoderUtil.nextAddressFar;

/**
 * <pre>
 * Processes instruction codes between 90h and 9Fh (e.g. "CBW")
 * 	---------------------------------------------------------------------------
 * 	type	bits	description 				comments
 * 	---------------------------------------------------------------------------
 * 	i		4	 	instruction type
 * 	j		3		instruction sub type
 * 	k		1	 	instruction category code
 * 	d		8/16	data value					(optional)
 *
 * ---------------------------------------------------------------------------
 * instruction			code 			iiii jjj k dddd dddd
 * ---------------------------------------------------------------------------
 * xchg ax,ax | nop		90				1001 000 0
 * xchg cx,ax			91				1001 000 1
 * xchg dx,ax			92				1001 001 0
 * xchg bx,ax			93				1001 001 1
 * xchg sp,ax			94				1001 010 0
 * xchg bp,ax			95				1001 010 1
 * xchg si,ax			96				1001 011 0
 * xchg di,ax			97				1001 011 1
 * cbw					98				1001 100 0
 * cwd					99				1001 100 1
 * call nnnn:nnnn		9A				1001 101 0 nnnn nnnn
 * wait					9B				1001 101 1
 * pushf				9C				1001 110 0
 * popf					9D				1001 110 1
 * sahf					9E				1001 111 0
 * lahf					9F				1001 111 1
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 */
public class Decoder90 implements Decoder {

    /**
     * {@inheritDoc}
     */
    @Override
    public OpCode decode(final I8086 cpu, final X86MemoryProxy proxy, DecodeProcessor processor) {
        // get the instruction code
        final int code8 = proxy.nextByte();

        // evaluate the code
        switch (code8) {
            // XCHG AX,AX | NOP
            case 0x90:
                return NOP.getInstance();
            // XCHG AX,CX
            case 0x91:
                return new XCHG(cpu.AX, cpu.CX);
            // XCHG AX,DX
            case 0x92:
                return new XCHG(cpu.AX, cpu.DX);
            // XCHG AX,BX
            case 0x93:
                return new XCHG(cpu.AX, cpu.BX);
            // XCHG AX,SP
            case 0x94:
                return new XCHG(cpu.AX, cpu.SP);
            // XCHG AX,BP
            case 0x95:
                return new XCHG(cpu.AX, cpu.BP);
            // XCHG AX,SI
            case 0x96:
                return new XCHG(cpu.AX, cpu.SI);
            // XCHG AX,DI
            case 0x97:
                return new XCHG(cpu.AX, cpu.DI);
            // CBW/CWDE
            case 0x98:
                return CBW.getInstance();
            // CWD/CDQ
            case 0x99:
                return CWD.getInstance();
            // CALL offset, segment
            case 0x9A:
                return new CALL(nextAddressFar(cpu, proxy));
            // WAIT
            case 0x9B:
                return WAIT.getInstance();
            // PUSHF
            case 0x9C:
                return PUSHF.getInstance();
            // POPF
            case 0x9D:
                return POPF.getInstance();
            // SAHF
            case 0x9E:
                return SAHF.getInstance();
            // LAHF
            case 0x9F:
                return LAHF.getInstance();
            // unrecognized
            default:
                throw new UnhandledByteCodeException(code8);
        }
    }

}