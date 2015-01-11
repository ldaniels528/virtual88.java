/**
 *
 */
package org.ldaniels528.javapc.ibmpc.compiler;

import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenIterator;
import org.ldaniels528.javapc.ibmpc.exceptions.X86AssemblyException;
import org.ldaniels528.javapc.ibmpc.compiler.element.X86DataElement;
import org.ldaniels528.javapc.ibmpc.compiler.exception.X86MalformedInstructionException;

import java.util.ArrayList;
import java.util.List;

import static org.ldaniels528.javapc.ibmpc.compiler.element.addressing.X86ReferencedAddressParser.isReference;
import static org.ldaniels528.javapc.ibmpc.compiler.element.addressing.X86ReferencedAddressParser.parseReference;
import static org.ldaniels528.javapc.ibmpc.compiler.element.registers.X86RegisterReferences.isRegister;
import static org.ldaniels528.javapc.ibmpc.compiler.element.registers.X86RegisterReferences.lookupRegister;
import static org.ldaniels528.javapc.ibmpc.compiler.element.values.X86Value.*;
import static org.ldaniels528.javapc.ibmpc.util.X86CompilerUtil.mandateElement;
import static org.ldaniels528.javapc.ibmpc.util.X86CompilerUtil.nextElement;
import static java.util.Arrays.asList;

/**
 * Represents a 80x86 assembly language instruction parser
 *
 * @author lawrence.daniels@gmail.com
 */
public class X86InstructionParser {
    private static final List<String> QUANTIFIERS = asList("byte ptr", "word ptr", "word", "dword", "qword");

    /**
     * Parses the given assembly code string into an x86 instruction object
     *
     * @param it the {@link TokenIterator tokenizer} containing the assembly code string
     * @return an {@link X86Instruction x86 instruction} object
     * @throws X86AssemblyException
     */
    public static X86Instruction parse(final TokenIterator it) throws X86AssemblyException {
        // get the instruction's name
        final String name = nextElement(it);

        // if there are no other elements, just return
        if (!it.hasNext()) {
            return X86Instruction.create(name);
        }

        // create a container for the parameters
        final List<X86DataElement> params = new ArrayList<X86DataElement>(3);

        // gather the parameters
        while (it.hasNext()) {
            // get the next data element from the iterator
            params.add(nextDataElement(it));

            // is there another element
            if (it.hasNext()) {
                mandateElement(it, ",");
            }
        }

        // return a new instruction with the list of paramters
        return X86Instruction.create(name, params);
    }

    /**
     * Converts the given token iterator to a data element.
     *
     * @param it the given {@link TokenIterator token iterator}
     * @return the corresponding {@link X86DataElement data element}
     * @throws X86AssemblyException
     */
    private static X86DataElement nextDataElement(final TokenIterator it) throws X86AssemblyException {
        // get the next token
        final String token = nextElement(it);

        // is it a quantifier (e.g. 'byte ptr')?
        if (QUANTIFIERS.contains(token)) {
            throw new X86MalformedInstructionException("'" + token + "' is not yet implemented");
        }

        // is it a numeric or string value?
        else if (isNumeric(token) || isString(token)) {
            return parseValue(token);
        }

        // is it a register?
        else if (isRegister(token)) {
            return lookupRegister(token);
        }

        // is it a memory reference?
        else if (isReference(token)) {
            return parseReference(token);
        }

        // unrecognized
        else {
            throw new X86MalformedInstructionException("unrecognized element '" + token + "'");
        }
    }

}
