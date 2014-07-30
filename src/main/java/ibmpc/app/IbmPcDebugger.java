package ibmpc.app;

import static ibmpc.util.IbmPcValues.parseHexadecimalString;
import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.OpCode;
import ibmpc.devices.cpu.ProgramContext;
import ibmpc.devices.cpu.x86.decoder.DecodeProcessor;
import ibmpc.devices.cpu.x86.decoder.Decoder;
import ibmpc.devices.cpu.x86.decoder.Decoder00;
import ibmpc.devices.cpu.x86.decoder.Decoder10;
import ibmpc.devices.cpu.x86.decoder.Decoder20;
import ibmpc.devices.cpu.x86.decoder.Decoder30;
import ibmpc.devices.cpu.x86.decoder.Decoder40;
import ibmpc.devices.cpu.x86.decoder.Decoder50;
import ibmpc.devices.cpu.x86.decoder.Decoder60;
import ibmpc.devices.cpu.x86.decoder.Decoder70;
import ibmpc.devices.cpu.x86.decoder.Decoder80;
import ibmpc.devices.cpu.x86.decoder.Decoder90;
import ibmpc.devices.cpu.x86.decoder.DecoderA0;
import ibmpc.devices.cpu.x86.decoder.DecoderB0;
import ibmpc.devices.cpu.x86.decoder.DecoderC0;
import ibmpc.devices.cpu.x86.decoder.DecoderD0;
import ibmpc.devices.cpu.x86.decoder.DecoderE0;
import ibmpc.devices.cpu.x86.decoder.DecoderF0;
import ibmpc.devices.cpu.x86.opcodes.data.DB;
import ibmpc.devices.cpu.x86.opcodes.data.DW;
import ibmpc.devices.display.IbmPcDisplay;
import ibmpc.devices.display.IbmPcDisplayFrame;
import ibmpc.devices.keyboard.IbmPcKeyboard;
import ibmpc.devices.memory.IbmPcRandomAccessMemory;
import ibmpc.devices.memory.X86MemoryProxy;
import ibmpc.exceptions.IbmPcException;
import ibmpc.exceptions.IbmPcNumericFormatException;
import ibmpc.exceptions.X86AssemblyException;
import ibmpc.system.IbmPcSystemXT;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * IBM PC/MS DOS Debugger
 * @author lawrence.daniels@gmail.com
 */
public class IbmPcDebugger {
	private static final float VERSION = 0.02f;
	private final IbmPcRandomAccessMemory memory;
	private final X86MemoryProxy proxy;
	private final DebugDecoder decoder;
	private final IbmPcDisplay display;
	private final IbmPcKeyboard keyboard;
	private final Intel80x86 cpu;
	private String filename;
	private boolean alive;
	
	/**
	 * Default constructor
	 */
	public IbmPcDebugger() {
		// create the display frame and system
		final IbmPcDisplayFrame frame = new IbmPcDisplayFrame( String.format( "JDebug v%f", VERSION ) );
		final IbmPcSystemXT system = new IbmPcSystemXT( frame );
		
		// get references to all devices
		this.cpu		= system.getCPU();
		this.memory		= system.getRandomAccessMemory();
		this.display	= system.getDisplay();
		this.keyboard	= system.getKeyboard();
		
		// create debug helper objects
		this.proxy		= new X86MemoryProxy( memory, 0x13F0, 0x0100 );
		this.decoder 	= new DebugDecoder( cpu, proxy );
		
		// (un)set the CPU into virtual mode
		cpu.FLAGS.setVM( false );
	}

	/**
	 * For standalone operation
	 * @param args the given commandline arguments
	 * @throws X86MalformedInstructionException 
	 */
	public static void main( final String[] args ) 
	throws IbmPcException {
		// start the application
		final IbmPcDebugger app = new IbmPcDebugger();
		
		// display title
		app.outputln( "X86Debugger v0.1" );
		app.outputln( "IntelligentXChange (c) Oct 2006" );
		app.outputln( "" );
		
		// was an executable file specified?
		if( args.length > 0 ) {
			// point to the file
			app.filename = args[0];
			
			// attemp to load the file
			app.loadExecutable( app.filename );
		}
		
		// start the debugger
		app.run();
	}
	
	/* 
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() throws IbmPcException {
		alive = true;
		 while( alive ) {
			 output( "- " );
			 
			 // get the next line input
			 final String input = keyboard.nextLine();
			 
			 // interpret the command
			 interpret( input );
		 }
	}

	/**
	 * Stops the debugger
	 */
	public void stop() {
		this.alive = false;
	}

