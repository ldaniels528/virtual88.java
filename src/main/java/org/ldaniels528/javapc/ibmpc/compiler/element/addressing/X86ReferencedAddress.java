package org.ldaniels528.javapc.ibmpc.compiler.element.addressing;

import org.ldaniels528.javapc.ibmpc.compiler.element.registers.X86RegisterRef;
import org.ldaniels528.javapc.ibmpc.compiler.element.values.X86ByteValue;
import org.ldaniels528.javapc.ibmpc.compiler.element.values.X86NumericValue;
import org.ldaniels528.javapc.ibmpc.compiler.element.values.X86WordValue;

import static org.ldaniels528.javapc.ibmpc.devices.cpu.operands.memory.MemoryReference.*;

/**
 * Represents an 8086 "Referenced" Address (e.g. "[bx+si]")
 *
 * @author lawrence.daniels@gmail.com
 */
public class X86ReferencedAddress extends X86MemoryAddress {
    private static final X86ReferencedAddress REF_BX_SI = new X86ReferencedAddress(REF_CONST_BX_SI);
    private static final X86ReferencedAddress REF_BX_DI = new X86ReferencedAddress(REF_CONST_BX_DI);
    private static final X86ReferencedAddress REF_BP_SI = new X86ReferencedAddress(REF_CONST_BP_SI);
    private static final X86ReferencedAddress REF_BP_DI = new X86ReferencedAddress(REF_CONST_BP_DI);
    private static final X86ReferencedAddress REF_SI = new X86ReferencedAddress(REF_CONST_SI);
    private static final X86ReferencedAddress REF_DI = new X86ReferencedAddress(REF_CONST_DI);
    private static final X86ReferencedAddress REF_BX = new X86ReferencedAddress(REF_CONST_BX);
    private final X86NumericValue offset;
    private final int sequence;

    ////////////////////////////////////////////////////////////
    //		Constructor(s)
    ////////////////////////////////////////////////////////////

    /**
     * Creates an instance this address
     *
     * @param sequence the given sequence number
     */
    private X86ReferencedAddress(final int sequence) {
        this(sequence, null);
    }

    /**
     * Creates an instance this address
     *
     * @param sequence the given sequence number
     * @param offset   the given {@link X86NumericValue offset}
     */
    private X86ReferencedAddress(final int sequence, final X86NumericValue offset) {
        this.sequence = sequence;
        this.offset = offset;
    }

    ////////////////////////////////////////////////////////////
    //		Static method(s)
    ////////////////////////////////////////////////////////////

    /**
     * Returns a "[BX+SI]" reference
     *
     * @return a "[BX+SI]" reference
     */
    public static X86ReferencedAddress createBX_SI() {
        return REF_BX_SI;
    }

    /**
     * Returns a "[BX+DI]" reference
     *
     * @return a "[BX+DI]" reference
     */
    public static X86ReferencedAddress createBX_DI() {
        return REF_BX_DI;
    }

    /**
     * Returns a "[BP+SI]" reference
     *
     * @return a "[BP+SI]" reference
     */
    public static X86ReferencedAddress createBP_SI() {
        return REF_BP_SI;
    }

    /**
     * Returns a "[BP+DI]" reference
     *
     * @return a "[BP+DI]" reference
     */
    public static X86ReferencedAddress createBP_DI() {
        return REF_BP_DI;
    }

    /**
     * Returns a "[SI]" reference
     *
     * @return a "[SI]" reference
     */
    public static X86ReferencedAddress createSI() {
        return REF_SI;
    }

    /**
     * Returns a "[DI]" reference
     *
     * @return a "[DI]" reference
     */
    public static X86ReferencedAddress createDI() {
        return REF_DI;
    }

    /**
     * Returns a "[nnnn]" reference
     *
     * @return a "[nnnn]" reference
     */
    public static X86ReferencedAddress createNNNN(final X86WordValue offset) {
        return new X86ReferencedAddress(REF_CONST_NNNN, offset);
    }

    /**
     * Returns a "[BX]" reference
     *
     * @return a "[BX]" reference
     */
    public static X86ReferencedAddress createBX() {
        return REF_BX;
    }

    /**
     * Returns a "[BX+SI+nn]" reference
     *
     * @return a "[BX+SI+nn]" reference
     */
    public static X86ReferencedAddress createBX_SI_NN(final X86ByteValue offset) {
        return new X86ReferencedAddress(REF_CONST_BX_SI, offset);
    }

    /**
     * Returns a "[BX+DI+nn]" reference
     *
     * @return a "[BX+DI+nn]" reference
     */
    public static X86ReferencedAddress createBX_DI_NN(final X86ByteValue offset) {
        return new X86ReferencedAddress(REF_CONST_BX_SI, offset);
    }

    /**
     * Returns a "[BP+SI+nn]" reference
     *
     * @return a "[BP+SI+nn]" reference
     */
    public static X86ReferencedAddress createBP_SI_NN(final X86ByteValue offset) {
        return new X86ReferencedAddress(REF_CONST_BX_SI, offset);
    }

    /**
     * Returns a "[BP+DI+nn]" reference
     *
     * @return a "[BP+DI+nn]" reference
     */
    public static X86ReferencedAddress createBP_DI_NN(final X86ByteValue offset) {
        return new X86ReferencedAddress(REF_CONST_BX_SI, offset);
    }

