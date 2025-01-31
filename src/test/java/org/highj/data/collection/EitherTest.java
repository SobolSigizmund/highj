package org.highj.data.collection;

import org.derive4j.hkt.__;
import org.derive4j.hkt.__2;
import org.highj.data.collection.either.EitherExtend;
import org.highj.data.collection.either.EitherMonad;
import org.highj.data.collection.either.EitherMonadPlus;
import org.highj.data.functions.Strings;
import org.highj.data.tuple.T2;
import org.highj.typeclass0.compare.Eq;
import org.highj.typeclass0.compare.Ord;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.NoSuchElementException;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.highj.data.collection.Either.*;


public class EitherTest {

    @Rule
    public ExpectedException shouldThrow = ExpectedException.none();

    @Test
    public void testBiFunctor() {
        Either<String, Integer> left = newLeft("Test");
        Either<String, Integer> right = newRight(42);
        assertThat(bifunctor.bimap(x -> x + x, y -> y / 7, left).getLeft()).isEqualTo("TestTest");
        assertThat(bifunctor.bimap(x -> x + x, y -> y / 7, right).getRight()).isEqualTo(6);
    }

    @Test
    public void testBimap() {
        Either<String, Integer> left = newLeft("Test");
        Either<String, Integer> right = newRight(42);
        assertThat(left.bimap(x -> x + x, y -> y / 7).getLeft()).isEqualTo("TestTest");
        assertThat(right.bimap(x -> x + x, y -> y / 7).getRight()).isEqualTo(6);
    }

    @Test
    public void testConstant() {
        Either<String, Integer> left = newLeft("Test");
        Either<String, Integer> right = newRight(42);
        assertThat(left.constant("x", "y")).isEqualTo("x");
        assertThat(right.constant("x", "y")).isEqualTo("y");
    }

    @Test
    public void testEither() {
        Either<String, Integer> left = newLeft("Test");
        Either<String, Integer> right = newRight(42);
        assertThat(left.either(String::length, Function.identity())).isEqualTo(4);
        assertThat(right.either(String::length, Function.identity())).isEqualTo(42);
    }

    @Test
    public void testEq() {
        Eq<Either<String, Integer>> eq = eq(new Eq.JavaEq<String>(), new Eq.JavaEq<Integer>());
        Either<String, Integer> left = newLeft("Test");
        Either<String, Integer> left2 = newLeft("Test");
        Either<String, Integer> left3 = newLeft("TestX");
        Either<String, Integer> lazyLeft = lazyLeft(() -> "Test");
        Either<String, Integer> right = newRight(42);
        Either<String, Integer> right2 = newRight(42);
        Either<String, Integer> right3 = newRight(43);
        Either<String, Integer> lazyRight = lazyRight(() -> 42);
        assertThat(eq.eq(left, left2)).isTrue();
        assertThat(eq.eq(left, left3)).isFalse();
        assertThat(eq.eq(left, lazyLeft)).isTrue();
        assertThat(eq.eq(right, right2)).isTrue();
        assertThat(eq.eq(right, right3)).isFalse();
        assertThat(eq.eq(right, lazyRight)).isTrue();
        assertThat(eq.eq(left, right)).isFalse();
        assertThat(eq.eq(left, null)).isFalse();
        assertThat(eq.eq(null, right)).isFalse();
        assertThat(eq.eq(null, null)).isTrue();
    }

    @Test
    public void testEquals() {
        Either<String, Integer> left = newLeft("Test");
        Either<String, Integer> left2 = newLeft("Test");
        Either<String, Integer> left3 = newLeft("TestX");
        Either<String, Integer> lazyLeft = lazyLeft(() -> "Test");
        Either<String, Integer> right = newRight(42);
        Either<String, Integer> right2 = newRight(42);
        Either<String, Integer> right3 = newRight(43);
        Either<String, Integer> lazyRight = lazyRight(() -> 42);
        Either<Integer, String> rightString = newRight("Test");
        assertThat(left.equals(left2)).isTrue();
        assertThat(left.equals(left3)).isFalse();
        assertThat(left.equals(lazyLeft)).isTrue();
        assertThat(right.equals(right2)).isTrue();
        assertThat(right.equals(right3)).isFalse();
        assertThat(right.equals(lazyRight)).isTrue();
        assertThat(left.equals(right)).isFalse();
        assertThat(left.equals(rightString)).isFalse();
    }

