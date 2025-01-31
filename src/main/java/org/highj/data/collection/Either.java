package org.highj.data.collection;

import org.derive4j.hkt.__;
import org.derive4j.hkt.__2;
import org.highj.data.collection.either.EitherBifunctor;
import org.highj.data.collection.either.EitherExtend;
import org.highj.data.collection.either.EitherMonad;
import org.highj.data.collection.either.EitherMonadPlus;
import org.highj.data.compare.Ordering;
import org.highj.data.functions.Functions;
import org.highj.data.functions.Strings;
import org.highj.data.tuple.T2;
import org.highj.typeclass0.compare.Eq;
import org.highj.typeclass0.compare.Ord;
import org.highj.typeclass0.group.Monoid;
import org.highj.typeclass1.monad.Monad;
import org.highj.typeclass1.monad.MonadPlus;

import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class Either<A, B> implements __2<Either.µ, A, B> {

    private static final String SHOW_LEFT = "Left(%s)";
    private static final String SHOW_RIGHT = "Right(%s)";

    /**
     * The witness class of {@link Either}.
     */
    public static class µ {
    }

    private Either() {
    }

    /**
     * Recovers an {@link Either} value.
     *
     * @param value the curried higher kinded type representation
     * @param <A> the Left type
     * @param <B> the Right type
     * @return the standard representation
     */
    @SuppressWarnings("unchecked")
    public static <A, B> Either<A, B> narrow(__<__<µ, A>, B> value) {
        return (Either) value;
    }

    /**
     * Recovers an {@link Either} value.
     *
     * @param value the higher kinded type representation
     * @param <A> the Left type
     * @param <B> the Right type
     * @return the standard representation
     */
    @SuppressWarnings("unchecked")
    public static <A, B> Either<A, B> narrow(__2<µ, A, B> value) {
        return (Either) value;
    }

    /**
     * The catamorphism of {@link Either}
     *
     * @param leftFn  function for Left values
     * @param rightFn function for Right values
     * @param <C>     the result type
     * @return the result of the function application
     */
    public abstract <C> C either(Function<A, C> leftFn, Function<B, C> rightFn);

    /**
     * The catamorphism using constant functions.
     *
     * @param left  result if it is a Left value
     * @param right result if it is a Right value
     * @param <C>   the result type
     * @return the result of the function application
     */
    public <C> C constant(C left, C right) {
        return either(Functions.<A, C>constant(left), Functions.<B, C>constant(right));
    }

    /**
     * Converts an {@link Either}.
     *
     * @param leftFn  the mapping function for a Left
     * @param rightFn the mapping function for a Right
     * @param <C>     the new Left type
     * @param <D>     the new Right type
     * @return the converted {@link Either}
     */
    public <C, D> Either<C, D> bimap(Function<? super A, ? extends C> leftFn, Function<? super B, ? extends D> rightFn) {
        return either(a -> Either.<C, D>newLeft(leftFn.apply(a)),
                b -> Either.<C, D>newRight(rightFn.apply(b)));
    }

    public <C> Either<C, B> leftMap(Function<? super A, ? extends C> leftFn) {
        return bimap(leftFn, Function.<B>identity());
    }

    public <C> Either<A, C> rightMap(Function<? super B, ? extends C> rightFn) {
        return bimap(Function.<A>identity(), rightFn);
    }

    /**
     * Constructs of a Left instance.
     *
     * @param a   the Left value
     * @param <A> the Left type
     * @param <B> the Right type
     * @return the Left instance
     */
    public static <A, B> Either<A, B> newLeft(final A a) {
        return new Either<A, B>() {

            @Override
            public <C> C either(Function<A, C> leftFn, Function<B, C> rightFn) {
                return leftFn.apply(a);
            }

            @Override
            public boolean isLeft() {
                return true;
            }

        };
    }

    /**
     * Constructs a Left instance with inference support.
     * <p>
     * In cases where type inference can't infer the Right type, you can write e.g.
     * <code>newLeft("foo", Integer.class)</code>
     * instead of <code>Either.&lt;String, Integer&gt;newLeft("foo")</code>
     *
     * @param a     the Left value
     * @param clazz the class of the Right type
     * @param <A>   the Left type
     * @param <B>   the Right type
     * @return the Left instance
     */
    public static <A, B> Either<A, B> newLeft(final A a, Class<B> clazz) {
        return newLeft(a);
    }

    /**
     * Constructs a lazy Left instance.
     *
     * @param supplier the Left {@link Supplier}
     * @param <A>   the Left type
     * @param <B>   the Right type
     * @return the Left instance
     */
    public static <A, B> Either<A, B> lazyLeft(final Supplier<A> supplier) {
        return new Either<A, B>() {

            @Override
            public <C> C either(Function<A, C> leftFn, Function<B, C> rightFn) {
                return leftFn.apply(supplier.get());
            }

            @Override
            public boolean isLeft() {
                return true;
            }
        };
    }

    /**
     * Constructs a Right instance.
     *
     * @param b   the Right value
     * @param <A> the type of the Left value
     * @param <B> the type of the Right value
     * @return the Right instance
     */
    public static <A, B> Either<A, B> newRight(final B b) {
        return new Either<A, B>() {

            @Override
            public <C> C either(Function<A, C> leftFn, Function<B, C> rightFn) {
                return rightFn.apply(b);
            }

            @Override
            public boolean isLeft() {
                return false;
            }
        };
    }

    /**
     * Constructs a Right instance with inference support.
     * <p>
     * In cases where type inference can't infer the type of Left, you can write e.g.
     * <code>newRight(Integer.class, "foo")</code>
     * instead of <code>Either.&lt;Integer,String&gt;newRight("foo")</code>
     *
     * @param clazz the class of the Left type
     * @param b     the Right value
     * @param <A>   the type of the Left value
     * @param <B>   the type of the Right value
     * @return the Right instance.
     */
    public static <A, B> Either<A, B> newRight(Class<A> clazz, final B b) {
        return newRight(b);
    }

    /**
     * Constructs a lazy Right instance.
     *
     * @param supplier the Right {@link Supplier}
     * @param <A>   the Left type
     * @param <B>   the Right type
     * @return the Right instance
     */
    public static <A, B> Either<A, B> lazyRight(final Supplier<B> supplier) {
        return new Either<A, B>() {

            @Override
            public <C> C either(Function<A, C> leftFn, Function<B, C> rightFn) {
                return rightFn.apply(supplier.get());
            }

            @Override
            public boolean isLeft() {
                return false;
            }
        };
    }

    /**
     * Returns an {@link Either} with Left and Right swapped.
     *
     * @return the {@link Either} with swapped Left and Right values
     */
    public Either<B, A> swap() {
        return either(left -> Either.<B, A>newRight(left), right -> Either.<B, A>newLeft(right));
    }

    /**
     * Test for Left.
     *
     * @return true if this is a Left
     */
    public abstract boolean isLeft();

    /**
     * Test for Right.
     *
     * @return true if this is a Right
     */
    public boolean isRight() {
        return ! isLeft();
    }

    /**
     * Extracts the Left value as {@link Maybe}.
     *
     * @return a {@link Maybe} containing the Left value, if it exists
     */
    public Maybe<A> maybeLeft() {
        return either(Maybe::<A>newJust, Functions.constant(Maybe.newNothing()));
    }

    /**
     * Extracts the Right value as {@link Maybe}.
     *
     * @return a {@link Maybe} containing Right value, if it exists
     */
    public Maybe<B> maybeRight() {
        return either(Functions.constant(Maybe.newNothing()), Maybe::<B>newJust);
    }

    /**
     * Extracts the Left value, or uses the default value if it doesn't exist.
     *
     * @param defaultValue default value
     * @return the Left value if it exists, else the default value
     */
    public A leftOrElse(A defaultValue) {
        return either(Function.identity(), Functions.constant(defaultValue));
    }

    /**
     * Extracts the Right value, or uses the default value if it doesn't exist.
     *
     * @param defaultValue default value
     * @return the Right value if it exists, else the default value
     */
    public B rightOrElse(B defaultValue) {
        return either(Functions.<A, B>constant(defaultValue), Function.<B>identity());
    }

    /**
     * Extracts the Left value.
     *
     * @return the Left value
     * @throws NoSuchElementException
     */
    public A getLeft() throws NoSuchElementException {
        return either(Function.identity(), Functions.constant(
                Functions.error(NoSuchElementException.class, "getLeft called on a lazyRight")));
    }

    /**
     * Extracts the Right value.
     *
     * @return the Right value
     * @throws NoSuchElementException
     */
    public B getRight() throws NoSuchElementException {
        return either(Functions.constant(
                Functions.error(NoSuchElementException.class, "getRight called on a lazyLeft")), Function.identity());
    }

    /**
     * Extracts the Left values from a {@link List}.
     * @param list a {@link List} of {@link Either} values
     * @param <A> Left type
     * @return a {@link List} of all Left values
     */
    public static <A> List<A> lefts(List<? extends Either<A, ?>> list) {
        List<A> result = List.nil();
        for (Either<A, ?> either : list) {
            if (either.isLeft()) {
                result = result.plus(either.getLeft());
            }
        }
        return result.reverse();
    }

    /**
     * Extracts the Left values from a {@link List} in a lazy fashion.
     * @param list a {@link List} of {@link Either} values
     * @param <A> Left type
     * @return a {@link List} of all Left values
     */
    public static <A> List<A> lazyLefts(List<? extends Either<A, ?>> list) throws StackOverflowError {
        if (list.isEmpty()) {
            return List.nil();
        } else {
            Either<A, ?> head = list.head();
            return head.<List<A>>either(a -> List.newLazyList(a, () -> Either.<A>lazyLefts(list.tail())),
                    b -> lazyLefts(list.tail()));
        }
    }

    /**
     * Extracts the Right values from a {@link List}.
     * @param list a {@link List} of {@link Either} values
     * @param <B> Right type
     * @return a {@link List} of all Right values
     */
    public static <B> List<B> rights(List<? extends Either<?, B>> list) {
        List<B> result = List.nil();
        for (Either<?, B> either : list) {
            if (either.isRight()) {
                result = result.plus(either.getRight());
            }
        }
        return result.reverse();
    }


    /**
     * Extracts the Right values from a {@link List} in a lazy fashion.
     * @param list a {@link List} of {@link Either} values
     * @param <B> Right type
     * @return a {@link List} of all Right values
     */
    public static <B> List<B> lazyRights(List<? extends Either<?, B>> list) throws StackOverflowError {
        if (list.isEmpty()) {
            return List.nil();
        } else {
            Either<?, B> head = list.head();
            return head.<List<B>>either(a -> lazyRights(list.tail()),
                    b -> List.newLazyList(b, () -> Either.<B>lazyRights(list.tail())));
        }
    }

    /**
     * Extracts the Left and Right values from a {@link List} of {@link Either} values.
     *
     * @param list a {@link List} of {@link Either} values
     * @param <A> Left type
     * @param <B> Right type
     * @return a {@link T2} pair of two lists containing the Left and Right values
     */
    public static <A, B> T2<List<A>, List<B>> split(List<Either<A, B>> list) {
        List<A> lefts = List.nil();
        List<B> rights = List.nil();
        for (Either<A, B> either : list) {
            if (either.isLeft()) {
                lefts = lefts.plus(either.getLeft());
            } else {
                rights = rights.plus(either.getRight());
            }
        }
        return T2.of(lefts.reverse(), rights.reverse());
    }

    /**
     * Generates a String representation.
     *
     * @return a String representation
     */
    @Override
    public String toString() {
        return either(Strings.format(SHOW_LEFT), Strings.format(SHOW_RIGHT));
    }

    /**
     * Calculates a hash code.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return either(x -> x.hashCode() * 31, y -> y.hashCode() * 47);
    }

    /**
     * Tests equality.
     *
     * @param obj the other object
     * @return true if both objects are equal
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Either) {
            Either<?, ?> that = (Either) obj;
            return this.maybeLeft().equals(that.maybeLeft()) &&
                    this.maybeRight().equals(that.maybeRight());
        }
        return false;
    }

    /**
     * Extract the value, if Left and Right have the same type
     *
     * @param either an {@link Either} object with same Left and Right type
     * @param <A> Left and Right type
     * @return the value extracted from Left or Right
     */
    public static <A> A unify(Either<A, A> either) {
        return either.either(Function.identity(), Function.identity());
    }

    /**
     * Generates the {@link Eq} instance of {@link Either}.
     *
     * @param eqA {@link Eq} instance for Left type
     * @param eqB {@link Eq} instance for Right type
     * @param <A> Left type
     * @param <B> Right type
     * @return the {@link Eq} instance
     */
    public static <A, B> Eq<Either<A, B>> eq(Eq<A> eqA, Eq<B> eqB) {
        return (one, two) -> {
            if (one == null && two == null) {
                return true;
            } else if (one == null || two == null) {
                return false;
            } else if (one.isLeft() && two.isLeft()) {
                return eqA.eq(one.getLeft(), two.getLeft());
            } else {
                return one.isRight() && two.isRight() && eqB.eq(one.getRight(), two.getRight());
            }
        };
    }

    /**
     * Generates an {@link Ord} instance for a given {@link Either} type.
     *
     * @param ordA the {@link Ord} for Left values
     * @param ordB the {@link Ord} for Right values
     * @param <A>  the Left type
     * @param <B>  the Right type
     * @return the {@link Ord} instance
     */
    public static <A, B> Ord<Either<A, B>> ord(Ord<A> ordA, Ord<B> ordB) {
        return (one, two) -> {
            if (one == null && two == null) {
                return Ordering.EQ;
            } else if (one == null) {
                return Ordering.LT;
            } else if (two == null) {
                return Ordering.GT;
            } else if (one.isLeft()) {
                return two.isRight() ? Ordering.LT : ordA.cmp(one.getLeft(), two.getLeft());
            } else {
                return two.isLeft() ? Ordering.GT : ordB.cmp(one.getRight(), two.getRight());
            }
        };
    }

    /**
     * The {@link Monad} type class of Either
     *
     * @param <S> the Left type of {@link Either}
     * @return the {@link Either} monad
     */
    public static <S> EitherMonad<S> monad() {
        return new EitherMonad<S>() {
        };
    }

    /**
     * The {@link MonadPlus} instance of {@link Either} which prefers the first Right value.
     *
     * @param monoid the {@link Monoid} used to combine Left values
     * @param <S>    the Left type of {@link Either}
     * @return the {@link MonadPlus} instance
     */
    public static <S> EitherMonadPlus<S> firstBiasedMonadPlus(Monoid<S> monoid) {
        return new EitherMonadPlus<S>() {
            @Override
            public Monoid monoid() {
                return monoid;
            }

            @Override
            public Bias bias() {
                return Bias.FIRST;
            }
        };
    }

    /**
     * The {@link MonadPlus} instance of {@link Either} which prefers the last Right value.
     *
     * @param monoid the {@link Monoid} used to combine Left values
     * @param <S>    the Left type of {@link Either}
     * @return the {@link MonadPlus} instance
     */
    public static <S> EitherMonadPlus<S> lastBiasedMonadPlus(Monoid<S> monoid) {
        return new EitherMonadPlus<S>(){
            @Override
            public Monoid monoid() {
                return monoid;
            }

            @Override
            public Bias bias() {
                return Bias.LAST;
            }
        };
    }

    /**
     * The {@link org.highj.typeclass2.bifunctor.Bifunctor} instance of {@link Either}.
     */
    public static final EitherBifunctor bifunctor = new EitherBifunctor() {
    };

    /**
     * The {@link org.highj.typeclass1.comonad.Extend} type class instance of {@link Either}.
     * @param <S> the Left type of {@link Either}
     * @return the {@link org.highj.typeclass1.comonad.Extend} instance
     */
    public static <S> EitherExtend<S> extend() {
        return new EitherExtend<S>() {
        };
    }
}
