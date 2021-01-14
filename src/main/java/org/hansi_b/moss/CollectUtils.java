package org.hansi_b.moss;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class CollectUtils {

	/**
	 * Defines a comparator over SortedSets of Comparable elements: Order is defined
	 * by pairwise comparison of the elements (i.e., compare the first two elements,
	 * then the next two, etc.) according to the iteration order of the respective
	 * sets. If one set is a "prefix" of the other, it is considered smaller.
	 */
	private static final class SortedSetComparator<E> implements Comparator<SortedSet<E>> {

		private final Comparator<E> elementComparator;

		public SortedSetComparator(final Comparator<E> elementComparator) {
			this.elementComparator = elementComparator;
		}

		@Override
		public int compare(final SortedSet<E> s1, final SortedSet<E> s2) {

			if (s1 == s2)
				return 0;

			final Iterator<E> myIter = s1.iterator();
			final Iterator<E> otherIter = s2.iterator();

			while (myIter.hasNext()) {
				if (!otherIter.hasNext())
					return 1;
				final int comp = elementComparator.compare(myIter.next(), otherIter.next());
				if (comp != 0)
					return comp;
			}
			return otherIter.hasNext() ? -1 : 0;
		}
	}

	/**
	 * A not necessarily efficient method to get combinations from a SortedSet -
	 * good enough for our purposes here.
	 *
	 * @return a List of all distinct combinations of count elements
	 */
	public static <T> Stream<SortedSet<T>> combinations(final SortedSet<T> elements, final int count) {
		if (count > elements.size() || count < 1)
			return Stream.empty();

		if (count == elements.size()) {
			final SortedSet<T> res = new TreeSet<>(elements.comparator());
			res.addAll(elements);
			return Stream.of(new TreeSet<>(elements));
		}

		if (count == 1) {
			return elements.stream().map(e -> {
				final SortedSet<T> res = new TreeSet<>(elements.comparator());
				res.add(e);
				return res;
			});
		}

		return elements.stream().flatMap(e -> combinations(elements.headSet(e), count - 1).map(s -> {
			s.add(e);
			return s;
		}));
	}

	/**
	 * Generate all distinct ascending pairs up the argument bound; returns them as
	 * pair arrays in a list
	 *
	 * @param max the exclusive upper bound
	 * @return a list of int arrays, each containing two elements
	 */
	public static List<int[]> pairCombinations(final int max) {
		return IntStream.range(0, max - 1).mapToObj(i -> IntStream.range(i + 1, max).mapToObj(j -> new int[] { i, j }))
				.flatMap(i -> i).collect(Collectors.toList());
	}

	@SuppressWarnings("unchecked")
	public static <T> Stream<T[]> pairCombinations(final List<T> elements) {
		return IntStream.range(0, elements.size() - 1).mapToObj(i -> IntStream.range(i + 1, elements.size())
				.mapToObj(j -> genericArray(elements.get(i), elements.get(j)))).flatMap(i -> i);
	}

	/**
	 * This seems to work, but it's black magic. See
	 * https://stackoverflow.com/a/530289/1016514
	 */
	@SuppressWarnings("unchecked")
	private static <T> T[] genericArray(final T... es) {
		final T[] newInstance = (T[]) Array.newInstance(es[0].getClass(), es.length);
		System.arraycopy(es, 0, newInstance, 0, es.length);
		return newInstance;
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

	/**
	 * @return a copy of the 'one' set without the elements from the other
	 */
	public static <T> SortedSet<T> difference(final SortedSet<T> one, final Collection<T> other) {
		final TreeSet<T> base = new TreeSet<>(one);
		base.removeAll(other);
		return base;
	}

	public static <E> Comparator<SortedSet<E>> sortedSetComparator(final Comparator<E> comparator) {
		return new SortedSetComparator<>(comparator);
	}

	public static <E extends Comparable<E>> Comparator<SortedSet<E>> sortedSetComparator() {
		return new SortedSetComparator<>((e1, e2) -> e1.compareTo(e2));
	}

	/**
	 * A convenience method for streaming a sorted map & collecting it into a list.
	 *
	 * @param <K>          the type of the keys in the map
	 * @param <V>          the type of the values in the map
	 * @param <R>          the type of elements in the resulting list
	 * @param map          the map to map to a list - sorted so that
	 * @param keyValueFunc the transformation to call on the map
	 * @return a list resulting from mapping the function on the argument map
	 */
	public static <K, V, R> List<R> mapSortedMapToList(final SortedMap<K, V> map,
			final BiFunction<? super K, ? super V, ? extends R> keyValueFunc) {
		return map.entrySet().stream().map(e -> keyValueFunc.apply(e.getKey(), e.getValue()))
				.collect(Collectors.toList());
	}
}
