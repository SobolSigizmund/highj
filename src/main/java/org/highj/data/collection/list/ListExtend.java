package org.highj.data.collection.list;

import org.derive4j.hkt.__;
import org.highj.data.collection.List;
import org.highj.typeclass1.comonad.Extend;

public interface  ListExtend extends ListFunctor, Extend<List.µ> {
    @Override
    default <A> List<__<List.µ, A>> duplicate(__<List.µ, A> nestedA) {
        //init . tails
        List<A> listA = List.narrow(nestedA);
        List<List<A>> result = listA.tailsLazy().initLazy();
        return List.<__<List.µ, A>,List<A>>contravariant(result);
    }
}
