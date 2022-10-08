package org.hansi_b.moss;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.hansib.sundries.Errors;

public class Sudoku implements Iterable<Cell> {

	private static class Factory {

		private static final int DEFAULT_SIZE = 9;

		private Sudoku sudoku;

		Sudoku empty() {
			return empty(DEFAULT_SIZE);
		}

		Sudoku copyOf(final Sudoku original) {

			final Sudoku copy = empty(original.size);
			for (int row = 0; row < original.size; row++)
				System.arraycopy(original.cellValues[row], 0, copy.cellValues[row], 0, original.size);
			return copy;
		}

		private static final Integer ZERO = Integer.valueOf(0);

		Sudoku filled(List<List<Integer>> values) {
			final int size = values.size();
			final Sudoku su = empty(size);
			for (int row = 0; row < size; row++)
				System.arraycopy(mapVals(values.get(row).stream()), 0, su.cellValues[row], 0, size);
			return su;
		}

		Sudoku filled(final Integer... values) {
			final int size = (int) Math.sqrt(values.length);

			final Sudoku su = empty(size);
			for (int row = 0; row < size; row++)
				System.arraycopy(mapVals(Arrays.stream(values)), row * size, su.cellValues[row], 0, size);
			return su;
		}

		private static Integer[] mapVals(Stream<Integer> vals) {
			return vals.map(v -> ZERO.equals(v) ? null : v).toArray(Integer[]::new);
		}

		Sudoku empty(final int size) {
			final double sqrt = Math.sqrt(size);
			final int sizeSqrt = (int) Math.floor(sqrt);
			if (sizeSqrt != sqrt)
				throw Errors.illegalArg("Sudoku cannot be initialised with a non-square size (got %d)", size);

			sudoku = new Sudoku(size);
			initCells();
			initGroups();
			return sudoku;
		}

		private void initCells() {
			IntStream.range(0, sudoku.size).forEach(rowIdx -> //
			IntStream.range(0, sudoku.size).forEach(colIdx -> //
			sudoku.cells[rowIdx][colIdx] = new Cell(sudoku, Pos.at(rowIdx, colIdx))));
		}

		private void initGroups() {
			for (GroupType groupType : GroupType.values()) {
				final List<CellGroup> groups = IntStream.range(0, sudoku.size)
						.mapToObj(idx -> initGroup(groupType, idx)).toList();
				sudoku.groupsByType.put(groupType, groups);
			}
		}

		private CellGroup initGroup(final GroupType cellGroupType, final int idx) {

			final List<Pos> posList = cellGroupType.getPos(idx, sudoku.size).toList();
			final List<Cell> cells = posList.stream().map(p -> sudoku.cells[p.row()][p.col()]).toList();

			final CellGroup group = new CellGroup(sudoku, cellGroupType, cells);
			for (final Pos pos : posList)
				sudoku.groups[pos.row()][pos.col()].put(group.type(), group);

			return group;
		}
	}

	private final int size;

	private final Cell[][] cells;
	private final EnumMap<GroupType, CellGroup>[][] groups;

	private final EnumMap<GroupType, List<CellGroup>> groupsByType;

	private final TreeSet<Integer> possibleValues;
	private final Integer[][] cellValues;

	private Sudoku(final int size) {
		this.size = size;

		this.cells = new Cell[size][size];
		this.groups = createGroupMaps(size);

		this.groupsByType = new EnumMap<>(GroupType.class);

		this.possibleValues = IntStream.range(1, size + 1).mapToObj(Integer::valueOf)
				.collect(Collectors.toCollection(TreeSet::new));
		this.cellValues = new Integer[size][size];
	}

	private static EnumMap<GroupType, CellGroup>[][] createGroupMaps(final int size) {
		@SuppressWarnings("unchecked")
		final EnumMap<GroupType, CellGroup>[][] cellGroups = new EnumMap[size][size];
		for (int rowIdx = 0; rowIdx < size; rowIdx++)
			for (int colIdx = 0; colIdx < size; colIdx++)
				cellGroups[rowIdx][colIdx] = new EnumMap<>(GroupType.class);
		return cellGroups;
	}

	/**
	 * @return an empty Sudoku of the standard 9 x 9 size
	 */
	public static Sudoku empty() {
		return new Factory().empty();
	}

	/**
	 * @param size the number of cells per row/column/block in the new Sudoku; must
	 *             be a square number
	 * @return a Sudoku square without any set values
	 */
	public static Sudoku empty(final int size) {
		return new Factory().empty(size);
	}

	/**
	 * @param values a flat array of integers containing the size × size values of
	 *               the Sudoku, where null or zero denote an empty cell
	 * @return a Sudoku filled with the argument numbers
	 */
	public static Sudoku filled(final Integer... values) {
		return new Factory().filled(values);
	}

	public static Sudoku filled(final List<List<Integer>> values) {
		return new Factory().filled(values);
	}

	/**
	 * @return an independent copy of the argument Sudoku
	 */
	public Sudoku copy() {
		return new Factory().copyOf(this);
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
	 * @param row      the 0-based row in the Sudoku (i.e., maximum is the Sudoku's
	 *                 size minus one)
	 * @param col      the 0-based column in the Sudoku (i.e., the maximum allowed
	 *                 value is the Sudoku's size minus one)
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
			throw Errors.illegalArg("Cell value must be null or between one and at most %d (is %d)", size, newValue);
		return newValue;
	}

	private void checkArg(final int arg, final String label) {
		if (arg < 0 || arg >= size)
			throw Errors.illegalArg("%s argument must not be negative and at most %d (is %d)", label, size - 1, arg);
	}

	/**
	 * @return a fresh copy of this Sudoku's possible values, i.e., just the
	 *         universe of all numbers any cell could take
	 */
	public SortedSet<Integer> possibleValues() {
		return new TreeSet<>(possibleValues);
	}

	public boolean isSolved() {
		for (final GroupType groupType : GroupType.values())
			if (streamGroups(groupType).anyMatch(g -> !g.isSolved()))
				return false;
		return true;
	}

	public Cell getCell(final Pos pos) {
		return cells[pos.row()][pos.col()];
	}

	public CellGroup getGroup(final GroupType groupType, final int groupIndex) {
		return groupsByType.get(groupType).get(groupIndex);
	}

	/**
	 * @return the cell group of the given type at the given position
	 */
	public CellGroup getGroup(final GroupType groupType, final Pos pos) {
		return groups[pos.row()][pos.col()].get(groupType);
	}

	public Stream<CellGroup> streamGroups(final Pos pos) {
		return Arrays.stream(GroupType.values()).map(groups[pos.row()][pos.col()]::get);
	}

	public Stream<CellGroup> streamGroups() {
		return Arrays.stream(GroupType.values()).map(groupsByType::get).flatMap(List::stream);
	}

	public Stream<CellGroup> streamGroups(final GroupType groupType) {
		return getGroups(groupType).stream();
	}

	public List<CellGroup> getGroups(final GroupType groupType) {
		return groupsByType.get(groupType);
	}

	public Stream<Cell> streamEmptyCells() {
		return Arrays.stream(cells).flatMap(Arrays::stream).filter(Cell::isEmpty);
	}

	@Override
	public Iterator<Cell> iterator() {
		return Arrays.stream(cells).flatMap(Arrays::stream).iterator();
	}

	@Override
	public String toString() {
		final StringBuilder cellValuesStr = new StringBuilder();
		for (final Integer[] row : cellValues)
			cellValuesStr.append(Arrays.toString(row));
		return String.format("Sudoku[%s]", cellValuesStr);
	}
}
