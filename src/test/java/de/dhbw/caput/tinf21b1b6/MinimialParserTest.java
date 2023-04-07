package de.dhbw.caput.tinf21b1b6;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

final class MinimialParserTest {

	private static final FiniteStateAutomaton EVEN_PARITY = FiniteStateAutomaton.from(RegularExpression
			.from( "(0|1·0*·1)·(0|1·0*·1)*" ));

	@ParameterizedTest
	@ValueSource( strings = {"0", "00", "11", "000", "011", "101", "110"} )
	void accept_binaries_with_even_parity( String input ){
		assertTrue( EVEN_PARITY.accepts(input) );
	}

	@ParameterizedTest
	@ValueSource( strings = {"", "1", "01", "10", "001", "010", "100", "111"} )
	void reject_binaries_with_odd_parity( String input ){
		assertFalse( EVEN_PARITY.accepts(input) );
	}

}
