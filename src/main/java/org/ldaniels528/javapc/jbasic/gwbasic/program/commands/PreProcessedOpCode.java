package org.ldaniels528.javapc.jbasic.gwbasic.program.commands;

import org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException;
import org.ldaniels528.javapc.jbasic.common.program.JBasicCompiledCode;
import org.ldaniels528.javapc.jbasic.common.program.OpCode;

/**
 * Represents an {@link OpCode opCode} that should be pre-processed
 *
 * @author lawrence.daniels@gmail.com
 */
public interface PreProcessedOpCode extends OpCode {

    /**
     * Pre-processes this {@link OpCode opCode}
     *
     * @param compiledCode the current executing {@link JBasicCompiledCode compiled code}
     */
    void preProcess(JBasicCompiledCode compiledCode) throws JBasicException;

}
