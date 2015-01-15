package org.ldaniels528.javapc.ibmpc.devices.cpu.x86.opcodes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates which registers are used by the host opCode
 *
 * @author lawrence.daniels@gmail.com
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RegistersUsed {
    String[] value();
}
