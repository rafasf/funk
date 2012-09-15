/*
 * Copyright (C) 2011-Present Funk committers.
 * All rights reserved.
 *
 * The software in this package is published under the terms of the BSD
 * style license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 */
package org.javafunk.funk;

import org.javafunk.funk.datastructures.tuples.*;
import org.junit.Test;

import java.util.Collection;
import java.util.Iterator;

import static org.hamcrest.Matchers.is;
import static org.javafunk.funk.Iterables.asList;
import static org.javafunk.funk.Iterables.materialize;
import static org.javafunk.funk.Literals.*;
import static org.javafunk.matchbox.Matchers.hasOnlyItemsInOrder;
import static org.junit.Assert.assertThat;

public class LazilyZipEnumerateTest {
    @Test
    public void shouldEnumerateSequence() {
        // Given
        Iterable<String> iterable = iterableWith("A", "B", "C");
        Collection<Pair<Integer, String>> expected = collectionWith(tuple(0, "A"), tuple(1, "B"), tuple(2, "C"));

        // When
        Iterable<Pair<Integer, String>> actual = Lazily.enumerate(iterable);

        // Then
        assertThat(asList(actual), hasOnlyItemsInOrder(expected));
    }

    @Test
    public void shouldReturnDistinctIteratorsEachTimeIteratorIsCalledOnTheReturnedEnumeratedIterable() throws Exception {
        // Given
        Iterable<String> input = iterableWith("A", "B", "C");

        // When
        Iterable<Pair<Integer, String>> iterable = Lazily.enumerate(input);
        Iterator<Pair<Integer, String>> iterator1 = iterable.iterator();
        Iterator<Pair<Integer, String>> iterator2 = iterable.iterator();

        // Then
        assertThat(iterator2.next(), is(tuple(0, "A")));
        assertThat(iterator2.next(), is(tuple(1, "B")));
        assertThat(iterator1.next(), is(tuple(0, "A")));
        assertThat(iterator2.next(), is(tuple(2, "C")));
        assertThat(iterator1.next(), is(tuple(1, "B")));
        assertThat(iterator1.next(), is(tuple(2, "C")));
    }

    @Test
    public void shouldZipTwoIterables() {
        // Given
        Iterable<String> iterable1 = iterableWith("A", "B", "C");
        Iterable<Integer> iterable2 = iterableWith(1, 2, 3);

        Collection<Pair<String, Integer>> expected = collectionWith(tuple("A", 1), tuple("B", 2), tuple("C", 3));

        // When
        Collection<Pair<String, Integer>> actual = asList(Lazily.zip(iterable1, iterable2));

        // Then
        assertThat(actual, hasOnlyItemsInOrder(expected));
    }

    @Test
    public void shouldZipTwoIterablesWithLongerFirstIterable() {
        // Given
        Iterable<String> iterable1 = iterableWith("A", "B", "C", "D");
        Iterable<Integer> iterable2 = iterableWith(1, 2, 3);
        Collection<Pair<String, Integer>> expected = collectionWith(tuple("A", 1), tuple("B", 2), tuple("C", 3));

        // When
        Collection<Pair<String, Integer>> actual = asList(Lazily.zip(iterable1, iterable2));

        // Then
        assertThat(actual, hasOnlyItemsInOrder(expected));
    }

    @Test
    public void shouldZipTwoIterablesWithShorterFirstIterable() {
        // Given
        Iterable<String> iterable1 = iterableWith("A", "B", "C");
        Iterable<Integer> iterable2 = iterableWith(1, 2, 3, 4);
        Collection<Pair<String, Integer>> expected = collectionWith(tuple("A", 1), tuple("B", 2), tuple("C", 3));

        // When
        Iterable<Pair<String, Integer>> actual = Lazily.zip(iterable1, iterable2);

        // Then
        assertThat(asList(actual), hasOnlyItemsInOrder(expected));
    }

    @Test
    public void shouldReturnDistinctIteratorsEachTimeIteratorIsCalledOnTheReturnedTwoZippedIterable() throws Exception {
        // Given
        Iterable<String> iterable1 = iterableWith("A", "B", "C");
        Iterable<Integer> iterable2 = iterableWith(1, 2, 3);

        // When
        Iterable<Pair<String, Integer>> outputIterable = Lazily.zip(iterable1, iterable2);
        Iterator<Pair<String, Integer>> firstIterator = outputIterable.iterator();
        Iterator<Pair<String, Integer>> secondIterator = outputIterable.iterator();

        // Then
        assertThat(secondIterator.next(), is(tuple("A", 1)));
        assertThat(secondIterator.next(), is(tuple("B", 2)));
        assertThat(firstIterator.next(), is(tuple("A", 1)));
        assertThat(secondIterator.next(), is(tuple("C", 3)));
        assertThat(firstIterator.next(), is(tuple("B", 2)));
    }

