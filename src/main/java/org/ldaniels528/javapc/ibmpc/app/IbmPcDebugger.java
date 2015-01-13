package org.ldaniels528.javapc.ibmpc.app;

import org.ldaniels528.javapc.JavaPCConstants;
import org.ldaniels528.javapc.ibmpc.devices.cpu.I8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.OpCode;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.decoder.DecodeProcessor;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.decoder.DecodeProcessorImpl;
import org.ldaniels528.javapc.ibmpc.devices.display.IbmPcDisplay;
import org.ldaniels528.javapc.ibmpc.devices.display.IbmPcDisplayFrame;
import org.ldaniels528.javapc.ibmpc.devices.memory.IbmPcRandomAccessMemory;
import org.ldaniels528.javapc.ibmpc.devices.memory.X86MemoryProxy;
import org.ldaniels528.javapc.ibmpc.exceptions.IbmPcException;
import org.ldaniels528.javapc.ibmpc.exceptions.IbmPcNumericFormatException;
import org.ldaniels528.javapc.ibmpc.exceptions.X86AssemblyException;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystemPCjr;
import org.ldaniels528.javapc.util.ResourceHelper;

import java.io.*;
import java.util.*;

import static org.ldaniels528.javapc.ibmpc.util.IbmPcValues.parseHexadecimalString;

/**
 * IBM PC/MS-DOS Debugger
 *
 * @author lawrence.daniels@gmail.com
 */
public class IbmPcDebugger implements JavaPCConstants {
    private final IbmPcRandomAccessMemory memory;
    private final X86MemoryProxy proxy;
    private final DecodeProcessor decoder;
    private final IbmPcSystemPCjr system;
    private final IbmPcDisplay display;
    private final I8086 cpu;
    private final PrintStream out = System.out;
    private boolean firstInstruction = true;
    private String filename;
    private boolean alive;

    /**
     * Default constructor
     */
    public IbmPcDebugger() {
        this.system = new IbmPcSystemPCjr(new IbmPcDisplayFrame(String.format("JavaPC/Debugger v%s", VERSION)));
        this.cpu = system.getCPU();
        this.memory = system.getRandomAccessMemory();
        this.display = system.getDisplay();

        // create debug helper objects
        this.proxy = system.getMemoryProxy();
        this.decoder = new DecodeProcessorImpl(cpu, proxy);
        proxy.setSegment(0x13F0);
        proxy.setOffset(0x100);
    }

    /**
     * For standalone operation
     *
     * @param args the given commandline arguments
     * @throws Throwable
     */
    public static void main(final String[] args) throws Throwable {
        // start the application
        final IbmPcDebugger app = new IbmPcDebugger();

        // display title
        System.out.printf("JavaPC/Debugger v%s\n", VERSION);
        System.out.println("");

        // was an executable file specified?
        if (args.length > 0) {
            // point to the file
            app.filename = args[0];

            // attempt to load the file
            app.loadExecutable(app.filename);
        }

        // start the debugger
        app.run();
    }

    /**
     * Runs the debugger
     *
     * @throws IOException
     * @throws java.lang.InterruptedException
     */
    public void run() throws IbmPcException, InterruptedException, IOException {
        // initialize the display
        display.update();

        // get a console reader
        final BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

        // cycle indefinitely
        alive = true;
        while (alive) {
            System.out.printf("- ");

            // get the next line input
            final String line = console.readLine().trim();

            // interpret the command
            interpret(line);

            // refresh the display
            display.update();
        }
    }

