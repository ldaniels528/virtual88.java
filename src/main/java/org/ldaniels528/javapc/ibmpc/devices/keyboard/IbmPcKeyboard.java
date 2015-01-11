package org.ldaniels528.javapc.ibmpc.devices.keyboard;

import org.ldaniels528.javapc.ibmpc.devices.display.IbmPcDisplay;
import org.ldaniels528.javapc.ibmpc.devices.display.IbmPcDisplayFrame;
import org.apache.log4j.Logger;

import static java.lang.String.format;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Collection;
import java.util.LinkedList;

/**
 * IBM PC Style Console Input Device
 * @author lawrence.daniels@gmail.com
 */
public class IbmPcKeyboard implements KeyListener, IbmPcKeyConstants {
	private final Logger logger = Logger.getLogger(getClass());
	private static final int BUFFER_SIZE = 512;
	private final Collection<IbmPcKeyEventListener> keyListeners;
	private final IbmPcDisplay display;
	private final StringBuilder buffer;
	private boolean complete;
	private int keyFlags;
	
  /////////////////////////////////////////////////////
  //      Constructor(s)
  /////////////////////////////////////////////////////
  
  /**
   * Creates an instance of this scanner that operates on the given stream.
   * @param display the given {@link IbmPcDisplay display}
   */
  public IbmPcKeyboard( final IbmPcDisplay display ) {
    this.display		= display;
    this.buffer       	= new StringBuilder( BUFFER_SIZE );
    this.keyListeners	= new LinkedList<IbmPcKeyEventListener>();
  }
  
  /////////////////////////////////////////////////////
  //      Service Method(s)
  /////////////////////////////////////////////////////

  /**
   * Initializes the keyboard by registering the given frame
   * for keystore notifications.
   * @param frame the given {@link IbmPcDisplayFrame IBM PC display frame}
   */
  public void init( final IbmPcDisplayFrame frame ) {
	  frame.addKeyListener( this );
  }
  
  /**
   * Returns the 8-bit key flags value
   * <pre>
   *	7 6 5 4 3 2 1 0
   *	| | | | | | | | 
   *	| | | | | | | +---- right shift key depressed
   *	| | | | | | +----- left shift key depressed
   *	| | | | | +------ CTRL key depressed
   *	| | | | +------- ALT key depressed
   *	| | | +-------- scroll-lock is active
   *	| | +--------- num-lock is active
   *	| +---------- caps-lock is active
   *	+----------- insert is active
   * </pre>
   * @return the key flags
   */
  public int getKeyFlags() {
	  return keyFlags;
  }
  
  /**
   * Indicates whether keystrokes are waiting to be consumed
   * @return true, if at least one keystore is waiting in the buffer
   */
  public boolean keyStrokesWaiting() {
	  synchronized( buffer ) {
		  return buffer.length() > 0;
	  }
  }
  
  /**
   * Retrieves the next count of characters from the keyboard's buffer
   * @param maxChars the maximum number of characters to be read
   * @return a string containing the desired characters
   */
  public String next( final int maxChars ) {
		// start taking in input
	    synchronized( buffer ) {
			// set things up
			complete = false;
			
	    	logger.info(format("buffer size = %d", buffer.length()));
	    	
			// gather input until ENTER is pressed
			while( buffer.length() < maxChars ) {
				// wait until notified
				try { buffer.wait(); } catch( Exception e ) { }
			}

			// get the text from the buffer
			final String text = ( buffer.length() == maxChars )
								? buffer.toString()
								: buffer.substring( 0, maxChars );
			
			// clear the buffer
			if( buffer.length() > 0 )
				buffer.delete( 0, buffer.length() );
			
			// return the text to the caller
			return text;
	    }
  }
  
  /**
   * Retrieves the next character of input from the underlying stream
   * @return the next character
   */
  public int nextChar() {
		// start taking in input
	    synchronized( buffer ) {
	    	if( buffer.length() > 0 ) {
	    		final int ch = buffer.charAt( 0 );
	    		buffer.deleteCharAt( 0 );
	    		return ch;
	    	}
	    }
	    throw new IllegalStateException( "No keystrokes were waiting" );
  }
  
  /**
   * Retrieves the next line of input from the underlying stream
   * @return the next line of input
   */
  public String nextLine() {
	  return nextLine( null );
  }
  
  /**
   * Retrieves the next line of input from the underlying stream
   * @param initialText the initial text that will appear in this scanner
   * @return the next line of input
   */
  public String nextLine( final String initialText ) { 
	  return nextLine( initialText, BUFFER_SIZE );
  }
  
  /**
   * Retrieves the next line of input from the underlying stream
   * @param initialText the initial text that will appear in this scanner
   * @param maxChars the given number of characters to read
   * @return the next line of input
   */
  public String nextLine( final String initialText, final int maxChars ) {  
	// start taking in input
    synchronized( buffer ) {
		// set things up
		complete = false;
    		
		// append the initial text
		if( initialText != null )  {
			printString( initialText );
		}
	
		// gather input until ENTER is pressed
		while( ( buffer.length() < maxChars ) && !complete ) {
			// wait until notified
			try { buffer.wait(); } catch( Exception e ) { }
		}

		// get the text from the buffer
		final String text = buffer.toString();
		
		// clear the buffer
		if( buffer.length() > 0 )
			buffer.delete( 0, buffer.length() );
		
		// return the text to the caller
		return text;
    }
  }
  
  /**
   * Returns the next keystore from the buffer
   * without consuming it.
   * @return the next keystore from the buffer
   */
  public int peekAtNextKeyStore() {
	  synchronized( buffer ) {
		  if( buffer.length() > 0 ) {
			  return buffer.charAt( 0 );
		  }
		  else {
			  return 0;
		  }
	  }
  }

