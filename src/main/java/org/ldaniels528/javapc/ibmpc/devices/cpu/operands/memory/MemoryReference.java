package org.ldaniels528.javapc.ibmpc.devices.cpu.operands.memory;

import java.util.HashMap;
import java.util.Map;

import org.ldaniels528.javapc.ibmpc.devices.cpu.Intel80x86;
import org.ldaniels528.javapc.ibmpc.devices.cpu.X86RegisterSet;
import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.Operand;

/**
 * Represents an 80x86 Memory Reference.  References of this type are
 * not {@link Operand operands}, but are merely memory locations.
 * <pre>
 * 80x86 Memory Address Reference Types
 * 
 * Reference	Code	tt xxx mmm
 * ---------	----	----------
 * [BX+SI]		00		00 ... 000
 * [BX+DI]		01		00 ... 001
 * [BP+SI]		02		00 ... 010
 * [BP+DI]		03		00 ... 011
 * 
 * [SI]			04		00 ... 100
 * [DI]			05		00 ... 101
 * [nnnn]		06		00 ... 110
 * [BX]			07		00 ... 111
 * 
 * [BX+SI+nn]	40		01 ... 000
 * [BX+DI+nn]	41		01 ... 001
 * [BP+SI+nn]	42		01 ... 010
 * [BP+DI+nn]	43		01 ... 011
 * 
 * [SI+nn]		44		01 ... 100
 * [DI+nn]		45		01 ... 101
 * [BP+nn]		46		01 ... 110
 * [BX+nn]		47		01 ... 111
 * 
 * [BX+SI+nnnn]	80		10 ... 000	
 * [BX+DI+nnnn]	81		10 ... 001
 * [BP+SI+nnnn]	82		10 ... 010
 * [BP+DI+nnnn]	83		10 ... 011
 * 
 * [SI+nnnn]	84		10 ... 100
 * [DI+nnnn]	85		10 ... 101
 * [BP+nnnn]	86		10 ... 110
 * [BX+nnnn]	87		10 ... 111
 * 
 * (upper 2-bits = 8/16-bit value, lower 3-bits = type)
 * </pre>
 * @author lawrence.daniels@gmail.com
 */
public class MemoryReference {
	// reference - set 1
	public static final int REF_CONST_BX_SI 		= 0x00; // 00 ... 000
	public static final int REF_CONST_BX_DI 		= 0x01; // 00 ... 001
	public static final int REF_CONST_BP_SI 		= 0x02; // 00 ... 010
	public static final int REF_CONST_BP_DI 		= 0x03; // 00 ... 011
	// reference - set 2
	public static final int REF_CONST_SI 			= 0x04; // 00 ... 100
	public static final int REF_CONST_DI 			= 0x05; // 00 ... 101
	public static final int REF_CONST_NNNN 			= 0x06; // 00 ... 110
	public static final int REF_CONST_BX 			= 0x07; // 00 ... 111
	// reference - set 3
	public static final int REF_CONST_BX_SI_NN		= 0x40; // 01 ... 000
	public static final int REF_CONST_BX_DI_NN		= 0x41; // 01 ... 001
	public static final int REF_CONST_BP_SI_NN		= 0x42; // 01 ... 010
	public static final int REF_CONST_BP_DI_NN		= 0x43; // 01 ... 011
	// reference - set 4
	public static final int REF_CONST_SI_NN			= 0x44; // 01 ... 100
	public static final int REF_CONST_DI_NN			= 0x45; // 01 ... 101
	public static final int REF_CONST_BP_NN			= 0x46; // 01 ... 110
	public static final int REF_CONST_BX_NN			= 0x47; // 01 ... 111
	// reference - set 5
	public static final int REF_CONST_BX_SI_NNNN	= 0x80; // 10 ... 000
	public static final int REF_CONST_BX_DI_NNNN	= 0x81; // 10 ... 001
	public static final int REF_CONST_BP_SI_NNNN	= 0x82; // 10 ... 010
	public static final int REF_CONST_BP_DI_NNNN	= 0x83; // 10 ... 011
	// reference - set 6
	public static final int REF_CONST_SI_NNNN		= 0x84; // 10 ... 100
	public static final int REF_CONST_DI_NNNN		= 0x85; // 10 ... 101
	public static final int REF_CONST_BP_NNNN		= 0x86; // 10 ... 110
	public static final int REF_CONST_BX_NNNN		= 0x87; // 10 ... 111
	// build the string mappings
	public static final Map<Integer,String> REFERENCE_MAPPING = createReferenceMapping();
	// fields
	private final Intel80x86 cpu;
	private final int refCode;
	private final int relOffset;
	
