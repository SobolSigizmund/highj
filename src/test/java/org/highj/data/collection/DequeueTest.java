package org.highj.data.collection;

import org.derive4j.hkt.__;
import org.highj.data.tuple.T2;
import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

public class DequeueTest {

    @Test
    public void testNarrow() throws Exception {
        __<Dequeue.µ, String> dqHKT = Dequeue.of("a","b","c");
        Dequeue<String> dq = Dequeue.narrow(dqHKT);
        assertThat(dq).containsExactly("a","b","c");
    }

    @Test
    public void testToList() throws Exception {
        Dequeue<String> empty = Dequeue.empty();
        assertThat(empty.toList()).isEqualTo(List.empty());
        Dequeue<String> dq = Dequeue.of("a","b","c");
        assertThat(dq.toList()).isEqualTo(List.of("a","b","c"));
    }

    @Test
    public void testEmpty() throws Exception {
        Dequeue<String> dq = Dequeue.empty();
        assertThat(dq).isEmpty();
        assertThat(dq.getFirst()).isEqualTo(Maybe.newNothing());
        assertThat(dq.getLast()).isEqualTo(Maybe.newNothing());
    }

    @Test
    public void testOf() throws Exception {
        Dequeue<String> empty = Dequeue.of();
        assertThat(empty).isEmpty();
        Dequeue<String> dq = Dequeue.of("a","b","c");
        assertThat(dq).containsExactly("a","b","c");
    }

    @Test
    public void testIsEmpty() throws Exception {
        Dequeue<String> dq = Dequeue.of("a","b","c");
        assertThat(dq.isEmpty()).isFalse();
        Dequeue<String> empty = Dequeue.empty();
        assertThat(empty.isEmpty()).isTrue();
    }

    @Test
    public void testSize() throws Exception {
        Dequeue<String> dq = Dequeue.of("a","b","c");
        assertThat(dq.size()).isEqualTo(3);
        Dequeue<String> empty = Dequeue.empty();
        assertThat(empty.size()).isEqualTo(0);
    }

    @Test
    public void testGetFirst() throws Exception {
        Dequeue<String> dq = Dequeue.of("a","b","c");
        assertThat(dq.getFirst().get()).isEqualTo("a");
        Dequeue<String> empty = Dequeue.empty();
        assertThat(empty.getFirst().isNothing()).isTrue();
    }

    @Test
    public void testGetLast() throws Exception {
        Dequeue<String> dq = Dequeue.of("a","b","c");
        assertThat(dq.getLast().get()).isEqualTo("c");
        Dequeue<String> empty = Dequeue.empty();
        assertThat(empty.getLast().isNothing()).isTrue();
    }

    @Test
    public void testTakeFront() throws Exception {
        Dequeue<String> dq = Dequeue.of("a","b","c");
        assertThat(dq.takeFront(-1)).isEmpty();
        assertThat(dq.takeFront(0)).isEmpty();
        assertThat(dq.takeFront(1)).containsExactly("a");
        assertThat(dq.takeFront(3)).containsExactly("a","b","c");
        assertThat(dq.takeFront(100)).containsExactly("a","b","c");
        Dequeue<String> empty = Dequeue.empty();
        assertThat(empty.takeFront(-1)).isEmpty();
        assertThat(empty.takeFront(0)).isEmpty();
        assertThat(empty.takeFront(100)).isEmpty();
    }

    @Test
    public void testTakeBack() throws Exception {
        Dequeue<String> dq = Dequeue.of("a","b","c");
        assertThat(dq.takeBack(-1)).isEmpty();
        assertThat(dq.takeBack(0)).isEmpty();
        assertThat(dq.takeBack(1)).containsExactly("c");
        assertThat(dq.takeBack(3)).containsExactly("c","b","a");
        assertThat(dq.takeBack(100)).containsExactly("c","b","a");
        Dequeue<String> empty = Dequeue.empty();
        assertThat(empty.takeBack(-1)).isEmpty();
        assertThat(empty.takeBack(0)).isEmpty();
        assertThat(empty.takeBack(100)).isEmpty();
    }

