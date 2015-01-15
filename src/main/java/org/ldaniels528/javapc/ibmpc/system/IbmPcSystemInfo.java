package org.ldaniels528.javapc.ibmpc.system;

import org.ldaniels528.javapc.ibmpc.devices.display.modes.IbmPcDisplayMode;

/**
 * Represents the system information for an IBM PC/XT compatible computer system
 *
 * @author lawrence.daniels@gmail.com
 */
public interface IbmPcSystemInfo {

    /**
     * Return the number of floppy drives installed in the system
     *
     * @return the number of floppy drives
     */
    int getFloppyDrives();

    /**
     * Returns the initial display mode used by this system
     *
     * @return the initial {@link IbmPcDisplayMode display mode}
     * @see org.ldaniels528.javapc.ibmpc.devices.display.modes.IbmPcDisplayModes
     */
    IbmPcDisplayMode getInitialDisplayMode();

    /**
     * Returns the size of install memory (in kilobytes)
     *
     * @return the size of install memory (in kilobytes)
     */
    int getInstalledMemory();

    /**
     * Return the number of printers installed on the system
     *
     * @return the number of printers
     */
    int getInstalledPrinters();

    /**
     * Return the number of serial ports installed in the system
     *
     * @return the number of serial ports
     */
    int getSerialPorts();

    /**
     * Returns the system type code (e.g. 0xFD = 'IBM PC jr')
     *
     * @return the system type code
     */
    int getSystemType();

    /**
     * Indicates whether a game port is installed in the system
     *
     * @return true, if a game port is installed
     */
    boolean isGamePortInstalled();

    /**
     * Indicates whether an internal modem is installed in the system
     *
     * @return true, if an internal modem is installed
     */
    boolean isInternalModemInstalled();

}
