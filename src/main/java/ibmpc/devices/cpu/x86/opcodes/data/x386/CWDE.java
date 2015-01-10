package ibmpc.devices.cpu.x86.opcodes.data.x386;

import ibmpc.devices.cpu.Intel80x86;
import ibmpc.devices.cpu.x86.opcodes.AbstractOpCode;

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
public class CWDE extends AbstractOpCode {
    private static CWDE instance = new CWDE();

    /**
     * Private constructor
     */
    private CWDE() {
        super();
    }

    /**
     * @return the singleton instance of this class
     */
    public static CWDE getInstance() {
        return instance;
    }

    /*
     * (non-Javadoc)
     * @see ibmpc.devices.cpu.OpCode#execute(ibmpc.devices.cpu.Intel80x86)
     */
    public void execute(final Intel80x86 cpu) {
        // get the signed 16-bit integer from AX
        final int signedInt16 = cpu.AX.get();

        // get the sign of the 16-bit integer
        // mask 1000 0000 0000 0000
        final int high16 = (signedInt16 & 0x8000);

        // get the unsigned 16-bit integer
        // mask 0111 1111 1111 1111
        final int low16 = (signedInt16 & 0x7FFF);

        // build a signed 32-bit integer
        // code x... .... .... .... xxxx xxxx xxxx xxxxx
        // mask 1000 0000 0000 00000 0000 0000 0000 00000
        final int signedInt32 = (high16 << 16) | low16;

        // put the 32-bit signed integer into EAX
        cpu.EAX.set(signedInt32);
    }

}
