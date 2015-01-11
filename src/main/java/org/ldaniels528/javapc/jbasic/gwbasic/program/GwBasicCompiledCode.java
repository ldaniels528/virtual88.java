package org.ldaniels528.javapc.jbasic.gwbasic.program;

import org.ldaniels528.javapc.ibmpc.app.IbmPcAssemblyLanguageCompiler;
import org.ldaniels528.javapc.ibmpc.devices.cpu.Intel80x86;
import org.ldaniels528.javapc.ibmpc.devices.cpu.ProgramContext;
import org.ldaniels528.javapc.ibmpc.devices.display.IbmPcDisplay;
import org.ldaniels528.javapc.ibmpc.devices.keyboard.IbmPcKeyEventListener;
import org.ldaniels528.javapc.ibmpc.devices.keyboard.IbmPcKeyboard;
import org.ldaniels528.javapc.ibmpc.devices.memory.IbmPcRandomAccessMemory;
import org.ldaniels528.javapc.ibmpc.devices.memory.MemoryManager;
import org.ldaniels528.javapc.ibmpc.devices.memory.MemoryObject;
import org.ldaniels528.javapc.ibmpc.devices.memory.OutOfMemoryException;
import org.ldaniels528.javapc.ibmpc.exceptions.X86AssemblyException;
import org.ldaniels528.javapc.ibmpc.system.IbmPcSystem;
import org.ldaniels528.javapc.jbasic.common.exceptions.*;
import org.ldaniels528.javapc.jbasic.common.program.AbortableOpCode;
import org.ldaniels528.javapc.jbasic.common.program.ConditionalControlBlock;
import org.ldaniels528.javapc.jbasic.common.program.ConditionalOpCode;
import org.ldaniels528.javapc.jbasic.common.program.JBasicCompiledCode;
import org.ldaniels528.javapc.jbasic.common.values.*;
import org.ldaniels528.javapc.jbasic.common.values.impl.SimpleConstant;
import org.ldaniels528.javapc.jbasic.common.values.impl.SimpleVariable;
import org.ldaniels528.javapc.jbasic.common.values.impl.SimpleVariableArray;
import org.ldaniels528.javapc.jbasic.common.values.systemvariables.*;
import org.ldaniels528.javapc.jbasic.common.values.types.impl.JBasicNumber;
import org.ldaniels528.javapc.jbasic.common.values.types.impl.JBasicString;
import org.ldaniels528.javapc.jbasic.gwbasic.GwBasicEnvironment;
import org.ldaniels528.javapc.jbasic.gwbasic.program.commands.GwBasicCommand;
import org.apache.log4j.Logger;

import java.awt.event.KeyEvent;
import java.util.*;

import static java.lang.String.format;

/**
 * Represents GWBASICA/BASICA Program Source Code
 *
 * @author lawrence.daniels@gmail.com
 */
public class GwBasicCompiledCode implements JBasicCompiledCode, IbmPcKeyEventListener {
    // constants
    private static final int PROGRAM_SEGEMENT = GwBasicEnvironment.PROGRAM_SEGEMENT;

    // generic fields
    private final List<VariableTypeDefinition> typeDefs;
    private final List<SimpleConstant> dataValues;
    private final List<GwBasicCommand> opCodes;
    private final Map<String, VariableArray> arrays;
    private final Map<String, Variable> variables;
    private final IbmPcRandomAccessMemory memory;
    private final MemoryManager memoryManager;
    private final GwBasicEnvironment environment;
    private final Random random;
    private final int programSegment;
    private int defaultMemorySegment;
    private int dataPtr;

    // control flow-based fields
    private final Logger logger = Logger.getLogger(getClass());
    private final Map<String, ConditionalControlBlock> controlBlockMapping;
    private final LinkedList<ConditionalControlBlock> controlStack;
    private final LinkedList<Integer> callStack;
    protected final Map<String, Integer> labelMapping;
    private int opCodePointer;
    private boolean trace;

