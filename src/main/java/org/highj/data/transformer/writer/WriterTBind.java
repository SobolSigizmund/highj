package org.highj.data.transformer.writer;

import org.derive4j.hkt.__;
import org.highj.data.transformer.WriterT;
import org.highj.data.tuple.T2;
import org.highj.typeclass1.monad.Bind;

import java.util.function.Function;

/**
 * @author Clinton Selke
 */
public interface WriterTBind<W, M> extends WriterTApply<W, M>, Bind<__<__<WriterT.µ, W>, M>> {

    public Bind<M> get();

    @Override
    public default <A, B> WriterT<W, M, B> bind(__<__<__<WriterT.µ, W>, M>, A> nestedA, Function<A, __<__<__<WriterT.µ, W>, M>, B>> fn) {
        return () -> get().bind(
                WriterT.narrow(nestedA).run(),
                (T2<A, W> x1) -> get().map(
                        (T2<B, W> x2) -> T2.of(x2._1(), wSemigroup().apply(x1._2(), x2._2())),
                        WriterT.narrow(fn.apply(x1._1())).run()
                )
        );
    }
}