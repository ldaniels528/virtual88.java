/**
 * 
 */
package org.ldaniels528.javapc.ibmpc.compiler.element.values;

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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte[] getBytes() {
		return dataString.getBytes();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getValue() {
		return dataString;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isNumeric() {
		return false;
	}

}
