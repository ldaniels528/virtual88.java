package ibmpc.devices.cpu.x86.bios.services;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.display.IbmPcColorSet;
import ibmpc.devices.display.IbmPcDisplay;
import ibmpc.devices.display.modes.IbmPcDisplayMode;
import ibmpc.exceptions.X86AssemblyException;
import ibmpc.system.IbmPcSystem;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

import static ibmpc.devices.display.modes.IbmPcDisplayModes.DISPLAY_MODES;
import static java.lang.String.format;

/**
 * BIOS Video Services Interrupt (INT 10h) Processor
 *
 * @author lawrence.daniels@gmail.com
 */
public class VideoServices implements InterruptHandler {
    private static final Map<Integer, IbmPcDisplayMode> AVAIL_DISPLAY_MODES = getAvailableDisplayModes();
    private static final VideoServices instance = new VideoServices();
    private final Logger logger = Logger.getLogger(getClass());

    /**
     * Private constructor
     */
    private VideoServices() {
        super();
    }

    /**
     * Returns the singleton instance of this class
     *
     * @return the singleton instance of this class
     */
    public static VideoServices getInstance() {
        return instance;
    }

    /**
     * Process the Video Services Interrupt (INT 10h)
     *
     * @throws X86AssemblyException
     */
    public void process(final IbmPcSystem system, final Intel80x86 cpu) throws X86AssemblyException {
        // get the display
        final IbmPcDisplay display = system.getDisplay();

        // determine what to do
        switch (cpu.AH.get()) {
            case 0x00:
                setVideoMode(display, cpu);
                break;
            case 0x01:
                setCursorSizeAndShape(display, cpu);
                break;
            case 0x02:
                setCursorPosition(display, cpu);
                break;
            case 0x03:
                queryCursorPositionAndSize(display, cpu);
                break;
            case 0x04:
                readLightPen(display, cpu);
                break;
            case 0x05:
                setActiveDisplayPage(display, cpu);
                break;
            case 0x06:
                scrollWindowUp(display, cpu);
                break;
            case 0x07:
                scrollWindowDown(display, cpu);
                break;
            case 0x08:
                readCharacterOrAttribute(display, cpu);
                break;
            case 0x09:
                writeCharacterOrAttribute(display, cpu);
                break;
            case 0x0A:
                writeCharacterAsTTY(display, cpu);
                break;
            case 0x0B:
                setColorPaletteOrBorder(display, cpu);
                break;
            case 0x0C:
                writeGraphicsPixel(display, cpu);
                break;
            case 0x0D:
                readGraphicsPixel(display, cpu);
                break;
            case 0x0E:
                writeCharacterAsTTY(display, cpu);
                break;
            case 0x0F:
                readVideoMode(display, cpu);
                break;
            default:
                throw new X86AssemblyException(String.format("Invalid call (%02X)", cpu.AH.get()));
        }
    }

    /**
     * Sets the display mode
     *
     * @param display the given {@link IbmPcDisplay display}
     * @param cpu     the given {@link Intel80x86 register context}
     * @throws X86AssemblyException
     */
    private void setVideoMode(final IbmPcDisplay display, final Intel80x86 cpu) throws X86AssemblyException {
        // get the request mode
        final int mode = cpu.AL.get();

        // lookup for a matching display mode
        final IbmPcDisplayMode displayMode = AVAIL_DISPLAY_MODES.get(mode);

        // if the mode wasn't found, error ...
        if (displayMode == null)
            throw new X86AssemblyException(String.format("Invalid Mode (mode = %02X)", mode));

        // set the display with the new mode
        display.setDisplayMode(displayMode);
    }

    /**
     * Sets the cursor size and/or shape
     *
     * @param display the given {@link IbmPcDisplay display}
     * @param cpu     the given {@link Intel80x86 register context}
     * @throws X86AssemblyException
     * @see <a href="http://www.htl-steyr.ac.at/~morg/pcinfo/hardware/interrupts/inte6lbk.htm">Set Cursor Type</a>
     */
    private void setCursorSizeAndShape(final IbmPcDisplay display, final Intel80x86 cpu) throws X86AssemblyException {
        logger.info(format("setCursorSizeAndShape is not yet implemented"));
    }

    /**
     * Sets the cursor position
     *
     * @param display the given {@link IbmPcDisplay display}
     * @param cpu     the given {@link Intel80x86 register context}
     */
    private void setCursorPosition(final IbmPcDisplay display, final Intel80x86 cpu) {
        // get video page
        final int page = cpu.BH.get();
        if (page != 0) {
            logger.debug(format("Video page is %d", page));
        }

        // get the row and column
        final int row = cpu.DH.get();
        final int col = cpu.DH.get();

        // set the cursor position
        display.setCursorPosition(col, row);
    }