    @Test
    public void testExtend() {
        EitherExtend<String> extend = Either.extend();
        Either<String, Integer> left = newLeft("Test");
        Either<String, Integer> right = newRight(42);
        assertThat(Either.narrow(extend.duplicate(left)).getLeft()).isEqualTo("Test");
        assertThat(Either.narrow(Either.narrow(extend.duplicate(right)).getRight()).getRight()).isEqualTo(42);

        Function<__<__<µ, String>,Integer>, __<__<µ, String>,Integer>> fun = extend.extend(
                either -> Either.narrow(either).rightMap(x -> x / 2).rightOrElse(-1));
        assertThat(Either.narrow(fun.apply(left)).getLeft()).isEqualTo("Test");
        assertThat(Either.narrow(fun.apply(right)).getRight()).isEqualTo(21);
    }

    @Test
    public void testFirstBiasedMonadPlus() {
        EitherMonadPlus<String> eitherMonadPlus = Either.firstBiasedMonadPlus(Strings.group);
        testMonadPlus(eitherMonadPlus, 1);
    }

    @Test
    public void testGetLeft() {
        Either<String, Integer> left = newLeft("Test");
        Either<String, Integer> right = newRight(42);
        assertThat(left.getLeft()).isEqualTo("Test");
        shouldThrow.expect(NoSuchElementException.class);
        right.getLeft();
    }

    @Test
    public void testGetRight() {
        Either<String, Integer> left = newLeft("Test");
        Either<String, Integer> right = newRight(42);
        assertThat(right.getRight()).isEqualTo(42);
        shouldThrow.expect(NoSuchElementException.class);
        left.getRight();
    }

    @Test
    public void testHashCode() {
        Either<String, Integer> left = newLeft("Test");
        Either<String, Integer> left2 = newLeft("Test");
        Either<String, Integer> left3 = newLeft("TestX");
        Either<String, Integer> lazyLeft = lazyLeft(() -> "Test");
        Either<String, Integer> right = newRight(42);
        Either<String, Integer> right2 = newRight(42);
        Either<String, Integer> right3 = newRight(43);
        Either<String, Integer> lazyRight = lazyRight(() -> 42);
        Either<Integer, String> rightString = newRight("Test");
        assertThat(left.hashCode()).isEqualTo(left2.hashCode());
        assertThat(left.hashCode()).isNotEqualTo(left3.hashCode());
        assertThat(left.hashCode()).isEqualTo(lazyLeft.hashCode());
        assertThat(right.hashCode()).isEqualTo(right2.hashCode());
        assertThat(right.hashCode()).isNotEqualTo(right3.hashCode());
        assertThat(right.hashCode()).isEqualTo(lazyRight.hashCode());
        assertThat(left.hashCode()).isNotEqualTo(right.hashCode());
        assertThat(left.hashCode()).isNotEqualTo(rightString.hashCode());
    }

    @Test
    public void testIsLeft() {
        Either<String, Integer> left = newLeft("Test");
        Either<String, Integer> right = newRight(42);
        assertThat(left.isLeft()).isTrue();
        assertThat(right.isLeft()).isFalse();
    }

    @Test
    public void testIsRight() {
        Either<String, Integer> left = newLeft("Test");
        Either<String, Integer> right = newRight(42);
        assertThat(left.isRight()).isFalse();
        assertThat(right.isRight()).isTrue();
    }

    @Test
    public void testLastBiasedMonadPlus() {
        EitherMonadPlus<String> eitherMonadPlus = Either.lastBiasedMonadPlus(Strings.group);
        testMonadPlus(eitherMonadPlus, 2);
    }

    @Test
    public void testLazyLeft() {
        String[] sideEffects = {"unchanged"};
        Either<String, Integer> left = lazyLeft(() -> {
            sideEffects[0] = "changed";
            return "Test";
        });
        assertThat(left.isLeft()).isTrue();
        assertThat(sideEffects[0]).isEqualTo("unchanged");
        assertThat(left.getLeft()).isEqualTo("Test");
        assertThat(sideEffects[0]).isEqualTo("changed");
    }

    @Test
    public void testLazyLefts() {
        List<Either<String, Integer>> list = List.cycle(
                newLeft("a", Integer.class),
                newLeft("b", Integer.class),
                newRight(String.class, 1));
        assertThat(lazyLefts(list).take(4)).containsExactly("a", "b", "a", "b");
    }

    @Test
    public void testLazyRight() {
        String[] sideEffects = {"unchanged"};
        Either<String, Integer> right = lazyRight(() -> {
            sideEffects[0] = "changed";
            return 42;
        });
        assertThat(right.isRight()).isTrue();
        assertThat(sideEffects[0]).isEqualTo("unchanged");
        assertThat(right.getRight()).isEqualTo(42);
        assertThat(sideEffects[0]).isEqualTo("changed");
    }

