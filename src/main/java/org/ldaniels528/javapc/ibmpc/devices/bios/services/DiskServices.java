package org.ldaniels528.javapc.ibmpc.devices.bios.services;

import org.ldaniels528.javapc.ibmpc.devices.cpu.I8086;
import org.ldaniels528.javapc.ibmpc.exceptions.X86AssemblyException;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

/**
 * BIOS Disk Services (Interrupt 13h)
 * @author lawrence.daniels@gmail.com
 */
public class DiskServices implements InterruptHandler {
	private static final DiskServices instance = new DiskServices();
	
	/**
	 * Private constructor
	 */
	private DiskServices() {
		super();
	}
	
	/**
	 * Returns the singleton instance of this class 
	 * @return the singleton instance of this class 
	 */
	public static DiskServices getInstance() {
		return instance;
	}
	
	/**
	 * Process the Disk Services Interrupt (INT 13h)
	 * @throws X86AssemblyException
	 */
	public void process(IbmPcSystem system, final I8086 cpu)
	throws X86AssemblyException {
		// determine what to do
		switch( cpu.AH.get() ) {
			case 0x00: resetDiskSystem( cpu ); break;
			case 0x01: getDiskStatus( cpu ); break;
			case 0x02: readDiskSectors( cpu ); break;
			case 0x03: writeDiskSectors( cpu ); break;
			case 0x04: verifySectors( cpu ); break;
			case 0x05: formatTrack( cpu ); break;
			case 0x06: formatTrackAndSetBadSectors( cpu ); break;
			case 0x07: formatDriveStartingAtSpecifiedTrack( cpu ); break;
			case 0x08: getCurrentDriveAndParameters( cpu ); break;
			case 0x0A: readLongSector( cpu ); break;
			case 0x0B: writeLongSectors( cpu ); break;
			case 0x0C: seekToCylinder( cpu ); break;
			case 0x0D: alternateDiskReset( cpu ); break;
			case 0x0E: readSectorBuffer( cpu ); break;
			case 0x0F: writeSectorBuffer( cpu ); break;
			case 0x10: testForDriveReady( cpu ); break;
			case 0x11: recalibrateDrive( cpu ); break;
			case 0x12: controllerRamDiagnostic( cpu ); break;
			case 0x13: driveDiagnostic( cpu ); break;
			case 0x14: controllerInternalDiagnostic( cpu ); break;
			case 0x15: readDasdType( cpu ); break;
			case 0x16: ChangeOfDiskStatus( cpu ); break;
			case 0x17: setDasdType( cpu ); break;
			case 0x18: setMediaTypeForFormat( cpu ); break;
			case 0x19: parkFixedDiskHeads( cpu ); break;
			case 0x1A: formatUnit( cpu ); break;
			default:
				throw new X86AssemblyException( "Invalid call (" + cpu.AH + ")" );
		}
	}
	
	/** 
	 * Reset Disk System
	 * Parameters:
	 * 	AH = 0
	 * 	DL = drive number (0=A:, 1=2nd floppy, 80h=drive 0, 81h=drive 1)
	 * Returns:
	 *	AH = disk operation status (see ~INT 13,STATUS~)
	 *	CF 	= 0 if successful
	 *		= 1 if error
	 */
	private void resetDiskSystem( I8086 cpu ) {
		cpu.AH.set( 0 );
		cpu.FLAGS.setCF( false );
	}
	
