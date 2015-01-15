package com.ldaniels528.javapc.ibmpc.app

import java.io._

import com.ldaniels528.javapc.ibmpc.app.CommandParser.UnixLikeArgs
import com.ldaniels528.javapc.ibmpc.app.Debugger._
import org.ldaniels528.javapc.JavaPCConstants
import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.Operand
import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.memory.MemoryPointer
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.decoder.{DecodeProcessorImpl, FlowControlCallBackOpCode}
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.addressing.DataSegmentOverride
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.flags.AbstractFlagUpdateOpCode
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes.{AbstractDualOperandOpCode, AbstractSingleOperandOpCode, StackModifyingOpCode}
import org.ldaniels528.javapc.ibmpc.devices.cpu.{OpCode, X86CompositeRegister16Bit, X86Register16bit, X86Register8bit}
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
  private var firstInstruction = true
  private var filename: String = null
  private var alive: Boolean = false

  // create a PC instance
  val system = IbmPcSystemFactory.getIBMPCjr(new IbmPcDisplayFrame(s"JavaPC/Debugger v${JavaPCConstants.VERSION}"))
  val cpu = system.getCPU
  val memory = system.getRandomAccessMemory
  val display = system.getDisplay

  // create debug helper objects
  val proxy = system.getMemoryProxy
  val decoder = new DecodeProcessorImpl(cpu, proxy)

  // point the a stable segment and offset
  proxy.setSegment(0x13F0)
  proxy.setOffset(0x100)

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
   * [f]ile - sets the current file name (for reading or saving)
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
          case "d" | "dump" => dump(params)
          case "g" | "go" => executeCode(params)
          case "f" | "file" => setFileName(line)
          case "n" | "next" => executeNext()
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
   * Dumps the specific number of bytes to the console
   * <p/>Syntax1: d[ump] [offset] [-n count] [-c columns]
   * <p/>Syntax2: d[ump] [segment] [offset] [-n count] [-c columns]
   * @param params the given command parameters
   */
  private def dump(params: UnixLikeArgs) {
    val DUMP_LENGTH = 16

    // extract the parameters
    val columns = params("-c") map (_.toInt) getOrElse DUMP_LENGTH
    val count = params("-n") map (_.toInt) getOrElse 128
    val (segment, offset) = params.args match {
      case aSegment :: aOffset :: Nil => (aSegment.toInt, aOffset.toInt)
      case aOffset :: Nil => (proxy.getSegment, aOffset.toInt)
      case Nil => (proxy.getSegment, proxy.getOffset)
      case _ =>
        throw new IllegalArgumentException("Syntax: dump [[segment]:offset] [-n count] [-c columns]")
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
    val limit = params("-n") map (_.toInt) getOrElse 100
    val (segment, offset) = params.args match {
      case aSegment :: aOffset :: Nil => (aSegment.toInt, aOffset.toInt)
      case aOffset :: Nil => (proxy.getSegment, aOffset.toInt)
      case Nil => (proxy.getSegment, proxy.getOffset)
      case _ =>
        throw new IllegalArgumentException("Syntax: go [[segment]:offset] [-n count")
    }

    // execute the code
    var count = 0
    proxy.setSegment(segment)
    proxy.setOffset(offset)

    while (count < limit && cpu.isActive) {
      val opCode: OpCode = decoder.decodeNext
      out.println("[%04X:%04X] %12X[%d] %-28s | %s".format(cpu.CS.get, cpu.IP.get, opCode.getInstructionCode, opCode.getLength, opCode, inspect(opCode)))
      cpu.execute(system, opCode)
      count += 1
    }
  }

  /**
   * Executes the next instruction
   * <p/>Syntax1: n[ext] [offset] [-n count]
   * <p/>Syntax2: n[ext] [segment] [offset] [-n count]
   */
  private def executeNext() {
    // make sure we start with the first instruction
    if (firstInstruction) {
      firstInstruction = false
      proxy.setOffset(0x100)
    }

    val opCode = decoder.decodeNext()
    out.println("[%04X:%04X] %12X[%d] %-28s | %s".format(cpu.CS.get, cpu.IP.get, opCode.getInstructionCode, opCode.getLength, opCode, inspect(opCode)))
    cpu.execute(system, opCode)
  }

  private def inspect(opCode: OpCode): String = {
    opCode match {
      case op: AbstractDualOperandOpCode =>
        Seq(expand(op.src), expand(op.dest), expandSP(op)).formatCode
      case op: DataSegmentOverride =>
        Seq(expand(op.register).getOrElse(""), inspect(op.instruction)).formatCode
      case op: AbstractFlagUpdateOpCode =>
        s"FL = ${cpu.FLAGS}"
      case op: AbstractSingleOperandOpCode =>
        Seq(expand(op.operand), expandSP(op)).formatCode
      case op: StackModifyingOpCode =>
        expand(cpu.SP).getOrElse("")
      case op: FlowControlCallBackOpCode =>
        inspect(op.opCode)
      case _ => ""
    }
  }

  private def expand(operand: Operand): Option[String] = {
    operand match {
      case mp: MemoryPointer =>
        Some(f"[${mp.getMemoryReference.getOffset}%04X] = ${mp.get}%04X")
      case r: X86Register8bit =>
        Some(f"${r.name} = ${r.get}%02X")
      case r: X86Register16bit =>
        Some(f"$r = ${r.get}%04X")
      case r: X86CompositeRegister16Bit =>
        Some(f"$r = ${r.get}%04X")
      case o =>
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
   *
   * @param filename the file name of the executable to load
   */
  private def loadExecutable(filename: String) {
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
   * Sets the name of a file to be read or written
   *
   * @param command the given command string (e.g. 'nFROGGER.COM')
   */
  private def setFileName(command: String) {
    filename = command.substring(1)
    out.println(s"Filename: $filename")
  }

  /**
   * Stops the debugger
   */
  private def stop() = sys.exit(0)

  /**
   * Disassemble the specified number of instructions.
   *
   * @param params the given UNIX-style parameters
   */
  private def dumpAssembly(params: UnixLikeArgs) {
    // extract the parameters
    val count = params("-n") map (_.toInt) getOrElse 10
    val (segment0, offset0) = params.args match {
      case aSegment :: aOffset :: Nil => (aSegment.toInt, aOffset.toInt)
      case aOffset :: Nil => (proxy.getSegment, aOffset.toInt)
      case Nil => (proxy.getSegment, proxy.getOffset)
      case _ =>
        throw new IllegalArgumentException("Syntax: u [[segment]:offset] [-n count")
    }

    // set the initial segment and offset
    proxy.setSegment(segment0)
    proxy.setOffset(offset0)

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
    System.out.printf("JavaPC/Debugger v%s\n", JavaPCConstants.VERSION)
    System.out.println("")

    val app = new Debugger()
    args.toList match {
      case fileName :: Nil => app.loadExecutable(fileName)
      case _ =>
    }
    app.run()
  }

  /**
   * Instruction Formatter (Type A)
   * @param tokens the given instruction tokens
   */
  implicit class InstructionFormatterA(tokens: Seq[Option[String]]) {

    def formatCode: String = tokens.flatten mkString ", "

  }

  /**
   * Instruction Formatter (Type B)
   * @param tokens the given instruction tokens
   */
  implicit class InstructionFormatterB(tokens: Seq[String]) {

    def formatCode: String = tokens filter (_.nonEmpty) mkString ", "

  }

}
