package jbasic.app.ide;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.JTextComponent;
import javax.swing.text.LayeredHighlighter;
import javax.swing.text.Position;
import javax.swing.text.View;

import com.ldaniels528.tokenizer.Token;
import com.ldaniels528.tokenizer.Tokenizer;
import com.ldaniels528.tokenizer.TokenizerContext;
import com.ldaniels528.tokenizer.parsers.DoubleQuotedTextTokenParser;
import com.ldaniels528.tokenizer.parsers.EndOfLineTokenParser;
import com.ldaniels528.tokenizer.parsers.NumericTokenParser;
import com.ldaniels528.tokenizer.parsers.OperatorTokenParser;
import com.ldaniels528.tokenizer.parsers.TextTokenParser;


/**
 * JBasic Editor Syntax Highlighter
 * @author lawrence.daniels@gmail.com
 */
public class JBasicSyntaxHightlighter {	
	private final UnderlineHighlightPainter painter;
	private final DefaultHighlighter highlighter;
	private final Tokenizer tokenizer;	
	private final JTextPane textComp;
	
	/**
	 * Creates an instance of this syntax highlighter
	 * @param textComp the given {@link JTextComponent text component}
	 */
	public JBasicSyntaxHightlighter( JTextPane textComp ) {
		this.textComp 		= textComp;	
		this.highlighter		= new DefaultHighlighter();
		this.painter			= new UnderlineHighlightPainter( Color.RED );
		this.tokenizer		= createTokenizer();	
		
		textComp.setHighlighter(highlighter);
		highlighter.install( textComp );
	}
	
	/**
	 * Updates the highlighting on the underlying
	 * text component.
	 */
	public void updateAll() {
		try {
			// remove the current highlights
			//highlighter.removeAllHighlights();
			
			// get the text
			final String text = textComp.getText();
			
			// tokenize the text and add highlights
		 	TokenizerContext context = tokenizer.parse( text );
		 	Token token;
		 	while( ( token = tokenizer.nextToken( context ) ) != null ) {
		 		switch( token.getType() ) {
		 			case Token.QUOTED_TEXT:
		 				highlighter.addHighlight( token.getStart(), token.getEnd(), painter );
		 				break;
		 				
		 			case Token.NUMERIC:
		 				//highlighter.addHighlight( token.getStart(), token.getEnd(), painter );
		 				break;
		 		}
		 	}
		 	
		 	textComp.updateUI();
		}
		catch( Exception e ) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates a custom tokenizer for parsing text
	 * @return a custom {@link Tokenizer tokenizer} for parsing text
	 */
	private Tokenizer createTokenizer() {
		final Tokenizer tokenizer = new Tokenizer();
		tokenizer.add( new EndOfLineTokenParser() );	  
		tokenizer.add( new NumericTokenParser() );
		tokenizer.add( new OperatorTokenParser() );
		tokenizer.add( new DoubleQuotedTextTokenParser() );
		tokenizer.add( new TextTokenParser() );
		return tokenizer;
	}
	
	/**
	 * Underlining Highlight Painter
	 */
	private static class UnderlineHighlightPainter extends LayeredHighlighter.LayerPainter {
		private Color color;
		
		public UnderlineHighlightPainter( Color color ) {
			this.color = color;
		}

		@Override
		public Shape paintLayer( Graphics g, int offs0, int offs1, Shape bounds, JTextComponent c, View view ) {
			Rectangle rect = null;
			if (offs0 == view.getStartOffset() && offs1 == view.getEndOffset() ) {
				if( bounds instanceof Rectangle ) {
					rect = (Rectangle)bounds;
				} else {
					rect = bounds.getBounds();
				}
			} 
			else {
				try {
					final Shape shape = view.modelToView(offs0, Position.Bias.Forward, offs1, Position.Bias.Backward, bounds);
					rect = ( shape instanceof Rectangle ) ? (Rectangle)shape : shape.getBounds();
				} 
				catch (BadLocationException e) {
					e.printStackTrace();
					return null;
				}
			}	
			
			final FontMetrics fm = c.getFontMetrics(c.getFont());
			final int baseline = rect.y + rect.height - fm.getDescent() + 1;
			
			g.setColor( color );
			g.drawLine( rect.x, baseline, rect.x + rect.width, baseline);
			g.drawLine( rect.x, baseline + 1, rect.x + rect.width, baseline + 1);
			return rect;
		}

		public void paint(Graphics g, int offs0, int offs1, Shape shape, JTextComponent c ) {
			// never gets called						
		}
	}

}
