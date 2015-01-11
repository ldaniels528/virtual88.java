package org.ldaniels528.javapc.jbasic.gwbasic.program.commands.console;

import org.ldaniels528.javapc.ibmpc.devices.display.IbmPcColorSet;
import org.ldaniels528.javapc.ibmpc.devices.display.IbmPcDisplay;
import org.ldaniels528.javapc.ibmpc.devices.memory.MemoryObject;
import org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException;
import org.ldaniels528.javapc.jbasic.common.exceptions.TypeMismatchException;
import org.ldaniels528.javapc.jbasic.common.program.JBasicCompiledCode;
import org.ldaniels528.javapc.jbasic.common.util.JBasicTokenUtil;
import org.ldaniels528.javapc.jbasic.common.values.Value;
import org.ldaniels528.javapc.jbasic.gwbasic.program.commands.GwBasicCommand;

import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenIterator;

/**
 * COLOR Command
 * <pre>
 * Syntax: 	COLOR foreground[,background][,border]]
 *			COLOR foreground[,background]
 * 			COLOR background[,palette]
 *</pre>
 */
public class ColorOp extends GwBasicCommand {
	private final Value param1;
	private final Value param2;
	private final Value param3;
	
  /**
   * Creates an instance of this {@link org.ldaniels528.javapc.jbasic.common.program.OpCode opCode}
   * @param it the given {@link org.ldaniels528.javapc.jbasic.common.tokenizer.TokenIterator token iterator}
   * @throws JBasicException
   */
  public ColorOp( final TokenIterator it ) 
  throws JBasicException {
	  final Value[] values = JBasicTokenUtil.parseValues( it, 1, 3 );
	  param1 = values[0];
	  param2 = ( values.length > 1 ) ? values[1] : null;
	  param3 = ( values.length > 2 ) ? values[2] : null;
  }

  /* 
   * (non-Javadoc)
   * @see org.ldaniels528.javapc.jbasic.program.opcodes.OpCode#execute(org.ldaniels528.javapc.jbasic.environment.JBasicEnvironment)
   */
  public void execute( final JBasicCompiledCode compiledCode ) 
  throws JBasicException {	  	  
	  // construct a container for the values
	  final int[] colorParams = new int[] {
			  getColorIndex( compiledCode, param1 ),
			  getColorIndex( compiledCode, param2 ),
			  getColorIndex( compiledCode, param3 )
	  };
	  
	  // create a color set
	  final IbmPcColorSet colorSet = new IbmPcColorSet( colorParams );
	  
	  // get the display instance 
	  final IbmPcDisplay display = compiledCode.getSystem().getDisplay(); 
	  
	  // set the color(s)
	  display.setColor( colorSet );
  }
  
  /**
   * Returns the integer value of the given value
   * @param compiledCode the given {@link JBasicCompiledCode compiled code}
   * @param value the given {@link Value value}
   * @return the {@link Integer integer} value of the given {@link Value value}
   * @throws JBasicException
   */
  private int getColorIndex( final JBasicCompiledCode compiledCode, final Value value ) 
  throws JBasicException {
	  // if the parameter is null, return null
	  if( value == null ) return 0;	 
	   	  
	  // get the typed value
	  final MemoryObject object = value.getValue( compiledCode );
	  
	  // if the value is not numeric, type mismatch error
	  if( !object.isNumeric() )
		  throw new TypeMismatchException( object );
	  
	  // return the color index
	  return object.toInteger();
  }
  
  /* 
   * (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  public String toString() {
	  return new StringBuilder( 50 )
	  			.append( "COLOR[" )
	  			.append( param1 )
	  			.append( ',' )
	  			.append( param2 )
	  			.append( ',' )
	  			.append( param3 )
	  			.append( ']' )
	  			.toString();
  }

}