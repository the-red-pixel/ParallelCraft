package org.kucro3.parallelcraft.aopeng.asm.graph.manipulator;

import org.kucro3.parallelcraft.aopeng.asm.graph.Node;
import org.kucro3.parallelcraft.aopeng.asm.graph.Path;
import org.kucro3.parallelcraft.aopeng.asm.graph.manipulator.result.IntManipulationResult;
import org.kucro3.parallelcraft.aopeng.asm.graph.manipulator.result.ManipulationResult;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

/**
 * 有下路径限制的节点操作器。<br>
 * 对节点连接的下路径数量进行限制。
 *
 * @param <T> 节点类型
 */
public class LowerLimitedGraphNodeManipulator<T extends Node<T>> extends AbstractGraphNodeManipulator<T> {
    public LowerLimitedGraphNodeManipulator(@Nonnegative int limit)
    {
        this.limit = limit;
    }

    @Override
    public boolean hasLowerLimit()
    {
        return true;
    }

    @Override
    public int getLowerLimit()
    {
        return limit;
    }

    @Override
    public @Nonnull IntManipulationResult appendLowerPath(@Nonnull Path<T> path)
    {
        if (getLowerPathCount() < limit)
            return super.appendLowerPath(path);

        return LimitedGraphNodeManipulator.INT_RESULT_LOWER_LIMIT_EXCEEDED;
    }

    @Override
    public @Nonnull ManipulationResult pushLowerPath(@Nonnull Path<T> path)
    {
        if (getLowerPathCount() < limit)
            return super.pushLowerPath(path);

        return LimitedGraphNodeManipulator.RESULT_LOWER_LIMIT_EXCEEDED;
    }

    private final int limit;
}
