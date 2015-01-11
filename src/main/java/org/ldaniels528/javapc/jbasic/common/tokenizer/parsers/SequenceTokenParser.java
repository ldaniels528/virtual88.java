package org.ldaniels528.javapc.jbasic.common.tokenizer.parsers;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import org.ldaniels528.javapc.jbasic.common.tokenizer.SimpleToken;
import org.ldaniels528.javapc.jbasic.common.tokenizer.Token;
import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenParser;
import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenizerContext;


public class SequenceTokenParser implements TokenParser {
	private Collection<TokenSequence> sequences;
	
	/**
	 * Default Constructor
	 */
	public SequenceTokenParser() {
		this.sequences = new LinkedList<TokenSequence>();
	}
	
	/**
	 * Adds the given token sequence to this token parser
	 * @param sequence the given {@link TokenSequence token sequence}
	 */
	public void add( TokenSequence sequence ) {
		sequences.add( sequence );
	}

	/**
	 * Checks for token sequence i.e.  <% ... %>
	 * @param ctx the given {@link TokenizerContext tokenizer context}
	 * @return a {@link Token token} representing the {@link TokenSequence token sequence}
	 */
	public Token getToken( final TokenizerContext ctx ) {
	    for( final Iterator<TokenSequence> it = sequences.iterator(); it.hasNext(); ) {
	        final TokenSequence tms = (TokenSequence)it.next();
	        final int slen = tms.getStart().length();
	        final int elen = tms.getEnd().length();

	        if( ( ctx.position + slen - 1 < ctx.exprCh.length ) && ctx.expr.substring( ctx.position, ctx.position + slen ).equals( tms.getStart() ) ) {
	          final int start  = ctx.position;
	          while( ( ctx.position + elen < ctx.exprCh.length ) && !ctx.expr.substring( ctx.position, ctx.position + elen ).equals( tms.getEnd() ) ) ctx.position++;
	          ctx.position += slen;
	          
	          // return the token
	          return new SimpleToken(
	            ctx.expr.substring( start, ctx.position ),
	            tms.getTokenType(),
	            start,
	            ctx.position > ctx.exprCh.length ? ctx.exprCh.length : ctx.position,
	            ctx.lineNo
	          );
	        }
	      }
	      return null;
	}

}