	/**
	 * <pre>
	 * Get Disk Status
	 * Parameters:
	 * 	AH = 1
	 * Returns:
	 *	AL = status:
	 *		00 no error
	 *		01 bad command passed to driver
	 *		02 address mark not found or bad sector
	 *		03 diskette write protect error
	 *		04 sector not found
	 *		05 fixed disk reset failed
	 *		06 diskette changed or removed
	 *		07 bad fixed disk parameter table
	 *		08 DMA overrun
	 *		09 DMA access across 64k boundary
	 *		0A bad fixed disk sector flag
	 *		0B bad fixed disk cylinder
	 *		0C unsupported track/invalid media
	 *		0D invalid number of sectors on fixed disk format
	 *		0E fixed disk controlled data address mark detected
	 *		0F fixed disk DMA arbitration level out of range
	 *		10 ECC/CRC error on disk read
	 *		11 recoverable fixed disk data error, data fixed by ECC
	 *		20 controller error (NEC for floppies)
	 *		40 seek failure
	 *		80 time out, drive not ready
	 *		AA fixed disk drive not ready
	 *		BB fixed disk undefined error
	 *		CC fixed disk write fault on selected drive
	 *		E0 fixed disk status error/Error reg = 0
	 *		FF sense operation failed
	 * </pre>
	 */
	private void getDiskStatus( I8086 cpu ) {
		cpu.AL.set( 0x00 );
	}
	
	/**
	 * Read Disk Sectors
	 * Parameters:
	 * 	AH = 02
	 *	AL = number of sectors to read (1-128 dec.)
	 *	CH = track/cylinder number (0-1023 dec., see below)
	 *	CL = sector number (1-17 dec.)
	 *	CX = CH:CL
	 *		|F|E|D|C|B|A|9|8|7|6|5-0|
	 *		| | | | | | | | | | +----- sector number
	 *		| | | | | | | | +--------- high order 2 bits of track/cylinder
	 *		+------------------------ low order 8 bits of track/cyl number
	 *	DH = head number (0-15 dec.)
	 *	DL = drive number (0=A:, 1=2nd floppy, 80h=drive 0, 81h=drive 1)
	 *	ES:BX = pointer to buffer
	 * Returns:
	 * 	AH = status (see ~INT 13,STATUS~)
	 *	AL 	= number of sectors read
	 *	CF 	= 0 if successful
	 *		= 1 if error
	 */
	private void readDiskSectors( I8086 cpu ) {
		cpu.AH.set( 0 );
		cpu.AL.set( 1 );
		cpu.FLAGS.setCF( false );
	}

	/**
	 * Write Disk Sectors
	 * Parameters: 
	 * 	AH = 03
	 * 	AL = number of sectors to write (1-128 dec.)
	 * 	CH = track/cylinder number (0-1023 dec.)
	 * 	CL = sector number (1-17 dec., see below)
	 * 	CX = CH:CL
	 * 		|F|E|D|C|B|A|9|8|7|6|5-0| 
	 *		| | | | | | | | | | +----- sector number
	 *		| | | | | | | | +--------- high order 2 bits of track/cylinder
	 *		+------------------------ low order 8 bits of track/cyl number 
	 * 	DH = head number (0-15 dec.)
	 * 	DL = drive number (0=A:, 1=2nd floppy, 80h=drive 0, 81h=drive 1)
	 * 	ES:BX = pointer to buffer
	 * Returns:
	 * 	AH = 0 if CF=0; otherwise disk status (see ~INT 13,STATUS~)
	 * 	AL = number of sectors written
	 * 	CF 	= 0 if successful
	 * 		= 1 if error
	 */
	private void writeDiskSectors( I8086 cpu ) {
		cpu.AH.set( 0 );
		cpu.AL.set( 1 );
		cpu.FLAGS.setCF( false );
	}
	
	/**
	 * Verify Disk Sectors
	 * Parameters:
	 * 	AH = 04
	 * 	AL = number of sectors to verify (1-128 dec.)
	 * 	CH = track/cylinder number (0-1023 dec., see below)
	 * 	CL = sector number (1-17 dec.)
	 * 	CX = CH:CL
	 * 		|F|E|D|C|B|A|9|8|7|6|5-0| 
	 *		| | | | | | | | | | +----- sector number
	 *		| | | | | | | | +--------- high order 2 bits of track/cylinder
	 *		+------------------------ low order 8 bits of track/cyl number 	 
	 * 	DH = head number (0-15 dec.)
	 * 	DL = drive number (0=A:, 1=2nd floppy, 80h=drive 0, 81h=drive 1)
	 * 	ES:BX = pointer to buffer
	 * Returns:
	 * 	AH = status (see ~INT 13,STATUS~)
	 * 	AL = number of sectors verified
	 * 	CF 	= 0 if successful
	 * 		= 1 if error
	 */
	private void verifySectors( I8086 cpu ) {
		cpu.AH.set( 0 );
		cpu.AL.set( 1 );
		cpu.FLAGS.setCF( false );
	}
	
