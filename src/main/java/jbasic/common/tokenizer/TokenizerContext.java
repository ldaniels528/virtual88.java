package jbasic.common.tokenizer;

/**
 * Represents the context of a {@link Tokenizer Tokenizer}
 * @author lawrence.daniels@gmail.com
 */
public class TokenizerContext {
	public boolean ignoreWhiteSpace;
	public final String expr;
	public final char[] exprCh;
	public int position;
	public int lineNo;
	
	/**
	 * Creates an instance of this context
	 */
	protected TokenizerContext( final String text ) {
		this.expr     		= text;
	    this.exprCh   		= text.toCharArray();
	    this.position 		= 0;
	    this.lineNo   		= 1;
	    this.ignoreWhiteSpace = true;
	}
	  
}
