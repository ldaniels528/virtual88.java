package ibmpc.devices.cpu;

import static ibmpc.devices.cpu.operands.Operand.*;

import ibmpc.devices.cpu.operands.Operand;
import ibmpc.devices.cpu.operands.memory.MemoryAddressFAR32;
import ibmpc.devices.cpu.x86.decoder.DecodeProcessor;
import ibmpc.devices.cpu.x86.decoder.DecodeProcessorImpl;
import ibmpc.devices.cpu.x86.opcodes.system.INT;
import ibmpc.devices.memory.IbmPcRandomAccessMemory;
import ibmpc.devices.ports.IbmPcHardwarePorts;
import ibmpc.exceptions.X86AssemblyException;
import ibmpc.system.IbmPcSystem;
import org.apache.log4j.Logger;

import static java.lang.String.format;

/**
 * Emulates an Intel 80x86 16/32-bit microprocessor
 * @author lawrence.daniels@gmail.com
 */
public class Intel80x86 extends X86RegisterSet {
	// CPU mode constants
	public static final int REAL_MODE = 0; 		// [80286] 20 bit segmented memory address space 
	public static final int PROTECTED_MODE = 1; // [80286] Enhances multitasking & system stability 
	public static final int VIRTUAL_MODE = 2; 	// [80386] Allows the execution of real mode applications that violate the rules under the control of a protected mode operating system.
	public static final int LONG_MODE = 3; 		// [AMD64] Provides an application access to 64-bit instructions and registers

	// system timer frequency
	private static final long SYSTEM_TIMER_FREQ = 1000 / 18; // 18 times/sec

	// internal fields
	private final Logger logger = Logger.getLogger(getClass());
	private final IbmPcRandomAccessMemory memory;
	private final DecodeProcessor decoder;
	private final IbmPcHardwarePorts ports;
	private final IbmPcSystem system;
	private final X86Stack stack;
	private long lastTimerUpdate;
	private boolean ipChanged;
	private boolean active;

	/////////////////////////////////////////////////////////
	//		Constructor(s)
	/////////////////////////////////////////////////////////
	
	/**
	 * Creates a new i8086 instance
	 * @param system the {@link IbmPcSystem system} the CPU reside within
	 * @param memory the {@link IbmPcRandomAccessMemory random access memory} instance
	 */
	public Intel80x86( final IbmPcSystem system, final IbmPcRandomAccessMemory memory ) {
		this.system 			= system;
		this.memory				= memory;
		this.active				= true;
		this.stack				= new X86Stack( memory, this );
		this.decoder 			= new DecodeProcessorImpl( this, memory );
		this.ports				= new IbmPcHardwarePorts( memory );
		this.lastTimerUpdate	= System.currentTimeMillis();
		this.ipChanged			= false;
	}
		
	/////////////////////////////////////////////////////////
	//		CPU Service Method(s)
	/////////////////////////////////////////////////////////

	/**
	 * @return the current program stack
	 */
	public X86Stack getStack() {
		return stack;
	}
	
	/**
	 * Returns the IBM PC System that the CPU is currently running on
	 * @return the IBM PC System instance
	 */
	public IbmPcSystem getSystem() {
		return system;
	}
	
	/////////////////////////////////////////////////////////
	//		CPU Execution Method(s)
	/////////////////////////////////////////////////////////
	
	/**
	 * Executes the given compiled code
	 * @param context the given {@link ProgramContext 80x86 execution context}
	 */
	public void execute( final ProgramContext context )
	throws X86AssemblyException {
		// cache the code segment and offset
		final int codeSegment = context.getCodeSegment();
		final int codeOffset  = context.getCodeOffset();
		
		// setup the segments
		CS.set( context.getCodeSegment() );
		DS.set( context.getDataSegment() );
		ES.set( context.getDataSegment() );
		SS.set( context.getDataSegment() );
		
		// setup the instruction and stack pointers
		IP.set( context.getCodeOffset() );
		SP.set( 0xFFFE );
		
		// push the arguments onto the stack
		final ProgramArguments[] args = context.getArguments();
		if( args != null ) {
			for( final ProgramArguments arg : args ) {
				stack.pushValue( arg.getOffset() );
			}
		}
		
		// point the decoder to the code segment and offset
		decoder.redirect( codeSegment, codeOffset );
		
		try {
			// initialize the decoder
			decoder.init();
			
			// continue to execute while active
			while( isActive() ) {
				// decode the next opCode
				final OpCode opCode = getNextOpCode();
				
				// execute the opCode
				execute( opCode );
			}
		}
		finally {
			// shutdown the decoder
			decoder.shutdown();
		}
	}
	