	/** 
	 * Format Disk Track
	 * Parameters
	 * 	AH = 05
	 * 	AL = interleave value (XT only)
	 * 	CX = track/cylinder number (see below for format)
	 *		|F|E|D|C|B|A|9|8|7|6|5-0|
	 *		| | | | | | | | | | +----- sector number
	 *		| | | | | | | | +--------- high order 2 bits of track/cylinder
	 *		+------------------------ low order 8 bits of track/cyl number
	 * 	DH = head number (0-15 dec.)
	 * 	DL = drive number (0=A:, 1=2nd floppy, 80h=drive 0, 81h=drive 1)
	 * 	ES:BX = pointer to block of "track address fields" containing
	 * 	four byte fields for each sector to be formatted of the form:
	 * 	1 byte track number
	 * 	1 byte head number Size #
	 * 	1 byte sector number Codes Bytes
	 * 	1 byte sector size code 0 128
	 * 	1 256
	 * 	2 512
	 * 	3 1024
	 * Returns:
	 * 	AH = status (see ~INT 13,STATUS~)
	 * 	CF 	= 0 if successful
	 * 		= 1 if error
	 */
	private void formatTrack( I8086 cpu ) {
		cpu.AH.set( 0 );
		cpu.FLAGS.setCF( false );
	}
	
	/** 
	 * Format Track and Set Bad Sector Flags (XT & portable)
	 * Parameters:
	 * 	AH = 06
	 * 	AL = Interleave value (XT only)
	 * 	BX = format buffer, size = 512 bytes; the first
	 * 		2*(sectors/track) bytes contain F,N for each sector
	 * 		F = 00h for good sector,
	 * 		F = 80h for bad sector
	 * 		N = sector number
	 * Returns:
	 * 	AH = status (see ~INT 13,STATUS~)
	 * 	CF 	= 0 if successful
	 * 		= 1 if error
	 *
	 */
	private void formatTrackAndSetBadSectors( I8086 cpu ) {
		cpu.AH.set( 0 );
		cpu.FLAGS.setCF( false );
	}
	
	/** 
	 * Format Drive Starting at Specified Track (XT & portable)
	 * Parameters:
	 *	AH = 07
	 *	AL = interleave value (XT only)
	 *	BX = format buffer, size = 512 bytes; the first 2*(sectors/track)
	 *		bytes contain F, N for each sector where:
	 *		F = 00h for good sector
	 *		F = 80h for bad sector
	 *		N = sector number
	 * Returns:
	 *	AH = status (see ~INT 13,STATUS~)
	 *	CF 	= 0 if successful
	 *		= 1 if error
	 */
	private void formatDriveStartingAtSpecifiedTrack( I8086 cpu ) {
		cpu.AH.set( 0 );
		cpu.FLAGS.setCF( false );
	}
	
	/** 
	 * Get Current Drive Parameters (XT & newer)
	 * Parameters:
	 *	AH = 08
	 *	DL = drive number (0=A:, 1=2nd floppy, 80h=drive 0, 81h=drive 1)
	 * Returns:
	 *	AH = status (see ~INT 13,STATUS~)
	 *	BL = CMOS drive type
	 *	01 - 5| 360K 03 - 3| 720K
	 *	02 - 5| 1.2Mb 04 - 3| 1.44Mb
	 *	CH = cylinders (0-1023 dec. see below)
	 *	CL = sectors per track (see below)
	 *	DH = number of sides (0 based)
	 *	DL = number of drives attached
	 *	ES:DI = pointer to 11 byte ~Disk Base Table~ (DBT)
	 *	CF 	= 0 if successful
	 *		= 1 if error
	 */
	private void getCurrentDriveAndParameters( I8086 cpu ) {
		cpu.FLAGS.setCF( false );
	}
	
