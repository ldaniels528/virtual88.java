package msdos.storage;

import ibmpc.devices.memory.X86MemoryProxy;

/**
 * Represents an MS-DOS File Control Block (FCB)
 * <pre>
 * Offset Size      Contents
 * ---------------------------------------------------------------------
 * 00     1 Byte    Drive number - 0 for default, 1 for A:, 2 for B:,...
 * 01     8 bytes   File name }
 * 09     3 bytes   File type } together these form an 8.3 name. 
 * 0C     20 bytes  Implementation dependent - should be initialised 
 *                  to zero before the FCB is opened.
 * 20     1 byte    Record number in the current section of the file -
 *                  used when performing sequential access.
 * 21     3 bytes   Record number to use when performing random access.
 * ---------------------------------------------------------------------
 *       36 bytes
 * </pre>
 * @author lawrence.daniels@gmail.com
 */
public class MsDosFileControlBlock {
	private int driveNumber;
	private String fileName;
	private String fileType;
	private byte[] miscData;
	private int recordNumber;
	private int randomAccessRecordNumber;
	
	/**
	 * Default constructor
	 */
	public MsDosFileControlBlock() {
		this.miscData = new byte[20];
	}
	
	/**
	 * @return the driveNumber
	 */
	public int getDriveNumber() {
		return driveNumber;
	}

	/**
	 * @param driveNumber the driveNumber to set
	 */
	public void setDriveNumber(int driveNumber) {
		this.driveNumber = driveNumber;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the fileType
	 */
	public String getFileType() {
		return fileType;
	}

	/**
	 * @param fileType the fileType to set
	 */
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	/**
	 * @return the miscData
	 */
	public byte[] getMiscData() {
		return miscData;
	}

	/**
	 * @param miscData the miscData to set
	 */
	public void setMiscData(byte[] miscData) {
		this.miscData = miscData;
	}

	/**
	 * @return the randomAccessRecordNumber
	 */
	public int getRandomAccessRecordNumber() {
		return randomAccessRecordNumber;
	}

	/**
	 * @param randomAccessRecordNumber the randomAccessRecordNumber to set
	 */
	public void setRandomAccessRecordNumber(int randomAccessRecordNumber) {
		this.randomAccessRecordNumber = randomAccessRecordNumber;
	}

	/**
	 * @return the recordNumber
	 */
	public int getRecordNumber() {
		return recordNumber;
	}

	/**
	 * @param recordNumber the recordNumber to set
	 */
	public void setRecordNumber(int recordNumber) {
		this.recordNumber = recordNumber;
	}

	/**
	 * Writes the file control block to the given memory proxy
	 * @param proxy the given {@link X86MemoryProxy memory proxy}
	 */
	public void writeTo( final X86MemoryProxy proxy ) {
		proxy.setByte( driveNumber );
		proxy.setBytes( fileName.getBytes() );
		proxy.setBytes( fileType.getBytes() );
		proxy.setBytes( miscData );
		proxy.setByte( recordNumber );
		proxy.setBytes( as24Bit( randomAccessRecordNumber ) );
	}
	
	/**
	 * Converts the given 24-bit value into a byte array
	 * @param value the given 24-bit value
	 * @return a byte array containing 3 bytes
	 */
	private byte[] as24Bit( final int value ) {
		final byte[] bytes = new byte[3];
		bytes[0] = (byte)( ( value & 0xFF0000 ) >> 16 );
		bytes[1] = (byte)( ( value & 0x00FF00 ) >> 8 );
		bytes[2] = (byte)( value & 0x0000FF );
		return bytes;
	}

}
