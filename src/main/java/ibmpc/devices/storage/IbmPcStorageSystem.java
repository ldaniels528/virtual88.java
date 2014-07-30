package ibmpc.devices.storage;

import ibmpc.devices.memory.MemoryObject;
import ibmpc.exceptions.IbmPcException;

import java.io.File;
import java.util.Collection;

import msdos.storage.MsDosFileAccessMode;
import msdos.storage.MsDosFileAttributes;
import msdos.storage.MsDosFileHandle;

/**
 * Represents a persistent storage system
 * @author lawrence.daniels@gmail.com
 */
public interface IbmPcStorageSystem {
	
	// /////////////////////////////////////////////////////
	// File-related Method(s)
	// /////////////////////////////////////////////////////

	/**
	 * Creates a new file
	 * @param filename the given file name
	 * @param fileAttrs the given file attributes
	 * @return a handle to the newly created file
	 * @throws IbmPcException
	 */
	MsDosFileHandle createFile( String filename, final MsDosFileAttributes fileAttributes ) 
	throws IbmPcException;
	
	/**
	 * @return the array of {@link java.io.File files} from the current directory
	 */
	File[] getFiles();

	/**
	 * @return the current working directory
	 */
	File getCurrentdirectory();

	/**
	 * Changes the current directory
	 * @param directory the given {@link File directory}
	 * @return true, if the current directory was successfully changed
	 */
	boolean setCurrentdirectory( File directory )  
	throws IbmPcException;

	/**
	 * Renames the source file name to the target file name
	 * @param sourceFileName the given {@link File source file}
	 * @param targetFileName the givewn {@link File target file}
	 * @return true, if the file was successfully renamed
	 */
	boolean renameFile( String sourceFileName, String targetFileName );
	
	// /////////////////////////////////////////////////////
	// File Handle-related Method(s)
	// /////////////////////////////////////////////////////

	/**
	 * Opens the given file and associates it to the given file handle
	 * @param filename the given {@link File file}
	 * @param accessMode the given {@link MsDosFileAccessMode access mode}
	 * @return the resultant {@link MsDosFileHandle file handle}
	 * @throws IbmPcException
	 */
	MsDosFileHandle openDevice(String filename, MsDosFileAccessMode accessMode )
	throws IbmPcException;

	/**
	 * Closes the file that is referenced via the given file handle
	 * @param fileHandle the given file handle
	 * @return TODO
	 * @throws IbmPcException
	 */
	boolean closeDevice(int fileHandle) throws IbmPcException;

	/**
	 * Closes all currently open devices
	 * @throws IbmPcException
	 */
	void closeAllDevices() throws IbmPcException;

	/**
	 * Reads a block of data from the file that is referenced via the given file
	 * handle
	 * @param fileHandle the given file handle
	 * @param block the block that the data will be returned within
	 * @param length the number of bytes to read from the file
	 * @return the count of bytes read from the file
	 * @throws IbmPcException
	 */
	int readFromDevice( int fileHandle, byte[] block, int length )
	throws IbmPcException;
	
	/**
	 * Writes the given collection of values to the device via the given file
	 * handle
	 * @param fileHandle the given file handle
	 * @param dataBlock the data block to write to the device
	 * @throws IbmPcException
	 */
	int writeToDevice( int fileHandle, byte[] dataBlock )
	throws IbmPcException;

	/**
	 * Writes the given collection of values to the device via the given file
	 * handle
	 * @param fileHandle the given file handle
	 * @param values the collection of values that are to be written to the device
	 * @throws IbmPcException
	 */
	int writeToDevice( int fileHandle, Collection<MemoryObject> values )
	throws IbmPcException;

}
