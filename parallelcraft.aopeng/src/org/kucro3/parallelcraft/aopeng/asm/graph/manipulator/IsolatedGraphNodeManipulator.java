package org.kucro3.parallelcraft.aopeng.asm.graph.manipulator;

import org.kucro3.parallelcraft.aopeng.asm.graph.Node;
import org.kucro3.parallelcraft.aopeng.asm.graph.Path;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public class IsolatedGraphNodeManipulator<T extends Node<T>> implements GraphNodeManipulator<T> {
    @Override
    public boolean hasLowerLimit()
    {
        return true;
    }

    @Override
    public int getLowerLimit()
    {
        return 0;
    }

    @Override
    public @Nonnull List<Path<T>> getLowerPaths()
    {
        return Collections.emptyList();
    }

    @Override
    public boolean hasUpperLimit()
    {
        return false;
    }

    @Override
    public int getUpperLimit()
    {
        return 0;
    }

    @Override
    public @Nonnull List<Path<T>> getUpperPaths()
    {
        return Collections.emptyList();
    }

    @Override
    public boolean unlink()
    {
        return false;
    }

    @Override
    public boolean setUpperPath(int ordinal, @Nonnull Path node)
    {
        return false;
    }

    @Override
    public boolean pushUpperPath(@Nonnull Path<T> node)
    {
        return false;
    }

    @Override
    public void popUpperPath()
    {
    }

    @Override
    public int appendUpperPath(@Nonnull Path<T> path)
    {
        return -1;
    }

    @Override
    public void truncateUpperPath()
    {
    }

    @Override
    public void clearUpperPaths()
    {
    }

    @Override
    public boolean setLowerPath(int ordinal, @Nonnull Path<T> node)
    {
        return false;
    }

    @Override
    public boolean pushLowerPath(@Nonnull Path<T> node)
    {
        return false;
    }

    @Override
    public void popLowerPath()
    {
    }

    @Override
    public int appendLowerPath(@Nonnull Path<T> path)
    {
        return -1;
    }

    @Override
    public void truncateLowerPath()
    {
    }

    @Override
    public void clearLowerPaths()
    {
    }
}
