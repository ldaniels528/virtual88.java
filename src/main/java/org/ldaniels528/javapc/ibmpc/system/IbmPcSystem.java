package org.ldaniels528.javapc.ibmpc.system;

import org.ldaniels528.javapc.ibmpc.devices.cpu.Intel80x86;
import org.ldaniels528.javapc.ibmpc.devices.cpu.OpCode;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.bios.IbmPcBIOS;
import org.ldaniels528.javapc.ibmpc.devices.display.IbmPcDisplay;
import org.ldaniels528.javapc.ibmpc.devices.keyboard.IbmPcKeyboard;
import org.ldaniels528.javapc.ibmpc.devices.memory.IbmPcRandomAccessMemory;
import org.ldaniels528.javapc.ibmpc.devices.mouse.IbmPcMouse;
import org.ldaniels528.javapc.ibmpc.devices.ports.IbmPcHardwarePorts;
import org.ldaniels528.javapc.ibmpc.devices.storage.IbmPcStorageSystem;
import org.ldaniels528.javapc.ibmpc.exceptions.X86AssemblyException;

import java.util.List;

/**
 * Represents an IBM PC/XT/AT Compatible System
 *
 * @author lawrence.daniels@gmail.com
 */
public interface IbmPcSystem {

    /**
     * Executes the given collection of opCodes
     *
     * @param opCodes the given collection of {@link OpCode opCodes}
     * @throws X86AssemblyException
     */
    void execute(List<OpCode> opCodes) throws X86AssemblyException;

    /**
     * @return the Basic Input/Output System (BIOS)
     */
    IbmPcBIOS getBIOS();

    /**
     * @return the IBM PC-compatible central processing unit (CPU)
     */
    Intel80x86 getCPU();

    /**
     * @return the graphical display manager.
     */
    IbmPcDisplay getDisplay();

    /**
     * @return the hardware ports
     */
    IbmPcHardwarePorts getHardwarePorts();

    /**
     * @return the console input device associated to this environment.
     */
    IbmPcKeyboard getKeyboard();

    /**
     * @return the mouse input device associated to this environment.
     */
    IbmPcMouse getMouse();

    /**
     * @return the random access memory instance of this environment.
     */
    IbmPcRandomAccessMemory getRandomAccessMemory();

    /**
     * @return the persistence storage device belonging to this environment.
     */
    IbmPcStorageSystem getStorageSystem();

    /**
     * @return detail information about the system
     */
    IbmPcSystemInfo getInformation();

}
