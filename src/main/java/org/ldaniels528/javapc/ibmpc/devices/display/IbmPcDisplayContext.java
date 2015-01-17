package org.ldaniels528.javapc.ibmpc.devices.display;

import org.ldaniels528.javapc.ibmpc.devices.bios.IbmPcBIOS;
import org.ldaniels528.javapc.ibmpc.devices.memory.IbmPcRandomAccessMemory;

/**
 * IBM PC Display Context
 * @author lawrence.daniels@gmail.com
 */
public class IbmPcDisplayContext {
	public final IbmPcBIOS bios;
	public final IbmPcRandomAccessMemory memory;
	public IbmPcDisplayFrame frame;
	public IbmPcColorSet color;
	public int position;
	public int activePage;
	
	/**
	 * Creates an instance of this display context
	 * @param bios the given {@link IbmPcBIOS BIOS} instance
	 */
	public IbmPcDisplayContext( final IbmPcBIOS bios ) {
		this.bios				= bios;
		this.memory				= bios.getMemory();
		this.color				= new IbmPcColorSet( 7, 0, 0 );
		this.frame				= null;
		this.position			= 0;
		this.activePage			= 0;
	}

}
