package com.ldaniels528.javapc.ibmpc.app

import java.io._

import com.ldaniels528.javapc.ibmpc.app.CommandParser.UnixLikeArgs
import com.ldaniels528.javapc.ibmpc.app.Debugger._
import com.ldaniels528.javapc.ibmpc.app.util.OptionHelper._
import org.ldaniels528.javapc.JavaPCConstants
import org.ldaniels528.javapc.ibmpc.devices.cpu._
import org.ldaniels528.javapc.ibmpc.devices.cpu.decoders.{DecodeProcessorImpl, FlowControlCallBackOpCode}
import org.ldaniels528.javapc.ibmpc.devices.cpu.opcodes._
import org.ldaniels528.javapc.ibmpc.devices.cpu.opcodes.addressing.DataSegmentOverride
import org.ldaniels528.javapc.ibmpc.devices.cpu.opcodes.flags.AbstractFlagUpdateOpCode
import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.Operand
import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.memory.{MemoryAddressNEAR16, MemoryPointer}
import org.ldaniels528.javapc.ibmpc.devices.cpu.registers.{X86CompositeRegister16Bit, X86Flags, X86Register16bit, X86Register8bit}
import org.ldaniels528.javapc.ibmpc.devices.display.IbmPcDisplayFrame
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystemFactory
import org.ldaniels528.javapc.util.ResourceHelper

import scala.util.{Failure, Success, Try}

/**
 * IBM PC/MS-DOS Debugger
 * @author lawrence.daniels@gmail.com
 */
class Debugger() {
  private val out = System.out
  private var lastExecutePtr = (0x13F0, 0x100)
  private var alive: Boolean = false

  // create a PC instance
  val system = IbmPcSystemFactory.getIBMPCjr(new IbmPcDisplayFrame(s"JavaPC/Debugger v${JavaPCConstants.VERSION}"))
  val cpu = system.getCPU
  val memory = system.getRandomAccessMemory
  val display = system.getDisplay

  val registers = Map(
    "AX" -> cpu.AX, "BX" -> cpu.BX, "CX" -> cpu.CX, "DX" -> cpu.DX,
    "SI" -> cpu.SI, "DI" -> cpu.DI, "BP" -> cpu.BP, "SP" -> cpu.SP,
    "CS" -> cpu.CS, "DS" -> cpu.DS, "ES" -> cpu.ES, "SS" -> cpu.SS
  )

  val flags = Map(
    "AF" -> cpu.FLAGS.AF, "CF" -> cpu.FLAGS.CF, "DF" -> cpu.FLAGS.DF,
    "IF" -> cpu.FLAGS.IF, "OF" -> cpu.FLAGS.OF, "PF" -> cpu.FLAGS.PF,
    "SF" -> cpu.FLAGS.SF, "TF" -> cpu.FLAGS.TF, "ZF" -> cpu.FLAGS.ZF
  )

  // create debug helper objects
  val proxy = system.getMemoryProxy
  val decoder = new DecodeProcessorImpl(cpu, proxy)

  // point the a stable segment and offset
  proxy.setPointer(lastExecutePtr._1, lastExecutePtr._2)

  /**
   * Application entry point
   */
  def run() {
    // initialize the display
    display.update()

    // get a console reader
    val console = new BufferedReader(new InputStreamReader(System.in))

    // cycle indefinitely
    alive = true
    while (alive) {
      System.out.printf("- ")

      // interpret and execute the command, then update the PC's display
      Option(console.readLine) map (_.trim) foreach { line =>
        interpret(line)
        display.update()
      }
    }
  }

