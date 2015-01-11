package org.ldaniels528.javapc.jbasic.app.ide;

/**
 * Context for communication between the JBasic IDE objects
 * @author lawrence.daniels@gmail.com
 */
public class JBasicIdeContext {
	private final JBasicMenuManager menuManager;
	private final JBasicExplorer explorer;
	private final JBasicEditor editor;
	
	/**
	 * Default constructor
	 */
	public JBasicIdeContext() {
		this.editor 		= new JBasicEditor( this );
		this.explorer 	= new JBasicExplorer( this );
		this.menuManager = new JBasicMenuManager( this );
	}

	public JBasicEditor getEditor() {
		return editor;
	}


	public JBasicExplorer getExplorer() {
		return explorer;
	}


	public JBasicMenuManager getMenuManager() {
		return menuManager;
	}

	
	
}
