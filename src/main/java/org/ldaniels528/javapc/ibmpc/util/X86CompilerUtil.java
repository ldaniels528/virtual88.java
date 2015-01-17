package org.ldaniels528.javapc.ibmpc.util;

import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenIterator;
import org.ldaniels528.javapc.jbasic.common.tokenizer.Tokenizer;
import org.ldaniels528.javapc.jbasic.common.tokenizer.parsers.*;
import org.ldaniels528.javapc.ibmpc.exceptions.X86AssemblyException;
import org.ldaniels528.javapc.ibmpc.compiler.element.addressing.X86MemoryAddressParser;
import org.ldaniels528.javapc.jbasic.common.exceptions.IllegalNumberFormat;

import static org.ldaniels528.javapc.jbasic.gwbasic.values.GwBasicValues.*;

/**
 * 8086 Compiler Utilities
 *
 * @author lawrence.daniels@gmail.com
 */
public class X86CompilerUtil {

    ///////////////////////////////////////////////////////////////////
    //			Memory-related Method(s)
    ///////////////////////////////////////////////////////////////////

    /**
     * Returns the given memory address' offset
     *
     * @param identifier the given direct address (e.g. '[2345]')
     * @return the memory address' offset in memory
     * @throws X86AssemblyException
     */
    public static int getMemoryOffset(final String identifier)
            throws X86AssemblyException {
        // is the identifier a direct memory address?
        if (X86MemoryAddressParser.isDirectAddress(identifier)) {
            return parseNumeric(identifier.substring(1, identifier.length() - 1));
        } else throw new X86AssemblyException("Illegal identifier '" + identifier + "'");
    }

    ///////////////////////////////////////////////////////////////////
    //			Type Identifier Method(s)
    ///////////////////////////////////////////////////////////////////

    /**
     * Indicates whether the given string represents an integer value.
     *
     * @return true, if the given string represents an integer value.
     */
    public static boolean isNumber(final String numString) {
        return isHexadecimalConstant(numString) ||
                isIntegerConstant(numString) ||
                isOctalConstant(numString);
    }

    /**
     * Indicates whether the given string represents a variable (e.g. 'X$').
     *
     * @return true, if the given string represents a variable.
     */
    public static boolean isVariable(final String identifier) {
        return identifier.matches("^[A-Z|a-z]");
    }

    ///////////////////////////////////////////////////////////////////
    //			Conversion Method(s)
    ///////////////////////////////////////////////////////////////////

    /**
     * Parses the given string into an integer value
     *
     * @param numString the given string; which can be decimal, heaxdecimal, octal, or binary
     * @return the numeric equivalent of the given string
     * @throws X86AssemblyException
     */
    public static int parseNumeric(final String numString)
            throws X86AssemblyException {
        // hexadecimal string?
        if (isHexadecimalConstant(numString))
            return parseHexadecimalString(numString);
            // integer string?
        else if (isIntegerConstant(numString))
            return parseIntegerString(numString);
            // octal string?
        else if (isOctalConstant(numString))
            return parseHexadecimalString(numString);
            // unknown ...
        else throw new IllegalNumberFormat(numString);
    }

    ///////////////////////////////////////////////////////////////////
    //			Tokenizer-related Method(s)
    ///////////////////////////////////////////////////////////////////

    /**
     * @return a GWBASIC configured tokenizer
     */
    public static Tokenizer getConfiguredTokenizer() {
        final Tokenizer tokenizer = new Tokenizer();
        tokenizer.add(new EndOfLineTokenParser());
        tokenizer.add(new NumericTokenParser());
        tokenizer.add(new OperatorTokenParser());
        tokenizer.add(new DoubleQuotedTextTokenParser());
        tokenizer.add(new TextTokenParser());
        return tokenizer;
    }

    /**
     * Mandates the given expected value
     *
     * @param it       the given {@link TokenIterator token iterator}
     * @param expected the expected value
     * @throws X86AssemblyException
     */
    public static void mandateToken(final TokenIterator it, final String expected)
            throws X86AssemblyException {
        // there must be a next token
        if (!it.hasNext())
            throw new X86AssemblyException("");

        // get the next token
        final String found = it.next();
        if (!found.equals(expected))
            throw new X86AssemblyException("Expected '" + expected + "' Found '" + found + "'");
    }

    /**
     * Mandates the given expected value
     *
     * @param it       the given {@link TokenIterator token iterator}
     * @param expected the expected value
     * @throws X86AssemblyException
     */
    public static void mandateElement(final TokenIterator it, final String expected)
            throws X86AssemblyException {
        final String found = nextElement(it);
        if (!found.equals(expected))
            throw new X86AssemblyException("Expected '" + expected + "' Found '" + found + "'");
    }

    /**
     * Returns the next element from the token iterator
     *
     * @param it the given {@link TokenIterator token iterator}
     * @return the next element from the token iterator
     * @throws X86AssemblyException
     */
    public static String nextElement(final TokenIterator it)
            throws X86AssemblyException {
        // there must be a next token
        if (!it.hasNext())
            throw new X86AssemblyException("Identifier expected");

        // get the next token
        final String token = it.next();

        // create a buffer for the token
        final StringBuilder buffer = new StringBuilder();
        buffer.append(token);

        // check for '[' ... ']'
        if ("[".equals(token)) {
            String s = null;
            while (it.hasNext() && !(s = it.next()).equals("]")) {
                buffer.append(s);
                s = null;
            }
            if (s != null) buffer.append(s);
        }

        // check for 'BYTE PTR' or 'WORD PTR'
        else if ("BYTE".equals(token) || "WORD".equals(token) || "DWORD".equals(token)) {
            if ("PTR".equals(it.peekAtNext())) {
                buffer.append(' ').append(it.next());
            }
        }

        // return the element
        return buffer.toString();
    }

    /**
     * Allow no more tokens
     *
     * @param it the given {@link TokenIterator token iterator}
     * @throws X86AssemblyException
     */
    public static void noMoreTokens(final TokenIterator it)
            throws X86AssemblyException {
        if (it.hasNext())
            throw new X86AssemblyException("Invalid statement");
    }

}
