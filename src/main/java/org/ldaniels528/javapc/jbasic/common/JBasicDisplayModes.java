package org.ldaniels528.javapc.jbasic.common;

import org.ldaniels528.javapc.ibmpc.devices.display.modes.IbmPcDisplayMode;
import org.ldaniels528.javapc.ibmpc.devices.display.modes.IbmPcDisplayModes;

/**
 * Represents the set of available BASICA/GWBASIC/QBASIC graphical modes
 * @author lawrence.daniels@gmail.com
 */
public class JBasicDisplayModes {	
	// non-graphical display modes	
	public static final IbmPcDisplayMode MODE0a = IbmPcDisplayModes.CGA_40X25X16;
	public static final IbmPcDisplayMode MODE0b = IbmPcDisplayModes.CGA_80X25X16;
	
	// CGA graphical display modes
	public static final IbmPcDisplayMode MODE1	= IbmPcDisplayModes.CGA_320X200X4;
	public static final IbmPcDisplayMode MODE2 	= IbmPcDisplayModes.CGA_640X200X2;
	
	// PCjr graphical display modes
	public static final IbmPcDisplayMode MODE3 	= IbmPcDisplayModes.PCJR_160X200X16;
	public static final IbmPcDisplayMode MODE4 	= IbmPcDisplayModes.PCJR_320X200X16;
	public static final IbmPcDisplayMode MODE5 	= IbmPcDisplayModes.PCJR_640X200X4;
	
	// EGA graphical display modes
	public static final IbmPcDisplayMode MODE7 	= IbmPcDisplayModes.EGA_320X200X16;
	public static final IbmPcDisplayMode MODE8 	= IbmPcDisplayModes.EGA_640X200X16;
	public static final IbmPcDisplayMode MODE9 	= IbmPcDisplayModes.EGA_640X350X16;
		
	// VGA graphical display modes
	public static final IbmPcDisplayMode MODE10 = IbmPcDisplayModes.VGA_320X200X256;
	
}
