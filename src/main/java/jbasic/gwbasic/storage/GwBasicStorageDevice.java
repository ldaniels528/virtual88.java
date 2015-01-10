package jbasic.gwbasic.storage;

import ibmpc.devices.memory.OutOfMemoryException;
import jbasic.common.exceptions.FileNotFoundException;
import jbasic.common.exceptions.JBasicException;
import jbasic.common.program.JBasicProgramStatement;
import jbasic.common.program.JBasicSourceCode;
import jbasic.gwbasic.program.GwBasicStatement;
import jbasic.gwbasic.values.GwBasicValues;
import msdos.storage.MsDosStorageSystem;

import java.io.*;
import java.util.Collection;

/**
 * GWBASIC/BASICA Storage Device
 *
 * @author lawrence.daniels@gmail.com
 */
public class GwBasicStorageDevice extends MsDosStorageSystem {

    /**
     * Loads a program into the given environment
     *
     * @param program the given {@link JBasicSourceCode JBasic source code}
     * @param file    the given {@link File file path}
     * @throws JBasicException
     * @throws OutOfMemoryException
     */
    public void load(final JBasicSourceCode program, final File file) throws JBasicException, OutOfMemoryException {
        // load the new program
        try (final BufferedReader reader = new BufferedReader(new FileReader(file))) {
            // clear the current program
            program.clear();

            // load the data
            String line;
            while ((line = reader.readLine()) != null) {
                line = trim(line);
                int index = line.indexOf(' ');
                if (index != -1) {
                    int lineNo = GwBasicValues.parseIntegerString(line.substring(0, index));
                    String text = line.substring(index, line.length());
                    program.add(new GwBasicStatement(lineNo, text));
                }
            }
        } catch (final java.io.FileNotFoundException e) {
            throw new FileNotFoundException(file);
        } catch (final IOException e) {
            throw new JBasicException(e);
        }
    }

    /**
     * Saves the current program into the given environment
     *
     * @param program the given {@link JBasicSourceCode JBasic source code}
     * @param file    the given {@link File file path}
     * @throws JBasicException
     */
    public void save(final JBasicSourceCode program, final File file) throws JBasicException {
        // get the contents of the program
        final Collection<JBasicProgramStatement> statements = program.getStatements();

        // save the program to disk
        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (JBasicProgramStatement statement : statements) {
                writer.write(statement.toString());
                writer.newLine();
            }
            writer.flush();
        } catch (Exception e) {
            throw new JBasicException(e);
        }
    }

    /**
     * Trims the given string
     *
     * @param s the given string
     * @return a trimmed version of the given string
     */
    private String trim(final String s) {
        final StringBuilder sb = new StringBuilder(s.trim());
        while (sb.length() > 0 && sb.charAt(0) == ' ') {
            sb.deleteCharAt(0);
        }
        return sb.toString();
    }

}
