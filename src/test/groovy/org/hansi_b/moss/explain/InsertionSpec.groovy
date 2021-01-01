package org.hansi_b.moss.explain;

import org.hansi_b.moss.Cell
import org.hansi_b.moss.Pos
import org.hansi_b.moss.Sudoku
import org.hansi_b.moss.explain.Move.Strategy

public class InsertionSpec extends spock.lang.Specification {

	Sudoku sudoku = Mock()
	Pos pos = new Pos(3,5)

	def cell = new Cell(sudoku, pos)

	def "strategy requires three arguments"() {

		when:
		Strategy.groupTypeMapper(Strategy.NakedSingleInRow)

		then:
		thrown IllegalArgumentException
	}

	def "moves are equal in all three positions"() {

		when:
		Insertion move = new Insertion(Strategy.HiddenSingleInBlock, cell, 2)

		then:
		move.equals(new Insertion(Strategy.HiddenSingleInBlock, cell, 2))
	}

	def "differing strategy makes moves unequal"() {

		when:
		Insertion move = new Insertion(Strategy.HiddenSingleInBlock, cell, 2)

		then:
		!move.equals(new Insertion(Strategy.NakedSingle, cell, 2))
	}

	def "differing cell makes moves unequal"() {

		when:
		Insertion move = new Insertion(Strategy.HiddenSingleInBlock, cell, 2)

		then:
		!move.equals(new Insertion(Strategy.HiddenSingleInBlock, new Cell(sudoku, pos), 2))
	}

	def "differing new value makes moves unequal"() {

		when:
		Insertion move = new Insertion(Strategy.HiddenSingleInBlock, cell, 2)

		then:
		!move.equals(new Insertion(Strategy.HiddenSingleInBlock, cell, 1))
	}

	def "applying move delegates to Cell/Sudoku"() {

		when:
		new Insertion(Strategy.HiddenSingleInBlock, cell, 2).apply()

		then:
		1 * sudoku.set(3, 5, 2)
	}
}