    /**
     * Interprets and executes the given command
     * Commands:
     * <pre>
     *   [d]ump - dump the contents of memory at the current IP position
     *   [f]lags - displays the current state of all flags
     *   [g]o - starts executing code at the current IP position
     *   [f]ileName - sets the current file name (for reading or saving)
     *   [n]ext - executes the next instruction
     *   [q]uit - exits the Debugger
     *   [r]egisters - displays the current state of all registers
     *   [u]nassemble - decodes assembly code at the current IP position
     * </pre>
     *
     * @param line the given line
     */
    private void interpret(final String line) {
        // parse the command and get the parameters
        final List<String> args = Arrays.asList(line.split("[ ]"));
        final String command = args.get(0).toLowerCase();
        final List<String> params = args.size() < 2 ? Collections.<String>emptyList() : args.subList(1, args.size());

        try {
            switch (command) {
                case "d":
                    dump(params);
                    break;
                case "g":
                    executeCode(cpu.IP.get(), 100);
                    break;
                case "f":
                    setFileName(line);
                    break;
                case "n":
                    executeNext();
                    break;
                case "q":
                    stop();
                    break;
                case "r":
                    out.println(cpu);
                    break;
                case "u":
                    unassemble(line, 10);
                    break;
            }
        } catch (X86AssemblyException | IbmPcNumericFormatException e) {
            System.err.printf("Run-time error: %s\n", e.getMessage());
        }
    }

    /**
     * Dumps the specific number of bytes on screen
     * <p/>Syntax1: d [offset] [count]
     * <p/>Syntax2: d [segment:offset] [count]
     *
     * @param params the given command parameters
     */
    private void dump(final List<String> params) {
        final int DUMP_LENGTH = 16;
        final int segment0 = proxy.getSegment();
        final int offset0 = proxy.getOffset();

        int segment = segment0;
        int offset = offset0;
        int count = 128;
        switch (params.size()) {
            case 2:
                count = Integer.parseInt(params.get(1));
            case 1:
                final String[] pcs = params.get(0).split(":");
                switch (pcs.length) {
                    case 2:
                        segment = Integer.parseInt(pcs[0]);
                        offset = Integer.parseInt(pcs[1]);
                        break;
                    case 1:
                        offset = Integer.parseInt(pcs[0]);
                        break;
                }
            case 0:
                break;
        }

        // move to the user defined segment:offset
        proxy.setSegment(segment);
        proxy.setOffset(offset);

        // create a container for lines of data
        final List<String> lines = new LinkedList<>();

        // determine how many lines to display
        final int nLines = count / DUMP_LENGTH;

        // display each line of binary data
        for (int n = 0; n < nLines; n++) {
            final List<Integer> bytes = new ArrayList<>(DUMP_LENGTH);
            for (int m = 0; m < DUMP_LENGTH; m++) {
                bytes.add(proxy.nextByte());
            }
            lines.add(layoutBytes(bytes));
        }

        // display the results
        lines.forEach(out::println);

        // move back to the original segment:offset
        proxy.setSegment(segment0);
        proxy.setOffset(offset0);
    }

    private int[] parseSegmentOffset(final String param) {
        int segment = proxy.getSegment();
        int offset = proxy.getOffset();

        final String[] pcs = param.split(":");
        switch (pcs.length) {
            case 2:
                segment = Integer.parseInt(pcs[0]);
                offset = Integer.parseInt(pcs[1]);
                break;
            case 1:
                offset = Integer.parseInt(pcs[0]);
                break;
        }

        return new int[] { segment, offset };
    }

    private void executeCode(final int offset, final int limit) throws X86AssemblyException {
        int count = 0;

        // point the the offset
        proxy.setOffset(offset);

        // execute instructions until the CPU is halted
        while (count++ < limit && cpu.isActive()) {
            final OpCode opCode = decoder.decodeNext();
            out.printf("[%04X:%04X] %10X[%d] %s\n",
                    cpu.CS.get(), cpu.IP.get(), opCode.getInstructionCode(), opCode.getLength(), opCode);
            cpu.execute(system, opCode);
        }
    }

    private void executeNext() throws X86AssemblyException {
        if (firstInstruction) {
            firstInstruction = false;
            proxy.setOffset(0x100);
        }

        final OpCode opCode = decoder.decodeNext();
        out.printf("[%04X:%04X] %10X[%d] %s\n",
                cpu.CS.get(), cpu.IP.get(), opCode.getInstructionCode(), opCode.getLength(), opCode);
        cpu.execute(system, opCode);
    }

