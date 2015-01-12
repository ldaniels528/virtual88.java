package org.ldaniels528.javapc.ibmpc.system;

import org.ldaniels528.javapc.ibmpc.devices.cpu.Intel8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.OpCode;
import org.ldaniels528.javapc.ibmpc.devices.cpu.ProgramContext;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.bios.IbmPcBIOS;
import org.ldaniels528.javapc.ibmpc.devices.display.IbmPcDisplay;
import org.ldaniels528.javapc.ibmpc.devices.display.IbmPcDisplayFrame;
import org.ldaniels528.javapc.ibmpc.devices.display.IbmPcVideoDisplay;
import org.ldaniels528.javapc.ibmpc.devices.display.modes.IbmPcDisplayMode;
import org.ldaniels528.javapc.ibmpc.devices.keyboard.IbmPcKeyEventListener;
import org.ldaniels528.javapc.ibmpc.devices.keyboard.IbmPcKeyboard;
import org.ldaniels528.javapc.ibmpc.devices.memory.IbmPcRandomAccessMemory;
import org.ldaniels528.javapc.ibmpc.devices.memory.X86MemoryProxy;
import org.ldaniels528.javapc.ibmpc.devices.mouse.IbmPcMouse;
import org.ldaniels528.javapc.ibmpc.devices.ports.IbmPcHardwarePorts;
import org.ldaniels528.javapc.ibmpc.devices.storage.IbmPcStorageSystem;
import org.ldaniels528.javapc.ibmpc.exceptions.X86AssemblyException;
import org.ldaniels528.javapc.msdos.services.MsDosMouseServices;
import org.ldaniels528.javapc.msdos.services.MsDosSystemServices;
import org.ldaniels528.javapc.msdos.services.ProgramTerminateService;
import org.ldaniels528.javapc.msdos.services.TerminateStayResidentServices;
import org.ldaniels528.javapc.msdos.storage.MsDosStorageSystem;

import java.awt.event.KeyEvent;
import java.util.List;

import static org.ldaniels528.javapc.ibmpc.devices.display.modes.IbmPcDisplayModes.CGA_80X25X16;

/**
 * Represents an IBM PCjr System which used an 4.77MHz Intel 8086 CPU.
 *
 * @author lawrence.daniels@gmail.com
 */
public class IbmPcSystemPCjr implements IbmPcSystem, IbmPcKeyEventListener {
    protected final IbmPcRandomAccessMemory memory;
    protected final X86MemoryProxy proxy;
    protected final IbmPcStorageSystem storageSystem;
    protected final IbmPcDisplay display;
    protected final IbmPcHardwarePorts hardwarePorts;
    protected final IbmPcKeyboard keyboard;
    protected final IbmPcSystemInfo systemInfo;
    protected final IbmPcMouse mouse;
    protected final IbmPcBIOS bios;
    protected final Intel8086 cpu;

    /**
     * Creates an instance of this {@link org.ldaniels528.javapc.ibmpc.system.IbmPcSystem system}
     *
     * @param frame the given {@link IbmPcDisplayFrame frame}
     */
    public IbmPcSystemPCjr(final IbmPcDisplayFrame frame) {
        this.systemInfo = new IbmPcSystemInfoXT();
        this.memory = new IbmPcRandomAccessMemory();
        this.proxy = new X86MemoryProxy(memory, 0, 0);
        this.bios = new IbmPcBIOS(memory);
        this.cpu = new Intel8086(proxy);
        this.display = new IbmPcVideoDisplay(bios, systemInfo.getInitialDisplayMode());
        this.hardwarePorts = new IbmPcHardwarePorts(memory);
        this.keyboard = new IbmPcKeyboard(display);
        this.storageSystem = new MsDosStorageSystem();
        this.mouse = new IbmPcMouse();

        // set the PC identifier byte at F000:FFFE
        memory.setByte(0xF000, 0xFFFE, IBM_PCjr);

        // initialize the virtual BIOS
        initializeBIOS(systemInfo);

        // initialize the objects that depend on the frame
        if (frame != null) {
            display.init(frame);
            mouse.init(frame);
            keyboard.init(frame);
        }

        // register for specific key events
        keyboard.register(this);
    }

    public void execute(final ProgramContext context) throws X86AssemblyException {
        cpu.execute(this, context);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(final List<OpCode> opCodes) throws X86AssemblyException {
        for (final OpCode opCode : opCodes) {
            cpu.execute(this, opCode);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IbmPcBIOS getBIOS() {
        return bios;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Intel8086 getCPU() {
        return cpu;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IbmPcDisplay getDisplay() {
        return display;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IbmPcHardwarePorts getHardwarePorts() {
        return hardwarePorts;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IbmPcKeyboard getKeyboard() {
        return keyboard;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public X86MemoryProxy getMemoryProxy() {
        return proxy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IbmPcMouse getMouse() {
        return mouse;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IbmPcRandomAccessMemory getRandomAccessMemory() {
        return memory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IbmPcStorageSystem getStorageSystem() {
        return storageSystem;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IbmPcSystemInfo getInformation() {
        return systemInfo;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void keyPressed(final IbmPcKeyboard keyboard, final KeyEvent e) {
        bios.updateKeyFlags(keyboard.getKeyFlags());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void keyReleased(final IbmPcKeyboard keyboard, final KeyEvent e) {
        bios.updateKeyFlags(keyboard.getKeyFlags());
    }

    /**
     * Initializes the interrupt handlers
     */
    private void initializeBIOS(final IbmPcSystemInfo systemInfo) {
        // initialize the virtual BIOS
        bios.updateSystemInfo(systemInfo);

        // MS-DOS services
        bios.register(0x20, 0xF000, 0x0100, ProgramTerminateService.getInstance());
        bios.register(0x21, 0xF000, 0x0104, MsDosSystemServices.getInstance());
        bios.register(0x27, 0xF000, 0x0108, TerminateStayResidentServices.getInstance());
        bios.register(0x33, 0xF000, 0x010C, MsDosMouseServices.getInstance());
    }

    /**
     * Represents system information for this PC/XT System
     *
     * @author lawrence.daniels@gmail.com
     */
    private class IbmPcSystemInfoXT implements IbmPcSystemInfo {

        /**
         * Default constructor
         */
        public IbmPcSystemInfoXT() {
            super();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getFloppyDrives() {
            return 1;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public IbmPcDisplayMode getInitialDisplayMode() {
            return CGA_80X25X16;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getInstalledMemory() {
            return memory.sizeInKilobytes();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getInstalledPrinters() {
            return 0;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getSerialPorts() {
            return 0;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getSystemType() {
            return memory.getByte(0xF000, 0xFFFE);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isGamePortInstalled() {
            return false;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isInternalModemInstalled() {
            return false;
        }

    }

}
