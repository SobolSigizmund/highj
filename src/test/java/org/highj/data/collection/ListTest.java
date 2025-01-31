package org.highj.data.collection;

import org.derive4j.hkt.__;
import org.highj.data.collection.list.ListMonadPlus;
import org.highj.data.collection.list.ListTraversable;
import org.highj.data.collection.list.ZipApplicative;
import org.highj.data.functions.Integers;
import org.highj.data.functions.Strings;
import org.highj.data.tuple.T2;
import org.highj.data.tuple.T3;
import org.highj.data.tuple.T4;
import org.highj.typeclass0.group.Group;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

public class ListTest {

    @Rule
    public ExpectedException shouldThrow = ExpectedException.none();

    @Test
    public void testAppend() {
        assertThat(List.append(List.of(2, 5), List.of(3, 8, 9))).containsExactly(2, 5, 3, 8, 9);
        assertThat(List.append(List.of(2, 5), List.of())).containsExactly(2, 5);
        assertThat(List.append(List.of(), List.of(3, 8, 9))).containsExactly(3, 8, 9);
        assertThat(List.append(List.of(), List.of())).isEmpty();
        //ensure laziness
        assertThat(List.append(List.of(3), List.cycle(1,2))).startsWith(3,1,2,1,2,1);
        assertThat(List.append(List.cycle(1,2), List.of(3))).startsWith(1,2,1,2,1,2);
    }

    @Test
    public void testContains() {
        List<Integer> list = List.of(2, 8, 8, 4, 6, -2, 10);
        assertThat(list.contains(2)).isTrue();
        assertThat(list.contains(4)).isTrue();
        assertThat(list.contains(10)).isTrue();
        assertThat(list.contains(5)).isFalse();
        assertThat(list.contains(Integers.negative)).isTrue();
        assertThat(list.contains(Integers.odd)).isFalse();
    }

    @Test
    public void testContravariant() {
        List<Integer> intList = List.of(1, 2, 3);
        List<Number> numList = List.contravariant(intList);
        assertThat(numList).containsExactly(1, 2, 3);
        assertThat(numList.get(1)).isInstanceOf(Integer.class);
    }

    @Test
    public void testCount() {
        List<Integer> list = List.of(2, 3, 3, 2, 3, 4, 3, 3);
        assertThat(list.count(3)).isEqualTo(5);
        assertThat(list.count(Integers.even)).isEqualTo(3);

        List<Integer> emptyList = List.empty();
        assertThat(emptyList.count(3)).isEqualTo(0);
        assertThat(emptyList.count(Integers.even)).isEqualTo(0);
    }

    @Test
    public void testCycle() {
        assertThat(List.cycle(1, 2, 3).take(9)).containsExactly(1, 2, 3, 1, 2, 3, 1, 2, 3);
        assertThat(List.cycle(42).take(3)).containsExactly(42, 42, 42);
        shouldThrow.expect(NoSuchElementException.class);
        List.cycle();
    }

    @Test
    public void testDrop() {
        assertThat(List.of(1, 2, 3, 4).drop(10)).isEmpty();
        assertThat(List.of(1, 2, 3, 4).drop(4)).isEmpty();
        assertThat(List.of(1, 2, 3, 4).drop(3)).containsExactly(4);
        assertThat(List.of(1, 2, 3, 4).drop(1)).containsExactly(2, 3, 4);
        assertThat(List.of(1, 2, 3, 4).drop(0)).containsExactly(1, 2, 3, 4);
        assertThat(List.of(1, 2, 3, 4).drop(-10)).containsExactly(1, 2, 3, 4);

        assertThat(List.of().drop(-10)).isEmpty();
        assertThat(List.of().drop(0)).isEmpty();
        assertThat(List.of().drop(10)).isEmpty();
    }