	/** 
	 * Read Long Sector (XT & newer)
	 * Parameters:
	 * 	AH = 0A
	 * 	AL = number of sectors (1-121 dec.)
	 * 	CH = track number (0-1023 dec., see below)
	 * 	CL = sector number (1-17 dec., see below)
	 * 	DH = head number (0-15 dec.)
	 * 	DL = fixed drive number (80h=drive 0, 81h=drive 1)
	 * 	ES:BX = address of buffer
	 * Returns:
	 * 	AH = status (see ~INT 13,STATUS~)
	 * 	AL = number of sectors actually transferred
	 * 	CF 	= 0 if successful
	 * 		= 1 if error
	 */
	private void readLongSector( I8086 cpu ) {
		cpu.AH.set( 0 );
		cpu.FLAGS.setCF( false );
	}
	
	/**
	 * Write Long Sectors (XT & newer)
	 * Parameters:
	 * 	AH = 0B
	 * 	AL = number of sectors (1-121 dec.)
	 * 	CH = track number (0-1023 dec., see below)
	 * 	CL = sector number (1-17 dec., see below)
	 * 	DH = head number (0-15 dec.)
	 * 	DL = fixed drive number (80h=drive 0, 81h=drive 1)
	 * 	ES:BX = address of buffer
	 * Returns:
	 * 	AL = number of sectors actually transferred
	 * 	AH = status (see ~INT 13,STATUS~)
	 * 	CF 	= 0 if successful
	 * 		= 1 if error
	 */
	private void writeLongSectors( I8086 cpu ) {
		cpu.AH.set( 0 );
		cpu.FLAGS.setCF( false );
	}
	
	/**
	 * Seek to Cylinder (XT & newer)
	 * Parameters:
	 *	AH = 0C
	 *	CH = low order byte of cylinder number (see below)
	 *	CL = high order byte of cylinder number (see below)
	 *	DH = head number (0-15)
	 *	DL = fixed drive number (80h=drive 0, 81h=drive 1)
	 * Returns:
	 *	AH = status (see ~INT 13,STATUS~)
	 *	CF 	= 0 if successful
	 *		= 1 if error
	 */
	private void seekToCylinder( I8086 cpu ) {
		cpu.AH.set( 0 );
		cpu.FLAGS.setCF( false );
	}
	
	/** 
	 * Alternate Disk Reset (XT & newer)
	 * <pre>
	 * 1 Many good programming references indicate this function is only
	 *	available on the AT, PS/2 and later systems, but all hard disk
	 *	systems since the XT have this function available
	 * 2 Used to force drive recalibration similar to ~INT 13,0~
	 * 3 Drive heads are positioned at track zero
	 * </pre>
	 * Parameters:
	 *	AH = 0D
	 *	DL = fixed drive number (80h=drive 0, 81h=drive 1)
	 * Returns:
	 *	AH = status (see ~INT 13,STATUS~)
	 *	CF 	= 0 if successful
	 *		= 1 if error
	 */
	private void alternateDiskReset( I8086 cpu ) {
		cpu.AH.set( 0 );
		cpu.FLAGS.setCF( false );
	}
	
	/**
	 * Read Sector Buffer (XT & portable only)
	 * Parameters:
	 * 	AH = 0E
	 * Returns:
	 * 	AL = number of sectors actually transferred
	 * 	AH = status (see ~INT 13,STATUS~)
	 * 	CF 	= 0 if successful
	 * 		= 1 if error
	 */
	private void readSectorBuffer( I8086 cpu ) {
		cpu.AH.set( 0 );
		cpu.FLAGS.setCF( false );
	}
	
	/** 
	 * Write Sector Buffer (XT & portable only)
	 * Parameters:
	 *	AH = 0F
	 * Returns:
	 *	AL = number of sectors actually transferred
	 *	AH = status (see ~INT 13,STATUS~)
	 *	CF 	= 0 if successful
	 *		= 1 if error
	 */
	private void writeSectorBuffer( I8086 cpu ) {
		// TODO finish this method
	}
	
