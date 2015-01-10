package msdos.storage;

import static ibmpc.util.BitMaskGenerator.turnBitOnMask;

/**
 * Represents an MS-DOS Device Information
 * <pre>
 * Device Data Word
 * F E D C B A 9 8 7 6 5 4 3 2 1 0
 * | | | | | | | | | | | | | | | | 
 * | | | | | | | | | | | | | | | +---- 1 = standard input device
 * | | | | | | | | | | | | | | +---- 1 = standard output device
 * | | | | | | | | | | | | | +---- 1 = NUL device
 * | | | | | | | | | | | | +---- 1 = clock device
 * | | | | | | | | | | | +---- reserved
 * | | | | | | | | | | +---- 1 = binary mode, 0 = translated
 * | | | | | | | | | +---- 0 = end of file on input
 * | | | | | | | | +---- 1 = character device
 * | ? ? ? ? ? ?
 * |
 * +------------------ reserved, must be zero
 * </pre>
 * See <a href="http://www.htl-steyr.ac.at/~morg/pcinfo/hardware/interrupts/inte7wq8.htm">Device Data Word</a>
 * @author lawrence.daniels@gmail.com
 */
public class MsDosDeviceInfo {
	private final int deviceDataWord;
	
	/**
	 * Creates a new device information instance
	 * @param deviceDataWord the given integer bit array 
	 * representing the MS-DOS-specific device information.
	 */
	public MsDosDeviceInfo( final int deviceInfo ) {
		this.deviceDataWord = deviceInfo;
	}
		
	/**
	 * Indicates whether the device is a standard input device.
	 * @return true, if the device is a standard input device.
	 */
	public boolean isStandardInputDevice() {
		// is the bit #0 set?
		return ( deviceDataWord & turnBitOnMask( 0x00 ) ) > 0;
	}
	
	/**
	 * Indicates whether the device is a standard output device.
	 * @return true, if the device is a standard output device.
	 */
	public boolean isStandardOutputDevice() {
		// is the bit #1 set?
		return ( deviceDataWord & turnBitOnMask( 0x01 ) ) > 0;
	}
	
	/**
	 * Indicates whether the device is a NUL device.
	 * @return true, if the device is a NUL device.
	 */
	public boolean isNULDevice() {
		// is the bit #2 set?
		return ( deviceDataWord & turnBitOnMask( 0x02 ) ) > 0;
	}
	
	/**
	 * Indicates whether the device is a clock device.
	 * @return true, if the device is a clock device.
	 */
	public boolean isClockDevice() {
		// is the bit #3 set?
		return ( deviceDataWord & turnBitOnMask( 0x03 ) ) > 0;
	}
	
	/**
	 * Indicates whether the device is in binary mode, or
	 * conversely translated (ascii) mode.
	 * @return true, if the device is in binary mode
	 */
	public boolean isBinaryMode() {
		// is the bit #5 set?
		return ( deviceDataWord & turnBitOnMask( 0x05 ) ) > 0;
	}
	
	/**
	 * Indicates whether the input file is at the end of the file
	 * @return true, if the input file is at the end of the file
	 */
	public boolean isEndOfFileOnInput() {
		// is the bit #6 set?
		return ( deviceDataWord & turnBitOnMask( 0x06 ) ) > 0;
	}
	
	/**
	 * Indicates whether the device is a character device.
	 * @return true, if the device is a character device.
	 */
	public boolean isCharacterDevice() {
		// is the bit #7 set?
		return ( deviceDataWord & turnBitOnMask( 0x07 ) ) > 0;
	}
	
	/**
	 * Returns MS DOS Device Information as the following:
	 * @return the MS DOS device data word
	 */
	public int getDeviceDataWord() {
		return deviceDataWord;
	}

}
