package org.hansi_b.moss.explain;

import java.util.Objects;

import org.hansi_b.moss.Cell;

public class Move {

	enum Strategy {
		SoleCandidateInRow, //
		SoleCandidateInCol, //
		SoleCandidateInBlock, //
		SoleCandidate, //
	}

	private final Strategy strategy;
	private final Cell cell;
	private final Integer newValue;

	Move(final Strategy strategy, final Cell cell, final Integer newValue) {
		this.strategy = strategy;
		this.cell = cell;
		this.newValue = newValue;
	}

	public Strategy getStrategy() {
		return strategy;
	}

	public Cell getCell() {
		return cell;
	}

	public Integer getNewValue() {
		return newValue;
	}

	@Override
	public boolean equals(final Object obj) {
		if (!(obj instanceof Move))
			return false;
		final Move m = (Move) obj;
		return strategy == m.strategy && cell == m.cell && newValue == m.newValue;
	}

	@Override
	public int hashCode() {
		return Objects.hash(strategy, cell, newValue);
	}

	@Override
	public String toString() {
		return String.format("%s: %s <- %d", strategy, cell, newValue);
	}
}
