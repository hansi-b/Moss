package org.hansi_b.moss.explain;

import org.hansi_b.moss.Cell
import org.hansi_b.moss.Pos
import org.hansi_b.moss.Sudoku
import org.hansi_b.moss.explain.Move.Strategy
import org.junit.Before

import spock.lang.Specification

public class MoveSpec extends Specification {

	Sudoku sudoku = Mock()
	Pos pos = Mock()

	def cell = new Cell(sudoku, pos)

	def "moves are equal in all three positions"() {

		when:
		Move move = new Move(Strategy.HiddenSingleInBlock, cell, 2)

		then:
		move.equals(new Move(Strategy.HiddenSingleInBlock, cell, 2))
	}
	
	def "differing strategy makes moves unequal"() {

		when:
		Move move = new Move(Strategy.HiddenSingleInBlock, cell, 2)

		then:
		!move.equals(new Move(Strategy.NakedSingle, cell, 2))
	}

	def "differing cell makes moves unequal"() {

		when:
		Move move = new Move(Strategy.HiddenSingleInBlock, cell, 2)

		then:
		!move.equals(new Move(Strategy.HiddenSingleInBlock, new Cell(sudoku, pos), 2))
	}
	
	def "differing new value makes moves unequal"() {

		when:
		Move move = new Move(Strategy.HiddenSingleInBlock, cell, 2)

		then:
		!move.equals(new Move(Strategy.HiddenSingleInBlock, cell, 1))
	}
}
