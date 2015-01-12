package org.ldaniels528.javapc.ibmpc.devices.cpu;

import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.Operand;

import static org.ldaniels528.javapc.util.BitMaskGenerator.*;

/**
 * 80x86 Flags (FLAGS) Register
 * <pre>
 * Bit # (in hex)	Acronym	Description
 * 0	 			CF	 	Carry flag
 * 1		 		1       Reserved
 * 2	 			PF		Parity flag
 * 3		 		0       Reserved
 * 4				AF		Auxiliary flag
 * 5				0       Reserved
 * 6				ZF		Zero flag
 * 7				SF		Sign flag
 * 8				TF		Trap flag (single step)
 * 9				IF		Interrupt flag
 * 10				DF		Direction flag
 * 11				OF		Overflow flag
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 */
public class X86Flags implements Operand {
    // setup the default state of the flags
    // 		                                fedc ba98 7654 3210
    //                                 mask .... odit sz0a 0p1c
    private static final int FIXED_BITS = 0b0000_0010_0000_0010;

    // Flag bit number constants
    private static final int FLAG_LEN = 16;
    private static final int CF_BIT = 0;
    private static final int PF_BIT = 2;
    private static final int AF_BIT = 4;
    private static final int ZF_BIT = 6;
    private static final int SF_BIT = 7;
    private static final int TF_BIT = 8;
    private static final int IF_BIT = 9;
    private static final int DF_BIT = 10;
    private static final int OF_BIT = 11;

    // internal fields
    private int value;

    /**
     * Default constructor
     */
    public X86Flags() {
        this.value = FIXED_BITS;
    }

    /**
     * Performs an ADD on the two operand values and updates the flags
     *
     * @param dest the given primary operand
     * @param src  the given secondary operand
     */
    public int updateADD(final Operand dest, final Operand src) {
        // perform the ADD operation on the primary and secondary values
        final int value1 = dest.get();
        final int value0 = src.get();
        final int addValue = value0 + value1;
        final int _orValue = value0 | value1;

        // update the flags
        setCF(addValue != _orValue);
        setOF(isBitSet(addValue, dest.size() - 1));
        setPF(determineParityState(addValue));
        setSF(isBitSet(addValue, dest.size() - 1));
        setZF(addValue == 0);
        return addValue;
    }

    /**
     * Performs a bitwise AND on the two operand values and updates the flags
     *
     * @param dest the given primary operand
     * @param src  the given secondary operand
     */
    public int updateAND(final Operand dest, final Operand src) {
        // perform the bitwise AND operation on the primary and secondary values
        final int andValue = dest.get() & src.get();

        // update the flags
        setOF(isBitSet(andValue, dest.size() - 1));
        setPF(andValue % 2 == 0);
        setSF(isBitSet(andValue, dest.size() - 1));
        setZF(andValue == 0);
        return andValue;
    }

    /**
     * Performs an DEC on the given operand value
     *
     * @param dest the given {@link org.ldaniels528.javapc.ibmpc.devices.cpu.operands.Operand operand}
     */
    public int updateDEC(final Operand dest) {
        // perform the DEC operation
        final int decValue = dest.get() - 1;

        // update the flags
        setOF(isBitSet(decValue, dest.size() - 1));
        setPF(determineParityState(decValue));
        setSF(isBitSet(decValue, dest.size() - 1));
        setZF(decValue == 0);
        return decValue;
    }

    /**
     * Performs an INC on the given operand value
     *
     * @param dest the given {@link org.ldaniels528.javapc.ibmpc.devices.cpu.operands.Operand operand}
     */
    public int updateINC(final Operand dest) {
        // perform the INC operation
        final int incValue = dest.get() + 1;

        // update the flags
        setOF(isBitSet(incValue, dest.size() - 1));
        setPF(determineParityState(incValue));
        setSF(isBitSet(incValue, dest.size() - 1));
        setZF(incValue == 0);
        return incValue;
    }

    /**
     * Performs a bitwise OR on the two operand values and updates the flags
     *
     * @param dest the given primary operand
     * @param src  the given secondary operand
     */
    public int updateOR(final Operand dest, final Operand src) {
        // perform the bitwise OR operation on the primary and secondary values
        final int _orValue = dest.get() | src.get();

        // update the flags
        setOF(isBitSet(_orValue, dest.size() - 1));
        setPF(_orValue % 2 == 0);
        setSF(isBitSet(_orValue, dest.size() - 1));
        setZF(_orValue == 0);
        return _orValue;
    }

