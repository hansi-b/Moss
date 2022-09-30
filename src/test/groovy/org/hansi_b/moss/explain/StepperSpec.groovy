package org.hansi_b.moss.explain;

import java.util.stream.Stream

import org.hansi_b.moss.Sudoku
import org.hansi_b.moss.explain.Move.Strategy

import spock.lang.Specification

public class StepperSpec extends Specification {

	Technique tec = Mock()
	Sudoku su = Mock()

	def "can find next move"() {

		when:
		Move move = new Elimination(Strategy.NakedSingleInRow, Collections.emptySortedMap())

		tec.findMoves(_, _) >>> [
			Stream.of(move),
			Stream.empty()
		]

		def stepper = new Stepper(su, tec)

		then:
		def found = stepper.hasNext()
		move == stepper.next()
		!stepper.hasNext()
	}

	def "throws exception on no next element"() {

		given:
		tec.findMoves(_, _) >> Stream.empty()
		def stepper = new Stepper(su, tec)

		when:
		stepper.next()

		then:
		thrown NoSuchElementException
	}
}