    /**
     * Queries the cursor position and size
     *
     * @param display the given {@link IbmPcDisplay display}
     * @param cpu     the given {@link Intel80x86 register context}
     * @throws X86AssemblyException
     */
    private void queryCursorPositionAndSize(final IbmPcDisplay display, final Intel80x86 cpu) throws X86AssemblyException {
        throw new X86AssemblyException("Not yet implemeneted");
    }

    /**
     * Reads light pen information
     *
     * @param display the given {@link IbmPcDisplay display}
     * @param cpu     the given {@link Intel80x86 register context}
     * @throws X86AssemblyException
     */
    private void readLightPen(final IbmPcDisplay display, final Intel80x86 cpu) throws X86AssemblyException {
        throw new X86AssemblyException("Not yet implemeneted");
    }

    /**
     * Select active display page
     *
     * @param display the given {@link IbmPcDisplay display}
     * @param cpu     the given {@link Intel80x86 register context}
     * @throws X86AssemblyException
     */
    private void setActiveDisplayPage(final IbmPcDisplay display, final Intel80x86 cpu) throws X86AssemblyException {
        final int page = cpu.AL.get();
        display.setActivePage(page);
    }

    /**
     * Scroll Up / Clear Screen Rectangle
     *
     * @param display the given {@link IbmPcDisplay display}
     * @param cpu     the given {@link Intel80x86 register context}
     * @throws X86AssemblyException
     */
    private void scrollWindowUp(final IbmPcDisplay display, final Intel80x86 cpu) throws X86AssemblyException {
        // number of lines to scroll in (0=blank entire rectangle)
        final int nLines = cpu.AL.get();

        // video attribute to be used on blank line(s)
        //final int attribute 	= cpu.BH.get();

        // lower-right corner of rectangle to scroll/blank
        //final int row0		= cpu.CH.get();
        //final int col0		= cpu.CL.get();

        // upper-left corner of rectangle to scroll/blank
        //final int row1		= cpu.DH.get();
        //final int col1		= cpu.DL.get();

        // scroll the video
        display.scroll(nLines);
        display.update();
    }

    /**
     * Scroll Down / Clear Screen Rectangle
     *
     * @param display the given {@link IbmPcDisplay display}
     * @param cpu     the given {@link Intel80x86 register context}
     * @throws X86AssemblyException
     */
    private void scrollWindowDown(final IbmPcDisplay display, final Intel80x86 cpu) throws X86AssemblyException {
        // number of lines to scroll in (0=blank entire rectangle)
        final int nLines = cpu.AL.get();

        // video attribute to be used on blank line(s)
        //final int attribute 	= cpu.BH.get();

        // lower-right corner of rectangle to scroll/blank
        //final int row0		= cpu.CH.get();
        //final int col0		= cpu.CL.get();

        // upper-left corner of rectangle to scroll/blank
        //final int row1		= cpu.DH.get();
        //final int col1		= cpu.DL.get();

        // scroll the video
        display.scroll(nLines);
    }

    /**
     * Read Character/Attribute at Cursor Location
     *
     * @param display the given {@link IbmPcDisplay display}
     * @param cpu     the given {@link Intel80x86 register context}
     * @throws X86AssemblyException
     */
    private void readCharacterOrAttribute(final IbmPcDisplay display, final Intel80x86 cpu) throws X86AssemblyException {
        // get display page
        final int page = cpu.BH.get();

        // get the current cursor position
        final int offset = display.getCursorPosition(page);

        // get the character/attribute value
        final int charAttr = display.getCharacterAttribute(offset);

        // if the display mode is graphical ...
        if (display.getDisplayMode().isGraphical()) {
            cpu.AL.set(charAttr);
        }
        // must be text-based
        else {
            // set AL=character, BH=attribute
            cpu.AL.set((charAttr & 0xFF00) >> 4);
            cpu.BH.set(charAttr & 0x00FF);
        }
    }

    /**
     * Write Character/Attribute to Cursor Location
     *
     * @param display the given {@link IbmPcDisplay display}
     * @param cpu     the given {@link Intel80x86 register context}
     * @throws X86AssemblyException
     */
    private void writeCharacterOrAttribute(final IbmPcDisplay display, final Intel80x86 cpu) throws X86AssemblyException {
        // get the display mode
        final IbmPcDisplayMode mode = display.getDisplayMode();
        final int step = mode.isGraphical() ? 1 : 2;

        // get the character, display page, and attribute
        final int chra = cpu.AL.get();
        final int page = cpu.BH.get();
        final int attr = cpu.BL.get();
        int count = cpu.CX.get();

        // get cursor position
        int offset = display.getCursorPosition(page);

        // write the character(s)
        while (count-- > 0) {
            final int charAttr = mode.isGraphical()
                    ? chra << 4 | attr
                    : chra;
            display.setCharacterAttribute(offset, charAttr);
            offset += step;
        }
    }

