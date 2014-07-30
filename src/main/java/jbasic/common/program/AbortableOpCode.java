package jbasic.common.program;

/**
 * Represents an {@link OpCode opCode} that causes the current
 * process to abort.
 */
public interface AbortableOpCode extends OpCode {

	/**
	 * @return indicates whether this opCode should abort
	 */
	public boolean abort();
	
}