	/**
	 * Creates a new memory reference
	 * @param cpu the given {@link Intel80x86 CPU} instance
	 * @param refCode the given compsite element code and reference code
	 * @param relOffset the relative offset
	 */
	public MemoryReference( final Intel80x86 cpu,
					  		final int refCode, 
					  		final int relOffset ) {
		this.cpu	 	= cpu;
		this.refCode 	= refCode;
		this.relOffset	= relOffset;
	}
	
	/**
	 * Returns the CPU reference
	 * @return the {@link Intel80x86 CPU} reference
	 */
	public Intel80x86 getCPU() {
		return cpu;
	}
	
	/**
	 * Returns the "effective" offset (within {@link X86RegisterSet#DS DS})
	 * @return the "effective" offset
	 */
	public int getOffset() {
		return getReferencedAddress( refCode, relOffset );
	}

	/* 
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return String.format( REFERENCE_MAPPING.get( refCode ), relOffset );
	}
	
	/**
	 * Returns the offset for the "referenced" address represented
	 * by the given code
	 * @param refCode the given "referenced" address code
	 * @param relOffset the given relative offset
	 * @return the offset for the "referenced" address
	 * @throws IllegalStateException if the reference code is invalid
	 */
	private int getReferencedAddress( final int refCode, final int relOffset ) {
		int offset;
		switch( refCode ) {
			// [bx+si]
			case REF_CONST_BX_SI:	
				offset = cpu.BX.get() + cpu.SI.get(); 
				break;
				
			// [bx+di]						
			case REF_CONST_BX_DI:	
				offset = cpu.BX.get() + cpu.DI.get(); 
				break;
						
			// [bp+si]				
			case REF_CONST_BP_SI:	
				offset = cpu.BP.get() + cpu.SI.get(); 
				break;
				
			// [bp+di]
			case REF_CONST_BP_DI:	
				offset = cpu.BP.get() + cpu.DI.get(); 
				break;
						
			// [si]
			case REF_CONST_SI:
				offset = cpu.SI.get(); 
				break;

			// [di]				
			case REF_CONST_DI:
				offset = cpu.DI.get(); 
				break;
					
			// [nnnn]
			case REF_CONST_NNNN:	
				offset = relOffset;
				break;
				
			// [bx]
			case REF_CONST_BX: 		
				offset = cpu.BX.get(); 
				break;
				
				// [bx]
			case REF_CONST_BX_NN: 		
				offset = cpu.BX.get() + relOffset; 
				break;
				
			// [bx+si+nn]
			case REF_CONST_BX_SI_NN: 
				offset = cpu.BX.get() + cpu.SI.get() + relOffset; 
				break;
			
			// [bx+di+nn]
			case REF_CONST_BX_DI_NN: 
				offset = cpu.BX.get() + cpu.DI.get() + relOffset; 
				break;
			
			// [bp+si+nn]
			case REF_CONST_BP_SI_NN: 
				offset = cpu.BP.get() + cpu.SI.get() + relOffset; 
				break;
			
			// [bp+di+nn]
			case REF_CONST_BP_DI_NN: 
				offset = cpu.BP.get() + cpu.DI.get() + relOffset; 
				break;
			
			// [si+nn]
			case REF_CONST_SI_NN: 	
				offset = cpu.SI.get() + relOffset; 
				break;
			
			// [di+nn]
			case REF_CONST_DI_NN: 	
				offset = cpu.DI.get() + relOffset; 
				break;
			
			// [bp+nn]
			case REF_CONST_BP_NN: 	
				offset = cpu.BP.get() + relOffset; 
				break;
				
			// [bx+si+nnnn]
			case REF_CONST_BX_SI_NNNN: 
				offset = cpu.BX.get() + cpu.SI.get() + relOffset; 
				break;
			
			// [bx+di+nnnn]
			case REF_CONST_BX_DI_NNNN: 
				offset = cpu.BX.get() + cpu.DI.get() + relOffset; 
				break;
			
			// [bp+si+nnnn]
			case REF_CONST_BP_SI_NNNN: 
				offset = cpu.BP.get() + cpu.SI.get() + relOffset; 
				break;
			
			// [bp+di+nnnn]
			case REF_CONST_BP_DI_NNNN: 
				offset = cpu.BP.get() + cpu.DI.get() + relOffset; 
				break;
				
			// [si+nnnn]
			case REF_CONST_SI_NNNN: 	
				offset = cpu.SI.get() + relOffset; 
				break;
			
			// [di+nnnn]
			case REF_CONST_DI_NNNN: 	
				offset = cpu.DI.get() + relOffset; 
				break;
			
			// [bp+nnnn]
			case REF_CONST_BP_NNNN: 	
				offset = cpu.BP.get() + relOffset; 
				break;
				
			// [bx+nnnn]
			case REF_CONST_BX_NNNN:
				offset = cpu.BX.get() + relOffset; 
				break;
				
			// unknown
			default:
				throw new IllegalStateException( String.format( "Invalid memory reference code (code=%02X)", refCode ) );
		}
		return offset;
	}
	
