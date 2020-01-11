package org.kucro3.parallelcraft.aopeng.asm.graph.manipulator;

import org.kucro3.parallelcraft.aopeng.asm.graph.Node;
import org.kucro3.parallelcraft.aopeng.asm.graph.Path;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.List;

@SuppressWarnings("BooleanMethodIsAlwaysInverted")
public interface GraphNodeManipulator<T extends Node<T>> {
    public default boolean hasUpperLimit()
    {
        return false;
    }

    public default int getUpperLimit()
    {
        return -1;
    }

    public @Nonnull List<Path<T>> getUpperPaths();

    public default @Nonnull Path<T> getUpperPath(int index)
    {
        return getUpperPaths().get(index);
    }

    public default @Nonnull Path<T> peekUpperPath()
    {
        return getUpperPath(0);
    }

    public default @Nonnull T peekUpperNode()
    {
        return peekUpperPath().getUpperNode();
    }

    public default int getUpperPathCount()
    {
        return getUpperPaths().size();
    }

    public default boolean hasUpperPath()
    {
        return getUpperPathCount() > 0;
    }

    public default boolean hasLowerLimit()
    {
        return false;
    }

    public default int getLowerLimit()
    {
        return -1;
    }

    public @Nonnull List<Path<T>> getLowerPaths();

    public default @Nonnull Path<T> getLowerPath(int index)
    {
        return getLowerPaths().get(index);
    }

    public default @Nonnull Path<T> peekLowerPath()
    {
        return getLowerPath(0);
    }

    public default @Nonnull T peekLowerNode()
    {
        return peekLowerPath().getLowerNode();
    }

    public default int getLowerPathCount()
    {
        return getLowerPaths().size();
    }

    public default boolean hasLowerPath()
    {
        return getLowerPathCount() > 0;
    }

    public boolean unlink();

    public boolean setUpperPath(@Nonnegative int ordinal, @Nonnull Path<T> path);

    // add the path to the head
    public boolean pushUpperPath(@Nonnull Path<T> path);

    // remove the first path
    public void popUpperPath();

    // add the path to the tail
    public int appendUpperPath(@Nonnull Path<T> path);

    // remove the last path
    public void truncateUpperPath();

    public void clearUpperPaths();

    public boolean setLowerPath(@Nonnegative int ordinal, @Nonnull Path<T> path);

    // add the path to the head
    public boolean pushLowerPath(@Nonnull Path<T> path);

    // remove the first path
    public void popLowerPath();

    // add the path to the tail
    public int appendLowerPath(@Nonnull Path<T> path);

    // remove the last path
    public void truncateLowerPath();

    public void clearLowerPaths();
}
