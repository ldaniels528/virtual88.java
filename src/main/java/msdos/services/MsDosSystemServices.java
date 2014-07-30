package msdos.services;


import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.x86.bios.IbmPcBIOS;
import ibmpc.devices.cpu.x86.bios.services.InterruptHandler;
import ibmpc.devices.display.IbmPcDisplay;
import ibmpc.devices.keyboard.IbmPcKeyboard;
import ibmpc.devices.memory.IbmPcRandomAccessMemory;
import ibmpc.exceptions.IbmPcException;
import ibmpc.exceptions.X86AssemblyException;
import ibmpc.system.IbmPcSystem;
import ibmpc.util.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Calendar;

import msdos.storage.MsDosFileAccessMode;
import msdos.storage.MsDosFileAttributes;
import msdos.storage.MsDosFileControlBlock;
import msdos.storage.MsDosFileHandle;
import msdos.storage.MsDosStorageSystem;

/**
 * MSDOS System Services (INT 21h) Interrupt Processor
 * @author lawrence.daniels@gmail.com
 */
public class MsDosSystemServices implements InterruptHandler {
	private static final MsDosSystemServices instance = new MsDosSystemServices();
	private static final int LAST_OFFSET = 0xFFFF;
	
	/**
	 * Private constructor
	 */
	private MsDosSystemServices() {
		super();
	}
	
	/**
	 * Returns the singleton instance of this class 
	 * @return the singleton instance of this class 
	 */
	public static MsDosSystemServices getInstance() {
		return instance;
	}
	
	/**
	 * Processes the MSDOS Services (INT 21h) Interrupt
	 * @throws IbmPcException 
	 */
	public void process( final Intel80x86 cpu ) 
	throws X86AssemblyException {
		// get the memory, display, and storage subsystems
		final IbmPcSystem system = cpu.getSystem();
		final IbmPcRandomAccessMemory memory = system.getRandomAccessMemory();
		final IbmPcDisplay display = system.getDisplay();
		final MsDosStorageSystem disk = (MsDosStorageSystem)system.getStorageSystem();
		
		try {
			// determine what to do
			switch( cpu.AH.get() ) {
				case 0x01:	readCharacterFromSTDIN( cpu ); break;
				case 0x02: 	writeCharacterToSTDOUT( display, cpu ); break;
				case 0x03:	waitForAuxiliaryDeviceInput( system, cpu ); break;
				case 0x04:	waitForAuxiliaryDeviceOutput( system, cpu ); break;
				case 0x05: 	writeCharacterToPrinter( cpu ); break;
				case 0x06:	directConsoleIO( system, display, cpu ); break;
				case 0x07:	directConsoleInputWithoutEcho( system, cpu ); break;
				case 0x08:	consoleInputWithEcho( system, cpu ); break;
				case 0x09: 	displayString( display, memory, cpu ); break;
				case 0x0A:	bufferedKeyboardInput( system, cpu ); break;
				case 0x0B:	checkStandardInputStatus( system, cpu ); break;
				case 0x0C:	clearKeyboardBufferAndInvokeKeyboardFunction( system, cpu ); break;
				case 0x0D:	diskReset( disk, cpu ); break;
				case 0x0E:	selectDefaultDrive( disk, cpu ); break;
				case 0x0F:	openFileUsingFCB( disk, cpu ); break;
				case 0x10:	closeFileUsingFCB( disk, cpu ); break;
				case 0x11:	searchForFirstEntryUsingFCB( disk, cpu ); break;
				case 0x12:	searchForNextEntryUsingFCB( disk, cpu ); break;
				case 0x13:	deleteFileUsingFCB( disk, cpu ); break;
				case 0x14:	sequentialReadUsingFCB( disk, cpu ); break;
				case 0x15:	sequentialWriteUsingFCB( disk, cpu ); break;
				case 0x16:	createFileUsingFCB( disk, cpu ); break;
				case 0x17:	renameFileUsingFCB( disk, cpu ); break;
				// sub-function 18h does not exist
				case 0x19:	getDefaultDrive( disk, cpu ); break;
				case 0x1A:	setDiskTransferArea( disk, cpu ); break;
				case 0x1B:	getAllocationTableInformation( disk, cpu ); break;
				case 0x1C:	getAllocationTableInfoForSpecifiedDrive( disk, cpu ); break;
				// sub-functions 1Dh - 1Eh do not exist
				case 0x1F:	getPointerToCurrentDriveParameterTable( disk, cpu ); break;
				// subfunction 20h - Undocumented function?
				case 0x21:	randomReadUsingFCB( disk, cpu ); break;
				case 0x22:	randomWriteUsingFCB( disk, cpu ); break;
				case 0x23:	getFileSizeUsingFCB( disk, cpu); break;
				case 0x24:	setRelativeRecordFieldInFCB( disk, cpu ); break;
				case 0x25:	setInterruptVector( memory, system, cpu ); break;
				case 0x26:	createNewProgramSegmentPrefix( system, cpu ); break;
				// TODO 27h - 29h
				case 0x2A: 	getSystemDate( cpu ); break;
				case 0x2B: 	setSystemDate ( cpu ); break;
				case 0x2C: 	getSystemTime( cpu ); break;
				case 0x2D: 	setSystemTime( cpu ); break;
				case 0x2E:	setVerifySwitch( disk, cpu ); break;
				case 0x2F:	getDiskTransferArea( disk, cpu ); break;
				case 0x30: 	getDOSVersion( cpu ); break;
				case 0x31:	terminateProcessAndRemainResident( cpu ); break;
				// TODO 32h - 34h
				case 0x35:	getInterruptVector( system, cpu ); break;
				case 0x36: 	getFreeDiskSpace( cpu ); break;
				// TODO 37h - 38h
				case 0x39: 	mkdir( memory, cpu ); break;	
				case 0x3A: 	rmdir( memory, cpu ); break;
				case 0x3B: 	chdir( disk, memory, cpu ); break;
				case 0x3C: 	createFileUsingHandle( disk, memory, cpu ); break;
				case 0x3D: 	openFileUsingHandle( disk, memory, cpu ); break;
				case 0x3E:	closeFileUsingHandle( disk, memory, cpu ); break;
				case 0x3F: 	readFromFileOrDeviceUsingHandle( disk, memory, cpu ); break;
				case 0x40:	writeToFileOrDeviceUsingHandle( disk, memory, cpu ); break;
				case 0x41:	deleteFile( disk, memory, cpu ); break;
				case 0x42:	moveFilePointerUsingHandle( disk, memory, cpu ); break;
				case 0x43:	getSetFileAttributes( disk, memory, cpu ); break;
				case 0x44:	inputOutputControlForDevices( disk, memory, cpu ); break;
				// TODO 45h - 46h
				case 0x47: 	getCurrentWorkingDirectory( disk, memory, cpu ); break;
				// TODO 48h - 4Ah
				case 0x4B:	loadAndExecuteProgram( disk, memory, cpu ); break;
				case 0x4C: 	exitWithReturnCode( cpu ); break;				
				// TODO 4Dh - 55h
				case 0x56: 	renameFile( disk, memory, cpu ); break;
				case 0x57: 	getOrSetFileLastModifiedDateTime( cpu ); break;
				case 0x5B:	createFile( disk, memory, cpu ); break;
				// TODO 5Ch - 66h
				case 0x67:	setHandleCount( disk, memory, cpu ); break;
				
				// Unhandled Function
				default:	
					throw new X86AssemblyException( String.format( "Invalid call (AH = %02X)", cpu.AH.get() ) );
			}
		}
		catch( final IbmPcException e ) {
			throw new X86AssemblyException( e );
		}
	}
	
	/**
	 * <pre>
	 * Returns a DOS string (that ends with NUL (zero))
	 * Entry: Usually DS:DX -> ASCIZ string
	 * </pre>
	 * @param segment the given segment (usually DS)
	 * @param offset the given offset (usually DX)
	 * @return the string that was found at DS:DX
	 */
	private String getASCIZString( final int segment, final int offset, final IbmPcRandomAccessMemory memory ) {		
		// determine the full length of the string
		int length = 0;	
		while( ( offset + length < LAST_OFFSET ) && 
				memory.getByte( segment, offset + length ) != 0 ) length++;
		
		// extract the string
		final byte[] string = memory.getBytes( segment, offset, length );
		return new String( string );
	}
	
	/**
	 * Writes a DOS string to DS:SI (64 characters max)
	 * @param string the given string
	 */
	private void setDosString( final IbmPcRandomAccessMemory memory, 
							   final Intel80x86 cpu, 
							   final String string ) {
		// get the segment and offset of the string
		final int segment = cpu.DS.get();
		final int offset  = cpu.SI.get();
		
		// get the string as a collection of bytes
		final byte[] data = string.length() < 65 
							? string.getBytes() 
							: string.substring( 0, 65 ).getBytes();
							
		// write the data to the buffer			
		memory.setBytes( segment, offset, data, data.length );
	}	
	
	/** 
	 * Reads a character from STDIN
	 * Return: AL = character read
	 */
	private void readCharacterFromSTDIN( final Intel80x86 cpu ) {
		// TODO Implement me!
		throw new IllegalStateException( "readCharacterFromSTDIN: Not Yet Implemented" );
	}
	
