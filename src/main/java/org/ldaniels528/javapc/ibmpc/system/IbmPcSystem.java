package org.ldaniels528.javapc.ibmpc.system;

import org.ldaniels528.javapc.ibmpc.devices.cpu.Intel8086;
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
 * <pre>
 * IBM computer-type code; see also BIOS INT 15H C0H:
 *  0ffH = original PC
 *  0feH = XT or Portable PC
 *  0fdH = PCjr
 *  0fcH = AT (or XT model 286) (or PS/2 Model 50/60)
 *  0fbH = XT with 640K motherboard
 *  0faH = PS/2 Model 30
 *  0f9H = Convertible PC (easily converts into a paperweight)
 *  0f8H = PS/2 Model 80
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 */
public interface IbmPcSystem {
    int IBM_PC = 0xFF;
    int XT_OR_PORTABLE_PC = 0XFE;
    int IBM_PCjr = 0xFD;
    int IBM_PC_AT = 0xFC;
    int IBM_PC_XT = 0xFB;
    int IBM_PS_2_MODEL30 = 0xFA;
    int CONVERTIBLE_PC = 0xF9;
    int IBM_PS_2_MODEL80 = 0xF8;

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
    Intel8086 getCPU();

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
