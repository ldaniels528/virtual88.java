package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.system;

import org.ldaniels528.javapc.ibmpc.devices.cpu.Intel8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;
import org.ldaniels528.javapc.ibmpc.exceptions.X86AssemblyException;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

/**
 * <pre>
 * Interrupt
 *
 * Usage:  INT     num
 * Modifies flags: TF IF
 *
 * Initiates a software interrupt by pushing the flags, clearing the
 * Trap and Interrupt Flags, pushing CS followed by IP and loading
 * CS:IP with the value found in the interrupt vector table.  Execution
 * then begins at the location addressed by the new CS:IP
 *
 * See <a href="http://www.shsu.edu/~csc_tjm/spring2001/cs272/interrupt.html">Interrupt Detail</a>
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 */
public class INT extends AbstractOpCode {
    // define the special interrupts
    public static final INT DIVBYZR = new INT(0); // Divide By Zero interrupt
    public static final INT BREAKPT = new INT(3); // Break Point interrupt
    public static final INT SYSTIMR = new INT(8); // IRQ0 System Timer interrupt
    public static final INT KEYBRD = new INT(8); // IRQ1 Keyboard interrupt

    // internal fields
    private final int interruptNumber;

    /**
     * Creates a new interrupt instruction
     *
     * @param interruptNumber the interrupt number to execute
     */
    public INT(final int interruptNumber) {
        this.interruptNumber = interruptNumber;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(final IbmPcSystem system, final Intel8086 cpu) throws X86AssemblyException {
        system.getBIOS().invoke(system, cpu, this);
    }

    /**
     * @return the interrupt number
     */
    public int getInterruptNumber() {
        return interruptNumber;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format("INT %02X", interruptNumber);
    }

}
