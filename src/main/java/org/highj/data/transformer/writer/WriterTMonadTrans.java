package org.highj.data.transformer.writer;

import org.derive4j.hkt.__;
import org.highj.data.transformer.WriterT;
import org.highj.data.tuple.T2;
import org.highj.typeclass1.monad.Monad;
import org.highj.typeclass1.monad.MonadTrans;

/**
 * @author Clinton Selke
 */
public interface WriterTMonadTrans<W, M> extends WriterTMonad<W, M>, MonadTrans<__<WriterT.µ, W>, M> {

    @Override
    public Monad<M> get();

    @Override
    public default <A> WriterT<W, M, A> lift(__<M, A> nestedA) {
        return () -> get().map((A a) -> T2.of(a, wMonoid().identity()), nestedA);
    }
}