	/**
	 * Interprets and executes the given command
	 * @param command the given command
	 */
	private void interpret( final String command ) {
		 // [d]ump
		 if( command.startsWith( "d" ) ) {
			 dump( 128 );
		 } 
		 
		 // [g]o
		 else if( command.startsWith( "g" ) ) {
			 executeCode( cpu.IP.get() );
		 } 
		 
		 // [n]ame
		 else if( command.startsWith( "n" ) ) {
			 name( command );
		 } 
		 
		 // [q]uit
		 else if( command.startsWith( "q" ) ) {
			 stop();
		 } 
		 
		 // [r]egisters
		 else if( command.startsWith( "r" ) ) {
			 System.out.println( cpu );
		 } 
		 
		 // [u]nassemble
		 else if( command.startsWith( "u" ) ) {
			 unassemble( command, 10 );
		 }
	}
	
	/**
	 * Dumps the specific number of bytes on screen
	 * @param count the specified number of bytes to display
	 * @return a {@link List collection} of string representing 
	 * the dumped output.
	 */
	private void dump( final int count ) {
		final int DUMP_LENGTH = 16;
		
		// create a container for lines of data
		final List<String> data = new LinkedList<String>();
		
		// determine how many lines to display
		final int lines = count / DUMP_LENGTH;
		
		// display each line of binary data
		for( int n = 0; n < lines; n++ ) {
			final List<Integer> bytes = new ArrayList<Integer>( DUMP_LENGTH );
			for( int m = 0; m < DUMP_LENGTH; m++ ) {
				bytes.add( proxy.nextByte() );
			}
			data.add( layoutBytes( bytes ) );
		}
		
		// display the results
		for( final String instruction : data ) {
			outputln( instruction );	 
		}
	}
	
	private void executeCode( final int offset ) {
		try {
			// create the program context
			final ProgramContext context = new ProgramContext(
						proxy.getSegment(), offset, proxy.getSegment(), null
					);
			
			// execute the code
			cpu.execute( context );
		}
		catch( X86AssemblyException e ) {
			outputln( e.getMessage() );
		}
	}
	
	/**
	 * Lays out the given collection of bytes.
	 * @param byteCodes the given {@link List collection} of bytes
	 * @return the formatted string
	 */
	private String layoutBytes( final List<Integer> byteCodes ) {
		final StringBuffer sb1 = new StringBuffer( 3 * byteCodes.size() );
		final StringBuffer sb2 = new StringBuffer( 3 * byteCodes.size() );
		
		sb2.append( '[' );
		for( final Integer byteCode : byteCodes ) {
		 	sb1.append( String.format( "%02X ", byteCode ) );
		 	sb2.append( String.format( ( byteCode >= 32 && byteCode <= 255 ) ? "%c" : ".", byteCode ) );
		}
		sb2.append( ']' );
		sb1.append( "- " ).append( sb2 );
		return sb1.toString();
	}
	
	/**
	 * Loads the given executable into memory
	 * @param filename the file name of the executable to load
	 */
	private void loadExecutable( final String filename ) {
		FileInputStream fis = null;
		try {
			// open the file for reading
			fis = new FileInputStream( filename );
			
			// load the contents into a buffer
			final ByteArrayOutputStream baos = new ByteArrayOutputStream( 65536 );
			final byte[] buf = new byte[1024];
			int count = 0;
			int total = 0;
			while( ( count = fis.read( buf ) ) != -1 ) {
				baos.write( buf );
				total += count;
			}
		
			// dump the content into a buffer
			final byte[] code = baos.toByteArray();
			
			// copy the program into memory
			final int codeSegment = proxy.getSegment();
			final int codeOffset  = 0x0100;
			memory.setBytes( proxy.getSegment(), codeOffset, code, code.length );
			
			// point IP to the code
			cpu.CS.set( codeSegment );
			cpu.DS.set( codeSegment );
			cpu.ES.set( codeSegment );
			cpu.SS.set( codeSegment );
			cpu.IP.set( codeOffset );
			
			outputln( String.format( "Loaded %s: %d bytes", filename, total ) );
		}
		catch( final IOException e ) {
			outputln( String.format( "Unable to load '%s'", filename ) );
		}
		finally {
			if( fis != null ) {
				try { fis.close(); } catch( Exception e ) { }
			}
		}
	}
	
	/**
	 * Sets the name of a file to be read or written
	 * @param command the given command string (e.g. 'nFROGGER.COM')
	 */
	private void name( final String command ) {
		// get & display the file name
		filename = command.substring( 1 );
		System.out.printf( "Filename: %s\n", filename );
	}
	
