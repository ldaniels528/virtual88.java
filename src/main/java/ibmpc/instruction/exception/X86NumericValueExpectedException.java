/**
 * 
 */
package ibmpc.instruction.exception;

import ibmpc.instruction.element.X86DataElement;

/**
 * 80x86 Malformed Instruction Exception - Numeric value expected
 * @author lawrence.daniels@gmail.com
 */
@SuppressWarnings("serial")
public class X86NumericValueExpectedException extends X86MalformedInstructionException {
	private final X86DataElement dataElement;
	
	/**
	 * Creates a new "Numeric value expected" exception
	 * @param dataElement the given {@link X86DataElement data element}
	 */
	public X86NumericValueExpectedException( final X86DataElement dataElement ) {
		super( "Numeric value expected near '" + dataElement + "'" );
		this.dataElement = dataElement;
	}

	/**
	 * @return the {@link X86DataElement data element}
	 */
	public X86DataElement getDataElement() {
		return dataElement;
	}

}