	/**
	 * Test for Drive Ready (XT & newer)
	 * <pre>
	 * 	1.	Many good programming references indicate this function is only
	 * 		available on the AT, PS/2 and later systems, but all hard disk
	 * 		systems since the XT have this function available
	 * </pre> 
	 * Parameters:
	 * 	AH = 10h
	 * 	DL = drive number (0=A:, 1=2nd floppy, 80h=drive 0, 81h=drive 1)
	 * Returns:
	 * 	AH = status (see ~INT 13,STATUS~)
	 * 	CF 	= 0 if successful
	 * 		= 1 if error
	 */
	private void testForDriveReady( I8086 cpu ) {
		// TODO finish this method
	}
	
	/**
	 * Recalibrate Drive (XT & newer)
	 * <pre>
	 *	1.	many good programming references indicate this function is only
	 *		available on the AT, PS/2 and later systems, but all hard disk
	 *		systems since the XT have this function available
	 * </pre>
	 * Parameters:
	 * 	AH = 11h
	 * 	DL = drive number (0=A:, 1=2nd floppy, 80h=drive 0, 81h=drive 1)
	 * Returns:
	 *	AH = status (see ~INT 13,STATUS~)
	 *	CF 	= 0 if successful
	 *		= 1 if error
	 */
	private void recalibrateDrive( I8086 cpu ) {
		// TODO finish this method
	}
	
	/**
	 * Controller RAM Diagnostic (XT & portable only)
	 * <pre>
	 * Parameters:
	 *	AH = 12h
	 * Returns:
	 * 	AH = status ({@link #getDiskStatus() see Status})
	 * 	CF 	= 0 if successful
	 * 		= 1 if error
	 * </pre>
	 */
	private void controllerRamDiagnostic( I8086 cpu ) {
		// TODO finish this method
	}
	
	/**
	 * Drive Diagnostic (XT & portable only)
	 * Parameters:
	 *	AH = 13h
	 * Returns:
	 *	AH = status (see ~INT 13,STATUS~)
	 *	CF 	= 0 if successful
	 *		= 1 if error
	 */
	private void driveDiagnostic( I8086 cpu ) {
		// TODO finish this method
	}
	
	/** 
	 * Controller Internal Diagnostic (XT & newer)
	 * <pre>
	 * 	1.	Many good programming references indicate this function is only
	 *		available on the AT, PS/2 and later systems, but all hard disk
	 *		systems since the XT have this function available
	 *	2.	Not valid on PS/2 model 30
	 * Parameters:
	 *	AH = 14h
	 * Returns:
	 *	AH = status (see ~INT 13,STATUS~)
	 *	CF 	= 0 if successful
	 *		= 1 if error
	 * </pre>
	 */
	private void controllerInternalDiagnostic( I8086 cpu ) {
		// TODO finish this method
	}
	
	/**
	 * Read DASD Type (XT BIOS from 1/10/86 & newer)
	 * <pre>
	 *	1.	XT's must have a BIOS date 1/10/86 or newer
	 *	2.	Used to determine if ~INT 13,16~ can detect disk change
	 *	3.	See ~INT 13,STATUS~
	 * Parameters:
	 *	AH = 15h
	 *	DL = drive number (0=A:, 1=2nd floppy, 80h=drive 0, 81h=drive 1)
	 * Returns:
	 *	AH 	= 00 drive not present
	 *		= 01 diskette, no change detection present
	 *		= 02 diskette, change detection present
	 *		= 03 fixed disk present
	 *	CX:DX = number of fixed disk sectors; if 3 is returned in AH
	 *	CF 	= 0 if successful
	 *		= 1 if error
	 * </pre>
	 */
	private void readDasdType( I8086 cpu ) {
		// TODO finish this method
	}
	
