package org.highj.data.structural.constant;

import org.derive4j.hkt.__;
import org.highj.data.structural.Const;
import org.highj.typeclass1.contravariant.Contravariant;

import java.util.function.Function;

import static org.highj.data.structural.Const.narrow;
import static org.highj.data.structural.Const.µ;

public interface ConstContravariant<S> extends Contravariant<__<µ,S>> {

    @Override
    public default <A, B> Const<S, A> contramap(Function<A, B> fn,__<__<µ, S>, B> nestedB) {
        //contramap __ (Const a) = Const a
        Const<S,B> constB = narrow(nestedB);
        return new Const<>(constB.get());
    }
}
