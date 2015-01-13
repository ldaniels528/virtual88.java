package org.ldaniels528.javapc.ibmpc.devices.cpu;

import org.apache.log4j.Logger;
import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.Operand;
import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.memory.MemoryAddressFAR32;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.decoder.DecodeProcessor;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.decoder.DecodeProcessorImpl;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.system.INT;
import org.ldaniels528.javapc.ibmpc.devices.memory.IbmPcRandomAccessMemory;
import org.ldaniels528.javapc.ibmpc.devices.memory.X86MemoryProxy;
import org.ldaniels528.javapc.ibmpc.exceptions.X86AssemblyException;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

import static java.lang.String.format;
import static org.ldaniels528.javapc.ibmpc.devices.cpu.operands.Operand.SIZE_16BIT;
import static org.ldaniels528.javapc.ibmpc.devices.cpu.operands.Operand.SIZE_8BIT;

/**
 * Emulates an Intel 8086 8/16-bit microprocessor
 *
 * @author lawrence.daniels@gmail.com
 */
public class I8086 extends X86RegisterSet {
    // system timer frequency
    private static final long SYSTEM_TIMER_FREQ = 1000 / 18; // 18 times/sec

    // internal fields
    private final Logger logger = Logger.getLogger(getClass());
    private final IbmPcRandomAccessMemory memory;
    private final DecodeProcessor decoder;
    private final X86Stack stack;
    private X86Register16bit XDS; // 16-bit data segment override register
    private long lastTimerUpdate;
    private boolean ipChanged;
    private boolean active;
    private boolean debugMode = false;

    /////////////////////////////////////////////////////////
    //		Constructor(s)
    /////////////////////////////////////////////////////////

    /**
     * Creates a new i8086 instance
     *
     * @param proxy the {@link X86MemoryProxy memory proxy} instance
     */
    public I8086(final X86MemoryProxy proxy) {
        this.XDS = DS;
        this.memory = proxy.getMemory();
        this.active = true;
        this.stack = new X86Stack(memory, this);
        this.decoder = new DecodeProcessorImpl(this, proxy);
        this.lastTimerUpdate = System.currentTimeMillis();
        this.ipChanged = false;
    }

    /////////////////////////////////////////////////////////
    //		CPU Service Method(s)
    /////////////////////////////////////////////////////////

    /**
     * @return the current program stack
     */
    public X86Stack getStack() {
        return stack;
    }

    /////////////////////////////////////////////////////////
    //		CPU Execution Method(s)
    /////////////////////////////////////////////////////////

    /**
     * Executes the given compiled code
     *
     * @param system  the given {@link IbmPcSystem IBM PC system}
     * @param context the given {@link ProgramContext 80x86 execution context}
     */
    public void execute(final IbmPcSystem system, final ProgramContext context) throws X86AssemblyException {
        // setup the segments
        CS.set(context.getCodeSegment());
        DS.set(context.getDataSegment());
        ES.set(context.getDataSegment());
        SS.set(context.getDataSegment());

        // setup the instruction and stack pointers
        IP.set(context.getCodeOffset());
        SP.set(0xFFFE);

        // push the arguments onto the stack
        final ProgramArguments[] args = context.getArguments();
        if (args != null) {
            for (final ProgramArguments arg : args) {
                stack.pushValue(arg.getOffset());
            }
        }

        // point the decoder to the code segment and offset
        decoder.redirect(context.getCodeSegment(), context.getCodeOffset());

        // continue to decode and execute while active
        while (isActive()) {
            execute(system, getNextOpCode());
        }
    }

    /**
     * Executes the given opCode
     *
     * @param system the given {@link IbmPcSystem IBM PC system}
     * @param opCode the given {@link OpCode opCode}
     * @throws X86AssemblyException
     */
    public void execute(final IbmPcSystem system, final OpCode opCode) throws X86AssemblyException {
        // update the system timer
        updateSystemTimer(system);

        // display the instruction information
        if(debugMode) {
            logger.info(format("E [%04X:%04X] %10X[%d] %s", CS.get(), IP.get(), opCode.getInstructionCode(), opCode.getLength(), opCode));
        }

        // execute the instruction
        opCode.execute(system, this);

        // advance the instruction pointer
        if (!ipChanged) {
            IP.add(opCode.getLength());
        } else {
            ipChanged = false;
        }
    }

    /**
     * Retrieves the next 80x86 opCode from the decoder
     *
     * @return an {@link OpCode opCode}
     */
    public OpCode getNextOpCode() {
        return decoder.decodeNext();
    }

    /////////////////////////////////////////////////////////
    //		CPU State Method(s)
    /////////////////////////////////////////////////////////

    /**
     * Halts the CPU
     */
    public void halt() {
        this.active = false;
    }

    /**
     * Indicates whether the CPU is currently active (executing code).
     *
     * @return true, if the CPU is currently executing code.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Causes the CPU to resume executing code
     */
    public void resume() {
        this.active = true;
    }

    /////////////////////////////////////////////////////////
    //		Memory Access Method(s)
    /////////////////////////////////////////////////////////

    /**
     * @return the random access memory (RAM) instance
     */
    public IbmPcRandomAccessMemory getRandomAccessMemory() {
        return memory;
    }

    /**
     * Resets the register override; pointing back to DS as the default data segment register
     */
    public void resetOverrideRegister() {
        XDS = DS;
    }

    /**
     * Sets the default data segment register as the given register; overriding DS.
     *
     * @param register the given segment register (CS, DS, ES or SS)
     */
    public void setOverrideRegister(final X86Register16bit register) {
        XDS = register;
    }