    @Test
    public void testDropWhile() {
        assertThat(List.of(2, 4, 6).dropWhile(Integers.even)).isEmpty();
        assertThat(List.of(2, 4, 6, 7, 8, 10).dropWhile(Integers.even)).containsExactly(7, 8, 10);
        assertThat(List.of(7, 8, 10).dropWhile(Integers.even)).containsExactly(7, 8, 10);

        assertThat(List.<Integer>of().dropWhile(Integers.even)).isEmpty();
    }

    @Test
    public void testEmpty() {
        List<Integer> empty = List.empty();
        assertThat(empty).isEmpty();
    }

    @Test
    public void testFilter() {
        List<Integer> list = List.of(2, 5, 3, 8);
        assertThat(list.filter(Integers.even)).containsExactly(2, 8);
        assertThat(list.filter(Integers.negative)).isEmpty();
        assertThat(list.filter(Integers.positive)).containsExactly(2, 5, 3, 8);

        assertThat(List.<Integer>empty().filter(Integers.even)).isEmpty();
    }

    @Test
    public void testFoldl() {
        assertThat(List.of(1, 2, 3).foldl("xy", Strings.repeat)).isEqualTo("xyxyxyxyxyxy");
        assertThat(List.of(1, 2, 3, 4).foldl(10, x -> y -> x - y)).isEqualTo(0);
    }

    @Test
    public void testFoldr() {
        assertThat(List.of(1, 2, 3).foldr(a -> b -> Strings.repeat.apply(b).apply(a), "xy")).isEqualTo("xyxyxyxyxyxy");
        assertThat(List.of(1, 2, 3, 4, 5).foldr(x -> y -> y - x, 15)).isEqualTo(0);
    }

    @Test
    public void testFromStream() {
        assertThat(List.fromStream(Stream.range(1)).take(4)).containsExactly(1, 2, 3, 4);
    }

    @Test
    public void testGet() {
        List<Integer> list = List.of(10, 20, 30, 40);
        assertThat(list.get(0)).isEqualTo(10);
        assertThat(list.get(3)).isEqualTo(40);
    }

    @Test
    public void testGet_exceedingIndex() {
        List<Integer> list = List.of(10, 20, 30, 40);
        shouldThrow.expect(IndexOutOfBoundsException.class);
        list.get(4);
    }

    @Test
    public void testGet_negativeIndex() {
        List<Integer> list = List.of(10, 20, 30, 40);
        shouldThrow.expect(IndexOutOfBoundsException.class);
        list.get(-1);
    }

    @Test
    public void testHead() {
        assertThat(List.of(42).head()).isEqualTo(42);
        assertThat(List.of(42, 5, 7).head()).isEqualTo(42);
        shouldThrow.expect(NoSuchElementException.class);
        List.of().head();
    }

    @Test
    public void testIntersperse() {
        assertThat(List.of(1, 3, 5, 7).intersperse(42)).containsExactly(1, 42, 3, 42, 5, 42, 7);
        //test laziness
        assertThat(List.range(1).intersperse(42).take(5)).containsExactly(1, 42, 2, 42, 3);
    }

    @Test
    public void testIsEmpty() {
        assertThat(List.of().isEmpty()).isTrue();
        assertThat(List.of(1, 2, 4).isEmpty()).isFalse();
    }

    @Test
    public void testIterator() {
        StringBuilder sb = new StringBuilder();
        for (int i : List.of(7, 2, 5, 9)) {
            sb.append(i);
        }
        assertThat(sb.toString()).isEqualTo("7259");
    }

    @Test
    public void testJoin() {
        List<List<Integer>> list = List.of(List.of(1, 2), List.of(), List.of(30, 40, 50), List.of(600));
        assertThat(List.join(list)).containsExactly(1, 2, 30, 40, 50, 600);

        List<List<Integer>> emptyList = List.empty();
        assertThat(List.join(emptyList)).isEmpty();
    }

    @Test
    public void testMap() {
        List<Integer> list = List.of(2, 5, 3, 8);
        assertThat(list.map(Integers.even::test)).containsExactly(true, false, false, true);

        assertThat(List.<Integer>empty().map(Integers.even::test)).isEmpty();
    }

