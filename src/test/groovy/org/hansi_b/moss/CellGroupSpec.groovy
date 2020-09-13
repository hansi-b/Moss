package org.hansi_b.moss;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.list;
import static org.assertj.core.util.Sets.*;

import org.hansi_b.moss.CellGroup.Type;
import org.junit.jupiter.api.Test;

import spock.lang.Specification
import spock.lang.Unroll

public class CellGroupSpec extends Specification {

	@Unroll
	def "values for row #groupIdx"() {

		given:
		final Integer[] values =
				[1, 0, 4, 2]+
				[2, 4, 0, 1]+
				[3, 1, 2, 0]+
				[0, 2, 1, 3]
		final Sudoku su = Sudoku.filled(values)

		expect:
		su.getGroup(Type.Row, groupIdx).values() == groupValues

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
		final Integer[] values =
				[1, 0, 4, 2]+
				[2, 4, 0, 1]+
				[3, 1, 2, 0]+
				[0, 2, 1, 3]
		final Sudoku su = Sudoku.filled(values)

		expect:
		su.getGroup(Type.Col, groupIdx).values() == groupValues

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
		final Integer[] values =
				[1, 0, 4, 2]+
				[2, 4, 0, 1]+
				[3, 1, 2, 0]+
				[0, 2, 1, 3]
		final Sudoku su = Sudoku.filled(values)

		expect:
		su.getGroup(Type.Block, groupIdx).values() == groupValues

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
		final Integer[] values =
				[1, 0, 4, 2]+
				[2, 4, 0, 0]+
				[3, 0, 0, 0]+
				[0, 0, 0, 0]
		final Sudoku su = Sudoku.filled(values);

		expect:
		su.getGroup(Type.Block, groupIdx).missing() == missingVals as Set

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
		final Integer[] values =
				[1, 1, 1, 1]+
				[0, 0, 0, 0]+
				[0, 0, 0, 0]+
				[0, 0, 0, 0]
		final Sudoku su = Sudoku.filled(values);

		expect:
		su.getGroup(Type.Row, groupIdx).missing() == missingVals as Set

		where:
		groupIdx | missingVals
		0 | [2, 3, 4]
		1 | [1, 2, 3, 4]
	}
}
