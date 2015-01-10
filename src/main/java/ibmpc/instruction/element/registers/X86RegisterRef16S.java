package ibmpc.instruction.element.registers;

/**
 * Represents a 16-bit 80x86 Segment Register Descriptors
 * @author lawrence.daniels@gmail.com
 */
public class X86RegisterRef16S extends X86RegisterRef16 {
	// 16-bit segment registers
	public static final X86RegisterRef16S ES = new X86RegisterRef16S( "ES", 8, 0x00, 0 ); // extra segment register
	public static final X86RegisterRef16S CS = new X86RegisterRef16S( "CS", 9, 0x08, 1 ); // code segment register
	public static final X86RegisterRef16S SS = new X86RegisterRef16S( "SS", 10, 0x10, 2 ); // stack segment register
	public static final X86RegisterRef16S DS = new X86RegisterRef16S( "DS", 11, 0x18, 3 ); // data segment register
	
	/**
	 * Creates a new x86 segment register descriptor
	 * @param name the name of the segment register
	 * @param id the register's ID
	 * @param index the given index
	 * @param groupIndex the given group index
	 */
	private X86RegisterRef16S( final String name, final int id, final int index, final int groupIndex ) {
		super( name, id, index, groupIndex );
	}
	
	/* (non-Javadoc)
	 * @see ibmpc.assembly.encoder.registers.X86RegisterDescriptor#isSegmentRegister()
	 */
	public boolean is16BitSegmentRegister() {
		return true;
	}
	
}
