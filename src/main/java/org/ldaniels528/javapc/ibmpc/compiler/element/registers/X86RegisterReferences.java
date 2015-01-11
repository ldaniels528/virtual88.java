package org.ldaniels528.javapc.ibmpc.compiler.element.registers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a collection of x86 register descriptors
 * @author lawrence.daniels@gmail.com
 */
public class X86RegisterReferences {
	
	///////////////////////////////////////////////////////////////////
	//			Register Enumeration(s)
	///////////////////////////////////////////////////////////////////
	
	// 16-bit general purpose registers
	public static final X86RegisterRef16G AX = X86RegisterRef16G.AX; // accumulator register
	public static final X86RegisterRef16G CX = X86RegisterRef16G.CX; // counter register
	public static final X86RegisterRef16G DX = X86RegisterRef16G.DX; // data register
	public static final X86RegisterRef16G BX = X86RegisterRef16G.BX; // base register
	
	// 16-bit indexer registers
	public static final X86RegisterRef16G SI = X86RegisterRef16G.SI; // source register
	public static final X86RegisterRef16G DI = X86RegisterRef16G.DI; // destination register
	public static final X86RegisterRef16G SP = X86RegisterRef16G.SP; // stack pointer register
	public static final X86RegisterRef16G BP = X86RegisterRef16G.BP; // base pointer register
	
	// 8-bit general purpose registers
	public static final X86RegisterRef8G AL = X86RegisterRef8G.AL; // lower half of AX
	public static final X86RegisterRef8G CL = X86RegisterRef8G.CL; // lower half of CX
	public static final X86RegisterRef8G DL = X86RegisterRef8G.DL; // lower half of DX
	public static final X86RegisterRef8G BL = X86RegisterRef8G.BL; // lower half of BX
	public static final X86RegisterRef8G AH = X86RegisterRef8G.AH; // upper half of AX
	public static final X86RegisterRef8G CH = X86RegisterRef8G.CH; // upper half of CX
	public static final X86RegisterRef8G DH = X86RegisterRef8G.DH; // upper half of DX
	public static final X86RegisterRef8G BH = X86RegisterRef8G.BH; // upper half of BX
	
	// 16-bit segment registers
	public static final X86RegisterRef16S ES = X86RegisterRef16S.ES; // extra segment register
	public static final X86RegisterRef16S CS = X86RegisterRef16S.CS; // code segment register
	public static final X86RegisterRef16S SS = X86RegisterRef16S.SS; // stack segment register
	public static final X86RegisterRef16S DS = X86RegisterRef16S.DS; // data segment register

	// target 16-bit segment registers
	public static final int DST_ES 			= 0x00;	// 00000 000
	public static final int DST_CS 			= 0x01;	// 00000 001
	public static final int DST_SS 			= 0x02;	// 00000 010
	public static final int DST_DS 			= 0x03;	// 00000 011
	// target 16-bit general purpose registers
	public static final int DST_AX 			= 0x00;	// 00000 000 
	public static final int DST_CX 			= 0x01;	// 00000 001
	public static final int DST_DX 			= 0x02;	// 00000 010
	public static final int DST_BX 			= 0x03;	// 00000 011
	public static final int DST_SP 			= 0x04;	// 00000 100
	public static final int DST_BP 			= 0x05;	// 00000 101
	public static final int DST_SI 			= 0x06;	// 00000 110
	public static final int DST_DI 			= 0x07;	// 00000 111
	// target 8-bit general purpose registers
	public static final int DST_AL 			= 0x00;	// 00000 000
	public static final int DST_CL 			= 0x01;	// 00000 001
	public static final int DST_DL 			= 0x02;	// 00000 010
	public static final int DST_BL 			= 0x03;	// 00000 011
	public static final int DST_AH 			= 0x04;	// 00000 100
	public static final int DST_CH 			= 0x05;	// 00000 101
	public static final int DST_DH 			= 0x06;	// 00000 110
	public static final int DST_BH 			= 0x07;	// 00000 111
	
	///////////////////////////////////////////////////////////////////
	//			Register Lookup Method(s)
	///////////////////////////////////////////////////////////////////
	