    /**
     * Performs an SUB on the two operand values and updates the flags
     *
     * @param dest the given primary operand
     * @param src  the given secondary operand
     */
    public int updateSUB(final Operand dest, final Operand src) {
        // perform the logic AND operation on the primary and secondary values
        final int value1 = dest.get();
        final int value0 = src.get();
        final int andValue = value0 & value1;
        final int subValue = value0 - value1;

        // update the flags
        setAF(determineAuxiliaryState(value0, value1));
        setCF(andValue != subValue);
        setOF(isBitSet(andValue, dest.size() - 1));
        setPF(determineParityState(subValue));
        setSF(isBitSet(andValue, dest.size() - 1));
        setZF(subValue == 0);
        return subValue;
    }

    /**
     * Performs a bitwise Exclusive OR on the two operand values and updates the flags
     *
     * @param dest the given primary operand
     * @param src  the given secondary operand
     */
    public int updateXOR(final Operand dest, final Operand src) {
        // perform the bitwise XOR operation on the primary and secondary values
        final int xorValue = dest.get() ^ src.get();

        // update the flags
        setOF(isBitSet(xorValue, dest.size() - 1));
        setPF(xorValue % 2 == 0);
        setSF(isBitSet(xorValue, dest.size() - 1));
        setZF(xorValue == 0);
        return xorValue;
    }

    /**
     * It indicates when an arithmetic carry or borrow has been generated out of the four least significant bits,
     * or lower nibble.<p/>
     * The Auxiliary flag is set (to 1) if there is a carry from the low nibble (lowest four bits)
     * to the high nibble (upper four bits), or a borrow from the high nibble to the low nibble, in the low-order
     * 8-bit portion of an addition or subtraction operation. Otherwise, if no such carry or borrow occurs,
     * the flag is cleared (reset to 0).
     *
     * @param value0 the given primary data
     * @param value1 the given secondary data
     * @return the state of the auxiliary (adjust) flag
     */
    private boolean determineAuxiliaryState(final int value0, final int value1) {
        final int valueA = value0 & 0x0F;
        final int valueB = value1 & 0x0F;
        final int sum = valueA + valueB;
        final int _or = valueA | valueB;
        return sum == _or;
    }