	/**
	 * Writes a character to STDOUT 
	 * Entry: DL = character to write
	 * Return: AL = last character output
	 */
	private void writeCharacterToSTDOUT( final IbmPcDisplay display, final Intel80x86 cpu ) {
		// get the character from DL
		final String chra = String.valueOf( (char)cpu.DL.get() );
		
		// copy the character to AL
		cpu.DL.set( cpu.DL.get() );
		
		// write the character to STDOUT
		display.write( chra );
		display.update();
	}
	
	/**
	 * Writes a character to the Printer
	 * Entry: DL = character to print
	 * @throws X86AssemblyException 
	 */
	private void writeCharacterToPrinter( final Intel80x86 cpu ) {
		// TODO Implement me!
		throw new IllegalStateException( "writeCharacterToPrinter: Not Yet Implemented" );
	}
	
	/** 
	 * <pre>
	 * INT 21,6 - Direct Console I/O
	 * AH = 06
	 * DL = (0-FE) character to output
	 *    = FF if console input request
	 * on return:
	 * AL = input character if console input request (DL=FF)
	 * ZF = 0 if console request character available (in AL)
	 *    = 1 if no character is ready, and function request
	 *        was console input
	 * - reads from or writes to the console device depending on
	 *   the value of DL
	 * - cannot output character FF (DL=FF indicates read function)
	 * - for console read, no echo is produced
	 * - returns 0 for extended keystroke, then function must be
	 *   called again to return scan code
	 * - ignores Ctrl-Break and Ctrl-PrtSc
	 * </pre>
	 */
	private void directConsoleIO( final IbmPcSystem system, final IbmPcDisplay display, final Intel80x86 cpu ) {
		// cache the value in DL
		final int _DL = cpu.DL.get();
		
		// is this a console input request?
		final boolean inputReq = ( _DL == 0xFF );
		if( inputReq ) {
			// get the keyboard instance
			final IbmPcKeyboard keyboard = system.getKeyboard();
			synchronized( keyboard ) {
				// are keystrokes waiting to be consumed?
				final boolean keystrokesWaiting = keyboard.keyStrokesWaiting();
				
				// if a keystroke is waiting, put it in AL
				if( keystrokesWaiting ) {
					cpu.AL.set( keyboard.nextChar() );
				}
				
				// set flags
				cpu.FLAGS.setZF( !keystrokesWaiting );
			}
		}
		
		// must be output of a character
		else {
			// cache the charaacter and display page
			final char character  = (char)_DL;
			final int displayPage = display.getActivePage();
			
			// display the string
			display.writeCharacter( displayPage, character, true );
			display.update();
		}

	}
	
	/** 
	 * <pre>
	 * INT 21,7 - Direct Console Input Without Echo
	 * AH = 07
	 * on return:
	 * AL = character from STDIN
	 * - waits for keyboard input until keystroke is ready
	 * - character is not echoed to STDOUT
	 * - returns 0 for extended keystroke, then function must be
	 *   called again to return scan code
	 * - ignores Ctrl-Break and Ctrl-PrtSc
	 * - see ~INT 21,1~
	 * </pre>
	 */
	private void directConsoleInputWithoutEcho( final IbmPcSystem system, final Intel80x86 cpu ) {
		// TODO Implement me!
		throw new IllegalStateException( "directConsoleInputWithoutEcho: Not Yet Implemented" );
	}
	
	/** 
	 * INT 21,8 - Console Input Without Echo
	 * AH = 08
	 * on return:
	 * AL = character from STDIN
	 * - returns 0 for extended keystroke, then function must be
	 *   called again to return scan code
	 * - waits for character from STDIN and returns data in AL
	 * - if ~Ctrl-Break~ is detected, ~INT 23~ is executed
	 */
	private void consoleInputWithEcho( final IbmPcSystem system, final Intel80x86 cpu ) {
		// TODO Implement me!
		throw new IllegalStateException( "consoleInputWithEcho: Not Yet Implemented" );
	}
	
	/** 
	 * <pre>
	 * INT 21,3 - Wait for Auxiliary Device Input
	 * AH = 03
	 * on return:
	 * AL = character from the auxiliary device
	 * - does not supply error returns
	 * - waits for character and reads from STDAUX
	 * - default DOS AUX parameters are 2400,N,8,1
	 * </pre>
	 */
	private void waitForAuxiliaryDeviceInput( final IbmPcSystem system, final Intel80x86 cpu ) {
		// TODO Implement me!
		throw new IllegalStateException( "waitForAuxiliaryDeviceInput: Not Yet Implemented" );
	}
	
	/** 
	 * <pre>
	 * INT 21,4 - Auxiliary Output
	 * AH = 04
	 * DL = character to output
	 * returns nothing
	 * - sends character in DL to STDAUX
	 * - does not supply error returns
	 * - waits until STDAUX is available
	 * - default DOS AUX parameters are 2400,N,8,1
	 * </pre>
	 */
	private void waitForAuxiliaryDeviceOutput( final IbmPcSystem system, final Intel80x86 cpu ) {
		// TODO Implement me!
		throw new IllegalStateException( "waitForAuxiliaryDeviceOutput: Not Yet Implemented" );
	}
	
	/**
	 * <pre>
	 * INT 21,9 - Print String to STDOUT
	 * AH = 09
	 * DS:DX = pointer to string ending in "$"
	 * returns nothing
	 * - outputs character string to STDOUT up to "$"
	 * - backspace is treated as non-destructive
	 * - if ~Ctrl-Break~ is detected, ~INT 23~ is executed
	 * </pre>
	 */
	private void displayString( final IbmPcDisplay display, 
								final IbmPcRandomAccessMemory memory, 
								final Intel80x86 cpu ) {
		// get the segment and offset of the start of the string
		final int segment	= cpu.DS.get();
		final int offset	= cpu.DX.get();
		
		// determine the full length of the string
		int length = 0;	
		while( ( offset + length < LAST_OFFSET ) && 
				memory.getByte( segment, offset + length ) != '$' ) length++;
		
		// extract the string
		final byte[] bytes = memory.getBytes( segment, offset, length );
		final String string = new String( bytes );
		
		// display the string
		display.write( string );
		display.update();
	}
	
	/** 
	 * <pre>
	 * INT 21,A - Buffered Keyboard Input
	 * AH = 0A
	 * DS:DX = pointer to input buffer of the format:
	 * 
	 * | max | count | BUFFER (N bytes)
	 * | | +------ input buffer
	 * | +------------ number of characters returned (byte)
	 * +-------------- maximum number of characters to read (byte)
	 * returns nothing
	 * - since strings can be pre-loaded, it is recommended that the
	 *   default string be terminated with a CR
	 * - N bytes of data are read from STDIN into buffer+2
	 * - max buffer size is 255, minimum buffer size is 1 byte
	 * - chars up to and including a CR are placed into the buffer
	 *   beginning at byte 2; Byte 1 returns the number of chars
	 *   placed into the buffer (extended codes take 2 characters)
	 * - DOS editing keys are active during this call
	 * - ~INT 23~ is called if Ctrl-Break or Ctrl-C detected
	 * </pre>
	 */
	private void bufferedKeyboardInput( final IbmPcSystem system, final Intel80x86 cpu  ) {
		throw new IllegalStateException( "Not yet implemented" );
	}
	
	/** 
	 * <pre>
	 * INT 21,B - Check Standard Input Status
	 * AH = 0B
	 * on return:
	 * AL = 00 if no character available
	 *    = FF if character available
	 * - checks STDIN for available characters
	 * - character is not returned
	 * - if Ctrl-Break is detected ~INT 23~ is executed
	 * </pre>
	 */
	private void checkStandardInputStatus( final IbmPcSystem system, final Intel80x86 cpu ) {
		// TODO Implement me!
		throw new IllegalStateException( "Not yet implemented" );
	}
	
	/** 
	 * <pre>
	 * INT 21,C - Clear Keyboard Buffer and Invoke Keyboard Function
	 * AH = 0C
	 * AL = 01, 06, 07, 08 or 0A (INT 21 input functions)
	 * on return:
	 * see return values from INT 21,AL where AL is 1, 6, 7, 8 or A
	 * - main function is to clear the input buffer and call INT 21h with
	 *   the specified function (in AL)
	 * - see ~INT 21,1~, ~INT 21,6~, ~INT 21,7~, ~INT 21,8~ & ~INT 21,A~
	 * </pre>
	 */
	private void clearKeyboardBufferAndInvokeKeyboardFunction( final IbmPcSystem system, final Intel80x86 cpu ) {
		// TODO Implement me!
		throw new IllegalStateException( "Not yet implemented" );
	}
	
	/** 
	 * <pre>
	 * INT 21,D - Disk Reset
	 * AH = 0D
	 * returns nothing
	 * - all file buffers are flushed to disk
	 * - does NOT update directory entry
	 * <pre>
	 * @param disk the given {@link MsDosStorageSystem storage device} instance
	 * @param cpu the given {@link Intel80x86 CPU} instance
	 */
	private void diskReset( final MsDosStorageSystem disk, final Intel80x86 cpu ) {
		disk.reset();
	}

