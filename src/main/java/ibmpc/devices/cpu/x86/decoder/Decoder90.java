package ibmpc.devices.cpu.x86.decoder;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.OpCode;
import ibmpc.devices.cpu.x86.opcodes.bitwise.NOP;
import ibmpc.devices.cpu.x86.opcodes.data.CBW;
import ibmpc.devices.cpu.x86.opcodes.data.CWD;
import ibmpc.devices.cpu.x86.opcodes.data.XCHG;
import ibmpc.devices.cpu.x86.opcodes.data.x386.CDQ;
import ibmpc.devices.cpu.x86.opcodes.data.x386.CWDE;
import ibmpc.devices.cpu.x86.opcodes.flags.LAHF;
import ibmpc.devices.cpu.x86.opcodes.flags.SAHF;
import ibmpc.devices.cpu.x86.opcodes.flow.callret.CALL;
import ibmpc.devices.cpu.x86.opcodes.stack.POPF;
import ibmpc.devices.cpu.x86.opcodes.stack.PUSHF;
import ibmpc.devices.cpu.x86.opcodes.stack.x386.POPFD;
import ibmpc.devices.cpu.x86.opcodes.stack.x386.PUSHFD;
import ibmpc.devices.cpu.x86.opcodes.system.WAIT;
import ibmpc.devices.memory.X86MemoryProxy;

import static ibmpc.devices.cpu.x86.decoder.DecoderUtil.nextAddressFar;

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
    public OpCode decode(final Intel80x86 cpu, final X86MemoryProxy proxy) {
        // get the instruction code
        final int code8 = proxy.nextByte();

        // is the CPU in vitual mode
        final boolean virtualMode = cpu.FLAGS.isVM();

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
                return virtualMode ? CWDE.getInstance() : CBW.getInstance();
            // CWD/CDQ
            case 0x99:
                return virtualMode ? CDQ.getInstance() : CWD.getInstance();
            // CALL offset, segment
            case 0x9A:
                return new CALL(nextAddressFar(cpu, proxy));
            // WAIT
            case 0x9B:
                return WAIT.getInstance();
            // PUSHF
            case 0x9C:
                return virtualMode ? PUSHFD.getInstance() : PUSHF.getInstance();
            // POPF
            case 0x9D:
                return virtualMode ? POPFD.getInstance() : POPF.getInstance();
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