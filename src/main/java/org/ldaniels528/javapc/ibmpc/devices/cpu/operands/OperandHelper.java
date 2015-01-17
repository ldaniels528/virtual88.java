package org.ldaniels528.javapc.ibmpc.devices.cpu.operands;

import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.memory.BytePtr;
import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.memory.DWordPtr;
import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.memory.MemoryReference;
import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.memory.WordPtr;

import static java.lang.String.format;
import static org.ldaniels528.javapc.ibmpc.devices.cpu.operands.Operand.*;

/**
 * Operand Convenience Utility
 *
 * @author lawrence.daniels@gmail.com
 */
public class OperandHelper {

    public static Operand getMemoryPointer(final MemoryReference memoryReference, final int size) {
        switch (size) {
            case SIZE_8BIT:
                return new BytePtr(memoryReference);
            case SIZE_16BIT:
                return new WordPtr(memoryReference);
            case SIZE_32BIT:
                return new DWordPtr(memoryReference);
            default:
                throw new IllegalArgumentException(format("Invalid operand size: %d-bits", size));
        }
    }

    public static Operand getEffectiveAddress(final MemoryReference memoryReference, final int size) {
        return new Operand() {
            @Override
            public int get() {
                return memoryReference.getOffset();
            }

            @Override
            public void set(int value) {
                throw new IllegalStateException(format("Effective addresses cannot be set"));
            }

            @Override
            public int size() {
                return size;
            }

            @Override
            public String toString() {
                return memoryReference.toString();
            }
        };
    }

}
