package org.highj.data.transformer.identity;

import org.derive4j.hkt.__;
import org.highj.data.functions.Functions;
import org.highj.data.transformer.IdentityT;
import org.highj.typeclass1.monad.Bind;

import java.util.function.Function;

public interface IdentityTBind<M> extends IdentityTApply<M>, Bind<__<IdentityT.µ, M>> {

    @Override
    public Bind<M> get();

    @Override
    public default <A, B> IdentityT<M, B> bind(__<__<IdentityT.µ, M>, A> nestedA, Function<A, __<__<IdentityT.µ, M>, B>> fn) {
        IdentityT<M, A> aId = IdentityT.narrow(nestedA);
        return new IdentityT<>(get().bind(aId.get(), Functions.compose(a -> IdentityT.<M, B>narrow(a).get(), fn)));
    }
}
