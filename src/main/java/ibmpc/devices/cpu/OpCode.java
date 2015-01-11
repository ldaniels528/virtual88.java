package ibmpc.devices.cpu;

import ibmpc.exceptions.X86AssemblyException;
import ibmpc.system.IbmPcSystem;

/**
 * Represents an Executable OpCode
 *
 * @author lawrence.daniels@gmail.com
 */
public interface OpCode {

    /**
     * Executes the opCode
     *
     * @param system the given {@ink IbmPcSystem IBM PC system}
     * @param cpu    the {@link Intel80x86 Intel 8086} instance
     * @throws X86AssemblyException
     */
    void execute(IbmPcSystem system, Intel80x86 cpu) throws X86AssemblyException;

    /**
     * Indicates that this instruction is a conditional instruction
     * requiring branching logic.
     *
     * @return true, if this instruction is a conditional instruction.
     */
    boolean isConditional();

    /**
     * Indicates whether the instruction is a force redirect, meaning
     * a non-conditional jump or loop.
     *
     * @return true, the instruction is a non-conditional jump or loop.
     */
    boolean isForcedRedirect();

    /**
     * @return the length
     */
    int getLength();

    /**
     * @param length the length to set
     */
    void setLength(int length);

    /**
     * Returns the 80x86 instruction code
     *
     * @return the 80x86 instruction code
     */
    long getInstructionCode();

    /**
     * Sets the 80x86 instruction code
     *
     * @param instructionCode the 80x86 instruction code
     */
    void setInstructionCode(long instructionCode);

}