    @Test
    public void testMaybeHead() {
        assertThat(List.of().maybeHead().isNothing()).isTrue();
        assertThat(List.of(42).maybeHead().get()).isEqualTo(42);
        assertThat(List.of(42, 5, 7).maybeHead().get()).isEqualTo(42);
    }

    @Test
    public void testMaybeTail() {
        assertThat(List.of().maybeTail().isNothing()).isTrue();
        assertThat(List.of(42).maybeTail().get().isEmpty());
        assertThat(List.of(42, 5, 7).maybeTail().get()).containsExactly(5, 7);
    }

    @Test
    public void testMinus() {
        assertThat(List.<Integer>of().minus(1)).isEmpty();
        assertThat(List.of(1).minus(1)).isEmpty();
        assertThat(List.of(4, 1, 2, 3).minus(4)).containsExactly(1, 2, 3);
        assertThat(List.of(1, 2, 4, 3).minus(4)).containsExactly(1, 2, 3);
        assertThat(List.of(1, 2, 3, 4).minus(4)).containsExactly(1, 2, 3);
        assertThat(List.of(1, 4, 2, 3, 4).minus(4)).containsExactly(1, 2, 3, 4);
        assertThat(List.range(1).minus(4).take(4)).containsExactly(1, 2, 3, 5);
    }

    @Test
    public void testMinusAll() {
        assertThat(List.<Integer>of().minusAll(1)).isEmpty();
        assertThat(List.of(1).minusAll(1)).isEmpty();
        assertThat(List.of(1, 1, 1, 1, 1).minusAll(1)).isEmpty();
        assertThat(List.of(4, 1, 2, 3).minusAll(4)).containsExactly(1, 2, 3);
        assertThat(List.of(1, 2, 4, 3).minusAll(4)).containsExactly(1, 2, 3);
        assertThat(List.of(1, 2, 3, 4).minusAll(4)).containsExactly(1, 2, 3);
        assertThat(List.of(4, 1, 4, 2, 4, 4, 4, 3, 4).minusAll(4)).containsExactly(1, 2, 3);
        assertThat(List.of(4, 1, 4, 2, 4, 4, 4, 3, 4).minusAll(4, 1)).containsExactly(2, 3);
        assertThat(List.range(1).minusAll(3, 5).take(4)).containsExactly(1, 2, 4, 6);
    }

    @Test
    public void testMonadPlus() {
        ListMonadPlus monadPlus = List.monadPlus;

        __<List.µ, Integer> list1 = monadPlus.map(String::length, List.of("ab", "c", "def"));
        assertThat(List.narrow(list1)).containsExactly(2, 1, 3);

        __<List.µ, String> list2 = monadPlus.bind(List.of(2, 0, 4), i -> List.replicate(i, "xy"));
        assertThat(List.narrow(list2)).containsExactly("xy", "xy", "xy", "xy", "xy", "xy");

        __<List.µ, __<List.µ, Integer>> list3 = List.of(List.of(1, 2), List.of(), List.of(30, 40, 50), List.of(600));
        assertThat(List.narrow(monadPlus.join(list3))).containsExactly(1, 2, 30, 40, 50, 600);

        List<String> list4 = monadPlus.mplus(List.of("a", "b"), List.of("c", "d", "e"));
        assertThat(list4).containsExactly("a", "b", "c", "d", "e");

        assertThat(monadPlus.mzero()).isEmpty();

        Function<String, __<List.µ, Either<String, Character>>> substrings = s -> {
            if (s.length() == 1) {
                return List.of(Either.newRight(s.charAt(0)));
            }
            List<String> result = List.empty();
            for (int lower = 0; lower < s.length(); lower++) {
                for (int higher = lower + 1; higher <= s.length(); higher++) {
                    String t = s.substring(lower, higher);
                    if (!t.equals(s)) {
                        result = result.plus(s.substring(lower, higher));
                    }
                }
            }
            return result.reverse().map(Either::newLeft);
        };

        List<Character> list5 = monadPlus.tailRec(substrings, "abc");
        assertThat(list5).containsExactly('a', 'a', 'b', 'b', 'b', 'c', 'c');
    }