	/**
	 * <pre> 
	 * INT 21,E - Select Disk
	 * AH = 0E
	 * DL = zero based, drive number (0-25, A: - Z:)
	 * on return:
	 * AL = one based, total number of logical drives including
	 * hardfiles (1-26)
	 * - for DOS 3.x+, this function returns the number of logical
	 * drives or the value of LASTDRIVE (default of 5) in the
	 * CONFIG.SYS file
	 * </pre>
	 * @param disk the given {@link MsDosStorageSystem storage device} instance
	 * @param cpu the given {@link Intel80x86 CPU} instance
	 */
	private void selectDefaultDrive( final MsDosStorageSystem disk, final Intel80x86 cpu ) {
		disk.setDefaultDrive( cpu.DL.get() );		
	}
	
	/** 
	 * <pre>
	 * INT 21,F - Open a File Using FCB
	 * AH = 0F
	 * DS:DX = pointer to unopened {@link MsDosFileControlBlock FCB}
	 * on return:
	 * AL = 00 if file opened
	 *    = FF if unable to open
	 * - opens an existing file using a previously setup FCB
	 * - the FCB fields drive identifier, filename and extension
	 *   must be filled in before call
	 * - sets default FCB fields; current block number is set to 0;
	 *   record size is set to 80h; file size, date and time are set
	 *   to the values from the directory
	 * - does not create file, see ~INT 21,16~
	 * - DOS 2.x allows opening of subdirectories, DOS 3.x does not
	 * </pre>
	 * @param disk the given {@link MsDosStorageSystem storage device} instance
	 * @param cpu the given {@link Intel80x86 CPU} instance
	 */
	private void openFileUsingFCB( final MsDosStorageSystem disk, final Intel80x86 cpu ) {
		// TODO Implement me!
		Logger.error( "openFileUsingFCB: not yet implemented\n" );
		cpu.AL.set( 0xFF );
	}
	
	/** 
	 * <pre>
	 * INT 21,10 - Close a File Using FCB
	 * AH = 10h
	 * DS:DX = pointer to opened {@link MsDosFileControlBlock FCB}
	 * on return:
	 * AL = 00 if file closed
	 *    = FF if file not closed
	 * - closes a previously opened file opened with an FCB
	 * - FCB must be setup with drive id, filename, and extension
	 * before call
	 * </pre>
	 * @param disk the given {@link MsDosStorageSystem storage device} instance
	 * @param cpu the given {@link Intel80x86 CPU} instance
	 */
	private void closeFileUsingFCB( final MsDosStorageSystem disk, final Intel80x86 cpu ) {
		// TODO Implement me!
		Logger.error( "closeFileUsingFCB: not yet implemented\n" );
		cpu.AL.set( 0xFF );
	}
	
	/** 
	 * <pre>
	 * INT 21,11 - Search for First Entry Using FCB
	 * AH = 11h
	 * DS:DX = pointer to unopened {@link MsDosFileControlBlock FCB}
	 * on return:
	 * AL = 00 if matching file found
	 *    = FF if file not found
	 * - searches for first occurrence of filename specified in FCB
	 * - FCB must have drive id, filename, and extension before call
	 * - extended FCB can be used to specify a search criteria based
	 *   on attributes; hidden, system, label, and directory attributes
	 *   can be used to narrow the search (see ~FILE ATTRIBUTES~)
	 * - after successful call DTA holds an unopened ~FCB~/~XFCB~ for
	 *   the requested file. Using any of the other FCB functions
	 *   destroys this ~DTA~ copy of the FCB/XFCB
	 * - searching can be continued with the FCB find-next function
	 * - "?" wildcard supported after DOS 2.1, "*" supported in DOS 3.x
	 * - DOS 2.x can't find . and .. entries, DOS 3.x can (unless in root)
	 * </pre>
	 * @param disk the given {@link MsDosStorageSystem storage device} instance
	 * @param cpu the given {@link Intel80x86 CPU} instance
	 * @see #searchForNextEntryUsingFCB(MsDosStorageSystem, Intel80x86)
	 */
	private void searchForFirstEntryUsingFCB( final MsDosStorageSystem disk, final Intel80x86 cpu ) {
		// TODO Implement me!
		Logger.error( "searchForFirstEntryUsingFCB: not yet implemented\n" );
		cpu.AL.set( 0xFF );
	}
	
	/** 
	 * <pre>
	 * INT 21,12 - Search for Next Entry Using FCB
	 * AH = 12h
	 * DS:DX = pointer to unopened {@link MsDosFileControlBlock FCB} returned from
	 * {@link #searchForFirstEntryUsingFCB(MsDosStorageSystem, Intel80x86) INT 21,11} 
	 * or {@link #searchForNextEntryUsingFCB(MsDosStorageSystem, Intel80x86) INT 21,12}
	 * on return:
	 * AL = 00 if file found
	 *    = FF if file not found
	 * - finds next matching file after calls to ~INT 21,11~ and
	 * ~INT 21,12~
	 * - FCB should be the same across calls to INT 21,11 and 12
	 * - after successful call ~DTA~ holds an unopened ~FCB~/~XFCB~ for
	 *   the requested file. Using any of the other FCB functions
	 *   destroys this DTA copy of the FCB/XFCB
	 * </pre>
	 * @param disk the given {@link MsDosStorageSystem storage device} instance
	 * @param cpu the given {@link Intel80x86 CPU} instance
	 * @see #searchForFirstEntryUsingFCB(MsDosStorageSystem, Intel80x86)
	 */
	private void searchForNextEntryUsingFCB( final MsDosStorageSystem disk, final Intel80x86 cpu ) {
		// TODO Implement me!
		Logger.error( "searchForFirstEntryUsingFCB: not yet implemented\n" );
		cpu.AL.set( 0xFF );
	}
	
	/** 
	 * <pre>
	 * INT 21,13 - Delete File Using FCB
	 * AH = 13h
	 * DS:DX = pointer to an unopened {@link MsDosFileControlBlock FCB}
	 * on return:
	 * AL = 00 if file deleted
	 *    = FF if file not found
	 * - deletes unopened file with normal attributes
	 * - FCB must contain drive id, filename, and extension before call
	 * - "?" wildcard supported after DOS 2.1, "*" supported by DOS 3.x+
	 * - DOS 2.x allowed deletion of a subdirectory if ~XFCB~ was provided,
	 *   even if files existed, causing lost clusters. DOS 3.x does not
	 * </pre>
	 * @param disk the given {@link MsDosStorageSystem storage device} instance
	 * @param cpu the given {@link Intel80x86 CPU} instance
	 */
	private void deleteFileUsingFCB( final MsDosStorageSystem disk, final Intel80x86 cpu ) {
		// TODO Implement me!
		Logger.error( "deleteFileUsingFCB: not yet implemented\n" );
		cpu.AL.set( 0xFF );
	}
	
	/** 
	 * <pre>
	 * INT 21,14 - Sequential Read Using FCB
	 * AH = 14h
	 * DS:DX = pointer to an opened {@link MsDosFileControlBlock FCB}
	 * on return:
	 * AL = 00 if successful read
	 *    = 01 if end of file (no data read)
	 *    = 02 if ~DTA~ is too small
	 *    = 03 if end of file or partial record read
	 * - reads a record from file pointed to by FCB at the location
	 *   specified in current block and current record number
	 * - data record is read into the DTA
	 * - FCB record number is updated
	 * </pre>
	 * @param disk the given {@link MsDosStorageSystem storage device} instance
	 * @param cpu the given {@link Intel80x86 CPU} instance
	 */
	private void sequentialReadUsingFCB( final MsDosStorageSystem disk, final Intel80x86 cpu ) {
		// TODO Implement me!
		Logger.error( "sequentialReadUsingFCB: not yet implemented\n" );
		cpu.AL.set( 0xFF );
	}
	
	/** 
	 * <pre>
	 * INT 21,15 - Sequential Write Using FCB
	 * AH = 15h
	 * DS:DX = pointer to an opened {@link MsDosFileControlBlock FCB}
	 * on return:
	 * AL = 00 if write was successful
	 *    = 01 if diskette is full or read only
	 *    = 02 if ~DTA~ is too small
	 * - writes a record from the DTA to the current record position
	 *   in file specified by the opened FCB
	 * - record size and output location are maintained in the FCB
	 * </pre>
	 * @param disk the given {@link MsDosStorageSystem storage device} instance
	 * @param cpu the given {@link Intel80x86 CPU} instance
	 */
	private void sequentialWriteUsingFCB( final MsDosStorageSystem disk, final Intel80x86 cpu ) {
		// TODO Implement me!
		Logger.error( "sequentialWriteUsingFCB: not yet implemented\n" );
		cpu.AL.set( 0xFF );
	}
	
