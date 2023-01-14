package org.hansi_b.moss.explain.technique;

import java.util.stream.Stream

import org.hansi_b.moss.CellGroup
import org.hansi_b.moss.GroupType
import org.hansi_b.moss.Sudoku
import org.hansi_b.moss.explain.Insertion
import org.hansi_b.moss.explain.Move
import org.hansi_b.moss.explain.PencilMarks
import org.hansi_b.moss.explain.Strategy

import spock.lang.Specification

public class GroupBasedTechniqueSpec extends Specification {

	class SingleInsertion extends GroupBasedTechnique {

		SingleInsertion() {
			super(Strategy.NakedSingleInRow, Strategy.NakedSingleInCol, Strategy.NakedSingleInBlock);
		}

		@Override
		Stream<Move> findMoves(CellGroup group, Strategy strategy, PencilMarks cached) {
			return Stream.of(new Insertion(strategy, null, null));
		}
	}

	def"group types are mapped to corresponding strategies"() {

		given:
		CellGroup block = Mock()
		block.type() >> GroupType.Block
		CellGroup col = Mock()
		col.type() >> GroupType.Col
		CellGroup row = Mock()
		row.type() >> GroupType.Row

		Sudoku s = Mock()
		s.streamGroups() >> Stream.of(block, col, row)

		when:
		def mvs = new SingleInsertion().findMoves(s, null)

		then:
		mvs.collect { m -> m.strategy() } == [
			Strategy.NakedSingleInBlock,
			Strategy.NakedSingleInCol,
			Strategy.NakedSingleInRow
		]
	}
}
