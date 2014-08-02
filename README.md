# JBasic

GWBASIC interpreter and IBM PC emulator

### Motivations

My first programming language was GWBASIC. As such, BASIC remains my first love; thus, I have fond memories of
programming in the little language that could, and quite a few old programs that I can no longer run because I've switched to Mac OS X.
I started JBasic in 2004 (as a hobby project) to be able to run those old programs on via the JVM. This codebase is
now defunct, as I'm not longer developing it (and haven't for almost 10 years), but I'm considering rekindling/rewritting the 
project in Scala.

### Build Requirements

* Java SDK 1.5+
* SBT 0.13.0+

### Building the code

    $ sbt clean package
      
### Running the tests

    $ sbt test    

### Run the application

	$ java -jar jbasic.jar