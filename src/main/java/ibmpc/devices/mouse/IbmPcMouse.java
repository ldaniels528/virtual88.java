/**
 * 
 */
package ibmpc.devices.mouse;

import ibmpc.devices.display.IbmPcDisplayFrame;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Represents an IBM PC Mouse
 * @author lawrence.daniels@gmail.com
 */
public class IbmPcMouse implements MouseListener {
	private int maximumX;
	private int maximumY;
	private int minimumX;
	private int minimumY;
	private int mouseX;
	private int mouseY;
	private int buttonStatus;
	private int pressCount;

	///////////////////////////////////////////////////////////////////
	//			Constructor(s)
	///////////////////////////////////////////////////////////////////
	
	/** 
	 * Default constructor
	 */
	public IbmPcMouse() {
		this.mouseX	= 0;
		this.mouseY	= 0;
	}
	
	///////////////////////////////////////////////////////////////////
	//			Accessor and Mutator Method(s)
	///////////////////////////////////////////////////////////////////
	
	/**
	 * @return Returns the buttonStatus.
	 */
	public int getButtonStatus() {
		return buttonStatus;
	}
	
	/**
	 * @return Returns the maximumX.
	 */
	public int getMaximumX() {
		return maximumX;
	}
	
	/**
	 * @param maximumX The maximumX to set.
	 */
	public void setMaximumX(int maximumX) {
		this.maximumX = maximumX;
	}
	
	/**
	 * @return Returns the maximumY.
	 */
	public int getMaximumY() {
		return maximumY;
	}
	
	/**
	 * @param maximumY The maximumY to set.
	 */
	public void setMaximumY(int maximumY) {
		this.maximumY = maximumY;
	}
	
	/**
	 * @return Returns the minimumX.
	 */
	public int getMinimumX() {
		return minimumX;
	}
	
	/**
	 * @param minimumX The minimumX to set.
	 */
	public void setMinimumX(int minimumX) {
		this.minimumX = minimumX;
	}
		
	/**
	 * @return Returns the minimumY.
	 */
	public int getMinimumY() {
		return minimumY;
	}
	
	/**
	 * @param minimumY The minimumY to set.
	 */
	public void setMinimumY(int minimumY) {
		this.minimumY = minimumY;
	}
	
	/**
	 * @return Returns the mouseX.
	 */
	public int getMouseX() {
		return mouseX;
	}
	
	/**
	 * @param mouseX The mouseX to set.
	 */
	public void setMouseX( int mouseX ) {
		this.mouseX = mouseX;
	}
	
	/**
	 * @return Returns the mouseY.
	 */
	public int getMouseY() {
		return mouseY;
	}
	
	/**
	 * @param mouseY The mouseY to set.
	 */
	public void setMouseY( int mouseY ) {
		this.mouseY = mouseY;
	}
	
	/**
	 * @return Returns the pressCount.
	 */
	public int getPressCount() {
		final int tempCount = pressCount;
		pressCount = 0;
		return tempCount;
	}
	
	///////////////////////////////////////////////////////////////////
	//			Service Method(s)
	///////////////////////////////////////////////////////////////////
	
	/**
	 * Initializes this mouse
	 * @param frame the given {@link IbmPcDisplayFrame frame}
	 */
	public void init( IbmPcDisplayFrame frame ) {
		frame.addMouseListener( this );
	}
	
	/** 
	 * Shows Mouse Cursor
	 */
	public void showCusror() {
		// TODO need to figure out how to do this
	}
	
	/** 
	 * Hides Mouse Cursor
	 */
	public void hideCursor() {
		// TODO need to figure out how to do this
	}
		
	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked( MouseEvent event ) {
		this.buttonStatus = event.getButton();	
		this.pressCount++;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent event) {
		// TODO Auto-generated method stub		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent event) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered(MouseEvent event) {
		this.mouseX = event.getX();
		this.mouseY	= event.getY();
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited(MouseEvent event) {
		// TODO Auto-generated method stub		
	}

}
