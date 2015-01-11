package org.ldaniels528.javapc.ibmpc.app;

import org.ldaniels528.javapc.JavaPCConstants;
import org.ldaniels528.javapc.ibmpc.devices.cpu.Intel8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.OpCode;
import org.ldaniels528.javapc.ibmpc.devices.cpu.ProgramContext;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.decoder.*;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.data.DB;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.data.DW;
import org.ldaniels528.javapc.ibmpc.devices.display.IbmPcDisplay;
import org.ldaniels528.javapc.ibmpc.devices.display.IbmPcDisplayFrame;
import org.ldaniels528.javapc.ibmpc.devices.keyboard.IbmPcKeyboard;
import org.ldaniels528.javapc.ibmpc.devices.memory.IbmPcRandomAccessMemory;
import org.ldaniels528.javapc.ibmpc.devices.memory.X86MemoryProxy;
import org.ldaniels528.javapc.ibmpc.exceptions.IbmPcException;
import org.ldaniels528.javapc.ibmpc.exceptions.IbmPcNumericFormatException;
import org.ldaniels528.javapc.ibmpc.exceptions.X86AssemblyException;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystemXT;
import org.ldaniels528.javapc.util.ResourceHelper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static java.lang.String.format;
import static org.ldaniels528.javapc.ibmpc.util.IbmPcValues.parseHexadecimalString;

/**
 * IBM PC/MS-DOS Debugger
 *
 * @author lawrence.daniels@gmail.com
 */
public class IbmPcDebugger implements JavaPCConstants {
    private final IbmPcRandomAccessMemory memory;
    private final X86MemoryProxy proxy;
    private final DebugDecoder decoder;
    private final IbmPcSystemXT system;
    private final IbmPcDisplay display;
    private final IbmPcKeyboard keyboard;
    private final Intel8086 cpu;
    private String filename;
    private boolean alive;

    /**
     * Default constructor
     */
    public IbmPcDebugger() {
        this.system = new IbmPcSystemXT(new IbmPcDisplayFrame(String.format("Java PC - Debugger v%s", VERSION)));
        this.cpu = system.getCPU();
        this.memory = system.getRandomAccessMemory();
        this.display = system.getDisplay();
        this.keyboard = system.getKeyboard();

        // create debug helper objects
        this.proxy = new X86MemoryProxy(memory, 0x13F0, 0x0100);
        this.decoder = new DebugDecoder(cpu, proxy);
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
        app.outputln(format("JavaPC/Debugger v%s", VERSION));
        app.outputln("");

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
     * @throws org.ldaniels528.javapc.ibmpc.exceptions.IbmPcException
     * @throws java.lang.InterruptedException
     */
    public void run() throws IbmPcException, InterruptedException {
        alive = true;
        while (alive) {
            output("- ");

            // get the next line input
            final String input = keyboard.nextLine();

            // interpret the command
            interpret(input);
        }
    }

    /**
     * Stops the debugger
     */
    public void stop() {
        this.alive = false;
    }

    /**
     * Interprets and executes the given command
     * Commands:
     * <pre>
     *   [d]ump - dump the contents of memory at the current IP position
     *   [f]lags - displays the current state of all flags
     *   [g]o - starts executing code at the current IP position
     *   [n]ame - sets the current file name (for reading or saving)
     *   [q]uit - exits the Debugger
     *   [r]egisters - displays the current state of all registers
     *   [u]nassemble - decodes assembly code at the current IP position
     * </pre>
     *
     * @param command the given command
     */
    private void interpret(final String command) {
        try {
            if (command.startsWith("d")) dump(128);
            else if (command.startsWith("f")) System.out.println(cpu.FLAGS);
            else if (command.startsWith("g")) executeCode(cpu.IP.get());
            else if (command.startsWith("n")) name(command);
            else if (command.startsWith("q")) stop();
            else if (command.startsWith("r")) System.out.println(cpu);
            else if (command.startsWith("u")) unassemble(command, 10);
        } catch (X86AssemblyException | IbmPcNumericFormatException e) {
            System.err.printf("Run-time error: %s\n", e.getMessage());
        }
    }

    /**
     * Dumps the specific number of bytes on screen
     *
     * @param count the specified number of bytes to display
     */
    private void dump(final int count) {
        final int DUMP_LENGTH = 16;

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
        lines.forEach(this::outputln);
    }

    private void executeCode(final int offset) throws X86AssemblyException {
        system.execute(new ProgramContext(proxy.getSegment(), offset, proxy.getSegment(), null));
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

            outputln(String.format("Loaded %s: %d bytes", filename, code.length));
        } catch (final IOException e) {
            outputln(String.format("Unable to load '%s'", filename));
        }
    }

