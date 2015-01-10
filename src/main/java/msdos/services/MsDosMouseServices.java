package msdos.services;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.x86.bios.services.InterruptHandler;
import ibmpc.devices.cpu.x86.bios.services.VideoServices;
import ibmpc.devices.display.IbmPcDisplay;
import ibmpc.devices.mouse.IbmPcMouse;
import ibmpc.exceptions.X86AssemblyException;

/**
 * MS-DOS Mouse Services
 * @author lawrence.daniels@gmail.com
 */
public class MsDosMouseServices implements InterruptHandler {
	private static final MsDosMouseServices instance = new MsDosMouseServices();
	
	/**
	 * Private constructor
	 */
	private MsDosMouseServices() {
		super();
	}
	
	/**
	 * Returns the singleton instance of this class 
	 * @return the singleton instance of this class 
	 */
	public static MsDosMouseServices getInstance() {
		return instance;
	}
	
	/**
	 * Process the DOS Mouse Services Interrupt (INT 33h)
	 * @throws X86AssemblyException
	 */
	public void process( final Intel80x86 cpu ) 
	throws X86AssemblyException {
		// get an instance of the mouse
		final IbmPcMouse mouse = cpu.getSystem().getMouse();
		
		// determine what to do
		switch( cpu.AX.get() ) {
			case 0x00: resetMouse( cpu ); break;
			case 0x01: showCusror( mouse ); break;
			case 0x02: hideCursor( mouse ); break;
			case 0x03: getPositionAndButtonStatus( mouse, cpu ); break;
			case 0x04: setPosition( mouse, cpu ); break;
			case 0x05: getButtonPressInfo( mouse, cpu ); break;
			case 0x06: getButtonReleseInfo( mouse, cpu ); break;
			case 0x07: setHorizontalMinOrMax( mouse, cpu ); break;
			case 0x08: setVerticalMinOrMax( mouse, cpu ); break;
			case 0x09: setGraphicsCursor( mouse, cpu ); break;
			case 0x0A: setTextCursor( mouse, cpu ); break;
			case 0x0B: readMotionCounters( mouse, cpu ); break;
			case 0x0C: setUserDefinedInputMask( mouse, cpu ); break;
			case 0x0D: setMouseLightPenEmulationOn( mouse, cpu ); break;
			case 0x0E: setMouseLightPenEmulationOff( mouse, cpu ); break;
			case 0x0F: setMousePixelRatio( mouse, cpu ); break;
			case 0x10: setMouseConditionOff( mouse, cpu ); break;
			case 0x13: setMouseDoubleSpeedThresthold( mouse, cpu ); break;
			case 0x14: swapInterruptSubroutine( mouse, cpu ); break;
			case 0x15: getDriverStateAndRequirements( mouse, cpu ); break;
			case 0x16: saveDriverState( mouse, cpu ); break;
			case 0x17: restoreDriverState( mouse, cpu ); break;
			case 0x18: setAlternateSubroutineCallMaskAndAddress( mouse, cpu ); break;
			case 0x19: getAlternateInterruptAddress( mouse, cpu ); break;
			case 0x1A: setSensitivity( mouse, cpu ); break;
			case 0x1B: getSensitivity( mouse, cpu ); break;
			case 0x1C: setInterruptRate( mouse, cpu ); break;
			case 0x1D: setCrtPage( mouse, cpu ); break;
			case 0x1E: getCrtPage( mouse, cpu ); break;
			case 0x1F: disableDriver( mouse, cpu ); break;
			case 0x20: enableDriver( mouse, cpu ); break;
			case 0x21: resetSoftware( mouse, cpu ); break;
			case 0x22: setLanguageForMessages( mouse, cpu ); break;
			case 0x23: getLanguageNumber( mouse, cpu ); break;
			case 0x24: getDriverVersionMouseTypeAndIRQ( mouse, cpu ); break;
			default:
				throw new X86AssemblyException( "Invalid call (" + cpu.AX + ")" );
		}
	}
	