  /**
   * Interprets and executes the given command
   * Commands:
   * <pre>
   * [a]sm - decodes assembly code at the current IP position
   * [d]ump - dump the contents of memory at the current IP position
   * [f]lags - displays the current state of all flags
   * [g]o - starts executing code at the current IP position
   * [l]oad - sets the current file name (for reading or saving)
   * [n]ext - executes the next instruction
   * [q]uit - exits the Debugger
   * [r]egs - displays the current state of all registers
   * </pre>
   *
   * @param line the given line of input
   */
  private def interpret(line: String) {
    if (line.nonEmpty) {
      val tokens = CommandParser.parseTokens(line)
      val (command, params) = (tokens.head.toLowerCase.toLowerCase, CommandParser.parse(tokens.tail))

      Try {
        command match {
          case "a" | "asm" => dumpAssembly(params)
          case "d" | "dump" => dumpData(params)
          case "g" | "go" => executeCode(params)
          case "l" | "load" => loadFile(params)
          case "n" | "next" => executeNext(params)
          case "q" | "quit" => stop()
          case "r" | "regs" => out.println(cpu)
          case x =>
            throw new IllegalArgumentException(s"Command '$x' not recognized")
        }
      }
      match {
        case Success(_) =>
        case Failure(e) =>
          System.err.println(s"Run-time error: ${e.getMessage}")
      }
    }
  }

  /**
   * Disassemble the specified number of instructions.
   *
   * @param params the given parameters
   */
  private def dumpAssembly(params: UnixLikeArgs) {
    // extract the parameters
    val count = params("-n") map (_.toInt) getOrElse 10
    val (segment0, offset0) = params.args match {
      case aSegment :: aOffset :: Nil => (aSegment.toInt, aOffset.toInt)
      case aOffset :: Nil => (proxy.getSegment, aOffset.toInt)
      case Nil => (proxy.getSegment, proxy.getOffset)
      case _ =>
        throw new IllegalArgumentException("Syntax: a[sm] [[segment]:offset] [-n count")
    }

    // set the initial segment and offset
    proxy.setPointer(segment0, offset0)

    // execute the command
    for (_ <- 1 to count) {
      val segment = proxy.getSegment
      val offset = proxy.getOffset
      val instruction = decoder.decodeNext
      val byteCodeString = getByteCodeString(segment, offset)
      out.println("[%04X:%04X] %-12s %s".format(segment, offset, byteCodeString, instruction))
    }
  }

  /**
   * Dumps the specific number of bytes to the console
   * <p/>Syntax1: d[ump] [offset] [-n count] [-c columns]
   * <p/>Syntax2: d[ump] [segment] [offset] [-n count] [-c columns]
   * @param params the given parameters
   */
  private def dumpData(params: UnixLikeArgs) {
    val DUMP_LENGTH = 16

    // extract the parameters
    val columns = params("-c") map (_.toInt) getOrElse DUMP_LENGTH
    val count = params("-n") map (_.toInt) getOrElse 128
    val (segment, offset) = params.args match {
      case aSegment :: aOffset :: Nil => (aSegment.toInt, aOffset.toInt)
      case aOffset :: Nil => (proxy.getSegment, aOffset.toInt)
      case Nil => (proxy.getSegment, proxy.getOffset)
      case _ =>
        throw new IllegalArgumentException("Syntax: d[ump] [[segment]:offset] [-n count] [-c columns]")
    }

    // copy the block of data
    val block = new Array[Byte](count)
    proxy.getMemory.getBytes(segment, offset, block, count)

    block.sliding(columns, columns) foreach { chunk =>
      val hex = chunk map (b => f"$b%02x") mkString "."
      val chars = chunk.map(b => if (b >= 32 && b <= 127) b.toChar else '.').mkString
      out.println(f"$hex%48s $chars")
    }
  }

