package org.kucro3.parallelcraft.aopeng.asm.graph.manipulator;

import org.kucro3.parallelcraft.aopeng.asm.graph.Node;
import org.kucro3.parallelcraft.aopeng.asm.graph.Path;
import org.kucro3.parallelcraft.aopeng.asm.graph.manipulator.result.IntManipulationResult;
import org.kucro3.parallelcraft.aopeng.asm.graph.manipulator.result.ManipulationResult;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * 独立节点操作器。<br>
 * 独立节点不允许连接任何上路径与下路径。
 *
 * @see org.kucro3.parallelcraft.aopeng.asm.graph.manipulator.GraphNodeManipulator
 *
 * @param <T> 节点类型
 */
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
        return true;
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
    public @Nonnull ManipulationResult unlink()
    {
        return RESULT_UNLINKING;
    }

    @Override
    public @Nonnull ManipulationResult setUpperPath(int ordinal, @Nonnull Path<T> node)
    {
        return RESULT_LINKING;
    }

    @Override
    public @Nonnull ManipulationResult pushUpperPath(@Nonnull Path<T> node)
    {
        return RESULT_LINKING;
    }

    @Override
    public @Nonnull Path<T> popUpperPath()
    {
        throw new NoSuchElementException();
    }

    @Override
    public @Nonnull IntManipulationResult appendUpperPath(@Nonnull Path<T> path)
    {
        return INT_RESULT_LINKING;
    }

    @Override
    public @Nonnull Path<T> truncateUpperPath()
    {
        throw new NoSuchElementException();
    }

    @Override
    public void clearUpperPaths()
    {
    }

    @Override
    public @Nonnull ManipulationResult setLowerPath(int ordinal, @Nonnull Path<T> node)
    {
        return RESULT_LINKING;
    }

    @Override
    public @Nonnull ManipulationResult pushLowerPath(@Nonnull Path<T> node)
    {
        return RESULT_LINKING;
    }

    @Override
    public @Nonnull Path<T> popLowerPath()
    {
        throw new NoSuchElementException();
    }

    @Override
    public @Nonnull IntManipulationResult appendLowerPath(@Nonnull Path<T> path)
    {
        return INT_RESULT_LINKING;
    }

    @Override
    public @Nonnull Path<T> truncateLowerPath()
    {
        throw new NoSuchElementException();
    }

    @Override
    public void clearLowerPaths()
    {
    }

    private static final String MESSAGE_UNLINKING = "an isloated node cannot be unlinked";

    private static final String MESSAGE_LINKING = "any connection is not allowed for an isloated node";

    private static final ManipulationResult RESULT_UNLINKING = ManipulationResult.failed(MESSAGE_UNLINKING);

    private static final ManipulationResult RESULT_LINKING = ManipulationResult.failed(MESSAGE_LINKING);

    private static final IntManipulationResult INT_RESULT_LINKING = IntManipulationResult.failed(MESSAGE_LINKING, -1);
}
