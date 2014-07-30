package jbasic.gwbasic.program.commands.ide;

import jbasic.common.exceptions.JBasicException;
import jbasic.common.exceptions.TypeMismatchException;
import jbasic.common.program.JBasicCompiledCode;
import jbasic.common.program.JBasicProgramStatement;
import jbasic.gwbasic.GwBasicEnvironment;
import jbasic.gwbasic.program.GwBasicProgram;
import jbasic.gwbasic.program.GwBasicStatement;
import jbasic.gwbasic.program.commands.GwBasicCommand;
import jbasic.gwbasic.values.GwBasicValues;

import com.ldaniels528.tokenizer.TokenIterator;

/**
 * Syntax: <i>lineNumber</i> <i>command1<i>[: <i>command2<i>[: <i>commandN</i>]]</i><br>
 * Example: 100 COLOR 2,1: PRINT "HELLO"<br>
 * @author lawrence.daniels@gmail.com
 */
public class AddProgramLineOp extends GwBasicCommand {
	private final int lineNumber;
	private final String commands;

	  /**
	   * Creates an instance of this command
	   * @param it the parsed text that describes the BASIC instruction
	   * @throws JBasicException
	   */
	  public AddProgramLineOp( final TokenIterator it ) 
	  throws JBasicException {
		  this.lineNumber = parseLineNumber( it );
		  this.commands	= parseCommands( it );
	  }

	  /* 
	   * (non-Javadoc)
	   * @see jbasic.common.program.OpCode#execute(jbasic.common.program.JBasicCompiledCode)
	   */
	  public void execute( final JBasicCompiledCode compiledCode ) 
	  throws JBasicException {
		  // get the GWBASIC environment
		  final GwBasicEnvironment environment = (GwBasicEnvironment)compiledCode.getSystem();
		  
		  // get the memory resident program
		  final GwBasicProgram program = environment.getProgram();
		   
		  // get the statment
		  final JBasicProgramStatement statement = new GwBasicStatement( lineNumber, commands );
		  
		  // add the line to the program
		  program.add( statement );
	  }
	  
	  /**
	   * Parses the given line number
	   * @param it the given {@link TokenIterator token iterator}
	   * @return the line number
	   * @throws JBasicException
	   */
	  private int parseLineNumber( final TokenIterator it )
	  throws JBasicException {
		  // get the line number value
		  final String value = GwBasicValues.nextValue( it );
		  
		  // it must be numeric
		  if( !GwBasicValues.isNumericConstant( value ) )
			  throw new TypeMismatchException( value );
		  
		  // return the value as an integer
		  return Integer.parseInt( value );
	  }
	  
	  /**
	   * Parses the given commands
	   * @param it the given {@link TokenIterator token iterator}
	   * @return the commands
	   * @throws JBasicException
	   */
	  private String parseCommands( final TokenIterator it )
	  throws JBasicException {
		  return it.toString();
	  }
	  
}
