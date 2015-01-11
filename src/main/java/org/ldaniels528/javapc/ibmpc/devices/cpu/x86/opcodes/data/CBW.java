package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.data;

import org.ldaniels528.javapc.ibmpc.devices.cpu.Intel8086;
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;

/**
 * <pre>
 * CBW/CWDE -- Convert Byte to Word/Convert Word to Doubleword
 *
 * Opcode    Instruction     Clocks          Description
 * 98        CBW             3               AX := sign-extend of AL
 * 98        CWDE            3               EAX := sign-extend of AX
 *
 * Operation
 * IF OperandSize = 16 (* instruction = CBW *)
 * THEN AX := SignExtend(AL);
 * ELSE (* OperandSize = 32, instruction = CWDE *)
 *    EAX := SignExtend(AX);
 * FI;
 *
 * Description
 * CBW converts the signed byte in AL to a signed word in AX by
 * extending the most significant bit of AL (the sign bit) into
 * all of the bits of AH. CWDE converts the signed word in AX to
 * a doubleword in EAX by extending the most significant bit of AX
 * into the two most significant bytes of EAX. Note that CWDE is
 * different from CWD. CWD uses DX:AX rather than EAX as a destination.
 * </pre>
 *
 * @author lawrence.daniels@gmail.com
 */
public class CBW extends AbstractOpCode {
    private static CBW instance = new CBW();

    /**
     * Private constructor
     */
    private CBW() {
        super();
    }

    /**
     * @return the singleton instance of this class
     */
    public static CBW getInstance() {
        return instance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(IbmPcSystem system, final Intel8086 cpu) {
        // get the signed 8-bit integer from AL
        final int signedInt8 = cpu.AL.get();

        // get the sign of the 8-bit integer
        // mask 1000 0000
        final int high8 = (signedInt8 & 0x80);

        // get the unsigned 8-bit integer
        // mask 0111 1111
        final int low8 = (signedInt8 & 0x7F);

        // build a signed 16-bit integer
        // value x... .... xxxx xxxxx
        final int signedInt16 = (high8 << 8) | low8;

        // put the signed byte from AL as a signed word in AX
        cpu.AX.set(signedInt16);
    }

}
