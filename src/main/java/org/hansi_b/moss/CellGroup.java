package org.hansi_b.moss;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class CellGroup implements Iterable<Cell> {
	public enum Type {
		Row, Col, Block
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