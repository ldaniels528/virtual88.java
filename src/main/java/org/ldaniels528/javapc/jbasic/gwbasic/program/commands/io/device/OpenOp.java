package org.ldaniels528.javapc.jbasic.gwbasic.program.commands.io.device;

import org.ldaniels528.javapc.ibmpc.devices.memory.MemoryManager;
import org.ldaniels528.javapc.ibmpc.devices.memory.MemoryObject;
import org.ldaniels528.javapc.ibmpc.devices.storage.IbmPcStorageSystem;
import org.ldaniels528.javapc.ibmpc.exceptions.IbmPcException;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException;
import org.ldaniels528.javapc.jbasic.common.exceptions.SyntaxErrorException;
import org.ldaniels528.javapc.jbasic.common.exceptions.TypeMismatchException;
import org.ldaniels528.javapc.jbasic.common.program.JBasicCompiledCode;
import org.ldaniels528.javapc.jbasic.common.util.JBasicTokenUtil;
import org.ldaniels528.javapc.jbasic.common.values.Value;
import org.ldaniels528.javapc.jbasic.gwbasic.GwBasicEnvironment;
import org.ldaniels528.javapc.jbasic.gwbasic.program.commands.io.file.AbstactFileOpCode;
import org.ldaniels528.javapc.jbasic.gwbasic.values.GwBasicValues;
import org.ldaniels528.javapc.msdos.storage.MsDosFileAccessMode;
import org.ldaniels528.javapc.msdos.storage.MsDosDisketteFileControlBlock;

import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenIterator;

/**
 * OPEN Command
 * <br>Syntax1: OPEN mode,[#]file number,filename[,reclen]
 * <br>Syntax2: OPEN filename [FOR mode] [ACCESS access] [lock] AS [#]file number [LEN=reclen]
 * @author lawrence.daniels@gmail.com
 */
public class OpenOp extends AbstactFileOpCode {
	private static final List MODES1 = Arrays.asList( new String[] { "I", "O", "A", "R" } );	
	private static final List MODES2 = Arrays.asList( new String[] { "INPUT", "OUTPUT", "APPEND", "RANDOM" } );
	private static final List ACCESS = Arrays.asList( new String[] { "READ", "WRITE" } );
	private static final List LOCKS = Arrays.asList( new String[] { "SHARED", "READ", "WRITE"  } );	
	private Value recordLength;
	private int handleID;
	private int device;
	private int access;
	private int mode;		
	private int lock;

	  /**
	   * Creates an instance of this opCode
	   * @param it the parsed text that describes the BASIC instruction
	   * @throws JBasicException
	   */
	  public OpenOp( final TokenIterator it ) 
	  throws JBasicException {
		  super( it );
	  }

	  /* 
	   * (non-Javadoc)
	   * @see org.ldaniels528.javapc.jbasic.program.opcodes.OpCode#execute(org.ldaniels528.javapc.jbasic.program.JBasicProgram, org.ldaniels528.javapc.jbasic.environment.JBasicEnvironment)
	   */
	  public void execute( final JBasicCompiledCode compiledCode ) 
	  throws JBasicException {
		  // get the environment
		  final GwBasicEnvironment environment = (GwBasicEnvironment)compiledCode.getSystem();
		  
		  // determine the full path of the file
		  final File file = getFileReference( sourcePath, null, compiledCode, environment );
		  
		  // create the file control block		  
		  final MsDosDisketteFileControlBlock fcb = createControlBlock( environment, compiledCode );
		  
		  final MsDosFileAccessMode accessMode = MsDosFileAccessMode.decode( mode );
		  
		  // get the storage device
		  final IbmPcStorageSystem storage = environment.getStorageSystem();
		  
		  // open the file
		  try {
			storage.openDevice( file.getAbsolutePath(), accessMode );
		  } 
		  catch( final IbmPcException e ) {
			throw new JBasicException( e );
		  }
	  }
	  
