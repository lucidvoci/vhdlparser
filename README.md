# README

`vhdlparser` is a simple console tool to generate annotations
(currently in PSL / Sugar format) for VHDL files.

## How To Run

~~~~~~~~~~
vhdlparser [ -f | -F ] source
    -h    this help
    -f    vhdl file source
    -F    folder with vhdl file source
~~~~~~~~~~

## Example

Input file:

~~~~~~~~~~
  1 TBD
  2 Line 2
  3 ...
~~~~~~~~~~

Generated annotations:

~~~~~~~~~~
TBD
~~~~~~~~~~

## List of Created Annotations
 * Correctness of the sensitivity list when process is clocked 
 * Completeness of the sensitivity list when process is not clocked 
 * Not writing into a single output signal from more than one process 
 * Process is not clocked by more than one signal 
 * The output signal is not being read from 
 * Correctness of the access to the vector 
 * Correctness of the access to the array
 
## Building From Source

Please use an included IntelliJ IDEA project. ANTLR4 library is necessary
for compilation.

## References
 * ...
 


