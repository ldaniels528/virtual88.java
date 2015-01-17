package org.ldaniels528.javapc.ibmpc.devices.cpu.decoders;

import org.ldaniels528.javapc.ibmpc.devices.cpu.I8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.*;
import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.memory.*;
import org.ldaniels528.javapc.ibmpc.devices.cpu.registers.X86Register;
import org.ldaniels528.javapc.ibmpc.devices.memory.X86MemoryProxy;

import static org.ldaniels528.javapc.ibmpc.devices.cpu.operands.Operand.*;
import static org.ldaniels528.javapc.ibmpc.devices.cpu.operands.memory.MemoryReference.*;

/**
 * 8086 Instruction Interpret Utility
 *
 * @author lawrence.daniels@gmail.com
 */
public class DecoderUtil {
    // define the memory class constants
    public static final int MEM_CLASS_8BIT = 0;
    public static final int MEM_CLASS_16BIT = 1;

    // define the register type constants
    public static final int REG_TYPE_GEN_PURPOSE = 0;
    public static final int REG_TYPE_SEGMENT = 1;

    // define the instruction source code constants
    public static final int SRC_CODE_PRIMARY = 0;
    public static final int SRC_CODE_SECONDARY = 1;

    // define the 8086 register code
    public static final int REGISTER_CODE = 3;

    /**
     * Retrieves a numeric value from given memory cpu based on the given memory code
     *
     * @param memCode the given memory code (0=8-bit, 1=16-bit)
     * @param proxy   the given {@link X86MemoryProxy memory proxy}
     * @return a {@link OperandValue operand value}
     */
    public static OperandValue getOperandValue(final int memCode, final X86MemoryProxy proxy) {
        switch (memCode) {
            case MEM_CLASS_8BIT:
                return new ByteValue(proxy.nextByte());
            case MEM_CLASS_16BIT:
                return new WordValue(proxy.nextWord());
            default:
                return null;
        }
    }

    /**
     * Returns the register based on the given register code
     *
     * @param cpu     the given {@link org.ldaniels528.javapc.ibmpc.devices.cpu.I8086 CPU} instance
     * @param regCode the given 3-bit register code
     * @param size    the size of the desired register (in bits)
     * @return the appropriate {@link Operand operand}
     */
    public static X86Register getRegister(final I8086 cpu,
                                          final int regCode,
                                          final int size) {
        switch (size) {
            case SIZE_8BIT:
                return cpu.get8bitRegister(regCode);
            case SIZE_16BIT:
                return cpu.get16bitGeneralPurposeRegister(regCode);
            default:
                throw new IllegalArgumentException(String.format("Invalid register size (%d bits)", size));
        }
    }

    /**
     * Returns the register based on the given register code
     *
     * @param cpu    the given {@link org.ldaniels528.javapc.ibmpc.devices.cpu.I8086 CPU} instance
     * @param proxy  the given {@link X86MemoryProxy memory proxy}
     * @param code16 the given 16-bit instruction code
     * @return the appropriate {@link Operand operand}
     */
    public static X86Register lookupRegister(final I8086 cpu,
                                             final X86MemoryProxy proxy,
                                             final int code16) {
        // get the memory class and register code
        // code: ..rr r... .... ...m
        final int regCode = (code16 & 0b0011_1000_0000_0000) >> 11;
        final int memClass = code16 & 0b0000_0000_0000_0001;

        // return the register
        return lookupRegister(cpu, regCode, memClass, REG_TYPE_GEN_PURPOSE);
    }

    /**
     * Returns the register based on the given register code
     *
     * @param cpu        the given {@link org.ldaniels528.javapc.ibmpc.devices.cpu.I8086 CPU} instance
     * @param regCode    the given register code (e.g. 01h -> cx)
     * @param memClass   the given memory class
     * @param segRegCode segment register code
     * @return the {@link X86Register register} or <tt>null</tt> if not found
     */
    public static X86Register lookupRegister(final I8086 cpu,
                                             final int regCode,
                                             final int memClass,
                                             final int segRegCode) {
        // is it a general purpose register?
        if (segRegCode == REG_TYPE_GEN_PURPOSE) {
            // return the appropriate register
            return memClass == MEM_CLASS_8BIT
                    ? cpu.get8bitRegister(regCode)
                    : cpu.get16bitGeneralPurposeRegister(regCode);
        }

        // must be a segment register ...
        else {
            return cpu.get16BitSegmentRegister(regCode);
        }
    }

