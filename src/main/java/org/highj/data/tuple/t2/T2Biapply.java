package org.highj.data.tuple.t2;

import org.derive4j.hkt.__2;
import org.highj.data.tuple.T2;
import org.highj.typeclass2.bifunctor.Biapply;

import java.util.function.Function;

public interface T2Biapply extends T2Bifunctor, Biapply<T2.µ> {

    @Override
    public default <A, B, C, D> T2<B, D> biapply(__2<T2.µ, Function<A, B>, Function<C, D>> fn, __2<T2.µ, A, C> ac) {
        T2<Function<A, B>, Function<C, D>> fnPair = T2.narrow(fn);
        return bimap(fnPair._1(), fnPair._2(), ac);
    }
}
