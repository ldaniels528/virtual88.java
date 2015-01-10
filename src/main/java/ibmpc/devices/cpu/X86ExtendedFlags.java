package ibmpc.devices.cpu;

import ibmpc.devices.cpu.operands.Operand;

import static ibmpc.util.BitMaskGenerator.turnBitOffMask;
import static ibmpc.util.BitMaskGenerator.turnBitOnMask;

/**
 * 80x86 Extended Flags (EFLAGS) Register
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
 * 12,13			IOPL	I/O Privilege Level (286+ only)
 * 14	 			NT		Nested Task Flag (286+ only)
 * 15		 		0
 * 16	 			RF		Resume Flag (386+ only)
 * 17	 			VM		Virtual Mode Flag (386+ only)
 * 18				AC	 	Alignment Check (486SX+ only)
 * 19				VIF		Virtual Interrupt Flag (Pentium+)
 * 20				VIP		Virtual Interrupt Pending (Pentium+)
 * 21				ID		Identification (Pentium+)
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 */
public class X86ExtendedFlags implements Operand {
    // Flag bit number constants
    private static final int FLAG_LEN = 16;
    private static final int CF_BIT = 0x00;
    private static final int PF_BIT = 0x02;
    private static final int AF_BIT = 0x04;
    private static final int ZF_BIT = 0x06;
    private static final int SF_BIT = 0x07;
    private static final int TF_BIT = 0x08;
    private static final int IF_BIT = 0x09;
    private static final int DF_BIT = 0x0A;
    private static final int OF_BIT = 0x0B;
    //private static final int IOPL_BIT = 0x0C; // 0C-0D
    private static final int NT_BIT = 0x0E;
    private static final int RF_BIT = 0x10;
    private static final int VM_BIT = 0x11;
    private static final int AC_BIT = 0x12;
    private static final int VIF_BIT = 0x13;
    private static final int VIP_BIT = 0x14;
    private static final int ID_BIT = 0x15;

    // internal fields
    private int value;

    /**
     * Default constructor
     */
    public X86ExtendedFlags() {
        // set the default state of the flags
        // 		fedc ba98 7654 3210 fedc ba98 7654 3210
        // mask	0000 0000 0000 0000 0000 0010 0000 0010
        this.value = 0x00000202;
    }

    /**
     * Compares the two operand values
     *
     * @param value0 the given primary data
     * @param value1 the given secondary data
     */
    public void compare(final int value0, final int value1) {
        // compute the values we need
        final int sum = value0 + value1;
        final int _or = value0 | value1;
        final int diff = value0 - value1;

        // set the flags
        setAF(determineAuxiliaryState(value0, value1));
        setCF(sum == _or);
        setOF(sum > 0xFFFF); // TODO different for 8/16-bit
        setPF(determineParityState(diff));
        setSF((sum & 0b1000_0000) > 0); // TODO different for 8/16-bit
        setZF(diff == 0);
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
        final int valueA = value0 & 0b0000_1111;
        final int valueB = value1 & 0b0000_1111;
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
        this.value = (value & 0xFF00) | (value & 0x00FF);
    }

    /**
     * Indicates if the Carry Flag (CF) bit is set
     *
     * @return true, if the bit is set
     */
    public boolean isCF() {
        return (value & turnBitOnMask(CF_BIT)) > 0;
    }

    /**
     * Sets the Carry Flag (CF) on/off (bit 0)
     *
     * @param on is true, if the flag is to be turned on
     */
    public void setCF(final boolean on) {
        // turn the bit on
        if (on) {
            value |= turnBitOnMask(CF_BIT);
        }

        // turn the bit off
        else {
            value &= turnBitOffMask(FLAG_LEN, CF_BIT);
        }
    }

    /**
     * Indicates if the Parity Flag (PF) bit is set
     *
     * @return true, if the bit is set
     */
    public boolean isPF() {
        return (value & turnBitOnMask(PF_BIT)) > 0;
    }

    /**
     * Sets the Parity Flag (PF) on/off
     *
     * @param on is true, if the flag is to be turned on
     */
    public void setPF(final boolean on) {
        // turn the bit on
        if (on) {
            value |= turnBitOnMask(PF_BIT);
        }

        // turn the bit off
        else {
            value &= turnBitOffMask(FLAG_LEN, PF_BIT);
        }
    }

    /**
     * Indicates if the Auxiliary Flag (AF) bit is set
     *
     * @return true, if the bit is set
     */
    public boolean isAF() {
        return (value & turnBitOnMask(AF_BIT)) > 0;
    }

