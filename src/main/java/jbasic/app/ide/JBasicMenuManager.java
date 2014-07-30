package jbasic.app.ide;

import java.awt.Component;
import java.awt.Menu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileFilter;

/**
 * JBasic Menu Manager
 * @author lawrence.daniels@gmail.com
 */
@SuppressWarnings("serial")
public class JBasicMenuManager extends JMenuBar {
	// edit menu icons
	public static final Icon COPY 		= JBasicImageManager.COPY;
	public static final Icon CUT			= JBasicImageManager.CUT;
	public static final Icon PASTE 		= JBasicImageManager.PASTE;
	// file menu icons
	public static final Icon NEW 			= JBasicImageManager.NEW;
	public static final Icon OPEN			= JBasicImageManager.OPEN;
	public static final Icon SAVE 		= JBasicImageManager.SAVE;
	public static final Icon SAVE_ALL		= JBasicImageManager.SAVE_ALL;
	public static final Icon SAVE_AS 		= JBasicImageManager.SAVE_AS;
	// program menu icons
	public static final Icon COMPILE 		= JBasicImageManager.COMPILE;
	public static final Icon DEBUG 		= JBasicImageManager.DEBUG;
	public static final Icon RUN	 		= JBasicImageManager.RUN;
	public static final Icon STOP	 		= JBasicImageManager.STOP;
	// general constants
	private static final File DEFAULT_DIRECTORY = new File( "." );
	// internal fields
	private final FileFilter fileFilter = new JbFileFilter(); 
	private final JBasicEditor editor;
	
	/////////////////////////////////////////////////////
	//      Constructor(s)
	/////////////////////////////////////////////////////
	
	/**
	 * Default constructor
	 * @param context the given {@link JBasicIdeContext frame}
	 */
	public JBasicMenuManager( JBasicIdeContext context ) {
		this.editor	= context.getEditor();
		
		// add menus
		super.add( createFileMenu() );	
		super.add( createEditMenu() );	
		super.add( createProgramMenu() );
	}
	
	/////////////////////////////////////////////////////
	//      Menu Creation Method(s)
	/////////////////////////////////////////////////////

	/**
	 * Creates the "File" Menu
	 * @return the {@link Menu menu}
	 */
	private JMenu createFileMenu() {
		JMenu menu = new JMenu( "File" );
		menu.add( createItem( "New", NEW, new FileNewHandler() ) );
		menu.add( createItem( "Open", OPEN, new FileOpenHandler() ) );
		menu.add( createItem( "Close", null, new FileCloseHandler() ) );
		menu.add( createItem( "Close All", null, new FileCloseHandler() ) );
		menu.add( createItem( "Save", SAVE, new FileSaveHandler() ) );
		menu.add( createItem( "Save As ...", SAVE_AS, new FileSaveHandler() ) );
		menu.add( createItem( "Save All", SAVE_ALL, new FileSaveHandler() ) );
		return menu;
	}
	
	/**
	 * Creates the "Edit" Menu
	 * @return the {@link Menu menu}
	 */
	private JMenu createEditMenu() {
		JMenu menu = new JMenu( "Edit" );
		menu.add( createItem( "Cut", CUT, new EditCutHandler() ) );
		menu.add( createItem( "Copy", COPY, new EditCopyHandler() ) );
		menu.add( createItem( "Paste", PASTE, new EditPasteHandler() ) );
		return menu;
	}
	
	/**
	 * Creates the "Program" Menu
	 * @return the {@link Menu menu}
	 */
	private JMenu createProgramMenu() {
		JMenu menu = new JMenu( "Program" ); 
		menu.add( createItem( "Debug", DEBUG, new ProgramDebugHandler() ) );
		menu.add( createItem( "Run", RUN, new ProgramRunHandler() ) );
		menu.add( createItem( "Stop", STOP, new ProgramStopHandler() ) );
		return menu;
	}
	
	/////////////////////////////////////////////////////
	//      File Menu Action Classes(s)
	/////////////////////////////////////////////////////
	
	/** 
	 * File Close Handler
	 */
	private class FileCloseHandler implements ActionListener {