	/**
	 * Mouse Reset/Get Mouse Installed Flag
	 * Returns:
	 * 	AX = 0000h mouse driver not installed
	 * 	AX = FFFFh mouse driver installed
	 * 	BX = number of buttons
	 */
	private void resetMouse( Intel80x86 cpu ) {
		cpu.AX.set( 0xFFFF );
		cpu.BX.set( 2 ); // TODO Detect how many mouse buttons?
	}
	
	/** 
	 * Show Mouse Cursor
	 */
	private void showCusror( IbmPcMouse mouse ) {
		mouse.showCusror();
	}
	
	/** 
	 * Hide Mouse Cursor
	 */
	private void hideCursor( IbmPcMouse mouse ) {
		mouse.hideCursor();
	}
	
	/** 
	 * Get Mouse Position and Button Status
	 * Returns:
	 *	CX = horizontal (X) position (0..639)
	 *	DX = vertical (Y) position (0..199)
	 *	BX = button status:
	 *		|F-8|7|6|5|4|3|2|1|0| Button Status
	 *		| | | | | | | | +---- left button (1 = pressed)
	 * 		| | | | | | | +----- right button (1 = pressed)
	 */
	private void getPositionAndButtonStatus( IbmPcMouse mouse, Intel80x86 cpu ) {
		cpu.CX.set( mouse.getMouseX() );
		cpu.DX.set( mouse.getMouseY() );
		cpu.BX.set( mouse.getButtonStatus() );
	}
	
	/**
	 * Set Mouse Cursor Position
	 * Parameters:
	 *	CX = horizontal position
	 *	DX = vertical position
	 */
	private void setPosition( IbmPcMouse mouse, Intel80x86 cpu ) {
		mouse.setMouseX( cpu.CX.get() );
		mouse.setMouseY( cpu.DX.get() );
	}
	
	/** 
	 * Get Mouse Button Press Information
	 * Parameters:
	 * 	BX = 0 left button
	 *		 1 right button
	 * Returns:
	 *	BX = count of button presses (0-32767), set to zero after call
	 *	CX = horizontal position at last press
	 *	DX = vertical position at last press
	 *	AX = status:
	 *		|F-8|7|6|5|4|3|2|1|0| Button Status
	 *		| | | | | | | | +---- left button (1 = pressed)
	 *		| | | | | | | +----- right button (1 = pressed)
	 *		+------------------- unused
	 */
	private void getButtonPressInfo( IbmPcMouse mouse, Intel80x86 cpu ) {
		cpu.CX.set( mouse.getMouseX() );
		cpu.DX.set( mouse.getMouseY() );
		cpu.BX.set( mouse.getPressCount() );
		cpu.AX.set( mouse.getButtonStatus() );
	}
	
	/**
	 * Get Mouse Button Release Information
	 * Parameters:
	 * 	BX = 0 left button
	 *		 1 right button
	 * Returns:
	 *	BX = count of button releases (0-32767), set to zero after call
	 *	CX = horizontal position at last release
	 *	DX = vertical position at last release
	 *	AX = status
	 *		|F-8|7|6|5|4|3|2|1|0| Button status
	 *		| | | | | | | | +---- left button (1 = pressed)
	 *		| | | | | | | +----- right button (1 = pressed)
	 *		+------------------- unused
	 */
	private void getButtonReleseInfo( IbmPcMouse mouse, Intel80x86 cpu ) {
		cpu.CX.set( mouse.getMouseX() );
		cpu.DX.set( mouse.getMouseY() );
		cpu.BX.set( mouse.getPressCount() );
		cpu.AX.set( mouse.getButtonStatus() );
	}
	
