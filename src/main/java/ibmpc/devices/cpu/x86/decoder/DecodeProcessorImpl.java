package ibmpc.devices.cpu.x86.decoder;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.OpCode;
import ibmpc.devices.cpu.operands.Operand;
import ibmpc.devices.cpu.x86.opcodes.flow.AbstractForcedRedirectOpCode;
import ibmpc.devices.memory.IbmPcRandomAccessMemory;
import ibmpc.devices.memory.X86MemoryProxy;

import static java.lang.String.format;

import org.apache.log4j.Logger;

/**
 * 80x86 Decode Processor
 * @author lawrence.daniels@gmail.com
 */
public class DecodeProcessorImpl implements DecodeProcessor {
	private final Logger logger = Logger.getLogger(getClass());
	protected final IbmPcRandomAccessMemory memory;
	protected final DecodeCache cache;
	protected final X86MemoryProxy proxy;
	private final Decoder[] decoders;
	protected final Intel80x86 cpu;
	
	/**
	 * Creates a new instance decode processor
	 * @param cpu the given {@link Intel80x86 CPU}
	 * @param proxy the given {@link X86MemoryProxy memory}
	 */
	public DecodeProcessorImpl( final Intel80x86 cpu, final X86MemoryProxy proxy ) {
		this.cpu		= cpu;
		this.proxy		= proxy;
		this.memory		= proxy.getMemory();
		this.cache		= new DecodeCache( 1000 );	
		this.decoders	= new Decoder[] {
							new Decoder00(), new Decoder10(), new Decoder20(), new Decoder30(),
							new Decoder40(), new Decoder50(), new Decoder60(), new Decoder70(),
							new Decoder80(), new Decoder90(), new DecoderA0(), new DecoderB0(),
							new DecoderC0(), new DecoderD0(), new DecoderE0(), new DecoderF0( this )
						};
	}
	
	/**
	 * Creates a new instance decode processor
	 * @param cpu the given {@link Intel80x86 CPU}
	 * @param memory the given {@link IbmPcRandomAccessMemory memory}
	 */
	public DecodeProcessorImpl( final Intel80x86 cpu, final IbmPcRandomAccessMemory memory ) {
		this( cpu, new X86MemoryProxy( memory, 0, 0 ) );
	}
	
	/* 
	 * (non-Javadoc)
	 * @see ibmpc.devices.cpu.x86.decoder.DecodeProcessor#decodeNext()
	 */
	public OpCode decodeNext() {			
		// capture the current offset
		final int offset0 = proxy.getOffset();
		
		// peek at the next byte
		final int code = proxy.peekAtNextByte();
					
		// get the first hex digit (mask = 1111 0000)
		final int index = ( ( code & 0xF0 ) >> 4 );
		
		// invoke the appropriate interpreter
		final OpCode opCode = decoders[index].decode( cpu, proxy );

		// get the length of the instruction
		final int codeLength = proxy.getOffset() - offset0;
		
		// capture the instruction's machine code
		final long insCode = getInstructionCode( offset0, codeLength );
		
		// set the instruction code and length
		opCode.setLength( codeLength );
		opCode.setInstructionCode( insCode );
		
		// cache the instruction
		//logger.info(format("D [%04X:%04X] %10X[%d] %s", proxy.getSegment(), offset0, insCode, codeLength, opCode));
		cache.cache( insCode, opCode );
			
		// is it a forced redirect?
		if( opCode.isForcedRedirect() ) {
			final AbstractForcedRedirectOpCode forcedRedirectOpCode = (AbstractForcedRedirectOpCode)opCode;
			final Operand destination = forcedRedirectOpCode.getDestination();
			proxy.setDestination( destination );
			logger.info(format("Redirecting to %04X:%04X", proxy.getSegment(), proxy.getOffset()));
		}
		
		// return the opCode
		return ( opCode.isConditional() ) 
				? new FlowControlCallBackOpCode( this, opCode ) 
				: opCode;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see ibmpc.devices.cpu.x86.decoder.DecodeProcessor#init()
	 */
	public void init() {
		// nothing to do
	}
	
	/* 
	 * (non-Javadoc)
	 * @see ibmpc.devices.cpu.x86.decoder.DecodeProcessor#shutdown()
	 */
	public void shutdown() {
		// nothing to do
	}

	/* 
	 * (non-Javadoc)
	 * @see ibmpc.devices.cpu.x86.decoder.DecodeProcessor#redirect(int, int)
	 */
	public void redirect( final int segment, final int offset ) {
		// point to the new decode position
		proxy.setSegment( segment );
		proxy.setOffset( offset );
	}
	
	/**
	 * Retrieves the instruction code
	 * @param offset the initial offset of the instruction
	 * @return the instruction code
	 */
	private long getInstructionCode( final int offset, final int codeLength ) {
		return memory.getBytesAsLong( proxy.getSegment(), offset, codeLength );
	}
}
