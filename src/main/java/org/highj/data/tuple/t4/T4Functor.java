package org.highj.data.tuple.t4;

import org.derive4j.hkt.__;
import org.highj.data.tuple.T4;
import org.highj.typeclass1.functor.Functor;

import java.util.function.Function;

public interface T4Functor<S,T,U> extends Functor<__<__<__<T4.µ,S>, T>, U>> {
    @Override
    public default <A, B> T4<S, T, U, B> map(Function<A, B> fn, __<__<__<__<T4.µ, S>, T>, U>, A> nestedA) {
        return T4.narrow(nestedA).map_4(fn);
    }
}
