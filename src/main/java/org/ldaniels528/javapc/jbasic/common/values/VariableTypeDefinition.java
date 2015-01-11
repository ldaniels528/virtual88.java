package org.ldaniels528.javapc.jbasic.common.values;

/**
 * Variable Type Definition Inner Class
 * @see org.ldaniels528.javapc.jbasic.gwbasic.program.commands.system.DefIntOp
 * @see org.ldaniels528.javapc.jbasic.gwbasic.program.commands.system.DefDblOp
 * @see org.ldaniels528.javapc.jbasic.gwbasic.program.commands.system.DefSngOp
 * @author lawrence.daniels@gmail.com
 */
public class VariableTypeDefinition {
	public static final int TYPE_INTEGER 		= 0;
	public static final int TYPE_SINGLE_PREC	= 1;
	public static final int TYPE_DOUBLE_PREC 	= 2;
	public static final int TYPE_STRING 		= 3;
	private final char start;
	private final char end;
	private final int type;

	/**
	 * Creates a instance of this variable type definition
	 * @param start the given starting attribute (i.e. 'A')
	 * @param end the given ending attribute (i.e. 'Z')
	 * @param type the attribute type
	 */
	public VariableTypeDefinition(char start, char end, int type) {
		this.start = start;
		this.end = end;
		this.type = type;
	}

	/** 
	 * @return the starting attribute (i.e. 'A')
	 */
	public char getStart() {
		return start;
	}

	/** 
	 * @return the ending attribute (i.e. 'Z')
	 */
	public char getEnd() {
		return end;
	}

	/** 
	 * @return the attribute type
	 * @see #TYPE_INTEGER
	 * @see #TYPE_SINGLE_PREC
	 * @see #TYPE_DOUBLE_PREC
	 */
	public int getType() {
		return type;
	}

}
