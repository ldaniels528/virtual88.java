package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.data;

import org.ldaniels528.javapc.ibmpc.devices.cpu.I8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

/**
 * <pre>
 * CWD/CDQ -- Convert Word to Doubleword/Convert Doubleword to Quadword
 *
 * Opcode    Instruction        Clocks   Description
 * 99        CWD                2        DX:AX := sign-extend of AX
 * 99        CDQ                2        EDX:EAX := sign-extend of EAX
 *
 * Operation
 * IF OperandSize = 16 (* CWD instruction *)
 * THEN
 *  IF AX < 0 THEN DX := 0FFFFH; ELSE DX := 0; FI;
 *  ELSE (* OperandSize = 32, CDQ instruction *)
 *  IF EAX < 0 THEN EDX := 0FFFFFFFFH; ELSE EDX := 0; FI;
 * FI;
 *
 * Description
 * CWD converts the signed word in AX to a signed doubleword in DX:AX
 * by extending the most significant bit of AX into all the bits of DX.
 * CDQ converts the signed doubleword in EAX to a signed 64-bit integer
 * in the register pair EDX:EAX by extending the most significant bit
 * of EAX (the sign bit) into all the bits of EDX. Note that CWD is
 * different from CWDE. CWDE uses EAX as a destination, instead of DX:AX.
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 */
public class CWD extends AbstractOpCode {
    private static CWD instance = new CWD();

    /**
     * Private constructor
     */
    private CWD() {
        super();
    }

    /**
     * @return the singleton instance of this class
     */
    public static CWD getInstance() {
        return instance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(IbmPcSystem system, final I8086 cpu) {
        // get the signed 16-bit integer from AX
        final int signedInt16 = cpu.AX.get();

        // get the sign of the 16-bit integer
        // mask 1000 0000 0000 0000
        final int high16 = (signedInt16 & 0x8000);

        // get the unsigned 16-bit integer
        // mask 0111 1111 1111 1111
        final int low16 = (signedInt16 & 0x7FFF);

        // put the high portion in DX and the low portion in AX
        cpu.DX.set(high16);
        cpu.AX.set(low16);
    }

}