    @Test
    public void shouldZipThreeIterables() throws Exception {
        // Given
        Iterable<String> iterable1 = iterableWith("A", "B", "C");
        Iterable<Integer> iterable2 = iterableWith(1, 2, 3);
        Iterable<Boolean> iterable3 = iterableWith(true, false, true);

        Collection<Triple<String, Integer, Boolean>> expected = collectionWith(tuple("A", 1, true), tuple("B", 2, false), tuple("C", 3, true));

        // When
        Collection<Triple<String, Integer, Boolean>> actual = materialize(Lazily.zip(iterable1, iterable2, iterable3));

        // Then
        assertThat(actual, hasOnlyItemsInOrder(expected));
    }

    @Test
    public void shouldZipThreeIterablesToTheLengthOfTheShortestIterable() {
        // Given
        Iterable<String> iterable1 = iterableWith("A", "B", "C", "D");
        Iterable<Integer> iterable2 = iterableWith(1, 2, 3);
        Iterable<Boolean> iterable3 = iterableWith(true, false);
        Collection<Triple<String, Integer, Boolean>> expected = collectionWith(tuple("A", 1, true), tuple("B", 2, false));

        // When
        Collection<Triple<String, Integer, Boolean>> actual = asList(Lazily.zip(iterable1, iterable2, iterable3));

        // Then
        assertThat(actual, hasOnlyItemsInOrder(expected));
    }

    @Test
    public void shouldReturnDistinctIteratorsEachTimeIteratorIsCalledOnTheReturnedThreeZippedIterable() throws Exception {
        // Given
        Iterable<String> iterable1 = iterableWith("A", "B", "C");
        Iterable<Integer> iterable2 = iterableWith(1, 2, 3);
        Iterable<Boolean> iterable3 = iterableWith(true, false, true);

        // When
        Iterable<Triple<String, Integer, Boolean>> outputIterable = Lazily.zip(iterable1, iterable2, iterable3);
        Iterator<Triple<String, Integer, Boolean>> firstIterator = outputIterable.iterator();
        Iterator<Triple<String, Integer, Boolean>> secondIterator = outputIterable.iterator();

        // Then
        assertThat(secondIterator.next(), is(tuple("A", 1, true)));
        assertThat(secondIterator.next(), is(tuple("B", 2, false)));
        assertThat(firstIterator.next(), is(tuple("A", 1, true)));
        assertThat(secondIterator.next(), is(tuple("C", 3, true)));
        assertThat(firstIterator.next(), is(tuple("B", 2, false)));
    }

    @Test
    public void shouldZipFourIterables() throws Exception {
        // Given
        Iterable<String> iterable1 = iterableWith("A", "B", "C");
        Iterable<Integer> iterable2 = iterableWith(1, 2, 3);
        Iterable<Boolean> iterable3 = iterableWith(true, false, true);
        Iterable<Character> iterable4 = iterableWith('a', 'b', 'c');

        Collection<Quadruple<String, Integer, Boolean, Character>> expected = collectionWith(
                tuple("A", 1, true, 'a'),
                tuple("B", 2, false, 'b'),
                tuple("C", 3, true, 'c'));

        // When
        Collection<Quadruple<String, Integer, Boolean, Character>> actual =
                materialize(Lazily.zip(iterable1, iterable2, iterable3, iterable4));

        // Then
        assertThat(actual, hasOnlyItemsInOrder(expected));
    }

    @Test
    public void shouldZipFourIterablesToTheLengthOfTheShortestIterable() {
        // Given
        Iterable<String> iterable1 = iterableWith("A", "B", "C", "D");
        Iterable<Integer> iterable2 = iterableWith(1, 2, 3);
        Iterable<Boolean> iterable3 = iterableWith(true, false);
        Iterable<Character> iterable4 = iterableWith('a', 'b', 'c', 'd', 'e', 'f');
        Collection<Quadruple<String, Integer, Boolean, Character>> expected =
                collectionWith(tuple("A", 1, true, 'a'), tuple("B", 2, false, 'b'));

        // When
        Collection<Quadruple<String, Integer, Boolean, Character>> actual = asList(Lazily.zip(iterable1, iterable2, iterable3, iterable4));

        // Then
        assertThat(actual, hasOnlyItemsInOrder(expected));
    }