    @Test
    public void testPushFront() throws Exception {
        Dequeue<String> dq = Dequeue.of("a","b","c");
        assertThat(dq.pushFront("z")).containsExactly("z","a","b","c");
        Dequeue<String> empty = Dequeue.empty();
        assertThat(empty.pushFront("z")).containsExactly("z");
    }

    @Test
    public void testPopFront() throws Exception {
        Dequeue<String> dq = Dequeue.of("a","b","c");
        T2<Maybe<String>, Dequeue<String>> dqT2 = dq.popFront();
        assertThat(dqT2._1().get()).isEqualTo("a");
        assertThat(dqT2._2()).containsExactly("b","c");
        Dequeue<String> empty = Dequeue.empty();
        T2<Maybe<String>, Dequeue<String>> emptyT2 = empty.popFront();
        assertThat(emptyT2._1().isNothing()).isTrue();
        assertThat(emptyT2._2()).isEmpty();
    }

    @Test
    public void testPushBack() throws Exception {
        Dequeue<String> dq = Dequeue.of("a","b","c");
        assertThat(dq.pushBack("z")).containsExactly("a","b","c","z");
        Dequeue<String> empty = Dequeue.empty();
        assertThat(empty.pushBack("z")).containsExactly("z");
    }

    @Test
    public void testPopBack() throws Exception {
        Dequeue<String> dq = Dequeue.of("a","b","c");
        T2<Maybe<String>, Dequeue<String>> dqT2 = dq.popBack();
        assertThat(dqT2._1().get()).isEqualTo("c");
        assertThat(dqT2._2()).containsExactly("a","b");
        Dequeue<String> empty = Dequeue.empty();
        T2<Maybe<String>, Dequeue<String>> emptyT2 = empty.popBack();
        assertThat(emptyT2._1().isNothing()).isTrue();
        assertThat(emptyT2._2()).isEmpty();
    }

    @Test
    public void testFromList() throws Exception {
        Dequeue<String> dq = Dequeue.fromList(List.of("a","b","c"));
        assertThat(dq).containsExactly("a","b","c");
        Dequeue<String> empty = Dequeue.fromList(List.empty());
        assertThat(empty).isEmpty();
    }

    @Test
    public void testToString() throws Exception {
        Dequeue<String> dq = Dequeue.of("a","b","c");
        assertThat(dq.toString()).isEqualTo("Dequeue(a,b,c)");
        Dequeue<String> empty = Dequeue.empty();
        assertThat(empty.toString()).isEqualTo("Dequeue()");
    }

    @Test
    public void testIterator() throws Exception {
        Dequeue<String> dq = Dequeue.of("a","b","c");
        assertThat(dq.iterator()).containsExactly("a","b","c");
        Dequeue<String> empty = Dequeue.empty();
        assertThat(empty.iterator()).isEmpty();
    }

    @Test
    public void testFoldr() throws Exception {
        Dequeue<Character> dq = Dequeue.of('a','b','c');
        assertThat(dq.<String>foldr(c -> s -> s + "-" + c, "z")).isEqualTo("z-c-b-a");
    }

    @Test
    public void testFoldl() throws Exception {
        Dequeue<Character> dq = Dequeue.of('a','b','c');
        assertThat(dq.<String>foldl("z", s -> c -> s + "-" + c)).isEqualTo("z-a-b-c");
    }

    @Test
    public void testFunctor() throws Exception {
        Dequeue<String> dq = Dequeue.of("a","bb","cccc");
        Dequeue<Integer> result = Dequeue.functor.map(String::length, dq);
        assertThat(result).containsExactly(1,2,4);
    }

    @Test
    public void testFoldable() throws Exception {
        Dequeue<String> dq = Dequeue.of("a","bb","cccc");
        Integer result = Dequeue.traversable.foldl(i -> s -> 10*i + s.length(), 5, dq);
        assertThat(result).isEqualTo(5124);
    }
}