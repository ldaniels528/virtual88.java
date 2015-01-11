# JavaPC

IBM PC emulator and GWBASIC interpreter implemented in Java (Java2D, Swing)

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
	$ java -jar javapc-0.431-jar-with-dependencies.jar
```

To execute the IBM PC/MS-DOS emulator:

```bash
	$ java -cp javapc-0.431-jar-with-dependencies.jar ibmpc.app.IbmPcEmulator
```

To execute the IBM PC/MS-DOS debugger:

```bash
	$ java -cp javapc-0.431-jar-with-dependencies.jar ibmpc.app.IbmPcDebugger
```

### Using the application(s)

One of the goals of this project is to satisfy three distinct use cases:
* Ability to run GWBASIC/BASICA programs via the JavaPC/BASIC emulator (**works**)
* Ability to run MS-DOS .com executables via the JavaPC/PC emulator (**wip**)
* Ability to execute hand written assembly code via the JavaPC/PC emulator (**works**)

The following is a working example of the third use case:

```scala

	final IbmPcDisplayFrame frame = new IbmPcDisplayFrame("Test PC");
	final IbmPcSystem system = new IbmPcSystemXT(frame);
	final Intel80x86 cpu = system.getCPU();
	final IbmPcRandomAccessMemory memory = system.getRandomAccessMemory();

	// insert a text string into memory
	final int segment = 0x13CF;
	final int offset = 0x2000;
	final String text = "Hello World$";
	memory.setBytes(segment, offset, text.getBytes(), text.length());

	// execute the 8086 instructions to print the text string
	system.execute(Arrays.asList(
			new MOV(cpu.CX, new WordValue(segment)),    // mov ax, segment
			new PUSH(cpu.CX),                           // push ax
			new POP(cpu.DS),                            // pop ds
			new MOV(cpu.DX, new WordValue(offset)),     // mov dx, offset
			new MOV(cpu.AH, new WordValue(0x09)),       // mov ah, 9h
			new INT(0x21)                               // int 21h
	));
```