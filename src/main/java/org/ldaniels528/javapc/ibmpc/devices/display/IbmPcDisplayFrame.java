package org.ldaniels528.javapc.ibmpc.devices.display;

import javax.swing.*;
import java.awt.*;

/**
 * IBM PC Display Frame
 *
 * @author lawrence.daniels@gmail.com
 * @see org.ldaniels528.javapc.ibmpc.devices.display.IbmPcDisplay
 */
@SuppressWarnings("serial")
public class IbmPcDisplayFrame extends JFrame {
    private final Graphics2D offScreen;
    private final Graphics2D theScreen;
    private final Image buffer;
    private final int paneHeight;
    private final int paneWidth;
    private IbmPcDisplay display;

    /**
     * Default Constructor
     *
     * @throws HeadlessException
     */
    public IbmPcDisplayFrame(final String title)
            throws HeadlessException {
        super(title);

        // calculate screen extents
        this.paneWidth = 640;
        this.paneHeight = 400;

        // setup the frame
        super.setDefaultCloseOperation(EXIT_ON_CLOSE);
        super.setBackground(Color.BLACK);
        super.setSize(getScreenSize(paneWidth, paneHeight));
        super.setResizable(false);
        super.setVisible(true);

        // get graphics context
        this.buffer = super.createImage(paneWidth, paneHeight);
        this.theScreen = (Graphics2D) super.getContentPane().getGraphics();
        this.offScreen = (Graphics2D) buffer.getGraphics();
    }

    /**
     * @return the off-screen image buffer.
     */
    public Image getBuffer() {
        return buffer;
    }

    /**
     * @return the off-screen graphics context.
     */
    public Graphics2D getOffScreen() {
        return offScreen;
    }

    /**
     * Attaches the given screen and uses it to update this frame.
     *
     * @param display the given {@link IbmPcDisplay display}
     */
    public void attach(final IbmPcDisplay display) {
        this.display = display;
    }

    /**
     * Blits the off-screen context to the screen
     */
    public void blit() {
        theScreen.drawImage(buffer, 0, 0, this);
    }

    /**
     * @return Returns the paneWidth.
     */
    public int getPaneWidth() {
        return paneWidth;
    }

    /**
     * @return Returns the paneHeight.
     */
    public int getPaneHeight() {
        return paneHeight;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void paint(final Graphics g) {
        if (display != null)
            display.update();
        else
            super.paint(g);
    }

    /**
     * Just calls {@link #paint(Graphics)}. This method was overridden to
     * prevent an unnecessary call to clear the background.
     *
     * @param g the Graphics context in which to paint
     */
    @Override
    public void update(final Graphics g) {
        paint(g);
    }

    /**
     * Determines the dimension necessary to provide a fullscreen view
     *
     * @param width the width of the frame
     * @param height the height of the frame
     * @return the {@link Dimension dimension} necessary to provide a fullscreen view
     */
    private Dimension getScreenSize(final int width, final int height) {
        // pack this frame
        super.pack();

        // get screen insets
        final Insets insets = super.getInsets();

        // adjust window to screen size
        final int newWidth = (int) (width + (insets.left + insets.right));
        final int newHeigth = (int) (height + (insets.top + insets.bottom));
        return new Dimension(newWidth, newHeigth);
    }

}