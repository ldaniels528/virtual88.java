package org.ldaniels528.javapc.ibmpc.devices.bios.services;

import org.ldaniels528.javapc.ibmpc.devices.cpu.I8086;
import org.ldaniels528.javapc.ibmpc.exceptions.X86AssemblyException;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystemInfo;

/**
 * BIOS Equipment List Services
 * <pre>
 * Equipment Check
 * 16-15     Number of printers installed
 * 14 = 1    if internal modem installed
 * 13 = 1    if game adapter installed
 * 12-10     Number of serial ports installed
 * 9         not used
 * 8-6       Number of floppy drives (if bit 0=1)
 *               00=1, 01=2KB, 10=3, 11=4
 * 5-4       Initial video mode
 *              00=not used, 01=40x25 color,
 *              10=80x25 color, 11=80x25 monochrome
 * 4-2       System board RAM size (original PC)
 *              00=16KB, 01=32KB, 10=48KB, 11=64KB
 * 2 = 1     if math coprocessor installed
 * 1 = 1     if floppy drive installed
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 */
public class EquipmentListServices implements InterruptHandler {
    private static final EquipmentListServices instance = new EquipmentListServices();

    /**
     * Private constructor
     */
    private EquipmentListServices() {
        super();
    }

    /**
     * Returns the singleton instance of this class
     *
     * @return the singleton instance of this class
     */
    public static EquipmentListServices getInstance() {
        return instance;
    }

    /**
     * Process the Equipment Services Interrupt (INT 11h)
     *
     * @param system the given {@link org.ldaniels528.javapc.ibmpc.system.IbmPcSystem IBM PC system}
     * @param cpu    the given {@link org.ldaniels528.javapc.ibmpc.devices.cpu.I8086 8086 CPU} instance
     * @throws X86AssemblyException
     */
    public void process(final IbmPcSystem system, final I8086 cpu) throws X86AssemblyException {
        // get the system information
        final IbmPcSystemInfo systemInfo = system.getInformation();

        // set the properties
        setInstalledMemory(cpu, systemInfo.getInstalledMemory());
        setInitialDisplayMode(cpu, systemInfo.getInitialDisplayMode().getVideoMode());
        setFloppiesInstalled(cpu, systemInfo.getFloppyDrives());
        setInstalledSerialPorts(cpu, systemInfo.getSerialPorts());
        setGamePortInstalled(cpu, systemInfo.isGamePortInstalled());
        setInternalModemInstalled(cpu, systemInfo.isInternalModemInstalled());
        setPrintersInstalled(cpu, systemInfo.getInstalledPrinters());
    }

    /**
     * Sets the amount of memory installed
     *
     * @param memorySize the given memory size (in kilobytes)
     */
    private void setInstalledMemory(final I8086 cpu, final int memorySize) {
        // determine the memory index (e.g. 00=16KB, 01=32KB, 10=48KB, 11=64KB)
        final int memIndex = determineMemoryIndex(memorySize);

        cpu.AX.setBit(3, (memIndex & 0x01) > 0); // 01
        cpu.AX.setBit(4, (memIndex & 0x02) > 0); // 10
    }

    /**
     * Determine the memory index (e.g. 00=16KB, 01=32KB, 10=48KB, 11=64KB)
     * based on the given memory size
     *
     * @param memorySize the given memory size (in kilobytes)
     * @return the memory index value
     */
    private int determineMemoryIndex(final int memorySize) {
        if (memorySize >= 64) return 0b11;
        else if (memorySize >= 48) return 0b10;
        else if (memorySize >= 32) return 0b01;
        else if (memorySize >= 16) return 0b00;
        else return 0b00;
    }

    /**
     * Sets the initial display mode
     *
     * @param mode the initial display mode
     *             (e.g. 00=not used, 01=40x25 color, 10=80x25 color, 11=80x25 monochrome )
     */
    private void setInitialDisplayMode(final I8086 cpu, final int mode) {
        cpu.AX.setBit(5, (mode & 0b01) > 0);
        cpu.AX.setBit(6, (mode & 0b10) > 0);
    }

    /**
     * Sets the number of floppies installed
     *
     * @param floppies the given number of floppies installed
     */
    private void setFloppiesInstalled(final I8086 cpu, final int floppies) {
        cpu.AX.setBit(1, floppies != 0);
        cpu.AX.setBit(6, (floppies & 0x01) > 0); // 001
        cpu.AX.setBit(7, (floppies & 0x02) > 0); // 010
        cpu.AX.setBit(8, (floppies & 0x04) > 0); // 100
    }

    /**
     * Sets the number of serial ports installed
     *
     * @param cpu   the given {@link org.ldaniels528.javapc.ibmpc.devices.cpu.I8086 8086 CPU}
     * @param ports the given number of ports
     */
    private void setInstalledSerialPorts(final I8086 cpu, final int ports) {
        cpu.AX.setBit(10, (ports & 0x01) > 0); // 001
        cpu.AX.setBit(11, (ports & 0x02) > 0); // 010
        cpu.AX.setBit(12, (ports & 0x04) > 0); // 100
    }

    /**
     * Sets the game port flag to installed/uninstalled
     *
     * @param installed indicates whether the hardware is installed or not
     */
    private void setGamePortInstalled(final I8086 cpu, final boolean installed) {
        cpu.AX.setBit(13, installed);
    }

    /**
     * Sets the internal modem flag to installed/uninstalled
     *
     * @param installed indicates whether the hardware is installed or not
     */
    private void setInternalModemInstalled(final I8086 cpu, final boolean installed) {
        cpu.AX.setBit(14, installed);
    }

    /**
     * Sets the number of printers installed
     *
     * @param cpu      the given {@link org.ldaniels528.javapc.ibmpc.devices.cpu.I8086 8086 CPU}
     * @param printers the given number of printers
     */
    private void setPrintersInstalled(I8086 cpu, int printers) {
        cpu.AX.setBit(15, (printers & 0x01) > 0); // 01
        cpu.AX.setBit(16, (printers & 0x02) > 0); // 10
    }

}
