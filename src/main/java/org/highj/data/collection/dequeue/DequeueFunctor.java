package org.highj.data.collection.dequeue;

import org.derive4j.hkt.__;
import org.highj.data.collection.Dequeue;
import org.highj.typeclass1.functor.Functor;

import java.util.function.Function;

public interface DequeueFunctor extends Functor<Dequeue.µ> {
    @Override
    default <A, B> Dequeue<B> map(final Function<A, B> fn, __<Dequeue.µ, A> nestedA) {
        return Dequeue.narrow(nestedA).map(fn);
    }
}
