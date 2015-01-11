package org.ldaniels528.javapc.jbasic.gwbasic.program.commands.graphics;

import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenIterator;

import org.ldaniels528.javapc.ibmpc.devices.display.IbmPcDisplay;
import org.ldaniels528.javapc.ibmpc.devices.display.modes.IbmPcDisplayMode;
import org.ldaniels528.javapc.ibmpc.devices.memory.MemoryObject;
import org.ldaniels528.javapc.jbasic.common.JBasicGMLProcessor;
import org.ldaniels528.javapc.jbasic.common.exceptions.IllegalFunctionCallException;
import org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException;
import org.ldaniels528.javapc.jbasic.common.exceptions.TypeMismatchException;
import org.ldaniels528.javapc.jbasic.common.program.JBasicCompiledCode;
import org.ldaniels528.javapc.jbasic.common.util.JBasicTokenUtil;
import org.ldaniels528.javapc.jbasic.common.values.Value;
import org.ldaniels528.javapc.jbasic.gwbasic.program.commands.GwBasicCommand;
import org.ldaniels528.javapc.jbasic.gwbasic.values.GwBasicValues;

/**
 * DRAW Command
 * <br>Purpose: To draw a figure.
 * <br>Syntax: DRAW <i>drawingOps</i>
 * <br>Example: DRAW "C1R5U6L5D6"
 * @see org.ldaniels528.javapc.jbasic.common.JBasicGMLProcessor
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
	   * @see org.ldaniels528.javapc.jbasic.base.program.OpCode#execute(org.ldaniels528.javapc.jbasic.base.program.JBasicProgram, org.ldaniels528.javapc.jbasic.base.environment.JBasicEnvironment)
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
