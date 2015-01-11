package ibmpc.system;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.OpCode;
import ibmpc.devices.cpu.x86.bios.IbmPcBIOS;
import ibmpc.devices.display.IbmPcDisplay;
import ibmpc.devices.display.IbmPcDisplayFrame;
import ibmpc.devices.display.IbmPcVideoDisplay;
import ibmpc.devices.display.modes.IbmPcDisplayMode;
import ibmpc.devices.keyboard.IbmPcKeyEventListener;
import ibmpc.devices.keyboard.IbmPcKeyboard;
import ibmpc.devices.memory.IbmPcRandomAccessMemory;
import ibmpc.devices.mouse.IbmPcMouse;
import ibmpc.devices.ports.IbmPcHardwarePorts;
import ibmpc.devices.storage.IbmPcStorageSystem;
import ibmpc.exceptions.X86AssemblyException;
import msdos.services.MsDosMouseServices;
import msdos.services.MsDosSystemServices;
import msdos.services.ProgramTerminateService;
import msdos.services.TerminateStayResidentServices;
import msdos.storage.MsDosStorageSystem;

import java.awt.event.KeyEvent;
import java.util.List;

import static ibmpc.devices.display.modes.IbmPcDisplayModes.CGA_80X25X16;

/**
 * Represents an IBM PC/XT System which used an 4.77MHz Intel 8086 CPU.
 *
 * @author lawrence.daniels@gmail.com
 */
public class IbmPcSystemXT implements IbmPcSystem, IbmPcSystemTypeConstants, IbmPcKeyEventListener {
    protected final IbmPcRandomAccessMemory memory;
    protected final IbmPcStorageSystem storageSystem;
    protected final IbmPcDisplay display;
    protected final IbmPcHardwarePorts hardwarePorts;
    protected final IbmPcKeyboard keyboard;
    protected final IbmPcSystemInfo systemInfo;
    protected final IbmPcMouse mouse;
    protected final IbmPcBIOS bios;
    protected final Intel80x86 cpu;

    /**
     * Creates an instance of this {@link ibmpc.system.IbmPcSystem system}
     *
     * @param frame the given {@link IbmPcDisplayFrame frame}
     */
    public IbmPcSystemXT(final IbmPcDisplayFrame frame) {
        this.systemInfo = new IbmPcSystemInfoXT();
        this.memory = new IbmPcRandomAccessMemory();
        this.bios = new IbmPcBIOS(memory);
        this.cpu = new Intel80x86(memory);
        this.display = new IbmPcVideoDisplay(bios, systemInfo.getInitialDisplayMode());
        this.hardwarePorts = new IbmPcHardwarePorts(memory);
        this.keyboard = new IbmPcKeyboard(display);
        this.storageSystem = new MsDosStorageSystem();
        this.mouse = new IbmPcMouse();

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
    public Intel80x86 getCPU() {
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
            return 0;
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
            return IBM_PCjr;
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
