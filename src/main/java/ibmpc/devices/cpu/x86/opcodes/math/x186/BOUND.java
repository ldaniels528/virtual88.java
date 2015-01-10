package ibmpc.devices.cpu.x86.opcodes.math.x186;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.operands.Operand;
import ibmpc.devices.cpu.x86.opcodes.AbstractDualOperandOpCode;
import ibmpc.devices.cpu.x86.opcodes.system.INT;
import ibmpc.devices.cpu.x86.registers.X86Register;
import ibmpc.devices.memory.IbmPcRandomAccessMemory;
import ibmpc.exceptions.X86AssemblyException;

/**
 * <pre>
 * BOUND -- Check Array Index Against Bounds (80188+)
 *
 * Description
 *
 * BOUND ensures that a signed array index is within the limits specified
 * by a block of memory consisting of an upper and a lower bound. Each
 * bound uses one word for an operand-size attribute of 16 bits and a
 * doubleword for an operand-size attribute of 32 bits. The first operand
 * (a register) must be greater than or equal to the first bound in memory
 * (lower bound), and less than or equal to the second bound in memory
 * (upper bound). If the register is not within bounds, an Interrupt 5 occurs;
 * the return EIP points to the BOUND instruction.
 *
 * The bounds limit data structure is usually placed just before the array itself,
 * making the limits addressable via a constant offset from the beginning of the array.
 *
 * Opcode    Instruction          Clocks    Description
 * 62 /r     BOUND r16,m16&16     10        Check if r16 is within bounds (passes test)
 * 62 /r     BOUND r32,m32&32     10        Check if r32 is within bounds (passes test)
 *
 * Operation
 *
 * IF (LeftSRC < [RightSRC] OR LeftSRC > [RightSRC + OperandSize/8])
 * 		(* Under lower bound or over upper bound *)
 * 		THEN Interrupt 5;
 * FI;
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 */
public class BOUND extends AbstractDualOperandOpCode {
    private final INT interrupt05 = new INT(5);

    /**
     * BOUND dst, src
     *
     * @param dest the given {@link X86Register destination}
     * @param src  the given {@link Operand source}
     */
    public BOUND(final X86Register dest, final Operand src) {
        super("BOUND", dest, src);
    }

    /*
     * (non-Javadoc)
     * @see ibmpc.devices.cpu.OpCode#execute(ibmpc.devices.cpu.Intel80x86)
     */
    public void execute(final Intel80x86 cpu)
            throws X86AssemblyException {
        // get the memory instance
        final IbmPcRandomAccessMemory memory = cpu.getRandomAccessMemory();

        // get value, size, and upper and lower bounds
        final int size = dest.size();
        final int value = dest.get();
        final int lowerBound = memory.getOperand(cpu.DS.get(), src.get(), size);
        final int upperBound = memory.getOperand(cpu.DS.get(), src.get() + size / 8, size);

        // is it within the bounds?
        if ((value < lowerBound) || (value > upperBound)) {
            interrupt05.execute(cpu);
        }
    }

}
