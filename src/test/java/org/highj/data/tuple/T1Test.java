package org.highj.data.tuple;

import org.derive4j.hkt.__;
import org.highj.data.collection.Either;
import org.highj.data.functions.Functions;
import org.highj.data.functions.Integers;
import org.highj.data.collection.HList;
import org.highj.data.tuple.t1.T1Comonad;
import org.highj.data.tuple.t1.T1Monad;
import org.highj.typeclass0.compare.Eq;
import org.highj.typeclass0.compare.Ord;
import org.highj.typeclass0.group.Group;
import org.highj.typeclass1.functor.Functor;
import org.highj.typeclass1.monad.Apply;
import org.highj.typeclass1.monad.Monad;
import org.junit.Test;

import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

public class T1Test {

    @Test
    public void testAp() {
        T1<Function<String, Integer>> lengthFn = T1.of(String::length);
        T1<String> hello = T1.of("hello");
        T1<Integer> five = hello.ap(lengthFn);
        assertThat(five._1()).isEqualTo(5);
    }

    @Test
    public void testApply() {
        Apply<T1.µ> apply = T1.monad;
        T1<Function<String, Integer>> lengthFn = T1.of(String::length);
        T1<String> hello = T1.of("hello");
        T1<Integer> five = T1.narrow(apply.ap(lengthFn, hello));
        assertThat(five._1()).isEqualTo(5);
    }

    @Test
    public void testBind() {
        Function<String, T1<Integer>> lengthFn = Functions.compose(T1::of, String::length);
        T1<String> hello = T1.of("hello");
        T1<Integer> five = hello.bind(lengthFn);
        assertThat(five._1()).isEqualTo(5);
    }

    @Test
    public void testComonad() {
        T1Comonad comonad = T1.comonad;
        assertThat(comonad.duplicate(T1.of(2))).isEqualTo(T1.of(T1.of(2)));
        assertThat(comonad.extract(T1.of(2))).isEqualTo(2);
        Function<__<T1.µ, String>, Integer> function = nested -> T1.narrow(nested)._1().length();
        assertThat(comonad.extend(function).apply(T1.of("hello"))).isEqualTo(T1.of(5));
    }

    @Test
    public void testEq() {
        Eq<T1<String>> eq = T1.eq(new Eq.JavaEq<>());
        T1<String> one = T1.of("hello");
        T1<String> two = T1.ofLazy(() -> "hello");
        T1<String> three = T1.of("world");
        assertThat(eq.eq(one, two)).isTrue();
        assertThat(eq.eq(one, three)).isFalse();
        assertThat(eq.eq(two, three)).isFalse();
    }

    @Test
    public void testFunctor() {
        Functor<T1.µ> functor = T1.monad;
        T1<String> hello = T1.of("hello");
        T1<Integer> five = T1.narrow(functor.map(String::length, hello));
        assertThat(five._1()).isEqualTo(5);
    }

    @Test
    public void testGroup() {
        Group<T1<Integer>> group = T1.group(Integers.additiveGroup);
        assertThat(group.apply(T1.of(2), T1.of(3))).isEqualTo(T1.of(5));
        assertThat(group.identity()).isEqualTo(T1.of(0));
        assertThat(group.inverse(T1.of(2))).isEqualTo(T1.of(-2));
    }

    @Test
    public void testMap() {
        T1<String> hello = T1.of("hello");
        T1<Integer> five = hello.map(String::length);
        assertThat(five._1()).isEqualTo(5);

        //lazy version
        T1<String> helloLazy = T1.ofLazy(() -> "hello");
        T1<Integer> fiveLazy = helloLazy.map(String::length);
        assertThat(fiveLazy._1()).isEqualTo(5);
    }

    @Test
    public void testMerge() {
        T1<String> hello = T1.of("hello");
        T1<Integer> fortyTwo = T1.of(42);
        Function<String, Function<Integer, String>> function = s -> i -> String.format("%s %d!", s, i);
        assertThat(T1.merge(hello, fortyTwo, function)._1()).isEqualTo("hello 42!");
    }

    @Test
    public void testMonad() {
        Monad<T1.µ> monad = T1.monad;
        Function<String, __<T1.µ, Integer>> lengthFn = Functions.compose(T1::of, String::length);
        T1<String> hello = T1.of("hello");
        T1<Integer> five = T1.narrow(monad.bind(hello, lengthFn));
        assertThat(five._1()).isEqualTo(5);
    }

    @Test
    public void testMonadRec() {
        T1Monad monad = T1.monad;
        Function<Integer, __<T1.µ, Either<Integer,Integer>>> digitSum = i ->
                T1.of(i < 10 ? Either.newRight(i) : Either.newLeft(i / 10 + i % 10));
        assertThat(monad.tailRec(digitSum, 4711)).isEqualTo(T1.of(4));
    }

    @Test
    public void testNarrow() {
        __<T1.µ, Integer> fortyTwo = T1.of(42);
        assertThat(T1.narrow(fortyTwo)._1()).isEqualTo(42);
        //lazy version
        __<T1.µ, String> hello = T1.ofLazy(() -> "hello");
        assertThat(T1.narrow(hello)._1());
    }

    @Test
    public void testOrd() {
        Ord<T1<String>> ord = T1.ord(Ord.fromComparable());
        T1<String> one = T1.of("hello");
        T1<String> two = T1.ofLazy(() -> "hello");
        T1<String> three = T1.of("world");
        assertThat(ord.eq(one, two)).isTrue();
        assertThat(ord.greaterEqual(one, two)).isTrue();
        assertThat(ord.greater(one, three)).isFalse();
        assertThat(ord.greaterEqual(one, three)).isFalse();
        assertThat(ord.lessEqual(two, three)).isTrue();
    }

    @Test
    public void testToHList() {
        HList.HCons<Integer, HList.HNil> hList = T1.of(42).toHlist();
        assertThat(hList.head()).isEqualTo(42);
    }

    @Test
    public void testToString() {
        T1<Integer> fortyTwo = T1.of(42);
        assertThat(fortyTwo.toString()).isEqualTo("(42)");
        //lazy version
        T1<String> hello = T1.ofLazy(() -> "hello");
        assertThat(hello.toString()).isEqualTo("(hello)");
    }
}
