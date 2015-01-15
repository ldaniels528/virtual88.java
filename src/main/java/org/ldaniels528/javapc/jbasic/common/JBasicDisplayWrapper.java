package org.ldaniels528.javapc.jbasic.common;

import org.ldaniels528.javapc.ibmpc.devices.display.IbmPcColorSet;
import org.ldaniels528.javapc.ibmpc.devices.display.IbmPcDisplay;
import org.ldaniels528.javapc.ibmpc.devices.display.IbmPcDisplayContext;
import org.ldaniels528.javapc.ibmpc.devices.display.IbmPcDisplayFrame;
import org.ldaniels528.javapc.ibmpc.devices.display.modes.IbmPcDisplayMode;

/**
 * Represents a BASIC display wrapper.
 *
 * @author lawrence.daniels@gmail.com
 */
public class JBasicDisplayWrapper implements IbmPcDisplay {
    private final IbmPcDisplay display;
    private final JBasicKeyLabels keyLabels;

    /////////////////////////////////////////////////////
    //      Constructor(s)
    /////////////////////////////////////////////////////

    /**
     * Creates an instance of this virtual screen
     *
     * @param display   the given {@link IbmPcDisplay IBM PC-compatible display}
     * @param mode      the given {@link IbmPcDisplayMode display mode}
     * @param keyLabels the BASIC key labels
     */
    public JBasicDisplayWrapper(final IbmPcDisplay display,
                                final IbmPcDisplayMode mode,
                                final JBasicKeyLabels keyLabels) {
        this.display = display;
        this.keyLabels = keyLabels;
        setDisplayMode(mode);
    }

    /////////////////////////////////////////////////////
    //      Service Methods
    /////////////////////////////////////////////////////

    /**
     * {@inheritDoc}
     */
    @Override
    public void backspace() {
        display.backspace();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        display.clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void copyPage(int sourcePage, int targetPage) {
        display.copyPage(sourcePage, targetPage);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void drawLine(int x1, int y1, int x2, int y2, int colorIndex) {
        display.drawLine(x1, y1, x2, y2, colorIndex);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getActivePage() {
        return display.getActivePage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCharacterAttribute(int offset) {
        return display.getCharacterAttribute(offset);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IbmPcDisplayContext getContext() {
        return display.getContext();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCursorPosition() {
        return display.getCursorPosition();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCursorPosition(int displayPage) {
        return display.getCursorPosition(displayPage);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IbmPcDisplayMode getDisplayMode() {
        return display.getDisplayMode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getVideoOffset(int column, int row) {
        return display.getVideoOffset(column, row);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init(IbmPcDisplayFrame frame) {
        display.init(frame);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void newLine() {
        display.newLine();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int readPixel(int x, int y) {
        return display.readPixel(x, y);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void scroll(int nRows) {
        display.scroll(nRows);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setActivePage(int page) {
        display.setActivePage(page);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCharacterAttribute(int offset, int charAttr) {
        display.setCharacterAttribute(offset, charAttr);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setColor(IbmPcColorSet colorSet) {
        display.setColor(colorSet);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCursorPosition(int column, int row) {
        display.setCursorPosition(column, row);
    }

    /////////////////////////////////////////////////////
    //      Service Methods
    /////////////////////////////////////////////////////

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDisplayMode(final IbmPcDisplayMode mode) {
        keyLabels.setColumns(mode.getColumns());
        mode.setCaption(keyLabels.getLabelText());
        display.setDisplayMode(mode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update() {
        display.update();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeCharacter(final int displayPage,
                               final int character,
                               final boolean moveCursor) {
        display.writeCharacter(displayPage, character, moveCursor);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeCharacter(final int displayPage,
                               final int character,
                               final int attribute,
                               final boolean moveCursor) {
        display.writeCharacter(displayPage, character, attribute, moveCursor);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(final String text) {
        display.write(text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeLine(final String text) {
        display.writeLine(text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writePixel(final int x, final int y, final int colorIndex) {
        display.writePixel(x, y, colorIndex);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeXY(final int col, final int row, final String text) {
        display.writeXY(col, row, text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCursorPosition(final int position) {
        display.setCursorPosition(position);
    }

}