    @Test
    public void testNarrow() {
        __<List.µ, Integer> list_ = List.of(1, 2, 3);
        List<Integer> list = List.narrow(list_);
        assertThat(list).containsExactly(1, 2, 3);
    }

    @Test
    public void testNil() {
        List<Integer> empty = List.nil();
        assertThat(empty).isEmpty();
    }

    @Test
    public void testOf() {
        assertThat(List.of()).isEmpty();
        assertThat(List.of(1, 2, 3)).containsExactly(1, 2, 3);
        assertThat(List.of(new int[]{1, 2, 3})).containsExactly(1, 2, 3);
    }

    @Test
    public void testPlus() {
        assertThat(List.of().plus(1)).containsExactly(1);
        assertThat(List.of(1, 2, 3).plus(4)).containsExactly(4, 1, 2, 3);
    }

    @Test
    public void testRange() {
        assertThat(List.range(1).take(3)).containsExactly(1, 2, 3);
        assertThat(List.range(20, 10).take(3)).containsExactly(20, 30, 40);
        assertThat(List.range(20, 10, 40)).containsExactly(20, 30, 40);
        assertThat(List.range(20, 10, 43)).containsExactly(20, 30, 40);
        assertThat(List.range(20, -2, 14)).containsExactly(20, 18, 16, 14);
        assertThat(List.range(20, -2, 13)).containsExactly(20, 18, 16, 14);
    }

    @Test
    public void testReplicate() {
        assertThat(List.replicate(-5, 2)).isEmpty();
        assertThat(List.replicate(0, 2)).isEmpty();
        assertThat(List.replicate(5, 2)).containsExactly(2, 2, 2, 2, 2);
    }

    @Test
    public void testReverse() {
        assertThat(List.of().reverse()).isEmpty();
        assertThat(List.of(1, 2, 3).reverse()).containsExactly(3, 2, 1);
    }

    @Test
    public void testSize() {
        assertThat(List.of().size()).isEqualTo(0);
        assertThat(List.of(1).size()).isEqualTo(1);
        assertThat(List.of(1, 2, 3, 4, 5).size()).isEqualTo(5);
    }

    @Test
    public void testSort() {
        assertThat(List.<Integer>of().sort(Comparator.naturalOrder())).isEmpty();
        assertThat(List.of(5,7,3,1,3,2).sort(Comparator.naturalOrder())).containsExactly(1,2,3,3,5,7);
    }

    @Test
    public void testTail() {
        assertThat(List.of(42).tail()).isEmpty();
        assertThat(List.of(42, 5, 7).tail()).containsExactly(5, 7);
        shouldThrow.expect(NoSuchElementException.class);
        List.of().tail();
    }

    @Test
    public void testTake() {
        List<Integer> infiniteList = List.range(1);
        assertThat(infiniteList.take(-5)).isEmpty();
        assertThat(infiniteList.take(0)).isEmpty();
        assertThat(infiniteList.take(1)).containsExactly(1);
        assertThat(infiniteList.take(5)).containsExactly(1, 2, 3, 4, 5);

        List<Integer> finiteList = List.of(1, 2, 3, 4);
        assertThat(finiteList.take(-5)).isEmpty();
        assertThat(finiteList.take(0)).isEmpty();
        assertThat(finiteList.take(1)).containsExactly(1);
        assertThat(finiteList.take(4)).containsExactly(1, 2, 3, 4);
        assertThat(finiteList.take(5)).containsExactly(1, 2, 3, 4);
    }