    /**
     * Default constructor
     */
    public GwBasicCompiledCode(final GwBasicEnvironment environment) {
        this.environment = environment;
        this.memory = environment.getRandomAccessMemory();
        this.memoryManager = environment.getMemoryManager();
        this.dataValues = new LinkedList<>();
        this.typeDefs = new LinkedList<>();
        this.variables = new HashMap<>();
        this.arrays = new HashMap<>();
        this.random = new Random(System.currentTimeMillis());
        this.opCodes = new LinkedList<>();
        this.programSegment = PROGRAM_SEGEMENT;
        this.defaultMemorySegment = PROGRAM_SEGEMENT;

        this.controlBlockMapping = new LinkedHashMap<>();
        this.controlStack = new LinkedList<>();
        this.callStack = new LinkedList<>();
        this.labelMapping = new HashMap<>();
        this.trace = false;

        // add system variables
        addSystemVariables();

        // register the program as a listener to the keyboard
        final IbmPcKeyboard keyboard = environment.getKeyboard();
        keyboard.register(this);
    }

    public GwBasicEnvironment getEnvironment() {
        return environment;
    }

    ///////////////////////////////////////////////////////
    //      Event Handler Method(s)
    ///////////////////////////////////////////////////////

    /**
     * {@inheritDoc}
     */
    @Override
    public void keyPressed(final IbmPcKeyboard keyboard, final KeyEvent event) {
        // if CTRL key pressed ...
        if (event.isControlDown()) {
            final char keyChar = event.getKeyChar();

            // CTRL-C?
            if (keyChar == 3) {
                // create a CTRL-C event
                // TODO do something here
            }
            return;
        }

        // set the INKEY$ variables
        final InKeyVariable variable = (InKeyVariable) variables.get("INKEY$");
        if (variable != null) variable.update(event);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void keyReleased(final IbmPcKeyboard keyboard, final KeyEvent event) {
        // do nothing
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void add(final VariableTypeDefinition typeDef) {
        typeDefs.add(typeDef);
    }

    /**
     * Adds the given command to the compiled code
     *
     * @param command the given {@link GwBasicCommand command/opCode}
     */
    public void add(final GwBasicCommand command) {
        opCodes.add(command);
    }

    /**
     * Adds the given command to the compiled code
     *
     * @param commands the given {@link GwBasicCommand commands/opCodes}
     */
    public void addAll(final Collection<GwBasicCommand> commands) {
        opCodes.addAll(commands);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addLabel(final String lineNumber) {
        labelMapping.put(lineNumber, opCodes.size());
    }

    /**
     * Returns the array of command/opCodes held by this container
     *
     * @return the array of command/opCodes held by this container
     */
    public GwBasicCommand[] getCommands() {
        return opCodes.toArray(new GwBasicCommand[opCodes.size()]);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int assemble(final String... asmCodes) throws JBasicException {
        try {
            // create the x86 opCodes
            final byte[] x86Code = IbmPcAssemblyLanguageCompiler.encode(asmCodes);

            // allocate memory for the opCodes
            final int offset = memoryManager.allocate(x86Code.length);

            // write the opCodes to memory
            memory.setBytes(programSegment, offset, x86Code, x86Code.length);
            return offset;
        } catch (final X86AssemblyException e) {
            throw new JBasicException(e);
        }
    }

    /**
     * Clears the program; including all arrays, variables and other data
     */
    public void clear() {
        // clear out all references
        arrays.clear();
        variables.clear();
        dataValues.clear();
        memoryManager.clear();

        // reset points
        this.defaultMemorySegment = programSegment;
        this.dataPtr = 0;

        // add the system variables
        addSystemVariables();
    }

    /**
     * Sets the state of the trace (debugging) mechanism on/off
     *
     * @param state the state of the trace
     */
    public void setTrace(final boolean state) {
        this.trace = state;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void randomize(final long seed) {
        random.setSeed(seed);
    }

    ///////////////////////////////////////////////////////
    //      Program-related Method(s)
    ///////////////////////////////////////////////////////

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCodeSegment() {
        return programSegment;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDataSegment() {
        return programSegment;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDefaultMemorySegment() {
        return defaultMemorySegment;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDefaultMemorySegment(final int defaultSegment) {
        this.defaultMemorySegment = defaultSegment;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IbmPcSystem getSystem() {
        return environment;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MemoryManager getMemoryManager() {
        return memoryManager;
    }

    ///////////////////////////////////////////////////////
    //      Data-related Method(s)
    ///////////////////////////////////////////////////////

    /**
     * {@inheritDoc}
     */
    @Override
    public void addData(final SimpleConstant constant) {
        dataValues.add(constant);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SimpleConstant getNextData() throws JBasicException {
        // make sure there is at least one more value
        if (dataPtr >= dataValues.size())
            throw new JBasicException("No more data");

        // return the value
        return dataValues.get(dataPtr++);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resetDataPointer() {
        this.dataPtr = 0;
    }


    ///////////////////////////////////////////////////////
    //      Variable-related Method(s)
    ///////////////////////////////////////////////////////

    /**
     * {@inheritDoc}
     */
    @Override
    public MemoryObject createObject(final String name)
            throws OutOfMemoryException {
        // extract the last character from the variable's name
        final char lastChar = name.charAt(name.length() - 1);

        // determine which type of object to return
        switch (lastChar) {
            case '%':
                return memoryManager.createInteger();
            case '!':
                return memoryManager.createSinglePrecisionDecimal();
            case '#':
                return memoryManager.createDoublePrecisionDecimal();
            case '$':
                return memoryManager.createString();
            default:
                final char firstChar = name.charAt(0);
                for (VariableTypeDefinition typeDef : typeDefs) {
                    if ((firstChar >= typeDef.getStart()) && (firstChar <= typeDef.getEnd())) {
                        switch (typeDef.getType()) {
                            case VariableTypeDefinition.TYPE_INTEGER:
                                return memoryManager.createInteger();
                            case VariableTypeDefinition.TYPE_SINGLE_PREC:
                                return memoryManager.createSinglePrecisionDecimal();
                            case VariableTypeDefinition.TYPE_DOUBLE_PREC:
                                return memoryManager.createDoublePrecisionDecimal();
                            case VariableTypeDefinition.TYPE_STRING:
                                return memoryManager.createString();
                        }
                    }
                }
                return memoryManager.createSinglePrecisionDecimal();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MemoryObject wrapObject(final String name, final int offset) {
        // extract the last character from the variable's name
        final char lastChar = name.charAt(name.length() - 1);

        // determine which type of object to return
        switch (lastChar) {
            case '%':
                return new JBasicNumber(memoryManager, PROGRAM_SEGEMENT, offset, JBasicNumber.MODE_INTEGER);
            case '!':
                return new JBasicNumber(memoryManager, PROGRAM_SEGEMENT, offset, JBasicNumber.MODE_SINGLE_PREC);
            case '#':
                return new JBasicNumber(memoryManager, PROGRAM_SEGEMENT, offset, JBasicNumber.MODE_DOUBLE_PREC);
            case '$':
                return new JBasicString(memoryManager, PROGRAM_SEGEMENT, offset);
            default:
                final char firstChar = name.charAt(0);
                for (VariableTypeDefinition typeDef : typeDefs) {
                    if ((firstChar >= typeDef.getStart()) && (firstChar <= typeDef.getEnd())) {
                        switch (typeDef.getType()) {
                            case VariableTypeDefinition.TYPE_INTEGER:
                                return new JBasicNumber(memoryManager, PROGRAM_SEGEMENT, offset, JBasicNumber.MODE_INTEGER);
                            case VariableTypeDefinition.TYPE_SINGLE_PREC:
                                return new JBasicNumber(memoryManager, PROGRAM_SEGEMENT, offset, JBasicNumber.MODE_SINGLE_PREC);
                            case VariableTypeDefinition.TYPE_DOUBLE_PREC:
                                return new JBasicNumber(memoryManager, PROGRAM_SEGEMENT, offset, JBasicNumber.MODE_DOUBLE_PREC);
                            case VariableTypeDefinition.TYPE_STRING:
                                return new JBasicString(memoryManager, PROGRAM_SEGEMENT, offset);
                        }
                    }
                }
                return new JBasicNumber(memoryManager, PROGRAM_SEGEMENT, offset, JBasicNumber.MODE_SINGLE_PREC);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void executeAssembly(final int offset) throws JBasicException {
        // create an execution context
        final ProgramContext context =
                new ProgramContext(
                        defaultMemorySegment,
                        defaultMemorySegment,
                        offset,
                        null
                );


        try {
            // get the CPU instance
            final Intel80x86 cpu = environment.getCPU();

            // execute the code
            cpu.execute(environment, context);
        } catch (final X86AssemblyException e) {
            throw new JBasicException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createVariable(final Variable variable) {
        variables.put(variable.getName(), variable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createArrayVariable(final String name, final int length)
            throws OutOfMemoryException {
        // compute the size of the memory block
        final int size = length * 2;

        // create a memory block large enough for the array
        final int offset = memoryManager.allocate(size);

        // create the array using the previously created variables
        final SimpleVariableArray array = new SimpleVariableArray(this, name, length, offset, size);

        // add the array to the collection of arrays
        arrays.put(array.getName(), array);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void destroyVariable(String variableName)
            throws JBasicException {
        // the array must already exist
        if (!variables.containsKey(variableName))
            throw new IllegalFunctionCallException();

        // remove the array object
        final Variable variable = variables.remove(variableName);

        // deallocate the object within the array
        if (variable instanceof SimpleVariable) {
            SimpleVariable gwBasicVariable = (SimpleVariable) variable;
            gwBasicVariable.destroy();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void destroyVariableArray(String arrayName)
            throws JBasicException {
        // the array must already exist
        if (!arrays.containsKey(arrayName))
            throw new IllegalFunctionCallException();

        // remove the array object
        final VariableArray array = arrays.remove(arrayName);

        // deallocate the object within the array
        array.destroy();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Variable getVariable(VariableReference reference) throws OutOfMemoryException {
        // get the name of the referenced variable
        final String name = reference.getName();

        // if the variable doesn't yet exist, create it
        if (!variables.containsKey(name))
            variables.put(name, createVariable(reference));

        // return the variable
        return variables.get(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Variable getArrayVariable(VariableArrayIndexReference reference) {
        // get the name of the variable array
        final String name = reference.getName();

        // get the index being referenced
        final int index = reference.getIndex().getValue(this).toInteger();

        // if the variable doesn't yet exist, create it
        if (!arrays.containsKey(name))
            throw new SubscriptOutOfRangeException(name, index);

        // lookup the array
        final VariableArray array = arrays.get(name);

        // return the variable
        return array.getElement(index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() throws JBasicException {
        // get the opCodes
        final GwBasicCommand[] opCodes = this.getCommands();

        // get the screen instance, and create a string buffer (used for traces)
        final IbmPcDisplay screen = environment.getDisplay();
        final StringBuilder buffer = new StringBuilder(10);

        // execute the program
        GwBasicCommand opCode = null;
        try {
            // create opCode counter
            final long startTime = System.currentTimeMillis();
            int counter = 0;

            // iterate the opCodes
            for (opCodePointer = 0; opCodePointer < opCodes.length; opCodePointer++) {
                // get the instruction
                opCode = opCodes[opCodePointer];

                // advance the count
                counter++;

                // if trace is on, display the line number
                if (trace) {
                    // clear the buffer
                    if (buffer.length() > 0) buffer.delete(0, buffer.length());
                    // write the current line number to the buffer
                    buffer.append('[').append(opCode.getLineNumber()).append(']');
                    // write the contents of the buffer to the screen
                    screen.write(buffer.toString());
                }

                // execute the instruction
                opCode.execute(this);

                // if this instruction an instruction to abort execution?
                if ((opCode instanceof AbortableOpCode) && ((AbortableOpCode) opCode).abort()) break;
            }

            // display statistics
            final long elapsed = System.currentTimeMillis() - startTime;
            final double avg = (double) counter / (double) elapsed;
            logger.info(format("program executed %d opCodes in %d msec(s): %f opCodes/msec", counter, elapsed, avg));
        } catch (final RuntimeException e) {
            // get the opCode's line number
            assert opCode != null;
            final int lineNumber = opCode.getLineNumber();
            // re-throw the exception using the line number
            throw new GwBasicProgramSyntaxException(e, lineNumber);
        } catch (final JBasicException e) {
            // get the opCode's line number
            final int lineNumber = opCode.getLineNumber();
            // re-throw the exception using the line number
            throw new GwBasicProgramSyntaxException(e, lineNumber);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void conditionalControlBegin(final Variable variable, final ConditionalOpCode opCode) {
        // create the control block
        final ConditionalControlBlock controlBlock = new ConditionalControlBlock(variable, opCode, opCodePointer);

        // add it to the mapping
        controlBlockMapping.put(variable.getName(), controlBlock);

        // add it to the stack
        controlStack.add(controlBlock);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void conditionalControlIterate(final JBasicCompiledCode compiledCode, final Variable variable)
            throws JBasicException {
        // lookup the condition control block
        final ConditionalControlBlock controlBlock = controlBlockMapping.get(variable.getName());

        // remove this control block from the mapping
        //controlStack.removeLast();

        // if no control block was found, error
        if (controlBlock == null)
            throw new SyntaxErrorException();

        // if the condition wasn't satisfied, loop ...
        if (!controlBlock.conditionSatisfied(compiledCode)) {
            opCodePointer = controlBlock.getOpCodePointer();
        } else {
            controlBlockMapping.remove(variable.getName());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Variable getLastControlStackVariable() {
        // get the last control block from the stack
        final ConditionalControlBlock controlBlock = controlStack.removeLast();

        // return the variable
        return controlBlock.getVariable();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void gotoLabel(final String label, final boolean returns)
            throws JBasicException {
        // if needs to return, setup call stack
        if (returns) callStack.add(opCodePointer);

        // make sure jump point exists
        if (!labelMapping.containsKey(label)) {
            throw new InvalidLabelException(label);
        }

        // get jump point
        final int jumpPoint = labelMapping.get(label);

        // set the Instruction Point to the given offset
        opCodePointer = jumpPoint - 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void popFromCallStack() throws JBasicException {
        // stack must not be empty
        if (callStack.isEmpty())
            throw new JBasicException("Stack is empty");

        // get the new offset from the stack
        opCodePointer = callStack.removeLast();
    }

    ///////////////////////////////////////////////////////
    //      Internal Service Methods
    ///////////////////////////////////////////////////////

    /**
     * Adds built-in system variables
     */
    private void addSystemVariables() {
        this.createVariable(new CursorLineVariable());
        this.createVariable(new InKeyVariable());
        this.createVariable(new SystemDateVariable());
        this.createVariable(new SystemTimeVariable());
        this.createVariable(new SystemTimerVariable());
    }

    /**
     * Creates a variable matching the criteria within the given variable reference
     *
     * @param reference the given {@link VariableReference variable reference}
     * @return a {@link Variable variable} matching the criteria within the given variable reference
     * @throws OutOfMemoryException
     */
    private Variable createVariable(final VariableReference reference)
            throws OutOfMemoryException {
        final MemoryObject valueObject = createObject(reference.getName());
        return new SimpleVariable(reference.getName(), valueObject);
    }

}