    /**
     * Sets the Auxiliary Flag (AF) on/off
     *
     * @param on is true, if the flag is to be turned on
     */
    public void setAF(final boolean on) {
        // turn the bit on
        if (on) {
            value |= turnBitOnMask(AF_BIT);
        }

        // turn the bit off
        else {
            value &= turnBitOffMask(FLAG_LEN, AF_BIT);
        }
    }

    /**
     * Indicates if the Zero Flag (ZF) bit is set
     *
     * @return true, if the bit is set
     */
    public boolean isZF() {
        return (value & turnBitOnMask(ZF_BIT)) > 0;
    }

    /**
     * Sets the Zero Flag (ZF) on/off
     *
     * @param on is true, if the flag is to be turned on
     */
    public void setZF(final boolean on) {
        // turn the bit on
        if (on) {
            value |= turnBitOnMask(ZF_BIT);
        }

        // turn the bit off
        else {
            value &= turnBitOffMask(FLAG_LEN, ZF_BIT);
        }
    }

    /**
     * Indicates if the Sign Flag (SF) bit is set
     *
     * @return true, if the bit is set
     */
    public boolean isSF() {
        return (value & turnBitOnMask(SF_BIT)) > 0;
    }

    /**
     * Sets the Sign Flag (SF) on/off
     *
     * @param on is true, if the flag is to be turned on
     */
    public void setSF(final boolean on) {
        // turn the bit on
        if (on) {
            value |= turnBitOnMask(SF_BIT);
        }

        // turn the bit off
        else {
            value &= turnBitOffMask(FLAG_LEN, SF_BIT);
        }
    }

    /**
     * Indicates if the Trap Flag (TF) bit is set
     *
     * @return true, if the bit is set
     */
    public boolean isTF() {
        return (value & turnBitOnMask(TF_BIT)) > 0;
    }

    /**
     * Sets the Trap Flag (TF) on/off
     *
     * @param on is true, if the flag is to be turned on
     */
    public void setTF(final boolean on) {
        // turn the bit on
        if (on) {
            value |= turnBitOnMask(TF_BIT);
        }

        // turn the bit off
        else {
            value &= turnBitOffMask(FLAG_LEN, TF_BIT);
        }
    }

    /**
     * Indicates if the Interrupt Flag (IF) bit is set
     *
     * @return true, if the bit is set
     */
    public boolean isIF() {
        return (value & turnBitOnMask(IF_BIT)) > 0;
    }

    /**
     * Sets the Interrupt Flag (IF) on/off (bit 9)
     *
     * @param on is true, if the flag is to be turned on
     */
    public void setIF(final boolean on) {
        // turn the bit on
        if (on) {
            value |= turnBitOnMask(IF_BIT);
        }

        // turn the bit off
        else {
            value &= turnBitOffMask(FLAG_LEN, IF_BIT);
        }
    }

    /**
     * Indicates if the Direction Flag (DF) bit is set
     *
     * @return true, if the bit is set
     */
    public boolean isDF() {
        return (value & turnBitOnMask(DF_BIT)) > 0;
    }

    /**
     * Sets the Direction Flag (DF) on/off (bit 10)
     *
     * @param on is true, if the flag is to be turned on
     */
    public void setDF(final boolean on) {
        // turn the bit on
        if (on) {
            value |= turnBitOnMask(DF_BIT);
        }

        // turn the bit off
        else {
            value &= turnBitOffMask(FLAG_LEN, DF_BIT);
        }
    }

    /**
     * Indicates if the Overflow Flag (OF) bit is set
     *
     * @return true, if the bit is set
     */
    public boolean isOF() {
        return (value & turnBitOnMask(OF_BIT)) > 0;
    }

    /**
     * Sets the Overflow Flag (OF) on/off
     *
     * @param on is true, if the flag is to be turned on
     */
    public void setOF(final boolean on) {
        // turn the bit on
        if (on) {
            value |= turnBitOnMask(OF_BIT);
        }

        // turn the bit off
        else {
            value &= turnBitOffMask(FLAG_LEN, OF_BIT);
        }
    }

    /**
     * Indicates if the I/O Privilege Level (IOPL) bits (2) are set (286+ Only)
     *
     * @return true, if the bits are set
     */
    public boolean isIOPL() {
        // read the bits 0C and 0D
        //		  fedc ba98 7654 3210
        // mask = ..11 .... .... ....
        return (value & 0x3000) > 0;
    }

    /**
     * Sets the I/O Privilege Level (IOPL) Flag on/off (286+ Only)
     *
     * @param on is true, if the flag is to be turned on
     */
    public void setIOPL(final boolean on) {
        //		  fedc ba98 7654 3210
        // mask = ..11 .... .... ....
        if (on) this.value |= 0x3000;

            // mask = 11.. 1111 1111 1111
        else this.value &= 0xCFFF;
    }

