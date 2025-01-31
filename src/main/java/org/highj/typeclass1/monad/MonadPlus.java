package org.highj.typeclass1.monad;

import org.derive4j.hkt.__;
import org.highj.data.collection.List;
import org.highj.typeclass1.alternative.Alternative;

public interface MonadPlus<M> extends MonadZero<M>, Alternative<M> {

    //MonadPlus.(++) (Control.Monad)
    public <A> __<M, A> mplus(__<M, A> one, __<M, A> two);


    //msum (Control.Monad)
    public default <A> __<M, A> msum(__<List.µ, __<M, A>> list) {
        return List.narrow(list).foldr((__<M, A> one) -> (__<M, A> two) -> mplus(one, two), this.<A>mzero());
    }

    enum Bias {
        FIRST {
            @Override
            public <T> T select(T first, T last) {
                return first;
            }
        }, LAST {
            @Override
            public <T> T select(T first, T last) {
                return last;
            }
        };

        public abstract <T> T select(T first, T last);
    }
}
