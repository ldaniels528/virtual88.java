package org.ldaniels528.javapc.jbasic.gwbasic.program;

import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;
import org.ldaniels528.javapc.jbasic.common.program.JBasicProgramStatement;
import org.ldaniels528.javapc.jbasic.common.program.JBasicSourceCode;

import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Represents a GWBASICA/BASICA memory resident program.
 *
 * @author lawrence.daniels@gmail.com
 */
public class GwBasicProgram implements JBasicSourceCode {
    // line number comparator
    private static final GwBasicStatementComparator comparator = new GwBasicStatementComparator();
    // fields
    private final SortedSet<JBasicProgramStatement> statements;
    private final IbmPcSystem environment;

    ///////////////////////////////////////////////////////
    //      Constructor(s)
    ///////////////////////////////////////////////////////

    /**
     * Creates a BASICA/GWBASIC compatible {@link JBasicSourceCode program}
     */
    @SuppressWarnings("unchecked")
    public GwBasicProgram(final IbmPcSystem environment) {
        this.environment = environment;
        this.statements = new TreeSet<>(comparator);
    }


    ///////////////////////////////////////////////////////
    //      Integrated Development Environment (IDE) Method(s)
    ///////////////////////////////////////////////////////

    /**
     * {@inheritDoc}
     */
    @Override
    public void add(final JBasicProgramStatement stmt) {
        GwBasicStatement statement = (GwBasicStatement) stmt;
        // if the statement already exists, remove it
        if (statements.contains(statement)) statements.remove(statement);

        // add the new statement
        statements.add(statement);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        statements.clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SortedSet<JBasicProgramStatement> getStatements() {
        return statements;
    }

    /**
     * Returns the statements from the program that is currently resides in memory
     *
     * @param range the range of {@link JBasicProgramStatement statements} to return
     * @return the {@link Collection collection} of statements that make up this program
     */
    public Collection<JBasicProgramStatement> getStatements(final int... range) {
        GwBasicStatement stmtA = new GwBasicStatement(range[0], null);
        GwBasicStatement stmtB = new GwBasicStatement(range[1], null);
        return statements.subSet(stmtA, stmtB);
    }

    /**
     * Removes the given range of statements from this proram
     *
     * @param range the given range of statements
     */
    public boolean removeStatements(final int... range) {
        final GwBasicStatement stmtA = new GwBasicStatement(range[0], null);
        final  GwBasicStatement stmtB = new GwBasicStatement(range[1], null);
        final  Collection<JBasicProgramStatement> stmts = statements.subSet(stmtA, stmtB);
        return statements.remove(stmts);
    }

    /*
     * (non-Javadoc)
     * @see org.ldaniels528.javapc.jbasic.common.program.JBasicSourceCode#getEnvironment()
     */
    public IbmPcSystem getEnvironment() { // TODO remove this method
        return environment;
    }


}