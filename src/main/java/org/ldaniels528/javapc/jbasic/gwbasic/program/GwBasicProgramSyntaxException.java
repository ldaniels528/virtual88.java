package org.ldaniels528.javapc.jbasic.gwbasic.program;

import org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException;

import static java.lang.String.format;

/**
 * An exception generated at the time of executing a
 * line numbered program
 *
 * @author lawrence.daniels@gmail.com
 */
@SuppressWarnings("serial")
public class GwBasicProgramSyntaxException extends JBasicException {
    private Integer lineNumber;

    /**
     * Creates an instance of this exception
     *
     * @param cause      the cause of this exception
     * @param lineNumber the line number of the instruction that failed
     */
    public GwBasicProgramSyntaxException(final Exception cause, final Integer lineNumber) {
        super(cause);
        this.lineNumber = lineNumber;
    }

    /**
     * @return the line number that generated this exception
     */
    public Integer getLineNumber() {
        return lineNumber;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return format("%s on line %d", getCause().getMessage(), lineNumber);
    }

}