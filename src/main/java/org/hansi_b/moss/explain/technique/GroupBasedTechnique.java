package org.hansi_b.moss.explain.technique;

import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Stream;

import org.hansi_b.moss.CellGroup;
import org.hansi_b.moss.GroupType;
import org.hansi_b.moss.Sudoku;
import org.hansi_b.moss.explain.Move;
import org.hansi_b.moss.explain.PencilMarks;
import org.hansi_b.moss.explain.Strategy;
import org.hansi_b.moss.explain.Technique;

/**
 * A common pattern: A technique is based on a group, and accumulating the moves
 * over the Sudoku is a mere concatenation.
 */
abstract class GroupBasedTechnique implements Technique {

	private final Map<GroupType, Strategy> strategyByGroupType;

	protected GroupBasedTechnique(final Strategy rowStrategy, Strategy colStrategy, Strategy blockStrategy) {
		strategyByGroupType = new EnumMap<>(GroupType.class);
		strategyByGroupType.put(GroupType.Row, rowStrategy);
		strategyByGroupType.put(GroupType.Col, colStrategy);
		strategyByGroupType.put(GroupType.Block, blockStrategy);
	}

	@Override
	public Stream<Move> findMoves(final Sudoku sudoku, final PencilMarks cached) {
		return sudoku.streamGroups().flatMap(group -> findMoves(group, strategyByGroupType.get(group.type()), cached));
	}

	abstract Stream<Move> findMoves(CellGroup group, Strategy strategy, PencilMarks cached);
}