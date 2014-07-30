package jbasic.common.exceptions;


@SuppressWarnings("serial")
public class OverflowException extends RuntimeException {
	private double value;

	public OverflowException( double value ) {
		super( "Overflow" );
		this.value = value;
	}
	
	public double getValue() {
		return value;
	}
	
}