	/** 
	 * Builds a mapping of register references to machine code operands
	 * @return a mapping of register references to machine code operands
	 */
	private static Map<Integer,String> createReferenceMapping() {
		final Map<Integer,String> mapping = new HashMap<Integer,String>();
		mapping.put( REF_CONST_BX_SI, 		"[BX+SI]" );
		mapping.put( REF_CONST_BX_DI, 		"[BX+DI]" );
		mapping.put( REF_CONST_BP_SI, 		"[BP+SI]" );
		mapping.put( REF_CONST_BP_DI,		"[BP+DI]" );
		
		mapping.put( REF_CONST_BX,			"[BX]" );
		mapping.put( REF_CONST_SI,			"[SI]" );
		mapping.put( REF_CONST_DI,			"[DI]"  );
		mapping.put( REF_CONST_NNNN,		"[%04X]" );	
		
		mapping.put( REF_CONST_BX_SI_NN,	"[BX+SI+%02X]" );
		mapping.put( REF_CONST_BX_DI_NN,	"[BX+DI+%02X]" );
		mapping.put( REF_CONST_BP_SI_NN,	"[BP+SI+%02X]" );
		mapping.put( REF_CONST_BP_DI_NN,	"[BP+DI+%02X]" );
		
		mapping.put( REF_CONST_SI_NN,		"[SI+%02X]" );
		mapping.put( REF_CONST_DI_NN,		"[DI+%02X]" );
		mapping.put( REF_CONST_BP_NN,		"[BP+%02X]" );
		mapping.put( REF_CONST_BX_NN,		"[BX+%02X]" );
		
		mapping.put( REF_CONST_BX_SI_NNNN,	"[BX+SI+%04X]" );
		mapping.put( REF_CONST_BX_DI_NNNN,	"[BX+DI+%04X]" );
		mapping.put( REF_CONST_BP_SI_NNNN,	"[BP+SI+%04X]" );
		mapping.put( REF_CONST_BP_DI_NNNN,	"[BP+DI+%04X]" );
		
		mapping.put( REF_CONST_SI_NNNN,		"[SI+%04X]" );
		mapping.put( REF_CONST_DI_NNNN,		"[DI+%04X]" );
		mapping.put( REF_CONST_BP_NNNN,		"[BP+%04X]" );
		mapping.put( REF_CONST_BX_NNNN,		"[BX+%04X]" );
		return mapping;
	}

}
