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