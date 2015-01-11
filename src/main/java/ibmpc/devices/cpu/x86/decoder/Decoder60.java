package ibmpc.devices.cpu.x86.decoder;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.OpCode;
import ibmpc.devices.cpu.x86.opcodes.data.DB;
import ibmpc.devices.cpu.x86.opcodes.io.INSB;
import ibmpc.devices.cpu.x86.opcodes.io.INSW;
import ibmpc.devices.cpu.x86.opcodes.io.OUTSB;
import ibmpc.devices.cpu.x86.opcodes.io.OUTSW;
import ibmpc.devices.memory.X86MemoryProxy;

/**
 * Decodes instruction codes between 60h and 6Fh
 * <pre>
 * 	---------------------------------------------------------------------------
 * 	type	bits		description 			comments
 * 	---------------------------------------------------------------------------
 * 	t		2			element code
 * 	r		3			register code
 * 	m		3			reference code
 * 	i		7	 		instruction type
 * 	j		1			instruction sub type
 *
 *  Instruction code layout
 *  -----------------------------
 *  fedc ba98 7654 3210 (8/16 bits)
 *  ttrr rmmm iiii iiij
 *
 * ---------------------------------------------------------------------------
 * instruction				code 	tt rrr mmm	iiiiiii j
 * ---------------------------------------------------------------------------
 * pusha/pushad				  60				0110000 0
 * popa/popad				  61				0110000 1
 *
 * arpl ax,cx				C163	11 000 001 	0110001 1
 * arpl cx,ax				C863	11 001 000 	0110001 1
 *
 * insb						  6C				0110110 0
 * insw | insd				  6D				0110110 1
 * outsb					  6E				0110111 0
 * outsw | outsd			  6F				0110111 1
 * </pre>
 * See <a href="http://pdos.csail.mit.edu/6.828/2006/readings/i386/OUTS.htm">OUT instruction</a>
 *
 * @author lawrence.daniels@gmail.com
 */
public class Decoder60 implements Decoder {

    /**
     * {@inheritDoc}
     */
    @Override
    public OpCode decode(final Intel80x86 cpu, final X86MemoryProxy proxy) {
        // get the 8-bit instruction code
        final int code8 = proxy.nextByte();

        // decode the instruction
        switch (code8) {
            case 0x6C:
                return INSB.getInstance();
            case 0x6D:
                return INSW.getInstance();
            case 0x6E:
                return OUTSB.getInstance();
            case 0x6F:
                return OUTSW.getInstance();
            default:
                return new DB(code8);
        }
    }

}