    /**
     * Returns the appropriate operand based on the given
     * register code and memory code information
     *
     * @param cpu    the given {@link org.ldaniels528.javapc.ibmpc.devices.cpu.I8086 CPU} instance
     * @param proxy  the given {@link X86MemoryProxy memory proxy}
     * @param code16 the given instruction code containing
     *               the register/reference type code.
     * @return the array of {@link Operand operands}
     */
    public static Operand[] lookupOperands(final I8086 cpu,
                                           final X86MemoryProxy proxy,
                                           final int code16) {
        // extract the register/reference element type (11b=register, 00b/01b/10b=reference)
        // code: tt.. .... .... ....
        final int elemCode = (code16 & 0b1100_0000_0000_0000) >> 14;

        // extract the instruction sub code bits
        // code: ..rr r... .... ....
        final int regCode = (code16 & 0b0011_1000_0000_0000) >> 11;

        // extract the target register index bits
        // code: .... .mmm .... ....
        final int refCode = (code16 & 0b0000_0111_0000_0000) >> 8;

        // extract the source type bit (0=register, 1=memory type)
        // code: .... .... .... ..s.
        final int srcCode = (code16 & 0b0000_0000_0000_0010) >> 1;

        // extract the memory class (0=8-bit, 1=16-bit)
        // code: .... .... .... ...c
        final int memClass = code16 & 0b0000_0000_0000_0001;

        // lookup the primary operand (register)
        final Operand primary = lookupRegister(cpu, regCode, memClass, REG_TYPE_GEN_PURPOSE);

        // lookup the secondary operand (register or memory reference)
        final Operand secondary = lookupOperand(cpu, proxy, elemCode, refCode, memClass);

        // return the operands based on the source code
        return reorderElements(primary, secondary, srcCode);
    }

    /**
     * Returns the appropriate operand based on the given
     * register code and memory code information
     *
     * @param cpu            the given {@link org.ldaniels528.javapc.ibmpc.devices.cpu.I8086 CPU} instance
     * @param proxy          the given {@link X86MemoryProxy memory proxy}
     * @param code16         the given instruction code containing
     *                       the register/reference type code.
     * @param reorder        indicates whether the primary and second operands
     *                       are allowed to be re-ordered.
     * @param segRegsAllowed indicates whether segment registers are allowed
     * @return the array of {@link Operand operands}
     */
    public static Operand[] lookupOperands(final I8086 cpu,
                                           final X86MemoryProxy proxy,
                                           final int code16,
                                           final boolean reorder,
                                           final boolean segRegsAllowed) {
        // extract the register/reference element type (11b=register, 00b/01b/10b=reference)
        // code: tt.. .... .... ....
        final int elemCode = (code16 & 0b1100_0000_0000_0000) >> 14;

        // extract the instruction sub code bits
        // code: ..rr r... .... ....
        final int regCode = (code16 & 0b0011_1000_0000_0000) >> 11;

        // extract the target register index bits
        // code: .... .mmm .... ....
        final int refCode = (code16 & 0b0000_0111_0000_0000) >> 8;

        // extract the source type bit (0=register, 1=memory type)
        // code: .... .... .... ..s.
        final int srcCode = (code16 & 0b0000_0000_0000_0010) >> 1;

        // extract the memory class
        // code: .... .... .... ...c
        final int memClass = (code16 & 0b0000_0000_0000_0001);

        // lookup the primary and secondary operands
        final Operand primary;
        final Operand secondary;

        // are segments allowed?
        if (segRegsAllowed) {
            // extract the general purpose register bit
            // code: .... .... .... .g..
            final int gprCode = (code16 & 0b0000_0000_0000_0100) >> 2;

            // lookup the primary operand (register)
            primary = lookupRegister(cpu, regCode, memClass, gprCode);

            // lookup the secondary operand (register or memory reference)
            final boolean isSegReg = (gprCode == REG_TYPE_SEGMENT);
            secondary = lookupOperand(cpu, proxy, elemCode, refCode, isSegReg ? MEM_CLASS_16BIT : memClass);
        }

        // use only general purpose registers ...
        else {
            // lookup the primary operand (register)
            primary = lookupRegister(cpu, regCode, memClass, REG_TYPE_GEN_PURPOSE);

            // lookup the secondary operand (register or memory reference)
            secondary = lookupOperand(cpu, proxy, elemCode, refCode, memClass);
        }

        // return the operands based on the source code
        return reorder
                ? reorderElements(primary, secondary, srcCode)
                : new Operand[]{primary, secondary};
    }

