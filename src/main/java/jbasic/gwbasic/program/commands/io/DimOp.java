package jbasic.gwbasic.program.commands.io;

import ibmpc.devices.memory.MemoryObject;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.ldaniels528.tokenizer.TokenIterator;

import jbasic.common.exceptions.JBasicException;
import jbasic.common.exceptions.TypeMismatchException;
import jbasic.common.program.JBasicCompiledCode;
import jbasic.common.util.JBasicTokenUtil;
import jbasic.common.values.Value;
import jbasic.gwbasic.program.commands.GwBasicCommand;
import jbasic.gwbasic.values.GwBasicValues;

/**
 * DIM Command
 * <br>Syntax: DIM <i>arrayName</i>(<i>size</i>)[,<i>arrayName</i>(<i>size</i>)]
 */
public class DimOp extends GwBasicCommand {
  private final Map<String,Collection<Value>> arrays;

  /**
   * Creates an instance of this opCode
   * @param it the parsed text that describes the BASIC instruction
   * @throws JBasicException
   */
  public DimOp( TokenIterator it ) throws JBasicException {
	  this.arrays = parse( it );
  }

  /* 
   * (non-Javadoc)
   * @see jbasic.common.program.OpCode#execute(jbasic.common.program.JBasicSourceCode)
   */
  public void execute( JBasicCompiledCode program ) 
  throws JBasicException {
	  // create each array
	  for( final String name : arrays.keySet() ) {
		  // get collection of values
		  final Collection<Value> values = arrays.get( name );
		  
		  // get the memory objects		  
		  final MemoryObject[] objects = new MemoryObject[ values.size() ];
		  int n = 0;
		  int size = 0;
		  for( Value value : values ) {
			  // get the memory object
			  MemoryObject object = value.getValue( program );
			  
			  // make sure it's numeric
			  if( !object.isNumeric() )
				  throw new TypeMismatchException( object );
			  
			  // remember this object
			  objects[n++] = object;
			  
			  // update the size
			  if( size == 0 ) size = object.toInteger();
			  else {
				  size *= object.toInteger();
			  }
		  }
			  
		  // create the array
		  program.createArrayVariable( name, size );
	  }
  }

  /**
   * Converts the given textual representation into {@link jbasic.common.values.Value values}
   * that will be displayed at runtime
   * @param it the given {@link com.ldaniels528.tokenizer.TokenIterator iterator}
   * @throws jbasic.common.exceptions.JBasicException
   */
  private Map<String,Collection<Value>> parse( TokenIterator it ) throws JBasicException {
	  final Map<String,Collection<Value>> arrays = new HashMap<String,Collection<Value>>();
	  while( it.hasNext() ) {
	      // get the array's name
	      final String arrayName = JBasicTokenUtil.nextToken( it );
	
		  // next must be opening parenthesis '('
		  JBasicTokenUtil.mandateToken( it, "(" );
		  
		  // build a value container
		  final Collection<Value> values = new LinkedList<Value>(); 
	
		  // get the size of the array
		  final Value size = GwBasicValues.getValue( it );
		  values.add( size );
	
		  // is this a multi-dimensional array?
		  while( ",".equals( it.peekAtNext() ) ) {
			  it.next();
			  
			  // get the next size element
			  final Value value = GwBasicValues.getValue( it );
			  values.add( value );
		  }
		  
		  // next must be opening parenthesis ')'
		  JBasicTokenUtil.mandateToken( it, ")" );
	
		  // add the array of our mapping
		  arrays.put( arrayName, values );
		  
	      // there there are more elements; check for a command separator (comma)
	      if( it.hasNext() ) {
	    	  	JBasicTokenUtil.mandateToken( it, "," );
	      }
	  }
	  return arrays;
  }

}
