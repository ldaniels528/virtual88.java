package org.ldaniels528.javapc.ibmpc.devices.display;

import org.ldaniels528.javapc.ibmpc.devices.bios.IbmPcBIOS;
import org.ldaniels528.javapc.ibmpc.devices.display.modes.IbmPcDisplayMode;

import java.awt.*;

/**
 * Represents an IBM PC Video Display capable
 * of CGA, EGA, and VGA (Text and Graphics) Modes.
 *
 * @author lawrence.daniels@gmail.com
 */
public class IbmPcVideoDisplay implements IbmPcDisplay {
    protected final IbmPcDisplayContext dc;
    protected IbmPcDisplayMode mode;

    /**
     * Creates an instance of this color display
     *
     * @param bios the given {@link IbmPcBIOS BIOS} instance
     * @param mode the given {@link IbmPcDisplayMode display mode} instance
     */
    public IbmPcVideoDisplay(final IbmPcBIOS bios, final IbmPcDisplayMode mode) {
        this.dc = new IbmPcDisplayContext(bios);
        setDisplayMode(mode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void backspace() {
        mode.backspace(dc);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        mode.clear(dc);
        dc.position = 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init(final IbmPcDisplayFrame frame) {
        dc.frame = frame;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getActivePage() {
        return dc.activePage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setActivePage(final int page) {
        dc.activePage = page;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setColor(final IbmPcColorSet colorSet) {
        dc.color = colorSet;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IbmPcDisplayContext getContext() {
        return dc;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void copyPage(final int sourcePage, final int targetPage) {
        mode.copyPage(dc, sourcePage, targetPage);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCursorPosition() {
        return dc.activePage * mode.getPageSize() + dc.position;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCursorPosition(final int displayPage) {
        return displayPage * mode.getPageSize() + dc.position;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCursorPosition(final int position) {
        dc.position = position;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCursorPosition(final int column, final int row) {
        dc.position = mode.getCursorPosition(dc, column, row);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IbmPcDisplayMode getDisplayMode() {
        return mode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDisplayMode(final IbmPcDisplayMode newMode) {
        this.mode = newMode;
        dc.activePage = 0;
        dc.position = 0;
        mode.clear(dc);
        mode.updateVirtualBIOS(dc);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void newLine() {
        mode.newLine(dc);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void scroll(final int nRows) {
        mode.scroll(dc, nRows);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update() {
        if (dc.frame != null) {
            // get the offscreen graphics context
            final Graphics2D offscreen = dc.frame.getOffScreen();

            // copy the text in cache to screen
            mode.render(dc, offscreen);

            // blit to the screen
            dc.frame.blit();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeCharacter(final int displayPage,
                               final int character,
                               final boolean moveCursor) {
        dc.position = mode.writeCharacter(dc, displayPage, character, moveCursor);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeCharacter(final int displayPage,
                               final int character,
                               final int attribute,
                               final boolean moveCursor) {
        dc.position = mode.writeCharacter(dc, displayPage, character, attribute, moveCursor);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(final String text) {
        dc.position = mode.writeText(dc, dc.position, text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeLine(final String text) {
        mode.writeText(dc, dc.position, text);
        mode.newLine(dc);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeXY(int column, int row, String text) {
        final int position = mode.getCursorPosition(dc, column, row);
        mode.writeText(dc, position, text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCharacterAttribute(final int offset) {
        return mode.isGraphical()
                ? dc.memory.getByte(mode.getMemorySegment(), offset)
                : dc.memory.getWord(mode.getMemorySegment(), offset);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCharacterAttribute(final int offset, final int charAttr) {
        if (mode.isGraphical()) {
            dc.memory.setByte(mode.getMemorySegment(), offset, (byte) charAttr);
        } else {
            dc.memory.setWord(mode.getMemorySegment(), offset, charAttr);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getVideoOffset(final int column, final int row) {
        return mode.getCursorPosition(dc, column, row);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void drawLine(final int x1, final int y1, final int x2, final int y2, final int colorIndex) {
        mode.drawLine(dc, x1, y1, x2, y2, colorIndex);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writePixel(final int x, final int y, final int colorIndex) {
        mode.drawPixel(dc, x, y, colorIndex);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int readPixel(final int x, final int y) {
        return mode.readPixel(dc, x, y);
    }

}
