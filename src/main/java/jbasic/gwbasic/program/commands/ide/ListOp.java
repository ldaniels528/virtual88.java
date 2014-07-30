package jbasic.gwbasic.program.commands.ide;

import ibmpc.devices.display.IbmPcDisplay;

import java.util.Collection;

import com.ldaniels528.tokenizer.TokenIterator;

import jbasic.common.exceptions.JBasicException;
import jbasic.common.exceptions.SyntaxErrorException;
import jbasic.common.program.JBasicCompiledCode;
import jbasic.common.program.JBasicProgramStatement;
import jbasic.gwbasic.GwBasicEnvironment;
import jbasic.gwbasic.program.GwBasicProgram;
import jbasic.gwbasic.program.commands.GwBasicCommand;
import jbasic.gwbasic.values.GwBasicValues;

/**
 * LIST Command
 * <br>Purpose: List the contents of the current program in memory
 * <br>Syntax: LIST [[<i>lineNumber</i>] - [<i>lineNumber</i>]]
 * <br>Example 1: LIST
 * <br>Example 2: LIST 100
 * <br>Example 3: LIST -100
 */
public class ListOp extends GwBasicCommand {
	private static final String MAX_LINE_NUMBER = "65336";
	private int[] range;

	/**
	 * Creates an instance of this opCode
	 * @param it the given {@link TokenIterator token iterator}
	 * @throws JBasicException
	 */
	public ListOp( TokenIterator it ) throws JBasicException {
		parse( it );
	}

	/* 
	 * (non-Javadoc)
	 * @see jbasic.program.opcodes.OpCode#execute(jbasic.program.JBasicProgram, jbasic.environment.JBasicEnvironment)
	 */
	public void execute( final JBasicCompiledCode compiledCode ) 
	throws JBasicException {
		// get the environment
		final GwBasicEnvironment environment = (GwBasicEnvironment)compiledCode.getSystem();
		
		// cast the program to the appropriate type
		final GwBasicProgram gwBasicProgram = environment.getProgram();
		  
		// get the statements of the program
		final Collection<JBasicProgramStatement> statements = ( range != null )  
										? gwBasicProgram.getStatements( range ) 
										: gwBasicProgram.getStatements();
	 
		// get an instance of the display
		final IbmPcDisplay screen = compiledCode.getSystem().getDisplay();
		
		// write the statements to the buffer
		for( final JBasicProgramStatement statement : statements ) {
			screen.writeLine( statement.toString() );
			screen.update();
		}				
	}
  
	/**
	 * Converts the given textual representation into {@link jbasic.common.values.Value values}
	 * that will be displayed at runtime
	 * @param it the given {@link com.ldaniels528.tokenizer.TokenIterator iterator}
	 * @throws JBasicException
	 */
	private void parse( TokenIterator it ) 
	throws JBasicException {
		// if there are tokens ...
		if( it.hasNext() ) {
			range = getListRange( it );
		}		
	}
	
	/**
	 * Returns a range of line numbers to display
	 * @param it the given {@link com.ldaniels528.tokenizer.TokenIterator iterator}
	 * @return a range of line numbers to display
	 * @throws JBasicException
	 */
	private int[] getListRange( TokenIterator it ) throws JBasicException {
		// build the parameter string that will contain the range
		final String params = buildParameterString( it );	
		
		// split the range into two parts
		final String[] pcs = params.split( "[-]" );
		if( pcs.length != 2 )
			throw new SyntaxErrorException();
		
		// both pieces must be numeric
		if( !GwBasicValues.isNumericConstant( pcs[0] ) || 
				!GwBasicValues.isNumericConstant( pcs[1] ) )
			throw new SyntaxErrorException();
		
		// get start and end of the range
		final int start = (int)GwBasicValues.parseDecimalString( pcs[0] );
		final int end   = (int)GwBasicValues.parseDecimalString( pcs[1] );
		
		// construct the range as an integer array
		final int[] range = ( start <= end ) ? new int[] { start, end } :  new int[] { -1, -1 };
		
		return range;
	}

	/**
	 * Builds the parameter string for the range (e.g. "100-1000")
	 * @param it the given {@link com.ldaniels528.tokenizer.TokenIterator iterator}
	 * @return the parameter string for the range
	 */
	private String buildParameterString( TokenIterator it ) {
		final String MINUS = "-";
		
		// create a parameter string
		String params = "";
		while( it.hasNext() ) {
			params += it.next();
		}
		
		// if the string starts with a minus (-), 
		// then add zero (0) as the start of the range.
		if( params.startsWith( MINUS ) ) params = "0" + params;
		
		// if the string ends with a minus (-),
		// then add the highest line number		
		if( params.endsWith( MINUS ) ) params += MAX_LINE_NUMBER;
		
		// if no minus (-), fake the range
		if( !params.contains( MINUS ) ) {
			params = ( params + MINUS + getNextLineNumber( params ) );
		}
		
		// return the parameters
		return params;
	}
	
	/**
	 * Returns the next line number in the sequence
	 * @param line the given line number
	 * @return the next line number in the sequence
	 */ 
	private String getNextLineNumber( final String line ) {
		final int lineNumber = (int)GwBasicValues.parseNumericString( line ) ;
		return String.valueOf( lineNumber + 1 );
	}
	
}