    @Test
    public void testTakeWhile() {
        assertThat(List.of(2, 4, 6).takeWhile(Integers.even)).containsExactly(2, 4, 6);
        assertThat(List.of(2, 4, 6, 7, 8, 10).takeWhile(Integers.even)).containsExactly(2, 4, 6);
        assertThat(List.of(2, 4, 6, 7, 8, 10).takeWhile(Integers.odd)).isEmpty();

        assertThat(List.<Integer>of().takeWhile(Integers.odd)).isEmpty();
    }


    @Test
    public void testToJList() {
        java.util.List<Integer> jList = List.of(1, 2, 3, 4, 5).toJList();
        assertThat(jList).containsExactly(1, 2, 3, 4, 5);

        java.util.List<Integer> emptyJList = List.<Integer>of().toJList();
        assertThat(emptyJList).isEmpty();
    }


    @Test
    public void testToString() {
        assertThat(List.of().toString()).isEqualTo("List()");
        assertThat(List.of(2, 5, 3, 8).toString()).isEqualTo("List(2,5,3,8)");
    }

    @Test
    public void testUnzip() {
        List<T2<Integer, String>> list = List.of(T2.of(1, "one"), T2.of(5, "five"), T2.of(7, "seven"), T2.of(3, "three"));
        T2<List<Integer>, List<String>> t2 = List.unzip(list);
        assertThat(t2._1()).containsExactly(1, 5, 7, 3);
        assertThat(t2._2()).containsExactly("one", "five", "seven", "three");

        List<T2<Integer, String>> emptyList = List.of();
        T2<List<Integer>, List<String>> emptyT2 = List.unzip(emptyList);
        assertThat(emptyT2._1()).isEmpty();
        assertThat(emptyT2._2()).isEmpty();
    }

    @Test
    public void testUnzip3() {
        List<T3<Integer, String, Boolean>> list = List.of(T3.of(1, "one", true), T3.of(5, "five", false), T3.of(7, "seven", true), T3.of(3, "three", true));
        T3<List<Integer>, List<String>, List<Boolean>> t3 = List.unzip3(list);
        assertThat(t3._1()).containsExactly(1, 5, 7, 3);
        assertThat(t3._2()).containsExactly("one", "five", "seven", "three");
        assertThat(t3._3()).containsExactly(true, false, true, true);

        List<T3<Integer, String, Boolean>> emptyList = List.of();
        T3<List<Integer>, List<String>, List<Boolean>> emptyT3 = List.unzip3(emptyList);
        assertThat(emptyT3._1()).isEmpty();
        assertThat(emptyT3._2()).isEmpty();
        assertThat(emptyT3._3()).isEmpty();
    }

    @Test
    public void testUnzip4() {
        List<T4<Integer, String, Boolean, Long>> list = List.of(
                T4.of(1, "one", true, 100L),
                T4.of(5, "five", false, 200L),
                T4.of(7, "seven", true, 300L),
                T4.of(3, "three", true, 400L));
        T4<List<Integer>, List<String>, List<Boolean>, List<Long>> t4 = List.unzip4(list);
        assertThat(t4._1()).containsExactly(1, 5, 7, 3);
        assertThat(t4._2()).containsExactly("one", "five", "seven", "three");
        assertThat(t4._3()).containsExactly(true, false, true, true);
        assertThat(t4._4()).containsExactly(100L, 200L, 300L, 400L);

        List<T4<Integer, String, Boolean, Long>> emptyList = List.of();
        T4<List<Integer>, List<String>, List<Boolean>, List<Long>> emptyT4 = List.unzip4(emptyList);
        assertThat(emptyT4._1()).isEmpty();
        assertThat(emptyT4._2()).isEmpty();
        assertThat(emptyT4._3()).isEmpty();
        assertThat(emptyT4._4()).isEmpty();
    }


