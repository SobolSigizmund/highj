package org.highj.data.tuple.t3;

import org.derive4j.hkt.__;
import org.highj.data.tuple.T3;
import org.highj.typeclass1.comonad.Comonad;

public interface T3Comonad<S,T> extends T3Functor<S,T>, Comonad<__<__<T3.µ, S>, T>> {

    @Override
    public default <A> T3<S, T, __<__<__<T3.µ, S>, T>, A>> duplicate(__<__<__<T3.µ, S>, T>, A> nestedA) {
        T3<S, T, A> triple = T3.narrow(nestedA);
        return T3.of(triple._1(), triple._2(), nestedA);
    }

    @Override
    public default <A> A extract(__<__<__<T3.µ, S>, T>, A> nestedA) {
        T3<S, T, A> triple = T3.narrow(nestedA);
        return triple._3();
    }
}
