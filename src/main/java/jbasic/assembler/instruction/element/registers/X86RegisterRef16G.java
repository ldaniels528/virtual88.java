package jbasic.assembler.instruction.element.registers;

/**
 * Represents a 16-bit 80x86 General Purpose Register Descriptors
 * @author lawrence.daniels@gmail.com
 */
public class X86RegisterRef16G extends X86RegisterRef16 {
	// 16-bit general purpose registers
	public static final X86RegisterRef16G AX = new X86RegisterRef16G( "AX", 0, 0, 0 ); // accumulator register
	public static final X86RegisterRef16G CX = new X86RegisterRef16G( "CX", 1, 1, 1 ); // counter register
	public static final X86RegisterRef16G DX = new X86RegisterRef16G( "DX", 2, 2, 2 ); // data register
	public static final X86RegisterRef16G BX = new X86RegisterRef16G( "BX", 3, 3, 3 ); // base register
	
	// 16-bit indexer registers
	public static final X86RegisterRef16G SI = new X86RegisterRef16G( "SI", 4, 4, 4 ); // source register
	public static final X86RegisterRef16G DI = new X86RegisterRef16G( "DI", 5, 5, 5 ); // destination register
	
	// 16-bit stack pointer registers
	public static final X86RegisterRef16G SP = new X86RegisterRef16G( "SP", 6, 6, 6 ); // stack pointer register
	public static final X86RegisterRef16G BP = new X86RegisterRef16G( "BP", 7, 7, 7 ); // base pointer register
	
	/**
	 * Creates a new x86 segment register descriptor
	 * @param name the name of the register
	 * @param id the register's ID
	 * @param index the given index
	 * @param groupIndex the given group index
	 */
	private X86RegisterRef16G( final String name, final int id, final int index, final int groupIndex ) {
		super( name, id, index, groupIndex );
	}

	/* (non-Javadoc)
	 * @see ibmpc.assembly.encoder.registers.X86RegisterDescriptor#is16BitGeneralPurposeRegister()
	 */
	public boolean is16BitGeneralPurposeRegister() {
		return true;
	}
	
}
