package org.highj.data.collection;

import org.derive4j.hkt.__;
import org.highj.data.collection.tree.TreeMonad;
import org.highj.data.functions.Functions;
import org.highj.data.functions.Strings;
import org.highj.data.tuple.T2;

import java.util.function.Function;
import java.util.function.Supplier;

public abstract class Tree<A> implements __<Tree.µ, A> {
    public static class µ {
    }

    public A rootLabel;

    public abstract List<Tree<A>> subForest();

    private Tree(A rootLabel) {
        this.rootLabel = rootLabel;
    }

    @SuppressWarnings("unchecked")
    public static <A> Tree<A> narrow(__<µ, A> nestedA) {
        return (Tree) nestedA;
    }

    public static <A> Tree<A> newTree(A rootLabel, List<Tree<A>> subForest) {
        return new Tree<A>(rootLabel) {
            @Override
            public List<Tree<A>> subForest() {
                return subForest;
            }
        };
    }

    @SafeVarargs
    public static <A> Tree<A> newTree(A rootLabel, Tree<A>... subForest) {
        return new Tree<A>(rootLabel) {
            @Override
            public List<Tree<A>> subForest() {
                return List.of(subForest);
            }
        };
    }

    public static <A> Tree<A> newLazyTree(A rootLabel, Supplier<List<Tree<A>>> supplier) {
        return new Tree<A>(rootLabel) {
            @Override
            public List<Tree<A>> subForest() {
                return supplier.get();
            }
        };
    }

    public List<A> flatten() {
        return List.newList(rootLabel, subForest().concatMap(t -> t.flatten()));
    }

    public List<List<A>> levels() {
        return Functions.iterate(ts -> ts.concatMap(t -> t.subForest()), List.of(this))
                .takeWhile((List<Tree<A>> ts) -> !ts.isEmpty())
                .map(xs -> xs.map(x -> x.rootLabel));
    }

    public String toString() {
        return "Tree(" + rootLabel + Strings.mkString(",[",",","])", subForest().map(Tree::toString));
    }

    public String drawTree() {
        return Strings.mkString("\n", draw());
    }

    private List<String> draw() {
        return List.newList(rootLabel.toString(), drawSubTrees(subForest()));
    }

    private static <A> List<String> drawSubTrees(List<Tree<A>> forest) {
        if (forest.isEmpty()) {
            return List.empty();
        } else if (forest.tail().isEmpty()) {
            return List.newList("|", shift("`- ", "   ", forest.head().draw()));
        } else {
            return List.append(List.newList("|", shift("+- ", "|  ", forest.head().draw())), drawSubTrees(forest.tail()));
        }
    }

    private static List<String> shift(String first, String other, List<String> strings) {
        return List.zipWith(List.newList(first, List.cycle(other)), strings, x -> y -> x + y);
    }

    public boolean equals(Object o) {
        if (o instanceof Tree) {
            Tree<?> that = (Tree) o;
            return this.rootLabel.equals(that.rootLabel) && this.subForest().equals(that.subForest());
        }
        return false;
    }

    public static <A,B> Tree<A> unfold(Function<B,T2<A,List<B>>> fn, B b) {
        T2<A,List<B>> pair = fn.apply(b);
        return newLazyTree(pair._1(), () -> unfoldForest(fn, pair._2()));
    }

    private static <A,B> List<Tree<A>> unfoldForest(Function<B,T2<A,List<B>>> fn, List<B> bs) {
        return bs.map(b -> unfold(fn,b));
    }

    public static final TreeMonad monad = new TreeMonad();

}
