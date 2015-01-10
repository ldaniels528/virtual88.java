package ibmpc.devices.cpu;

import ibmpc.devices.cpu.operands.Operand;

import static ibmpc.util.BitMaskGenerator.turnBitOnMask;

/**
 * MSW: 80x86 Machine Status Word (286+ only)
 * <pre>
 * Bit #	Acronym	Description
 * 0		PE		Protection Enable, switches processor between protected and real mode
 * 1		MP		Math Present, controls function of the WAIT instruction
 * 2		EM		Emulation, indicates whether coprocessor functions are to be emulated
 * 3		TS		Task Switched, set and interrogated by coprocessor on task switches and
 * 					when interpretting coprocessor instructions
 * 4		ET		Extension Type, indicates type of coprocessor in system
 * 5-30		Reserved
 * 31		PG		Paging, indicates whether the processor uses page tables to translate linear
 * 					addresses to physical addresses
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 */
public class X86MachineStatusWord implements Operand {
    private int msw;

    /**
     * Default constructor
     */
    public X86MachineStatusWord() {
        // set the default state of the machine status word
        //      fedc ba98 7654 3210 fedc ba98 7654 3210
        // mask 1000 0000 0000 0000 0000 0000 0001 1010
        this.msw = 0x8000001A;
    }

    /* (non-Javadoc)
     * @see ibmpc.devices.cpu.operands.Operand#get()
     */
    public int get() {
        return msw;
    }

    /**
     * Overlays the given 16-bit value onto the current MSW
     *
     * @param value16 the given 16-bit value
     */
    public void overlay(final int value16) {
        this.msw = (msw | (value16 & 0xFFFF));
    }

    /* (non-Javadoc)
     * @see ibmpc.devices.cpu.operands.Operand#set(int)
     */
    public void set(final int value) {
        this.msw = value;
    }

    /* (non-Javadoc)
     * @see ibmpc.devices.cpu.operands.Operand#size()
     */
    public int size() {
        return SIZE_32BIT;
    }

    /**
     * Indicates whether protected mode is enabled
     * (or conversely real mode)
     *
     * @return true, if protected mode is enabled
     */
    public boolean isProtectedMode() {
        return (msw & turnBitOnMask(0x00)) > 0;
    }

    /**
     * Indicates whether an Floating Point Unit (FPU)
     * is present.
     *
     * @return true, if an FPU is present
     */
    public boolean isFPUPresent() {
        return (msw & turnBitOnMask(0x01)) > 0;
    }

    /**
     * Indicates whether the Floating Point Unit (FPU)
     * is being emulated.
     *
     * @return true, if the FPU is being emulated
     */
    public boolean isFPUEmulated() {
        return (msw & turnBitOnMask(0x02)) > 0;
    }

    /**
     * Indicates whether the task is swithed
     *
     * @return true, if the task is swithed
     */
    public boolean isTaskSwithed() {
        return (msw & turnBitOnMask(0x03)) > 0;
    }

    /**
     * Indicates indicates the type of coprocessor in system
     *
     * @return true, if the extension type is enabled
     */
    public boolean isExtensionTypeSet() {
        return (msw & turnBitOnMask(0x04)) > 0;
    }

    /**
     * Indicates indicates whether paging is set
     *
     * @return true, if paging is set
     */
    public boolean isPagingSet() {
        return (msw & turnBitOnMask(0x1F)) > 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "MSW";
    }

}
