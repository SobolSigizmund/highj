package org.highj.data.transformer.writer;

import org.derive4j.hkt.__;
import org.highj.data.transformer.WriterT;
import org.highj.data.tuple.T2;
import org.highj.typeclass1.functor.Functor;

import java.util.function.Function;

/**
 * @author Cinton Selke
 */
public interface WriterTFunctor<W, M> extends Functor<__<__<WriterT.µ, W>, M>> {

    public Functor<M> get();

    @Override
    public default <A, B> WriterT<W, M, B> map(Function<A, B> fn, __<__<__<WriterT.µ, W>, M>, A> nestedA) {
        return () -> get().map(
                (T2<A, W> x) -> T2.of(fn.apply(x._1()), x._2()),
                WriterT.narrow(nestedA).run()
        );
    }
}