    @Test
    public void shouldReturnDistinctIteratorsEachTimeIteratorIsCalledOnTheReturnedFourZippedIterable() throws Exception {
        // Given
        Iterable<String> iterable1 = iterableWith("A", "B", "C");
        Iterable<Integer> iterable2 = iterableWith(1, 2, 3);
        Iterable<Boolean> iterable3 = iterableWith(true, false, true);
        Iterable<Character> iterable4 = iterableWith('a', 'b', 'c');

        // When
        Iterable<Quadruple<String, Integer, Boolean, Character>> outputIterable =
                Lazily.zip(iterable1, iterable2, iterable3, iterable4);
        Iterator<Quadruple<String, Integer, Boolean, Character>> firstIterator = outputIterable.iterator();
        Iterator<Quadruple<String, Integer, Boolean, Character>> secondIterator = outputIterable.iterator();

        // Then
        assertThat(secondIterator.next(), is(tuple("A", 1, true, 'a')));
        assertThat(secondIterator.next(), is(tuple("B", 2, false, 'b')));
        assertThat(firstIterator.next(), is(tuple("A", 1, true, 'a')));
        assertThat(secondIterator.next(), is(tuple("C", 3, true, 'c')));
        assertThat(firstIterator.next(), is(tuple("B", 2, false, 'b')));
    }

    @Test
    public void shouldZipFiveIterables() throws Exception {
        // Given
        Iterable<String> iterable1 = iterableWith("A", "B", "C");
        Iterable<Integer> iterable2 = iterableWith(1, 2, 3);
        Iterable<Boolean> iterable3 = iterableWith(true, false, true);
        Iterable<Character> iterable4 = iterableWith('a', 'b', 'c');
        Iterable<Double> iterable5 = iterableWith(1.2, 3.4, 5.6);

        Collection<Quintuple<String, Integer, Boolean, Character, Double>> expected = collectionWith(
                tuple("A", 1, true, 'a', 1.2),
                tuple("B", 2, false, 'b', 3.4),
                tuple("C", 3, true, 'c', 5.6));

        // When
        Collection<Quintuple<String, Integer, Boolean, Character, Double>> actual =
                materialize(Lazily.zip(iterable1, iterable2, iterable3, iterable4, iterable5));

        // Then
        assertThat(actual, hasOnlyItemsInOrder(expected));
    }

    @Test
    public void shouldZipFiveIterablesToTheLengthOfTheShortestIterable() {
        // Given
        Iterable<String> iterable1 = iterableWith("A", "B", "C", "D");
        Iterable<Integer> iterable2 = iterableWith(1, 2, 3);
        Iterable<Boolean> iterable3 = iterableWith(true, false);
        Iterable<Character> iterable4 = iterableWith('a', 'b', 'c', 'd', 'e', 'f');
        Iterable<Double> iterable5 = iterableWith(1.2, 3.4, 5.6);
        Collection<Quintuple<String, Integer, Boolean, Character, Double>> expected =
                collectionWith(
                        tuple("A", 1, true, 'a', 1.2),
                        tuple("B", 2, false, 'b', 3.4));

        // When
        Collection<Quintuple<String, Integer, Boolean, Character, Double>> actual =
                asList(Lazily.zip(iterable1, iterable2, iterable3, iterable4, iterable5));

        // Then
        assertThat(actual, hasOnlyItemsInOrder(expected));
    }

    @Test
    public void shouldReturnDistinctIteratorsEachTimeIteratorIsCalledOnTheReturnedFiveZippedIterable() throws Exception {
        // Given
        Iterable<String> iterable1 = iterableWith("A", "B", "C");
        Iterable<Integer> iterable2 = iterableWith(1, 2, 3);
        Iterable<Boolean> iterable3 = iterableWith(true, false, true);
        Iterable<Character> iterable4 = iterableWith('a', 'b', 'c');
        Iterable<Double> iterable5 = iterableWith(1.2, 3.4, 5.6);

        // When
        Iterable<Quintuple<String, Integer, Boolean, Character, Double>> outputIterable =
                Lazily.zip(iterable1, iterable2, iterable3, iterable4, iterable5);
        Iterator<Quintuple<String, Integer, Boolean, Character, Double>> firstIterator = outputIterable.iterator();
        Iterator<Quintuple<String, Integer, Boolean, Character, Double>> secondIterator = outputIterable.iterator();

        // Then
        assertThat(secondIterator.next(), is(tuple("A", 1, true, 'a', 1.2)));
        assertThat(secondIterator.next(), is(tuple("B", 2, false, 'b', 3.4)));
        assertThat(firstIterator.next(), is(tuple("A", 1, true, 'a', 1.2)));
        assertThat(secondIterator.next(), is(tuple("C", 3, true, 'c', 5.6)));
        assertThat(firstIterator.next(), is(tuple("B", 2, false, 'b', 3.4)));
    }

