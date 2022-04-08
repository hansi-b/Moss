package org.hansi_b.moss.explain;

import static org.hansib.sundries.CollectUtils.flatten;
import static org.hansib.sundries.CollectUtils.mapMap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Stream;

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

	Stream<WingTriple> streamWings() {
		return emptyCellsW2Cands.isEmpty() ? Stream.empty() : collectWingsFromMapping(filterAndMapCellsByCandidates());
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
	private Stream<WingTriple> collectWingsFromMapping(
			final SortedMap<Integer, SortedMap<Integer, SortedSet<Cell>>> cellsByCandidates) {

		return mapMap(cellsByCandidates, (commonCand, innerMappings) -> {
			final List<WingTriple> wings = new ArrayList<>();
			final Iterator<Integer> innerKeys = innerMappings.keySet().iterator();
			Integer next = innerKeys.next(); // we know we have at least one
			while (innerKeys.hasNext()) {
				final Integer currentCand = next;
				next = innerKeys.next();
				final Stream<WingTriple> wingStream = collectWingsFromTail(innerMappings.get(currentCand),
						innerMappings.tailMap(next).entrySet(), currentCand, commonCand);
				wings.addAll(wingStream.toList());
			}
			return wings.stream();
		}).flatMap(Function.identity());
	}

	private Stream<WingTriple> collectWingsFromTail(final SortedSet<Cell> currentCells,
			final Set<Entry<Integer, SortedSet<Cell>>> nextCells, final Integer currentCand, final Integer commonCand) {
		return flatten(currentCells, currCell -> flatten(nextCells,
				entry -> flatten(entry.getValue().stream().filter(c -> !currCell.sharesAnyGroup(c)), nextCell -> {
					final Integer nextCand = entry.getKey();
					final Set<Integer> requiredCands = Set.of(currentCand, nextCand);

					return emptyCellsW2Cands.stream().filter(x -> isWing(currCell, nextCell, x, requiredCands))
							.map(x -> new WingTriple(x, commonCand, currCell, nextCell));
				})));
	}

	private boolean isWing(final Cell currCell, final Cell nextCell, final Cell x, final Set<Integer> requiredCands) {
		return x != currCell && x != nextCell //
				&& requiredCands.equals(marks.candidates(x)) //
				&& x.sharesAnyGroup(currCell) && x.sharesAnyGroup(nextCell);
	}
}