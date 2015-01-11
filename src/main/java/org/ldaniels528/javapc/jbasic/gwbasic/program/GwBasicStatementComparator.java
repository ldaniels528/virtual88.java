package org.ldaniels528.javapc.jbasic.gwbasic.program;

import java.util.Comparator;

/**
 * BASICA/GWBASIC Language Statement Sorter
 *
 * @author lawrence.daniels@gmail.com
 */
class GwBasicStatementComparator implements Comparator {

    /* (non-javadoc)
     * @see Comparator#compare(Object,Object)
     */
    public int compare(Object o1, Object o2) {
        // cast both to statement objects
        GwBasicStatement stmt1 = (GwBasicStatement) o1;
        GwBasicStatement stmt2 = (GwBasicStatement) o2;

        // compare the line numbers
        if (stmt1.getLineNumber() > stmt2.getLineNumber()) return 1;
        else if (stmt1.getLineNumber() < stmt2.getLineNumber()) return -1;
        else return 0;
    }

}
