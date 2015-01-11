package org.ldaniels528.javapc.jbasic.app;

import org.ldaniels528.javapc.JavaPCConstants;
import org.ldaniels528.javapc.jbasic.app.ide.JBasicIDEFrame;
import org.ldaniels528.javapc.jbasic.app.ide.JBasicIdeContext;

import static java.lang.String.format;

/**
 * Represents a PowerBASIC compatible integrated
 * development environment (IDE).
 *
 * @author lawrence.daniels@gmail.com
 */
public class JBasicIDE implements JavaPCConstants {

    /**
     * Default constructor
     */
    public JBasicIDE() {
        super();
    }

    /**
     * Provides stand alone execution
     *
     * @param args the given commandline arguments
     */
    public static void main(String[] args) {
        (new JBasicIDE()).run();
    }

    /**
     * Starts the IDE
     */
    public void run() {
        new JBasicIDEFrame(format("JBasicIDE v%s", VERSION), new JBasicIdeContext());
    }

}