    /**
     * Returns a "[SI+nn]" reference
     *
     * @return a "[SI+nn]" reference
     */
    public static X86ReferencedAddress createSI_NN(final X86ByteValue offset) {
        return new X86ReferencedAddress(REF_CONST_BX_SI, offset);
    }

    /**
     * Returns a "[DI+nn]" reference
     *
     * @return a "[DI+nn]" reference
     */
    public static X86ReferencedAddress createDI_NN(final X86ByteValue offset) {
        return new X86ReferencedAddress(REF_CONST_BX_SI, offset);
    }

    /**
     * Returns a "[BP+nn]" reference
     *
     * @return a "[BP+nn]" reference
     */
    public static X86ReferencedAddress createBP_NN(final X86ByteValue offset) {
        return new X86ReferencedAddress(REF_CONST_BX_SI, offset);
    }

    /**
     * Returns a "[BX+nn]" reference
     *
     * @return a "[BX+nn]" reference
     */
    public static X86ReferencedAddress createBX_NN(final X86ByteValue offset) {
        return new X86ReferencedAddress(REF_CONST_BX_SI, offset);
    }

    /**
     * Returns a "[SI+nnnn]" reference
     *
     * @return a "[SI+nnnn]" reference
     */
    public static X86ReferencedAddress createSI_NNNN(final X86WordValue offset) {
        return new X86ReferencedAddress(REF_CONST_BX_SI, offset);
    }

    /**
     * Returns a "[DI+nnnn]" reference
     *
     * @return a "[DI+nnnn]" reference
     */
    public static X86ReferencedAddress createDI_NNNN(final X86WordValue offset) {
        return new X86ReferencedAddress(REF_CONST_BX_SI, offset);
    }

    /**
     * Returns a "[BP+nnnn]" reference
     *
     * @return a "[BP+nnnn]" reference
     */
    public static X86ReferencedAddress createBP_NNNN(final X86WordValue offset) {
        return new X86ReferencedAddress(REF_CONST_BX_SI, offset);
    }

    /**
     * Returns a "[BX+nnnn]" reference
     *
     * @return a "[BX+nnnn]" reference
     */
    public static X86ReferencedAddress createBX_NNNN(final X86WordValue offset) {
        return new X86ReferencedAddress(REF_CONST_BX_SI, offset);
    }

    ////////////////////////////////////////////////////////////
    //		Accessor and Mutator Method(s)
    ////////////////////////////////////////////////////////////

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] getByteCode() {
        throw new IllegalArgumentException("Use \"getByteCode(X86RegisterRef)\" instead");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] getByteCode(final X86RegisterRef reg) {
        return determineByteCode(reg.getSequence(), sequence);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getSequence() {
        return sequence;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(REFERENCE_MAPPING.get(sequence), offset);
    }

    /**
     * Determines the x86 byte code for the given reference
     *
     * @param target the given target type
     * @param source the given source type
     * @return an array of bytes
     */
    private byte[] determineByteCode(final int target, final int source) {
        // compute the reference code
        final byte code = generateReferenceCode(target, source);

        // if it's an offset reference?
        if (source == REF_CONST_NNNN) {
            final X86WordValue value16 = (X86WordValue) offset;
            final byte hiByte = value16.getHighByte();
            final byte loByte = value16.getLowByte();
            return new byte[]{code, loByte, hiByte};
        }
        // if it's a register reference?
        else if (source >= REF_CONST_BX_SI && source <= REF_CONST_BX) {
            return new byte[]{code};
        }
        // if it's a register and 8-bit offset reference?
        else if (source >= REF_CONST_BX_SI_NN && source <= REF_CONST_BX_NN) {
            final byte byteValue = ((Integer) offset.getValue()).byteValue();
            return new byte[]{code, byteValue};
        }
        // if it's a register and 16-bit offset reference?
        else if (source >= REF_CONST_SI_NNNN && source <= REF_CONST_BX_NNNN) {
            final X86WordValue value16 = (X86WordValue) offset;
            final byte hiByte = value16.getHighByte();
            final byte loByte = value16.getLowByte();
            return new byte[]{code, loByte, hiByte};
        }
        // dunno what?
        else return null;
    }

    /**
     * Generates the code that specifies the appropriate
     * source and target references.
     * <pre>
     * Code = mm.rrr.mmm
     * 		r = register reference (e.g. "ax") [3 bits]
     * 		m = memory reference (e.g. "[bx+si]") [5 bits]
     * </pre>
     *
     * @return the code that specifies the appropriate
     * source and target references.
     */
    private byte generateReferenceCode(final int register, final int memref) {
        // compute the register portion of the code (masking off unneeded bits)
        // Code = mm.rrr.mmm
        // 		r = register reference (e.g. "ax") [3 bits]
        // 		m = memory reference (e.g. "[bx+si]") [5 bits]

        // shift the register bits to the center, and AND off the rest
        final int rrr = (register << 3) & 0x0038; // 38h = 00111000b

        // AND off the center bits (bits 3, 4, and 5)
        final int mmm = memref & 0xC7; // C7h = 11000111b

        // combine the memory reference and regitser codes
        final int byteCode = mmm | rrr;

        // return the code
        return (byte) byteCode;
    }

}