	/** 
	 * <pre>
	 * INT 21,16 - Create a File Using FCB
	 * AH = 16h
	 * DS:DX = pointer to an unopened {@link MsDosFileControlBlock FCB}
	 * on return:
	 * AL = 00 if file created
	 *    = FF if file creation failed
	 * - creates file using FCB and leaves open for later output
	 * - FCB must be setup with drive id, filename, and extension
	 *   before call
	 * - an extended FCB can be used to also set ~file attributes~
	 * </pre>
	 * @param disk the given {@link MsDosStorageSystem storage device} instance
	 * @param cpu the given {@link Intel80x86 CPU} instance
	 */
	private void createFileUsingFCB( final MsDosStorageSystem disk, final Intel80x86 cpu ) {
		// TODO Implement me!
		Logger.error( "createFileUsingFCB: not yet implemented\n" );
		cpu.AL.set( 0xFF );
	}
	
	/**
	 * <pre>
	 * INT 21,17 - Rename a File Using FCB
	 * AH = 17h
	 * DS:DX = pointer to a modified {@link MsDosFileControlBlock FCB} of the format:
	 * Offset Description
	 * 00 drive designator
	 * 01 original file name
	 * 09 original file extension
	 * 11 new file name
	 * 19 new extension
	 * on return:
	 * AL = 00 if file renamed
	 *    = FF if file not renamed
	 * - allows renaming of files with normal attributes
	 * - "?" wildcard supported after DOS 2.1, "*" supported by
	 * DOS 3.x+
	 * </pre>
	 * @param disk the given {@link MsDosStorageSystem storage device} instance
	 * @param cpu the given {@link Intel80x86 CPU} instance
	 */
	private void renameFileUsingFCB( final MsDosStorageSystem disk, final Intel80x86 cpu ) {
		// TODO Implement me!
		Logger.error( "renameFileUsingFCB: not yet implemented\n" );
		cpu.AL.set( 0xFF );
	}
	
	/** 
	 * <pre>
	 * INT 21,21 - Random Read Using FCB
	 * AH = 21h
	 * DS:DX = pointer to an opened {@link MsDosFileControlBlock FCB}
	 * on return:
	 * AL = 00 if read successful
	 *    = 01 if EOF (no data read)
	 *    = 02 if ~DTA~ is too small
	 *    = 03 if EOF (partial record read)
	 * - reads random records from a file opened with an FCB
	 *   to the DTA
	 * - FCB must be setup with drive id, filename, extension,
	 *   record position and record length before call
	 * - current record position field in FCB is not updated
	 * </pre>
	 */
	private void randomReadUsingFCB( final MsDosStorageSystem disk, final Intel80x86 cpu ) {
		// TODO Implement me!
		Logger.error( "randomReadUsingFCB: not yet implemented\n" );
		cpu.AL.set( 0xFF );
	}
	
	/** 
	 * <pre>
	 * INT 21,22 - Random Write Using FCB
	 * AH = 22h
	 * DS:DX = far pointer to an opened {@link MsDosFileControlBlock FCB}
	 * on return:
	 * AL = 00 if write successful
	 *    = 01 if diskette full or read only
	 *    = 02 if ~DTA~ is too small
	 * - write records to random location in file opened with FCB
	 * - FCB must be setup with drive id, filename, extension,
	 *   record position and record length before call
	 * - current record position field in FCB is not updated
	 *</pre>
	 */
	private void randomWriteUsingFCB( final MsDosStorageSystem disk, final Intel80x86 cpu ) {
		// TODO Implement me!
		Logger.error( "randomWriteUsingFCB: not yet implemented\n" );
		cpu.AL.set( 0xFF );
	}
	
	/** 
	 * <pre>
	 * INT 21,2E - Set/Reset Verify Switch
	 * AH = 2E
	 * AL = 00 to set off
	 *    = 01 to set verify on
	 * DH = 00 for DOS versions before 3.0
	 * returns nothing
	 * - with the verify setting on, disk I/O is more secure but
	 *   takes longer to complete
	 * - see ~INT 21,54~
	 * </pre>
	 */
	private void setVerifySwitch( final MsDosStorageSystem disk, final Intel80x86 cpu ) {
		disk.setVerify( true );
	}
	
	/**
	 * <pre>
	 * INT 21,23 - Get File Size Using FCB
	 * AH = 23h
	 * DS:DX = pointer to an unopened {@link MsDosFileControlBlock FCB}
	 * on return:
	 * AL = 00 if successful
	 *    = FF if file not found
	 * - determines the number of records in a file
	 * - FCB must be setup with drive id, complete filename and
	 *   extension plus record length before call
	 * - updates random record position in FCB located at DS:DX
	 *   with file record count
	 * </pre>
	 */
	private void getFileSizeUsingFCB( final MsDosStorageSystem disk, final Intel80x86 cpu ) {
		// TODO Implement me!
		Logger.error( "getFileSizeUsingFCB: not yet implemented\n" );
		cpu.AL.set( 0xFF );
	}
	
	/** 
	 * <pre>
	 * INT 21,24 - Set Relative Record Field in FCB
	 * AH = 24h
	 * DS:DX = pointer to an opened {@link MsDosFileControlBlock FCB}
	 * returns nothing
	 * - modifies opened FCB for random operation
	 * - sets FCB random record field to current sequential block
	 *   and record fields
	 * </pre>
	 */
	private void setRelativeRecordFieldInFCB( final MsDosStorageSystem disk, final Intel80x86 cpu ) {
		// TODO Implement me!
		Logger.error( "setRelativeRecordFieldInFCB: not yet implemented\n" );
		cpu.AL.set( 0xFF );
	}
	
	/** 
	 * <pre>
	 * INT 21,19 - Get Current Default Drive
	 * AH = 19h
	 * on return:
	 * AL = current default drive (0=A,1=B,etc)
	 * - determines the current default drive
	 * </pre>
	 * @param disk the given {@link MsDosStorageSystem storage device} instance
	 * @param cpu the given {@link Intel80x86 CPU} instance
	 */
	private void getDefaultDrive( final MsDosStorageSystem disk, final Intel80x86 cpu ) {
		cpu.AL.set( disk.getDefaultDrive() );
	}
	
	/** 
	 * <pre>
	 * INT 21,1A - Set Disk Transfer Address (DTA)
	 * AH = 1A
	 * DS:DX = pointer to disk transfer address (~DTA~)
	 * returns nothing
	 * - specifies the disk transfer address to DOS
	 * - DTA cannot overlap 64K segment boundary
	 * - offset 80h in the ~PSP~ is a 128 byte default DTA supplied
	 *   by DOS upon program load
	 * - use of the DTA provided by DOS will result in the loss
	 *   of the program command tail which also occupies the 128
	 *   bytes starting at offset 80h of the PSP
	 * </pre>
	 * @param disk the given {@link MsDosStorageSystem storage device} instance
	 * @param cpu the given {@link Intel80x86 CPU} instance
	 * @see #getDiskTransferArea(MsDosStorageSystem, Intel80x86)
	 */
	private void setDiskTransferArea( final MsDosStorageSystem disk, final Intel80x86 cpu ) {
		// TODO Implement me!
		Logger.error( "setDiskTransferArea: not yet implemented\n" );
		cpu.AL.set( 0xFF );
	}
	
	/**
	 * <pre>
	 * INT 21,1B - Get Allocation Table Information
	 * AH = 1B
	 * on return:
	 * AL = sectors per cluster
	 * CX = bytes per sector
	 * DX = clusters on disk
	 * DS:BX = pointer to ~Media Descriptor Byte~ found in ~FAT~
	 * - retrieves information on capacity and format of default drive
	 * - DS:BX can be used to determine if drive is RAMDISK or removable
	 * - see ~INT 21,1C~
	 * </pre>
	 * @param disk the given {@link MsDosStorageSystem storage device} instance
	 * @param cpu the given {@link Intel80x86 CPU} instance
	 */
	private void getAllocationTableInformation( final MsDosStorageSystem disk, final Intel80x86 cpu ) {
		// TODO Implement me!
		Logger.error( "getAllocationTableInformation: not yet implemented\n" );
		cpu.AL.set( 0xFF );
	}
	
	/** 
	 * <pre>
	 * INT 21,1C - Get Allocation Table Info for Specified Drive
	 * AH = 1C
	 * DL = drive number (0 for default, 1 = A:, Z: = 26)
	 * on return:
	 * AL = sectors per cluster
	 * CX = bytes per sector
	 * DX = clusters on disk
	 * DS:BX = pointer to ~Media Descriptor Byte~ found in ~FAT~
	 * - retrieves information on capacity and format of specified drive
	 * - DS:BX can be used to determine if drive is RAMDISK or removable
	 * </pre>
	 * @param disk the given {@link MsDosStorageSystem storage device} instance
	 * @param cpu the given {@link Intel80x86 CPU} instance
	 * @see #getAllocationTableInformation(MsDosStorageSystem, Intel80x86)
	 */
	private void getAllocationTableInfoForSpecifiedDrive( final MsDosStorageSystem disk, final Intel80x86 cpu ) {
		// TODO Implement me!
		Logger.error( "getAllocationTableInfoForSpecifiedDrive: not yet implemented\n" );
		cpu.AL.set( 0xFF );
	}
	
