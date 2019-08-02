package org.hansi_b.moss.explain;

import java.util.List;

import org.hansi_b.moss.Sudoku;

interface Technique {
	List<Move> findMoves(Sudoku sudoku);
}