package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.bitwise

import java.lang.String.format

import com.ldaniels528.javapc.ibmpc.app.util.OperandHelper._
import org.ldaniels528.javapc.ibmpc.devices.display.IbmPcDisplayFrame
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystemFactory
import org.scalatest.{FunSpec, Matchers}

/**
 * Roll Left (ROL) Specification
 * {{{
 * 		Usage:  ROL     dest,count
 *      Modifies flags: CF OF
 *
 * +---+       +-----------------------+
 * | C |<---+--| 7 <---------------- 0 |<--+
 * +---+    |  +-----------------------+   |
 *          |                              |
 *          +------------------------------+
 *
 *      Rotates the bits in the destination to the left "count" times with
 *      all data pushed out the left side re-entering on the right.  The
 *      Carry Flag will contain the value of the last bit rotated out.
 *
 *                                Clocks                 Size
 *      Operands         808x  286   386   486          Bytes
 *
 *      reg,1             2     2     3     3             2
 *      mem,1           15+EA   7     7     4            2-4  (W88=23+EA)
 *      reg,CL           8+4n  5+n    3     3             2
 *      mem,CL        20+EA+4n 8+n    7     4            2-4  (W88=28+EA+4n)
 *      reg,immed8        -    5+n    3     2             3
 *      mem,immed8        -    8+n    7     4            3-5
 * }}}
 * @author lawrence.daniels@gmail.com
 */
class ROL_Spec() extends FunSpec with Matchers {

  describe("An 8086 Roll Left (ROL) instruction") {

    it("Should correctly rotate bits from right to left") {
      val frame = new IbmPcDisplayFrame("Test PC")
      val system = IbmPcSystemFactory.getIBMPCjr(frame)
      val cpu = system.getCPU

      // set the initial value of AL
      cpu.AL.set(1)

      // create a re-usable ROL instruction
      val opCode = new ROL(cpu.AL, 1.db)

      // perform the ROL 8 times
      for (n <- 1 to 8) {
        cpu.execute(system, opCode)
        val bits = format("%8s", Integer.toBinaryString(cpu.AL.get)).replace(' ', '0')
        info(f"$n%02d $bits - ${cpu.FLAGS.toString}")

        // validate the bits in AL and the Carry flag
        n match {
          case 8 =>
            cpu.AL.get shouldBe 1
            cpu.FLAGS.isCF shouldBe true
          case _ =>
            cpu.AL.get shouldBe (1 << n)
            cpu.FLAGS.isCF shouldBe false
        }
      }
    }
  }

}