    /**
     * Returns the appropriate data element based on the given
     * register code and memory code information
     *
     * @param cpu      the given {@link org.ldaniels528.javapc.ibmpc.devices.cpu.I8086 CPU}
     * @param proxy    the given {@link X86MemoryProxy memory proxy}
     * @param elemCode the given element code (11b=register,00b/01b/10b=reference)
     * @param refCode  the given register/memory reference code (000b=ax,001b=cx)
     * @param memClass the given memory code (0=8-bit,1=16-bit)
     * @return the appropriate {@link Operand operand}
     */
    public static Operand lookupOperand(final I8086 cpu,
                                        final X86MemoryProxy proxy,
                                        final int elemCode,
                                        final int refCode,
                                        final int memClass) {
        // is it a register?
        if (elemCode == REGISTER_CODE) {
            return lookupRegister(cpu, refCode, memClass, REG_TYPE_GEN_PURPOSE);
        }

        // must be a "referenced" memory address?
        else {
            // compute the composite code
            final int compCode = (elemCode << 6) | refCode;

            //logger.info( "elemCode = %02X, refCode = %02X, compCode = %02X", elemCode, refCode, compCode );

            // lookup the memory reference
            final MemoryReference ref = lookupReferencedAddress(cpu, proxy, compCode);

            // return the byte- or word- ptr
            return memClass == MEM_CLASS_8BIT ? new BytePtr(ref) : new WordPtr(ref);
        }
    }

    /**
     * Returns the appropriate data element based on the given
     * register code and memory code information
     *
     * @param cpu    the given {@link org.ldaniels528.javapc.ibmpc.devices.cpu.I8086 CPU} instance
     * @param proxy  the given {@link X86MemoryProxy memory proxy}
     * @param code16 the given 16-bit instruction code
     * @return the appropriate {@link X86Register operand}
     */
    public static X86Register lookupPrimaryOperand(final I8086 cpu,
                                                   final X86MemoryProxy proxy,
                                                   final int code16) {
        // get the memory class and register code
        final int regCode = (code16 & 0b0011_1000_0000_0000) >> 11;
        final int memClass = code16 & 0b0000_0000_0000_0001;

        // lookup the register
        return lookupRegister(cpu, regCode, memClass, REG_TYPE_GEN_PURPOSE);
    }

    /**
     * Returns the seconary operand based on the given
     * element code and reference code information
     *
     * @param cpu    the given {@link org.ldaniels528.javapc.ibmpc.devices.cpu.I8086 CPU} instance
     * @param proxy  the given {@link X86MemoryProxy memory proxy}
     * @param code16 the given instruction code containing
     *               the element/reference type code.
     * @return the array of {@link Operand operands}
     */
    public static Operand lookupSecondaryOperand(final I8086 cpu,
                                                 final X86MemoryProxy proxy,
                                                 final int code16) {
        // extract the register/reference element type (11b=register, 00b/01b/10b=reference)
        // code: tt.. .... .... ....
        final int elemCode = (code16 & 0b1100_0000_0000_0000) >> 14;

        // extract the target register index bits
        // code: .... .mmm .... ....
        final int refCode = (code16 & 0b0000_0111_0000_0000) >> 8;

        // extract the memory class
        // code: .... .... .... ...c
        final int memClass = (code16 & 0b0000_0000_0000_0001);

        // is it a register?
        if (elemCode == REGISTER_CODE) {
            return lookupRegister(cpu, refCode, memClass, REG_TYPE_GEN_PURPOSE);
        }

        // must be a "referenced" memory address?
        else {
            // get the composite code
            final int compCode = (elemCode << 6) | refCode;

            // get the memory reference
            final MemoryReference ref = lookupReferencedAddress(cpu, proxy, compCode);

            // is it an 8-bit reference?
            final boolean is8Bit = (memClass == MEM_CLASS_8BIT);

            // return the byte- or word- ptr
            return is8Bit ? new BytePtr(ref) : new WordPtr(ref);
        }
    }

