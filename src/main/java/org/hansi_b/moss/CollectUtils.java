package org.hansi_b.moss;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CollectUtils {

	/**
	 * @return a list of all integer combinations up to (max-1)
	 */
	public static List<Integer[]> getPairCombinations(final int max) {
		final List<Integer[]> indices = new ArrayList<>();
		for (int x = 0; x < max - 1; x++)
			for (int y = x + 1; y < max; y++)
				indices.add(new Integer[] { x, y });
		return indices;
	}

	/**
	 * Provides the intersection set of the argument collections: Copies a Set from
	 * the first argument, retains only elements which are present in all following
	 * arguments.
	 *
	 * @return the intersection of all argument collections
	 */
	@SafeVarargs
	public static <T> Set<T> intersection(final Collection<T>... collections) {

		if (collections.length == 0)
			return Collections.emptySet();
		final Set<T> result = new HashSet<>(collections[0]);
		for (int i = 1; i < collections.length; i++) {
			result.retainAll(collections[i]);
			if (result.isEmpty())
				return Collections.emptySet();
		}
		return result;
	}

	/**
	 * Provides the union set of the argument collection, i.e., all elements which
	 * are present in any argument collection.
	 *
	 * @return the union set of the argument collections
	 */
	@SafeVarargs
	public static <T> Set<T> union(final Collection<T>... collections) {
		if (collections.length == 0)
			return Collections.emptySet();

		return Arrays.stream(collections).flatMap(Collection::stream).collect(Collectors.toSet());
	}
}