	/** 
	 * Set Mouse Horizontal Min/Max Position
	 * Purpose:
	 *	Restricts mouse horizontal movement to window
	 * Notes:
	 *	if min value is greater than max value they are swapped  
	 * Parameters:
	 * 	CX = minimum horizontal position
	 *	DX = maximum horizontal position
	 */
	private void setHorizontalMinOrMax( IbmPcMouse mouse, Intel80x86 cpu ) {
		mouse.setMinimumX( cpu.CX.get() );
		mouse.setMaximumX( cpu.DX.get() );
	}
	
	/** 
	 * Set Mouse Vertical Min/Max Position
	 * Purpose:
	 *	Restricts mouse vertical movement to window
	 * Notes:
	 *	if min value is greater than max value they are swapped  
	 * Parameters:
	 * 	CX = minimum vertical position
	 *	DX = maximum vertical position
	 */
	private void setVerticalMinOrMax( IbmPcMouse mouse, Intel80x86 cpu ) {
		mouse.setMinimumY( cpu.CX.get() );
		mouse.setMaximumY( cpu.DX.get() );
	}
	
	/** 
	 * Set Mouse Graphics Cursor
	 * Parameters:
	 * 	BX = horizontal hot spot (-16 to 16)
	 *	CX = vertical hot spot (-16 to 16)
	 *	ES:DX = pointer to screen and cursor masks (16 byte bitmap)
	 */
	private void setGraphicsCursor( IbmPcMouse mouse, Intel80x86 cpu ) {
		// TODO Have to figure this out
	}
	
	/** 
	 * Set Mouse Text Cursor
	 * Parameters:
	 * 	BX = 00 software cursor
	 *		 01 hardware cursor
	 *	CX = start of screen mask or hardware cursor scan line
	 *	DX = end of screen mask or hardware cursor scan line
	 */
	private void setTextCursor( IbmPcMouse mouse, Intel80x86 cpu ) {
		// TODO Have to figure this out
	}
	
	/** 
	 * Read Mouse Motion Counters
	 * Notes:
	 * 	count values are 1/200 inch intervals (1/200 in. = 1 mickey)
	 * Returns:
	 *	CX = horizontal mickey count (-32768 to 32767)
	 *	DX = vertical mickey count (-32768 to 32767)
	 */
	private void readMotionCounters( IbmPcMouse mouse, Intel80x86 cpu ) {
		// TODO Have to figure this out
	}
	
	/** 
	 * Set Mouse User Defined Subroutine and Input Mask
	 * Parameters:
	 * 	ES:DX = far pointer to user interrupt
	 *	CX = user interrupt mask:
	 *		|F-5|4|3|2|1|0| user interrupt mask in CX
	 *		| | | | | +--- cursor position changed
	 *		| | | | +---- left button pressed
	 *		| | | +----- left button released
	 *		| | +------ right button pressed
	 *		| +------- right button released
	 *		+--------- unused
	 */
	private void setUserDefinedInputMask( IbmPcMouse mouse, Intel80x86 cpu ) {
		// TODO Have to figure this out
	}
	
	/** 
	 * Mouse Light Pen Emulation On
	 */
	private void setMouseLightPenEmulationOn( IbmPcMouse mouse, Intel80x86 cpu ) {
		// TODO Have to figure this out
	}
	
	/** 
	 * Mouse Light Pen Emulation Off
	 */
	private void setMouseLightPenEmulationOff( IbmPcMouse mouse, Intel80x86 cpu ) {
		// TODO Have to figure this out
	}
	
	/** 
	 * Set Mouse Pixel Ratio
	 * Purpose:
	 * 	Sets the ratio between physical cursor movement (mickeys) 
	 * 	and screen coordinate changes
	 * Parameters:
	 * 	CX = horizontal ratio (1..32767, default 8)
	 *	DX = vertical ratio (1..32767, default 16)
	 */
	private void setMousePixelRatio( IbmPcMouse mouse, Intel80x86 cpu ) {
		// TODO Have to figure this out
	}
	