    /**
     * The parity flag reflects the parity only of the least significant byte of the result,
     * and is set if the number of set bits of ones is even.
     *
     * @param diff the difference of the primary and secondary values
     * @return the state of the parity bit
     */
    private boolean determineParityState(final int diff) {
        final int lsb = diff & 0xFF;
        int count = 0;
        for (int n = 0; n < 8; n++) {
            final int mask = 1 << n;
            if ((lsb & mask) > 0) count += 1;
        }
        return count % 2 == 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int get() {
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void set(final int value) {
        this.value = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        return SIZE_16BIT;
    }

    /**
     * Overwrites bits 0-7 with the given 8-bit register
     *
     * @param value8 the given 8-bit register
     */
    public void overlay(final int value8) {
        this.value |= (value8 & 0b0111_1111) | FIXED_BITS;
    }

    /**
     * Indicates if the Carry Flag (CF) bit is set
     *
     * @return true, if the bit is set
     */
    public boolean isCF() {
        return isBitSet(value, CF_BIT);
    }

    /**
     * Sets the Carry Flag (CF) on/off (bit 0)
     *
     * @param on is true, if the flag is to be turned on
     */
    public X86Flags setCF(final boolean on) {
        // turn the bit on
        if (on) value |= turnBitOnMask(CF_BIT);

            // turn the bit off
        else value &= turnBitOffMask(FLAG_LEN, CF_BIT);
        return this;
    }

    /**
     * Indicates if the Parity Flag (PF) bit is set
     *
     * @return true, if the bit is set
     */
    public boolean isPF() {
        return isBitSet(value, PF_BIT);
    }

    /**
     * Sets the Parity Flag (PF) on/off
     *
     * @param on is true, if the flag is to be turned on
     */
    public X86Flags setPF(final boolean on) {
        // turn the bit on
        if (on) value |= turnBitOnMask(PF_BIT);

            // turn the bit off
        else value &= turnBitOffMask(FLAG_LEN, PF_BIT);
        return this;
    }

    /**
     * Indicates if the Auxiliary Flag (AF) bit is set
     *
     * @return true, if the bit is set
     */
    public boolean isAF() {
        return isBitSet(value, AF_BIT);
    }

    /**
     * Sets the Auxiliary Flag (AF) on/off
     *
     * @param on is true, if the flag is to be turned on
     */
    public X86Flags setAF(final boolean on) {
        // turn the bit on
        if (on) value |= turnBitOnMask(AF_BIT);

            // turn the bit off
        else value &= turnBitOffMask(FLAG_LEN, AF_BIT);
        return this;
    }

    /**
     * Indicates if the Zero Flag (ZF) bit is set
     *
     * @return true, if the bit is set
     */
    public boolean isZF() {
        return isBitSet(value, ZF_BIT);
    }

    /**
     * Sets the Zero Flag (ZF) on/off
     *
     * @param on is true, if the flag is to be turned on
     */
    public X86Flags setZF(final boolean on) {
        // turn the bit on
        if (on) value |= turnBitOnMask(ZF_BIT);

            // turn the bit off
        else value &= turnBitOffMask(FLAG_LEN, ZF_BIT);
        return this;
    }

    /**
     * Indicates if the Sign Flag (SF) bit is set
     *
     * @return true, if the bit is set
     */
    public boolean isSF() {
        return isBitSet(value, SF_BIT);
    }

    /**
     * Sets the Sign Flag (SF) on/off
     *
     * @param on is true, if the flag is to be turned on
     */
    public X86Flags setSF(final boolean on) {
        // turn the bit on
        if (on) value |= turnBitOnMask(SF_BIT);

            // turn the bit off
        else value &= turnBitOffMask(FLAG_LEN, SF_BIT);
        return this;
    }

    /**
     * Indicates if the Trap Flag (TF) bit is set
     *
     * @return true, if the bit is set
     */
    public boolean isTF() {
        return isBitSet(value, TF_BIT);
    }

    /**
     * Sets the Trap Flag (TF) on/off
     *
     * @param on is true, if the flag is to be turned on
     */
    public X86Flags setTF(final boolean on) {
        // turn the bit on
        if (on) value |= turnBitOnMask(TF_BIT);

            // turn the bit off
        else value &= turnBitOffMask(FLAG_LEN, TF_BIT);
        return this;
    }

    /**
     * Indicates if the Interrupt Flag (IF) bit is set
     *
     * @return true, if the bit is set
     */
    public boolean isIF() {
        return isBitSet(value, IF_BIT);
    }

    /**
     * Sets the Interrupt Flag (IF) on/off (bit 9)
     *
     * @param on is true, if the flag is to be turned on
     */
    public X86Flags setIF(final boolean on) {
        // turn the bit on
        if (on) value |= turnBitOnMask(IF_BIT);

            // turn the bit off
        else value &= turnBitOffMask(FLAG_LEN, IF_BIT);
        return this;
    }

    /**
     * Indicates if the Direction Flag (DF) bit is set
     *
     * @return true, if the bit is set
     */
    public boolean isDF() {
        return isBitSet(value, DF_BIT);
    }

    /**
     * Sets the Direction Flag (DF) on/off (bit 10)
     *
     * @param on is true, if the flag is to be turned on
     */
    public X86Flags setDF(final boolean on) {
        // turn the bit on
        if (on) value |= turnBitOnMask(DF_BIT);

            // turn the bit off
        else value &= turnBitOffMask(FLAG_LEN, DF_BIT);
        return this;
    }

    /**
     * Indicates if the Overflow Flag (OF) bit is set
     *
     * @return true, if the bit is set
     */
    public boolean isOF() {
        return isBitSet(value, OF_BIT);
    }

    /**
     * Sets the Overflow Flag (OF) on/off
     *
     * @param on is true, if the flag is to be turned on
     */
    public X86Flags setOF(final boolean on) {
        // turn the bit on
        if (on) value |= turnBitOnMask(OF_BIT);

            // turn the bit off
        else value &= turnBitOffMask(FLAG_LEN, OF_BIT);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(
                "%2s %2s %2s %2s %2s %2s %2s %2s %2s",
                isOF() ? "OV" : "NV",
                isDF() ? "DN" : "UP",
                isIF() ? "EI" : "DI",
                isSF() ? "NG" : "PL",
                isZF() ? "ZR" : "NZ",
                isAF() ? "AC" : "NA",
                isPF() ? "PE" : "PO",
                isCF() ? "CY" : "NC",
                isTF() ? "ET" : "DT"
        );
    }

}
