package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.registers;

import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.Operand;

/**
 * JBasic Assembly Language Registe
 * @author lawrence.daniels@gmail.com
 */
public interface X86Register extends Operand {
	
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.ibmpc.devices.cpu.opcode.X86Operand#get()
	 */
	public int get();
	
	/* 
	 * (non-Javadoc)
	 * @see org.ldaniels528.javapc.ibmpc.devices.cpu.opcode.X86Operand#set(int)
	 */
	public void set( int value );
	
	/**
	 * Adds the given delta value to this register's current value
	 * @param delta the given delta value
	 * @return the new value of this register
	 */
	public int add( int delta );
	
	/**
	 * Performs a logical AND between the register's value
	 * and the given masking value.
	 * @param value the given masking value
	 */
	public void and( int mask );
	
	/**
	 * Performs a logical OR between the register's value
	 * and the given masking value.
	 * @param value the given masking value
	 */
	public void or( int mask );
	
	/**
	 * Shifts the register's value to the right by 
	 * the given number of bits.
	 * @param bits the given number of bits
	 */
	public void lshift( int bits );
	
	/**
	 * Shifts the register's value to the right by 
	 * the given number of bits.
	 * @param bits the given number of bits
	 */
	public void rshift( int bits );
	
	/**
	 * @return the index of this register
	 */
	public int getIndex();
	
	/** 
	 * Sets the given bit #
	 * @param bitNum the given bit number (1-16)
	 * @param on true = 1 or false = 0
	 */
	public void setBit( int bitNum, boolean on );
	
}