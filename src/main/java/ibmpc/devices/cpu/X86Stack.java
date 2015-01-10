package ibmpc.devices.cpu;

import ibmpc.devices.cpu.operands.Operand;
import ibmpc.devices.memory.IbmPcRandomAccessMemory;
import ibmpc.exceptions.X86StackException;
import org.apache.log4j.Logger;

import static java.lang.String.format;

/**
 * Represents an 80x86 program stack
 * @author lawrence.daniels@gmail.com
 */
public class X86Stack implements Operand {
	private final Logger logger = Logger.getLogger(getClass());
	private final IbmPcRandomAccessMemory memory;
	private final Intel80x86 cpu;
	private int elements;
	
	/**
	 * Creates a new program stack
	 * @param memory the given {@link IbmPcRandomAccessMemory memory} instance
	 * @param cpu the given {@link Intel80x86 80x86 CPU} instance
	 */
	public X86Stack( final IbmPcRandomAccessMemory memory, final Intel80x86 cpu ) {
		this.memory = memory;
		this.cpu 	= cpu;
	}
	
	/* (non-Javadoc)
	 * @see ibmpc.devices.cpu.operands.Operand#get()
	 */
	public int get() {
		return popValue();
	}

	/* (non-Javadoc)
	 * @see ibmpc.devices.cpu.operands.Operand#set(int)
	 */
	public void set( final int value16 ) {
		pushValue( value16 );	
	}

	/* (non-Javadoc)
	 * @see ibmpc.devices.cpu.operands.Operand#size()
	 */
	public int size() {
		return SIZE_16BIT;
	}
	
	/**
	 * Indicates whether there is currently something on the stack
	 * @return true, if there is at least one element on the stack
	 */
	public boolean isEmpty() {
		return elements == 0;
	}
	
	/**
	 * Pops a value or offset from the stack
	 * @return a value or offset from the stack
	 * @throws X86StackException 
	 */
	public int popValue() throws X86StackException {
		// there must be an element in the stack
		if( elements == 0 ) {
			logger.warn(format("Stack overflow (pointer = %d)", elements));
		}
		
		// pop the value from the stack
		elements--;
		cpu.SP.add( 2 );		
		return memory.getWord( cpu.SS.get(), cpu.SP.get() );		
	}
	
	/**
	 * Pushes a value or offset on to the stack
	 */
	public void pushValue( final int value16 ) {
		elements++;
		memory.setWord( cpu.SS.get(), cpu.SP.get(), value16 );		
		cpu.SP.add( -2 );
	}
	
	/**
	 * Allows a peek at the a specific value on the stack
	 * without {@link #popValue() POPing} the value
	 * @param element the given element number (e.g. '[SP-<i>element#<i>]')
	 * @return the specific value on the stack
	 * @throws X86StackException
	 */
	public int peek( int element ) 
	throws X86StackException {
		// there must be an element in the stack
		if( elements == 0 ) {
			return -1;
		}
		
		// return the stack value
		return memory.getWord( cpu.SS.get(), cpu.SP.get() + ( element * 2 ) );	
	}
	
	/**
	 * Pops the next value from the stack into the given register
	 * @param operand the given {@link Operand operand}
	 * @throws X86StackException
	 */
	public void pop( final Operand operand ) 
	throws X86StackException {
		operand.set( popValue() );
	}
	
	/**
	 * Pushes value of the register onto the stack
	 * @param operand the given {@link Operand operand}
	 */
	public void push( final Operand operand ) {		
		pushValue( operand.get() );
	}

}
