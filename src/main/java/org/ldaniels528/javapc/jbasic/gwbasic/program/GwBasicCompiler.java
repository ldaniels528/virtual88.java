package org.ldaniels528.javapc.jbasic.gwbasic.program;

import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenIterator;
import org.ldaniels528.javapc.jbasic.common.tokenizer.Tokenizer;
import org.ldaniels528.javapc.jbasic.common.tokenizer.TokenizerContext;
import org.ldaniels528.javapc.jbasic.common.tokenizer.parsers.*;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;
import org.ldaniels528.javapc.jbasic.common.exceptions.JBasicException;
import org.ldaniels528.javapc.jbasic.common.exceptions.NeverShouldHappenException;
import org.ldaniels528.javapc.jbasic.common.exceptions.SyntaxErrorException;
import org.ldaniels528.javapc.jbasic.common.program.*;
import org.ldaniels528.javapc.jbasic.gwbasic.GwBasicEnvironment;
import org.ldaniels528.javapc.jbasic.gwbasic.program.commands.*;
import org.ldaniels528.javapc.jbasic.gwbasic.program.commands.console.*;
import org.ldaniels528.javapc.jbasic.gwbasic.program.commands.control.*;
import org.ldaniels528.javapc.jbasic.gwbasic.program.commands.events.OnErrorOp;
import org.ldaniels528.javapc.jbasic.gwbasic.program.commands.events.OnKeyOp;
import org.ldaniels528.javapc.jbasic.gwbasic.program.commands.events.OnOp;
import org.ldaniels528.javapc.jbasic.gwbasic.program.commands.graphics.*;
import org.ldaniels528.javapc.jbasic.gwbasic.program.commands.ide.*;
import org.ldaniels528.javapc.jbasic.gwbasic.program.commands.io.*;
import org.ldaniels528.javapc.jbasic.gwbasic.program.commands.io.device.*;
import org.ldaniels528.javapc.jbasic.gwbasic.program.commands.io.file.*;
import org.ldaniels528.javapc.jbasic.gwbasic.program.commands.system.*;
import org.ldaniels528.javapc.jbasic.gwbasic.values.GwBasicValues;

import java.util.*;

/**
 * BASICA/GWBASIC Statement Compiler
 *
 * @author lawrence.daniels@gmail.com
 */
@SuppressWarnings("unchecked")
public class GwBasicCompiler implements JBasicCompiler {
    private static final GwBasicCompiler instance = new GwBasicCompiler();
    // constants
    private static final String EQUALS = "=";
    private static final String BLANK = "";
    private static final Set ON_OFF = new HashSet(Arrays.asList(new String[]{"ON", "OFF"}));
    private final Tokenizer tokenizer;

    /**
     * Default constructor
     */
    private GwBasicCompiler() {
        this.tokenizer = getConfiguredTokenizer();
    }

