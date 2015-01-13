package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.bios.services;

import org.ldaniels528.javapc.ibmpc.devices.cpu.I8086;
import org.ldaniels528.javapc.ibmpc.devices.memory.IbmPcRandomAccessMemory;
import org.ldaniels528.javapc.ibmpc.exceptions.X86AssemblyException;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

/**
 * <pre>
 * INT 8 - System timer
 * no input data
 * 
 * % related memory:
 * 0040:006C = Daily timer counter (4 bytes)
 * 0040:0070 = 24 hr overflow flag (1 byte)
 * 0040:0067 = Day counter on all products after AT
 * 0040:0040 = Motor shutoff counter - decremented until 0 then
 *             shuts off diskette motor
 * - ~INT 1C~ is invoked as a user interrupt
 * - the byte at 40:70 is a flag that certain DOS functions use
 *   and adjust the date if necessary. Since this is a flag and
 *   not a counter it results in DOS (not the ~SYSTIMR~) losing days
 *   when several midnights pass before a DOS call
 * - generated 18.2 times per second by the ~8253~ Programmable Interval
 * 
 * Timer (PIT)
 * - normal INT 8 execution takes approximately 100 microseconds
 * - see ~8253~
 * </pre>
 * @author ldaniels
 */
public class SystemTimerService implements InterruptHandler {
	// define memory information
	private static final int SEGMENT 						= 0x0040;
	private static final int OFFSET_DAILY_TIMER_COUNTER 	= 0x006C;
	private static final int OFFSET_24_HR_OVERFLOW_FLAG		= 0x0070;
	private static final int OFFSET_DAY_COUNTER				= 0x0067;
	private static final int OFFSET_MOTOR_SHUTOFF_COUNTER	= 0x0040;
	
	// time/date computation values
	private static final int TICKS_PER_DAY = (int)( 24 /* hours */ * 60 /* mins */ * 60 /* secs */ * 18.2 /* ticks/sec */ );
	
	// define internal fields
	private int timerCounter;
	private int dayCounter;
	private int overFlow24Hours;
	private int motorShutOffCounter;
	
	/**
	 * Default constructor
	 */
	private SystemTimerService() {
		this.timerCounter			= 0;
		this.dayCounter				= 0;
		this.overFlow24Hours		= 0;
		this.motorShutOffCounter	= 0;
	}

	/**
	 * Returns a new instance of the system timer
	 * @return a new instance of the system timer
	 */
	public static SystemTimerService getInstance() {
		return new SystemTimerService();
	}
	
	/* (non-Javadoc)
	 * @see org.ldaniels528.javapc.ibmpc.devices.cpu.x86.bios.services.InterruptHandler#process(org.ldaniels528.javapc.ibmpc.devices.cpu.Intel80x86)
	 */
	public void process(IbmPcSystem system, final I8086 cpu)
	throws X86AssemblyException {
		// get the memory instance
		final IbmPcRandomAccessMemory memory = cpu.getRandomAccessMemory();
		
		// count this tick
		timerCounter++;
		
		// has a day elapsed
		if( timerCounter >= TICKS_PER_DAY ) {
			timerCounter = 0;
			dayCounter++;
			overFlow24Hours = 1;
		}
		
		// is there a motor shut-off count down?
		if( motorShutOffCounter > 0 ) {
			if( --motorShutOffCounter ==  0 ) {
				// TODO shut-off diskette motor
			}
		}
		
		// populate related memory
		memory.setWord( SEGMENT, OFFSET_DAY_COUNTER, dayCounter );
		memory.setWord( SEGMENT, OFFSET_24_HR_OVERFLOW_FLAG, overFlow24Hours );
		memory.setWord( SEGMENT, OFFSET_DAILY_TIMER_COUNTER, timerCounter );
		memory.setWord( SEGMENT, OFFSET_MOTOR_SHUTOFF_COUNTER, timerCounter );
	}

}
