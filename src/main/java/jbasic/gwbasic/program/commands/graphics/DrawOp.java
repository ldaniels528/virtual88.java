package jbasic.gwbasic.program.commands.graphics;

import com.ldaniels528.tokenizer.TokenIterator;

import ibmpc.devices.display.IbmPcDisplay;
import ibmpc.devices.display.modes.IbmPcDisplayMode;
import ibmpc.devices.memory.MemoryObject;
import jbasic.common.JBasicGMLProcessor;
import jbasic.common.exceptions.IllegalFunctionCallException;
import jbasic.common.exceptions.JBasicException;
import jbasic.common.exceptions.TypeMismatchException;
import jbasic.common.program.JBasicCompiledCode;
import jbasic.common.util.JBasicTokenUtil;
import jbasic.common.values.Value;
import jbasic.gwbasic.program.commands.GwBasicCommand;
import jbasic.gwbasic.values.GwBasicValues;

/**
 * DRAW Command
 * <br>Purpose: To draw a figure.
 * <br>Syntax: DRAW <i>drawingOps</i>
 * <br>Example: DRAW "C1R5U6L5D6"
 * @see jbasic.common.JBasicGMLProcessor
 * @author lawrence.daniels@gmail.com
 */
public class DrawOp extends GwBasicCommand {
	private static final JBasicGMLProcessor gml = new JBasicGMLProcessor();
	private final Value drawOps;

	  /**
	   * Creates an instance of this operation
	   * @param it the given {@link TokenIterator token iterator}
	   * @throws JBasicException
	   */
	  public DrawOp( TokenIterator it ) throws JBasicException {    
		  // get the draw ops
		  drawOps = GwBasicValues.getValue( it );
		  
		  // no more tokens
		  JBasicTokenUtil.noMoreTokens( it );
	  }

	  /* 
	   * (non-Javadoc)
	   * @see jbasic.base.program.OpCode#execute(jbasic.base.program.JBasicProgram, jbasic.base.environment.JBasicEnvironment)
	   */
	  public void execute( JBasicCompiledCode program ) 
	  throws JBasicException {
		  // get the screen instance
		  final IbmPcDisplay screen = program.getSystem().getDisplay();
		  
		  // if current mode is not graphical, error
		  final IbmPcDisplayMode mode = screen.getDisplayMode();
		  if( !mode.isGraphical() )
			  throw new IllegalFunctionCallException( this );
		  
		  // make sure that the value is a string
		  MemoryObject object = drawOps.getValue( program );
		  if( !object.isString() )
			  throw new TypeMismatchException( object );
		  
		  // interpret the draw ops
		  gml.draw( screen, drawOps.toString() );		  
	  }

}
