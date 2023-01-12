package org.hansi_b.moss.explain;

import java.util.Collection;
import java.util.Objects;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.hansi_b.moss.Cell;
import org.hansib.sundries.CollectUtils;

/**
 * A thin wrapper around a mapping from cells to sets of markings (e.g.,
 * candidates)
 */
public class Marks {

	private final SortedMap<Cell, SortedSet<Integer>> candsByCell;
	private final Function<? super Cell, ? extends SortedSet<Integer>> initialMarksFactory;

	public Marks() {
		this(k -> new TreeSet<>());
	}

	public Marks(Function<? super Cell, ? extends SortedSet<Integer>> initialMarksFactory) {
		this.candsByCell = Cell.newPosSortedMap();
		this.initialMarksFactory = initialMarksFactory;
	}

	public boolean add(final Cell cell, int candidates) {
		return targetSet(cell).add(candidates);
	}

	public boolean addAll(final Cell cell, final Collection<Integer> candidates) {
		if (candidates.isEmpty())
			return false;

		return targetSet(cell).addAll(candidates);
	}

	public SortedSet<Integer> get(final Cell cell) {
		return targetSet(cell);
	}

	private SortedSet<Integer> targetSet(final Cell cell) {
		return candsByCell.computeIfAbsent(cell, initialMarksFactory);
	}

	public boolean isEmpty() {
		return candsByCell.isEmpty();
	}

	public void forEach(BiConsumer<? super Cell, ? super SortedSet<Integer>> handler) {
		candsByCell.forEach(handler);
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof Marks cm && candsByCell.equals(cm.candsByCell);
	}

	@Override
	public int hashCode() {
		return Objects.hash(candsByCell);
	}

	@Override
	public String toString() {
		return String.join(", ", CollectUtils.mapMap(candsByCell, (k, v) -> String.format("%s - %s", k, v)).toList());
	}
}