	/**
	 * Change of Disk Status (XT BIOS from 1/10/86 & newer)
	 * <pre>
	 *	1.	Used to detect if a disk change has occurred
	 *	2.	See ~INT 13,STATUS~ ~INT 13,15~
	 * Parameters:
	 *	AH = 16h
	 *	DL = drive number (0=A:, 1=2nd floppy, 80h=drive 0, 81h=drive 1)
	 * Returns:
	 *	AH 	= 00 no disk change
	 *		= 01 disk changed
	 *	CF = set if disk has been removed or an error occurred
	 * </pre>
	 */
	private void ChangeOfDiskStatus( I8086 cpu ) {
		// TODO finish this method
	}
	
	/**
	 * <pre>
	 * Set DASD Type for Format (XT BIOS from 1/10/86 & newer)
	 * 	1.	Only the disk number is checked for validity
	 *	2.	Tells BIOS format routine about the disk type
	 * Parameters:
	 *	AH 	= 17h
	 *	AL	= 00 no disk
	 *		= 01 320k/360k diskette in 320k/360k drive
	 *		= 02 320k/360k diskette in 1.2Mb drive
	 *		= 03 1.2Mb diskette in 1.2Mb drive
	 *		= 04 720k diskette in 720k drive (BIOS 6/10/85 & newer)
	 *			720K diskette in 1.44Mb drive (PS/2)
	 *			1.44Mb diskette in 1.44Mb drive (PS/2)
	 *	DL 	= drive number (0=A:, 1=2nd floppy, 80h=drive 0, 81h=drive 1)
	 * </pre>
	 */
	private void setDasdType( I8086 cpu ) {
		// TODO finish this method
	}
	
	/**
	 * <pre>
	 * Set Media Type for Format (BIOS date specific)
	 *	AH = 18h
	 *	CH = lower 8 bits of number of tracks (0-1023 dec., see below)
	 *	CL = sectors per track (1-17 dec., see below)
	 *	DL = drive number (0=A:, 1=2nd floppy, 80h=drive 0, 81h=drive 1)
	 * Returns:
	 *	ES:DI = pointer to 11-byte ~Disk Base Table~ (DBT)
	 *	AH	= 00h if requested combination supported
	 *		= 01h if function not available
	 *		= 0Ch if not supported or drive type unknown
	 *		= 80h if there is no media in the drive
	 *	CF 	= 0 if successful
	 *		= 1 if error
	 * </pre>
	 */
	private void setMediaTypeForFormat( I8086 cpu ) {
		// TODO finish this method
	}
	
	/** 
	 * Park Fixed Disk Heads (AT & newer)
	 * 	Available only on AT, XT 283 and PS/2 machines
	 * Parameters:
	 *	AH = 19h
	 *	DL = drive number (0=A:, 1=2nd floppy, 80h=drive 0, 81h=drive 1)
	 * Returns:
	 *	AH = status (see ~INT 13,STATUS~)
	 *	CF 	= 0 if successful
	 *		= 1 if error
	 */
	private void parkFixedDiskHeads( I8086 cpu ) {
		// TODO finish this method
	}
	
	/** 
	 * Format Unit (PS/2 model 50+)
	 * 	Only the disk number is checked for validity
	 * Parameters:
	 *	AH = 1Ah
	 *	AL = defect table count
	 *	DL = drive number (0=A:, 1=2nd floppy, 80h=drive 0, 81h=drive 1)
	 *	ES:BX = far pointer to defect table
	 *	CL = modifier bits
	 *		|7|6|5|4|3|2|1|0| Format Unit Modifier Bits
	 *		| | | | | | | +---- 1=ignore primary defect map, 0=use map
	 *		| | | | | | +----- 1=ignore secondary defect map, 0=use map
	 *		| | | | | +------ 1=update secondary defect map, 0=don't
	 *		| | | | +------- 1=do extended surface analysis, 0=don't
	 *		| | | +-------- 1=periodic interrupt status on, 0=off
	 *		+------------- reserved
	 */
	private void formatUnit( I8086 cpu ) {
		// TODO finish this method
	}
	
}