    @Test
    public void testLazyRights() {
        List<Either<String, Integer>> list = List.cycle(
                newLeft("a", Integer.class),
                newRight(String.class, 1),
                newRight(String.class, 2));
        assertThat(lazyRights(list).take(4)).containsExactly(1, 2, 1, 2);

    }

    @Test
    public void testLeftMap() {
        Either<String, Integer> left = newLeft("Test");
        Either<String, Integer> right = newRight(42);
        assertThat(left.leftMap(x -> x + x).getLeft()).isEqualTo("TestTest");
        assertThat(right.leftMap(x -> x + x).getRight()).isEqualTo(42);
    }

    @Test
    public void testLeftOrElse() {
        Either<String, Integer> left = newLeft("Test");
        Either<String, Integer> right = newRight(42);
        assertThat(left.leftOrElse("Default")).isEqualTo("Test");
        assertThat(right.leftOrElse("Default")).isEqualTo("Default");
    }

    @Test
    public void testLefts() {
        List<Either<String, Integer>> list = List.of(
                newLeft("a", Integer.class),
                newLeft("b", Integer.class),
                newRight(String.class, 1),
                newLeft("c", Integer.class),
                newRight(String.class, 2));
        assertThat(lefts(list)).containsExactly("a", "b", "c");
    }

    @Test
    public void testMaybeLeft() {
        Either<String, Integer> left = newLeft("Test");
        Either<String, Integer> right = newRight(42);
        assertThat(left.maybeLeft()).isEqualTo(Maybe.newJust("Test"));
        assertThat(right.maybeLeft().isNothing()).isTrue();
    }

    @Test
    public void testMaybeRight() {
        Either<String, Integer> left = newLeft("Test");
        Either<String, Integer> right = newRight(42);
        assertThat(left.maybeRight().isNothing()).isTrue();
        assertThat(right.maybeRight()).isEqualTo(Maybe.newJust(42));
    }

    @Test
    public void testMonad() {
        EitherMonad<String> eitherMonad = Either.monad();

        //map
        Either<String, Integer> left = newLeft("Test");
        Either<String, Integer> right = newRight(42);
        assertThat(eitherMonad.map(x -> x / 2, left).getLeft()).isEqualTo("Test");
        assertThat(eitherMonad.map(x -> x / 2, right).getRight()).isEqualTo(21);

        //ap
        Either<String, Function<Integer, Integer>> leftFn = newLeft("Nope");
        Either<String, Function<Integer, Integer>> rightFn = newRight(x -> x / 2);
        assertThat(eitherMonad.ap(leftFn, left).getLeft()).isEqualTo("Nope");  //left biased like Haskell
        assertThat(eitherMonad.ap(leftFn, right).getLeft()).isEqualTo("Nope");
        assertThat(eitherMonad.ap(rightFn, left).getLeft()).isEqualTo("Test");
        assertThat(eitherMonad.ap(rightFn, right).getRight()).isEqualTo(21);

        //pure
        assertThat(eitherMonad.pure(12).getRight()).isEqualTo(12);

        //bind
        Either<String, Integer> rightOdd = newRight(43);
        Function<Integer, __<__<µ, String>, Integer>> halfEven =
                x -> x % 2 == 0 ? newRight(x / 2) : newLeft("Odd");
        assertThat(eitherMonad.bind(left, halfEven).getLeft()).isEqualTo("Test");
        assertThat(eitherMonad.bind(right, halfEven).getRight()).isEqualTo(21);
        assertThat(eitherMonad.bind(rightOdd, halfEven).getLeft()).isEqualTo("Odd");

        //tailRec
        Function<T2<Integer, Integer>, __<__<µ, String>, Either<T2<Integer,Integer>, Integer>>> factorial = pair ->  {
            int factor = pair._1();
            int product = pair._2();
            if (factor < 0) {
                return Either.newLeft("Can't be negative");
            } else if (factor == 0) {
                return Either.newRight(Either.newRight(product));
            } else {
                return Either.newRight(Either.newLeft(T2.of(factor - 1, factor * product)));
            }
        };
        Either<String, Integer> validResult = eitherMonad.tailRec(factorial, T2.of(10, 1));
        assertThat(validResult).isEqualTo(Either.newRight(3628800));
        Either<String, Integer> invalidResult = eitherMonad.tailRec(factorial, T2.of(-10, 1));
        assertThat(invalidResult).isEqualTo(Either.newLeft("Can't be negative"));
    }

