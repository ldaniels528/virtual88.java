package org.ldaniels528.javapc.jbasic.app.ide;


import java.awt.Component;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;

import org.ldaniels528.javapc.jbasic.common.program.OpCode;

/**
 * JBasic Explorer
 * @author lawrence.daniels@gmail.com
 */
@SuppressWarnings("serial")
public class JBasicExplorer extends JTree {
	private final Collection<OpCode> opCodes;
	
	/**
	 * Default constructor
	 * @param frame the given {@link JBasicIDEFrame frame}
	 */
	public JBasicExplorer( JBasicIdeContext context ) {
		this.opCodes = new LinkedList<OpCode>();
		super.setCellRenderer( new CellRenderer() );
		super.addTreeSelectionListener( new TreeSelectionHandler() );
		updateNodes();
	}
	
	/**
	 * Update the tree
	 * @param source the given source code
	 */
	public void updateTree( OpCode[] theOpCodes ) {				
		// clear the current opCodes
		opCodes.clear();
		opCodes.addAll( Arrays.asList( theOpCodes ) );
		
		// update nodes
		updateNodes();
	}
	
	/**
	 * Update the tree nodes
	 */
	private void updateNodes() {
		// construct the root of the tree
		final DefaultMutableTreeNode root = new DefaultMutableTreeNode( "program" );
		
		// attach all other objects to the root
		for( OpCode opCode : opCodes ) {
			root.add( new DefaultMutableTreeNode( opCode ) );
		}
		super.setModel( new DefaultTreeModel( root ) );
	}
	
	/**
	 * Custom Tree Cell Renderer
	 * @author lawrence.daniels@gmail.com
	 */
	private class CellRenderer extends DefaultTreeCellRenderer  {
		
		  /**
		    * Configures the renderer based on the passed in components.
		    * The value is set from messaging the tree with
		    * <code>convertValueToText</code>, which ultimately invokes
		    * <code>toString</code> on <code>value</code>.
		    * The foreground color is set based on the selection and the icon
		    * is set based on on leaf and expanded.
		    */
		  public Component getTreeCellRendererComponent( JTree tree,
		                                                 Object value,
		                                                 boolean sel,
		                                                 boolean expanded,
		                                                 boolean leaf,
		                                                 int row,
		                                                 boolean hasFocus ) {
		    // allow the parent to update itself
		    super.getTreeCellRendererComponent( tree, value, sel, expanded, leaf, row, hasFocus );

		    // get node and user object
		    final DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;

		    // let's figure out what it is...
		    final Object object = node.getUserObject();
		    if( object instanceof String ) {
		    		String string = (String)object;
		    		if( string.equals( "program" ) ) 
		    			super.setIcon( JBasicImageManager.PROGRAM );
		    }
		    else if( object instanceof OpCode ) {
		    		super.setIcon( JBasicImageManager.OPCODE );
		    }
		    return this;
		  }
		
	}
	
	/**
	 * TreeSelectionHandler manages tree selection changes
	 */
	private class TreeSelectionHandler implements TreeSelectionListener {

		public void valueChanged( TreeSelectionEvent e ) {
	    	
	    }
	}

}
