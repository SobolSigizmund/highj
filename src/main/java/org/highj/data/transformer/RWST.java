/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.highj.data.transformer;

import org.derive4j.hkt.__;
import org.derive4j.hkt.__5;
import org.highj.data.transformer.rws.RWSTApplicative;
import org.highj.data.transformer.rws.RWSTApply;
import org.highj.data.transformer.rws.RWSTBind;
import org.highj.data.transformer.rws.RWSTFunctor;
import org.highj.data.transformer.rws.RWSTMonad;
import org.highj.data.transformer.rws.RWSTMonadRWS;
import org.highj.data.transformer.rws.RWSTMonadReader;
import org.highj.data.transformer.rws.RWSTMonadRec;
import org.highj.data.transformer.rws.RWSTMonadState;
import org.highj.data.transformer.rws.RWSTMonadTrans;
import org.highj.data.transformer.rws.RWSTMonadWriter;
import org.highj.data.tuple.T3;
import org.highj.typeclass0.group.Monoid;
import org.highj.typeclass0.group.Semigroup;
import org.highj.typeclass1.functor.Functor;
import org.highj.typeclass1.monad.Bind;
import org.highj.typeclass1.monad.Monad;
import org.highj.typeclass1.monad.MonadRec;

/**
 *
 * @author clintonselke
 */
public interface RWST<R,W,S,M,A> extends __5<RWST.µ,R,W,S,M,A> {
    public static class µ {}

    public static <R,W,S,M,A> RWST<R,W,S,M,A> narrow(__5<µ,R,W,S,M,A> a) {
        return (RWST<R,W,S,M,A>)a;
    }

    public static <R,W,S,M,A> RWST<R,W,S,M,A> narrow(__<__<__<__<__<µ, R>, W>, S>, M>, A> a) {
        return (RWST<R,W,S,M,A>)a;
    }

    public __<M,T3<A,S,W>> run(R r, S s);

    public static <R,W,S,M> RWSTFunctor<R,W,S,M> functor(Functor<M> mFunctor) {
        return (RWSTFunctor<R,W,S,M>)() -> mFunctor;
    }

    public static <R,W,S,M> RWSTApply<R,W,S,M> apply(Bind<M> mBind, Semigroup<W> wSemigroup) {
        return new RWSTApply<R,W,S,M>() {
            @Override
            public Bind<M> m() {
                return mBind;
            }
            @Override
            public Semigroup<W> w() {
                return wSemigroup;
            }
        };
    }

    public static <R,W,S,M> RWSTApplicative<R,W,S,M> applicative(Monad<M> mMonad, Monoid<W> wMonoid) {
        return new RWSTApplicative<R,W,S,M>() {
            @Override
            public Monad<M> m() {
                return mMonad;
            }
            @Override
            public Monoid<W> w() {
                return wMonoid;
            }
        };
    }
    
    public static <R,W,S,M> RWSTBind<R,W,S,M> bind(Monad<M> mMonad, Semigroup<W> wSemigroup) {
        return new RWSTBind<R,W,S,M>() {
            @Override
            public Bind<M> m() {
                return mMonad;
            }
            @Override
            public Semigroup<W> w() {
                return wSemigroup;
            }
        };
    }

    public static <R,W,S,M> RWSTMonad<R,W,S,M> monad(Monad<M> mMonad, Monoid<W> wMonoid) {
        return new RWSTMonad<R,W,S,M>() {
            @Override
            public Monad<M> m() {
                return mMonad;
            }
            @Override
            public Monoid<W> w() {
                return wMonoid;
            }
        };
    }

    public static <R,W,S,M> RWSTMonadReader<R,W,S,M> monadReader(Monad<M> mMonad, Monoid<W> wMonoid) {
        return new RWSTMonadReader<R,W,S,M>() {
            @Override
            public Monad<M> m() {
                return mMonad;
            }
            @Override
            public Monoid<W> w() {
                return wMonoid;
            }
        };
    }

    public static <R,W,S,M> RWSTMonadWriter<R,W,S,M> monadWriter(Monad<M> mMonad, Monoid<W> wMonoid) {
        return new RWSTMonadWriter<R,W,S,M>() {
            @Override
            public Monad<M> m() {
                return mMonad;
            }
            @Override
            public Monoid<W> w() {
                return wMonoid;
            }
        };
    }

    public static <R,W,S,M> RWSTMonadState<R,W,S,M> monadState(Monad<M> mMonad, Monoid<W> wMonoid) {
        return new RWSTMonadState<R,W,S,M>() {
            @Override
            public Monad<M> m() {
                return mMonad;
            }
            @Override
            public Monoid<W> w() {
                return wMonoid;
            }
        };
    }
    
    public static <R,W,S,M> RWSTMonadRWS<R,W,S,M> monadRWS(Monad<M> mMonad, Monoid<W> wMonoid) {
        return new RWSTMonadRWS<R,W,S,M>() {
            @Override
            public Monad<M> m() {
                return mMonad;
            }
            @Override
            public Monoid<W> w() {
                return wMonoid;
            }
        };
    }

    public static <R,W,S,M> RWSTMonadTrans<R,W,S,M> monadTrans(Monad<M> mMonad, Monoid<W> wMonoid) {
        return new RWSTMonadTrans<R,W,S,M>() {
            @Override
            public Monad<M> m() {
                return mMonad;
            }
            @Override
            public Monoid<W> w() {
                return wMonoid;
            }
        };
    }
    
    public static <R,W,S,M> RWSTMonadRec<R,W,S,M> monadRec(MonadRec<M> mMonadRec, Monoid<W> wMonoid) {
        return new RWSTMonadRec<R,W,S,M>() {
            @Override
            public MonadRec<M> m() {
                return mMonadRec;
            }
            @Override
            public Monoid<W> w() {
                return wMonoid;
            }
        };
    }
}
