package org.hansi_b.moss;

import static org.hansi_b.moss.testSupport.Shortcuts.cellAt

import spock.lang.Specification
import spock.lang.Unroll

public class CellGroupSpec extends Specification {

	def "equality is defined by object identity"() {

		when:
		Sudoku s1 = Sudoku.empty()
		Sudoku s2 = Sudoku.empty()

		then:
		cellAt(s1, 0, 1).getGroup(GroupType.Row) == cellAt(s1, 0, 2).getGroup(GroupType.Row)
		cellAt(s2, 0, 1).getGroup(GroupType.Row) != cellAt(s1, 0, 2).getGroup(GroupType.Row)
	}

	@Unroll
	def "values for row #groupIdx"() {

		given:
		Integer[] values =
				[1, 0, 4, 2]+
				[2, 4, 0, 1]+
				[3, 1, 2, 0]+
				[0, 2, 1, 3]
		Sudoku su = Sudoku.filled(values)

		expect:
		su.getGroup(GroupType.Row, groupIdx).values() == groupValues

		where:
		groupIdx | groupValues
		0 | [1, null, 4, 2]
		1 | [2, 4, null, 1]
		2 | [3, 1, 2, null]
		3 | [null, 2, 1, 3]
	}

	@Unroll
	def "values for column #groupIdx"() {

		given:
		Integer[] values =
				[1, 0, 4, 2]+
				[2, 4, 0, 1]+
				[3, 1, 2, 0]+
				[0, 2, 1, 3]
		Sudoku su = Sudoku.filled(values)

		expect:
		su.getGroup(GroupType.Col, groupIdx).values() == groupValues

		where:
		groupIdx | groupValues
		0 | [1, 2, 3, null]
		1 | [null, 4, 1, 2]
		2 | [4, null, 2, 1]
		3 | [2, 1, null, 3]
	}

	@Unroll
	def "values for block #groupIdx"() {

		given:
		Integer[] values =
				[1, 0, 4, 2]+
				[2, 4, 0, 1]+
				[3, 1, 2, 0]+
				[0, 2, 1, 3]
		Sudoku su = Sudoku.filled(values)

		expect:
		su.getGroup(GroupType.Block, groupIdx).values() == groupValues

		where:
		groupIdx | groupValues
		0 | [1, null, 2, 4]
		1 | [4, 2, null, 1]
		2 | [3, 1, null, 2]
		3 | [2, null, 1, 3]
	}

	@Unroll
	def "missing values for block #groupIdx"() {

		given:
		Integer[] values =
				[1, 0, 4, 2]+
				[2, 4, 0, 0]+
				[3, 0, 0, 0]+
				[0, 0, 0, 0]
		Sudoku su = Sudoku.filled(values);

		expect:
		su.getGroup(GroupType.Block, groupIdx).missing() == missingVals as Set

		where:
		groupIdx | missingVals
		0 | [3]
		1 | [1, 3]
		2 | [1, 2, 4]
		3 | [1, 2, 3, 4]
	}

	@Unroll
	def "special case for row #groupIdx"() {

		given:
		Integer[] values =
				[1, 1, 1, 1]+
				[0, 0, 0, 0]+
				[0, 0, 0, 0]+
				[0, 0, 0, 0]
		Sudoku su = Sudoku.filled(values);

		expect:
		su.getGroup(GroupType.Row, groupIdx).missing() == missingVals as Set

		where:
		groupIdx | missingVals
		0 | [2, 3, 4]
		1 | [1, 2, 3, 4]
	}
}
