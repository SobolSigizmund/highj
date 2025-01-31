package org.highj.data.collection.maybe;

import org.derive4j.hkt.__;
import org.highj.data.collection.Maybe;
import org.highj.typeclass1.comonad.Extend;

public interface MaybeExtend extends MaybeFunctor, Extend<Maybe.µ> {
    @Override
    default <A> Maybe<__<Maybe.µ, A>> duplicate(__<Maybe.µ, A> nestedA) {
        return Maybe.narrow(nestedA).map(Maybe::newJust);
    }
}
