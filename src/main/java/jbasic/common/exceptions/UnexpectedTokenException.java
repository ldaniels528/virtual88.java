package jbasic.common.exceptions;

@SuppressWarnings("serial")
public class UnexpectedTokenException extends JBasicException {
	
	public UnexpectedTokenException( final String expected, final String found ) {
		super( "Expected '" + expected + "' found '" + found + "'"  );
	}

}
