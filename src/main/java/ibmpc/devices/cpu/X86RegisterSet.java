package ibmpc.devices.cpu;

import ibmpc.devices.cpu.x86.registers.X86Register;
import ibmpc.exceptions.X86AssemblyException;

/**
 * Represents a set of 80x86 registers including 8-, 16-, and 32-bit registers
 * @author lawrence.daniels@gmail.com
 */
public class X86RegisterSet {
	// 8-bit general purpose registers
	public final X86Register8bit AL;			// 8-bit low portion of AX
	public final X86Register8bit AH;			// 8-bit high portion of AX
	public final X86Register8bit BL;			// 8-bit low portion of BX
	public final X86Register8bit BH;			// 8-bit high portion of BX
	public final X86Register8bit CL;			// 8-bit low portion of CX
	public final X86Register8bit CH;			// 8-bit high portion of CX
	public final X86Register8bit DL;			// 8-bit low portion of DX
	public final X86Register8bit DH;			// 8-bit high portion of DX
	
	// 16-bit general purpose registers
	public final X86CompositeRegister16Bit AX; 	// 16-bit accumulator register
	public final X86CompositeRegister16Bit BX; 	// 16-bit base register
	public final X86CompositeRegister16Bit CX; 	// 16-bit counter register
	public final X86CompositeRegister16Bit DX; 	// 16-bit data register
	
	// 16-bit pointers/indexers
	public final X86Register16bit BP; 			// 16-bit base pointer register
	public final X86Register16bit SP; 			// 16-bit stack pointer register
	public final X86Register16bit DI; 			// 16-bit destination register
	public final X86Register16bit SI; 			// 16-bit source register
	
	// 16-bit segment registers
	public final X86Register16bit CS; 			// 16-bit code segment register
	public final X86Register16bit DS; 			// 16-bit data segment register
	public final X86Register16bit ES; 			// 16-bit extra segment register
	public final X86Register16bit SS; 			// 16-bit stack segment register
	
	// status and control registers
	public final X86ExtendedFlags FLAGS;		// 16-bit flags register
	public final X86MachineStatusWord MSW;		// 32-bit machine status word (286+)
	
	// instruction pointers
	public final X86RegisterIP IP; 	  			// 16-bit instruction pointer register
	public final X86Register32bit EIP; 	  		// 32-bit instruction pointer register
	
	// 32-bit segment registers (386+)
	public final X86Register16bit FS;			// 32-bit base pointer register
	public final X86Register16bit GS;			// 32-bit stack pointer register
	
	// 32-bit general purpose registers (386+)
	public final X86CompositeRegister32Bit EAX; // 32-bit accumulator register
	public final X86CompositeRegister32Bit EBX; // 32-bit base register
	public final X86CompositeRegister32Bit ECX; // 32-bit counter register
	public final X86CompositeRegister32Bit EDX; // 32-bit data register
	
	// 32-bit pointers/indexers (386+)
	public final X86CompositeRegister32Bit EBP;	// 32-bit base pointer register
	public final X86CompositeRegister32Bit ESP; // 32-bit stack pointer register
	public final X86CompositeRegister32Bit EDI; // 32-bit destination register
	public final X86CompositeRegister32Bit ESI; // 32-bit source register
	
	// register collections
	private final X86Register[] REG32_COMMON;
	private final X86Register[] REG16_SEGMENT;
	private final X86Register[] REG16_COMMON;
	private final X86Register[] REG8_COMMON;
	
	/////////////////////////////////////////////////////////
	//		Constructor(s)
	/////////////////////////////////////////////////////////
	
