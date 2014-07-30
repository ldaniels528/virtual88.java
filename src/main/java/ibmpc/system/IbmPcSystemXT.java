package ibmpc.system;

import static ibmpc.devices.display.modes.IbmPcDisplayModes.CGA_80X25X16;
import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.x86.bios.IbmPcBIOS;
import ibmpc.devices.display.IbmPcDisplay;
import ibmpc.devices.display.IbmPcDisplayFrame;
import ibmpc.devices.display.IbmPcVideoDisplay;
import ibmpc.devices.display.modes.IbmPcDisplayMode;
import ibmpc.devices.keyboard.IbmPcKeyEventListener;
import ibmpc.devices.keyboard.IbmPcKeyboard;
import ibmpc.devices.memory.IbmPcRandomAccessMemory;
import ibmpc.devices.mouse.IbmPcMouse;
import ibmpc.devices.storage.IbmPcStorageSystem;

import java.awt.event.KeyEvent;

import msdos.services.MsDosMouseServices;
import msdos.services.MsDosSystemServices;
import msdos.services.ProgramTerminateService;
import msdos.services.TerminateStayResidentServices;
import msdos.storage.MsDosStorageSystem;

/**
 * Represents an IBM PC/XT System which used an 4.77MHz Intel 8086 CPU.
 * @author lawrence.daniels@gmail.com
 */
public class IbmPcSystemXT implements IbmPcSystem, IbmPcSystemTypeConstants, IbmPcKeyEventListener {
	protected final IbmPcRandomAccessMemory memory;
	protected final IbmPcStorageSystem storageSystem;	
	protected final IbmPcDisplay display;
	protected final IbmPcKeyboard keyboard;
	protected final IbmPcSystemInfo systemInfo;
	protected final IbmPcMouse mouse;
	protected final IbmPcBIOS bios;
	protected final Intel80x86 cpu;

	/**
	 * Creates an instance of this {@link ibmpc.system.IbmPcSystem system}
	 */
	public IbmPcSystemXT( final IbmPcDisplayFrame frame ) {
		this.systemInfo		= new IbmPcSystemInfoXT();
		this.memory 		= new IbmPcRandomAccessMemory();	
		this.bios			= new IbmPcBIOS( memory );
		this.cpu			= new Intel80x86( this, memory );
		this.display   		= new IbmPcVideoDisplay( bios, systemInfo.getInitialDisplayMode() );
		this.keyboard		= new IbmPcKeyboard( display );
		this.storageSystem	= new MsDosStorageSystem();
		this.mouse			= new IbmPcMouse();
		
		// initialize the virtual BIOS
		initializeBIOS( systemInfo );
		
		// initialize the objects that depend on the frame
		if( frame != null ) {
			display.init( frame );
			mouse.init( frame );
			keyboard.init( frame );
		}	
		
		// register for specific key events
		keyboard.register( this ); 
	}
	
	/* 
	 * (non-Javadoc)
	 * @see ibmpc.system.IbmPcSystem#getBIOS()
	 */
	public IbmPcBIOS getBIOS() {
		return bios;
	}
	
	/* (non-Javadoc)
	 * @see ibmpc.configuration.IbmPcSystem#getCPU()
	 */
	public Intel80x86 getCPU() {
		return cpu;
	}

	/* (non-Javadoc)
	 * @see ibmpc.configuration.IbmPcSystem#getDisplay()
	 */
	public IbmPcDisplay getDisplay() {
		return display;
	}

	/* (non-Javadoc)
	 * @see ibmpc.configuration.IbmPcSystem#getKeyboard()
	 */
	public IbmPcKeyboard getKeyboard() {
		return keyboard;
	}

	/* (non-Javadoc)
	 * @see ibmpc.configuration.IbmPcSystem#getMouse()
	 */
	public IbmPcMouse getMouse() {
		return mouse;
	}

	/* (non-Javadoc)
	 * @see ibmpc.configuration.IbmPcSystem#getRandomAccessMemory()
	 */
	public IbmPcRandomAccessMemory getRandomAccessMemory() {
		return memory;
	}

