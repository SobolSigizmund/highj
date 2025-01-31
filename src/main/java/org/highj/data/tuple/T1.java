package org.highj.data.tuple;

import org.derive4j.hkt.__;
import org.highj.data.collection.HList;
import org.highj.data.tuple.t1.T1Comonad;
import org.highj.data.tuple.t1.T1Monad;
import org.highj.typeclass0.compare.Eq;
import org.highj.typeclass0.compare.Ord;
import org.highj.typeclass0.group.Group;
import org.highj.typeclass0.group.Monoid;
import org.highj.typeclass0.group.Semigroup;

import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A tuple of arity 1, a.k.a. "cell" or "Id".
 */
public abstract class T1<A> implements __<T1.µ, A>, Supplier<A> {
    public static class µ {

    }

    @Override
    public A get() {
        return _1();
    }

    public abstract A _1();

    public static <A> T1<A> of(A a) {
        return new T1<A>() {
            @Override
            public A _1() {
                return a;
            }
        };
    }

    public static <A> T1<A> ofLazy(Supplier<A> thunkA) {
        return new T1<A>() {
            @Override
            public A _1() {
                return thunkA.get();
            }
        };
    }

    @SuppressWarnings("unchecked")
    public static <A> T1<A> narrow(__<µ, A> value) {
        return (T1) value;
    }

    @Override
    public String toString() {
        return String.format("(%s)", _1());
    }

    public <B> T1<B> map(Function<? super A, ? extends B> fn) {
        return of(fn.apply(_1()));
    }

    public <B> T1<B> ap(T1<Function<A, B>> nestedFn) {
        return map(narrow(nestedFn)._1());
    }

    public <B> T1<B> bind(Function<A, T1<B>> fn) {
        return fn.apply(_1());
    }

    @Override
    public int hashCode() {
        return _1().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof T1) {
            T1<?> that = (T1) o;
            return this._1().equals(that._1());
        }
        return false;
    }

    public static <A, B, C> T1<C> merge(T1<A> a, T1<B> b, Function<A, Function<B, C>> fn) {
        return new T1<C>() {
            @Override
            public C _1() {
                return fn.apply(a._1()).apply(b._1());
            }
        };
    }

    public static <A> Eq<T1<A>> eq(Eq<? super A> eqA) {
        return (one, two) -> eqA.eq(one._1(), two._1());
    }

    public static <A> Ord<T1<A>> ord(Ord<? super A> ordA) {
        return (one, two) -> ordA.cmp(one._1(), two._1());
    }

    public static final T1Monad monad = new T1Monad(){};
    public static final T1Comonad comonad = new T1Comonad(){};

    public static <A> Semigroup<T1<A>> semigroup(BinaryOperator<A> semigroupA) {
        return (x, y) -> T1.of(semigroupA.apply(x._1(), y._1()));
    }

    public static <A> Monoid<T1<A>> monoid(Monoid<A> monoidA) {
        return Monoid.create(T1.of(monoidA.identity()),
                (x, y) -> T1.of(monoidA.apply(x._1(), y._1())));
    }

    public static <A> Group<T1<A>> group(Group<A> groupA) {
        return Group.create(T1.of(groupA.identity()),
                (x, y) -> T1.of(groupA.apply(x._1(), y._1())),
                z -> T1.of(groupA.inverse(z._1())));
    }

    public HList.HCons<A, HList.HNil> toHlist() {
        return HList.single(_1());
    }

}
