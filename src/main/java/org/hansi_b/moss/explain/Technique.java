package org.hansi_b.moss.explain;

import java.util.List;

import org.hansi_b.moss.Sudoku;

interface Technique {
	List<Insertion> findMoves(Sudoku sudoku, PencilMarks marks);
}