	/**
	 * Performs a register lookup by name
	 * @param registerName the name of the desired register 
	 * @return the requested {@link X86RegisterRef register} or <t>null</t> or if not found
	 */
	public static X86RegisterRef lookupRegister( final String registerName ) {
		return REGISTERS_ALL_MAPPING.get( registerName );
	}
	
	/**
	 * Performs a register lookup by name
	 * @param registerName the name of the desired register 
	 * @return the requested {@link X86RegisterRef8G register} or <t>null</t> or if not found
	 */
	public static X86RegisterRef8G lookupRegister8G( final String registerName ) {
		return REGISTERS_8G_MAPPING.get( registerName );
	}
	
	/**
	 * Performs an 8-bit general purpose register lookup by sequence (index)
	 * @param sequence the sequence of the desired register 
	 * @return the requested {@link X86RegisterRef8G register} or <t>null</t> or if not found
	 */
	public static X86RegisterRef8G lookupRegister8G( final int sequence ) {
		return ( sequence >= 0 && sequence <= 7 ) ? REGISTER_8G_LIST.get( sequence ) : null;
	}
	
	/**
	 * Performs a 16-bit general purpose or segment register lookup by name
	 * @param registerName the name of the desired register 
	 * @return the requested {@link X86RegisterRef16 register} or <t>null</t> or if not found
	 */
	public static X86RegisterRef16 lookupRegister16( final String registerName ) {
		return REGISTERS_16_MAPPING.get( registerName );
	}
	
	/**
	 * Performs a 16-bit general purpose register lookup by name
	 * @param registerName the name of the desired register 
	 * @return the requested {@link X86RegisterRef16G register} or <t>null</t> or if not found
	 */
	public static X86RegisterRef16G lookupRegister16G( final String registerName ) {
		return REGISTERS_16G_MAPPING.get( registerName );
	}
	
	/**
	 * Performs a 16-bit general purpose register lookup by sequence (index)
	 * @param sequence the sequence of the desired register 
	 * @return the requested {@link X86RegisterRef16G register} or <t>null</t> or if not found
	 */
	public static X86RegisterRef16G lookupRegister16G( final int sequence ) {
		return ( sequence >= 0 && sequence <= 7 ) ? REGISTER_16G_LIST.get( sequence ) : null;
	}
	
	/**
	 * Performs a 16-bit segment register lookup by name
	 * @param registerName the name of the desired register 
	 * @return the requested {@link X86RegisterRef16S register} or <t>null</t> or if not found
	 */
	public static X86RegisterRef16S lookupRegister16S( final String registerName ) {
		return REGISTERS_16S_MAPPING.get( registerName );
	}
	
	/**
	 * Performs a 16-bit segment register lookup by sequence (index)
	 * @param sequence the sequence of the desired register 
	 * @return the requested {@link X86RegisterRef16S register} or <t>null</t> or if not found
	 */
	public static X86RegisterRef16S lookupRegister16S( final int sequence ) {
		return ( sequence >= 0 && sequence <= 7 ) ? REGISTER_16S_LIST.get( sequence ) : null;
	}
	
	///////////////////////////////////////////////////////////////////
	//			Register Type Determination Method(s)
	///////////////////////////////////////////////////////////////////
	
	/**
	 * Indicates whether the given string represents
	 * a register (8-bit, 16-bit, or segment).
	 */
	public static boolean isRegister( final String registerName ) {
		return REGISTERS_ALL_MAPPING.containsKey( registerName );
	}
	
	/**
	 * Indicates whether the given string represents
	 * an 8-bit register
	 */
	public static boolean isRegister8G( final String s ) {
		return REGISTERS_8G_MAPPING.containsKey( s );
	}
	
	/**
	 * Indicates whether the given string represents
	 * a 16-bit register (general purpose or segment).
	 */
	public static boolean isRegister16( final String registerName ) {
		return REGISTERS_16_MAPPING.containsKey( registerName );
	}
		
	/**
	 * Indicates whether the given string represents
	 * a 16-bit general purpose register
	 */
	public static boolean isRegister16G( final String registerName ) {
		return REGISTERS_16G_MAPPING.containsKey( registerName );
	}
	
