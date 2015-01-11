# JBasic

GWBASIC interpreter and IBM PC emulator

### Motivations

My first programming language was GWBASIC. As such, BASIC remains my first love; thus, I have fond memories of
programming in the little language that could, and quite a few old programs that I can no longer run because I've 
switched to Mac OS X. I started JBasic in 2004 (as a hobby project) to be able to run those old programs on via the JVM. 

I've recently decided to update this project to Java 8, and improve its x86 compatibility and performance in the process.

### Build Requirements

* Java 8 SDK 
* Apache Maven 3.0+

### Building the code

    $ mvn clean package
      
### Running the tests

    $ mvn test    

### Run the application(s)

To execute the GWBASIC/BASICA emulator:

```bash
	$ java -jar jbasic-0.431-jar-with-dependencies.jar
```

To execute the IBM PC/MS-DOS emulator:

```bash
	$ java -cp jbasic-0.431-jar-with-dependencies.jar ibmpc.app.IbmPcEmulator
```

To execute the IBM PC/MS-DOS debugger:

```bash
	$ java -cp jbasic-0.431-jar-with-dependencies.jar ibmpc.app.IbmPcDebugger
```

### Using the application(s)

One of the goals of this project is to satisfy three distinct use cases:
* Ability to run GWBASIC/BASICA programs in the JBasic emulator (**works**)
* Ability to run MS-DOS .com executables in the JBasic/PC emulator (**wip**)
* Ability to execute hand written assembly code in the JBasic/PC emulator (**works**)

The following is a working example of the third use case:

```scala

	final IbmPcDisplayFrame frame = new IbmPcDisplayFrame("Test PC");
	final IbmPcSystem system = new IbmPcSystemXT(frame);
	final Intel80x86 cpu = system.getCPU();
	final IbmPcRandomAccessMemory memory = system.getRandomAccessMemory();

	// place a text string to print
	final int segment = 0x13CF;
	final int offset = 0x2000;
	final String text = "Hello World$";
	memory.setBytes(segment, offset, text.getBytes(), text.length());

	// create the instructions to execute
	final List<OpCode> opCodes = Arrays.asList(
			new MOV(cpu.CX, new WordValue(segment)),    // mov ax, segment
			new PUSH(cpu.CX),                           // push ax
			new POP(cpu.DS),                            // pop ds
			new MOV(cpu.DX, new WordValue(offset)),     // mov dx, offset
			new MOV(cpu.AH, new WordValue(0x09)),       // mov ah, 9h
			new INT(0x21)                               // int 21h
	);

	for (final OpCode opCode : opCodes) {
		cpu.execute(system, opCode);
	}

	logger.info("The registers should match the expected values:");
	logger.info(format("CX should be %04X", segment));
	assertEquals(cpu.CX.get(), segment);
	logger.info(format("DS should be %04X", segment));
	assertEquals(cpu.DS.get(), segment);
	logger.info(format("DX should be %04X", offset));
	assertEquals(cpu.DX.get(), offset);
	logger.info(format("AH should be %04X", 0x9));
	assertEquals(cpu.AH.get(), 0x9);
```