  /**
   * Executes a sequence of instructions
   * <p/>Syntax1: g[o] [offset] [-n count]
   * <p/>Syntax2: g[o] [segment] [offset] [-n count]
   * @param params the given [[UnixLikeArgs]]
   */
  private def executeCode(params: UnixLikeArgs) {
    // extract the parameters
    val limit = if (params.contains("-a")) Int.MaxValue else params("-n").map(_.toInt).getOrElse(100)
    val (segment, offset) = params.args match {
      case aSegment :: aOffset :: Nil => (aSegment.toInt, aOffset.toInt)
      case aOffset :: Nil => (proxy.getSegment, aOffset.toInt)
      case Nil => getLastExecutePointer
      case _ =>
        throw new IllegalArgumentException("Syntax: g[o] [[segment]:offset] [-n count]")
    }

    out.println(s"Executing $limit instructions")

    // execute the code
    var count = 0
    proxy.setPointer(segment, offset)

    while (count < limit && cpu.isActive) {
      val opCode: OpCode = decoder.decodeNext
      out.println("[%04X:%04X] %12X[%d] %-28s | %s".format(cpu.CS.get, cpu.IP.get, opCode.getInstructionCode, opCode.getLength, opCode, inspect(opCode)))
      cpu.execute(system, opCode)
      count += 1
    }

    // update the last execution offset
    lastExecutePtr = (cpu.CS.get, cpu.IP.get)
  }

  /**
   * Executes the next instruction
   * <p/>Syntax1: n[ext] [offset]
   * <p/>Syntax2: n[ext] [segment] [offset]
   */
  private def executeNext(params: UnixLikeArgs) {
    // extract the parameters
    val (segment, offset) = params.args match {
      case aSegment :: aOffset :: Nil => (aSegment.toInt, aOffset.toInt)
      case aOffset :: Nil => (proxy.getSegment, aOffset.toInt)
      case Nil => getLastExecutePointer
      case _ =>
        throw new IllegalArgumentException("Syntax: n[ext] [[segment] offset]")
    }

    // execute the code
    proxy.setPointer(segment, offset)

    val opCode = decoder.decodeNext()
    out.println("[%04X:%04X] %12X[%d] %-28s | %s".format(cpu.CS.get, cpu.IP.get, opCode.getInstructionCode, opCode.getLength, opCode, inspect(opCode)))
    cpu.execute(system, opCode)

    // update the last execution offset
    lastExecutePtr = (cpu.CS.get, cpu.IP.get)
  }

  private def getLastExecutePointer = {
    val (segment, offset) = (cpu.CS.get, cpu.IP.get)
    if (segment == proxy.getSegment) (segment, offset) else (proxy.getSegment, proxy.getOffset)
  }

  private def inspect(opCode: OpCode): String = {
    val myFlags = Option(opCode.getClass.getAnnotation(classOf[FlagsAffected]))
      .map(_.value().toSeq)
      .map(_ flatMap flags.get)
      .map(_ mkString " ")
      .map(s => f"FL = $s%-26s")

    (myFlags ?? expand(cpu.FLAGS) :: expand(opCode).reverse).formatCode
  }

  private def expand(opCode: OpCode): List[Option[String]] = {
    opCode match {
      case op: LoadStoreOpCode =>
        List(cpu.DS, cpu.SI, cpu.ES, cpu.DI).reverse map expand
      case op: StringFunctionOpCode =>
        List(cpu.DS, cpu.SI, cpu.ES, cpu.DI).reverse map expand
      case op: AbstractDualOperandOpCode =>
        expand(op.src) :: expand(op.dest) :: expandSP(op) :: Nil
      case op: DataSegmentOverride =>
        expand(op.register) :: expand(op.instruction)
      case op: AbstractFlagUpdateOpCode =>
        expand(cpu.SP) :: Nil
      case op: AbstractSingleOperandOpCode =>
        expand(op.operand) :: expandSP(op) :: Nil
      case op: StackModifyingOpCode =>
        expand(cpu.SP) :: Nil
      case op: FlowControlCallBackOpCode =>
        expand(op.opCode)
      case _ =>
        Nil
    }
  }

  private def expand(operand: Operand): Option[String] = {
    operand match {
      case m: MemoryAddressNEAR16 =>
        Some(f"$m = ${m.get}%04X")
      case m: MemoryPointer =>
        Some(f"[${m.getMemoryReference.getOffset}%04X] = ${m.get}%04X")
      case r: X86Register8bit =>
        Some(f"${r.name} =   ${r.get}%02X")
      case r: X86Register16bit =>
        Some(f"$r = ${r.get}%04X")
      case r: X86CompositeRegister16Bit =>
        Some(f"$r = ${r.get}%04X")
      case f: X86Flags =>
        Some(s"FL = ${cpu.FLAGS}")
      case o =>
        //System.err.println(s"opr = $o, class = ${o.getClass.getName}")
        None
    }
  }

