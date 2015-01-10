package ibmpc.instruction.element.registers;

/**
 * Represents an 8-bit 80x86 General Purpose Register Descriptors
 * @author lawrence.daniels@gmail.com
 */
public class X86RegisterRef8G extends X86RegisterRef {
	public static final X86RegisterRef8G AL = new X86RegisterRef8G( "AL", 12, 0, 0 );
	public static final X86RegisterRef8G CL = new X86RegisterRef8G( "CL", 13, 1, 1 );
	public static final X86RegisterRef8G DL = new X86RegisterRef8G( "DL", 14, 2, 2 );
	public static final X86RegisterRef8G BL = new X86RegisterRef8G( "BL", 15, 3, 3 );
	public static final X86RegisterRef8G AH = new X86RegisterRef8G( "AH", 16, 4, 4 );
	public static final X86RegisterRef8G CH = new X86RegisterRef8G( "CH", 17, 5, 5 );
	public static final X86RegisterRef8G DH = new X86RegisterRef8G( "DH", 18, 6, 6 );
	public static final X86RegisterRef8G BH = new X86RegisterRef8G( "BH", 19, 7, 7 );
	
	/**
	 * Creates a new x86 segment register descriptor
	 * @param name the given name of the register
	 * @param id the register's ID
	 * @param index the given index
	 * @param groupIndex the given index
	 */
	private X86RegisterRef8G( final String name, final int id, final int index, int groupIndex ) {
		super( name, id, index, groupIndex );
	}
	
	/* (non-Javadoc)
	 * @see ibmpc.assembly.encoder.registers.X86RegisterDescriptor#is8BitRegister()
	 */
	public boolean is8BitGeneralPurposeRegister() {
		return true;
	}
	
}