    /**
     * Returns the singleton compiler instance
     *
     * @return the singleton {@link GwBasicCompiler compiler instance}
     */
    public static GwBasicCompiler getInstance() {
        return instance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OpCode compile(final TokenIterator it) throws JBasicException {
        // failsafe
        if (!it.hasNext()) return new NoOp();

        // get the first token from the statement
        final String token1 = it.next();
        final String token2 = it.hasNext() ? it.peekAtNext() : BLANK;

        // interpret and return the opcode result
        return compile(token1, token2, it);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JBasicCompiledCode compile(final JBasicSourceCode program)
            throws JBasicException {
        // get the environment object
        final GwBasicEnvironment environment = (GwBasicEnvironment) program.getEnvironment();

        // create a compiled code object
        final GwBasicCompiledCode compiledCode = new GwBasicCompiledCode(environment);

        // get the statements from the program
        final Collection<JBasicProgramStatement> statements = program.getStatements();

        // iterate the statements, compiling each statement into opCode(s)
        for (final JBasicProgramStatement statement : statements) {
            try {
                // track the line number of the first opCode of each statement
                final String lineNumber = String.valueOf(statement.getLineNumber());
                compiledCode.addLabel(lineNumber);

                // compile the opCodes
                final GwBasicStatement gwStmt = (GwBasicStatement) statement;
                final List<GwBasicCommand> opCodeSet = compile(gwStmt, compiledCode);

                // compile the statement and capture the resultant opCode(s)
                compiledCode.addAll(opCodeSet);
            } catch (final JBasicException e) {
                throw new GwBasicProgramSyntaxException(e, statement.getLineNumber());
            } catch (final Exception e) {
                throw new NeverShouldHappenException(e);
            }
        }

        // return the compiled code
        return compiledCode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JBasicCompiledCode compile(final IbmPcSystem environment,
                                      final JBasicProgramStatement statement) throws JBasicException {
        // cast to a GWBASIC environment
        final GwBasicEnvironment gwEnvironment = (GwBasicEnvironment) environment;

        // create a reference to compile code
        final GwBasicCompiledCode compiledCode = new GwBasicCompiledCode(gwEnvironment);

        // retrieve the actual code from the statement
        String code = statement.getCode();

        // if the code is not blank, evaluate it
        while ((code != null) && !code.equals("")) {
            // get the index of the end of the chunk
            final int index = endOfChunk(code);

            // get a chunk of code
            final String chunk = code.substring(0, index);

            // tokenize the statement
            final TokenIterator it = tokenize(chunk);

            // translate the sections into a command/opCode
            // is it a new statement (beginning with a line number)?
            final GwBasicCommand command = GwBasicValues.isNumericConstant(it.peekAtNext())
                    ? new AddProgramLineOp(it)
                    : (GwBasicCommand) compile(it);

            // execute the opCode
            compiledCode.add(command);

            // point to the next chunk
            code = (index + 1 >= code.length()) ? null : code.substring(index + 1, code.length());
        }

        return compiledCode;
    }

    /**
     * Compile the given statement into a set of {@link GwBasicCommand opCodes}
     *
     * @param statement    the given {@link org.ldaniels528.javapc.jbasic.common.program.JBasicProgramStatement statement}
     * @param compiledCode the given {@link org.ldaniels528.javapc.jbasic.common.program.JBasicSourceCode program}
     * @return a {@link List list} of {@link GwBasicCommand opCodes}
     */
    private List<GwBasicCommand> compile(final GwBasicStatement statement, final JBasicCompiledCode compiledCode) throws JBasicException {
        // get a copy of the statement
        String code = statement.getCode();

        // create a container for return the opCodes
        final List<GwBasicCommand> opCodes = new LinkedList<>();

        // compile the statement into opCodes
        while ((code != null) && !code.equals("")) {
            // trim off any comments
            final int endOfComment = getCommentIndex(code);
            if (endOfComment != -1) {
                code = code.substring(0, endOfComment);
            }

            // get the index of the end of the chunk
            final int endOfChunk = endOfChunk(code);

            // get a chunk of code
            final String chunk = code.substring(0, endOfChunk);

            // tokenize the statement
            final TokenIterator it = tokenize(chunk);

            // compile the tokenized statement into a command/opCode
            final GwBasicCommand command = (GwBasicCommand) compile(it);
            command.setLineNumber(statement.getLineNumber());

            // pre-process the opCode            
            if (command instanceof PreProcessedOpCode) {
                final PreProcessedOpCode preprocessibleOpCode = (PreProcessedOpCode) command;
                preprocessibleOpCode.preProcess(compiledCode);
            }

            // add the opCode to our list of opCodes
            if (!(command instanceof NoOp))
                opCodes.add(command);

            // point to the next chunk
            code = (endOfChunk + 1 >= code.length()) ? null : code.substring(endOfChunk + 1, code.length());
        }
        return opCodes;
    }

    /**
     * Translates the given token iterator into
     *
     * @param token1 the first token in the iterator
     * @param token2 the second token in the iterator
     * @param it     the {@link TokenIterator token iterator}
     * @return the resultant {@link OpCode opcode}
     * @throws GwBasicProgramSyntaxException
     */
    private GwBasicCommand compile(final String token1,
                                   final String token2,
                                   final TokenIterator it) throws JBasicException {
        GwBasicCommand opCode;

        // get the first character of the first token
        final char firstChar = token1.charAt(0);

        // if it's an assembly instruction ...
        if (firstChar == '!') {
            return new AssemblyOp(it);
        }

        // if it's between 'A' and 'F' ...
        else if (firstChar >= 'A' && firstChar <= 'F') {
            if ((opCode = compile_A_to_F(token1, token2, it)) != null)
                return opCode;
        }

        // if it's between 'G' and 'M' ...
        else if (firstChar >= 'G' && firstChar <= 'M') {
            if ((opCode = compile_G_to_M(token1, token2, it)) != null)
                return opCode;
        }

        // if it's between 'N' and 'P' ...
        else if (firstChar >= 'N' && firstChar <= 'P') {
            if ((opCode = compile_N_to_P(token1, token2, it)) != null)
                return opCode;
        }

        // if it's between 'R' and 'Z' ...
        else if (firstChar >= 'R' && firstChar <= 'Z') {
            if ((opCode = compile_R_to_Z(token1, token2, it)) != null)
                return opCode;
        }

        // is it an assignment?
        if (it.contains(EQUALS)) {
            it.unGet(token1);
            return new AssignmentOp(it);
        }

        // not recognized
        throw new SyntaxErrorException();
    }

    /**
     * Compiles all instructions that start with 'A' through 'F'
     *
     * @param token1 the first token in the iterator
     * @param token2 the second token in the iterator
     * @param it     the {@link TokenIterator token iterator}
     * @throws JBasicException
     */
    private GwBasicCommand compile_A_to_F(final String token1,
                                          final String token2,
                                          final TokenIterator it) throws JBasicException {
        // BEEP
        switch (token1) {
            case "BEEP":
                return new BeepOp(it);
            // BLOAD
            case "BLOAD":
                return new BloadOp(it);
            // BSAVE
            case "BSAVE":
                return new BsaveOp(it);
            // CHDIR
            case "CHDIR":
                return new ChdirOp(it);
            // CIRCLE
            case "CIRCLE":
                return new DrawCircleOp(it);
            // CLOSE
            case "CLOSE":
                return new CloseOp(it);
            // CLS
            case "CLS":
                return new ClearScreenOp(it);
            // COLOR
            case "COLOR":
                return new ColorOp(it);
            // COMMON
            case "COMMON":
                return new CommonOp(it);
            // DATA
            case "DATA":
                return new DataOp(it);
            // DEF FN/SEG/USR
            case "DEF":
                if (token2.equals("FN")) return new DefFnOp(it);
                else if (token2.equals("SEG")) return new DefSegOp(it);
                else if (token2.startsWith("USR")) return new DefUsrOp(it);
                break;
            // DEF[DBL,INT,SNG,STR]
            case "DEFDBL":
                return new DefDblOp(it);
            case "DEFINT":
                return new DefIntOp(it);
            case "DEFSNG":
                return new DefSngOp(it);
            case "DEFSTR":
                return new DefStrOp(it);
            // DELETE
            case "DELETE":
                return new DeleteOp(it);
            // DIM
            case "DIM":
                return new DimOp(it);
            // DRAW
            case "DRAW":
                return new DrawOp(it);
            // EDIT
            case "EDIT":
                return new EditOp(it);
            // END
            case "END":
                return new EndOp(it);
            // ERASE
            case "ERASE":
                return new EraseOp(it);
            // FILES
            case "FILES":
                return new FilesOp(it);
            // FOR
            case "FOR":
                return new ForOp(it);
        }
        // Unknown
        return null;
    }

    /**
     * Compiles all instructions that start with 'G' through 'M'
     *
     * @param token1 the first token in the iterator
     * @param token2 the second token in the iterator
     * @param it     the {@link TokenIterator token iterator}
     * @throws JBasicException
     */
    private GwBasicCommand compile_G_to_M(final String token1,
                                          final String token2,
                                          final TokenIterator it)
            throws JBasicException {
        // GET
        switch (token1) {
            case "GET":
                return new GetImageOp(it);
            // GOSUB
            case "GOSUB":
                return new GosubOp(it);
            // GOTO
            case "GOTO":
                return new GotoOp(it);
            // IF
            case "IF":
                return new IfOp(it);
            // INPUT
            case "INPUT":
                return new InputOp(it);
            // KEY
            case "KEY":
                // KEY ON|OFF
                if (ON_OFF.contains(token2)) return new KeyOp(it);
                    // KEY 1, "FILES"
                else return new KeyLabelOp(it);

                // KILL
            case "KILL":
                return new KillOp(it);
            // LET    
            case "LET":
                return new AssignmentOp(it);
            // LINE
            case "LINE":
                // LINE INPUT
                if (token2.equals("INPUT")) return new LineInputOp(it);
                    // LINE (graphics)
                else return new DrawLineOp(it);

                // LIST
            case "LIST":
                return new ListOp(it);
            // LOAD
            case "LOAD":
                return new LoadOp(it);
            // LOCATE
            case "LOCATE":
                return new LocateOp(it);
            // LOCK
            case "LOCK":
                return new LockOp(it);
            // MKDIR
            case "MKDIR":
                return new MkdirOp(it);
        }
        // not recognized
        return null;
    }

    /**
     * Compiles all instructions that start with 'N' through 'P'
     *
     * @param token1 the first token in the iterator
     * @param token2 the second token in the iterator
     * @param it     the {@link TokenIterator token iterator}
     * @throws JBasicException
     */
    private GwBasicCommand compile_N_to_P(final String token1,
                                          final String token2,
                                          final TokenIterator it)
            throws JBasicException {
        // NAME
        switch (token1) {
            case "NAME":
                return new NameOp(it);
            // NEW
            case "NEW":
                return new NewOp(it);
            // NEXT
            case "NEXT":
                return new NextOp(it);
            // ON
            case "ON":
                // ON ERROR GOTO ...
                switch (token2) {
                    case "ERROR":
                        return new OnErrorOp(it);
                    // ON KEY n GOSUB ...
                    case "KEY":
                        return new OnKeyOp(it);
                    // ON n GOTO/GOSUB ...
                    default:
                        return new OnOp(it);
                }

                // OPEN
            case "OPEN":
                return new OpenOp(it);
            // OPTION
            case "OPTION":
                return new OptionBaseOp(it);
            // PAINT
            case "PAINT":
                return new PaintOp(it);
            // PCOPY
            case "PCOPY":
                return new PCopyOp(it);
            // POKE
            case "POKE":
                return new PokeCommand(it);
            // PRESET/PSET
            case "PRESET":
            case "PSET":
                return new PreSetOp(it);
            // PRINT
            case "PRINT":
                // PRINT#
                if (token2.equals("#")) return new PrintDevOp(it);
                    // PRINT
                else return new PrintOp(it);

                // PUT
            case "PUT":
                return new PutImageOp(it);
        }
        // Unknown
        return null;
    }

    /**
     * Compiles all instructions that start with 'R' through 'Z'
     *
     * @param token1 the first token in the iterator
     * @param token2 the second token in the iterator
     * @param it     the {@link TokenIterator token iterator}
     * @throws JBasicException
     */
    private GwBasicCommand compile_R_to_Z(final String token1,
                                          final String token2,
                                          final TokenIterator it)
            throws JBasicException {
        // RANDOMIZE
        switch (token1) {
            case "RANDOMIZE":
                return new RandomizeOp(it);
            // READ
            case "READ":
                return new ReadOp(it);
            // REM
            case "REM":
                return new RemarkOp(it);
            // RESTORE
            case "RESTORE":
                return new RestoreOp(it);
            // RESUME
            case "RESUME":
                return new ResumeOp(it);
            // RETURN
            case "RETURN":
                return new ReturnOp(it);
            // RMDIR
            case "RMDIR":
                return new RmdirOp(it);
            // RUN
            case "RUN":
                return new RunOp(it);
            // SAVE
            case "SAVE":
                return new SaveOp(it);
            // SCREEN
            case "SCREEN":
                return new ScreenOp(it);
            // SHELL
            case "SHELL":
                return new ShellOp(it);
            // SYSTEM
            case "SYSTEM":
                return new SystemOp(it);
            // SWAP
            case "SWAP":
                return new SwapOp(it);
            // TRON
            case "TRON":
                return new TraceOnOp(it);
            // TROFF
            case "TROFF":
                return new TraceOffOp(it);
            // UNLOCK
            case "UNLOCK":
                return new UnlockOp(it);
            // WHILE
            case "WHILE":
                return new WhileOp(it);
            // WIDTH
            case "WIDTH":
                return new WidthOp(it);
            // WINDOW    
            case "WINDOW":
                return new WindowOp(it);
            // WEND
            case "WEND":
                return new WhileEndOp(it);
        }
        // Unknown
        return null;
    }

    /**
     * Returns the index of the end of the code chunk
     *
     * @param code the given source code block
     * @return the index of the end of the code chunk
     */
    private int endOfChunk(final String code) {
        boolean inQuotes = false;
        final char[] chars = code.toCharArray();
        int i;
        for (i = 0; i < chars.length; i++) {
            if (chars[i] == '"') inQuotes = !inQuotes;
            if (!inQuotes && chars[i] == ':') return i;
        }
        return i;
    }

    /**
     * Returns the index of the beginning of a
     * code comment chunk
     *
     * @param code the given source code block
     * @return the index of the beginning of the comment code chunk
     */
    private int getCommentIndex(final String code) {
        boolean inQuotes = false;
        final char[] chars = code.toCharArray();
        int i;
        for (i = 0; i < chars.length; i++) {
            if (chars[i] == '"') inQuotes = !inQuotes;
            if (!inQuotes && chars[i] == '\'') return i;
        }
        return i;
    }

    /**
     * @return a GWBASIC configured tokenizer
     */
    private Tokenizer getConfiguredTokenizer() {
        final Tokenizer tokenizer = new Tokenizer();
        tokenizer.add(new EndOfLineTokenParser());
        tokenizer.add(new NumericTokenParser());
        tokenizer.add(new OperatorTokenParser());
        tokenizer.add(new DoubleQuotedTextTokenParser());
        tokenizer.add(new TextTokenParser());
        return tokenizer;
    }

    /**
     * Tokenizes the given text into sections
     *
     * @param text the given {@link String text}
     * @return a {@link List list} of tokens
     */
    private TokenIterator tokenize(final String text) {
        final TokenizerContext context = tokenizer.parse(text);
        return tokenizer.nextTokens(context);
    }

}