    private void testMonadPlus(EitherMonadPlus<String> eitherMonadPlus, Integer expectedWhenBiased) {
        Either<String, Integer> zero = eitherMonadPlus.mzero();
        assertThat(zero.getLeft()).isEqualTo("");
        //mplus
        Either<String, Integer> left1 = newLeft("one");
        Either<String, Integer> left2 = newLeft("two");
        Either<String, Integer> right1 = newRight(1);
        Either<String, Integer> right2 = newRight(2);
        assertThat(eitherMonadPlus.mplus(left1, left2).getLeft()).isEqualTo("onetwo");
        assertThat(eitherMonadPlus.mplus(right1, left2).getRight()).isEqualTo(1);
        assertThat(eitherMonadPlus.mplus(left1, right2).getRight()).isEqualTo(2);
        assertThat(eitherMonadPlus.mplus(right1, right2).getRight()).isEqualTo(expectedWhenBiased);
    }

    @Test
    public void testNarrow() {
        __2<µ, String, Integer> wideLeft = newLeft("Test");
        Either<String, Integer> left = narrow(wideLeft);
        assertThat(left).isEqualTo(wideLeft);
        __2<µ, String, Integer> wideRight = newRight(42);
        Either<String, Integer> right = narrow(wideRight);
        assertThat(right).isEqualTo(wideRight);
    }

    @Test
    public void testNewLeft() {
        Either<String, Integer> left = newLeft("Test");
        assertThat(left.isLeft()).isTrue();
        assertThat(left.getLeft()).isEqualTo("Test");
    }

    @Test
    public void testNewRight() {
        Either<String, Integer> right = newRight(42);
        assertThat(right.isRight()).isTrue();
        assertThat(right.getRight()).isEqualTo(42);
    }

    @Test
    public void testOrd() {
        Ord<Either<String, Integer>> ord = Either.ord(
                Ord.<String>fromComparable(),
                Ord.<Integer>fromComparable());
        List<Either<String, Integer>> list = List.of(
                newLeft("a", Integer.class),
                newLeft("c", Integer.class),
                newRight(String.class, 2),
                newLeft("b", Integer.class),
                newRight(String.class, 1));
        assertThat(list.sort(ord)).containsExactly(
                newLeft("a", Integer.class),
                newLeft("b", Integer.class),
                newLeft("c", Integer.class),
                newRight(String.class, 1),
                newRight(String.class, 2));
    }

    @Test
    public void testRightMap() {
        Either<String, Integer> left = newLeft("Test");
        Either<String, Integer> right = newRight(42);
        assertThat(left.rightMap(x -> x / 7).getLeft()).isEqualTo("Test");
        assertThat(right.rightMap(x -> x / 7).getRight()).isEqualTo(6);
    }

    @Test
    public void testRightOrElse() {
        Either<String, Integer> left = newLeft("Test");
        Either<String, Integer> right = newRight(42);
        assertThat(left.rightOrElse(12)).isEqualTo(12);
        assertThat(right.rightOrElse(12)).isEqualTo(42);
    }

    @Test
    public void testRights() {
        List<Either<String, Integer>> list = List.of(
                newLeft("a", Integer.class),
                newLeft("b", Integer.class),
                newRight(String.class, 1),
                newLeft("c", Integer.class),
                newRight(String.class, 2));
        assertThat(rights(list)).containsExactly(1, 2);
    }

    @Test
    public void testSplit() {
        List<Either<String, Integer>> list = List.of(
                newLeft("a", Integer.class),
                newLeft("b", Integer.class),
                newRight(String.class, 1),
                newLeft("c", Integer.class),
                newRight(String.class, 2));
        T2<List<String>, List<Integer>> t2 = split(list);
        assertThat(t2._1()).containsExactly("a", "b", "c");
        assertThat(t2._2()).containsExactly(1, 2);
    }

    @Test
    public void testSwap() {
        Either<String, Integer> left = newLeft("Test");
        Either<String, Integer> right = newRight(42);
        assertThat(left.swap()).isEqualTo(newRight(Integer.class, "Test"));
        assertThat(right.swap()).isEqualTo(newLeft(42, String.class));
    }

    @Test
    public void testToString() {
        assertThat(newLeft("Test").toString()).isEqualTo("Left(Test)");
        assertThat(newRight(42).toString()).isEqualTo("Right(42)");
    }

    @Test
    public void testUnify() {
        Either<String, String> left = newLeft("Foo");
        Either<String, String> right = newRight("Bar");
        assertThat(unify(left)).isEqualTo("Foo");
        assertThat(unify(right)).isEqualTo("Bar");
    }

}
