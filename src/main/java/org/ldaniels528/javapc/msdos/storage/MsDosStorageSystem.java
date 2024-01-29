package org.ldaniels528.javapc.msdos.storage;

import org.apache.log4j.Logger;
import org.ldaniels528.javapc.ibmpc.devices.memory.MemoryObject;
import org.ldaniels528.javapc.ibmpc.devices.storage.IbmPcStorageSystem;
import org.ldaniels528.javapc.ibmpc.exceptions.IbmPcException;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;

/**
 * IBM PC DOS Storage Device (Hard disk, floppy, etc.)
 *
 * @author lawrence.daniels@gmail.com
 */
public class MsDosStorageSystem implements IbmPcStorageSystem {
    private static int HANDLE_GENERATOR = 0;
    private final Logger logger = Logger.getLogger(getClass());
    private final Map<Integer, MsDosFileHandle> handles;
    private int maximumHandleCount;
    private File workingDirectory;
    private int defaultDrive;
    private boolean verify;

    /**
     * Creates an instance of this storage device
     */
    public MsDosStorageSystem() {
        this.maximumHandleCount = 100;
        this.handles = new HashMap<>();
        this.workingDirectory = new File(".");
        this.verify = false;
    }

    // /////////////////////////////////////////////////////
    // File-related Method(s)
    // /////////////////////////////////////////////////////

    public int getMaximumHandleCount() {
        return maximumHandleCount;
    }

    public void setMaximumHandleCount(final int maximumHandleCount) {
        this.maximumHandleCount = maximumHandleCount;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public File[] getFiles() {
        return workingDirectory.listFiles();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public File getCurrentDirectory() {
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
    public void setDefaultDrive(final int defaultDrive) {
        this.defaultDrive = defaultDrive;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCurrentDirectory(final File directory) {
        // if the directory doesn't exist, error
        if (!directory.exists()) {
            return;
        }

        // set the new working directory
        this.workingDirectory = directory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean renameFile(final String sourceFileName, final String targetFileName) {
        final File sourceFile = new File(sourceFileName);
        final File targetFile = new File(targetFileName);
        return sourceFile.renameTo(targetFile);
    }

    // /////////////////////////////////////////////////////
    // File Handle-related Method(s)
    // /////////////////////////////////////////////////////

    /**
     * {@inheritDoc}
     */
    @Override
    public MsDosFileHandle createFile(final String filename,
                                      final MsDosFileAttributes fileAttributes) throws IbmPcException {
        // create a file reference
        final File file = new File(filename);

        // get a handle to the file
        final MsDosFileHandle handle = createHandle(file);

        // open the handle for read/write access
        final MsDosFileAccessMode accessMode = MsDosFileAccessMode.decode(0x10);
        handle.open(accessMode);

        // return the handle
        return handle;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MsDosFileHandle openDevice(final String filename,
                                      final MsDosFileAccessMode accessMode) throws IbmPcException {
        // create a file reference
        final File file = new File(filename);

        // create the file handle object for this file
        final MsDosFileHandle handle = createHandle(file);

        // open the file via the file handle object
        handle.open(accessMode);

        // return the handle
        return handle;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean closeDevice(final int handleID) throws IbmPcException {
        // lookup the file handle ID
        final MsDosFileHandle handle = lookupHandle(handleID);

        // remove the handle from this program
        handles.remove(handleID);

        // close the file via the file handle object
        handle.close();
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void closeAllDevices()
            throws IbmPcException {
        for (final MsDosFileHandle handle : handles.values()) {
            closeDevice(handle.getHandleID());
        }
        handles.clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int readFromDevice(final int handleID, final byte[] block, final int length) throws IbmPcException {
        // the handle must exist
        if (!handles.containsKey(handleID))
            throw new MsDosFileHandleNotFoundException(handleID);

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
     *
     * @param state indicates whether verify should be
     *              on (<tt>true</tt>) or off (<tt>false</tt>)
     */
    public void setVerify(final boolean state) {
        this.verify = state;
    }

    /*
     * (non-Javadoc)
     * @see org.ldaniels528.javapc.ibmpc.devices.storage.IbmPcStorageSystem#writeToDevice(int, byte[])
     */
    public int writeToDevice(final int handleID, final byte[] dataBlock)
            throws IbmPcException {
        // lookup the file handle ID
        final MsDosFileHandle handle = lookupHandle(handleID);

        logger.info(format("writeToDevice: Writing %d bytes to handle %04X (%s)",
                dataBlock.length, handleID, handle.getFile().getAbsolutePath()));

        // write the data block to the device
        handle.write(dataBlock);
        return 0;
    }

    /*
     * (non-Javadoc)
     * @see org.ldaniels528.javapc.ibmpc.devices.storage.IbmPcStorageDevice#writeToDevice(int, java.util.Collection)
     */
    public int writeToDevice(final int handleID, final Collection<MemoryObject> values)
            throws IbmPcException {
        // lookup the file handle ID
        final MsDosFileHandle handle = lookupHandle(handleID);

        // write the block to data to the device
        int count = 0;
        for (final MemoryObject value : values) {
            // write the object to the device
            handle.write(value);
            count++;
        }

        return count;
    }

    /**
     * Returns a new handle for the given file
     *
     * @param file the given {@link File file}
     * @return the new {@link MsDosFileHandle file handle}
     */
    private MsDosFileHandle createHandle(final File file) {
        // get the next handle ID
        final int handleID = ++HANDLE_GENERATOR;

        // create the file handle
        final MsDosFileHandle fileHandle = new MsDosFileHandle(file, handleID);

        // record this handle in the mapping
        handles.put(handleID, fileHandle);

        // return the handle ID
        return fileHandle;
    }

    /**
     * Returns the handle object associated to the given handle ID
     *
     * @param handleID the given handle ID
     * @return the resultant {@link MsDosFileHandle file handle}
     * @throws MsDosFileHandleNotFoundException
     */
    private MsDosFileHandle lookupHandle(final int handleID)
            throws MsDosFileHandleNotFoundException {
        // the handle must exist
        if (!handles.containsKey(handleID))
            throw new MsDosFileHandleNotFoundException(handleID);

        // lookup the file handle ID
        return handles.get(handleID);
    }

}