    /**
     * Sets the name of a file to be read or written
     *
     * @param command the given command string (e.g. 'nFROGGER.COM')
     */
    private void name(final String command) {
        // get & display the file name
        filename = command.substring(1);
        System.out.printf("Filename: %s\n", filename);
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
            outputln(String.format("%04X:%04X %s %s", segment, offset, byteCodeString, instruction));
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

    private void output(final String text) {
        display.write(text);
        display.update();

        System.out.print(text);
    }

    private void outputln(final String text) {
        display.writeLine(text);
        display.update();

        System.out.println(text);
    }

    /**
     * Represents a simple 80x86 Decode Processor
     *
     * @author lawrence.daniels@gmail.com
     */
    private class DebugDecoder implements DecodeProcessor {
        private final X86MemoryProxy proxy;
        private final Decoder[] decoders;
        private final Intel8086 cpu;

        /**
         * Creates a new instance decode processor
         *
         * @param cpu   the given {@link org.ldaniels528.javapc.ibmpc.devices.cpu.Intel8086 CPU} instance
         * @param proxy the given {@link X86MemoryProxy memory proxy} instance
         */
        public DebugDecoder(final Intel8086 cpu, final X86MemoryProxy proxy) {
            this.cpu = cpu;
            this.proxy = proxy;
            this.decoders =
                    new Decoder[]{
                            new Decoder00(), new Decoder10(), new Decoder20(), new Decoder30(),
                            new Decoder40(), new Decoder50(), new Decoder60(), new Decoder70(),
                            new Decoder80(), new Decoder90(), new DecoderA0(), new DecoderB0(),
                            new DecoderC0(), new DecoderD0(), new DecoderE0(), new DecoderF0(this)
                    };
        }

        /**
         * {@inheritDoc}
         */
        public OpCode decodeNext() {
            // capture the current offset
            final int offset0 = proxy.getOffset();

            // peek at the next byte
            final int code = proxy.peekAtNextByte();

            // get the first hex digit (mask = 1111 0000)
            final int index = ((code & 0xF0) >> 4);

            // invoke the appropriate interpreter
            OpCode opCode;
            try {
                opCode = decoders[index].decode(cpu, proxy);
            } catch (final Throwable cause) {
                // get the length of the failed instruction
                final int length = proxy.getOffset() - offset0;
                final int bytecode = (int) memory.getBytesAsLong(proxy.getSegment(), offset0, length);
                opCode = (length == 1) ? new DB(bytecode) : new DW(bytecode);
            }

            // get the length of the instruction
            final int codeLength = proxy.getOffset() - offset0;

            // set the instruction code length
            opCode.setLength(codeLength);

            // return the opCode
            return opCode;
        }

        /**
         * {@inheritDoc}
         */
        public void init() {
            // no initialization needed
        }

        /**
         * {@inheritDoc}
         */
        public void redirect(final int segment, final int offset) {
            // no redirection needed
        }

        /**
         * {@inheritDoc}
         */
        public void shutdown() {
            // no shutdown needed
        }
    }

}
