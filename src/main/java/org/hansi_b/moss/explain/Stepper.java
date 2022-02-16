package org.hansi_b.moss.explain;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.hansi_b.moss.Sudoku;

public class Stepper implements Iterator<Move> {

	private final Technique[] techniques;

	private final Sudoku sudoku;
	private final PencilMarks pencilMarks;
	private Move move;

	public Stepper(Sudoku sudoku) {
		this(sudoku, Technique.allTechniques());
	}

	public Stepper(Sudoku orgSudoku, final Technique... techniques) {
		this.techniques = techniques;
		this.sudoku = orgSudoku.copy();
		this.pencilMarks = new PencilMarks();

		this.move = findNextMove(sudoku, pencilMarks);
	}

	private Move findNextMove(final Sudoku sudoku, final PencilMarks pencilMarks) {
		return Arrays.stream(techniques).flatMap(t -> t.findMoves(sudoku, pencilMarks)).findFirst()
				.orElseGet(() -> null);
	}

	/**
	 * @return a copy of the original Sudoku on which all moves up to (not
	 *         including) the next one
	 */
	public Sudoku getSudoku() {
		return sudoku;
	}

	public PencilMarks getPencilMarks() {
		return pencilMarks;
	}

	@Override
	public boolean hasNext() {
		return move != null;
	}

	@Override
	public Move next() {
		if (!hasNext())
			throw new NoSuchElementException();
		Move next = move;

		move.apply(pencilMarks);
		move = findNextMove(sudoku, pencilMarks);
		return next;
	}
}
