package jbasic.common.tokenizer;

/**
 * Represents a parser for a specific type of token
 * @author lawrence.daniels@gmail.com
 */
public interface TokenParser {
	
	/**
	 * Extracts the specific type of token from the source string.
	 * @return the extracted token, or <code>null</code> if the current position
	 * points to a non-matching token.
	 * @param context the given {@link TokenizerContext context}
	 */
	public Token getToken( TokenizerContext context );

}