	/* (non-Javadoc)
	 * @see ibmpc.configuration.IbmPcSystem#getStorageSystem()
	 */
	public IbmPcStorageSystem getStorageSystem() {
		return storageSystem;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see ibmpc.system.IbmPcSystem#getSystemInformation()
	 */
	public IbmPcSystemInfo getInformation() {
		return systemInfo;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see ibmpc.devices.keyboard.IbmPcKeyEventListener#keyPressed(ibmpc.devices.keyboard.IbmPcKeyboard, java.awt.event.KeyEvent)
	 */
	public void keyPressed( final IbmPcKeyboard keyboard, final KeyEvent e ) {
		bios.updateKeyFlags( keyboard.getKeyFlags() );
	}
	
	/* 
	 * (non-Javadoc)
	 * @see ibmpc.devices.keyboard.IbmPcKeyEventListener#keyReleased(ibmpc.devices.keyboard.IbmPcKeyboard, java.awt.event.KeyEvent)
	 */
	public void keyReleased( final IbmPcKeyboard keyboard, final KeyEvent e ) {
		bios.updateKeyFlags( keyboard.getKeyFlags() );
	}
	
	/**
	 * Initializes the interrupt handlers
	 */
	private void initializeBIOS( final IbmPcSystemInfo systemInfo ) {
		// initialize the virtual BIOS
		bios.updateSystemInfo( systemInfo );
		
		// MS-DOS services
		bios.register( 0x20, 0xF000, 0x0100, ProgramTerminateService.getInstance() ); 
		bios.register( 0x21, 0xF000, 0x0104, MsDosSystemServices.getInstance() );
		bios.register( 0x27, 0xF000, 0x0108, TerminateStayResidentServices.getInstance() ); 
		bios.register( 0x33, 0xF000, 0x010C, MsDosMouseServices.getInstance() );
	}
	
	/**
	 * Represents system information for this PC/XT System
	 * @author lawrence.daniels@gmail.com
	 */
	private class IbmPcSystemInfoXT implements IbmPcSystemInfo {
		
		/**
		 * Default constructor
		 */
		public IbmPcSystemInfoXT() {
			super();
		}
		
		/* 
		 * (non-Javadoc)
		 * @see ibmpc.system.IbmPcSystemInfo#getFloppyDrives()
		 */
		public int getFloppyDrives() {
			return 0;
		}
		
		/* (non-Javadoc)
		 * @see ibmpc.system.IbmPcSystemInfo#getInitialDisplayMode()
		 */
		public IbmPcDisplayMode getInitialDisplayMode() {
			return CGA_80X25X16;
		}

		/* (non-Javadoc)
		 * @see ibmpc.system.IbmPcSystemInfo#getInstalledMemory()
		 */
		public int getInstalledMemory() {
			return memory.sizeInKilobytes();
		}
		
		/* 
		 * (non-Javadoc)
		 * @see ibmpc.system.IbmPcSystemInfo#getInstalledPrinters()
		 */
		public int getInstalledPrinters() {
			return 0;
		}
		
		/* 
		 * (non-Javadoc)
		 * @see ibmpc.system.IbmPcSystemInfo#getSerialPorts()
		 */
		public int getSerialPorts() {
			return 0;
		}

		/* (non-Javadoc)
		 * @see ibmpc.system.IbmPcSystemInfo#getSystemType()
		 */
		public int getSystemType() {
			return IBM_PCjr;
		}
		
		/* 
		 * (non-Javadoc)
		 * @see ibmpc.system.IbmPcSystemInfo#isGamePortInstalled()
		 */
		public boolean isGamePortInstalled() {
			return false;
		}

		/* (non-Javadoc)
		 * @see ibmpc.system.IbmPcSystemInfo#isInternalModemInstalled()
		 */
		public boolean isInternalModemInstalled() {
			return false;
		}
		
	}

}
