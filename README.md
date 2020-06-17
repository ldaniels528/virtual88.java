# JavaPC

IBM PC emulator and GWBASIC interpreter implemented in Java and Scala (Java2D, Swing)

### Motivations

My first programming language was GWBASIC. As such, BASIC remains my first love; thus, I have fond memories of
programming in the little language that could, and quite a few old programs that I can no longer run because I've 
switched to Mac OS X. I started JavaPC in 2004 (as a hobby project) to be able to run those old programs via the JVM.

I've recently decided to update this project to Java 8 (with a few components moved to Scala), and improve its
8086 compatibility and performance in the process.

### Build Requirements

* Java 8 SDK
* Scala 2.13.x
* SBT 1.3.10

### Building the code

    $ sbt clean assembly
      
### Running the tests

    $ sbt test

### Run the application(s)

To execute the GWBASIC/BASICA emulator:

```bash
	$ java -cp javapc-assembly-0.431.jar org.ldaniels528.javapc.jbasic.app.BasicEmulator
```

To execute the IBM PC/MS-DOS emulator:

```bash
	$ java -cp javapc-assembly-0.431.jar org.ldaniels528.javapc.ibmpc.app.IbmPcEmulator
```

To execute the IBM PC/MS-DOS debugger:

```bash
	$ java -cp javapc-assembly-0.431.jar com.ldaniels528.javapc.ibmpc.app.Debugger
```

### Using the application(s)

One of the goals of this project is to satisfy three distinct use cases:
* Ability to run GWBASIC/BASICA programs via the JavaPC/BASIC emulator (**works**)
* Ability to run MS-DOS .com executables via the JavaPC/PC emulator (**wip**)
* Ability to execute hand written assembly code via the JavaPC/PC emulator (**works**)

The following is a working example of the third use case:

```java

	final IbmPcDisplayFrame frame = new IbmPcDisplayFrame("Test PC");
	final IbmPcSystem system = new IbmPcSystemPCjr(frame);
	final I8086 cpu = system.getCPU();
	final IbmPcRandomAccessMemory memory = system.getRandomAccessMemory();

	// insert a text string into memory
	final int segment = 0x13CF;
	final int offset = 0x2000;
	final String text = "Hello World$";
	memory.setBytes(segment, offset, text.getBytes(), text.length());

	// execute the 8086 instructions to print the text string
	system.execute(asList(
			new MOV(cpu.CX, new WordValue(segment)),    // mov ax, segment
			new PUSH(cpu.CX),                           // push ax
			new POP(cpu.DS),                            // pop ds
			new MOV(cpu.DX, new WordValue(offset)),     // mov dx, offset
			new MOV(cpu.AH, new WordValue(0x09)),       // mov ah, 9h
			new INT(0x21)                               // int 21h
	));
```

#### Debugger

The Debugger enables users to introspect MS DOS .com binaries and step through code
instruction by instruction. As you can see below, it also provides the contents of
registers or FLAGS depending upon which instruction is being executed.

```
	- g
	[13F0:0100]         FA[1] CLI                          | FL = NV UP EI PL NZ NA PO NC DT
	[13F0:0101]     B80F68[3] MOV AX,680F                  | AX = 0000
	[13F0:0104]      51000[3] ADD AX,0010                  | AX = 680F
	[13F0:0107]       B104[2] MOV CL,04                    | CL = 00
	[13F0:0109]       D3E8[2] SHR AX,CL                    | CL = 04, AX = 681F
	[13F0:010B]       8CCB[2] MOV BX,CS                    | CS = 13F0, BX = 0000
	[13F0:010D]        3C3[2] ADD AX,BX                    | BX = 13F0, AX = 0681
	[13F0:010F]       8ED8[2] MOV DS,AX                    | AX = 1A71, DS = 13F0
	[13F0:0111]       8ED0[2] MOV SS,AX                    | AX = 1A71, SS = 13F0
	[13F0:0113] 268B1E0200[5] ES: MOV BX,[0002]            | ES = 13F0, [0002] = 0000, BX = 13F0
	[13F0:011C]       2BD8[2] SUB BX,AX                    | AX = 1A71, BX = 9ADD
	[13F0:011E]   F7C300F0[4] TEST BX,F000                 | BX = 7F94
```