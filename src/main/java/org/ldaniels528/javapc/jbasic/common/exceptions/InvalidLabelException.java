package org.ldaniels528.javapc.jbasic.common.exceptions;

@SuppressWarnings("serial")
public class InvalidLabelException extends JBasicException {
	private final String label;
	
	public InvalidLabelException( String label ) {
		super( "Illegal line Number" );
		this.label = label;
	}
	
	public String getLabel() {
		return label;
	}

}