    /**
     * Lays out the given collection of bytes.
     *
     * @param byteCodes the given {@link List collection} of bytes
     * @return the formatted string
     */
    private String layoutBytes(final List<Integer> byteCodes) {
        final StringBuilder sb1 = new StringBuilder(3 * byteCodes.size());
        final StringBuilder sb2 = new StringBuilder(3 * byteCodes.size());

        sb2.append('[');
        for (final Integer byteCode : byteCodes) {
            sb1.append(String.format("%02X ", byteCode));
            sb2.append(String.format((byteCode >= 32 && byteCode <= 255) ? "%c" : ".", byteCode));
        }
        sb2.append(']');
        sb1.append("- ").append(sb2);
        return sb1.toString();
    }

    /**
     * Loads the given executable into memory
     *
     * @param filename the file name of the executable to load
     */
    private void loadExecutable(final String filename) {
        try {
            // dump the content into a buffer
            final byte[] code = ResourceHelper.getBinaryContents(new File(filename));

            // copy the program into memory
            final int codeSegment = proxy.getSegment();
            final int codeOffset = 0x0100;
            memory.setBytes(proxy.getSegment(), codeOffset, code, code.length);

            // point IP to the code
            cpu.CS.set(codeSegment);
            cpu.DS.set(codeSegment);
            cpu.ES.set(codeSegment);
            cpu.SS.set(codeSegment);
            cpu.IP.set(codeOffset);

            out.printf("Loaded %s: %d bytes\n", filename, code.length);
        } catch (final IOException e) {
            out.printf("Unable to load '%s'\n", filename);
        }
    }

    /**
     * Sets the name of a file to be read or written
     *
     * @param command the given command string (e.g. 'nFROGGER.COM')
     */
    private void setFileName(final String command) {
        // get & display the file name
        filename = command.substring(1);
        out.printf("Filename: %s\n", filename);
    }

    /**
     * Stops the debugger
     */
    private void stop() {
        this.alive = false;
    }

    /**
     * Disassemble the specified number of instructions.
     *
     * @param command the given command to disassemble
     * @param count   the specified number of instructions to unassemble.
     * @throws IbmPcNumericFormatException
     */
    private void unassemble(final String command, final int count) throws IbmPcNumericFormatException {
        // check the command arguments
        final String[] args = command.split("[ ]");
        final int origin = (args.length > 1) ? parseHexadecimalString(args[1]) : -1;

        // change the origin?
        if (origin != -1) {
            proxy.setOffset(origin);
        }

        // begin reverse engineering the byte code
        int n = 0;
        while (n++ < count) {
            // capture the segment and offset
            final int segment = proxy.getSegment();
            final int offset = proxy.getOffset();

            // get the next instruction
            final OpCode instruction = decoder.decodeNext();

            // get the byte code for the current instruction
            final String byteCodeString = getByteCodeString(segment, offset);

            // display the instruction
            out.printf("[%04X:%04X] %s %s\n", segment, offset, byteCodeString, instruction);
        }
    }

    /**
     * Creates a byte string
     *
     * @param segment the given segment
     * @param offset  the given start offset
     * @return a byte string
     */
    private String getByteCodeString(final int segment, final int offset) {
        final int MAX_LEN = 12;

        // get the data block
        final int length = proxy.getOffset() - offset;
        final byte[] block = new byte[length];
        memory.getBytes(segment, offset, block, block.length);

        // create a byte string
        final StringBuilder sb = new StringBuilder(MAX_LEN);
        for (final byte b : block) {
            sb.append(String.format("%02X", b));
        }

        // pad up to the maximum size
        while (sb.length() < MAX_LEN) {
            sb.append(' ');
        }

        return sb.toString();
    }

}