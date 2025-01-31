package org.highj.typeclass1.monad;

import org.highj.data.tuple.T0;
import org.derive4j.hkt.__;
import org.highj.data.collection.Maybe;
import org.highj.typeclass1.functor.Functor;
import org.junit.Test;

import java.util.function.Function;

import static junit.framework.Assert.assertEquals;
import static org.highj.data.collection.Maybe.*;

public class FunctorTest {

    private final Functor<µ> functor = monad;

    @Test
    public void testLeft$() {
        //3 <$ Just "x"
        //-- Just 3
        Maybe<String> justX = newJust("x");
        Maybe<Integer> three = narrow(functor.left$(3, justX));
        assertEquals("Just(3)", three.toString());

        //3 <$ Nothing
        //-- Nothing
        Maybe<String> nothingString = newNothing();
        Maybe<Integer> nothingInt = narrow(functor.left$(3, nothingString));
        assertEquals("Nothing", nothingInt.toString());
    }

    @Test
    public void testVoidF() {
        //void $ Just "x"
        //-- Just ()
        Maybe<String> justX = newJust("x");
        Maybe<T0> justUnit = narrow(functor.voidF(justX));
        assertEquals("Just(())", justUnit.toString());

        //void $ Nothing
        //-- Nothing
        Maybe<String> nothingString = newNothing();
        Maybe<T0> nothingUnit = narrow(functor.voidF(nothingString));
        assertEquals("Nothing", nothingUnit.toString());
    }

    @Test
    public void testFlip() {
        //flip (Just length) "foo"
        //-- Just 3
        Maybe<Function<String, Integer>> justStringLength = newJust(String::length);
        Maybe<Integer> justThree = narrow(functor.flip(justStringLength, "foo"));
        assertEquals("Just(3)", justThree.toString());
        
        //flip Nothing "foo"
        //-- Nothing
        Maybe<Function<String, Integer>> nothingFn = newNothing();
        Maybe<Integer> nothingInt = narrow(functor.flip(nothingFn, "foo"));
        assertEquals("Nothing", nothingInt.toString());
    }
    
    @Test
    public void testLift() {
        Function<__<µ,String>,__<µ,Integer>> liftedFn = functor.lift(String::length);

        //liftM length $ Just "foo"
        //-- Just 3
        Maybe<String> justString = newJust("foo");
        Maybe<Integer> justThree = narrow(liftedFn.apply(justString));
        assertEquals("Just(3)", justThree.toString());

        //liftM length Nothing
        //-- Nothing
        Maybe<String> nothingString = newNothing();
        Maybe<Integer> nothingInt = narrow(liftedFn.apply(nothingString));
        assertEquals("Nothing", nothingInt.toString());
    }
}
