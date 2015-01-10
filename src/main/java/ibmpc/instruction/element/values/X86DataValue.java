/**
 * 
 */
package ibmpc.instruction.element.values;

/**
 * Represents a data string
 * @author lawrence.daniels@gmail.com
 */
public class X86DataValue extends X86Value {
	private final String dataString;
	
	/**
	 * Creates a new data value
	 * @param dataString the given data string
	 */
	public X86DataValue( final String dataString ) {
		this.dataString = dataString;
	}

	/* (non-Javadoc)
	 * @see ibmpc.compiler.encoder.values.X86Value#getBytes()
	 */
	@Override
	public byte[] getBytes() {
		return dataString.getBytes();
	}

	/* (non-Javadoc)
	 * @see ibmpc.compiler.encoder.values.X86Value#getValue()
	 */
	@Override
	public String getValue() {
		return dataString;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see ibmpc.compiler.encoder.values.X86Value#isNumeric()
	 */
	public boolean isNumeric() {
		return false;
	}

}