	/**
	 * Indicates whether the given string represents
	 * a 16-bit segment register
	 */
	public static boolean isRegister16S( final String registerName ) {
		return REGISTERS_16S_MAPPING.containsKey( registerName );
	}
	
	///////////////////////////////////////////////////////////////////
	//			Register Type Determination Data
	///////////////////////////////////////////////////////////////////
	
	/**
	 * 8-bit general purpose register list
	 */
	private static final List<X86RegisterRef8G> REGISTER_8G_LIST = 
		new ArrayList<X86RegisterRef8G>(
					Arrays.asList( new X86RegisterRef8G[] { AL, CL, DL, BL, AH, CH, DH, BH } )
				);
	
	/**
	 * 8-bit general purpose register mapping
	 */
	private static final Map<String,X86RegisterRef8G> REGISTERS_8G_MAPPING = new HashMap<String,X86RegisterRef8G>();
	static {
		for( X86RegisterRef8G register : REGISTER_8G_LIST ) {
			REGISTERS_8G_MAPPING.put( register.getName(), register );
		}
	}
	
	/**
	 * 16-bit general purpose register list
	 */
	private static final List<X86RegisterRef16G> REGISTER_16G_LIST = 
		new ArrayList<X86RegisterRef16G>(
					Arrays.asList( new X86RegisterRef16G[] { AX, CX, DX, BX, SI, DI, SP, BP } )
				);
	
	/**
	 * 16-bit general purpose register mapping
	 */
	private static final Map<String,X86RegisterRef16G> REGISTERS_16G_MAPPING = new HashMap<String,X86RegisterRef16G>();
	static {
		for( X86RegisterRef16G register : REGISTER_16G_LIST ) {
			REGISTERS_16G_MAPPING.put( register.getName(), register );
		}
	}
	
	/**
	 * 16-bit segment register list
	 */
	private static final List<X86RegisterRef16S> REGISTER_16S_LIST = 
		new ArrayList<X86RegisterRef16S>(
					Arrays.asList( new X86RegisterRef16S[] { ES, CS, SS, DS } )
				);
	
	/**
	 * 16-bit segment register mapping
	 */
	private static final Map<String,X86RegisterRef16S> REGISTERS_16S_MAPPING = new HashMap<String,X86RegisterRef16S>(); 
	static {
		for( X86RegisterRef16S register : REGISTER_16S_LIST ) {
			REGISTERS_16S_MAPPING.put( register.getName(), register );
		}
	}
	
	/**
	 * 16-bit register (general purpose and segment registers combined) list
	 */
	private static final List<X86RegisterRef16> REGISTER_16_LIST = new ArrayList<X86RegisterRef16>();
	static {
		REGISTER_16_LIST.addAll( REGISTER_16G_LIST );
		REGISTER_16_LIST.addAll( REGISTER_16S_LIST );
	}
	
	/**
	 * 16-bit register (general purpose and segment registers combined) mapping
	 */
	private static final Map<String,X86RegisterRef16> REGISTERS_16_MAPPING = new HashMap<String,X86RegisterRef16>(); 
	static {
		for( X86RegisterRef16 register : REGISTER_16_LIST ) {
			REGISTERS_16_MAPPING.put( register.getName(), register );
		}
	}
	
	/**
	 * All Registers 
	 */
	private static final List<X86RegisterRef> REGISTERS_ALL_LIST = new ArrayList<X86RegisterRef>();
	static {		
		REGISTERS_ALL_LIST.addAll( REGISTER_8G_LIST );
		REGISTERS_ALL_LIST.addAll( REGISTER_16G_LIST );		
		REGISTERS_ALL_LIST.addAll( REGISTER_16S_LIST );
	}
	
	/**
	 * All register mappings
	 */
	private static final Map<String,X86RegisterRef> REGISTERS_ALL_MAPPING = new HashMap<String,X86RegisterRef>(); 
	static {
		for( X86RegisterRef register : REGISTERS_ALL_LIST ) {
			REGISTERS_ALL_MAPPING.put( register.getName(), register );
		}
	}
	
}
