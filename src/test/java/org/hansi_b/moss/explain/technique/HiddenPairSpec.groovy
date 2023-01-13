package org.hansi_b.moss.explain.technique;

import static org.hansi_b.moss.testsupport.Shortcuts.*

import org.hansi_b.moss.Sudoku
import org.hansi_b.moss.explain.Elimination
import org.hansi_b.moss.explain.PencilMarks
import org.hansi_b.moss.explain.Strategy

import spock.lang.Specification

public class HiddenPairSpec extends Specification {

	HiddenPair technique = new HiddenPair()

	def "find simple hidden pair of 1,9 in upper left block"() {

		when:
		final Integer[] values = //
				[7, 0, 0, 0, 0, 0, 0, 0, 0]+
				[0, 8, 4, 0, 0, 0, 0, 0, 0]+
				[0, 6, 0, 0, 0, 0, 0, 0, 0]+
				//
				[0, 0, 0, 0, 0, 0, 0, 0, 0]+
				[0, 0, 0, 0, 0, 0, 0, 0, 0]+
				[0, 0, 0, 0, 0, 0, 0, 0, 0]+
				//
				[0, 0, 0, 0, 0, 0, 0, 0, 0]+
				[0, 0, 0, 0, 0, 0, 0, 0, 0]+
				[0, 0, 0, 0, 0, 0, 0, 0, 0]

		Sudoku su = Sudoku.filled(values);
		def pm = new PencilMarks()
		marks(pm, cellAt(su, 0, 1), 2, 3)
		marks(pm, cellAt(su, 0, 2), 2, 3, 5)
		marks(pm, cellAt(su, 1, 0), 1, 2, 5, 9)
		marks(pm, cellAt(su, 2, 0), 1, 5, 9)
		marks(pm, cellAt(su, 2, 2), 3, 5)

		then:
		technique.findMoves(su, pm).toSet() == [
			new Elimination.Builder(Strategy.HiddenPairInBlock)
			.with(cellsAt(su, [1, 0]) as Set, [2, 5]).with(cellsAt(su, [2, 0]) as Set, 5).build()
		] as Set
	}

	def "find separate hidden pairs in same row"() {

		when:
		final Integer[] values = //
				[0, 0, 0, 0, 5, 6, 7, 0, 0]+
				[0, 0, 0, 0, 0, 0, 0, 0, 1]+
				[0, 0, 0, 0, 0, 0, 0, 0, 2]+
				//
				[0, 0, 0, 0, 0, 0, 0, 1, 3]+
				[0, 0, 0, 0, 0, 0, 0, 2, 4]+
				[0, 0, 0, 0, 0, 0, 0, 0, 0]+
				//
				[0, 0, 0, 0, 0, 0, 0, 3, 0]+
				[0, 0, 0, 0, 0, 0, 0, 4, 0]+
				[0, 0, 0, 0, 0, 0, 0, 0, 0]

		Sudoku su = Sudoku.filled(values);
		def pm = new PencilMarks()
		// 1+2 must be in first two cells
		marks(pm, cellAt(su, 0, 0), 1, 2, 9)
		marks(pm, cellAt(su, 0, 1), 1, 2, 8)
		// 3+4 must be in next two cells
		marks(pm, cellAt(su, 0, 2), 3, 4, 8)
		marks(pm, cellAt(su, 0, 3), 3, 4, 9)

		then:
		technique.findMoves(su, pm).toSet() == [
			new Elimination.Builder(Strategy.HiddenPairInRow)
			.with(cellsAt(su, [0, 0]) as Set, 9).with(cellsAt(su, [0, 1]) as Set, 8).build(),
			new Elimination.Builder(Strategy.HiddenPairInRow)
			.with(cellsAt(su, [0, 2]) as Set, 8).with(cellsAt(su, [0, 3]) as Set, 9).build(),
			// side effect:
			new Elimination.Builder(Strategy.HiddenPairInCol)
			.with(cellsAt(su, [1, 6], [2, 6]) as Set, [5, 6, 8, 9]).build(),
			new Elimination.Builder(Strategy.HiddenPairInBlock)
			.with(cellsAt(su, [1, 6], [2, 6]) as Set, [5, 6, 8, 9]).build(),
		] as Set
	}
}
