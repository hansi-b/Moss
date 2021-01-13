package org.hansi_b.moss.explain;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.hansi_b.moss.CellGroup;
import org.hansi_b.moss.CellGroup.Type;
import org.hansi_b.moss.Sudoku;
import org.hansi_b.moss.explain.Move.Strategy;

/**
 * A common pattern: A technique is based on a group, and accumulating the moves
 * over the Sudoku is a mere concatenation.
 */
abstract class GroupBasedTechnique implements Technique {

	private final Function<Type, Strategy> groupTypeMapper;

	/**
	 * @param groupTypes three move strategies ordered Row, Column, Block
	 */
	protected GroupBasedTechnique(final Strategy... groupTypes) {
		this.groupTypeMapper = Strategy.groupTypeMapper(groupTypes);
	}

	@Override
	public List<Move> findMoves(final Sudoku sudoku, final PencilMarks cached) {
		return sudoku.streamGroups()
				.flatMap(group -> findMoves(group, groupTypeMapper.apply(group.type()), cached).stream())
				.collect(Collectors.toList());
	}

	public abstract List<Move> findMoves(CellGroup group, Strategy strategy, PencilMarks cached);
}