    /**
     * Returns the seconary operand based on the given
     * element code and reference code information
     *
     * @param cpu    the given {@link org.ldaniels528.javapc.ibmpc.devices.cpu.I8086 CPU} instance
     * @param proxy  the given {@link X86MemoryProxy memory proxy}
     * @param code16 the given instruction code containing
     *               the element/reference type code.
     * @return the array of {@link Operand operands}
     */
    public static Operand lookupSecondaryOperand(final I8086 cpu,
                                                 final X86MemoryProxy proxy,
                                                 final int code16,
                                                 final int size) {
        // extract the register/reference element type (11b=register, 00b/01b/10b=reference)
        // code: tt.. .... .... ....
        final int elemCode = (code16 & 0b1100_0000_0000_0000) >> 14;

        // extract the target register index bits
        // code: .... .mmm .... ....
        final int refCode = (code16 & 0b0000_0111_0000_0000) >> 8;

        // extract the memory class
        // code: .... .... .... ...c
        final int memClass = code16 & 0b0000_0000_0000_0001;

        // is it a register?
        if (elemCode == REGISTER_CODE) {
            return lookupRegister(cpu, refCode, memClass, REG_TYPE_GEN_PURPOSE);
        }

        // must be a "referenced" memory address?
        else {
            // build the composite code
            final int compCode = (elemCode << 6) | refCode;

            // lookup the memory reference
            final MemoryReference ref = lookupReferencedAddress(cpu, proxy, compCode);

            // return the pointer of the appropriate size
            switch (size) {
                case SIZE_8BIT:
                    return new BytePtr(ref);
                case SIZE_16BIT:
                    return new WordPtr(ref);
                case SIZE_32BIT:
                    return new DWordPtr(ref);
                default:
                    throw new IllegalArgumentException("The size argument is invalid");
            }
        }
    }

    /**
     * Reoders the given operands based on the given source code
     *
     * @param primary   the given primary {@link Operand operand} (usually a register)
     * @param secondary the given primary {@link Operand operand} (usually a register or reference)
     * @param srcCode   the given source code (1=register, 0=reference)
     * @return the given operands is the appropriate order for the instruction
     */
    public static Operand[] reorderElements(final Operand primary,
                                            final Operand secondary,
                                            final int srcCode) {
        // determine the operand ordering
        final boolean isPrimary = srcCode == SRC_CODE_PRIMARY;
        final Operand source = isPrimary ? primary : secondary;
        final Operand target = isPrimary ? secondary : primary;

        // return the operands
        return new Operand[]{target, source};
    }

    /**
     * Returns the "referenced" memory address based on the given register code
     *
     * @param cpu      the given {@link org.ldaniels528.javapc.ibmpc.devices.cpu.I8086 CPU}
     * @param proxy    the given {@link X86MemoryProxy memory proxy}
     * @param compCode the given composite element and reference codes (e.g. 03h -> '[bp+di]')
     * @return the {@link MemoryReference "referenced" addess} or <tt>null</tt> if not found
     */
    public static MemoryReference lookupReferencedAddress(final I8086 cpu,
                                                          final X86MemoryProxy proxy,
                                                          final int compCode) {
        // make sure the composite code is valid
        if (!REFERENCE_MAPPING.containsKey(compCode)) {
            throw new IllegalStateException(String.format("Invalid reference code (code = %02X)", compCode));
        }

        // is an offset required?
        final int offset;
        switch (compCode) {
            // 16-bit offsets
            case REF_CONST_BX_SI_NNNN:
            case REF_CONST_BX_DI_NNNN:
            case REF_CONST_BP_SI_NNNN:
            case REF_CONST_BP_DI_NNNN:
            case REF_CONST_SI_NNNN:
            case REF_CONST_DI_NNNN:
            case REF_CONST_BP_NNNN:
            case REF_CONST_BX_NNNN:
            case REF_CONST_NNNN:
                offset = proxy.nextWord();
                break;

            // 8-bit offsets
            case REF_CONST_BX_SI_NN:
            case REF_CONST_BX_DI_NN:
            case REF_CONST_BP_SI_NN:
            case REF_CONST_BP_DI_NN:
            case REF_CONST_SI_NN:
            case REF_CONST_DI_NN:
            case REF_CONST_BP_NN:
            case REF_CONST_BX_NN:
                offset = proxy.nextByte();
                break;

            // No offset
            default:
                offset = 0;
        }

        // return the memory reference
        return new MemoryReference(cpu, proxy, compCode, offset);
    }

