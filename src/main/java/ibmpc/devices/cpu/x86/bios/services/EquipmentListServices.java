package ibmpc.devices.cpu.x86.bios.services;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.exceptions.X86AssemblyException;
import ibmpc.system.IbmPcSystem;
import ibmpc.system.IbmPcSystemInfo;

/**
 * BIOS Equipment List Services
 * <pre>
 * Equipment Check
 * 16-15     Number of printers installed 
 * 14 = 1    if internal modem installed 
 * 13 = 1    if game adapter installed 
 * 12-10     Number of serial ports installed 
 * 9         not used 
 * 8-6       Number of floppy drives (if bit 0=1) 
 *               00=1, 01=2KB, 10=3, 11=4 
 * 5-4       Initial video mode 
 *              00=not used, 01=40x25 color, 
 *              10=80x25 color, 11=80x25 monochrome 
 * 4-2       System board RAM size (original PC) 
 *              00=16KB, 01=32KB, 10=48KB, 11=64KB 
 * 2 = 1     if math coprocessor installed 
 * 1 = 1     if floppy drive installed
 * </pre>
 * @author lawrence.daniels@gmail.com
 */
public class EquipmentListServices implements InterruptHandler {
	private static final EquipmentListServices instance = new EquipmentListServices();
	
	/**
	 * Private constructor
	 */
	private EquipmentListServices() {
		super();
	}
	
	/**
	 * Returns the singleton instance of this class 
	 * @return the singleton instance of this class 
	 */
	public static EquipmentListServices getInstance() {
		return instance;
	}
	
	/**
	 * Process the Equipment Services Interrupt (INT 11h)
	 * @param system the given {@link IbmPcSystem IBM PC System} instance
	 * @throws X86AssemblyException
	 */
	public void process( final Intel80x86 cpu ) 
	throws X86AssemblyException {
		// get the system information
		final IbmPcSystemInfo systemInfo = cpu.getSystem().getInformation();
		
		// set the properties
		setMathCoprocessorInstalled( cpu, cpu.MSW.isFPUPresent() );
		setInstalledMemory( cpu, systemInfo.getInstalledMemory() );
		setInitialDisplayMode( cpu, systemInfo.getInitialDisplayMode().getVideoMode() );
		setFloppiesInstalled( cpu, systemInfo.getFloppyDrives() );
		setInstalledSerialPorts( cpu, systemInfo.getSerialPorts() );
		setGamePortInstalled( cpu, systemInfo.isGamePortInstalled() );
		setInternalModemInstalled( cpu, systemInfo.isInternalModemInstalled() );
		setPrintersInstalled( cpu, systemInfo.getInstalledPrinters() );
	}
	
	/**
	 * Sets the math co-processor flag to installed/uninstalled
	 * @param installed indicates whether the hardware is installed or not 
	 */
	private void setMathCoprocessorInstalled( final Intel80x86 cpu, final boolean installed ) {
		cpu.AX.setBit( 2, installed );
	}
	
	/**
	 * Sets the amount of memory installed
	 * @param memorySize the given memory size (in kilobytes)
	 */
	private void setInstalledMemory( final Intel80x86 cpu, final int memorySize ) {
		// determine the memory index (e.g. 00=16KB, 01=32KB, 10=48KB, 11=64KB)
		final int memIndex = determineMemoryIndex( memorySize );
		
		cpu.AX.setBit( 3, ( memIndex & 0x01 ) > 0 ); // 01
		cpu.AX.setBit( 4, ( memIndex & 0x02 ) > 0 ); // 10
	}
	
	/**
	 * Determine the memory index (e.g. 00=16KB, 01=32KB, 10=48KB, 11=64KB)
	 * based on the given memory size
	 * @param memorySize the given memory size (in kilobytes)
	 * @return the memory index value
	 */
	private int determineMemoryIndex( final int memorySize ) {
		if( memorySize >= 64 ) return 3; 		// 11
		else if( memorySize >= 48 ) return 2;	// 10
		else if( memorySize >= 32 ) return 1;	// 01
		else return 0;							// 00
	}
	
	/**
	 * Sets the initial display mode
	 * @param mode the initial display mode 
	 * (e.g. 00=not used, 01=40x25 color, 10=80x25 color, 11=80x25 monochrome )
	 */
	private void setInitialDisplayMode( final Intel80x86 cpu, final int mode ) {
		cpu.AX.setBit( 5, ( mode & 0x01 ) > 0 ); // 01
		cpu.AX.setBit( 6, ( mode & 0x02 ) > 0 ); // 10
	}
	
	/**
	 * Sets the number of floppies installed
	 * @param floppies the given number of floppies installed
	 */
	private void setFloppiesInstalled( final Intel80x86 cpu, final int floppies ) {
		cpu.AX.setBit( 1, floppies != 0 );
		cpu.AX.setBit( 6, ( floppies & 0x01 ) > 0 ); // 001
		cpu.AX.setBit( 7, ( floppies & 0x02 ) > 0 ); // 010
		cpu.AX.setBit( 8, ( floppies & 0x04 ) > 0 ); // 100
	}
	
	/**
	 * Sets the number of serial ports installed
	 * @param floppies the given number of serial ports installed
	 */
	private void setInstalledSerialPorts( final Intel80x86 cpu, final int ports ) {
		cpu.AX.setBit( 10, ( ports & 0x01 ) > 0 ); // 001
		cpu.AX.setBit( 11, ( ports & 0x02 ) > 0 ); // 010
		cpu.AX.setBit( 12, ( ports & 0x04 ) > 0 ); // 100
	}
	
	/**
	 * Sets the game port flag to installed/uninstalled
	 * @param installed indicates whether the hardware is installed or not 
	 */
	private void setGamePortInstalled( final Intel80x86 cpu, final boolean installed ) {
		cpu.AX.setBit( 13, installed );
	}
	
	/**
	 * Sets the internal modem flag to installed/uninstalled
	 * @param installed indicates whether the hardware is installed or not 
	 */
	private void setInternalModemInstalled( final Intel80x86 cpu, final boolean installed ) {
		cpu.AX.setBit( 14, installed );
	}
	
	/**
	 * Sets the number of printers installed
	 * @param floppies the given number of printers installed
	 */
	private void setPrintersInstalled( Intel80x86 cpu, int printers ) {
		cpu.AX.setBit( 15, ( printers & 0x01 ) > 0 ); // 01
		cpu.AX.setBit( 16, ( printers & 0x02 ) > 0 ); // 10
	}
	
}
