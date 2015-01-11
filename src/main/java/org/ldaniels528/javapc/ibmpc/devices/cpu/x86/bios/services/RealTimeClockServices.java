package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.bios.services;

import org.ldaniels528.javapc.ibmpc.devices.cpu.Intel80x86;
import org.ldaniels528.javapc.ibmpc.exceptions.X86AssemblyException;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

import java.util.Calendar;

/**
 * BIOS Time Services
 * @author lawrence.daniels@gmail.com
 */
public class RealTimeClockServices implements InterruptHandler {
	private static final RealTimeClockServices instance = new RealTimeClockServices();
	private long lastChangedTime;
	private long virtualTime;
	
	/**
	 * Private constructor
	 */
	private RealTimeClockServices() {
		this.lastChangedTime	= System.currentTimeMillis();
		this.virtualTime		= 0;
	}
	
	/**
	 * Returns the singleton instance of this class 
	 * @return the singleton instance of this class 
	 */
	public static RealTimeClockServices getInstance() {
		return instance;
	}
	
	/**
	 * Process the Time Services Interrupt (INT 1Ah)
	 * @throws X86AssemblyException
	 */
	public void process(IbmPcSystem system, final Intel80x86 cpu)
	throws X86AssemblyException {
		switch( cpu.AH.get() ) {
			case 0x00:	readSystemClockCounter( cpu ); break;
			case 0x01:	setSystemClockCounter( cpu ); break;
			default:	throw new X86AssemblyException( String.format( "Invalid call (AH = %02X)", cpu.AH.get() ) );
		}
	}
	
	/** 
	 * <pre>
	 * INT 1A,0 - Read System Clock Counter
	 * AH = 00
	 * on return:
	 *	AL = midnight flag, 1 if 24 hours passed since reset
	 *	CX = high order word of tick count
	 *	DX = low order word of tick count
	 * - incremented approximately 18.206 times per second
	 * - at midnight CX:DX is zero
	 * - this function can be called in a program to assure the date is
	 * updated after midnight; this will avoid the passing two midnights
	 * date problem
	 * </pre>
	 * @param cpu the given {@link Intel80x86 CPU} instance
	 */
	private void readSystemClockCounter( final Intel80x86 cpu ) {
		// get midnight in milliseconds
		final long midnightMillis = getMidnightTimeInMillis();
		
		// get the current time in milliseconds
		final long currentTimeMillis = System.currentTimeMillis();
	
		// how long since midnight?
		final int sinceMidNight = (int)( currentTimeMillis - midnightMillis );
		
		// get the high and low portions
		final int timeHi = ( sinceMidNight & 0xFFFF0000 ) >> 16;
		final int timeLo = ( sinceMidNight & 0x0000FFFF );
	
		// set registers
		cpu.AL.set( 1 );
		cpu.CX.set( timeHi );
		cpu.DX.set( timeLo );
	}
	
	/** 
	 * <pre>
	 * INT 1A,1 - Set System Clock Counter
	 * 
	 * pparameters:
	 * 		AH = 01
	 * 		CX = high order word of tick count
	 * 		DX = low order word of tick count
	 * returns nothing
	 * - CX:DX should be set to the number of seconds past
	 * midnight multiplied by approximately 18.206
	 * </pre>
	 * @param cpu the given {@link Intel80x86 CPU} instance
	 */
	private void setSystemClockCounter( final Intel80x86 cpu ) {
		// get the time since midnight from CX:DX
		final int timeSinceMidnight = ( cpu.CX.get() << 16 ) | cpu.DX.get();
		
		// get the time at midnight (in milliseconds)
		final long midnightMillis = getMidnightTimeInMillis();
		
		// set the new virtual time
		virtualTime = midnightMillis + timeSinceMidnight;
		
		// set the time of the last update of the virtual clock
		lastChangedTime = System.currentTimeMillis();
	}
	
	/**
	 * Returns the time in milliseconds for midnight of the 
	 * current date.
	 * @return the time in milliseconds for midnight of the 
	 * current date.
	 */
	private long getMidnightTimeInMillis() {
		// get the calendar instance 
		final Calendar midnight = Calendar.getInstance();
		midnight.setTimeInMillis( System.currentTimeMillis() );
		midnight.set( Calendar.HOUR, 0 );
		midnight.set( Calendar.MINUTE, 0 );
		midnight.set( Calendar.SECOND, 0 );
		midnight.set( Calendar.MILLISECOND, 0 );
		
		// return the date
		return midnight.getTimeInMillis();
	}
	
	/**
	 * Returns the current "virtual" time (in milliseconds)
	 * @return the current "virtual" time
	 */
	private long getCurrentVirtualTime() {
		// get the time since the mask was set
		final long timeDiff = System.currentTimeMillis() - lastChangedTime;
		
		// return the "virtual" time
		return timeDiff + virtualTime;
	}
 	
}