    /**
     * Retrieves a byte from [DS:offset] in memory
     *
     * @param offset the given offset
     * @return an 8-bit value from memory
     */
    public int getByte(final int offset) {
        final int value = memory.getByte(XDS.get(), offset);
        if(debugMode) {
            logger.info(format("D [%04X:%04X] %13s %02X", XDS.get(), offset, "", value));
        }
        return value;
    }

    /**
     * Sets the byte found at the given offset within the DS segment
     *
     * @param offset the given offset within the DS segment
     * @param value  the 8-bit value to set
     */
    public void setByte(final int offset, final int value) {
        memory.setByte(XDS.get(), offset, (byte) value);
    }

    /**
     * Retrieves a word from [DS:offset] in memory
     *
     * @param offset the given offset
     * @return a 16-bit value from memory
     */
    public int getWord(final int offset) {
        final int value = memory.getWord(XDS.get(), offset);
        if(debugMode) {
            logger.info(format("D [%04X:%04X] %13s data = %04X (%d)", XDS.get(), offset, "", value, value));
        }
        return value;
    }

    /**
     * Sets the word found at the given offset within the DS segment
     *
     * @param offset the given offset within the DS segment
     * @param value  the 16-bit value to set
     */
    public void setWord(final int offset, final int value) {
        memory.setWord(XDS.get(), offset, value);
    }

    /**
     * Retrieves a word from [DS:offset] in memory
     *
     * @param offset the given offset
     * @return a 16-bit value from memory
     */
    public int getDoubleWord(final int offset) {
        final int value = memory.getDoubleWord(XDS.get(), offset);
        if(debugMode) {
            logger.info(format("D [%04X:%04X] %08X", XDS.get(), offset, value));
        }
        return value;
    }

    /**
     * Sets the word found at the given offset
     * within the DS segment
     *
     * @param offset the given offset within the DS segment
     * @param value  the 16-bit value to set
     */
    public void setDoubleWord(final int offset, final int value) {
        memory.setDoubleWord(XDS.get(), offset, value);
    }

    /**
     * Moves the instruction pointer to the given offset within
     * the code segment.
     *
     * @param opCode      the opCode that has issued the jump.
     * @param destination the given {@link Operand destination}
     * @param savePoint   indicates whether the current position should
     *                    be saved to the stack.
     */
    public void jumpTo(final OpCode opCode, final Operand destination, final boolean savePoint) {
        // indicate that the IP address changed
        ipChanged = true;

        // get the pointer value
        final int pointer = destination.get();

        // is it a 32-bit address?
        switch (destination.size()) {
            case SIZE_8BIT:
            case SIZE_16BIT:
                // save this position?
                if (savePoint) {
                    if(debugMode) {
                        logger.info(format("Storing IP as %04X", IP.get()));
                    }
                    stack.pushValue(IP.get() + opCode.getLength());
                }

                // jump to the offset in memory
                IP.set(pointer);
                break;

            default:
                throw new IllegalArgumentException(format("%d-bit memory operands are not supported", destination.size()));
        }
    }

    /**
     * Performs an interrupt call to the given memory destination
     *
     * @param destination the given destination {@link MemoryAddressFAR32 memory address}
     */
    public void invokeInterrupt(final MemoryAddressFAR32 destination) {
        // push the FLAGS, CS, and IP
        stack.push(FLAGS);
        stack.push(CS);
        stack.push(IP);

        // jump to the position in memory
        CS.set(destination.getSegment());
        IP.set(destination.getOffset());

        // pop the IP, CS, and FLAGS
        stack.pop(IP);
        stack.pop(CS);
        stack.pop(FLAGS);
    }

    /**
     * Returns to the given offset within CS
     *
     * @param count the number of elements from the stack to POP
     */
    public void returnNear(final int count) {
        // is the stack is empty, stop executing
        if (stack.isEmpty()) {
            halt();
            return;
        }

        // indicate that the IP address changed
        ipChanged = true;

        // pop "count" values from the stack
        for (int i = 0; i < count; i++) {
            stack.popValue();
        }

        // get the return offset
        final int offset = stack.popValue();

        logger.info(format("Returning NEAR to %04X:%04X", CS.get(), offset));

        // jump to the offset in memory
        IP.set(offset);
    }

    /**
     * Returns to the given segment and offset
     *
     * @param count the number of elements from the stack to POP
     */
    public void returnFar(final int count) {
        // is the stack is empty, stop executing
        if (stack.isEmpty()) {
            this.halt();
            return;
        }

        // indicate that the IP address changed
        ipChanged = true;

        // pop "count" values from the stack
        for (int i = 0; i < count; i++) {
            stack.popValue();
        }

        // get the segment and offset
        final int offset = stack.popValue();
        final int segment = stack.popValue();

        if(debugMode) {
            logger.info(format("Returning FAR to %04X:%04X", segment, offset));
        }

        // jump to the offset in memory
        CS.set(segment);
        IP.set(offset);
    }

    /**
     * Updates the system timer (every 55 milliseconds ~ 18 times/sec)
     *
     * @param system the given {@link IbmPcSystem IBM PC system}
     * @throws X86AssemblyException
     */
    private void updateSystemTimer(final IbmPcSystem system) throws X86AssemblyException {
        // is it time to invoke the system timer?
        final long elapsedSinceUpdate = System.currentTimeMillis() - lastTimerUpdate;
        if ((elapsedSinceUpdate >= SYSTEM_TIMER_FREQ) && FLAGS.isIF()) {
            if(debugMode) {
                logger.info(format("SYSTIMR timer last update %d msec ago", elapsedSinceUpdate));
            }
            INT.SYSTIMR.execute(system, this);
            lastTimerUpdate = System.currentTimeMillis();
        }
    }

}