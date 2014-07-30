package msdos.storage;



/**
 * Represents an MS-DOS File Access Mode
 * <pre>
 * Access modes (bits):
 * 7 6 5 4 3 2 1 0
 * | | | | | | | | 
 * | | | | | +-------- read/write/update access mode
 * | | | | +--------- reserved, always 0
 * | +-------------- sharing mode (see below) (DOS 3.1+)
 * +--------------- 1 = private, 0 = inheritable (DOS 3.1+)
 * 
 * Sharing mode bits (DOS 3.1+): Access mode bits:
 * 000 compatibility mode (exclusive) 000 read access
 * 001 deny others read/write access 001 write access
 * 010 deny others write access 010 read/write access
 * 011 deny others read access
 * 100 full access permitted to all
 * </pre>
 * @author lawrence.daniels@gmail.com
 */
public class MsDosFileAccessMode {
	// sharing mode constants
	public static final int SHARING_COMPATIBLITY_MODE 		= 0x00; // 000b
	public static final int SHARING_DENY_OTHERS_READ_WRITE	= 0x01; // 001b
	public static final int SHARING_DENY_OTHERS_WRITE		= 0x02; // 010b
	public static final int SHARING_DENY_OTHERS_READ		= 0x03; // 011b
	public static final int SHARING_FULL_ACCESS_TO_ALL		= 0x04; // 100b
	// interal fields
	private boolean readWrite;
	private boolean shared;
	private boolean isPrivate;
	private int sharingMode;
	private int mode;
	
	/**
	 * Creates a new file access mode instance
	 */
	protected MsDosFileAccessMode( final boolean readWrite, 
			   					   final boolean isPrivate,
								   final boolean shared, 
								   final int sharingMode,
								   final int mode ) {
		this.readWrite		= readWrite;
		this.isPrivate		= isPrivate;
		this.shared			= shared;
		this.sharingMode	= sharingMode;
		this.mode			= mode;
	}
	
	/**
	 * Decodes the given access mode bit array into an 
	 * MS DOS File Access Mode object
	 * @param accessMode the given access mode (bit array)
	 * @return an MS DOS File Access Mode object
	 */
	public static MsDosFileAccessMode decode( final int accessMode ) {
		// decode the access mode
		final boolean isPrivate = ( accessMode & 0x80 ) > 0;	// 1000 0000
		final boolean shared 	= ( accessMode & 0x40 ) > 0;	// 0100 0000
		final boolean readWrite = ( accessMode & 0x10 ) > 0;	// 0001 0000
		final int sharingMode 	= SHARING_FULL_ACCESS_TO_ALL;
		
		// return the access mode object
		return new MsDosFileAccessMode( readWrite, isPrivate, shared, sharingMode, accessMode );
	}

	/**
	 * @return the readWrite
	 */
	public boolean isReadWrite() {
		return readWrite;
	}
	
	/**
	 * Indicates whether the file is marked private or 
	 * conversly inheritable.
	 * @return true, if the file is marked private
	 */
	public boolean isPrivate() {
		return isPrivate;
	}

	/**
	 * @return the shared
	 */
	public boolean isShared() {
		return shared;
	}

	/**
	 * @return the sharingMode
	 */
	public int getSharingMode() {
		return sharingMode;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return String.format( "%s%s%s[%02X]", 
				( readWrite ? "rw" : "r " ),
				( isPrivate ? "pv" : "np" ),
				( shared    ? "sh" : "ns" ),
				mode
		);
	}

}
