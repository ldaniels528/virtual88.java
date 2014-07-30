package msdos.storage;

import ibmpc.devices.memory.MemoryObject;
import ibmpc.devices.storage.IbmPcStorageSystem;
import ibmpc.exceptions.IbmPcException;
import ibmpc.util.Logger;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * IBM PC DOS Storage Device (Hard disk, floppy, etc.)
 * @author lawrence.daniels@gmail.com
 */
public class MsDosStorageSystem implements IbmPcStorageSystem {
	private static int HANDLE_GENERATOR = 0;
	private final Map<Integer,MsDosFileHandle> handles;
	private File workingDirectory;
	private int defaultDrive;
	private boolean verify;

	/**
	 * Creates an instance of this storage device
	 */
	public MsDosStorageSystem() {
		this.handles 			= new HashMap<Integer,MsDosFileHandle>();
		this.workingDirectory	= new File(".");
		this.verify				= false;
	}

	// /////////////////////////////////////////////////////
	// File-related Method(s)
	// /////////////////////////////////////////////////////

	/*
	 * (non-Javadoc)
	 * @see ibmpc.devices.storage.IbmPcStorageDevice#getFiles()
	 */
	public File[] getFiles() {
		return workingDirectory.listFiles();
	}

	/* 
	 * (non-Javadoc)
	 * @see ibmpc.devices.storage.IbmPcStorageDevice#getCurrentdirectory()
	 */
	public File getCurrentdirectory() {
		return workingDirectory;
	}

	/**
	 * @return the defaultDrive
	 */
	public int getDefaultDrive() {
		return defaultDrive;
	}

	/**
	 * @param defaultDrive the defaultDrive to set
	 */
	public void setDefaultDrive( final int defaultDrive ) {
		this.defaultDrive = defaultDrive;
	}

	/* 
	 * (non-Javadoc)
	 * @see ibmpc.devices.storage.IbmPcStorageDevice#setCurrentdirectory(java.io.File)
	 */
	public boolean setCurrentdirectory( final File directory ) 
	throws IbmPcException {
		// if the directory doesn't exist, error
		if( !directory.exists() ) {
			return false;
		}

		// set the new working directory
		this.workingDirectory = directory;
		return true;
	}

	/* 
	 * (non-Javadoc)
	 * @see ibmpc.devices.storage.IbmPcStorageDevice#renameFile(java.io.File, java.io.File)
	 */
	public boolean renameFile( final String sourceFileName, final String targetFileName ) {
		final File sourceFile = new File( sourceFileName );
		final File targetFile = new File( targetFileName );
		return sourceFile.renameTo( targetFile );
	}

	// /////////////////////////////////////////////////////
	// File Handle-related Method(s)
	// /////////////////////////////////////////////////////

	/*
	 * (non-Javadoc)
	 * @see ibmpc.devices.storage.IbmPcStorageDevice#createFile(String,int)
	 */
	public MsDosFileHandle createFile( final String filename, final MsDosFileAttributes fileAttributes )
	throws IbmPcException {
		// create a file reference
		final File file = new File( filename );
		
		// get a handle to the file
		final MsDosFileHandle handle = createHandle( file );
		
		// open the handle for read/write access
		final MsDosFileAccessMode accessMode = MsDosFileAccessMode.decode( 0x10 );
		handle.open( accessMode );

		// return the handle
		return handle;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see ibmpc.devices.storage.IbmPcStorageSystem#openDevice(java.lang.String, msdos.storage.MsDosFileAccessMode)
	 */
	public MsDosFileHandle openDevice( final String filename, final MsDosFileAccessMode accessMode ) 
	throws IbmPcException {
		// create a file reference
		final File file = new File( filename );
		
		// create the file handle object for this file
		final MsDosFileHandle handle = createHandle( file );

		// open the file via the file handle object
		handle.open( accessMode );
		
		// return the handle
		return handle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jbasic.program.JBasicProgram#closeDevice(int)
	 */
	public boolean closeDevice( final int handleID ) 
	throws IbmPcException {
		// lookup the file handle ID
		final MsDosFileHandle handle = lookupHandle( handleID );

		// remove the handle from this program
		handles.remove( handleID );

		// close the file via the file handle object
		handle.close();
		return true;
	}

	/* 
	 * (non-Javadoc)
	 * @see ibmpc.devices.storage.IbmPcStorageDevice#closeAllDevices()
	 */
	public void closeAllDevices() 
	throws IbmPcException {
		for( final MsDosFileHandle handle : handles.values() ) {
			closeDevice( handle.getHandleID() );
		}
		handles.clear();
	}

	/* 
	 * (non-Javadoc)
	 * @see ibmpc.devices.storage.IbmPcStorageDevice#readFromDevice(int, byte[], int)
	 */
	public int readFromDevice( final int handleID, final byte[] block, final int length ) 
	throws IbmPcException {
		// the handle must exist
		if( !handles.containsKey( handleID ) )
			throw new MsDosFileHandleNotFoundException( handleID );

		// read the block of data from the device
		final int count = 0; // TODO add read code here

		// return
		return count;
	}

	/**
	 * Resets the storage system
	 */
	public void reset() {
		// TODO do something here in the future
	}
	
	/**
	 * Turns on/off verify mode for the storage system
	 * @param state indicates whether verify should be 
	 * on (<tt>true</tt>) or off (<tt>false</tt>)
	 */
	public void setVerify( final boolean state ) {
		this.verify = state;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see ibmpc.devices.storage.IbmPcStorageSystem#writeToDevice(int, byte[])
	 */
	public int writeToDevice( final int handleID, final byte[] dataBlock )
	throws IbmPcException {
		// lookup the file handle ID
		final MsDosFileHandle handle = lookupHandle( handleID );
		
		Logger.info( "writeToDevice: Writing %d bytes to handle %04X (%s)\n", 
				dataBlock.length, handleID, handle.getFile().getAbsolutePath() );
		
		// write the data block to the device
		handle.write( dataBlock );	
		return 0;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see ibmpc.devices.storage.IbmPcStorageDevice#writeToDevice(int, java.util.Collection)
	 */
	public int writeToDevice( final int handleID, final Collection<MemoryObject> values ) 
	throws IbmPcException {
		// lookup the file handle ID
		final MsDosFileHandle handle = lookupHandle( handleID );

		// write the block to data to the device
		int count = 0;
		for( final MemoryObject value : values ) {
			// write the object to the device
			handle.write( value );
			count++;
		}

		return count;
	}

	/**
	 * Returns a new handle for the given file
	 * @param file the given {@link File file}
	 * @return the new {@link MsDosFileHandle file handle}
	 */
	private MsDosFileHandle createHandle( final File file ) {
		// get the next handle ID
		final int handleID = ++HANDLE_GENERATOR;
		
		// create the file handle
		final MsDosFileHandle fileHandle = new MsDosFileHandle( file, handleID ); 
		
		// record this handle in the mapping
		handles.put( handleID, fileHandle );

		// return the handle ID
		return fileHandle;
	}
	
	/**
	 * Returns the handle object associated to the given handle ID
	 * @param handleID the given handle ID
	 * @return the resultant {@link MsDosFileHandle file handle}
	 * @throws MsDosFileHandleNotFoundException
	 */
	private MsDosFileHandle lookupHandle( final int handleID ) 
	throws MsDosFileHandleNotFoundException {
		// the handle must exist
		if( !handles.containsKey( handleID ) ) 
			throw new MsDosFileHandleNotFoundException( handleID );

		// lookup the file handle ID
		return handles.get( handleID );
	}


}