  /**
   * Registers the given listener to handle the event generated when the 
   * key that corresponds to the given key code is pressed.
   * @param listener the given {@link IbmPcKeyEventListener listener}
   */
  public void register( final IbmPcKeyEventListener listener ) {
	  keyListeners.add( listener );
  }  
  
  /**
   * Perform the "BACKSPACE" key expansion
   */
  public void sendBackSpaceKey() {
    synchronized( buffer ) {
      // remove the character at the cursor position, and backup by 1
      display.backspace();
      display.update();
      
      // update the buffer
      final int length = buffer.length();
      buffer.deleteCharAt( length -1 );
      buffer.notifyAll();
    }
  }

  /**
   * Perform the "ENTER" key expansion
   */
  public void sendEnterKey() {
    // indicate that this input session has ended
    complete = true;
    
    // add new line character
    synchronized( buffer ) {
      display.newLine();
      display.update();
      buffer.notifyAll();
    }
  }
  
  /**
   * Allows a caller to send text to this scanner
   * @param text the given ascii text
   */
  public void sendKeyStrokes( final String text ) {
	  printString( text );
  }
  
  /////////////////////////////////////////////////////
  //      Key Input Method(s)
  /////////////////////////////////////////////////////

  /**
   * Invoked when a key has been typed.
   * See the class description for {@link java.awt.event.KeyEvent} for a definition of
   * a key typed event.
   */
  public void keyTyped( final KeyEvent event ) {
	  // get the key character
	  final char keyChar = event.getKeyChar();
    
	  // determine what to do with it
	  switch( keyChar ) {
	  	case KeyEvent.VK_BACK_SPACE: sendBackSpaceKey(); break;
	  	case KeyEvent.VK_ENTER: sendEnterKey(); break;
	  	default: printCharacter( event.getKeyChar() );
	  }
  }

  /**
   * Invoked when a key has been pressed.
   * See the class description for {@link java.awt.event.KeyEvent} for a definition of
   * a key pressed event.
   */
  public void keyPressed( final KeyEvent event ) {	  
	  updateKeyFlags( event );
	  callBackKeyEventListeners( event );
  }

  /**
   * Invoked when a key has been released.
   * See the class description for {@link java.awt.event.KeyEvent} for a definition of
   * a key released event.
   */
  public void keyReleased( final KeyEvent event ) {
	  updateKeyFlags( event );
	  callBackKeyEventListeners( event );
  }
  
  /////////////////////////////////////////////////////
  //      Internal Service Method(s)
  /////////////////////////////////////////////////////

  /**
   * Calls back all registered listeners
   */
  private void callBackKeyEventListeners( final KeyEvent event ) {
	  for( final IbmPcKeyEventListener listener : keyListeners ) {
		  listener.keyPressed( this, event );
	  }
  }

  /**
   * Prints the text string
   * @param string the given text string
   */
  private void printString( final String string ) {
	  synchronized( buffer ) {
		  // write the character to the screen
		  display.write( string );
		  display.update();
        
		  // write the character to the buffer
		  buffer.append( string );
		  buffer.notifyAll();
	  }
  }
  
  /**
   * Prints a displayable character
   * @param keyChar the given character (ASCII Codes 32 - 127)
   */
  private void printCharacter( final char keyChar ) {
    // display only characters between ' ' and 'z'
    if( ( keyChar >= ' ' ) && ( keyChar <= 'z' ) ) {
    		// convert the key to an ascii character
    		final String ascii = String.valueOf( keyChar );      
    		printString( ascii );
    }
  }
  
  /**
   * Updates the key flags; storing the result at [0040:0017]
   * <pre>
   *	7 6 5 4 3 2 1 0
   *	| | | | | | | | 
   *	| | | | | | | +---- right shift key depressed
   *	| | | | | | +----- left shift key depressed
   *	| | | | | +------ CTRL key depressed
   *	| | | | +------- ALT key depressed
   *	| | | +-------- scroll-lock is active
   *	| | +--------- num-lock is active
   *	| +---------- caps-lock is active
   *	+----------- insert is active
   * </pre>
   * @param event the given {@link KeyEvent key event}
   */
  private void updateKeyFlags( final KeyEvent event ) {
	  // get the key code
	  final int keyCode = event.getKeyCode();
	  
	  // build the key flags bit array
	  keyFlags =
		  	// left/right shift key depressed	(mask = 0000 0011)
	  		( event.isShiftDown() ? 0x03 : 0x00 ) |
	  		
		  	// CTRL key depressed				(mask = 0000 0100)
	  		( event.isControlDown() ? 0x04 : 0x00 ) |
	  		
		  	// ALT key depressed				(mask = 0000 1000)
	  		( event.isAltDown() ? 0x08 : 0x00 ) |
	  		
		  	// scroll-lock key depressed		(mask = 0001 0000)
	  		( keyCode == KeyEvent.VK_SCROLL_LOCK ? 0x10 : 0x00 ) |
	  		
		  	// num-lock key depressed			(mask = 0010 0000)
	  		( keyCode == KeyEvent.VK_NUM_LOCK ? 0x20 : 0x00 ) |
	  		
		  	// caps-lock key depressed			(mask = 0100 0000)
	  		( keyCode == KeyEvent.VK_CAPS_LOCK ? 0x40 : 0x00 ) |
	  		
	  		// insert is active 				(mask = 1000 0000)
	  		( keyCode == KeyEvent.VK_INSERT ? 0x80 : 0x00 );
  }
  
}
