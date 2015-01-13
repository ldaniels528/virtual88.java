package com.ldaniels528.javapc.ibmpc.app

import java.io._

import com.ldaniels528.javapc.ibmpc.app.CommandParser.UnixLikeArgs
import org.ldaniels528.javapc.JavaPCConstants
import org.ldaniels528.javapc.ibmpc.devices.cpu.OpCode
import org.ldaniels528.javapc.ibmpc.devices.cpu.x86.decoder.DecodeProcessorImpl
import org.ldaniels528.javapc.ibmpc.devices.display.IbmPcDisplayFrame
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystemPCjr
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
  val system = new IbmPcSystemPCjr(new IbmPcDisplayFrame(String.format("JavaPC/Debugger v%s", JavaPCConstants.VERSION)))
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
   * [d]ump - dump the contents of memory at the current IP position
   * [f]lags - displays the current state of all flags
   * [g]o - starts executing code at the current IP position
   * [f]ileName - sets the current file name (for reading or saving)
   * [n]ext - executes the next instruction
   * [q]uit - exits the Debugger
   * [r]egisters - displays the current state of all registers
   * [u]nassemble - decodes assembly code at the current IP position
   * </pre>
   *
   * @param line the given line of input
   */
  private def interpret(line: String) {
    val tokens = CommandParser.parseTokens(line)
    val (command, params) = (tokens.head.toLowerCase.toLowerCase, CommandParser.parse(tokens.tail))

    Try {
      command match {
        case "d" => dump(params)
        case "g" => executeCode(cpu.IP.get, 100)
        case "f" => setFileName(line)
        case "n" => executeNext()
        case "q" => stop()
        case "r" => out.println(cpu)
        case "u" => unassemble(params)
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

  /**
   * Dumps the specific number of bytes on screen
   * <p/>Syntax1: d [offset] [count]
   * <p/>Syntax2: d [segment] [offset] [-n count]
   * @param params the given command parameters
   */
  private def dump(params: UnixLikeArgs) {
    val DUMP_LENGTH = 16

    // extract the parameters
    val count = params("-n") map (_.toInt) getOrElse 128
    val (segment, offset) = params.args match {
      case aSegment :: aOffset :: Nil => (aSegment.toInt, aOffset.toInt)
      case aOffset :: Nil => (proxy.getSegment, aOffset.toInt)
      case Nil => (proxy.getSegment, proxy.getOffset)
      case _ =>
        throw new IllegalArgumentException("Syntax: d [[segment]:offset] [-n count")
    }

    // copy the block of data
    val block = new Array[Byte](count)
    proxy.getMemory.getBytes(segment, offset, block, count)

    block.sliding(DUMP_LENGTH, DUMP_LENGTH) foreach { chunk =>
      val hex = chunk map (b => f"$b%02x") mkString "."
      val chars = chunk.map(b => if (b >= 32 && b <= 127) b.toChar else '.').mkString
      out.println(f"$hex%48s $chars")
    }
  }

  private def executeCode(offset: Int, limit: Int) {
    var count = 0
    proxy.setOffset(offset)

    while (count < limit && cpu.isActive) {
      val opCode: OpCode = decoder.decodeNext
      out.println("[%04X:%04X] %10X[%d] %s".format(cpu.CS.get, cpu.IP.get, opCode.getInstructionCode, opCode.getLength, opCode))
      cpu.execute(system, opCode)
      count += 1
    }
  }

  private def executeNext() {
    // make sure we start with the first instruction
    if (firstInstruction) {
      firstInstruction = false
      proxy.setOffset(0x100)
    }

    val opCode = decoder.decodeNext()
    out.println("[%04X:%04X] %10X[%d] %s".format(cpu.CS.get, cpu.IP.get, opCode.getInstructionCode, opCode.getLength, opCode))
    cpu.execute(system, opCode)
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
  private def stop() {
    this.alive = false
  }

  /**
   * Disassemble the specified number of instructions.
   *
   * @param params the given UNIX-style parameters
   */
  private def unassemble(params: UnixLikeArgs) {
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

}