    /**
     * <pre>
     * Write Text in Teletype Mode (TTY)
     * AH = 0E
     * AL = ASCII character to write
     * BH = page number (text modes)
     * BL = foreground pixel color (graphics modes)
     * Returns nothing
     * - cursor advances after write
     * - characters BEL (7), BS (8), LF (A), and CR (D) are
     * 	 treated as control codes
     * - for some older BIOS (10/19/81), the BH register must point
     * 	 to the currently displayed page
     * - on CGA adapters this function can disable the video signal while
     * 	 performing the output which causes flitter.
     * </pre>
     *
     * @param display the given {@link IbmPcDisplay display}
     * @param cpu     the given {@link Intel80x86 registers}
     * @throws X86AssemblyException
     */
    private void writeCharacterAsTTY(final IbmPcDisplay display, final Intel80x86 cpu) throws X86AssemblyException {
        // get the display page number
        final int page = cpu.BH.get();

        // get the foreground pixel color
        final int attr = cpu.BL.get();

        // get the character to print
        final int chr = cpu.AL.get();

        // write the character
        display.writeCharacter(page, chr, attr, true);
    }

    /**
     * Sets the color palette (graphics mode) or border (text mode)
     *
     * @param display the given {@link IbmPcDisplay display}
     * @param cpu     the given {@link Intel80x86 registers}
     * @throws X86AssemblyException
     */
    private void setColorPaletteOrBorder(final IbmPcDisplay display, final Intel80x86 cpu) throws X86AssemblyException {
        // get the parameters
        final int color = cpu.BL.get(); // 0..3/15
        final int mode = cpu.BH.get(); // 0 or 1

        switch (mode) {
            // set border color?
            case 0:
                display.setColor(new IbmPcColorSet(-1, -1, color));
                break;

            // set palette?
            case 1:
                display.setColor(new IbmPcColorSet(-1, -1, color));
                break;

            // unrecognized
            default:
                throw new X86AssemblyException(String.format("Invalid mode (mode = %02X)", mode));
        }
    }

    /**
     * Sets the specified graphics pixel
     *
     * @param display the given {@link IbmPcDisplay display}
     * @param cpu     the given {@link Intel80x86 register context}
     * @throws X86AssemblyException
     */
    private void writeGraphicsPixel(final IbmPcDisplay display, final Intel80x86 cpu) throws X86AssemblyException {
        // get input parameters
        final int color = cpu.AL.get();    // 0..3/15/255
        final int column = cpu.CX.get();    // 0..319/639
        final int row = cpu.DX.get();    // 0..199/479

        // draw the pixel
        display.writePixel(column, row, color);
    }

    /**
     * Reads the specified graphics pixel
     *
     * @param display the given {@link IbmPcDisplay display}
     * @param cpu     the given {@link Intel80x86 register context}
     * @throws X86AssemblyException
     */
    private void readGraphicsPixel(final IbmPcDisplay display, final Intel80x86 cpu) throws X86AssemblyException {
        // get input parameters
        final int column = cpu.CX.get(); // 0..319/639
        final int row = cpu.DX.get(); // 0..199/479

        // return the color in AL
        final int color = display.readPixel(column, row);
        cpu.AL.set(color);
    }

    /**
     * Query Current Video Info
     *
     * @param display the given {@link IbmPcDisplay display}
     * @param cpu     the given {@link Intel80x86 register context}
     * @throws X86AssemblyException
     */
    private void readVideoMode(final IbmPcDisplay display, final Intel80x86 cpu) throws X86AssemblyException {
        // get the display mode
        final IbmPcDisplayMode mode = display.getDisplayMode();

        // set AL=mode, AH=columns, BH=page
        cpu.AL.set(mode.getVideoMode());
        cpu.AH.set(mode.getColumns());
        cpu.BH.set(display.getActivePage());
    }

    /////////////////////////////////////////////////////
    //      Internal Service Method(s)
    /////////////////////////////////////////////////////

    /**
     * Returns a mapping of available display modes
     *
     * @return a {@link Map mapping} of available {@link IbmPcDisplayMode display modes}
     */
    private static Map<Integer, IbmPcDisplayMode> getAvailableDisplayModes() {
        final Map<Integer, IbmPcDisplayMode> map = new HashMap<Integer, IbmPcDisplayMode>();
        for (final IbmPcDisplayMode mode : DISPLAY_MODES) {
            map.put(mode.getVideoMode(), mode);
        }
        return map;
    }

}
