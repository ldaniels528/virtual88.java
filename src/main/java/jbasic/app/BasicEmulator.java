package jbasic.app;

import ibmpc.devices.display.IbmPcDisplay;
import ibmpc.devices.display.IbmPcDisplayFrame;
import ibmpc.devices.keyboard.IbmPcKeyboard;
import jbasic.common.program.JBasicRuntime;
import jbasic.gwbasic.GwBasicEnvironment;
import jbasic.gwbasic.program.GwBasicProgram;
import jbasic.gwbasic.program.GwBasicRuntime;
import jbasic.gwbasic.program.GwBasicStatement;

/**
 * BASICA/GWBASIC-compatible Language operating environment
 * implemented in pure Java
 *
 * @author lawrence.daniels@gmail.com
 */
public class BasicEmulator {
    private static final String VERSION = "0.431";
    private static final String APP_TITLE = String.format("JBasicOS version %s", VERSION);
    private final GwBasicEnvironment environment;
    private final GwBasicProgram program;
    private final IbmPcKeyboard keyboard;
    private final IbmPcDisplay display;
    private final IbmPcDisplayFrame frame;
    private final JBasicRuntime runtime;
    private boolean alive;

    // /////////////////////////////////////////////////////
    // Constructor(s)
    // /////////////////////////////////////////////////////

    /**
     * Creates an instance of JBasic Operating System
     */
    public BasicEmulator() {
        // create the main applications window
        this.frame = new IbmPcDisplayFrame(APP_TITLE);

        // create the GWBASIC Environment
        this.environment = new GwBasicEnvironment(frame);

        // create the memory resident program
        this.program = environment.getProgram();

        // get the GWBASIC runtime instance
        this.runtime = GwBasicRuntime.getInstance();

        // get a reference to the screen
        this.display = environment.getDisplay();

        // get a reference to the keyboard
        this.keyboard = environment.getKeyboard();
    }

    // /////////////////////////////////////////////////////
    // Main Method
    // /////////////////////////////////////////////////////

    /**
     * Starts this class as a stand alone application
     *
     * @param args the given command line arguments
     * @throws Throwable
     */
    public static void main(final String[] args) throws Throwable {
        (new BasicEmulator()).execute();
    }

    // /////////////////////////////////////////////////////
    // Service Method(s)
    // /////////////////////////////////////////////////////

    /**
     * Orchestrates the execution process
     */
    public void execute() {
        // show the program header
        displayVersionAndCopyright();

        // start interactive session
        interactive();
    }

    /**
     * Causes the interactive process to be stopped
     */
    public void shutdown() {
        this.alive = false;
    }

    // /////////////////////////////////////////////////////
    // Internal Service Method(s)
    // /////////////////////////////////////////////////////

    /**
     * Displays the program version and copyright information
     */
    private void displayVersionAndCopyright() {
        // get the free memory
        final long freeMemory = environment.getMemoryManager().getUnallocatedAmount();

        // clear the display
        display.clear();

        // display the version and copyright info
        display.writeLine(APP_TITLE);
        display.writeLine("Author: Lawrence L. Daniels");
        display.writeLine("Copyright (C) Aug 2004 IntelligentXChange, Inc.");
        display.writeLine("All rights reserved");

        // display the free memory info
        display.writeLine(String.format("%d Bytes free", freeMemory));
        display.update();
    }

    /**
     * Interactive session with user (Main Loop)
     */
    private void interactive() {
        // cycle indefinitely
        this.alive = true;
        while (alive) {
            // show ready prompt
            display.writeLine("Ok");
            display.update();

            // get user input from console
            final String code = keyboard.nextLine();

            // interpret the code
            interpret(code);
        }
    }

    /**
     * Interprets the given code fragment
     *
     * @param code the given code that is to be interpreted
     */
    private void interpret(final String code) {
        // create a statement for the code fragment
        final GwBasicStatement statement = new GwBasicStatement(code);

        // execute the statement
        try {
            runtime.evaluate(program, statement);
        } catch (final Exception e) {
            display.writeLine(e.toString());
            e.printStackTrace();
        }
    }

}
