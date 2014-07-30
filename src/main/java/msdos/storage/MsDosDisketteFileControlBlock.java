package msdos.storage;

import ibmpc.devices.memory.MemoryObject;

/**
 * MSDOS Diskette File Control Block
 * <pre>
 * 	Offsets to FCB Information
 *  ------------------------------------------------------------------------------
 *	Offset	Length	Name		Description
 *	0		1		Mode		The mode in which the file was opened:
 *					1 			Input only
 *					2 			Output only
 *					4 			Random I/O
 *					16 			Append only
 *					32 			Internal use
 *					64 			Future use
 *					128 		Internal use
 *	1		38		FCB			Diskette file control block.
 *	39		2		CURLOC		Number of sectors read or written for sequential access. The last record number +1 read or written for random files.
 *	41		1		ORNOFS		Number of bytes in sector when read or written.
 *	42		1		NMLOFS		Number of bytes left in INPUT buffer.
 *	43		3		***			Reserved for future expansion.
 *	46		1		DEVICE		Device Number:
 *					0-9 		Disks A: through J:
 *					255 KYBD:
 *					254 SCRN:
 *					253 LPT1:
 *					252 CAS1:
 *					251 COM1:
 *					250 COM2:
 *					249 LPT2:
 *					248 LPT3:
 *	47		1		WIDTH		Device width.
 *	48		1		POS			Position in buffer for PRINT.
 *	49		1		FLAGS		Internal use during BLOAD/BSAVE. Not used for data files.
 *	50		1		OUTPOS		Output position used during tab expansion.
 *	51		128		BUFFER		Physical data buffer. Used to transfer data between DOS and BASIC. Use this offset to examine data in sequential I/O mode.
 *	179		2		VRECL		Variable length record size. Default is 128. Set by length option in OPEN statement.
 *	181		2		PHYREC		Current physical record number.
 *	183		2		LOGREC		Current logical record number.
 *	185		1		***			Future use.
 *	186		2		OUTPOS		Disk files only. Output position for PRINT, INPUT, and WRITE.
 *	188		n		FIELD		Actual FIELD data buffer. Size is determined by S:switch. VRECL bytes are transferred between BUFFER and FIELD on I/O operations. Use this offset to examine file data in random I/O mode.
 * </pre>
 * @author lawrence.daniels@gmail.com
 */
public interface MsDosDisketteFileControlBlock extends MemoryObject {
	
	///////////////////////////////////////////////////////
	//      Accessor and Mutator Method(s)
	///////////////////////////////////////////////////////
	
	/**
	 * @return the physical data buffer
	 */
	byte[] getBuffer();
	
	/** 
	 * Sets the physical data buffer
	 * <br><b>NOTE:</b> Used to transfer data between DOS and BASIC. 
	 * Use this offset to examine data in sequential I/O mode.
	 * @param buffer the data to be written to the buffer
	 */
	void setBuffer( byte[] buffer );
	
	/**
	 * @return the ID of the persistence device
	 */
	int getDevice();
	
	/**
	 * Sets the ID of the persistence device
	 * @param device the given device ID
	 */
	void setDevice( int device );
	
	/**
	 * @return the bytes contained with the diskette control block
	 */
	byte[] getDisketteControlBlock();
	
	/**
	 * Sets the bytes contained with the diskette control block
	 * @param block the bytes contained with the diskette control block
	 */
	void setDisketteControlBlock( byte[] block );
	
	/**
	 * @return the current flag state
	 */
	int getFlags();
	
	/**
	 * Sets the current flag state
	 * <br><b>NOTE</b>: Internal use during BLOAD/BSAVE. Not used for data files.
	 * @param flags
	 */
	void setFlags( int flags );
	
	/** 
	 * @return the current logical record number.
	 */
	int getLogicalRecordNumber();
	
	/** 
	 * Sets the current logical record number.
	 * @param recordNumber the logical record number.
	 */
	void setLogicalRecordNumber( int recordNumber );
	
	/**
	 * @return the current persistence mode
	 */
	int getMode();
	
	/**
	 * Sets the current persistence mode
	 * @param mode the given mode value
	 * @see #MODE_INPUT
	 * @see #MODE_OUTPUT
	 * @see #MODE_RANDOM
	 * @see #MODE_APPEND
	 */
	void setMode( int mode );
	
	/** 
	 * @return the current physical record number.
	 */
	int getPhysicalRecordNumber();
	
	/** 
	 * Sets the current physical record number.
	 * @param recordNumber the physical record number.
	 */
	void setPhysicalRecordNumber( int recordNumber );
	
	/**
	 * @return the current printing position
	 */
	int getPosition();
	
	/**
	 * Sets the current printing position
	 * @param position the current printing position
	 */
	void setPosition( int position );
	
	/**
	 * Variable length record size. Default is 128. Set by length option in OPEN statement.
	 * @return the variable record length
	 */
	int getVariableRecordLength();
	
	/**
	 * Sets the variable record length
	 * @param varRecLength the given variable record length
	 */
	void setVariableRecordLength( int varRecLength );
	
	/**
	 * @return the current device width
	 */
	int getWidth();
	
	/**
	 * Sets the device width
	 * @param width the given device width
	 */
	void setWidth( int width );
	
	///////////////////////////////////////////////////////
	//      Constant(s)
	///////////////////////////////////////////////////////
	
	// positional constants
	int POS_MODE 		= 0;
	int POS_FCB 		= 1;	
	int POS_CURLOC		= 39;
	int POS_ORNOFS		= 41;
	int POS_NMLOFS		= 42;
	int POS_DEVICE		= 46;
	int POS_WIDTH		= 47;
	int POS_POS			= 48;
	int POS_FLAGS		= 49;
	int POS_OUTPOS		= 50;
	int POS_BUFFER		= 51;	
	int POS_VERCL		= 179;
	int POS_PHYREC		= 181;
	int POS_LOGREC		= 183;
	int POS_OUTPOS2		= 186;
	
	// length constants
	int LEN_FCB 		= 38;
	int LEN_BUFFER		= 128;
	int LEN_VERCL		= 2;
	int LEN_PHYREC		= 2;
	int LEN_LOGREC		= 2;
	
	// mode constants
	int MODE_INPUT 		= 1;  // 00001b
	int MODE_OUTPUT 	= 2;  // 00010b
	int MODE_RANDOM 	= 4;  // 00100b
	int MODE_APPEND 	= 16; // 10000b
	
	// device constents
	int DEVICE_DRIVE_A	= 0;
	int DEVICE_DRIVE_B	= 1;
	int DEVICE_DRIVE_C	= 2;
	int DEVICE_KYBD		= 255;
	int DEVICE_SCRN		= 254;
	int DEVICE_LPT1		= 253;
	int DEVICE_CAS1		= 252;
	int DEVICE_COM1		= 251;
	int DEVICE_COM2		= 250;
	int DEVICE_LPT2		= 249;
	int DEVICE_LPT3		= 248;
	
	// general constants
	int DEFAULT_VRECL 	= 128;
	int LENGTH 			= 188;

}
