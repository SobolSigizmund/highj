package org.highj.data.tuple.t4;

import org.derive4j.hkt.__;
import org.highj.data.tuple.T4;
import org.highj.typeclass0.group.Monoid;
import org.highj.typeclass1.monad.Applicative;

public interface T4Applicative<S,T,U> extends T4Apply<S,T,U>, Applicative<__<__<__<T4.µ,S>, T>, U>> {

    @Override
    public Monoid<S> getS();

    @Override
    public Monoid<T> getT();

    @Override
    public Monoid<U> getU();

    @Override
    public default <A> T4<S, T, U, A> pure(A a) {
        return T4.of(getS().identity(), getT().identity(), getU().identity(), a);
    }

}
