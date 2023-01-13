package org.hansi_b.moss.explain.technique;

import java.util.function.Function;
import java.util.stream.Stream;

import org.hansi_b.moss.CellGroup;
import org.hansi_b.moss.GroupType;
import org.hansi_b.moss.Sudoku;
import org.hansi_b.moss.explain.Move;
import org.hansi_b.moss.explain.Move.Strategy;
import org.hansi_b.moss.explain.PencilMarks;

/**
 * A common pattern: A technique is based on a group, and accumulating the moves
 * over the Sudoku is a mere concatenation.
 */
abstract class GroupBasedTechnique implements Technique {

	private final Function<GroupType, Strategy> groupTypeMapper;

	/**
	 * @param groupTypes three move strategies ordered Row, Column, Block
	 */
	protected GroupBasedTechnique(final Strategy... groupTypes) {
		this.groupTypeMapper = Strategy.groupTypeMapper(groupTypes);
	}

	@Override
	public Stream<Move> findMoves(final Sudoku sudoku, final PencilMarks cached) {
		return sudoku.streamGroups().flatMap(group -> findMoves(group, groupTypeMapper.apply(group.type()), cached));
	}

	public abstract Stream<Move> findMoves(CellGroup group, Strategy strategy, PencilMarks cached);
}