    /**
     * Indicates if the NT bit is set (286+ Only)
     *
     * @return true, if the bit is set
     */
    public boolean isNT() {
        return (value & turnBitOnMask(NT_BIT)) > 0;
    }

    /**
     * Sets the Nested Tag Flag on/off (286+ Only)
     *
     * @param on is true, if the flag is to be turned on
     */
    public void setNT(final boolean on) {
        // turn the bit on
        if (on) {
            value |= turnBitOnMask(NT_BIT);
        }

        // turn the bit off
        else {
            value &= turnBitOffMask(FLAG_LEN, NT_BIT);
        }
    }

    /**
     * Indicates if the Resume Flag (RF) bit is set (386+ Only)
     *
     * @return true, if the bit 10h is set
     */
    public boolean isRF() {
        return (value & turnBitOnMask(RF_BIT)) > 0;
    }

    /**
     * Sets the Resume Flag (RF) on/off (386+ Only)
     *
     * @param on is true, if the flag is to be turned on
     */
    public void setRF(final boolean on) {
        // turn the bit on
        if (on) {
            value |= turnBitOnMask(RF_BIT);
        }

        // turn the bit off
        else {
            value &= turnBitOffMask(FLAG_LEN, RF_BIT);
        }
    }

    /**
     * Indicates if the Virtual Mode (VM) bit is set (386+ Only)
     *
     * @return true, if the bit 11h is set
     */
    public boolean isVM() {
        return (value & turnBitOnMask(VM_BIT)) > 0;
    }

    /**
     * Sets the Virtual Mode (VM) Flag on/off (386+ Only)
     *
     * @param on is true, if the flag is to be turned on
     */
    public void setVM(final boolean on) {
        // turn the bit on
        if (on) {
            value |= turnBitOnMask(VM_BIT);
        }

        // turn the bit off
        else {
            value &= turnBitOffMask(FLAG_LEN, VM_BIT);
        }
    }

    /**
     * Indicates if the Alignment Check (AC) bit is set (486SX+ Only)
     *
     * @return true, if the bit 12h is set
     */
    public boolean isAC() {
        return (value & turnBitOnMask(AC_BIT)) > 0;
    }

    /**
     * Sets the Alignment Check (AC) Flag on/off (486SX+ Only)
     *
     * @param on is true, if the flag is to be turned on
     */
    public void setAC(final boolean on) {
        // turn the bit on
        if (on) {
            value |= turnBitOnMask(AC_BIT);
        }

        // turn the bit off
        else {
            value &= turnBitOffMask(FLAG_LEN, AC_BIT);
        }
    }

    /**
     * Indicates if the Virtual Interrupt Flag (VIF) bit is set (Pentium+ Only)
     *
     * @return true, if the bit 15h is set
     */
    public boolean isVIF() {
        return (value & turnBitOnMask(VIF_BIT)) > 0;
    }

    /**
     * Sets the Virtual Interrupt Flag (VIF) Flag on/off (Pentium+ Only)
     *
     * @param on is true, if the flag is to be turned on
     */
    public void setVIF(final boolean on) {
        // turn the bit on
        if (on) {
            value |= turnBitOnMask(VIF_BIT);
        }

        // turn the bit off
        else {
            value &= turnBitOffMask(FLAG_LEN, VIF_BIT);
        }
    }

    /**
     * Indicates if the Virtual Interrupt Pending (VIP) bit is set (Pentium+ Only)
     *
     * @return true, if the bit 15h is set
     */
    public boolean isVIP() {
        return (value & turnBitOnMask(VIP_BIT)) > 0;
    }

    /**
     * Sets the Virtual Interrupt Pending (VIP) Flag on/off (Pentium+ Only)
     *
     * @param on is true, if the flag is to be turned on
     */
    public void setVIP(final boolean on) {
        // turn the bit on
        if (on) {
            value |= turnBitOnMask(VIP_BIT);
        }

        // turn the bit off
        else {
            value &= turnBitOffMask(FLAG_LEN, VIP_BIT);
        }
    }

    /**
     * Indicates if the Identification (ID) bit is set (Pentium+ Only)
     *
     * @return true, if the bit 15h is set
     */
    public boolean isID() {
        return (value & turnBitOnMask(ID_BIT)) > 0;
    }

    /**
     * Sets the Identification (ID) Flag on/off (Pentium+ Only)
     *
     * @param on is true, if the flag is to be turned on
     */
    public void setID(final boolean on) {
        // turn the bit on
        if (on) {
            value |= turnBitOnMask(ID_BIT);
        }

        // turn the bit off
        else {
            value &= turnBitOffMask(FLAG_LEN, ID_BIT);
        }
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
