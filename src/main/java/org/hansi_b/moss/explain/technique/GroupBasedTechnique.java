package org.hansi_b.moss.explain.technique;

import java.util.Arrays;
import java.util.function.Function;
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

	private final Function<GroupType, Strategy> groupTypeMapper;

	/**
	 * @param groupTypes three move strategies ordered Row, Column, Block
	 */
	protected GroupBasedTechnique(final Strategy... groupTypes) {
		this.groupTypeMapper = GroupBasedTechnique.groupTypeMapper(groupTypes);
	}

	static Function<GroupType, Strategy> groupTypeMapper(final Strategy... rowColBlockReturns) {
		if (rowColBlockReturns.length != 3)
			throw new IllegalArgumentException(String.format("Require three arguments to strategy mapping, got %s",
					Arrays.toString(rowColBlockReturns)));
		return (final GroupType type) -> {
			switch (type) {
			case Row:
				return rowColBlockReturns[0];
			case Col:
				return rowColBlockReturns[1];
			case Block:
				return rowColBlockReturns[2];
			default:
				throw new IllegalStateException(String.format("Unknown cell group type %s", type));
			}
		};
	}

	@Override
	public Stream<Move> findMoves(final Sudoku sudoku, final PencilMarks cached) {
		return sudoku.streamGroups().flatMap(group -> findMoves(group, groupTypeMapper.apply(group.type()), cached));
	}

	public abstract Stream<Move> findMoves(CellGroup group, Strategy strategy, PencilMarks cached);
}