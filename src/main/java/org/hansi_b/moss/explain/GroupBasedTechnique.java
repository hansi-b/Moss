package org.hansi_b.moss.explain;

import java.util.List;
import java.util.stream.Collectors;

import org.hansi_b.moss.CellGroup;
import org.hansi_b.moss.Sudoku;

/**
 * A common pattern: A technique is based on a group, and accumulating the moves
 * over the Sudoku is a mere concatenation.
 */
abstract class GroupBasedTechnique implements Technique {

	@Override
	public List<Move> findMoves(final Sudoku sudoku, final PencilMarks cached) {

		return sudoku.streamGroups().flatMap(g -> findMoves(g, cached).stream()).collect(Collectors.toList());
	}

	public abstract List<Move> findMoves(CellGroup group, PencilMarks cached);
}