    @Test
    public void testZip() {
        List<Integer> intList = List.of(1, 5, 7, 3);
        List<String> stringList = List.of("one", "five", "seven", "three", "blubb");
        List<Boolean> boolList = List.of(true, false, false);
        List<Long> longList = List.of(1L, 5L);
        assertThat(List.zip(intList, stringList)).containsExactly(
                T2.of(1, "one"), T2.of(5, "five"), T2.of(7, "seven"), T2.of(3, "three"));
        assertThat(List.zip(intList, stringList, boolList)).containsExactly(
                T3.of(1, "one", true), T3.of(5, "five", false), T3.of(7, "seven", false));
        assertThat(List.zip(intList, stringList, boolList, longList)).containsExactly(
                T4.of(1, "one", true, 1L), T4.of(5, "five", false, 5L));

        assertThat(List.zip(List.empty(), longList)).isEmpty();
        assertThat(List.zip(intList, List.empty(), List.empty())).isEmpty();
        assertThat(List.zip(intList, stringList, List.empty(), longList)).isEmpty();
    }

    @Test
    public void testZipWith() {
        List<Integer> intList = List.of(1, 2, 3, 2);
        List<String> stringList = List.of("one", "two", "three", "four", "blubb");
        assertThat(List.zipWith(stringList, intList, Strings.repeat)).containsExactly(
                "one", "twotwo", "threethreethree", "fourfour");
        assertThat(List.zipWith(stringList, intList, intList, s -> i -> j ->
                String.format("%s %d %d", s, i, j * j)))
                .containsExactly("one 1 1", "two 2 4", "three 3 9", "four 2 4");

        assertThat(List.zipWith(stringList, intList, intList, intList, s -> i -> j -> k ->
                String.format("%s %d %d %d", s, i, j * j, k + k)))
                .containsExactly("one 1 1 2", "two 2 4 4", "three 3 9 6", "four 2 4 4");

        assertThat(List.zipWith(List.empty(), intList, Strings.repeat)).isEmpty();
        assertThat(List.zipWith(stringList, List.empty(), Strings.repeat)).isEmpty();
    }

    @Test
    public void testTraversable() {
        ListTraversable traversable = List.traversable;
        List<__<Maybe.µ,Integer>> list = List.of(0,8,15).map(Maybe::newJust);
        assertThat(traversable.sequenceA(Maybe.monad, list)).isEqualTo(Maybe.newJust(List.of(0,8,15)));
        assertThat(traversable.sequenceA(Maybe.monad, list.plus(Maybe.newNothing()))).isEqualTo(Maybe.newNothing());
    }

    @Test
    public void testZipApplicative() {
        ZipApplicative zipAp = List.zipApplicative;
        assertThat(zipAp.pure(13)).startsWith(13,13,13,13);
        assertThat(zipAp.ap(List.of(x -> x + 10, x -> x + 20, x -> x + 30), List.of(1,2,3))).containsExactly(11,22,33);
        assertThat(zipAp.ap(List.of(x -> x + 10, x -> x + 20, x -> x + 30), List.<Integer>empty())).isEmpty();
        assertThat(zipAp.ap(List.empty(), List.of(1,2,3))).isEmpty();
        assertThat(zipAp.ap(zipAp.pure(x -> x + 10), List.of(1,2,3))).containsExactly(11,12,13);
        assertThat(zipAp.ap(List.of(x -> x + 10, x -> x + 20, x -> x + 30), zipAp.pure(1))).containsExactly(11,21,31);
    }

    @Test
    public void testGroup() {
        Group<List<Integer>> group = List.group();
        assertThat(group.identity()).isEmpty();
        assertThat(group.apply(group.identity(), group.identity())).isEmpty();
        assertThat(group.apply(List.of(1,2,3), group.identity())).containsExactly(1,2,3);
        assertThat(group.apply(group.identity(), List.of(1,2,3))).containsExactly(1,2,3);
        assertThat(group.apply(List.of(1,2,3), List.of(4,5,6))).containsExactly(1,2,3,4,5,6);
        assertThat(group.inverse(group.identity())).isEmpty();
        assertThat(group.inverse(List.of(1,2,3))).containsExactly(3,2,1);
    }
}
