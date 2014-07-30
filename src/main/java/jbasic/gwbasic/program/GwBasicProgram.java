package jbasic.gwbasic.program;

import ibmpc.system.IbmPcSystem;

import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

import jbasic.common.program.JBasicProgramStatement;
import jbasic.common.program.JBasicSourceCode;

/**
 * Represents a GWBASICA/BASICA memory resident program. 
 * @author lawrence.daniels@gmail.com
 */
public class GwBasicProgram implements JBasicSourceCode {	
	// line number comparator
	private static final GwBasicStatementComparator comparator = new GwBasicStatementComparator();
	// fields    
	private final SortedSet<JBasicProgramStatement> statements;
	private final IbmPcSystem environment;

	///////////////////////////////////////////////////////
	//      Constructor(s)
	///////////////////////////////////////////////////////
  
  /**
   * Creates a BASICA/GWBASIC compatible {@link JBasicSourceCode program}
   */
  @SuppressWarnings("unchecked")
  public GwBasicProgram( final IbmPcSystem environment ) {
	  this.environment	= environment;
	  this.statements 	= new TreeSet<JBasicProgramStatement>( comparator );	  	    
  }
  
  
  ///////////////////////////////////////////////////////
  //      Integrated Development Environment (IDE) Method(s)
  ///////////////////////////////////////////////////////
  
  /* 
   * (non-Javadoc)
   * @see jbasic.common.program.JBasicCompiledCode#add(JBasicProgramStatement)
   */
  public void add( final JBasicProgramStatement stmt ) {
	  GwBasicStatement statement = (GwBasicStatement)stmt;
	  // if the statement already exists, remove it
	  if( statements.contains( statement ) ) statements.remove( statement );
	  
	  // add the new statement
	  statements.add( statement );
  }
  
  /* 
   * (non-Javadoc)
   * @see jbasic.common.program.JBasicSourceCode#clear()
   */
  public void clear() {  
	  statements.clear();
  }
  
  /* 
   * (non-Javadoc)
   * @see jbasic.common.program.JBasicCompiledCode#list()
   */
  public SortedSet<JBasicProgramStatement> getStatements() {
	  return statements;
  }
  
  /**
   * Returns the statements from the program that is currently resides in memory
   * @param range the range of {@link JBasicProgramStatement statements} to return
   * @return the {@link Collection collection} of statements that make up this program
   */
  public Collection<JBasicProgramStatement> getStatements( final int ... range ) {
	  GwBasicStatement stmtA = new GwBasicStatement( range[0], null ); 
	  GwBasicStatement stmtB = new GwBasicStatement( range[1], null );
	  return statements.subSet( stmtA, stmtB );
  }
  
  /**
   * Removes the given range of statements from this proram
   * @param range the given range of statements
   */
  public boolean removeStatements( final int ... range ) {
	  GwBasicStatement stmtA = new GwBasicStatement( range[0], null ); 
	  GwBasicStatement stmtB = new GwBasicStatement( range[1], null );
	  Collection<JBasicProgramStatement> stmts = statements.subSet( stmtA, stmtB );
	  return statements.remove( stmts );
  }
  
 /* 
  * (non-Javadoc)
  * @see jbasic.common.program.JBasicSourceCode#getEnvironment()
  */
  public IbmPcSystem getEnvironment() { // TODO remove this method
	  return environment; 
  }
   

}