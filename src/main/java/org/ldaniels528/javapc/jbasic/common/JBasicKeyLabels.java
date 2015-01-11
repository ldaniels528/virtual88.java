package org.ldaniels528.javapc.jbasic.common;

import org.ldaniels528.javapc.ibmpc.devices.keyboard.IbmPcKeyConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Represents the GWBASIC/BASICA on-screen key labels
 * @author lawrence.daniels@gmail.com
 */
public class JBasicKeyLabels implements IbmPcKeyConstants {
	private final List<String> keyLabels;
	private String labelText;
	private boolean active;
	private int columns;
	
	/**
	 * Default constructor
	 */
	public JBasicKeyLabels() {
		this.keyLabels 	= getDefaultKeyLabels();
		this.active	 	= true;
	}
	
	/**
	 * Retrieves the label for a specific index
	 * @param index the given label index (0-9)
	 * @return the label
	 */
	public String getLabel( final int index ) {
		return keyLabels.get( index );
	}
	
	/**
	 * Sets the label at a specific index
	 * @param index the given label index (0-9)
	 * @param label the new label
	 */
	public void setLabel( final int index, final String label ) {
		keyLabels.set( index, label );
	}
	
	/**
	 * Returns the label text
	 * @return the label text
	 */	
	public String getLabelText() {
		if( labelText == null ) 
			labelText = buildKeyLabelText();
		return labelText;
	}

	 /**
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}
	
	/**
	 * @param active the active to set
	 */
	public void setActive( final boolean active ) {
		this.active = active;
	}
	
	/**
	 * @param columns the columns to set
	 */
	public void setColumns( final int columns ) {
		this.columns 	= columns;
	}
	
	  ///////////////////////////////////////////////////////////////////
	  //			Internal Service Method(s)
	  ///////////////////////////////////////////////////////////////////
	  
	  /**
	   * @return the default key definitions
	   */
	  @SuppressWarnings("unchecked")
	  private List<String> getDefaultKeyLabels() {	  
		  final String[] DEFAULT_KEY_LABELS = {
			    "LIST", "RUN" + ESC, "LOAD\"", "SAVE\"", "CONT" + ESC,
			    ",\"LPT1\"", "TRON", "TROFF", "KEY", "SCREEN"
		  };
		  return new ArrayList( Arrays.asList( DEFAULT_KEY_LABELS ) );	  
	  }
	
	  /**
	   * Returns a blank string of the given length
	   * @param length the desired length of the string
	   * @return a blank {@link String string}
	   */
	  private String createBlankString( final int length ) {
		  final StringBuilder sb = new StringBuilder( length );
		  for( int n = 0; n < length; n++ ) {
			  sb.append( ' ' );
		  }
		  return sb.toString();
	  }
	  
	  /**
	   * Builds the key label text
	   * @return the key label text
	   */
	  private String buildKeyLabelText() {
		  // create a container for the text
		  final StringBuilder sb = new StringBuilder( columns );
		  
		  // calculate how much space each of the keys    
		  final int maxKeys = ( columns / 8 ) + ( columns % 8 > 0 ? 1 : 0 );
		    
		  // set the cell width
		  final int cellWidth = columns / maxKeys;
		  
		  // build the text characters
		  int n = 1;
		  for( Iterator<String> it = keyLabels.iterator(); it.hasNext() && n <= maxKeys; ) {
			  final String keyLabel = it.next();
			  sb.append( n );
			  sb.append( keyLabel );
			  
			  // pad the string
			  while( sb.length() < 78 && sb.length() < cellWidth * n ) sb.append( ' ' );
			  n++;
		  }
		  return sb.toString();
	  }

}
