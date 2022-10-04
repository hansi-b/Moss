package org.hansi_b.moss.explain;

import static org.hansi_b.moss.testsupport.Shortcuts.*

import org.hansi_b.moss.Sudoku
import org.hansi_b.moss.explain.Move.Strategy

import spock.lang.Shared
import spock.lang.Specification

public class EliminationSpec extends Specification {

	@Shared su = Sudoku.empty()
	@Shared c13 = cellAt(su, 1, 3)
	@Shared c22 = cellAt(su, 2, 2)

	def "equality does not depend on build sequence"() {

		expect:
		eli1 == eli2

		where:
		eli1 << [
			new Elimination.Builder(Strategy.NakedPairInCol).with(c13, 7).with([c13, c22], [3]).build()
		]
		eli2 << [
			new Elimination.Builder(Strategy.NakedPairInCol).with(c13, 3).with(c22, 3).with(c13, 7).build()
		]
	}

	def "toString groups and sorts"() {

		expect:
		new Elimination.Builder(Strategy.NakedPairInCol).with(c22, 3).with(c13, 7).with(c13, 3).build().toString() ==
				'Eliminate: Cell@(1, 3) - [3, 7], Cell@(2, 2) - [3] (NakedPairInCol)'
	}
}
