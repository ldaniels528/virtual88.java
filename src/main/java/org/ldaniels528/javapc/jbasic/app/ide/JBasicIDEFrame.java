package org.ldaniels528.javapc.jbasic.app.ide;

import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

/**
 * JBasic IDE Frame
 * @author lawrence.daniels@gmail.com
 */
@SuppressWarnings("serial")
public class JBasicIDEFrame extends JFrame {		
	
	/**
	 * Default Constructor
	 */
	public JBasicIDEFrame( String title, JBasicIdeContext context ) {
		super( title );
		
		// create a split window view
		final JSplitPane splitPane = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT );
		splitPane.setDividerLocation( 200 );
		splitPane.add( new JScrollPane( context.getExplorer() ) );
		splitPane.add( new JScrollPane( context.getEditor() ) );
		
		// create content pane
		final JPanel contentPane = new JPanel( new GridLayout(), true );
		contentPane.add( splitPane );
		
		// complete the setup of this frame
		super.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		super.setJMenuBar( context.getMenuManager() );
		super.setContentPane( contentPane );
		super.pack();
		super.setSize( getScreenSize( this ) );
		super.setVisible( true );
		super.setResizable( true );
	}

	/**
	 * Determines the dimension necessary to provide a fullscreen view
	 * @param frame the frame to measure
	 * @return the {@link Dimension dimension} necessary to provide a fullscreen view 
	 */
	private Dimension getScreenSize( JFrame frame ) {
		// get screen dimension
		Dimension size = frame.getToolkit().getScreenSize();

		// get screen insets
		GraphicsEnvironment ge    = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice [] gda     = ge.getScreenDevices();
		GraphicsConfiguration gc  = gda[0].getDefaultConfiguration();
		Insets insets             = frame.getToolkit().getScreenInsets( gc );

		// adjust window to screen size
		final int width   = (int)( size.getWidth() - ( insets.left + insets.right ) );
		final int heigth  = (int)( size.getHeight() - ( insets.top + insets.bottom ) );
		return new Dimension( width, heigth );
	}
	
}
