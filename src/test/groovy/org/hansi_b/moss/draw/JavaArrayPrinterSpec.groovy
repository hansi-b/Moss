package org.hansi_b.moss.draw;

import org.hansi_b.moss.Sudoku

class JavaArrayPrinterSpec extends spock.lang.Specification {

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
		new JavaArrayPrinter().draw(su) ==
				"""final Integer[] values = { //
	1, 0, 2, 4, //
	0, 2, 0, 0, //
	//
	0, 0, 1, 0, //
	0, 0, 0, 0 //
};
"""
	}

	def "can draw 9x9 Sudoku"() {

		given:
		Integer[] values =
				[0, 0, 1, 0, 9, 0, 0, 6, 0]+ //
				[0, 0, 3, 0, 7, 0, 0, 0, 0]+ //
				[6, 2, 0, 0, 3, 0, 5, 0, 0]+ //
				//
				[0, 0, 0, 7, 0, 0, 6, 0, 0]+ //
				[4, 8, 6, 0, 0, 2, 7, 5, 0]+ //
				[0, 0, 0, 0, 6, 0, 0, 4, 1]+ //
				//
				[0, 0, 5, 6, 2, 0, 0, 0, 0]+ //
				[1, 0, 0, 0, 8, 5, 0, 0, 6]+ //
				[0, 0, 0, 0, 0, 7, 0, 8, 0] //
		when:
		Sudoku su = Sudoku.filled(values);

		then:
		new JavaArrayPrinter().draw(su) ==
				"""final Integer[] values = { //
	0, 0, 1, 0, 9, 0, 0, 6, 0, //
	0, 0, 3, 0, 7, 0, 0, 0, 0, //
	6, 2, 0, 0, 3, 0, 5, 0, 0, //
	//
	0, 0, 0, 7, 0, 0, 6, 0, 0, //
	4, 8, 6, 0, 0, 2, 7, 5, 0, //
	0, 0, 0, 0, 6, 0, 0, 4, 1, //
	//
	0, 0, 5, 6, 2, 0, 0, 0, 0, //
	1, 0, 0, 0, 8, 5, 0, 0, 6, //
	0, 0, 0, 0, 0, 7, 0, 8, 0 //
};
"""
	}
}
