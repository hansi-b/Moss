package org.hansi_b.moss.explain;

import org.hansi_b.moss.Sudoku

import spock.lang.Ignore

class XyWingSpec extends spock.lang.Specification {

	XyWing technique = new XyWing()

	@Ignore
	def testXyWing() {

		when:
		final Integer[] values =
				[5, 2, 0, 0, 0, 0, 0, 4, 0]+
				[6, 3, 0, 4, 0, 0, 5, 0, 0]+
				[7, 1, 4, 6, 9, 5, 2, 3, 8]+
				[0, 5, 1, 0, 6, 0, 0, 0, 2]+
				[0, 8, 7, 1, 0, 0, 0, 5, 0]+
				[2, 4, 6, 5, 7, 9, 1, 8, 3]+
				[8, 6, 2, 7, 4, 3, 9, 1, 5]+
				[4, 9, 3, 2, 5, 1, 8, 6, 7]+
				[1, 7, 5, 9, 8, 6, 3, 2, 4]
		Sudoku su = Sudoku.filled(values);

		def actual = technique.findMoves(su) as Set

		then:
		actual == [] as Set
	}

	@Ignore
	def "xy-wing in 4x4"() {

		given:
		Integer[] values =
				[1, 0, 2, 4]+
				[0, 0, 0, 0]+
				[3, 0, 0, 0]+
				[0, 0, 0, 0]
		Sudoku su = Sudoku.filled(values)

		def actual = technique.findMoves(su) as Set

		expect:
		actual == [ ] as Set
	}
}
