package de.dhbw.caput.tinf21b1b6;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

final class LeadingZerosParserTest {

	private static final FiniteStateAutomaton NO_LEADING_ZEROS = FiniteStateAutomaton.from(RegularExpression
			.from( "0|(1|2|3|4|5|6|7|8|9)·(0|1|2|3|4|5|6|7|8|9)*" ));

	@ParameterizedTest
	@ValueSource( strings = {"0", "1", "12", "123"} )
	void accept_numbers_without_leading_zeros( String input ){
		assertTrue( NO_LEADING_ZEROS.accepts(input) );
	}

	@ParameterizedTest
	@ValueSource( strings = {"", "00", "01", "012", "0049"} )
	void reject_numbers_with_leading_zeros( String input ){
		assertFalse( NO_LEADING_ZEROS.accepts(input) );
	}

}
