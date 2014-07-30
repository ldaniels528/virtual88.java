package jbasic.gwbasic.program;

import ibmpc.system.IbmPcSystem;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import jbasic.common.exceptions.JBasicException;
import jbasic.common.exceptions.NeverShouldHappenException;
import jbasic.common.exceptions.SyntaxErrorException;
import jbasic.common.program.JBasicCompiledCode;
import jbasic.common.program.JBasicCompiler;
import jbasic.common.program.JBasicProgramStatement;
import jbasic.common.program.JBasicSourceCode;
import jbasic.common.program.OpCode;
import jbasic.gwbasic.GwBasicEnvironment;
import jbasic.gwbasic.program.commands.BeepOp;
import jbasic.gwbasic.program.commands.GwBasicCommand;
import jbasic.gwbasic.program.commands.NoOp;
import jbasic.gwbasic.program.commands.PreprocessibleOpCode;
import jbasic.gwbasic.program.commands.RandomizeOp;
import jbasic.gwbasic.program.commands.RemarkOp;
import jbasic.gwbasic.program.commands.console.ClearScreenOp;
import jbasic.gwbasic.program.commands.console.ColorOp;
import jbasic.gwbasic.program.commands.console.InputOp;
import jbasic.gwbasic.program.commands.console.LineInputOp;
import jbasic.gwbasic.program.commands.console.LocateOp;
import jbasic.gwbasic.program.commands.console.PCopyOp;
import jbasic.gwbasic.program.commands.console.PrintOp;
import jbasic.gwbasic.program.commands.console.ScreenOp;
import jbasic.gwbasic.program.commands.console.WidthOp;
import jbasic.gwbasic.program.commands.control.EndOp;
import jbasic.gwbasic.program.commands.control.ForOp;
import jbasic.gwbasic.program.commands.control.GosubOp;
import jbasic.gwbasic.program.commands.control.GotoOp;
import jbasic.gwbasic.program.commands.control.IfOp;
import jbasic.gwbasic.program.commands.control.NextOp;
import jbasic.gwbasic.program.commands.control.ResumeOp;
import jbasic.gwbasic.program.commands.control.ReturnOp;
import jbasic.gwbasic.program.commands.control.RunOp;
import jbasic.gwbasic.program.commands.control.WhileEndOp;
import jbasic.gwbasic.program.commands.control.WhileOp;
import jbasic.gwbasic.program.commands.events.OnErrorOp;
import jbasic.gwbasic.program.commands.events.OnKeyOp;
import jbasic.gwbasic.program.commands.events.OnOp;
import jbasic.gwbasic.program.commands.graphics.DrawCircleOp;
import jbasic.gwbasic.program.commands.graphics.DrawLineOp;
import jbasic.gwbasic.program.commands.graphics.DrawOp;
import jbasic.gwbasic.program.commands.graphics.GetImageOp;
import jbasic.gwbasic.program.commands.graphics.PaintOp;
import jbasic.gwbasic.program.commands.graphics.PreSetOp;
import jbasic.gwbasic.program.commands.graphics.PutImageOp;
import jbasic.gwbasic.program.commands.graphics.WindowOp;
import jbasic.gwbasic.program.commands.ide.AddProgramLineOp;
import jbasic.gwbasic.program.commands.ide.DeleteOp;
import jbasic.gwbasic.program.commands.ide.EditOp;
import jbasic.gwbasic.program.commands.ide.KeyLabelOp;
import jbasic.gwbasic.program.commands.ide.KeyOp;
import jbasic.gwbasic.program.commands.ide.ListOp;
import jbasic.gwbasic.program.commands.ide.NewOp;
import jbasic.gwbasic.program.commands.ide.TraceOffOp;
import jbasic.gwbasic.program.commands.ide.TraceOnOp;
import jbasic.gwbasic.program.commands.io.AssignmentOp;
import jbasic.gwbasic.program.commands.io.CommonOp;
import jbasic.gwbasic.program.commands.io.DataOp;
import jbasic.gwbasic.program.commands.io.DimOp;
import jbasic.gwbasic.program.commands.io.EraseOp;
import jbasic.gwbasic.program.commands.io.PokeCommand;
import jbasic.gwbasic.program.commands.io.ReadOp;
import jbasic.gwbasic.program.commands.io.RestoreOp;
import jbasic.gwbasic.program.commands.io.SwapOp;
import jbasic.gwbasic.program.commands.io.device.CloseOp;
import jbasic.gwbasic.program.commands.io.device.LockOp;
import jbasic.gwbasic.program.commands.io.device.OpenOp;
import jbasic.gwbasic.program.commands.io.device.PrintDevOp;
import jbasic.gwbasic.program.commands.io.device.UnlockOp;
import jbasic.gwbasic.program.commands.io.file.BloadOp;
import jbasic.gwbasic.program.commands.io.file.BsaveOp;
import jbasic.gwbasic.program.commands.io.file.ChdirOp;
import jbasic.gwbasic.program.commands.io.file.FilesOp;
import jbasic.gwbasic.program.commands.io.file.KillOp;
import jbasic.gwbasic.program.commands.io.file.LoadOp;
import jbasic.gwbasic.program.commands.io.file.MkdirOp;
import jbasic.gwbasic.program.commands.io.file.NameOp;
import jbasic.gwbasic.program.commands.io.file.RmdirOp;
import jbasic.gwbasic.program.commands.io.file.SaveOp;
import jbasic.gwbasic.program.commands.system.AssemblyOp;
import jbasic.gwbasic.program.commands.system.DefDblOp;
import jbasic.gwbasic.program.commands.system.DefFnOp;
import jbasic.gwbasic.program.commands.system.DefIntOp;
import jbasic.gwbasic.program.commands.system.DefSegOp;
import jbasic.gwbasic.program.commands.system.DefSngOp;
import jbasic.gwbasic.program.commands.system.DefStrOp;
import jbasic.gwbasic.program.commands.system.DefUsrOp;
import jbasic.gwbasic.program.commands.system.OptionBaseOp;
import jbasic.gwbasic.program.commands.system.ShellOp;
import jbasic.gwbasic.program.commands.system.SystemOp;
import jbasic.gwbasic.values.GwBasicValues;

