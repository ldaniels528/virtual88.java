package jbasic.gwbasic.program.commands.console;

import com.ldaniels528.tokenizer.TokenIterator;

import ibmpc.devices.display.IbmPcDisplay;
import ibmpc.devices.display.modes.IbmPcDisplayMode;
import ibmpc.devices.memory.MemoryObject;
import jbasic.common.JBasicDisplayModes;
import jbasic.common.exceptions.IllegalFunctionCallException;
import jbasic.common.exceptions.JBasicException;
import jbasic.common.exceptions.TypeMismatchException;
import jbasic.common.program.JBasicCompiledCode;
import jbasic.common.util.JBasicTokenUtil;
import jbasic.common.values.Value;
import jbasic.gwbasic.program.commands.GwBasicCommand;

/**
 * SCREEN Statement
 * <br>Purpose: To set the specifications for the display screen.
 * <br>Syntax: SCREEN <i>mode</i>[,[<i>colorswitch</i>]][,[<i>apage</i>]][,[<i>vpage</i>]]
 * <br>Example: SCREEN 1
 * @author lawrence.daniels@gmail.com
 */
public class ScreenOp extends GwBasicCommand {
	private final Value screenVal;
	private final Value colorSwitchVal;
	private final Value accessPageVal;
	private final Value viewPageVal;
	
	  /**
	   * Creates an instance of this {@link jbasic.common.program.OpCode opCode}
	   * @param it the given {@link com.ldaniels528.tokenizer.TokenIterator token iterator}
	   * @throws JBasicException
	   */
	public ScreenOp( TokenIterator it ) throws JBasicException {
		// extract all possible values
		final Value[] values = JBasicTokenUtil.parseValues( it, 1, 4 );
						
		// get the extracted parameters
		screenVal 		= values[0];
		colorSwitchVal 	= ( values.length > 1 ) ? values[1] : null;
		accessPageVal	= ( values.length > 2 ) ? values[2] : null;
		viewPageVal		= ( values.length > 3 ) ? values[3] : null;		
	}

	/* 
	 * (non-Javadoc)
	 * @see jbasic.program.OpCode#execute(JBasicSourceCode)
	 */
	public void execute( JBasicCompiledCode program ) 
	throws JBasicException {
	    // get the screen value
	    final MemoryObject screenObj = screenVal.getValue( program );
	    if( !screenObj.isNumeric() )
	      throw new TypeMismatchException( screenObj );
	    
	    // get the screen instance
	    final IbmPcDisplay display = program.getSystem().getDisplay();

	    // lookup the current mode
	    final IbmPcDisplayMode currentMode = display.getDisplayMode();
	    
	    // set the screen definition
	    final int screen = screenObj.toInteger();
	    switch( screen ) {
	    		// non-graphical CGA modes
	    		case 0: display.setDisplayMode( 
	    					( currentMode.getColumns() == 80 ) 
	    					? JBasicDisplayModes.MODE0b 
	    					: JBasicDisplayModes.MODE0a 
	    				); 
	    				break;
	    		
	    		// CGA graphics modes
	    		case 1: display.setDisplayMode( JBasicDisplayModes.MODE1 ); break;
	    		case 2: display.setDisplayMode( JBasicDisplayModes.MODE2 ); break;
	    		
	    		// PCjr graphics modes
	    		case 3: display.setDisplayMode( JBasicDisplayModes.MODE3 ); break;
	    		case 4: display.setDisplayMode( JBasicDisplayModes.MODE4 ); break;
	    		case 5: display.setDisplayMode( JBasicDisplayModes.MODE5 ); break;
	    		
	    		// EGA graphics modes
	    		case 7: display.setDisplayMode( JBasicDisplayModes.MODE7 ); break;
	    		case 8: display.setDisplayMode( JBasicDisplayModes.MODE8 ); break;
	    		case 9: display.setDisplayMode( JBasicDisplayModes.MODE9 ); break;
	    		
	    		// VGA graphics modes
	    		case 10: display.setDisplayMode( JBasicDisplayModes.MODE10 ); break;
	    		
	    		// Unknown display mode
	    		default:	throw new IllegalFunctionCallException( this );
	    }
	    
	    // set the display page
	    if( accessPageVal != null ) {
	    		// get the access page
	    		final MemoryObject accessPage = accessPageVal.getValue( program );
	    		if( !accessPage.isNumeric() )
	    		      throw new TypeMismatchException( accessPage );
	    		// set the active page
	    		display.setActivePage( accessPage.toInteger() );
	    }
	}	

}
