package ibmpc.devices.cpu;

/**
 * Represents a context for executing an 80x86 memory resident program.
 * @author lawrence.daniels@gmail.com
 */
public class ProgramContext {
	private final ProgramArguments[] arguments;
	private final int codeSegment;
	private final int dataSegment;
	private final int codeOffset;
	
	/**
	 * Creates a new context for executing a memory resident program.
	 * @param codeSegment the segment where the program resides.
	 * @param dataSegment the segment where the program's data resides.
	 * @param codeOffset the offset within the code segment to start execution.
	 * @param arguments the given program arguments; push onto the stack at
	 * the start of execution.
	 */
	public ProgramContext( final int codeSegment, 
						   final int dataSegment,
						   final int codeOffset,
						   final ProgramArguments[] arguments ) {
		this.codeSegment	= codeSegment;
		this.dataSegment	= dataSegment;
		this.codeOffset		= codeOffset;
		this.arguments		= arguments;
	}
	
	/**
	 * @return the segment where the program code resides
	 */
	public int getCodeSegment() {
		return codeSegment;
	}
	
	/**
	 * @return the segment where the program data resides
	 */
	public int getDataSegment() {
		return dataSegment;
	}

	/**
	 * @return the program arguments
	 */
	public ProgramArguments[] getArguments() {
		return arguments;
	}

	/**
	 * @return the offset
	 */
	public int getCodeOffset() {
		return codeOffset;
	}

}