import com.ldaniels528.tokenizer.TokenIterator;
import com.ldaniels528.tokenizer.Tokenizer;
import com.ldaniels528.tokenizer.TokenizerContext;
import com.ldaniels528.tokenizer.parsers.DoubleQuotedTextTokenParser;
import com.ldaniels528.tokenizer.parsers.EndOfLineTokenParser;
import com.ldaniels528.tokenizer.parsers.NumericTokenParser;
import com.ldaniels528.tokenizer.parsers.OperatorTokenParser;
import com.ldaniels528.tokenizer.parsers.TextTokenParser;

/**
 * BASICA/GWBASIC Statement Compiler
 * @author lawrence.daniels@gmail.com
 */
@SuppressWarnings("unchecked")
public class GwBasicCompiler implements JBasicCompiler {
	private static final GwBasicCompiler instance = new GwBasicCompiler();
	// constants
	private static final String EQUALS = "=";
	private static final String BLANK = "";	
	private static final Set ON_OFF = new HashSet( Arrays.asList( new String[] { "ON", "OFF" } ) );
	private final Tokenizer tokenizer;  
	
	/**
	 * Default constructor
	 */
	private GwBasicCompiler() {
		this.tokenizer	= getConfiguredTokenizer();   
	}
	
	/**
	 * Returns the singleton compiler instance
	 * @return the singleton {@link GwBasicCompiler compiler instance}
	 */
	public static GwBasicCompiler getInstance() {
		return instance;
	}

	/* 
	 * (non-Javadoc)
	 * @see jbasic.common.program.JBasicCompiler#compile(com.ldaniels528.tokenizer.TokenIterator)
	 */
	public OpCode compile( final TokenIterator it ) 
	throws JBasicException {
		// failsafe
		if( !it.hasNext() ) return new NoOp();
  
		// get the first token from the statement
		final String token1 = it.next();
		final String token2 = it.hasNext() ? it.peekAtNext() : BLANK;
		
		// interpret and return the opcode result
		return compile( token1, token2, it );
	}