	/**
	 * Set Mouse Conditional OFF
	 * Parameters:
	 *	CX = upper X screen coordinate
	 *	DX = upper Y screen coordinate
	 *	SI = lower X screen coordinate
	 *	DI = lower Y screen coordinate
	 */
	private void setMouseConditionOff( IbmPcMouse mouse, Intel80x86 cpu ) {
		// TODO Have to figure this out
	}
	
	/** 
	 * Set Mouse Double Speed Threshold
	 * Parameters:
	 *	DX = threshold speed (mickeys per second, default 64)
	 */
	private void setMouseDoubleSpeedThresthold( IbmPcMouse mouse, Intel80x86 cpu ) {
		// TODO Have to figure this out
	}
	
	/** 
	 * Swap Interrupt Subroutines
	 * Notes:
	 * 	1. Routine at ES:DX is called if an event occurs and the
	 *		corresponding bit specified in user mask is set
	 * 	2. Routine at ES:DX receives parameters in the following
	 * Parameters:
	 *	ES:DX = far pointer to user routine
	 *	CX = user interrupt mask:
	 *		|F-8|7|6|5|4|3|2|1|0| user interrupt mask in CX
	 *		| | | | | | | | +--- cursor position changed
	 *		| | | | | | | +---- left button pressed
	 *		| | | | | | +----- left button released
	 *		| | | | | +------ right button pressed
	 *		| | | | +------- right button released
	 *		+--------------- unused
	 * Returns:
	 * 	CX = previous user interrupt mask
	 *	ES:DX = far pointer to previous user interrupt
	 *	AX = condition mask causing call
	 *	CX = horizontal cursor position
	 *	DX = vertical cursor position
	 *	DI = horizontal counts
	 *	SI = vertical counts
	 *	DS = mouse driver data segment
	 *	BX = button state:
	 *		|F-2|1|0|
	 *		| | +--- left button (1 = pressed)
	 *		| +---- right button (1 = pressed)
	 *		+------ unused
	 */
	private void swapInterruptSubroutine( IbmPcMouse mouse, Intel80x86 cpu ) {
		// TODO Have to figure this out
	}
	
	/** 
	 * Get Mouse Driver State and Memory Requirements
	 * Notes:
	 * 	Used before mouse functions 16h and 17h to determine memory
	 *	needed to save mouse state before giving up control of mouse
	 *	to another program
	 * Returns:
	 * 	BX = buffer size need to hold current mouse state
	 */
	private void getDriverStateAndRequirements( IbmPcMouse mouse, Intel80x86 cpu ) {
		cpu.BX.set( 1024 );
	}
	
	/**
	 * Save Mouse Driver State
	 * Notes:
	 * 	Used to save mouse information before relinquishing control
	 *	to another programs mouse handler
	 * Parameters:
	 * 	ES:DX = far pointer to mouse state save buffer
	 */
	private void saveDriverState( IbmPcMouse mouse, Intel80x86 cpu ) {
		// TODO Have to figure this out
	}
	
	/**
	 * Restore Mouse Driver State
	 * Notes:
	 * 	Used to restore mouse information after regaining control
	 *	from another programs mouse handler
	 * Parameters:
	 * 	ES:DX = far pointer to mouse state save buffer
	 */
	private void restoreDriverState( IbmPcMouse mouse, Intel80x86 cpu ) {
		// TODO Have to figure this out
	}
	
	/** 
	 * Set alternate subroutine call mask and address
	 * Parameters:
	 * 	DX = offset to function
	 *	CX = user interrupt mask:
	 *		|F-8|7|6|5|4|3|2|1|0| user interrupt mask in CX
	 *		| | | | | | | | +--- alt key pressed during event
	 *		| | | | | | | +---- ctrl key pressed during event
	 *		| | | | | | +----- shift key pressed during event
	 *		| | | | | +------ right button up event
	 *		| | | | +------- right button down event
	 *		| | | +-------- left button up event
	 *		| | +--------- left button down event
	 *		| +---------- cursor moved
	 *		+------------ unused
	 * Returns:
	 * 	AX = condition mask causing call
	 *	CX = horizontal cursor position
	 *	DX = vertical cursor position
	 *	DI = horizontal counts
	 *	SI = vertical counts
	 *	DS = mouse driver data segment
	 *	BX = button state:
	 *		|F-2|1|0|
	 *		| | +--- left button (1 = pressed)
	 *		| +---- right button (1 = pressed)
	 *		+------ unused
	 */
	private void setAlternateSubroutineCallMaskAndAddress( IbmPcMouse mouse, Intel80x86 cpu ) {
		// TODO Have to figure this out
	}
	
