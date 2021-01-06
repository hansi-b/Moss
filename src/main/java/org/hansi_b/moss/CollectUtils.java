package org.hansi_b.moss;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class CollectUtils {

	/*
	 * inefficient & naively recursive combinations method - good enough for our
	 * purposes here
	 */
	public static <T> List<SortedSet<T>> combinations(final SortedSet<T> elements, final int count) {
		if (count > elements.size() || count < 1)
			return Collections.emptyList();

		if (count == elements.size()) {
			return Collections.singletonList(new TreeSet<>(elements));
		}

		if (count == 1) {
			return elements.stream().map(e -> new TreeSet<>(Set.of(e))).collect(Collectors.toList());
		}

		final List<SortedSet<T>> result = new ArrayList<>();
		for (final T e : elements) {
			final List<SortedSet<T>> subcombs = combinations(elements.headSet(e), count - 1);
			subcombs.forEach(s -> s.add(e));
			result.addAll(subcombs);
		}
		return result;
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
}