	/**
	 * Executes the given opCode
	 * @param opCode the given {@link OpCode opCode}
	 * @throws X86AssemblyException
	 */
	public void execute( final OpCode opCode ) 
	throws X86AssemblyException {	
		// update the system timer
		updateSystemTimer();

		// display the instruction information
		logger.info(format("E [%04X:%04X] %10X[%d] %s", CS.get(), IP.get(), opCode.getInstructionCode(), opCode.getLength(), opCode));
		
		// execute the instruction
		opCode.execute( this );
		
		// advance the instruction pointer
		if( !ipChanged ) {
			IP.add( opCode.getLength() );
		}
		else {
			ipChanged = false;
		}
	}
	
	/**
	 * Retrieves the next 80x86 opCode from the decoder
	 * @return an {@link OpCode opCode}
	 */
	public OpCode getNextOpCode() {
		return decoder.decodeNext();
	}
	
	/**
	 * @return the hardware ports
	 */
	public IbmPcHardwarePorts getHardwarePorts() {
		return ports;
	}
	
	/////////////////////////////////////////////////////////
	//		CPU State Method(s)
	/////////////////////////////////////////////////////////
	
	/**
	 * Halts the CPU
	 */
	public void halt() {
		this.active = false;
	}
	
	/**
	 * Indicates whether the CPU is currently active (executing code).
	 * @return true, if the CPU is currently executing code.
	 */
	public boolean isActive() {
		return active;
	}
	
	/**
	 * Indicates whether the CPU is currently in virtual x86 mode.
	 * @return true, if the CPU is currently in virtual x86 mode.
	 */
	public boolean isVirtualMode() {
		return FLAGS.isVM();
	}
	
	/**
	 * Causes the CPU to resume executing code
	 */
	public void resume() {
		this.active = true;
	}
	
	/////////////////////////////////////////////////////////
	//		Memory Access Method(s)
	/////////////////////////////////////////////////////////

	/**
	 * @return the random access memory (RAM) instance 
	 */
	public IbmPcRandomAccessMemory getRandomAccessMemory() {
		return memory;
	}
	
	/**
	 * Retrieves a byte from [DS:offset] in memory
	 * @param offset the given offset
	 * @return an 8-bit value from memory
	 */
	public int getByte( final int offset ) {
		return memory.getByte( DS.get(), offset );
	}
	
	/**
	 * Retrieves a word from [DS:offset] in memory
	 * @param offset the given offset
	 * @return a 16-bit value from memory
	 */
	public int getWord( final int offset ) {
		return memory.getWord( DS.get(), offset );
	}

	/**
	 * Sets the byte found at the given offset 
	 * within the DS segment
	 * @param offset the given offset within the DS segment
	 * @param value the 8-bit value to set
	 */
	public void setByte( final int offset, final int value) {
		memory.setByte( DS.get(), offset, (byte)value );
	}

	/**
	 * Sets the word found at the given offset 
	 * within the DS segment
	 * @param offset the given offset within the DS segment
	 * @param value the 16-bit value to set
	 */
	public void setWord( final int offset, final int value) {
		memory.setWord( DS.get(), offset, value );
	}
	
