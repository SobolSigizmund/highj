package org.highj.data.transformer.writer;

import org.derive4j.hkt.__;
import org.highj.data.transformer.WriterT;
import org.highj.data.tuple.T0;
import org.highj.data.tuple.T2;
import org.highj.typeclass1.monad.Monad;
import org.highj.typeclass1.monad.MonadWriter;

import java.util.function.Function;

/**
 * @author Clinton Selke
 */
public interface WriterTMonadWriter<W, M> extends WriterTMonad<W, M>, MonadWriter<W, __<__<WriterT.µ, W>, M>> {

    @Override
    public Monad<M> get();

    @Override
    public default WriterT<W, M, T0> tell(W w) {
        return () -> get().pure(T2.of(T0.of(), w));
    }

    @Override
    public default <A> WriterT<W, M, T2<A, W>> listen(__<__<__<WriterT.µ, W>, M>, A> nestedA) {
        return () -> get().map(
                (T2<A, W> x) -> T2.of(x, x._2()),
                WriterT.narrow(nestedA).run()
        );
    }

    @Override
    public default <A> WriterT<W, M, A> pass(__<__<__<WriterT.µ, W>, M>, T2<A, Function<W, W>>> m) {
        return () -> get().map(
                (T2<T2<A, Function<W, W>>, W> x) -> T2.of(x._1()._1(), x._1()._2().apply(x._2())),
                WriterT.narrow(m).run()
        );
    }
}
