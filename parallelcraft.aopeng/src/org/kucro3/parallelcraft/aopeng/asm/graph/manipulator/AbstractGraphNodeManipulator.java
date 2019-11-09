package org.kucro3.parallelcraft.aopeng.asm.graph.manipulator;

import org.kucro3.parallelcraft.aopeng.asm.graph.Node;
import org.kucro3.parallelcraft.aopeng.asm.graph.Path;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.*;

public abstract class AbstractGraphNodeManipulator<T extends Node<T>> implements GraphNodeManipulator<T> {
    @Override
    public @Nonnull List<Path<T>> getUpperPaths()
    {
        return Collections.unmodifiableList(upperPaths);
    }

    @Override
    public @Nonnull List<Path<T>> getLowerPaths()
    {
        return Collections.unmodifiableList(lowerPaths);
    }

    @Override
    public boolean unlink()
    {
        if (getUpperPathCount() == getLowerPathCount())
        {
            Iterator<Path<T>> upperPaths = getUpperPaths().iterator();
            Iterator<Path<T>> lowerPaths = getLowerPaths().iterator();

            while (upperPaths.hasNext())
            {
                Path<T> upperPath = upperPaths.next();
                Path<T> lowerPath = lowerPaths.next();

                // phantom attribute override
                upperPath.setPhantom(upperPath.isPhantom() || lowerPath.isPhantom());

                // use upper path as new path
                upperPath.setLowerNode(lowerPath.getLowerNode());
                upperPath.setLowerOrdinal(lowerPath.getLowerOrdinal());

                if (!lowerPath.getLowerNode().getManipulator().setUpperPath(lowerPath.getLowerOrdinal(), upperPath))
                    throw new IllegalStateException("Corrupt SRFGv1 graph, failed to unlink");
            }

            clearUpperPaths();
            clearLowerPaths();

            return true;
        }

        return false;
    }

    @Override
    public boolean setUpperPath(@Nonnegative int ordinal, @Nonnull Path<T> path)
    {
        Objects.requireNonNull(path);

        if (ordinal < 0)
            throw new IllegalArgumentException("ordinal cannot be negative");

        if (ordinal == upperPaths.size())
            return appendUpperPath(path) >= 0;
        else
            upperPaths.set(ordinal, path);

        return true;
    }

    @Override
    public void truncateUpperPath()
    {
        if (!upperPaths.isEmpty())
            upperPaths.removeLast();
    }

    @Override
    public void popUpperPath()
    {
        if (!upperPaths.isEmpty())
        {
            upperPaths.removeFirst();

            for (Path<T> pathAfter : upperPaths)
                pathAfter.decLowerOrdinal();
        }
    }

    @Override
    public void clearUpperPaths()
    {
        upperPaths.clear();
    }

    @Override
    public int appendUpperPath(@Nonnull Path<T> path)
    {
        Objects.requireNonNull(path);

        upperPaths.addLast(path);
        return upperPaths.size() - 1;
    }

    @Override
    public boolean pushUpperPath(@Nonnull Path<T> path)
    {
        Objects.requireNonNull(path);

        for (Path<T> pathAfter : upperPaths)
            pathAfter.incLowerOrdinal();

        upperPaths.addFirst(path);

        return true;
    }

    @Override
    public boolean setLowerPath(@Nonnegative int ordinal, @Nonnull Path<T> path)
    {
        Objects.requireNonNull(path);

        if (ordinal < 0)
            throw new IllegalArgumentException("ordinal cannot be negative");

        if (ordinal == lowerPaths.size())
            return appendLowerPath(path) >= 0;
        else
            lowerPaths.set(ordinal, path);

        return true;
    }

    @Override
    public void truncateLowerPath()
    {
        if (!lowerPaths.isEmpty())
            lowerPaths.removeLast();
    }

    @Override
    public void popLowerPath()
    {
         if (!lowerPaths.isEmpty())
         {
             lowerPaths.removeFirst();

             for (Path<T> pathAfter : lowerPaths)
                 pathAfter.decUpperOrdinal();
         }
    }

    @Override
    public void clearLowerPaths()
    {
        lowerPaths.clear();
    }

    @Override
    public int appendLowerPath(@Nonnull Path<T> path)
    {
        Objects.requireNonNull(path);

        lowerPaths.addLast(path);
        return lowerPaths.size() - 1;
    }

    @Override
    public boolean pushLowerPath(@Nonnull Path<T> path)
    {
        Objects.requireNonNull(path);

        for (Path<T> pathAfter : lowerPaths)
            pathAfter.incUpperOrdinal();

        lowerPaths.addFirst(path);

        return true;
    }

    protected T node;

    protected final LinkedList<Path<T>> upperPaths = new LinkedList<>();

    protected final LinkedList<Path<T>> lowerPaths = new LinkedList<>();
}