	/**
	 * Get User Alternate Interrupt Address
	 * Notes:
	 * 	+ Returns vector to function defined by ~INT 33,18~
	 * 	+ Searches the event handlers defined by INT 33,18 for a routine
	 *		with a call mask matching CX
	 * Parameters:
	 * 	CX = user interrupt call mask (see below)
	 * Returns:
	 *	BX:DX = user interrupt vector
	 *	CX = user interrupt call mask or zero if not found
	 *		|F-8|7|6|5|4|3|2|1|0| user interrupt mask in CX
	 *		| | | | | | | | +--- alt key pressed during event
	 *		| | | | | | | +---- ctrl key pressed during event
	 *		| | | | | | +----- shift key pressed during event
	 *		| | | | | +------ right button up event
	 *		| | | | +------- right button down event
	 *		| | | +-------- left button up event
	 *		| | +--------- left button down event
	 *		| +---------- cursor moved
	 *		+------------ unused
	 */
	private void getAlternateInterruptAddress( IbmPcMouse mouse, Intel80x86 cpu ) {
		// TODO Have to figure this out
	}
	
	/** 
	 * Set Mouse Sensitivity
	 * <pre>
	 * Notes:
	 *	1 Sets mouse sensitivity by setting the ratio of the mouse coordinates per screen pixel
	 *	2 Provides same results as calls to both {@link #setMousePixelRatio()} and {@link #setMouseDoubleSpeedThresthold()}
	 *	3 These values are not reset by {@link #resetMouse()}
	 * Parameters:
	 * 	BX = horizontal coordinates per pixel (| 100)
	 *	CX = vertical coordinates per pixel (| 100)
	 *	DX = double speed threshold
	 *</pre>
	 *@see #resetMouse()
	 */
	private void setSensitivity( IbmPcMouse mouse, Intel80x86 cpu ) {
		// TODO Have to figure this out
	}
	
	/** 
	 * Get Mouse Sensitivity
	 * Notes:
	 * 	+ Returns mouse sensitivity information as the number of mouse
	 *		coordinates per screen pixel
	 * Returns:
	 * 	BX = horizontal coordinates per pixel (% 100)
	 *	CX = vertical coordinates per pixel (% 100)
	 *	DX = double speed threshold
	 */
	private void getSensitivity( IbmPcMouse mouse, Intel80x86 cpu ) {
		// TODO Have to figure this out
	}
	
	/** 
	 * Set Mouse Interrupt Rate (InPort only)
	 * Notes:
	 * 	+ Work with the InPort mouse only
	 *	+ Sets the rate the mouse status is polled by the mouse driver
	 *	+ Faster rates provide better resolution but take away CPU time
	 *	+ Values in BX > 4 can cause unpredicatable results
	 * Parameters:
	 *	BX 	= rate code
	 *		= 0 no interrupts
	 *		= 1 30 interrupts per second
	 *		= 2 50 interrupts per second
	 *		= 3 100 interrupts per second
	 *		= 4 200 interrupts per second
	 */
	private void setInterruptRate( IbmPcMouse mouse, Intel80x86 cpu ) {
		// TODO Have to figure this out
	}
	
	/** 
	 * Set Mouse CRT Page
	 * Notes:
	 * 	Sets the CRT page which the mouse cursor is displayed
	 * Parameters:
	 *	BX = CRT page number
	 * @see IbmPcDisplay#setActivePage(int)
	 */
	private void setCrtPage( IbmPcMouse mouse, Intel80x86 cpu ) {
		// TODO Have to figure this out
	}
	
