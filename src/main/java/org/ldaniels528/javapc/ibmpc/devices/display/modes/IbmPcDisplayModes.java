package org.ldaniels528.javapc.ibmpc.devices.display.modes;

import org.ldaniels528.javapc.ibmpc.devices.display.modes.cga.CGAGraphicsMode160x200PCjr;
import org.ldaniels528.javapc.ibmpc.devices.display.modes.cga.CGAGraphicsMode320x200;
import org.ldaniels528.javapc.ibmpc.devices.display.modes.cga.CGAGraphicsMode320x200PCjr;
import org.ldaniels528.javapc.ibmpc.devices.display.modes.cga.CGAGraphicsMode640x200Mono;
import org.ldaniels528.javapc.ibmpc.devices.display.modes.cga.CGAGraphicsMode640x200PCjr;
import org.ldaniels528.javapc.ibmpc.devices.display.modes.cga.CGATextMode40x25;
import org.ldaniels528.javapc.ibmpc.devices.display.modes.cga.CGATextMode80x25;
import org.ldaniels528.javapc.ibmpc.devices.display.modes.ega.EGAGraphicsMode320x200;
import org.ldaniels528.javapc.ibmpc.devices.display.modes.ega.EGAGraphicsMode640x200;
import org.ldaniels528.javapc.ibmpc.devices.display.modes.ega.EGAGraphicsMode640x350;
import org.ldaniels528.javapc.ibmpc.devices.display.modes.ega.EGAGraphicsMode640x350Mono;
import org.ldaniels528.javapc.ibmpc.devices.display.modes.mda.MDATextMode720x350;
import org.ldaniels528.javapc.ibmpc.devices.display.modes.vga.VGAGraphicsMode320x200;
import org.ldaniels528.javapc.ibmpc.devices.display.modes.vga.VGAGraphicsMode640x480;
import org.ldaniels528.javapc.ibmpc.devices.display.modes.vga.VGAGraphicsMode640x480Mono;

import java.util.Arrays;
import java.util.List;

/**
 * Represents a collection of IBM PC Display Modes
 * @author lawrence.daniels@gmail.com
 */
public class IbmPcDisplayModes {	
	// Color Graphics Adaptor (CGA) text modes	
	public static final IbmPcDisplayMode CGA_40X25X16BW 	= new CGATextMode40x25();			// 0x00
	public static final IbmPcDisplayMode CGA_40X25X16 		= new CGATextMode40x25(); 			// 0x01
	public static final IbmPcDisplayMode CGA_80X25X16BW 	= new CGATextMode80x25();			// 0x02
	public static final IbmPcDisplayMode CGA_80X25X16 		= new CGATextMode80x25();			// 0x03
	
	// Color Graphics Adaptor (CGA) graphics modes
	public static final IbmPcDisplayMode CGA_320X200X4 		= new CGAGraphicsMode320x200(); 	// 0x04
	public static final IbmPcDisplayMode CGA_320X200X4BW	= new CGAGraphicsMode320x200(); 	// 0x05
	public static final IbmPcDisplayMode CGA_640X200X2		= new CGAGraphicsMode640x200Mono();	// 0x06
	
	// Monochrome Display Adaptor (MDA) text modes
	public static final IbmPcDisplayMode MDA_720X350		= new MDATextMode720x350();			// 0x07
	
	// Color Graphics Adaptor (CGA) PCjr graphics modes
	public static final IbmPcDisplayMode PCJR_160X200X16	= new CGAGraphicsMode160x200PCjr();	// 0x08
	public static final IbmPcDisplayMode PCJR_320X200X16	= new CGAGraphicsMode320x200PCjr();	// 0x09
	public static final IbmPcDisplayMode PCJR_640X200X4		= new CGAGraphicsMode640x200PCjr();	// 0x0A
	
	// Enhanced Graphics Adaptor (EGA) graphics modes
	public static final IbmPcDisplayMode EGA_320X200X16		= new EGAGraphicsMode320x200();		// 0x0D
	public static final IbmPcDisplayMode EGA_640X200X16 	= new EGAGraphicsMode640x200();		// 0x0E
	public static final IbmPcDisplayMode EGA_640X350X4BW 	= new EGAGraphicsMode640x350Mono();	// 0x0F
	public static final IbmPcDisplayMode EGA_640X350X16 	= new EGAGraphicsMode640x350();		// 0x10

	// Video Graphics Adaptor (VGA) graphics modes
	public static final IbmPcDisplayMode VGA_640X480X2 		= new VGAGraphicsMode640x480Mono();	// 0x11
	public static final IbmPcDisplayMode VGA_640X480X16 	= new VGAGraphicsMode640x480();		// 0x12
	public static final IbmPcDisplayMode VGA_320X200X256	= new VGAGraphicsMode320x200();		// 0x13

	// enumeration of display modes
	public static final List<IbmPcDisplayMode> DISPLAY_MODES = 
		Arrays.asList( new IbmPcDisplayMode[] {
			CGA_40X25X16BW, CGA_40X25X16, CGA_80X25X16BW, CGA_80X25X16, 
			CGA_320X200X4, CGA_320X200X4BW, CGA_640X200X2, MDA_720X350,
			PCJR_160X200X16, PCJR_320X200X16, PCJR_640X200X4,
			EGA_320X200X16, EGA_640X200X16, EGA_640X350X4BW, EGA_640X350X16,
			VGA_640X480X2, VGA_640X480X16, VGA_320X200X256
		} );
	
}
