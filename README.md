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

Input file `memory.vhdl`:

~~~~~~~~~~
  1 library IEEE;
  2 use IEEE.STD_LOGIC_1164.ALL;
  3
  4 entity memory is
  5 port (
	 6     re : in std_logic;
  7     we : in std_logic;
  8     a : in std_logic_vector(7 downto 0);
  9     d : in std_logic_vector(7 downto 0);
	10     q : out std_logic_vector(7 downto 0)
 11 );
 12 end reg;
 13
 14 architecture Behavioral of memory is
 15 type ram_type is array (0 to 63, 128 to 255) of std_logic_vector(7 downto 0);
 16 signal ram : ram_type;
 17 begin
 18
 19 process (we, re, a)
	20 begin
	21 if we = '1' then
 22     ram(to_integer(unsigned(a))) <= d;
	23 end if;
 24
	25 if re = '1' then
	26     q <= ram(to_integer(unsigned(a)));
	27 end if;
 28 end process;
 29
 30 end Behavioral;
~~~~~~~~~~

Generated annotations:

~~~~~~~~~~
memory.vhdl:22: -- psl always ((to_integer(unsigned(a))) >= 0) and ((to_integer(unsigned(a))) <= 63)) or ((to_integer(unsigned(a))) >= 128) and ((to_integer(unsigned(a))) <= 255))
memory.vhdl:22: -- psl always false // 'd' controls output yet it is not in sensitivity list
memory.vhdl:26: -- psl always ((to_integer(unsigned(a))) >= 0) and ((to_integer(unsigned(a))) <= 63)) or ((to_integer(unsigned(a))) >= 128) and ((to_integer(unsigned(a))) <= 255))
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
 


