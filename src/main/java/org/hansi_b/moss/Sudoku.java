package org.hansi_b.moss;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.hansi_b.moss.CellGroup.Block;
import org.hansi_b.moss.CellGroup.Col;
import org.hansi_b.moss.CellGroup.Row;
import org.hansi_b.moss.CellGroup.Type;

public class Sudoku implements Iterable<Cell> {

	public static class Factory {

		private static final int DEFAULT_SIZE = 9;

		private Sudoku sudoku;

		public Sudoku create() {
			return create(DEFAULT_SIZE);
		}

		/**
		 * @param values a flat array of integers containing the size × size values of
		 *               the Sudoku, where null or zero denote empty cells
		 * @return a Sudoku filled with the argument numbers
		 */
		public Sudoku create(final Integer... values) {
			final int size = Double.valueOf(Math.sqrt(values.length)).intValue();

			final Integer[] mappedVals = Arrays.stream(values).map(v -> {
				return v == null || v > 0 ? v : null;
			}).toArray(Integer[]::new);

			final Sudoku su = create(size);
			for (int row = 0; row < size; row++)
				System.arraycopy(mappedVals, row * size, su.cellValues[row], 0, size);
			return su;
		}

		public Sudoku create(final int size) {
			final double sqrt = Math.sqrt(size);
			final int sizeSqrt = (int) Math.floor(sqrt);
			if (sizeSqrt != sqrt)
				throw new IllegalArgumentException(
						String.format("Sudoku cannot be initialised with a non-square size (got %d)", size));

			sudoku = new Sudoku(size);
			initCells();
			initGroupType(Type.Row, Row::new);
			initGroupType(Type.Col, Col::new);
			initGroupType(Type.Block, Block::new);
			return sudoku;
		}

		private void initCells() {
			IntStream.range(0, sudoku.size).forEach(rowIdx -> {
				IntStream.range(0, sudoku.size).forEach(colIdx -> {
					sudoku.cells[rowIdx][colIdx] = new Cell(sudoku, Pos.at(rowIdx, colIdx));
				});
			});
		}

		private <T extends CellGroup> void initGroupType(final Type cellGroupType,
				final BiFunction<Sudoku, List<Cell>, T> newCall) {

			final List<CellGroup> groups = IntStream.range(0, sudoku.size)
					.mapToObj(idx -> initGroup(cellGroupType, idx, newCall)).collect(Collectors.toList());
			sudoku.groupsByType.put(cellGroupType, groups);
		}

		private <T extends CellGroup> T initGroup(final Type cellGroupType, final int idx,
				final BiFunction<Sudoku, List<Cell>, T> newCall) {

			final List<Pos> posList = cellGroupType.getPos(idx, sudoku.size).collect(Collectors.toList());
			final List<Cell> cells = posList.stream().map(p -> sudoku.cells[p.row][p.col]).collect(Collectors.toList());

			final T group = newCall.apply(sudoku, cells);
			for (final Pos pos : posList)
				sudoku.groups[pos.row][pos.col].put(group.type(), group);

			return group;
		}
	}

	private final int size;

	private final EnumMap<Type, List<CellGroup>> groupsByType;

	private final Cell[][] cells;
	private final EnumMap<CellGroup.Type, CellGroup>[][] groups;

	private final TreeSet<Integer> possibleValues;
	private final Integer[][] cellValues;

	private Sudoku(final int size) {
		this.size = size;

		this.groupsByType = new EnumMap<>(Type.class);

		this.cells = new Cell[size][size];
		this.groups = initCellGroups(size);

		this.possibleValues = IntStream.range(1, size + 1).mapToObj(Integer::new)
				.collect(Collectors.toCollection(TreeSet::new));
		this.cellValues = new Integer[size][size];
	}

	private static EnumMap<CellGroup.Type, CellGroup>[][] initCellGroups(final int size) {
		@SuppressWarnings("unchecked")
		final EnumMap<CellGroup.Type, CellGroup>[][] cellGroups = new EnumMap[size][size];
		for (int rowIdx = 0; rowIdx < size; rowIdx++)
			for (int colIdx = 0; colIdx < size; colIdx++)
				cellGroups[rowIdx][colIdx] = new EnumMap<CellGroup.Type, CellGroup>(CellGroup.Type.class);
		return cellGroups;
	}

	/**
	 * @return the number of cells per row, column, and block
	 */
	public int size() {
		return size;
	}

	/**
	 * @param row the 0-based row in the Sudoku (i.e., maximum is equal to the
	 *            Sudoku's size)
	 * @param col the 0-based column in the Sudoku (i.e., the maximum allowed value
	 *            is the Sudoku's size)
	 * @return either an Integer with the cell's value, i.e., a number between one
	 *         and the Sudoku's size; or null to indicate that the field is empty
	 */
	public Integer getValue(final int row, final int col) {
		checkArg(row, "Row");
		checkArg(col, "Column");
		return valueAt(row, col);
	}

	private Integer valueAt(final int row, final int col) {
		return cellValues[row][col];
	}

	/**
	 * @param row      the 0-based row in the Sudoku (i.e., maximum is equal to the
	 *                 Sudoku's size)
	 * @param col      the 0-based column in the Sudoku (i.e., the maximum allowed
	 *                 value is the Sudoku's size)
	 * @param newValue either a value between one and the Sudoku's size (inclusive);
	 *                 or null to indicate the field should be empty
	 */
	public void set(final int row, final int col, final Integer newValue) {
		checkArg(row, "Row");
		checkArg(col, "Column");
		checkValueArg(newValue);
		cellValues[row][col] = newValue;
	}

	private Integer checkValueArg(final Integer newValue) {
		if (newValue != null && (newValue < 1 || newValue > size))
			throw new IllegalArgumentException(
					String.format("Cell value must be null or between one and at most %d (is %d)", size, newValue));
		return newValue;
	}

	private void checkArg(final int arg, final String label) {
		if (arg < 0 || arg >= size)
			throw new IllegalArgumentException(
					String.format("%s argument must not be negative and at most %d (is %d)", label, size - 1, arg));
	}

	public SortedSet<Integer> possibleValues() {
		return new TreeSet<>(possibleValues);
	}

	public boolean isSolved() {
		for (final Type groupType : Type.values())
			for (final CellGroup g : iterateGroups(groupType))
				if (!g.isSolved())
					return false;
		return true;
	}

	public Cell getCell(final Pos pos) {
		return cells[pos.row][pos.col];
	}

	public CellGroup getGroup(final Type groupType, final int groupIndex) {
		return groupsByType.get(groupType).get(groupIndex);
	}

	/**
	 * @return the cell group of the given type at the given position
	 */
	public CellGroup getGroup(final Type groupType, final Pos pos) {
		return groups[pos.row][pos.col].get(groupType);
	}

	public List<CellGroup> getGroups(final Pos pos) {
		return Arrays.stream(Type.values()).map(groups[pos.row][pos.col]::get).collect(Collectors.toList());
	}

	public Iterable<CellGroup> iterateGroups() {
		return Arrays.stream(Type.values()).map(groupsByType::get).flatMap(List::stream)::iterator;
	}

	public Iterable<CellGroup> iterateGroups(final Type groupType) {
		return groupsByType.get(groupType);
	}

	@Override
	public Iterator<Cell> iterator() {
		return Arrays.stream(cells).flatMap(Arrays::stream).iterator();
	}
}