	/** 
	 * Default constructor
	 */
	public X86RegisterSet() {
		// initialize the 16-bit segment registers
		this.ES 	= new X86Register16bit( "ES", 0 );
		this.CS 	= new X86Register16bit( "CS", 1 );
		this.SS 	= new X86Register16bit( "SS", 2 );
		this.DS 	= new X86Register16bit( "DS", 3 );
		this.FS		= new X86Register16bit( "FS", 4 );
		this.GS		= new X86Register16bit( "GS", 5 );
		
		// initialize the 8-bit general purpose registers
		this.AL 	= new X86Register8bit( "AL", 0 );
		this.CL 	= new X86Register8bit( "CL", 1 );
		this.DL 	= new X86Register8bit( "DL", 2 );
		this.BL 	= new X86Register8bit( "BL", 3 );
		this.AH 	= new X86Register8bit( "AH", 4 );
		this.CH 	= new X86Register8bit( "CH", 5 );
		this.DH 	= new X86Register8bit( "DH", 6 );
		this.BH 	= new X86Register8bit( "BH", 7 );
		
		// initialize the 16-bit general purpose registers
		this.AX		= new X86CompositeRegister16Bit( "AX", AH, AL, 0 );
		this.BX		= new X86CompositeRegister16Bit( "BX", BH, BL, 3 );
		this.CX		= new X86CompositeRegister16Bit( "CX", CH, CL, 1 );
		this.DX		= new X86CompositeRegister16Bit( "DX", DH, DL, 2 );
		this.SI 	= new X86Register16bit( "SI", 4 );
		this.DI 	= new X86Register16bit( "DI", 5 );
		this.SP 	= new X86Register16bit( "SP", 6 );
		this.BP 	= new X86Register16bit( "BP", 7 );
		this.IP 	= new X86RegisterIP();
		
		// initialize the 32-bit segment registers
		this.EIP	= new X86Register32bit( "EIP", 0xFE );
		
		// initialize the 32-bit general purpose registers
		this.EAX	= new X86CompositeRegister32Bit( "EAX", new X86Register16bit( "EAH", 0 ), AX, 0 );
		this.EBX	= new X86CompositeRegister32Bit( "EBX", new X86Register16bit( "EBH", 0 ), BX, 3 );
		this.ECX	= new X86CompositeRegister32Bit( "ECX", new X86Register16bit( "ECH", 0 ), CX, 1 );
		this.EDX	= new X86CompositeRegister32Bit( "EDX", new X86Register16bit( "EDH", 0 ), DX, 2 );
		this.ESI	= new X86CompositeRegister32Bit( "ECX", new X86Register16bit( "ECH", 0 ), CX, 4 );
		this.EDI	= new X86CompositeRegister32Bit( "EDX", new X86Register16bit( "EDH", 0 ), DX, 5 );
		this.ESP 	= new X86CompositeRegister32Bit( "ESP", new X86Register16bit( "ESH", 0 ), CX, 6 );
		this.EBP 	= new X86CompositeRegister32Bit( "EBP", new X86Register16bit( "EBH", 0 ), CX, 7 );
		
		// initialize flags register
		this.FLAGS = new X86ExtendedFlags();
		
		// initialize the machine status word
		this.MSW = new X86MachineStatusWord();
		
		// create 32-bit register references
		this.REG32_COMMON = new X86Register[] {
			EAX, ECX, EDX, EBX, ESP, EBP, ESI, EDI	
		};
		
		// create 16-bit register references
		this.REG16_COMMON = new X86Register[] {
				AX, CX, DX, BX, SP, BP, SI, DI
		};
		
		// create 8-bit register references
		this.REG8_COMMON = new X86Register[] {
				AL, CL, DL, BL, AH, CH, DH, BH
		};
		
		// define the segment registers
		this.REG16_SEGMENT = new X86Register[] {
				ES, CS, SS, DS, FS, GS
		};
	}
	
	///////////////////////////////////////////////////////////////////
	//			Service Method(s)
	///////////////////////////////////////////////////////////////////
	
	/**
	 * Retrieves the segment register based on the given register ID
	 * @param regCode the given register ID
	 * @return the appropriate segment register
	 * @throws X86AssemblyException
	 */
	public X86Register get16BitSegmentRegister( final int regCode ) {
		return REG16_SEGMENT[ regCode ];
	}
	
	/**
	 * Returns the appropriate 8-bit register based 
	 * on the given ID
	 * @param regCode the given register ID
	 * @return the appropriate 8-bit {@link X86Register register}
	 */
	public X86Register get8bitRegister( final int regCode ) {
		return REG8_COMMON[ regCode ];
	}
	
	/**
	 * Returns the appropriate 16-bit register based 
	 * on the given ID
	 * @param regCode the given register ID
	 * @return the appropriate 16-bit {@link X86Register register}
	 * @throws X86AssemblyException
	 */
	public X86Register get16bitGeneralPurposeRegister( final int regCode ) {
		return REG16_COMMON[ regCode ];
	}
	
	/**
	 * Returns the appropriate 32-bit register based 
	 * on the given ID
	 * @param regCode the given register ID
	 * @return the appropriate 32-bit {@link X86Register register}
	 * @throws X86AssemblyException
	 */
	public X86Register get32bitGeneralPurposeRegister( final int regCode ) {
		return REG32_COMMON[ regCode ];
	}
	
	/* 
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		// if the CPU is in virtual mode, it's 386+
		if( FLAGS.isVM() ) {
			return String.format(
					"EAX = %08X           ECX = %08X\n" +
					"EDX = %08X           EBX = %08X\n" +
					"ESI = %08X           EDI = %08X\n" +
					"FS  = %08X           GS  = %08X\n" +
					"CS  = %04X               DS = %04X\n" +
					"ES  = %04X               SS = %04X\n" +
					"EIP = %04X %s\n", 
						EAX.get(), ECX.get(), EDX.get(), EBX.get(), 
						ESI.get(), EDI.get(), FS.get(),  GS.get(),
						CS.get(),  DS.get(),  ES.get(),  SS.get(), 
						EIP.get(), FLAGS 
			);
		}
		
		// otherwise, it's 8086 ...
		else {
			return String.format(
					"AX = %04X CX = %04X DX = %04X BX = %04X\n" +
					"SI = %04X DI = %04X SP = %04X BP = %04X\n" +
					"CS = %04X DS = %04X ES = %04X SS = %04X\n" +
					"IP = %04X %s\n", 
						AX.get(), CX.get(), DX.get(), BX.get(), 
						SI.get(), DI.get(), SP.get(), BP.get(), 
						CS.get(), DS.get(), ES.get(), SS.get(), 
						IP.get(), FLAGS 
			);
		}
	}
	
}
