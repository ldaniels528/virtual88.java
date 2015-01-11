package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.data;

import org.ldaniels528.javapc.ibmpc.devices.cpu.Intel8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.Operand;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.AbstractDualOperandOpCode;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.registers.X86Register;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

import static org.ldaniels528.javapc.ibmpc.devices.cpu.operands.Operand.*;

/**
 * <pre>
 * MOVSX -- Move with Sign-Extend
 *
 * Opcode     Instruction        Clocks   Description
 * 0F  BE /r  MOVSX r16,r/m8     3/6      Move byte to word with sign-extend
 * 0F  BE /r  MOVSX r32,r/m8     3/6      Move byte to dword, sign-extend
 * 0F  BF /r  MOVSX r32,r/m16    3/6      Move word to dword, sign-extend
 *
 * Operation
 * DEST := SignExtend(SRC);
 *
 * Description
 * MOVSX reads the contents of the effective address or register as a byte
 * or a word, sign-extends the value to the operand-size attribute of the
 * instruction (16 or 32 bits), and stores the result in the destination
 * register.
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 */
public class MOVSX extends AbstractDualOperandOpCode {

    /**
     * MOVSX dst, src
     *
     * @param dest the given {@link X86Register destination}
     * @param src  the given {@link Operand source}
     */
    public MOVSX(final Operand dest, final Operand src) {
        super("MOVSX", dest, src);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(IbmPcSystem system, final Intel8086 cpu) {
        // get the source and destination operand sizes
        final int destSize = dest.size();
        final int srcSize = src.size();

        // if the source is 8-bit
        if (srcSize == SIZE_8BIT) {
            // and the destination is 16-bit ...
            if (destSize == SIZE_16BIT) {
                byteToWord(dest, src);
                return;
            }
        }

        // should never happen
        throw new IllegalStateException(String.format("Invalid paramets - %s to %s", src, dest));
    }

    /**
     * Move a signed byte to a signed word
     *
     * @param dest the given {@link Operand destination}
     * @param src  the given {@link Operand source}
     */
    private void byteToWord(final Operand dest, final Operand src) {
        // get the signed 8-bit integer from the source
        final int signedInt8 = src.get();

        // get the sign of the 8-bit integer
        // mask 1000 0000
        final int high8 = (signedInt8 & 0x80);

        // get the unsigned 8-bit integer
        // mask 0111 1111
        final int low8 = (signedInt8 & 0x7F);

        // build a signed 16-bit integer
        // value x... .... xxxx xxxxx
        final int signedInt16 = (high8 << 8) | low8;

        // set the destination
        dest.set(signedInt16);
    }

    /**
     * Move a signed byte to a signed double word
     *
     * @param dest the given {@link Operand destination}
     * @param src  the given {@link Operand source}
     */
    private void byteToDoubleWord(final Operand dest, final Operand src) {
        // get the signed 8-bit integer from the source
        final int signedInt8 = src.get();

        // get the sign of the 8-bit integer
        // mask 1000 0000
        final int high16 = (signedInt8 & 0x80);

        // get the unsigned 8-bit integer
        // mask 0111 1111
        final int low16 = (signedInt8 & 0x7F);

        // build a signed 16-bit integer
        // value x... .... xxxx xxxxx
        final int signedInt32 = (high16 << 16) | low16;

        // set the destination
        dest.set(signedInt32);
    }

    /**
     * Move a signed word to a signed double word
     *
     * @param dest the given {@link Operand destination}
     * @param src  the given {@link Operand source}
     */
    private void wordToDoubleWord(final Operand dest, final Operand src) {
        // get the signed 16-bit integer from the source
        final int signedInt16 = src.get();

        // get the sign of the 8-bit integer
        // mask 1000 0000 0000 0000
        final int high16 = (signedInt16 & 0x8000);

        // get the unsigned 8-bit integer
        // mask 0111 1111 1111 1111
        final int low16 = (signedInt16 & 0x7FFF);

        // build a signed 16-bit integer
        // value x... .... xxxx xxxxx
        final int signedInt32 = (high16 << 16) | low16;

        // set the destination
        dest.set(signedInt32);
    }

}