	/**
	 * Moves the instruction pointer to the given offset within 
	 * the code segment.
	 * @param opCode the opCode that has issued the jump.
	 * @param destination the given {@link Operand destination}
	 * @param savePoint indicates whether the current position should
	 * be saved to the stack.
	 */
	public void jumpTo( final OpCode opCode, final Operand destination, final boolean savePoint ) {
		// indicate that the IP address changed
		ipChanged = true;
		
		// get the pointer value
		final int pointer = destination.get();
		
		// is it a 32-bit address?
		switch( destination.size() ) {
			case SIZE_32BIT:
				// save this position?
				if( savePoint ) {
					logger.info(format("Storing CS:IP as %04X:%04X", CS.get(), IP.get()));
					stack.push( CS );
					stack.pushValue( IP.get() + opCode.getLength() );
				}
				
				// get the segment and offset
				final int offset  = ( pointer & 0xFFFF0000 ) >> 16;
				final int segment = ( pointer & 0x0000FFFF );
				
				// jump to the position in memory
				CS.set( segment );
				IP.set( offset );
				break;
		
			case SIZE_8BIT:
			case SIZE_16BIT:
				// save this position?
				if( savePoint ) {
					logger.info(format("Storing IP as %04X", IP.get()));
					stack.pushValue( IP.get() + opCode.getLength() );
				}
				
				// jump to the offset in memory
				IP.set( pointer );
				break;
				
			default:
				throw new IllegalArgumentException( 
						String.format( "%d-bit memory operands are not supported", destination.size() ) 
				);
		}
	}
	
	/**
	 * Performs an interrupt call to the given memory destination
	 * @param destination the given destination {@link MemoryAddressFAR32 memory address}
	 */
	public void invokeInterrupt( final MemoryAddressFAR32 destination ) {
		// push the FLAGS, CS, and IP
		stack.push( FLAGS );		
		stack.push( CS );
		stack.push( IP );
		
		// jump to the position in memory
		CS.set( destination.getSegment() );
		IP.set( destination.getOffset() );
		
		// pop the IP, CS, and FLAGS
		stack.pop( IP );
		stack.pop( CS );
		stack.pop( FLAGS );
	}
	
	/**
	 * Returns to the given offset within CS
	 * @param count the number of elements from the stack to POP
	 */
	public void returnNear( final int count ) {
		// is the stack is empty, stop executing
		if( stack.isEmpty() ) {
			this.halt();
			return;
		}
		
		// indicate that the IP address changed
		ipChanged = true;
		
		// pop "count" values from the stack
		for( int i = 0; i < count; i++ ) {
			stack.popValue();
		}
		
		// get the return offset
		final int offset = stack.popValue();
		
		logger.info(format("Returning NEAR to %04X:%04X", CS.get(), offset));
		
		// jump to the offset in memory
		IP.set( offset );	
	}
	
	/**
	 * Returns to the given segment and offset
	 * @param count the number of elements from the stack to POP
	 */
	public void returnFar( final int count ) {
		// is the stack is empty, stop executing
		if( stack.isEmpty() ) {
			this.halt();
			return;
		}
		
		// indicate that the IP address changed
		ipChanged = true;
		
		// pop "count" values from the stack
		for( int i = 0; i < count; i++ ) {
			stack.popValue();
		}
		
		// get the segment and offset
		final int offset = stack.popValue();
		final int segment = stack.popValue();
		
		logger.info(format("Returning FAR to %04X:%04X", segment, offset));
		
		// jump to the offset in memory
		CS.set( segment );
		IP.set( offset );	
	}
	
	/**
	 * Updates the system timer (18 times/sec)
	 * @throws X86AssemblyException
	 */
	private void updateSystemTimer() 
	throws X86AssemblyException {	
		// is it time to invoke the system timer?
		final long elapsedSinceUpdate = System.currentTimeMillis() - lastTimerUpdate;
		if( ( elapsedSinceUpdate >= SYSTEM_TIMER_FREQ ) && FLAGS.isIF() ) {
			INT.SYSTIMR.execute( this );
			lastTimerUpdate = System.currentTimeMillis();
			logger.info(format("SYSTIMR timer last update %d msec ago", elapsedSinceUpdate));
		}
	}
	
}