	/**
	 * Unassembles the specified number of instructions.
	 * @param origin indicates where to begin unassembling
	 * @param count the specified number of instructions to unassemble.
	 * @throws X86MalformedInstructionException
	 */
	private void unassemble( final String command, final int count ) {
		// check the command arguments
		final String[] args = command.split( "[ ]" );
		int origin = -1;
		try {
			origin = ( args.length > 1 ) ? parseHexadecimalString( args[1] ) : -1;
		} 
		catch( final IbmPcNumericFormatException e ) { } 
		 
		// change the origin?
		if( origin != -1 ) {
			proxy.setOffset( origin );
		}
		
		// begin unassembling
		int n = 0;
		try {
			while( n++ < count ) {
				// capture the segment and offset
				final int segment = proxy.getSegment();
				final int offset  = proxy.getOffset();
				
				// get the next instruction
				final OpCode instruction = decoder.decodeNext();
				
				// get the byte code for the current instruction
				final String byteCodeString = getByteCodeString( segment, offset );
				
				// display the instruction
				outputln( String.format( "%04X:%04X %s %s", segment, offset, byteCodeString, instruction ) );	 
			}
		}
		catch( final RuntimeException e ) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates a byte string
	 * @param segment the given segment
	 * @param offset the given start offset
	 * @return a byte string
	 */
	private String getByteCodeString( final int segment, final int offset ) {
		final int MAX_LEN = 12;
		
		// get the data block
		final int length = proxy.getOffset() - offset;
		final byte[] block = new byte[length];
		memory.getBytes( segment, offset, block, block.length );
		
		// create a byte string
		final StringBuffer sb = new StringBuffer( MAX_LEN );
		for( final byte b : block ) {
			sb.append( String.format( "%02X", b ) );
		}
		
		// pad upto the maximum size
		while( sb.length() < MAX_LEN ) {
			sb.append( ' ' );
		}
		
		return sb.toString();
	}
	
	private void output( final String text ) {
		display.write( text );
		display.update();
		
		System.out.print( text );
	}
	
	private void outputln( final String text ) {
		display.writeLine( text );
		display.update();
		
		System.out.println( text );
	}
	
	/**
	 * Represents a simple 80x86 Decode Processor
	 * @author lawrence.daniels@gmail.com
	 */
	private class DebugDecoder implements DecodeProcessor {
		private final X86MemoryProxy proxy;
		private final Decoder[] decoders;
		private final Intel80x86 cpu;
		
		/**
		 * Creates a new instance decode processor
		 * @param cpu the given {@link Intel80x86 CPU} instance
		 * @param proxy the given {@link X86MemoryProxy memory proxy} instance
		 */
		public DebugDecoder( final Intel80x86 cpu, final X86MemoryProxy proxy ) {
			this.cpu		= cpu;
			this.proxy		= proxy;
			this.decoders	= 
				new Decoder[] {
					new Decoder00(), new Decoder10(), new Decoder20(), new Decoder30(),
					new Decoder40(), new Decoder50(), new Decoder60(), new Decoder70(),
					new Decoder80(), new Decoder90(), new DecoderA0(), new DecoderB0(),
					new DecoderC0(), new DecoderD0(), new DecoderE0(), new DecoderF0( this )
				};
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
			OpCode opCode;
			try {
				opCode = decoders[index].decode( cpu, proxy );
			}
			catch( final Throwable cause ) {
				// get the length of the failed instruction
				final int length = proxy.getOffset() - offset0;
				final int bytecode = (int)memory.getBytesAsLong( proxy.getSegment(), offset0, length );
				opCode = ( length == 1 ) ? new DB( bytecode ) : new DW( bytecode );
			}

			// get the length of the instruction
			final int codeLength = proxy.getOffset() - offset0;
			
			// set the instruction code length
			opCode.setLength( codeLength );
			
			// return the opCode
			return opCode;
		}

		/* (non-Javadoc)
		 * @see ibmpc.devices.cpu.x86.decoder.DecodeProcessor#init()
		 */
		public void init() {
			// no initialization needed
		}

		/* (non-Javadoc)
		 * @see ibmpc.devices.cpu.x86.decoder.DecodeProcessor#redirect(int, int)
		 */
		public void redirect( final int segment, final int offset ) {
			// no redirection needed
		}

		/* (non-Javadoc)
		 * @see ibmpc.devices.cpu.x86.decoder.DecodeProcessor#shutdown()
		 */
		public void shutdown() {
			// no shutdown needed
		}
		
	}

	
}
