library IEEE;
use IEEE.STD_LOGIC_1164.ALL;

entity mux is
port (
	c1 : in std_logic;
	c2 : in std_logic;
	sel : in std_logic;
	q : out std_logic
);
end mux;

architecture Behavioral of mux is

begin
process
	begin
	case sel is
		when '0' =>
			q <= q;
		when '1' =>
			q <= c2;
		when others =>
			q <= '0';
	end case;
end process;
end Behavioral;