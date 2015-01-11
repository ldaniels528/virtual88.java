/**
 * 
 */
package org.ldaniels528.javapc.ibmpc.system;

/**
 * IBM computer-type code; see also BIOS INT 15H C0H
 * <pre>
 * 0ffH = original PC
 * 0feH = XT or Portable PC
 * 0fdH = PCjr
 * 0fcH = AT (or XT model 286) (or PS/2 Model 50/60)
 * 0fbH = XT with 640K motherboard
 * 0faH = PS/2 Model 30
 * 0f9H = Convertible PC (easily converts into a paperweight)
 * 0f8H = PS/2 Model 80
 * </pre>
 * @author lawrence.daniels@gmail.com
 * @see org.ldaniels528.javapc.ibmpc.devices.cpu.x86.bios.services.ATSystemServices
 */
public interface IbmPcSystemTypeConstants {
	public int IBM_PC				= 0xFF;
	public int XT_OR_PORTABLE_PC	= 0XFE;
	public int IBM_PCjr				= 0xFD;
	public int IBM_PC_AT			= 0xFC;
	public int IBM_PC_XT			= 0xFB;
	public int IBM_PS_2_MODEL30		= 0xFA;
	public int CONVERTIBLE_PC		= 0xF9;
	public int IBM_PS_2_MODEL80		= 0xF8;
	
}