	/** 
	 * Get Mouse CRT Page
	 * Returns:
	 * 	BX = CRT page number cursor is displayed on
	 * @see IbmPcDisplay#getActivePage()
	 */
	private void getCrtPage( IbmPcMouse mouse, Intel80x86 cpu ) {
		// TODO Have to figure this out
	}
		
	/** 
	 * Disable Mouse Driver
	 * Returns:
	 * 	AX 	= 001F if successful
	 * 	 	= FFFF if error
	 * 	ES:BX = previous ~INT 33~ vector
	 */
	private void disableDriver( IbmPcMouse mouse, Intel80x86 cpu ) {
		// TODO Have to figure this out
	}
	
	/** 
	 * Enable Mouse Driver
	 * Notes:
	 * 	Reinstalls the mouse drivers interrupt vectors for 
	 * 	{@link VideoServices INT 10} and INT 71 (8088/86) and INT 74 (286/386)
	 */
	private void enableDriver( IbmPcMouse mouse, Intel80x86 cpu ) {
		// TODO Have to figure this out
	}
	
	/** 
	 * Reset Mouse Software
	 * Returns:
	 * 	AX 	= 0021 mouse driver not installed
	 *		= FFFF mouse driver installed
	 *	BX 	= 2 mouse driver installed
	 */
	private void resetSoftware( IbmPcMouse mouse, Intel80x86 cpu ) {
		cpu.AX.set( 0xFFFF );
		cpu.BX.set( 2 );
	}
	
	/** 
	 * Set Language for Messages
	 * Notes:
	 * 	 Only works with international version of the mouse driver
	 * Parameters:
	 * 	BX 	= language number (with /L switch value):
	 *		= 0 English n/a
	 *		= 1 French F
	 *		= 2 Dutch NL
	 *		= 3 German D
	 *		= 4 Swedish S
	 *		= 5 Finnish SF
	 *		= 6 Spanish E
	 *		= 7 Portuguese P
	 *		= 8 Italian I
	 */
	private void setLanguageForMessages( IbmPcMouse mouse, Intel80x86 cpu ) {
		// TODO Have to figure this out
	}
	
	/** 
	 * Get Language Number
	 * Returns: 
	 *	BX 	= language number (with /L switch value):
	 *		= 0 English n/a
	 *		= 1 French F
	 *		= 2 Dutch NL
	 *		= 3 German D
	 *		= 4 Swedish S
	 *		= 5 Finnish SF
	 *		= 6 Spanish E
	 *		= 7 Portuguese P
	 *		= 8 Italian I
	 */
	private void getLanguageNumber( IbmPcMouse mouse, Intel80x86 cpu ) {
		// TODO Have to figure this out
	}
	
	/** 
	 * Get Driver Version, Mouse Type & IRQ Number
	 * Notes:
	 * 	 Version 6.1 would be represented as BH = 06h, BL = 10h
	 * Returns:
	 * 	BH 	= major version (see above)
	 *	BL 	= minor version (see above)
	 *	CH 	= mouse type:
	 *		= 1 bus mouse
	 *		= 2 serial mouse
	 *		= 3 InPort mouse
	 *		= 4 PS/2 mouse
	 *		= 5 Hewlett Packard mouse
	 *	CL 	= IRQ number:
	 *		= 0 PS/2
	 *		= 2 ~IRQ~ 2
	 *		= 5 IRQ 5
	 *		= 7 IRQ 7
	 */
	private void getDriverVersionMouseTypeAndIRQ( IbmPcMouse mouse, Intel80x86 cpu ) {
		// set major & minor version
		cpu.BH.set( 0x06 );
		cpu.BL.set( 0x10 );
		
		// set mouse type
		cpu.CH.set( 4 );
		
		// set IRQ #
		cpu.CL.set( 0 );
	}
	
}