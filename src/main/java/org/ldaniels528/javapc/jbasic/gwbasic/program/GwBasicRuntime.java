package org.ldaniels528.javapc.jbasic.gwbasic.program;

import org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException;
import org.ldaniels528.javapc.jbasic.common.program.JBasicCompiledCode;
import org.ldaniels528.javapc.jbasic.common.program.JBasicProgramStatement;
import org.ldaniels528.javapc.jbasic.common.program.JBasicRuntime;
import org.ldaniels528.javapc.jbasic.common.program.JBasicSourceCode;
import org.ldaniels528.javapc.jbasic.gwbasic.GwBasicEnvironment;

/**
 * BASICA/GWBASIC Runtime
 *
 * @author lawrence.daniels@gmail.com
 */
public class GwBasicRuntime implements JBasicRuntime {
    private static final GwBasicRuntime instance = new GwBasicRuntime();

    ///////////////////////////////////////////////////////
    //      Constructor(s)
    ///////////////////////////////////////////////////////

    /**
     * Creates an instance this compiler
     */
    private GwBasicRuntime() {
        super();
    }

    ///////////////////////////////////////////////////////
    //      Static Method(s)
    ///////////////////////////////////////////////////////

    /**
     * Returns the singleton instance of this class
     *
     * @return the singleton instance of this class
     */
    public static GwBasicRuntime getInstance() {
        return instance;
    }

    ///////////////////////////////////////////////////////
    //      Service Method(s)
    ///////////////////////////////////////////////////////

    /**
     * {@inheritDoc}
     */
    @Override
    public void evaluate(final JBasicSourceCode program,
                         final JBasicProgramStatement statement)
            throws JBasicException {
        // get the environment object
        final GwBasicEnvironment environment = (GwBasicEnvironment) program.getEnvironment();

        // get the compiler instance
        final GwBasicCompiler compiler = GwBasicCompiler.getInstance();

        // compile the statement into code
        final JBasicCompiledCode compiledCode = compiler.compile(environment, statement);

        // execute the compiled code
        compiledCode.execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run(final JBasicSourceCode program)
            throws JBasicException {
        // get the compiler instance
        final GwBasicCompiler compiler = GwBasicCompiler.getInstance();

        // compile the program into opCodes
        final JBasicCompiledCode compiledCode = compiler.compile(program);

        // execute the compiled code
        compiledCode.execute();
    }

}