	  /**
	   * Creates the file control block
	   * @param environment the given {@link GwBasicEnvironment GWBASIC environment}
	   * @param compiledCode the currently running {@link JBasicCompiledCode compiled code}
	   * @return the {@link MsDosDisketteFileControlBlock file control block}
	   * @throws JBasicException
	   */
	  private MsDosDisketteFileControlBlock createControlBlock( final GwBasicEnvironment environment, 
			  											   final JBasicCompiledCode compiledCode ) 
	  throws JBasicException {
		  // get the memory manager
		  final MemoryManager memoryManager = environment.getMemoryManager();
		  
		  // create the file control block
		  final MsDosDisketteFileControlBlock fcb = memoryManager.createFileControlBlock();
		  
		  // populate the file control block		  
		  fcb.setDevice( device );		 
		  fcb.setMode( mode );		  
		  fcb.setVariableRecordLength( 
				  ( recordLength != null ) 
				  ? getRecordLength( compiledCode ) 
				  : MsDosDisketteFileControlBlock.DEFAULT_VRECL 
		  );		  
		  return fcb;
	  }
	  
	  /**
	   * Converts the given textual representation into {@link org.ldaniels528.javapc.jbasic.common.values.Value values}
	   * that will be displayed at runtime
	   * @param it the given {@link TokenIterator iterator}
	   * @throws JBasicException
	   */
	  protected void parse( final TokenIterator it ) 
	  throws JBasicException {
		  // get a reference to the file
		  sourcePath = GwBasicValues.getValue( it );
		  
		  // set the device
		  device = MsDosDisketteFileControlBlock.DEVICE_DRIVE_C;

		  // if there are no more parameters, error ...
		  if( !it.hasNext() )
			  throw new SyntaxErrorException();
	    	
		  // is the mode being specified?
		  if( it.peekAtNext().equals( "FOR" ) ) {
			  // skip FOR keyword
			  it.next();	  
			  
			  // read the mode
			  final String modeString = JBasicTokenUtil.nextToken( it );
			  
			  // determine which mode it is
			  if( ( mode = MODES2.indexOf( modeString ) ) == -1 &&  
					  ( mode = MODES1.indexOf( modeString ) ) == -1 )
				  throw new SyntaxErrorException();
		  }
    		
		  // is the access being specified?
		  if( it.peekAtNext().equals( "ACCESS" ) ) {
			  // skip ACCESS keyword
			  it.next();
			  // read access value
			  if( ( access = ACCESS.indexOf( JBasicTokenUtil.nextToken( it ) ) ) == -1 )
				  throw new SyntaxErrorException();
		  }
    		
		  // check for lock usage
		  if( ( lock = LOCKS.indexOf( it.peekAtNext() ) ) != -1 ) {
			  it.next();
		  }	    		
    		
		  // must specify "AS"
		  JBasicTokenUtil.mandateToken( it, "AS" );
    		
		  // get the file handle
		  JBasicTokenUtil.mandateToken( it, "#" );
		  handleID = (int)JBasicTokenUtil.nextNumericToken( it );
    		
		  // is the length being specified?
		  if( it.peekAtNext().equals( "LEN" ) ) {
			  // skip LEN keyword
			  it.next();
			  // must specify equals (=)
			  JBasicTokenUtil.mandateToken( it, "=" );	    			
			  // get the record length
			  recordLength = GwBasicValues.getValue( it );
		  }	    			    
	  }
	  
	  /**
	   * Returns the variable record length 
	   * @param compiledCode the given {@link JBasicCompiledCode compiled code}
	   * @return the variable record length
	   * @throws JBasicException
	   */
	  private int getRecordLength( final JBasicCompiledCode compiledCode ) 
	  throws JBasicException {
		  // get the record length object
		  final MemoryObject object = recordLength.getValue( compiledCode );
		  
		  // the object must be numeric
		  if( !object.isNumeric() ) {
			  throw new TypeMismatchException( object );
		  }
		  
		  // return the length
		  final int length = object.toInteger();
		  return length;
	  }

}