package msdos.storage;


/**
 * Represents IBM PC DOS File Attributes
 * <pre>
 * Valid file attributes
 * 5 4 3 2 1 0
 * | | | | | |
 * | | | | | +---- 1 = read only
 * | | | | +----- 1 = hidden
 * | | | +------ 1 = system
 * | +--------- not used for this call
 * +---------- 1 = archive
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 */
public class MsDosFileAttributes {
    private final boolean isArchive;
    private final boolean isSystem;
    private final boolean isHidden;
    private final boolean isReadOnly;

    /**
     * Creates a new file attribute
     *
     * @param archive  indicates whether the file is archive
     * @param system   indicates whether the file is system
     * @param hidden   indicates whether the file is hidden
     * @param readOnly indicates whether the file is read-only
     */
    private MsDosFileAttributes(final boolean archive,
                                final boolean system,
                                final boolean hidden,
                                final boolean readOnly) {
        this.isArchive = archive;
        this.isSystem = system;
        this.isHidden = hidden;
        this.isReadOnly = readOnly;
    }

    public static MsDosFileAttributes decode(final int attributes) {
        // decode the attributes
        final boolean isArchive = (attributes & 0x20) > 0; // 100000
        final boolean isSystem = (attributes & 0x08) > 0; // 001000
        final boolean isHidden = (attributes & 0x02) > 0; // 000010
        final boolean isReadOnly = (attributes & 0x01) > 0; // 000001

        // return the instance
        return new MsDosFileAttributes(isArchive, isSystem, isHidden, isReadOnly);
    }

    /**
     * @return the isArchive
     */
    public boolean isArchive() {
        return isArchive;
    }

    /**
     * @return the isHidden
     */
    public boolean isHidden() {
        return isHidden;
    }

    /**
     * @return the isReadOnly
     */
    public boolean isReadOnly() {
        return isReadOnly;
    }

    /**
     * @return the isSystem
     */
    public boolean isSystem() {
        return isSystem;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return String.format("%s %s %s %s",
                isArchive ? "A" : "-",
                isHidden ? "H" : "V",
                isReadOnly ? "R" : "W",
                isSystem ? "S" : "N");
    }

}
