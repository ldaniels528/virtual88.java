package jbasic.assembler.instruction.element.registers;

import jbasic.assembler.instruction.element.X86DataElement;

/**
 * Represents an x86 register reference (descriptor)
 * @author lawrence.daniels@gmail.com
 */
public abstract class X86RegisterRef implements X86DataElement {
	private final String name;
	private final int id;
	private final int index;
	private final int groupIndex;
	
	/**
	 * Creates a new register descriptor
	 * @param name the name of the register
	 * @param id the register's ID
	 * @param index the given index
	 * @param groupIndex the given group index
	 */
	protected X86RegisterRef( final String name, final int id, final int index, final int groupIndex ) {
		this.name		= name;
		this.id			= id;
		this.index		= index;
		this.groupIndex = groupIndex;
	}
	
	
	/* (non-Javadoc)
	 * @see ibmpc.machinecode.encoder.X86DataElement#getSequence()
	 */
	public int getSequence() {
		return groupIndex;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see ibmpc.machinecode.encoder.X86DataElement#isMemoryReference()
	 */
	public boolean isMemoryReference() {
		return false;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see ibmpc.machinecode.encoder.X86DataElement#isRegister()
	 */
	public boolean isRegister() {
		return true;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see ibmpc.machinecode.encoder.X86DataElement#isValue()
	 */
	public boolean isValue() {
		return false;
	}
	
	/**
	 * Returns the name of the register
	 * @return the name of the register
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns the register's ID
	 * @return the register's ID
	 */
	public int getID() {
		return id;
	}
	
	/**
	 * Returns the index of the register
	 * @return the index of the register
	 */
	public int getIndex() {
		return index;
	}
	
	/**
	 * Returns the group index of the register
	 * @return the group index of the register
	 */
	public int getGroupIndex() {
		return groupIndex;
	}
	
	/**
	 * Returns the register class code (reg8=0Ah, reg16=0Bh, sreg16=0Eh)
	 * @return the register class code
	 */
	public int getClassCode() {
		if( is8BitGeneralPurposeRegister() ) return 0x02; 		// 2 = 0010
		else if( is16BitGeneralPurposeRegister() ) return 0x03; // 3 = 0011
		else return 0x06; 										// 6 = 0110
	}
	
	/**
	 * Indicates whether the register is 16-bits wide
	 * @return true, if the register is 16-bits wide
	 */
	public boolean is16BitRegister() {
		return false;
	}
	
	/**
	 * Indicates whether the register is 8-bits wide
	 * @return true, if the register is 8-bits wide
	 */
	public boolean is8BitGeneralPurposeRegister() {
		return false;
	}
	
	/**
	 * Indicates whether the register is general purpose
	 * @return true, if the register is general purpose
	 */
	public boolean is16BitGeneralPurposeRegister() {
		return false;
	}
	
	/**
	 * Indicates whether the register is a memory segment
	 * @return true, if the register is a memory segment
	 */
	public boolean is16BitSegmentRegister() {
		return false;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals( final Object o ) {
		return ( o instanceof X86RegisterRef ) &&
				( ((X86RegisterRef)o).getID() == id );
	}
	
	/* 
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return id;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return name;
	}

}
