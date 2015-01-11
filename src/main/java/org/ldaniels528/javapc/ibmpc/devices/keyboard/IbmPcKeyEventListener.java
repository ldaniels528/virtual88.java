package org.ldaniels528.javapc.ibmpc.devices.keyboard;

import java.awt.event.KeyEvent;

/**
 * JavaPC Key Pressed Event Listener
 * @author lawrence.daniels@gmail.com
 */
public interface IbmPcKeyEventListener {

	/**
	 * Called when a registered key is pressed
	 * @param keyboard the given {@link IbmPcKeyboard scanner}
	 * @param event the {@link KeyEvent key event} containing a reference
	 * to the pressed key.
	 */
	void keyPressed( IbmPcKeyboard keyboard, KeyEvent event );
	
	/**
	 * Called when a registered key is pressed then released
	 * @param keyboard the given {@link IbmPcKeyboard scanner}
	 * @param event the {@link KeyEvent key event} containing a reference
	 * to the pressed key.
	 */
	void keyReleased( IbmPcKeyboard keyboard, KeyEvent event );
	
}