	/** 
	 * <pre>
	 * INT 21,1F - Get Pointer to Current Drive Parameter Table (Undocumented)
	 * AH = 1F
	 * DL = drive number (0=default, 1=A, ...)
	 * on return:
	 * AL = 00 DS:BX is pointer to drive parameter table (~DPT~)
	 * 		FF drive does not exist
	 * DS:BX = pointer to drive parameter table (DPT) if AL=0
	 * - the format of the DPT varies between DOS versions
	 * - calls ~INT 21,32~ with DL=00 for DOS version 2.x+
	 * </pre>
	 */
	private void getPointerToCurrentDriveParameterTable( final MsDosStorageSystem disk, final Intel80x86 cpu ) {
		// TODO Implement me!
		Logger.error( "getPointerToCurrentDriveParameterTable: not yet implemented\n" );
		cpu.AL.set( 0xFF );
	}
	
	/** 
	 * <pre>
	 * INT 21,35 - Get Interrupt Vector
	 * AH = 35h
	 * AL = interrupt vector number
	 * on return:
	 * ES:BX = pointer to interrupt handler
	 * - standard method for retrieving interrupt vectors
	 * </pre>
	 * @param system the given {@link IbmPcSystem IBM PC System} instance
	 * @param cpu the given {@link Intel80x86 CPU} instance
	 * @see #setInterruptVector(IbmPcRandomAccessMemory, IbmPcSystem, Intel80x86)
	 */
	private void getInterruptVector( final IbmPcSystem system, final Intel80x86 cpu ) {
		// get the BIOS
		final IbmPcBIOS bios = system.getBIOS();
		
		// get the input parameters
		final int interruptNum	= cpu.AL.get();
		
		// get the vector address
		final int[] address = bios.getInterruptVector( interruptNum );
		
		// set output parameters
		cpu.ES.set( address[1] );
		cpu.BX.set( address[0] );
	}
	
	/** 
	 * <pre>
	 * INT 21,25 - Set Interrupt Vector
	 * AH = 25h
	 * AL = interrupt number
	 * DS:DX = pointer to interrupt handler
	 * returns nothing
	 * - provides a safe method for changing interrupt vectors
	 * </pre>
	 * @param memory the given {@link IbmPcRandomAccessMemory memory} instance
	 * @param system the given {@link IbmPcSystem IBM PC System} instance
	 * @param cpu the given {@link Intel80x86 CPU} instance
	 * @see #getInterruptVector(IbmPcSystem, Intel80x86)
	 */
	private void setInterruptVector( final IbmPcRandomAccessMemory memory, 
									 final IbmPcSystem system,
									 final Intel80x86 cpu ) {
		// get the input parameters
		final int interruptNum	= cpu.AL.get();
		final int baseSegment	= cpu.DS.get();
		final int baseOffset	= cpu.DX.get();
		
		// get the segment and offset pointed to by DS:[DX]
		final int segment		= memory.getWord( baseSegment, baseOffset );
		final int offset		= memory.getWord( baseSegment, baseOffset + 2 );
		
		// get the BIOS
		final IbmPcBIOS bios = system.getBIOS();
		
		// update the vector table entry
		bios.setInterruptVector( interruptNum, segment, offset );
	}
	
	/** 
	 * <pre>
	 * INT 21,26 - Create New Program Segment Prefix
	 * AH = 26h
	 * DX = segment address of new ~PSP~
	 * returns nothing
	 * - allocates memory for a PSP and copies current PSP there
	 * - used before DOS 2.x to spawn a child process
	 * - the application is responsible for allocating any memory
	 *   necessary for the child process
	 * - ~INT 21,4B~ (EXEC) is now the recommended method for starting
	 *   a child process, so this function should be avoided
	 * - see also ~INT 21,55~
	 * </pre>
	 */
	private void createNewProgramSegmentPrefix( final IbmPcSystem system,
			 									final Intel80x86 cpu ) {
		// TODO Implement me!
		throw new IllegalStateException( "Not yet implemented" );
	}
	
	/** 
	 * <pre>
	 * INT 21,2F - Get Disk Transfer Address (DTA)
	 * AH = 2F
	 * on return:
	 * ES:BX = pointer to current ~DTA~
	 * - returns the DTA address
	 * - the default DTA is a 128 byte block at address 80h in the
	 *   Program Segment Prefix (~PSP~). This area also contains the
	 *   command tail at program startup it must be saved or the DTA
	 *   must be relocated before use to preserve the command tail
	 * </pre>
	 * @param disk the given {@link MsDosStorageSystem storage device} instance
	 * @param cpu the given {@link Intel80x86 CPU} instance
	 * @see #setDiskTransferArea(MsDosStorageSystem, Intel80x86)
	 */
	private void getDiskTransferArea( final MsDosStorageSystem disk, final Intel80x86 cpu ) {
		// TODO Implement me!
		Logger.error( "getDiskTransferArea: not yet implemented\n" );
		cpu.AL.set( 0xFF );
	}
	
	/**
	 * Get System Date
	 * Return: CX = year (1980-2099), DH = month, DL = day, AL = day of week (00h=Sunday)
	 */
	private void getSystemDate( final Intel80x86 cpu ) {
		Calendar calendar = Calendar.getInstance();
		cpu.CX.set( calendar.get( Calendar.YEAR ) );
		cpu.DH.set( calendar.get( Calendar.MONTH ) );
		cpu.DL.set( calendar.get( Calendar.DAY_OF_MONTH ) );
		cpu.AL.set( calendar.get( Calendar.DAY_OF_WEEK ) );
	}
	
	/**
	 * Set System Date
	 * Entry: CX = year (1980-2099) DH = month DL = day
	 */
	private void setSystemDate( final Intel80x86 cpu ) {
		Calendar calendar = Calendar.getInstance();
		calendar.set( Calendar.YEAR, cpu.CX.get() );
		calendar.set( Calendar.MONTH, cpu.DH.get() );
		calendar.set( Calendar.DAY_OF_YEAR, cpu.CX.get() );		
	}
	
	/** 
	 * Get System Time
	 * Return: CH = hour CL = minute DH = second DL = 1/100 seconds
	 */
	private void getSystemTime( Intel80x86 cpu ) {
		Calendar calendar = Calendar.getInstance();
		cpu.CH.set( calendar.get( Calendar.HOUR ) );
		cpu.CL.set( calendar.get( Calendar.MINUTE ) );
		cpu.DH.set( calendar.get( Calendar.SECOND ) );
		cpu.DL.set( calendar.get( Calendar.MILLISECOND ) );
	}
	
	/** 
	 * Sets System Time
	 * Entry: CH = hour CL = minute DH = second DL = 1/100 seconds
	 */
	private void setSystemTime( Intel80x86 cpu ) {
		Calendar calendar = Calendar.getInstance();
		calendar.set( Calendar.HOUR, cpu.CH.get() );
		calendar.set( Calendar.MINUTE, cpu.CL.get() );
		calendar.set( Calendar.SECOND, cpu.DH.get() );
		calendar.set( Calendar.MILLISECOND, cpu.DL.get() );
	}

	/** 
	 * Get DOS Version
	 * Entry: AL = what to return in BH (00h OEM number, 01h version flag)
	 * Return:
	 * AL = major version number (00h if DOS 1.x)
	 * AH = minor version number
	 * BL:CX = 24-bit user serial number (most versions do not use this) if DOS <5 or AL=00h
	 * BH = MS-DOS OEM number if DOS 5+ and AL=01h
	 * BH = version flag bit 3: DOS is in ROM other: reserved (0)
	 */
	private void getDOSVersion( final Intel80x86 cpu ) {
		// AL = major version
		// AH = minor version
		cpu.AL.set( 3 );
		cpu.AH.set( 0 );
		
		// BL:CX = 24-bit user serial number
		cpu.BL.set( 0x3F );
		cpu.CX.set( 0x6F9F );
	}
	
	/** 
	 * <pre>
	 * INT 21,31 - Terminate Process and Remain Resident
	 * AH = 31h
	 * AL = exit code (returned to batch files)
	 * DX = memory size in paragraphs to reserve
	 * returns nothing
	 * - preferred method for Terminate and Stay Resident programs
	 * - terminates process without releasing allocated memory and
	 *   without closing open files
	 * - attempts allocation of memory specified in DX from memory
	 *   allocated by DOS at startup. ~INT 21,48~ memory allocation
	 *   is not affected
	 * - see ~INT 27~
	 * </pre>
	 * @param cpu the given {@link Intel80x86 CPU} instance
	 */
	private void terminateProcessAndRemainResident( final Intel80x86 cpu ) {
		// TODO figure out what to do
	}
	
	/**
	 * Get Free Disk Space
	 * Entry: DL = drive number (0=default, 1=A:, etc)
	 * Return:
	 *	AX = FFFFh if invalid drive
	 *	AX = sectors per cluster 
	 *	BX = number of free clusters 
	 *	CX = bytes per sector 
	 *	DX = total clusters on drive
	 */
	private void getFreeDiskSpace( final Intel80x86 cpu ) {
		cpu.AX.set( 0xFFFF );
	}
	
