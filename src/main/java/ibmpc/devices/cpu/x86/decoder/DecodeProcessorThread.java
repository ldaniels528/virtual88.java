/**
 * 
 */
package ibmpc.devices.cpu.x86.decoder;

import ibmpc.devices.cpu.OpCode;
import ibmpc.util.Logger;

import java.util.LinkedList;

/**
 * 80x86 Decode Processor Thread
 * @author lawrence.daniels@gmail.com
 */
public class DecodeProcessorThread implements DecodeProcessor, Runnable {
	private final LinkedList<OpCode> queue;
	private final DecodeProcessor decoder;
	private boolean alive;
	private Thread thread;
	
	/**
	 * Creates a new instance decode processor thread
	 * @param decoder the given {@link DecodeProcessor decode processor}
	 */
	public DecodeProcessorThread( final DecodeProcessor decoder ) {
		this.decoder = decoder;
		this.queue	 = new LinkedList<OpCode>();
	}
	
	/**
	 * Decodes the next instruction
	 * @return the next {@link OpCode opCode}
	 */
	public OpCode decodeNext() {
		OpCode opCode = null;
		
		// get the next opCode
		synchronized( queue ) {
			// wait until there's at least one instruction
			while( queue.isEmpty() ) {
				try { queue.wait(); } catch( InterruptedException e ) { }
			}
			
			// if there's something in the queue ...
			if( !queue.isEmpty() ) {
				// get the instruction from the queue
				opCode = queue.removeFirst();
			}
		}
		
		return opCode;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see ibmpc.devices.cpu.x86.decoder.DecodeProcessor#init()
	 */
	public void init() {
		start();
	}
	
	/* 
	 * (non-Javadoc)
	 * @see ibmpc.devices.cpu.x86.decoder.DecodeProcessor#shutdown()
	 */
	public void shutdown() {
		stop();
	}
	
	/**
	 * Clears the queue as a result of a branch change,
	 * and pointers the decoder to the new segment and offset.
	 * @param segment the new decode segment
	 * @param offset the new decode offset
	 */
	public void redirect( final int segment, final int offset ) {
		synchronized( queue ) {
			Logger.info( "Conditional redirect to %04X:%04X (queue size = %d)\n", segment, offset, queue.size() );
			
			// clear the queue
			queue.clear();
			queue.notifyAll();
			
			// point to the new decode position
			decoder.redirect( segment, offset );
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		try {
			while( alive ) {
				// decode the next instruction
				final OpCode opCode = decoder.decodeNext();
				
				// if the instruction was successfully decoded ...
				if( opCode != null ) {
					// queue the instruction for execution
					synchronized( queue ) {
						queue.add( opCode );
						queue.notifyAll();
					}
				}
			}
		}
		catch( final RuntimeException e ) {
			this.alive = false;
			e.printStackTrace();
		}
	}
	
	/**
	 * Starts the processor
	 */
	public void start() {
		if( !alive ) {
			alive = true;
			thread = new Thread( this );
			thread.start();
		}
	}
	
	/**
	 * Stops the processor
	 */
	public void stop() {
		alive = false;
		if( thread != null ) {
			thread.interrupt();
			thread = null;
		}
	}

}
