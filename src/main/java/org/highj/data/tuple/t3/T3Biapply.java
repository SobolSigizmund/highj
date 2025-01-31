package org.highj.data.tuple.t3;

import org.derive4j.hkt.__;
import org.derive4j.hkt.__2;
import org.highj.data.tuple.T3;
import org.highj.typeclass0.group.Semigroup;
import org.highj.typeclass2.bifunctor.Biapply;

import java.util.function.Function;

public interface T3Biapply<S> extends T3Bifunctor<S>, Biapply<__<T3.µ, S>> {

    public Semigroup<S> getS();

    @Override
    public default <A, B, C, D> T3<S, B, D> biapply(__2<__<T3.µ, S>, Function<A, B>, Function<C, D>> fn, __2<__<T3.µ, S>, A, C> ac) {
        T3<S, Function<A, B>, Function<C, D>> tripleFn = T3.narrow(fn);
        T3<S, A, C> tripleAc = T3.narrow(ac);
        return T3.of(getS().apply(tripleFn._1(), tripleAc._1()),
                tripleFn._2().apply(tripleAc._2()),
                tripleFn._3().apply(tripleAc._3()));
    }
}