	/** 
	 * Gets the current working directory
	 * Entry:
	 *	DL = drive number (00h = default, 01h = A:, etc)
	 *	DS:SI -> 64-byte buffer for ASCIZ pathname
	 * Return:
	 *	CF clear if successful
	 *	CF set on error, AX = error code (0Fh)
	 */
	private void getCurrentWorkingDirectory( final MsDosStorageSystem disk, 
											 final IbmPcRandomAccessMemory memory, 
											 final Intel80x86 cpu ) {
		// get the current working directory
		File directory = disk.getCurrentdirectory();
		
		// write the directory to DS:SI
		setDosString( memory, cpu, directory.getAbsolutePath() );
	}
	
	/** 
	 * Changes the current working directory (chdir)
	 *	Entry: DS:DX -> ASCIZ pathname of directory to be removed
	 * @throws IbmPcException 
	 */
	private void chdir( final MsDosStorageSystem disk, 
						final IbmPcRandomAccessMemory memory, 
						final Intel80x86 cpu ) 
	throws IbmPcException {
		// get the filename
		final String filename = getASCIZString( cpu.DS.get(), cpu.DX.get(), memory );
		
		// change to the directory		
		File directory = new File( filename );		
		disk.setCurrentdirectory( directory );
	}
	
	/**
	 * "mkdir" Make Directory
	 * Entry: DS:DX -> ASCIZ pathname
	 * Return:
	 * 	CF clear if successful AX destroyed
	 *	CF set on error AX = error code (03h,05h)
	 */
	private void mkdir( final IbmPcRandomAccessMemory memory, final Intel80x86 cpu ) {
		// get the filename
		final String filename = getASCIZString( cpu.DS.get(), cpu.DX.get(), memory );
		
		// create the directory
		final File directory = new File( filename );
		final boolean success = directory.mkdir();
		cpu.FLAGS.setCF( !success );
		cpu.AX.set( !success ? 0x03 : 0x00 );		 
	}
	
	/** 
	 * Removes a directory (rmdir)
	 *	Entry: DS:DX -> ASCIZ pathname of directory to be removed
	 */
	private void rmdir( final IbmPcRandomAccessMemory memory, final Intel80x86 cpu ) {
		// get the filename
		final String filename = getASCIZString( cpu.DS.get(), cpu.DX.get(), memory );
		
		// remove the directory
		File directory = new File( filename );
		if( directory.isDirectory() )
			directory.delete();
	}
	
	/**
	 * "EXIT" - TERMINATE WITH RETURN CODE 
	 * Entry: AL = return code
	 * Return: never returns	 
	 */
	private void exitWithReturnCode( final Intel80x86 cpu ) {
		cpu.halt();
	}
	
	/** 
	 * Renames a file
	 * Entry:
	 *	DS:DX -> ASCIZ filename of existing file (no wildcards, but see below)
	 *	ES:DI -> ASCIZ new filename (no wildcards)
	 *	CL = attribute mask (server call only, see below)
	 * Return:
	 *	CF clear if successful
	 *	CF set on error, AX= error code (02h,03h,05h,11h)
	 * @param disk the given {@link MsDosStorageSystem storage device} instance
	 * @param memory the given {@link IbmPcRandomAccessMemory RAM} instance
	 * @param cpu the given {@link Intel80x86 CPU} instance
	 */
	private void renameFile( final MsDosStorageSystem disk, 
							 final IbmPcRandomAccessMemory memory, 
							 final Intel80x86 cpu ) {
		// get the source and destination file names
		final String srcFileName = getASCIZString( cpu.DS.get(), cpu.DX.get(), memory );
		final String dstFileName = getASCIZString( cpu.ES.get(), cpu.DI.get(), memory );
		
		// perform the rename
		final boolean renamed = disk.renameFile( srcFileName, dstFileName );
		
		// set CF on error
		cpu.FLAGS.setCF( !renamed );
	}
	
	/**
	 * <pre>
	 * Get/Set File Date and Time Using Handle
	 * AH 	= 57h
	 * AL 	= 00 get date and time
	 * 		= 01 set date and time
	 * 		= 02 ??? (DOS 4.0+ undocumented)
	 * 		= 03 ??? (DOS 4.0+ undocumented)
	 * 		= 04 ??? (DOS 4.0+ undocumented)
	 * BX 	= file handle
	 * CX 	= time to set (if setting)
	 * DX 	= date to set (if setting)
	 * ES:DI = pointer to buffer to contain results
	 * 
	 * On return:
	 * 	AX = error code if CF set (01h,06h / see ~DOS ERROR CODES~)
	 * 	CX = file time (if reading, see below)
	 * 	DX = file date (if reading, see below)
	 * 
	 *	CF clear if successful
	 *	CF set on error
	 * 
	 * % Time encoding:
	 * |F|E|D|C|B|A|9|8|7|6|5|4|3|2|1|0| Time in CX
	 * | | | | | | | | | | | +------------ two second incr (0-29)
	 * | | | | | +----------------------- minutes 0-59)
	 * +-------------------------------- hours (0-29)
	 * 
	 * % Date Encoding
	 * |F|E|D|C|B|A|9|8|7|6|5|4|3|2|1|0| Date in DX
	 * | | | | | | | | | | | +------------ day (1-31)
	 * | | | | | | | +------------------- month (1-12)
	 * +-------------------------------- year - 1980
	 * </pre>
	 */
	private void getOrSetFileLastModifiedDateTime( final Intel80x86 cpu ) {
		// TODO Must finish file handle code first
		cpu.FLAGS.setCF( true );
	}
	
	/** 
	 * <pre>
	 * INT 21,5B - Create File (DOS 3.0+)
	 * AH = 5B
	 * CX = attribute
	 * DS:DX = pointer to ASCIIZ path/filename
	 * on return:
	 * AX = handle if CF not set
	 *    = error code if CF set (see ~DOS ERROR CODES~)
	 * - standard method of opening files
	 * - returns a file handle of a file opened with specified
	 *   attributes (combinations of normal, system and hidden)
	 * </pre>
	 */
	private void createFile( final MsDosStorageSystem disk, 
			  				 final IbmPcRandomAccessMemory memory, 
			  				 final Intel80x86 cpu ) {
		// get the filename
		final String filename = getASCIZString( cpu.DS.get(), cpu.DX.get(), memory );
		
		// decode the file attributes
		final MsDosFileAttributes fileAttributes = MsDosFileAttributes.decode( cpu.CX.get() );
	
		// create the file
		try {
			disk.createFile( filename, fileAttributes );
			cpu.FLAGS.setCF( false );
		} 
		catch( final IbmPcException e ) {
			cpu.FLAGS.setCF( true );
		}
	}
	
	/**
	 * <pre>
	 * Open File Using Handle
	 * AH = 3D
	 * AL = open access mode
	 * 	00 read only
	 * 	01 write only
	 * 	02 read/write
	 * DS:DX = pointer to an ASCIIZ file name
	 * on return:
	 * AX = file handle if CF not set
	 * 	  = error code if CF set (see ~DOS ERROR CODES~)
	 * 
	 * % Access modes
	 * 7 6 5 4 3 2 1 0
	 * | | | | | | | | 
	 * | | | | | +-------- read/write/update access mode
	 * | | | | +--------- reserved, always 0
	 * | +-------------- sharing mode (see below) (DOS 3.1+)
	 * +--------------- 1 = private, 0 = inheritable (DOS 3.1+)
	 * 
	 * % Sharing mode bits (DOS 3.1+): Access mode bits:
	 * % 654 210
	 * 000 compatibility mode (exclusive) 000 read access
	 * 001 deny others read/write access 001 write access
	 * 010 deny others write access 010 read/write access
	 * 011 deny others read access
	 * 100 full access permitted to all
	 * - will open normal, hidden and system files
	 * - file pointer is placed at beginning of file
	 * </pre>
	 */
	private void openFileUsingHandle( final MsDosStorageSystem disk, 
									  final IbmPcRandomAccessMemory memory, 
									  final Intel80x86 cpu ) {
		// get the filename
		final String filename = getASCIZString( cpu.DS.get(), cpu.DX.get(), memory );
		
		// get the access mode
		final MsDosFileAccessMode accessMode = MsDosFileAccessMode.decode( cpu.AL.get() );
		
		Logger.info( "openFileUsingHandle: DS:DX = [%04X:%04X], accessMode = %s, file = '%s'\n", cpu.DS.get(), cpu.DX.get(), accessMode, filename );
		
		try {
			// open the file
			final MsDosFileHandle handle = disk.openDevice( filename, accessMode );
			
			// set AX = file handle and CF = 0
			cpu.AX.set( handle.getHandleID() );
			cpu.FLAGS.setCF( false );
		}
		catch( final IbmPcException e ) {
			// on error, set the error code in AX and CF = 1
			cpu.AX.set( 0x0001 );
			cpu.FLAGS.setCF( true );
		}
	}
	
