package org.hansi_b.moss.explain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.hansi_b.moss.Cell;
import org.hansi_b.moss.Sudoku;
import org.hansi_b.moss.testSupport.VisibleForTesting;

class XyWingFinder {
	private final Sudoku sudoku;
	private final CachedCandidates cached;
	private final SortedSet<Cell> emptyCellsW2Cands;

	XyWingFinder(final Sudoku sudoku) {
		this.sudoku = sudoku;
		this.cached = new CachedCandidates();
		this.emptyCellsW2Cands = filterCandidates();
	}

	@VisibleForTesting
	SortedSet<Cell> filterCandidates() {
		return sudoku.streamEmptyCells().filter(c -> cached.candidates(c).size() == 2)
				.collect(Collectors.toCollection(() -> new TreeSet<>(Cell.positionComparator)));
	}

	List<WingTriple> findAllWings() {

		if (emptyCellsW2Cands.isEmpty())
			return Collections.emptyList();

		final SortedMap<Integer, SortedMap<Integer, SortedSet<Cell>>> candidatesMapping = filterAndMapCellsByCandidates();

		final List<WingTriple> wings = findWings(candidatesMapping);
		// wings.forEach(x -> System.out.println(x));
		return wings;
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
			final SortedSet<Integer> cands = cached.candidates(c);
			putCell(c, cands.first(), cands.last(), result);
			putCell(c, cands.last(), cands.first(), result);
		});
		return result;
	}

	private static void putCell(final Cell c, final Integer outer, final Integer inner,
			final SortedMap<Integer, SortedMap<Integer, SortedSet<Cell>>> result) {
		result.computeIfAbsent(outer, i -> new TreeMap<>())
				.computeIfAbsent(inner, i -> new TreeSet<>(Cell.positionComparator)).add(c);
	}

	/**
	 * Find candidate wings:
	 *
	 * <ol>
	 * <li>candidate values between cells overlap by one (that's guaranteed by the
	 * argument mapping)
	 * <li>the two cells have no common groups
	 * <li>the two cells share at least one empty candidate cell
	 * </ol>
	 */
	private List<WingTriple> findWings(
			final SortedMap<Integer, SortedMap<Integer, SortedSet<Cell>>> cellsByCandidates) {

		final List<WingTriple> wings = new ArrayList<>();
		cellsByCandidates.forEach((commonCand, innerMappings) -> {
			final Set<Integer> innerKeys = innerMappings.keySet();
			final Iterator<Integer> iter = innerKeys.iterator();
			Integer next = iter.next(); // we know we have at least one
			while (iter.hasNext()) {
				final Integer currentCand = next;
				next = iter.next();
				final SortedSet<Cell> currentCells = innerMappings.get(currentCand);
				for (final Entry<Integer, SortedSet<Cell>> entry : innerMappings.tailMap(next).entrySet())
					for (final Cell currCell : currentCells)
						for (final Cell nextCell : entry.getValue()) {

							if (currCell.sharesGroups(nextCell))
								continue;

							final Integer nextCand = entry.getKey();

							/*
							 * the middle cell for the xy-wing needs to have exactly these two candidate
							 * values and needs to share a group with each of the other cells
							 */
							final Set<Integer> requiredCands = Set.of(currentCand, nextCand);
							emptyCellsW2Cands.stream()
									.filter(x -> requiredCands.equals(cached.candidates(x)) && x.sharesGroups(currCell)
											&& x.sharesGroups(nextCell))
									.forEach(x -> wings.add(new WingTriple(x, commonCand, currCell, nextCell)));
						}
			}
		});
		return wings;
	}
}