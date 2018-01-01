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


```vhdl
library IEEE;
use IEEE.STD_LOGIC_1164.ALL;

entity memory is
port (
    re : in std_logic;
    we : in std_logic;
    a : in std_logic_vector(7 downto 0);
    d : in std_logic_vector(7 downto 0);
    q : out std_logic_vector(7 downto 0)
);
end memory;

architecture Behavioral of memory is
type ram_type is array (0 to 63, 128 to 255) of std_logic_vector(7 downto 0);
signal ram : ram_type;
begin

process (we, re, a)
    begin
    if we = '1' then
        ram(to_integer(unsigned(a))) <= d;
    end if;

    if re = '1' then
        q <= ram(to_integer(unsigned(a)));
    end if;
end process;

end Behavioral;
```


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
 