		/* 
		 * (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent event ) {
			// TODO Auto-generated method stub			
		}		
	}
	
	/**
	 * File New Handler 
	 */
	private class FileNewHandler implements ActionListener {

		/* 
		 * (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed( ActionEvent event ) {			
			final File file = openFileDialog( JBasicMenuManager.this, null );
			if( !file.exists() ) {
				
			}
		}		
	}
	
	/**
	 * File Open Handler 
	 */
	private class FileOpenHandler implements ActionListener {

		/* 
		 * (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed( ActionEvent event ) {			
			final File file = openFileDialog( JBasicMenuManager.this, DEFAULT_DIRECTORY );
			if( file != null )
				editor.loadFile( file );
		}		
	}
	
	/** 
	 * File Save Handler 
	 */
	private class FileSaveHandler implements ActionListener {

		/* 
		 * (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent event ) {
			final File file = saveFileDialog( JBasicMenuManager.this, DEFAULT_DIRECTORY );
			if( file != null );
		}		
	}
	
	/**
	 * JBasic File Filter
	 */
	private class JbFileFilter extends FileFilter {

		@Override
		public boolean accept( File file ) {			
			return file.isDirectory() || 
					file.getName().toUpperCase().endsWith( ".BAS" );
		}

		@Override
		public String getDescription() {
			return "Basic Sources (.bas)";
		}		
	}
	
	/////////////////////////////////////////////////////
	//      Edit Menu Action Classes(s)
	/////////////////////////////////////////////////////
	
	/** 
	 * Edit Cut Handler
	 */
	private class EditCutHandler implements ActionListener {

		/* 
		 * (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent event ) {
			// TODO Auto-generated method stub			
		}		
	}
	
	/** 
	 * Edit Copy Handler
	 */
	private class EditCopyHandler implements ActionListener {

		/* 
		 * (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent event ) {
			// TODO Auto-generated method stub			
		}		
	}
	
	/** 
	 * Edit Paste Handler
	 */
	private class EditPasteHandler implements ActionListener {

		/* 
		 * (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent event ) {
			// TODO Auto-generated method stub			
		}		
	}
	
	/////////////////////////////////////////////////////
	//      Program Menu Action Classes(s)
	/////////////////////////////////////////////////////
	
	/** 
	 * Program Debug Handler
	 */
	private class ProgramDebugHandler implements ActionListener {

		/* 
		 * (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent event ) {
			// TODO Auto-generated method stub			
		}		
	}
	
	/** 
	 * Program Run Handler
	 */
	private class ProgramRunHandler implements ActionListener {

		/* 
		 * (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent event ) {
			editor.run();		
		}		
	}
	
	/** 
	 * Program Stop Handler
	 */
	private class ProgramStopHandler implements ActionListener {

		/* 
		 * (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent event ) {
			// TODO Auto-generated method stub			
		}		
	}
	
	/////////////////////////////////////////////////////
	//      Utility Method(s)
	/////////////////////////////////////////////////////
	
	private JMenuItem createItem( String label, Icon icon, ActionListener listener ) {
		JMenuItem menuItem = new JMenuItem( label );
		menuItem.addActionListener( listener );
		menuItem.setIcon( icon );
		return menuItem;
	}	

	private File openFileDialog( Component comp, File file ) {
		// create a .BAS file chooser
	    JFileChooser chooser = new JFileChooser( ( file != null ) ? file.getParentFile() : DEFAULT_DIRECTORY );
	    chooser.setFileFilter( fileFilter );

	    // check result of file chosen
	    int returnVal = chooser.showOpenDialog( comp );
	    return ( returnVal == JFileChooser.APPROVE_OPTION ) ? chooser.getSelectedFile() : null;
	}

	private File saveFileDialog( Component comp, File file ) {
	    // create a .BAS file chooser
	    JFileChooser chooser = new JFileChooser( ( file != null ) ? file.getParentFile() : DEFAULT_DIRECTORY );
	    chooser.setFileFilter( fileFilter );

	    // check result of file chosen
	    int returnVal = chooser.showSaveDialog( comp );
	    return ( returnVal == JFileChooser.APPROVE_OPTION ) ? chooser.getSelectedFile() : null;
	}
	
}