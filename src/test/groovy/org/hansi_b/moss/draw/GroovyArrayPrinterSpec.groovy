package org.hansi_b.moss.draw;

import org.hansi_b.moss.Sudoku

class GroovyArrayPrinterSpec extends spock.lang.Specification {

	def "can draw 4x4 Sudoku"() {

		given:
		Integer[] values =
				[1, 0, 2, 4]+
				[0, 2, 0, 0]+
				[0, 0, 1, 0]+
				[0, 0, 0, 0];

		when:
		Sudoku su = Sudoku.filled(values);

		then:
		new GroovyArrayPrinter().draw(su) ==
				"""final Integer[] values =
	[1, 0, 2, 4]+
	[0, 2, 0, 0]+
	[0, 0, 1, 0]+
	[0, 0, 0, 0]
"""
	}
}