	/**
	 * <pre>
	 * Read From File or Device Using Handle
	 * AH = 3F
	 * BX = file handle
	 * CX = number of bytes to read
	 * DS:DX = pointer to read buffer
	 * on return:
	 * 	AX = number of bytes read if CF not set
	 * 	= error code if CF set (see ~DOS ERROR CODES~)
	 * 	- read specified number of bytes from file into buffer DS:DX
	 * 	- when AX is not equal to CX then a partial read occurred due
	 * 		to end of file
	 * 	- if AX is zero, no data was read, and EOF occurred before read
	 * </pre>
	 */
	private void readFromFileOrDeviceUsingHandle( final MsDosStorageSystem disk, 
			  				   					  final IbmPcRandomAccessMemory memory, 
			  				   					  final Intel80x86 cpu ) {
		// get the number of bytes to read
		final int count = cpu.CX.get();
		
		// get the handle ID
		final int handleID = cpu.BX.get();
		
		// create a memory block
		final byte[] block = new byte[ count ];
		
		Logger.info( "readFromFileOrDeviceUsingHandle: count = %d, handle = %d\n", count, handleID );
		
		try {
			// read the block from the file
			final int bytesRead = disk.readFromDevice( handleID, block, count );
			
			Logger.info( "readFromFileOrDeviceUsingHandle: bytesRead = %d\n", bytesRead );
			
			// put the block into memory
			memory.setBytes( cpu.DS.get(), cpu.DX.get(), block, bytesRead );
			
			// put bytes read into AX and CF = 0
			cpu.AX.set( bytesRead );
			cpu.FLAGS.setCF( false );
		} 
		catch( final IbmPcException e ) {
			cpu.FLAGS.setCF( true );
		}		
	}
	
	/** 
	 * <pre>
	 * INT 21,3C - Create File Using Handle
	 * AH = 3C
	 * CX = file attribute (see ~FILE ATTRIBUTES~)
	 * DS:DX = pointer to ASCIIZ path name
	 * on return:
	 * 	CF = 0 if successful
	 * 	   = 1 if error
	 * 
	 * AX = files handle if successful
	 *    = error code if failure (see ~DOS ERROR CODES~)
	 *    - if file already exists, it is truncated to zero bytes on opening
	 * </pre>
	 */
	private void createFileUsingHandle( final MsDosStorageSystem disk, 
				  						final IbmPcRandomAccessMemory memory, 
				  						final Intel80x86 cpu ) {
		// get the filename
		final String filename = getASCIZString( cpu.DS.get(), cpu.DX.get(), memory );
		
		// decode the file attributes
		final MsDosFileAttributes fileAttributes = MsDosFileAttributes.decode( cpu.CX.get() );
		
		Logger.info( "createFileUsingHandle: fileAttrs = %s, filename = '%s'\n", fileAttributes, filename );

		try {
			// create the file
			final MsDosFileHandle handle = disk.createFile( filename, fileAttributes );
			
			// set the file handle
			cpu.AX.set( handle.getHandleID() );
			cpu.FLAGS.setCF( false );
		} 
		catch( final IbmPcException e ) {
			cpu.FLAGS.setCF( true );
		}
	}
	
	/**
	 * <pre>
	 * Close File Using Handle
	 * AH = 3E
	 * BX = file handle to close
	 * on return:
	 * 	AX = error code if CF set (see ~DOS ERROR CODES~)
	 * 	- if file is opened for update, file time and date stamp
	 * 		as well as file size are updated in the directory
	 * 	- handle is freed
	 * </pre>
	 * @throws IbmPcException 
	 */
	private void closeFileUsingHandle( final MsDosStorageSystem disk, 
									   final IbmPcRandomAccessMemory memory, 
									   final Intel80x86 cpu ) 
	throws IbmPcException {
		// get the handle ID
		final int handle = cpu.BX.get();
		
		// close the file
		final boolean closed = disk.closeDevice( handle );
		cpu.FLAGS.setCF( !closed );
	}
	
	/**
	 * <pre>
	 * INT 21,40 - Write To File or Device Using Handle
	 * AH = 40h
	 * BX = file handle
	 * CX = number of bytes to write, a zero value truncates/extends
	 * 		the file to the current file position
	 * DS:DX = pointer to write buffer
	 * on return:
	 * AX = number of bytes written if CF not set
	 * = error code if CF set (see ~DOS ERROR CODES~)
	 * - if AX is not equal to CX on return, a partial write occurred
	 * - this function can be used to truncate a file to the current
	 * 	 file position by writing zero bytes
	 * </pre>
	 */
	private void writeToFileOrDeviceUsingHandle( final MsDosStorageSystem disk, 
			   									 final IbmPcRandomAccessMemory memory, 
			   									 final Intel80x86 cpu ) 
	throws IbmPcException {
		// get the file handle
		final int fileHandle = cpu.BX.get();
		
		// get the data block at DS:[DX], count is in CX
		final byte[] datablock = memory.getBytes( cpu.DS.get(), cpu.DX.get(), cpu.CX.get() );
		
		// write the block to the file
		disk.writeToDevice( fileHandle, datablock );
	}
	
	/**
	 * <pre>
	 * INT 21,41 - Delete File
	 * AH = 41h
	 * DS:DX = pointer to an ASCIIZ filename
	 * on return:
	 * AX = error code if CF set (see DOS ERROR CODES)
	 * 	- marks first byte of file directory entry with E5 to indicate
	 * 		the file has been deleted. The rest of the directory entry
	 * 		stays intact until reused. ~FAT~ pointers are returned to DOS
	 *	- documented as not accepting wildcards in filename but actually
	 *		does in several DOS versions
	 * </pre>
	 */
	private void deleteFile( final MsDosStorageSystem disk, 
				 			 final IbmPcRandomAccessMemory memory, 
				 			 final Intel80x86 cpu ) 
	throws IbmPcException {
		// TODO Implement me!
		throw new IllegalStateException( "Not yet implemented" );
	}
	
	/**
	 * <pre>
	 * INT 21,42 - Move File Pointer Using Handle
	 * AH = 42h
	 * AL = origin of move:
	 * 		00 = beginning of file plus offset (SEEK_SET)
	 * 		01 = current location plus offset (SEEK_CUR)
	 * 		02 = end of file plus offset (SEEK_END)
	 * BX = file handle
	 * CX = high order word of number of bytes to move
	 * DX = low order word of number of bytes to move
	 * on return:
	 * AX = error code if CF set (see ~DOS ERROR CODES~)
	 * DX:AX = new pointer location if CF not set
	 * 	- seeks to specified location in file
	 *
	 */
	private void moveFilePointerUsingHandle( final MsDosStorageSystem disk, 
			 								 final IbmPcRandomAccessMemory memory, 
			 								 final Intel80x86 cpu ) {
		// TODO Implement me!
		throw new IllegalStateException( "Not yet implemented" );
	}
	
	/** 
	 * <pre>
	 * INT 21,43 - Get/Set File Attributes
	 * AH = 43h
	 * AL = 00 to get attribute
	 * 	  = 01 to set attribute
	 * DS:DX = pointer to an ASCIIZ path name
	 * CX = attribute to set
	 * 
	 * Valid file attributes
	 * 5 4 3 2 1 0
	 * | | | | | | 
	 * | | | | | +---- 1 = read only
	 * | | | | +----- 1 = hidden
	 * | | | +------ 1 = system
	 * | +--------- not used for this call
	 * +---------- 1 = archive
	 * on return:
	 * AX = error code if CF set (see ~DOS ERROR CODES~)
	 * CX = the attribute if AL was 00
	 * - see ~DIRECTORY~
	 * </pre>
	 */
	private void getSetFileAttributes( final MsDosStorageSystem disk, 
			 						   final IbmPcRandomAccessMemory memory, 
			 						   final Intel80x86 cpu ) {
		// get the filename
		final String filename = getASCIZString( cpu.DS.get(), cpu.DX.get(), memory );
		
		// get the file reference
		final File fileRef = new File( filename );
		
		// getting or setting the attribute?
		final boolean getAttrib = ( cpu.AL.get() == 0 );
		
		// get the file attributes
		if( getAttrib ) {
			// encode the file attributes
			int attributes = 0;
			if( fileRef.isHidden() ) { attributes |= 0x02; }	// 000010
			
			// put the file attributes into CX
			cpu.CX.set( attributes );
			cpu.FLAGS.setCF( false );
		}
		
		// set the file attributes
		else {
			// decode the attributes
			final MsDosFileAttributes attributes = MsDosFileAttributes.decode( cpu.CX.get() );
			
			// set the file attributes
			if( attributes.isArchive() ) { }
			if( attributes.isSystem() ) { }
			if( attributes.isHidden() ) { }
			if( attributes.isReadOnly() ) { fileRef.setReadOnly(); }
			
			// unset CF
			cpu.FLAGS.setCF( false );
		}
	}
	
