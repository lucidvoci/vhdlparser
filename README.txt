README

vhdlparser is a simple console tool to generate PSL annotation for VHDL files.

=== To run ===
vhdlparser [-f-F] source
    -h    this help
    -f     vhdl file source
    -F    folder with vhdl file source

=== Creates annotation for: ===
 - Correctness of the sensitivity list when process is clocked  - Completeness of the sensitivity list when process is not clocked  - Not writing into a single output signal from more than one process  - Process is not clocked by more than one signal  - The output signal is not being read from  - Correctness of the access to the vector 
