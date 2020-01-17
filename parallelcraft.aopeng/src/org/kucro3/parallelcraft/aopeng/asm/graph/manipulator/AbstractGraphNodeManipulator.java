package org.kucro3.parallelcraft.aopeng.asm.graph.manipulator;

import com.theredpixelteam.redtea.util.Predication;
import org.kucro3.parallelcraft.aopeng.asm.graph.Node;
import org.kucro3.parallelcraft.aopeng.asm.graph.Path;
import org.kucro3.parallelcraft.aopeng.asm.graph.manipulator.result.IntManipulationResult;
import org.kucro3.parallelcraft.aopeng.asm.graph.manipulator.result.ManipulationResult;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.*;

/**
 * 抽象类。提供一些基本的节点管理操作。
 *
 * @see org.kucro3.parallelcraft.aopeng.asm.graph.manipulator.GraphNodeManipulator
 *
 * @param <T> 节点类型
 */
@SuppressWarnings("ConstantConditions")
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
    public @Nonnull ManipulationResult unlink()
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

                ManipulationResult result;
                if (!(result = lowerPath.getLowerNode().getManipulator().setUpperPath(lowerPath.getLowerOrdinal(), upperPath))
                        .isPassed())
                    throw new IllegalStateException("Failed to unlink: " + result.requireMessage());
            }

            clearUpperPaths();
            clearLowerPaths();

            return ManipulationResult.passed();
        }

        return ManipulationResult.failed("different upper and lower count");
    }

    @Override
    public @Nonnull ManipulationResult setUpperPath(@Nonnegative int ordinal, @Nonnull Path<T> path)
    {
        Predication.requireNonNull(path);

        if (ordinal < 0)
            throw new IllegalArgumentException("ordinal cannot be negative");

        if (ordinal == upperPaths.size())
            return appendUpperPath(path);
        else
            upperPaths.set(ordinal, path);

        return ManipulationResult.passed();
    }

    @Override
    public @Nonnull Path<T> truncateUpperPath()
    {
        return upperPaths.removeLast();
    }

    @Override
    public @Nonnull Path<T> popUpperPath()
    {
        Path<T> removed = upperPaths.removeFirst();

        for (Path<T> pathAfter : upperPaths)
            pathAfter.decLowerOrdinal();

        return removed;
    }

    @Override
    public void clearUpperPaths()
    {
        upperPaths.clear();
    }

    @Override
    public @Nonnull IntManipulationResult appendUpperPath(@Nonnull Path<T> path)
    {
        Predication.requireNonNull(path);

        upperPaths.addLast(path);
        return IntManipulationResult.passed(upperPaths.size() - 1);
    }

    @Override
    public @Nonnull ManipulationResult pushUpperPath(@Nonnull Path<T> path)
    {
        Predication.requireNonNull(path);

        for (Path<T> pathAfter : upperPaths)
            pathAfter.incLowerOrdinal();

        upperPaths.addFirst(path);

        return ManipulationResult.passed();
    }

    @Override
    public @Nonnull ManipulationResult setLowerPath(@Nonnegative int ordinal, @Nonnull Path<T> path)
    {
        Predication.requireNonNull(path);

        if (ordinal < 0)
            throw new IllegalArgumentException("ordinal cannot be negative");

        if (ordinal == lowerPaths.size())
            return appendLowerPath(path);
        else
            lowerPaths.set(ordinal, path);

        return ManipulationResult.passed();
    }

    @Override
    public @Nonnull Path<T> truncateLowerPath()
    {
        return lowerPaths.removeLast();
    }

    @Override
    public @Nonnull Path<T> popLowerPath()
    {
        Path<T> path = lowerPaths.removeFirst();

        for (Path<T> pathAfter : lowerPaths)
            pathAfter.decUpperOrdinal();

        return path;
    }

    @Override
    public void clearLowerPaths()
    {
        lowerPaths.clear();
    }

    @Override
    public @Nonnull IntManipulationResult appendLowerPath(@Nonnull Path<T> path)
    {
        Predication.requireNonNull(path);

        lowerPaths.addLast(path);
        return IntManipulationResult.passed(lowerPaths.size() - 1);
    }

    @Override
    public @Nonnull ManipulationResult pushLowerPath(@Nonnull Path<T> path)
    {
        Predication.requireNonNull(path);

        for (Path<T> pathAfter : lowerPaths)
            pathAfter.incUpperOrdinal();

        lowerPaths.addFirst(path);

        return ManipulationResult.passed();
    }

    protected final LinkedList<Path<T>> upperPaths = new LinkedList<>();

    protected final LinkedList<Path<T>> lowerPaths = new LinkedList<>();
}