    @Test
    public void shouldZipSixIterables() throws Exception {
        // Given
        Iterable<String> iterable1 = iterableWith("A", "B", "C");
        Iterable<Integer> iterable2 = iterableWith(1, 2, 3);
        Iterable<Boolean> iterable3 = iterableWith(true, false, true);
        Iterable<Character> iterable4 = iterableWith('a', 'b', 'c');
        Iterable<Double> iterable5 = iterableWith(1.2, 3.4, 5.6);
        Iterable<Long> iterable6 = iterableWith(1L, 2L, 3L);

        Collection<Sextuple<String, Integer, Boolean, Character, Double, Long>> expected = collectionWith(
                tuple("A", 1, true, 'a', 1.2, 1L),
                tuple("B", 2, false, 'b', 3.4, 2L),
                tuple("C", 3, true, 'c', 5.6, 3L));

        // When
        Collection<Sextuple<String, Integer, Boolean, Character, Double, Long>> actual =
                materialize(Lazily.zip(iterable1, iterable2, iterable3, iterable4, iterable5, iterable6));

        // Then
        assertThat(actual, hasOnlyItemsInOrder(expected));
    }

    @Test
    public void shouldZipSixIterablesToTheLengthOfTheShortestIterable() {
        // Given
        Iterable<String> iterable1 = iterableWith("A", "B", "C", "D");
        Iterable<Integer> iterable2 = iterableWith(1, 2, 3);
        Iterable<Boolean> iterable3 = iterableWith(true, false);
        Iterable<Character> iterable4 = iterableWith('a', 'b', 'c', 'd', 'e', 'f');
        Iterable<Double> iterable5 = iterableWith(1.2, 3.4, 5.6);
        Iterable<Long> iterable6 = iterableWith(1L, 2L, 3L);
        Collection<Sextuple<String, Integer, Boolean, Character, Double, Long>> expected =
                collectionWith(
                        tuple("A", 1, true, 'a', 1.2, 1L),
                        tuple("B", 2, false, 'b', 3.4, 2L));

        // When
        Collection<Sextuple<String, Integer, Boolean, Character, Double, Long>> actual =
                asList(Lazily.zip(iterable1, iterable2, iterable3, iterable4, iterable5, iterable6));

        // Then
        assertThat(actual, hasOnlyItemsInOrder(expected));
    }

    @Test
    public void shouldReturnDistinctIteratorsEachTimeIteratorIsCalledOnTheReturnedSixZippedIterable() throws Exception {
        // Given
        Iterable<String> iterable1 = iterableWith("A", "B", "C");
        Iterable<Integer> iterable2 = iterableWith(1, 2, 3);
        Iterable<Boolean> iterable3 = iterableWith(true, false, true);
        Iterable<Character> iterable4 = iterableWith('a', 'b', 'c');
        Iterable<Double> iterable5 = iterableWith(1.2, 3.4, 5.6);
        Iterable<Long> iterable6 = iterableWith(1L, 2L, 3L);

        // When
        Iterable<Sextuple<String, Integer, Boolean, Character, Double, Long>> outputIterable =
                Lazily.zip(iterable1, iterable2, iterable3, iterable4, iterable5, iterable6);
        Iterator<Sextuple<String, Integer, Boolean, Character, Double, Long>> firstIterator = outputIterable.iterator();
        Iterator<Sextuple<String, Integer, Boolean, Character, Double, Long>> secondIterator = outputIterable.iterator();

        // Then
        assertThat(secondIterator.next(), is(tuple("A", 1, true, 'a', 1.2, 1L)));
        assertThat(secondIterator.next(), is(tuple("B", 2, false, 'b', 3.4, 2L)));
        assertThat(firstIterator.next(), is(tuple("A", 1, true, 'a', 1.2, 1L)));
        assertThat(secondIterator.next(), is(tuple("C", 3, true, 'c', 5.6, 3L)));
        assertThat(firstIterator.next(), is(tuple("B", 2, false, 'b', 3.4, 2L)));
    }
}