	/* 
	 * (non-Javadoc)
	 * @see jbasic.common.program.JBasicCompiler#compile(jbasic.common.program.JBasicSourceCode)
	 */
	public JBasicCompiledCode compile( final JBasicSourceCode program ) 
	throws JBasicException {
	    // get the environment object
	    final GwBasicEnvironment environment = (GwBasicEnvironment)program.getEnvironment();
	    
		// create a compiled code object
		final GwBasicCompiledCode compiledCode = new GwBasicCompiledCode( environment );
	    
		// get the statements from the program
		final Collection<JBasicProgramStatement> statements = program.getStatements();
	    
	    // iterate the statements, compiling each statement into opCode(s)
	    for( final JBasicProgramStatement statement : statements ) {
	    	  try {
	    		  // track the line number of the first opCode of each statement
	    		  final String lineNumber = String.valueOf( statement.getLineNumber() );
	    		  compiledCode.addLabel( lineNumber );
			      
			      // compile the opCodes
			      final GwBasicStatement gwStmt = (GwBasicStatement)statement;
			      final List<GwBasicCommand> opCodeSet = compile( gwStmt, environment, compiledCode );
			      
			      // compile the statement and capture the resultant opCode(s)
			      compiledCode.addAll( opCodeSet );
	    	  }
	    	  catch( final JBasicException e ) {
	    		  throw new GwBasicProgramSyntaxException( e, statement.getLineNumber() );
	    	  }
	    	  catch( final Exception e ) {
	    		  throw new NeverShouldHappenException( e ); 
	    	  }
	    }

	    // return the compiled code
	    return compiledCode;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see jbasic.common.program.JBasicCompiler#compile(ibmpc.IbmPcEnvironment, jbasic.common.program.JBasicProgramStatement)
	 */
	public JBasicCompiledCode compile( final IbmPcSystem environment, 
									   final JBasicProgramStatement statement )
	throws JBasicException {	    
		// cast to a GWBASIC environment
		final GwBasicEnvironment gwEnvironment = (GwBasicEnvironment)environment;
		
	    // create a reference to compile code
	    final GwBasicCompiledCode compiledCode = new GwBasicCompiledCode( gwEnvironment );
	    
		// retrieve the actual code from the statement
	    String code = statement.getCode();
	    
	    // if the code is not blank, evaluate it
	    while( ( code != null ) && !code.equals( "" ) ) {
	      // get the index of the end of the chunk
	      final int index = endOfChunk( code );

	      // get a chunk of code
	      final String chunk = code.substring( 0, index );

	      // tokenize the statement
	      final TokenIterator it = tokenize( chunk );

	      // translate the sections into a command/opCode
	      // is it a new statement (beginning with a line number)?
	      final GwBasicCommand command = GwBasicValues.isNumericConstant( it.peekAtNext() ) 
	      								? new AddProgramLineOp( it )
	      								: (GwBasicCommand)compile( it );

	      // execute the opCode
	      compiledCode.add( command );

	      // point to the next chunk
	      code = ( index+1 >= code.length() ) ? null : code.substring( index+1, code.length() );
	    }
	    
		return compiledCode;
	}
	
	/**
	 * Compile the given statement into a set of {@link GwBasicCommand opCodes}
	 * @param statement the given {@link JBasicProgramStatement statement}
	 * @param environment the given {@link IbmPcSystem environment}
	 * @param compiledCode the given {@link JBasicSourceCode program}
	 * @return a {@link List list} of {@link GwBasicCommand opCodes}
	 * @throws jbasic.common.exceptions.JBasicException
	 */
	private List<GwBasicCommand> compile( final GwBasicStatement statement, 
										  final IbmPcSystem environment, 
										  final JBasicCompiledCode compiledCode )
	throws JBasicException {
	    // get a copy of the statement
	    String code = statement.getCode();
	    
	    // create a container for return the opCodes
	    final List<GwBasicCommand> opCodes = new LinkedList<GwBasicCommand>();
	    
	    // compile the statment into opCodes
	    while( ( code != null ) && !code.equals( "" ) ) {
	    	  // trim off any comments
	       final int endOfComment = getCommentIndex( code );
	       if( endOfComment != -1 ) {
	      	  code = code.substring( 0, endOfComment );
	       }
	    	
	    	  // get the index of the end of the chunk
	      final int endOfChunk = endOfChunk( code );

	      // get a chunk of code
	      final String chunk = code.substring( 0, endOfChunk );     
	      
	      // tokenize the statement
	      final TokenIterator it = tokenize( chunk ); 

	      // compile the tokenized statement into a command/opCode
	      final GwBasicCommand command = (GwBasicCommand)compile( it );
	      command.setLineNumber( statement.getLineNumber()  );
	      
	      // pre-process the opCode            
	      if( command instanceof PreprocessibleOpCode ) {
	    	  	final PreprocessibleOpCode preprocessibleOpCode = (PreprocessibleOpCode)command;
	    	  	preprocessibleOpCode.preprocess( compiledCode );
	      }
	            
	      // add the opCode to our list of opCodes
	      if( !( command instanceof NoOp ) )
	    	  	opCodes.add( command );     

	      // point to the next chunk
	      code = ( endOfChunk+1 >= code.length() ) ? null : code.substring( endOfChunk+1, code.length() );
	    }
	    return opCodes;
	}
	
  /**
   * Translates the given token iterator into  
   * @param token1 the first token in the iterator
   * @param token2 the second token in the iterator
   * @param it the {@link TokenIterator token iterator}
   * @return the resultant {@link OpCode opcode}
   * @throws GwBasicProgramSyntaxException 
   */
  private GwBasicCommand compile( final String token1, 
		  					   	  final String token2, 
		  					   	  final TokenIterator it ) 
  throws JBasicException {
	  GwBasicCommand opCode = null;
	  
	  // get the first character of the first token
	  final char firstChar = token1.charAt( 0 );
	  
	  // if it's an assembly instruction ...
	  if( firstChar == '!' ) {
		  return new AssemblyOp( it );
	  }
	  
	  // if it's between 'A' and 'F' ...
	  else if( firstChar >= 'A' && firstChar <= 'F' ) {
		  if( ( opCode = compile_A_to_F( token1, token2, it ) ) != null )
			  return opCode;
	  }
	  
	  // if it's between 'G' and 'M' ...
	  else if( firstChar >= 'G' && firstChar <= 'M' ) {
		  if( ( opCode = compile_G_to_M( token1, token2, it ) ) != null )
			  return opCode;
	  }
	  
	  // if it's between 'N' and 'P' ...
	  else if( firstChar >= 'N' && firstChar <= 'P' ) {
		  if( ( opCode = compile_N_to_P( token1, token2, it ) ) != null )
			  return opCode;
	  }
	  
	  // if it's between 'R' and 'Z' ...
	  else if( firstChar >= 'R' && firstChar <= 'Z' ) {
		  if( ( opCode = compile_R_to_Z( token1, token2, it ) ) != null )
			  return opCode;
	  }
	  
	  // is it an assignment?
	  if( it.contains( EQUALS ) ) {
		  it.unGet( token1 );
		  return new AssignmentOp( it );
	  }
	  
	  // not recognized
	 throw new SyntaxErrorException();
  }
  
  /**
   * Compiles all instructions that start with 'A' thru 'F'
   * @param token1 the first token in the iterator
   * @param token2 the second token in the iterator
   * @param it the {@link TokenIterator token iterator}
   * @throws JBasicException 
   */
  private GwBasicCommand compile_A_to_F( final String token1, 
		  							  	 final String token2, 
		  							  	 final TokenIterator it ) 
  throws JBasicException {
	// BEEP
	if( token1.equals( "BEEP" ) ) return new BeepOp( it );
    // BLOAD
    else if( token1.equals( "BLOAD" ) ) return new BloadOp( it );
    // BSAVE
    else if( token1.equals( "BSAVE" ) ) return new BsaveOp( it );
    // CHDIR
    else if( token1.equals( "CHDIR" ) ) return new ChdirOp( it );
    // CIRCLE
    else if( token1.equals( "CIRCLE" ) ) return new DrawCircleOp( it );
    // CLOSE
    else if( token1.equals( "CLOSE" ) ) return new CloseOp( it );
    // CLS
    else if( token1.equals( "CLS" ) ) return new ClearScreenOp( it );
    // COLOR
    else if( token1.equals( "COLOR" ) ) return new ColorOp( it );
    // COMMON
    else if( token1.equals( "COMMON" ) ) return new CommonOp( it );
    // DATA
    else if( token1.equals( "DATA" ) ) return new DataOp( it );
	// DEF FN/SEG/USR
    else if( token1.equals( "DEF" ) ) {
		if( token2.equals( "FN") ) return new DefFnOp( it );
		else if( token2.equals( "SEG") ) return new DefSegOp( it );
		else if( token2.startsWith( "USR") ) return new DefUsrOp( it );
    }
	// DEF[DBL,INT,SNG,STR]
	else if( token1.equals( "DEFDBL" ) ) return new DefDblOp( it );
	else if( token1.equals( "DEFINT" ) ) return new DefIntOp( it );
	else if( token1.equals( "DEFSNG" ) ) return new DefSngOp( it );
	else if( token1.equals( "DEFSTR" ) ) return new DefStrOp( it );
    // DELETE
    else if( token1.equals( "DELETE" ) ) return new DeleteOp( it );
    // DIM
    else if( token1.equals( "DIM" ) ) return new DimOp( it );
    // DRAW
    else if( token1.equals( "DRAW" ) ) return new DrawOp( it );
    // EDIT
    else if( token1.equals( "EDIT" ) ) return new EditOp( it );
    // END
    else if( token1.equals( "END" ) ) return new EndOp( it );
    // ERASE
    else if( token1.equals( "ERASE" ) ) return new EraseOp( it );
    // FILES
    else if( token1.equals( "FILES" ) ) return new FilesOp( it );
    // FOR
    else if( token1.equals( "FOR" ) ) return new ForOp( it );
	// Unknown
	return null;
  }
	
  /**
   * Compiles all instructions that start with 'G' thru 'M'
   * @param token1 the first token in the iterator
   * @param token2 the second token in the iterator
   * @param it the {@link TokenIterator token iterator}
   * @throws JBasicException 
   */
  private GwBasicCommand compile_G_to_M( final String token1, 
		  							  	 final String token2, 
		  							  	 final TokenIterator it ) 
  throws JBasicException {
    // GET
    if( token1.equals( "GET" ) ) return new GetImageOp( it );
    // GOSUB
    else if( token1.equals( "GOSUB" ) ) return new GosubOp( it );
    // GOTO
    else if( token1.equals( "GOTO" ) ) return new GotoOp( it );
    // IF
    else if( token1.equals( "IF" ) ) return new IfOp( it );
    // INPUT
    else if( token1.equals( "INPUT" ) ) return new InputOp( it );
    // KEY
    else if( token1.equals( "KEY" ) ) {
    		// KEY ON|OFF
    		if( ON_OFF.contains( token2 ) ) return new KeyOp( it );
    		// KEY 1, "FILES"
    		else return new KeyLabelOp( it );
    }
    // KILL
    else if( token1.equals( "KILL" ) ) return new KillOp( it );
    // LET    
    else if( token1.equals( "LET" ) ) return new AssignmentOp( it );
    // LINE
    else if( token1.equals( "LINE" ) ) {
    			// LINE INPUT
    			if( token2.equals( "INPUT" ) ) return new LineInputOp( it );
    			// LINE (graphics)
    			else return new DrawLineOp( it );
    }
    // LIST
    else if( token1.equals( "LIST" ) ) return new ListOp( it );
    // LOAD
    else if( token1.equals( "LOAD" ) ) return new LoadOp( it );
    // LOCATE
    else if( token1.equals( "LOCATE" ) ) return new LocateOp( it );
    // LOCK
    else if( token1.equals( "LOCK" ) ) return new LockOp( it );
    // MKDIR
    else if( token1.equals( "MKDIR" ) ) return new MkdirOp( it );	
	// not recognized
    return null;	
  }
  
  /**
   * Compiles all instructions that start with 'N' thru 'P'
   * @param token1 the first token in the iterator
   * @param token2 the second token in the iterator
   * @param it the {@link TokenIterator token iterator}
   * @throws JBasicException 
   */
  private GwBasicCommand compile_N_to_P( final String token1, 
		  							  	 final String token2, 
		  							  	 final TokenIterator it ) 
  throws JBasicException {
	// NAME
	if( token1.equals( "NAME" ) ) return new NameOp( it );
	// NEW
    else if( token1.equals( "NEW" ) ) return new NewOp( it );
    // NEXT
    else if( token1.equals( "NEXT" ) ) return new NextOp( it );
    // ON
    else if( token1.equals( "ON" ) ) {
		// ON ERROR GOTO ...
		if( token2.equals( "ERROR" ) ) return new OnErrorOp( it );
    		// ON KEY n GOSUB ...
		else if( token2.equals( "KEY" ) ) return new OnKeyOp( it );
    		// ON n GOTO/GOSUB ...
    		else return new OnOp( it );
    }
    // OPEN
    else if( token1.equals( "OPEN" ) ) return new OpenOp( it );
    // OPTION
    else if( token1.equals( "OPTION" ) ) return new OptionBaseOp( it );
	// PAINT
    else if( token1.equals( "PAINT" ) ) return new PaintOp( it );
	// PCOPY
    else if( token1.equals( "PCOPY" ) ) return new PCopyOp( it );
    // POKE
    else if( token1.equals( "POKE" ) ) return new PokeCommand( it );
    // PRESET/PSET
    else if( token1.equals( "PRESET" ) || 
    			token1.equals( "PSET" ) ) return new PreSetOp( it );
    // PRINT
    else if( token1.equals( "PRINT" ) ) {
    		// PRINT#
        if( token2.equals( "#" ) ) return new PrintDevOp( it );
        // PRINT
        else return new PrintOp( it );
    }
    // PUT
    else if( token1.equals( "PUT" ) ) return new PutImageOp( it );
	// Unknown
	return null;
  }
  
  /**
   * Compiles all instructions that start with 'R' thru 'Z'
   * @param token1 the first token in the iterator
   * @param token2 the second token in the iterator
   * @param it the {@link TokenIterator token iterator}
   * @throws JBasicException 
   */
  private GwBasicCommand compile_R_to_Z( final String token1, 
		  							  	 final String token2, 
		  							  	 final TokenIterator it ) 
  throws JBasicException {
    // RANDOMIZE
    if( token1.equals( "RANDOMIZE" ) ) return new RandomizeOp( it );
    // READ
    else if( token1.equals( "READ" ) ) return new ReadOp( it );
    // REM
    else if( token1.equals( "REM" ) ) return new RemarkOp( it );
    // RESTORE
    else if( token1.equals( "RESTORE" ) ) return new RestoreOp( it );
    // RESUME
    else if( token1.equals( "RESUME" ) ) return new ResumeOp( it );
    // RETURN
    else if( token1.equals( "RETURN" ) ) return new ReturnOp( it );
    // RMDIR
    else if( token1.equals( "RMDIR" ) ) return new RmdirOp( it );
    // RUN
    else if( token1.equals( "RUN" ) ) return new RunOp( it );
    // SAVE
    else if( token1.equals( "SAVE" ) ) return new SaveOp( it );
    // SCREEN
    else if( token1.equals( "SCREEN" ) ) return new ScreenOp( it );
    // SHELL
    else if( token1.equals( "SHELL" ) ) return new ShellOp( it );
    // SYSTEM
    else if( token1.equals( "SYSTEM" ) ) return new SystemOp( it );
    // SWAP
    else if( token1.equals( "SWAP" ) ) return new SwapOp( it );
    // TRON
    else if( token1.equals( "TRON" ) ) return new TraceOnOp( it );
    // TROFF
    else if( token1.equals( "TROFF" ) ) return new TraceOffOp( it );
    // UNLOCK
    else if( token1.equals( "UNLOCK" ) ) return new UnlockOp( it );
    // WHILE
    else if( token1.equals( "WHILE" ) ) return new WhileOp( it );
    // WIDTH
    else if( token1.equals( "WIDTH" ) ) return new WidthOp( it );
    // WINDOW    
    else if( token1.equals( "WINDOW" ) ) return new WindowOp( it );
    // WEND
    else if( token1.equals( "WEND" ) ) return new WhileEndOp( it );
    // Unknown
	return null;	
  }

  /**
   * Returns the index of the end of the code chunk
   * @param code the given source code block
   * @return the index of the end of the code chunk
   */
  private int endOfChunk( final String code ) {
    boolean inQuotes = false;
    final char[] chars = code.toCharArray();
    int i;
    for( i = 0; i < chars.length; i++ ) {
      if( chars[i] == '"' ) inQuotes = !inQuotes;
      if( !inQuotes && chars[i] == ':'  ) return i;
    }
    return i;
  }
  
  /**
   * Returns the index of the beginning of a 
   * code comment chunk
   * @param code the given source code block
   * @return the index of the beginning of the comment code chunk
   */
  private int getCommentIndex( final String code ) {
	boolean inQuotes = false;
    final char[] chars = code.toCharArray();
    int i;
    for( i = 0; i < chars.length; i++ ) {
      if( chars[i] == '"' ) inQuotes = !inQuotes;
      if( !inQuotes && chars[i] == '\''  ) return i;
    }
    return i;
  }
  
  /**
   * @return a GWBASIC configured tokenizer
   */
  private Tokenizer getConfiguredTokenizer() {
	  final Tokenizer tokenizer = new Tokenizer();
	  tokenizer.add( new EndOfLineTokenParser() );	  
	  tokenizer.add( new NumericTokenParser() );
	  tokenizer.add( new OperatorTokenParser() );
	  tokenizer.add( new DoubleQuotedTextTokenParser() );
	  tokenizer.add( new TextTokenParser() );
	  return tokenizer;
  }
  
  /**
   * Tokenizes the given text into sections
   * @param text the given {@link String text}
   * @return a {@link List list} of tokens
   */
  private TokenIterator tokenize( final String text ) {	  
	  final TokenizerContext context = tokenizer.parse( text );
	  return tokenizer.nextTokens( context );
  }

}