  private def expandSP(opCode: OpCode): Option[String] = {
    opCode match {
      case op: StackModifyingOpCode => expand(cpu.SP)
      case _ => None
    }
  }

  /**
   * Loads the given executable into memory
   * <p/>Syntax: load FROGGER.COM
   * @param params the given parameter
   */
  private def loadFile(params: UnixLikeArgs) {
    // get the file name
    val filename = params.args match {
      case aFilename :: Nil => aFilename
      case _ =>
        throw new IllegalArgumentException("Syntax: l[oad] <filename>")
    }

    // load the executable into memory
    injectExecutable(filename)
  }

  /**
   * Injects the given executable into memory
   *
   * @param filename the given file name (e.g. 'load FROGGER.COM')
   */
  private def injectExecutable(filename: String) {
    // load the executable into memory
    try {
      val code = ResourceHelper.getBinaryContents(new File(filename))
      val codeSegment = proxy.getSegment
      val codeOffset = 0x0100

      memory.setBytes(proxy.getSegment, codeOffset, code, code.length)

      cpu.CS.set(codeSegment)
      cpu.DS.set(codeSegment)
      cpu.ES.set(codeSegment)
      cpu.SS.set(codeSegment)
      cpu.IP.set(codeOffset)
      cpu.SP.set(0xFFFE)

      out.println(f"Loaded $filename: ${code.length}%d bytes")
    }
    catch {
      case e: IOException =>
        out.println(s"Unable to load '$filename'")
    }
  }

  /**
   * Stops the debugger
   */
  private def stop() = sys.exit(0)

  /**
   * Creates a byte string
   *
   * @param segment the given segment
   * @param offset  the given start offset
   * @return a byte string
   */
  private def getByteCodeString(segment: Int, offset: Int): String = {
    val length = proxy.getOffset - offset
    val block = new Array[Byte](length)
    memory.getBytes(segment, offset, block, block.length)

    block.map(b => f"$b%02X").mkString
  }

}

/**
 * IBM PC/MS-DOS Debugger Application
 * @author lawrence.daniels@gmail.com
 */
object Debugger {

  /**
   * For standalone operation
   * @param args the given commandline arguments
   */
  def main(args: Array[String]) {
    System.out.println(s"JavaPC/Debugger v${JavaPCConstants.VERSION}")
    System.out.println("")

    val app = new Debugger()
    args.toList match {
      case fileName :: Nil => app.injectExecutable(fileName)
      case _ =>
    }
    app.run()
  }

  /**
   * Instruction Formatter (Type A)
   * @param tokens the given instruction tokens
   */
  implicit class InstructionFormatterA(val tokens: Seq[Option[String]]) extends AnyVal {

    def formatCode: String = tokens.flatten.distinct filter (_.nonEmpty) mkString " - "

  }

  /**
   * Instruction Formatter (Type B)
   * @param tokens the given instruction tokens
   */
  implicit class InstructionFormatterB(val tokens: Seq[String]) extends AnyVal {

    def formatCode: String = tokens.distinct filter (_.nonEmpty) mkString " - "

  }

  /**
   * 8086 Flags Enrichment
   * @param flags the given [[X86Flags]]
   */
  implicit class FlagEnrichment(val flags: X86Flags) extends AnyVal {

    def AF = if (flags.isAF) "AC" else "NA"

    def CF = if (flags.isCF) "CY" else "NC"

    def DF = if (flags.isDF) "DN" else "UP"

    def IF = if (flags.isIF) "EI" else "DI"

    def OF = if (flags.isPF) "OV" else "NV"

    def PF = if (flags.isPF) "PE" else "PO"

    def SF = if (flags.isSF) "NG" else "PL"

    def TF = if (flags.isTF) "ET" else "DT"

    def ZF = if (flags.isZF) "ZR" else "NZ"

  }

}
