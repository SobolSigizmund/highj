package org.highj.data.collection.list;

import org.derive4j.hkt.__;
import org.highj.data.collection.List;
import org.highj.typeclass1.foldable.Traversable;
import org.highj.typeclass1.monad.Applicative;

import java.util.function.Function;
import static  org.highj.data.collection.List.*;

//Todo extends to Traversable1
public interface ListTraversable extends ListFunctor, Traversable<µ> {

    @Override
    default <A, B> List<B> map(final Function<A, B> fn, __<µ, A> nestedA) {
        return ListFunctor.super.map(fn,nestedA);
    }

    @Override
    default <A, B> B foldr(Function<A, Function<B, B>> fn, B b, __<µ, A> nestedA) {
        return narrow(nestedA).foldr(fn, b);
    }

    @Override
    default <A, B> A foldl(Function<A, Function<B, A>> fn, A a, __<µ, B> bs) {
        return narrow(bs).foldl(a, fn);
    }

    @Override
    default <A, B, X> __<X, __<µ, B>> traverse(Applicative<X> applicative, Function<A, __<X, B>> fn, __<µ, A> traversable) {
        //traverse f = Prelude.foldr cons_f (pure [])
        //  where cons_f x ys = (:) <$> f x <*> ys
        List<A> listA = narrow(traversable);
        __<µ, B> emptyB = List.<B>empty();
        return listA.foldr(a -> bs -> {
            __<X,Function<__<µ, B>, __<µ, B>>> mapF =
                    applicative.map(e -> es -> List.<B>narrow(es).plus(e), fn.apply(a));
            return applicative.ap(mapF, bs);
        }, applicative.pure(emptyB));
    }
}
