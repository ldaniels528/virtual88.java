package com.ldaniels528.javapc.ibmpc.app.util

import scala.language.implicitConversions

/**
 * Option Helper Utility Class
 * @author Lawrence Daniels <lawrence.daniels@gmail.com>
 */
object OptionHelper {

  object Risky {

    implicit def value2Option[T](value: T): Option[T] = Option(value)

  }

  /**
   * Facilitates option chaining
   */
  implicit class OptionalExtensions[T](val opA: Option[T]) extends AnyVal {

    def ??(opB: => Option[T]) = if (opA.isDefined) opA else opB

    def orDie(message: String): T = opA.getOrElse(throw new IllegalStateException(message))

  }

}