	/**
	 * <pre>
	 * INT 21,44 - I/O Control for Devices (IOCTL)
	 * % Standard Call Format
	 * AH = 44h
	 * AL = function value
	 * BX = file handle
	 * BL = logical device number (0=default, 1=A:, 2=B:, 3=C:, ...)
	 * CX = number of bytes to read or write
	 * DS:DX = data or buffer
	 * on return:
	 * AX = error code if CF set
	 * AX = # of bytes transferred if CF not set
	 * 
	 * % For more information, see the following topics:
	 * ~IOCTL,0~ Get Device Information
	 * ~IOCTL,1~ Set Device Information
	 * ~IOCTL,2~ Read From Character Device
	 * ~IOCTL,3~ Write to Character Device
	 * ~IOCTL,4~ Read From Block Device
	 * ~IOCTL,5~ Write to Block Device
	 * ~IOCTL,6~ Get Input Status
	 * ~IOCTL,7~ Get Output Status
	 * ~IOCTL,8~ Device Removable Query
	 * ~IOCTL,9~ Device Local or Remote Query
	 * ~IOCTL,A~ Handle Local or Remote Query
	 * ~IOCTL,B~ Set Sharing Retry Count
	 * ~IOCTL,C~ Generic I/O for Handles
	 * ~IOCTL,D~ Generic I/O for Block Devices (3.2+)
	 * ~IOCTL,E~ Get Logical Drive (3.2+)
	 * ~IOCTL,F~ Set Logical Drive (3.2+)
	 * 
	 * - see: ~DEVICE COMMAND CODES~
	 * ~DEVICE REQUEST HEADER~
	 * ~DEVICE STATUS~
	 * ~DEVICE HEADER~
	 * ~DEVICE ATTRIBUTES~
	 */
	private void inputOutputControlForDevices( final MsDosStorageSystem disk, 
			   								   final IbmPcRandomAccessMemory memory, 
			   								   final Intel80x86 cpu ) {
		// get sub-function
		final int subCode = cpu.AL.get();
		switch( subCode ) {
			case 0x00: ioctrl_getDeviceInformation( disk, memory, cpu ); break;
			// TODO Implement me!
			default:
				throw new IllegalArgumentException( String.format( "Invalid sub-function code (code = %02X)", subCode ) );
		}
	}
	
	/**
	 * INT 21,44,0 / IOCTL,0 - Get Device Information
	 * AH = 44h
	 * AL = 00
	 * BX = handle (must be an opened device)
	 * on return
	 * AX = error code if CF set (see ~DOS ERROR CODES~)
	 * DX = device information (see tables below)
	 * 
	 * DX Block Device Information
	 * F E D C B A-8 7 6 5-0
	 * | | | | | |   | | |    
	 * | | | | | | | | +----- drive number (0=A:,1=B:)
	 * | | | | | | | +------- 0 = file has been written
	 * | | | | | | +-------- 0 = disk file; 1 = character device
	 * | | | | | +---------- reserved, must be zero
	 * | | | | +------------ 1 = media not removable
	 * | | | +------------- 1 = network device (DOS 3.x+)
	 * | | +-------------- 1 = reserved
	 * | +--------------- 1 = don't update file time or date (DOS 4.x+)
	 * +---------------- 1 = file is remote (DOS 3.x+)
	 * 
	 * DX Character Device Information
	 * F E D C B A-8 7 6 5 4 3 2 1 0
	 * | | | | | |   | | | | | | | | 
	 * | | | | | | | | | | | | | +---- 1 = standard input device
	 * | | | | | | | | | | | | +---- 1 = standard output device
	 * | | | | | | | | | | | +---- 1 = NUL device
	 * | | | | | | | | | | +---- 1 = clock device
	 * | | | | | | | | | +---- uses DOS ~INT 29~ for fast character output
	 * | | | | | | | | +---- 1 = binary mode, 0 = translated
	 * | | | | | | | +---- 0 = end of file on input
	 * | | | | | | +---- 1 = character device, 0 if disk file
	 * | | | | | +----- reserved
	 * | | | | +------ 1 = media not removable
	 * | | | +------ 1 = network device (DOS 3.x+)
	 * | | +------ reserved
	 * | +------ 1 = supports IOCTL, via functions 2 & 3
	 * +------ reserved
	 * 
	 * - BIT 7 of register DX can be used to detect if STDIN/STDOUT is
	 * 	 redirected to/from disk; if a call to this function has DX BIT 7
	 * 	 set it's not redirected from/to disk; if it's clear then it is
	 * 	 redirected to/from disk
	 * - BIT B of register DX can be used to determine if a drive is
	 *   removable.
	 */
	private void ioctrl_getDeviceInformation( final MsDosStorageSystem disk, 
			   								  final IbmPcRandomAccessMemory memory, 
			   								  final Intel80x86 cpu ) {
		// TODO Implement me!
		throw new IllegalStateException( "Not yet implemented" );
	}
	
	/** 
	 * <pre>
	 * EXEC/Load and Execute Program
	 * AH 	= 4B
	 * AL 	= 00 to load and execute program
	 *		= 01 (Undocumented) create program segment prefix and load
	 *			program, but don't execute. The CS:IP and SS:SP of the
	 *			program is placed in parameter block. Used by debuggers
	 * 		= 03 load program only
	 * 		= 04 called by MSC spawn() when P_NOWAIT is specified
	 * DS:DX = pointer to an ASCIIZ filename
	 * ES:BX = pointer to a parameter block
	 * on return:
	 * 	AX = error code if CF set (see ~DOS ERROR CODES~)
	 * 	ES:BX = when AL=1, pointer to parameter block similar to:
	 * 		% 	Offset Size Description
	 * 		00	word when AL=1, segment of env. or zero if using parents env.
	 * 			word when AL=3, segment of load point for overlay
	 * 		02 	dword when AL=1, pointer to cmd line at PSP 80h
	 * 			word when AL=3, relocation factor for EXE overlay
	 * 		06 	dword pointer to default {@link MsDosFileControlBlock FCB} passed at ~PSP~ 5Ch
	 * 		0A 	dword pointer to default FCB passes at PSP 6Ch
	 * 		0E 	dword value of program SS:SP
	 * 		12 	dword value of program CS:IP
	 * 			- allows execution of an external program as well as overlay
	 * 				management from within an application
	 * 			- all registers except CS and IP are destroyed
	 * 			- SS and SP should be preserved in code segment before call
	 * 				since a bug in DOS version 2.x destroys these
	 * 			- return code can be retrieved if child process exits via ~INT 21,4C~
	 * 			- calling process must assure presence of enough unallocated memory
	 * 			- subfunction 4 returns with an error under DOS 4.x+
	 * 			- calls ~INT 21,55~
	 * 			- see also ~INT 21,26~
	 * </pre>
	 */
	private void loadAndExecuteProgram( final MsDosStorageSystem disk, 
			   							final IbmPcRandomAccessMemory memory, 
			   							final Intel80x86 cpu ) 
	throws IbmPcException {		
		try {
			// get the filename
			final String filename = getASCIZString( cpu.DS.get(), cpu.DX.get(), memory );
			
			// read the contents of the file			
			final byte[] x86Code = getFileContents( new File( filename ) );
			
			// get the parameter address
			final int paramSeg = cpu.ES.get();
			final int paramOfs = cpu.BX.get();
			
			// get the parameters
			final int stckSeg = memory.getWord( paramSeg, paramOfs + 0x0E );
			final int stckOfs = memory.getWord( paramSeg, paramOfs + 0x10 );
			final int codeSeg = memory.getWord( paramSeg, paramOfs + 0x12 );
			final int codeOfs = memory.getWord( paramSeg, paramOfs + 0x14 );
			
			// copy the x86 code to memory
			memory.setBytes( codeSeg, codeOfs, x86Code, x86Code.length );
			
			// point to the new stack (SS:SP)
			cpu.SS.set( stckSeg );
			cpu.SP.set( stckOfs );		
			
			// point to the new code (CS:IP)
			cpu.CS.set( codeSeg );
			cpu.IP.set( codeOfs );
		} 
		catch( final IOException e ) {
			throw new IbmPcException( e );
		}
	}
	
	/** 
	 * INT 21,67 - Set Handle Count (DOS 3.3+)
	 * AH = 67h
	 * BX = new maximum open handles allowed
	 * on return
	 * CF = 0 if successful
	 * 	  = 1 if error
	 * AX = error code if CF is set (see ~DOS ERROR CODES~)
	 * 	- gives program control of the number of files simultaneously open
	 * 	- if CX is less than the current number of open files the change
	 * 	  will take effect when the number of open handles falls below
	 *    the new limit
	 *  - this function allows the application to use more than 20 files,
	 *    up to the FILES=N limit
	 *  - earlier copies of IBM DOS 3.3 sometimes incorrectly allocates
	 *    memory (up to 64K) if an even number of handles is requested
	 *  - only the first 20 files handles are copied to a child process
	 *    regardless of the max number of files
	 *  - it is possible to allocate more than 255 file handles but it is
	 *    difficult to use more than 255
	 *  - see ~SFT~ ~FILE HANDLES~
	 */
	private void setHandleCount( final MsDosStorageSystem disk, 
								 final IbmPcRandomAccessMemory memory, 
								 final Intel80x86 cpu ) {
		// TODO Implement me!
		throw new IllegalStateException( "Not yet implemented" );
	}
		
	/**
	 * Reads the executable file from the disk
	 * @param file the given {@link File executable file}
	 * @return the x86 machine code
	 * @throws IOException
	 */
	private static byte[] getFileContents( final File file ) 
	throws IOException {
		final byte[] buffer = new byte[(int)file.length()];
		FileInputStream fis = new FileInputStream( file );
		fis.read( buffer );
		return buffer;
	}

}
