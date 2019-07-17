package org.hansi_b.moss;

import java.util.Iterator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class CellGroup implements Iterable<Cell> {

	public enum Type {

		Row(Type::rowPos), Col(Type::colPos), Block(Type::blockPos);

		private final BiFunction<Integer, Integer, Stream<Pos>> posStreamer;

		Type(final BiFunction<Integer, Integer, Stream<Pos>> posStreamer) {
			this.posStreamer = posStreamer;
		}

		/**
		 * @param groupIdx   the index of this group in the Sudoku; by definition, there
		 *                   should be - per group type - one group for each index
		 *                   between zero and the Sudoku size minus one
		 * @param sudokuSize the size of the Sudoku
		 * @return a Stream of the positions of this group's cells
		 */
		public Stream<Pos> getPos(final int groupIdx, final int sudokuSize) {
			return posStreamer.apply(groupIdx, sudokuSize);
		}

		private static Stream<Pos> rowPos(final int groupIdx, final int sudokuSize) {
			return IntStream.range(0, sudokuSize).mapToObj(colIdx -> Pos.at(groupIdx, colIdx));
		}

		private static Stream<Pos> colPos(final int groupIdx, final int sudokuSize) {
			return IntStream.range(0, sudokuSize).mapToObj(rowIdx -> Pos.at(rowIdx, groupIdx));
		}

		private static Stream<Pos> blockPos(final int groupIdx, final int sudokuSize) {
			final int sizeSqrt = (int) Math.sqrt(sudokuSize);
			// integer cutoff for the row offset:
			final int rowOffset = sizeSqrt * (groupIdx / sizeSqrt);
			final int colOffset = sizeSqrt * (groupIdx % sizeSqrt);

			return IntStream.range(0, sizeSqrt).boxed().flatMap( //
					rowIdx -> IntStream.range(0, sizeSqrt)
							.mapToObj(colIdx -> Pos.at(rowOffset + rowIdx, colOffset + colIdx)));
		}
	}

	static public class Row extends CellGroup {
		Row(final List<Cell> cells) {
			super(Type.Row, cells);
		}
	}

	static public class Col extends CellGroup {
		Col(final List<Cell> cells) {
			super(Type.Col, cells);
		}
	}

	static public class Block extends CellGroup {
		Block(final List<Cell> cells) {
			super(Type.Block, cells);
		}
	}

	private final CellGroup.Type type;
	private final List<Cell> cells;

	CellGroup(final CellGroup.Type type, final List<Cell> cells) {
		this.type = type;
		this.cells = cells;
	}

	public int size() {
		return cells.size();
	}

	public CellGroup.Type type() {
		return type;
	}

	public List<Integer> getValues() {
		return cells.stream().map(Cell::getValue).collect(Collectors.toList());
	}

	@Override
	public Iterator<Cell> iterator() {
		return cells.iterator();
	}

	@Override
	public String toString() {
		return String.format("%s%s", type, cells.toString());
	}
}