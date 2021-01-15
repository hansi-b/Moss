package org.hansi_b.moss.explain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;

import org.hansi_b.moss.Cell;
import org.hansi_b.moss.Sudoku;
import org.hansi_b.moss.testSupport.VisibleForTesting;

class XyWingFinder {
	private final Sudoku sudoku;
	private final PencilMarks marks;
	private final SortedSet<Cell> emptyCellsW2Cands;

	XyWingFinder(final Sudoku sudoku, final PencilMarks marks) {
		this.sudoku = sudoku;
		this.marks = marks;
		this.emptyCellsW2Cands = filterCandidates();
	}

	@VisibleForTesting
	SortedSet<Cell> filterCandidates() {
		return Cell.collect(sudoku.streamEmptyCells().filter(c -> marks.candidates(c).size() == 2));
	}

	List<WingTriple> findAllWings() {

		return emptyCellsW2Cands.isEmpty() ? Collections.emptyList()
				: collectWingsFromMapping(filterAndMapCellsByCandidates());
	}

	/**
	 * create the search mapping of empty two-candidate cells:
	 *
	 * outer: one candidate -> inner: { other candidate -> sorted{cells} }
	 *
	 * Note: By definition of the mapping
	 * <ol>
	 * <li>each two different inner mappings of the same outer mapping share exactly
	 * one candidate (i.e., the outer key)
	 * <li>each cell appears in two inner mappings of two different outer mappings
	 * </ol>
	 *
	 * Using Sorted{Map|Tree}s for deterministic search behaviour.
	 *
	 */
	@VisibleForTesting
	SortedMap<Integer, SortedMap<Integer, SortedSet<Cell>>> filterAndMapCellsByCandidates() {

		final SortedMap<Integer, SortedMap<Integer, SortedSet<Cell>>> result = new TreeMap<>();

		emptyCellsW2Cands.forEach(c -> {
			final SortedSet<Integer> cands = marks.candidates(c);
			putCell(c, cands.first(), cands.last(), result);
			putCell(c, cands.last(), cands.first(), result);
		});
		return result;
	}

	private static void putCell(final Cell c, final Integer outer, final Integer inner,
			final SortedMap<Integer, SortedMap<Integer, SortedSet<Cell>>> result) {
		result.computeIfAbsent(outer, i -> new TreeMap<>()).computeIfAbsent(inner, i -> Cell.newPosSortedSet()).add(c);
	}

	/**
	 * Find wings from our candidate cells A - X - B (where each has two candidates)
	 * which satisfy these requirements:
	 *
	 * <ol>
	 * <li>candidate values between cells A+B overlap by one (that's guaranteed by
	 * the argument mapping)
	 * <li>the two cells A+B have no common group
	 * <li>the cell X
	 * <ol>
	 * <li>has exactly the two candidate values not shared by A+B
	 * <li>shares a group with each of A+B
	 * </ol>
	 * </ol>
	 */
	private List<WingTriple> collectWingsFromMapping(
			final SortedMap<Integer, SortedMap<Integer, SortedSet<Cell>>> cellsByCandidates) {

		final List<WingTriple> wings = new ArrayList<>();
		cellsByCandidates.forEach((commonCand, innerMappings) -> {
			final Set<Integer> innerKeys = innerMappings.keySet();
			final Iterator<Integer> iter = innerKeys.iterator();
			Integer next = iter.next(); // we know we have at least one
			while (iter.hasNext()) {
				final Integer currentCand = next;
				next = iter.next();
				collectWingsFromTail(innerMappings.get(currentCand), innerMappings.tailMap(next).entrySet(),
						currentCand, commonCand, wings);
			}
		});
		return wings;
	}

	private void collectWingsFromTail(final SortedSet<Cell> currentCells,
			final Set<Entry<Integer, SortedSet<Cell>>> nextCells, final Integer currentCand, final Integer commonCand,
			final List<WingTriple> wings) {
		for (final Entry<Integer, SortedSet<Cell>> entry : nextCells)
			for (final Cell currCell : currentCells)
				for (final Cell nextCell : entry.getValue()) {

					if (currCell.sharesAnyGroup(nextCell))
						continue;

					final Integer nextCand = entry.getKey();
					final Set<Integer> requiredCands = Set.of(currentCand, nextCand);

					emptyCellsW2Cands.stream().filter(x -> isWing(currCell, nextCell, x, requiredCands))
							.forEach(x -> wings.add(new WingTriple(x, commonCand, currCell, nextCell)));
				}
	}

	private boolean isWing(final Cell currCell, final Cell nextCell, final Cell x, final Set<Integer> requiredCands) {
		return x != currCell && x != nextCell //
				&& requiredCands.equals(marks.candidates(x)) //
				&& x.sharesAnyGroup(currCell) && x.sharesAnyGroup(nextCell);
	}
}