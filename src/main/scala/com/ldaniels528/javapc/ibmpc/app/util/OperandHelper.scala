package com.ldaniels528.javapc.ibmpc.app.util

import org.ldaniels528.javapc.ibmpc.devices.cpu.operands.{ByteValue, WordValue}

/**
 * Operand Helper Utility
 * @author lawrence.daniels@gmail.com
 */
object OperandHelper {

  /**
   * Operand Extensions
   * @author lawrence.daniels@gmail.com
   */
  implicit class OperandExtensions(val value: Int) extends AnyVal {

    def db: ByteValue = new ByteValue(value)

    def dw: WordValue = new WordValue(value)

  }

}
