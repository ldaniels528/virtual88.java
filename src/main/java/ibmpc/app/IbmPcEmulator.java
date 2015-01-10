package ibmpc.app;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.ProgramArguments;
import ibmpc.devices.cpu.ProgramContext;
import ibmpc.devices.display.IbmPcDisplay;
import ibmpc.devices.display.IbmPcDisplayFrame;
import ibmpc.devices.keyboard.IbmPcKeyboard;
import ibmpc.devices.memory.IbmPcRandomAccessMemory;
import ibmpc.devices.memory.X86MemoryProxy;
import ibmpc.exceptions.X86AssemblyException;
import ibmpc.system.IbmPcSystemXT;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static java.lang.String.format;

/**
 * IBM PC MS-DOS Emulator
 *
 * @author lawrence.daniels@gmail.com
 */
public class IbmPcEmulator {
    private static final String VERSION = "0.431";
    private final IbmPcDisplayFrame frame;
    private final IbmPcSystemXT system;
    private final IbmPcRandomAccessMemory memory;
    private final X86MemoryProxy proxy;
    private final IbmPcDisplay display;
    private final IbmPcKeyboard keyboard;
    private final Intel80x86 cpu;

    /**
     * Default constructor
     */
    public IbmPcEmulator() {
        this.frame = new IbmPcDisplayFrame(String.format("JBasic - IBM PC Emulation Mode v%s", VERSION));
        this.system = new IbmPcSystemXT(frame);

        // get references to all devices
        this.cpu = system.getCPU();
        this.memory = system.getRandomAccessMemory();
        this.display = system.getDisplay();
        this.keyboard = system.getKeyboard();

        // create debug helper objects
        this.proxy = new X86MemoryProxy(memory, 0x13F0, 0x0100);

        // (un)set the CPU into virtual mode
        cpu.FLAGS.setVM(false);
    }

    /**
     * IBM PC Emulator application
     *
     * @param args the given command line arguments
     * @throws Throwable
     */
    public static void main(final String[] args) throws Throwable {
        // check the command line arguments
        if (args.length < 1) {
            throw new IllegalArgumentException(format("%s <binary.com>", IbmPcEmulator.class.getName()));
        }

        // load the .COM executable
        final IbmPcEmulator ibmPC = new IbmPcEmulator();
        ibmPC.execute(new File(args[0]));
    }

    /**
     * Executes the given .COM file
     *
     * @param binaryFile the given MS-DOS .COM binary file
     */
    public void execute(final File binaryFile) throws IOException, X86AssemblyException {
        // load the MS-DOS .COM binary file from disk
        final byte[] code = loadBinary(binaryFile);

        // initialize the CPU
        final ProgramContext pc = new ProgramContext(proxy.getSegment(), proxy.getSegment(), proxy.getOffset(), new ProgramArguments[0]);
        memory.setBytes(proxy.getSegment(), proxy.getOffset(), code, code.length);
        cpu.execute(pc);
    }

    /**
     * Loads the binary from disk
     *
     * @param binaryFile the given MS-DOS .COM binary file
     * @return the contents of the file
     * @throws IOException
     */
    private byte[] loadBinary(final File binaryFile) throws IOException {
        try (FileInputStream in = new FileInputStream(binaryFile)) {
            ByteArrayOutputStream out = new ByteArrayOutputStream((int) binaryFile.length());
            byte[] buf = new byte[1024];
            int count;
            while ((count = in.read(buf)) != -1) {
                out.write(buf, 0, count);
            }
            return out.toByteArray();
        }
    }

}