    /**
     * Returns an 8-bit NEAR data address (offset only)
     *
     * @param cpu   the given {@link org.ldaniels528.javapc.ibmpc.devices.cpu.I8086 CPU} instance
     * @param proxy the given {@link X86MemoryProxy memory proxy} instance
     * @return the {@link OperandAddress address operand}
     */
    public static OperandAddress nextAddressByte(final I8086 cpu, final X86MemoryProxy proxy) {
        return new MemoryAddressNEAR8(cpu, proxy.nextWord());
    }

    /**
     * Returns a 16-bit NEAR data address (offset only)
     *
     * @param cpu   the given {@link org.ldaniels528.javapc.ibmpc.devices.cpu.I8086 CPU} instance
     * @param proxy the given {@link X86MemoryProxy memory proxy} instance
     * @return the {@link OperandAddress address operand}
     */
    public static OperandAddress nextAddressWord(final I8086 cpu, final X86MemoryProxy proxy) {
        return new MemoryAddressNEAR16(cpu, proxy.nextWord());
    }

    /**
     * Returns a 32-bit FAR address (segment and offset)
     *
     * @param cpu   the given {@link org.ldaniels528.javapc.ibmpc.devices.cpu.I8086 CPU} instance
     * @param proxy the given {@link X86MemoryProxy memory proxy} instance
     * @return the {@link OperandAddress address operand}
     */
    public static OperandAddress nextAddressFar(final I8086 cpu, final X86MemoryProxy proxy) {
        final int offset = proxy.nextWord();
        final int segment = proxy.nextWord();
        return new MemoryAddressFAR16(cpu, segment, offset);
    }

    /**
     * Returns the 16-bit NEAR address
     *
     * @param proxy the given {@link X86MemoryProxy memory proxy} instance
     * @return the {@link OperandAddress address operand}
     */
    public static OperandValue nextAddressNear(final X86MemoryProxy proxy) {
        // compute the "real" offset
        final int relOffset = proxy.nextSignedWord();
        final int curOffset = proxy.getOffset();

        // return a near address
        return new WordValue(curOffset + relOffset);
    }

    /**
     * Returns the 8-bit SHORT address
     *
     * @param proxy the given {@link X86MemoryProxy memory proxy} instance
     * @return the {@link OperandAddress address operand}
     */
    public static OperandValue nextAddressShort(final X86MemoryProxy proxy) {
        // compute the "real" offset
        final int relOffset = proxy.nextSignedByte();
        final int curOffset = proxy.getOffset();

        // return a near address
        return new WordValue(curOffset + relOffset);
    }

    /**
     * Returns the 8-bit value
     *
     * @param proxy    the given {@link X86MemoryProxy memory proxy} instance
     * @param memClass the given memory class
     * @return the {@link ByteValue 8-bit} or {@link WordValue 16-bit} operand value
     * depending on the memory class.
     * @see #MEM_CLASS_16BIT
     * @see #MEM_CLASS_8BIT
     */
    public static Operand nextValue(final X86MemoryProxy proxy, final int memClass) {
        return (memClass == MEM_CLASS_8BIT)
                ? new ByteValue(proxy.nextByte())
                : new WordValue(proxy.nextWord());
    }

    /**
     * Returns the unsigned 8-bit value
     *
     * @param proxy the given {@link X86MemoryProxy memory proxy} instance
     * @return the 8-bit value
     */
    public static Operand nextValue8(final X86MemoryProxy proxy) {
        return new ByteValue(proxy.nextByte());
    }

    /**
     * Returns the signed 8-bit value
     *
     * @param proxy the given {@link X86MemoryProxy memory proxy} instance
     * @return the 8-bit value
     */
    public static Operand nextSignedValue8(final X86MemoryProxy proxy) {
        return new SignedByteValue(proxy.nextByte());
    }

    /**
     * Returns the 16-bit value
     *
     * @param proxy the given {@link X86MemoryProxy memory proxy} instance
     * @return the 16-bit value
     */
    public static Operand nextValue16(final X86MemoryProxy proxy) {
        return new WordValue(proxy.nextWord());
    }

}
