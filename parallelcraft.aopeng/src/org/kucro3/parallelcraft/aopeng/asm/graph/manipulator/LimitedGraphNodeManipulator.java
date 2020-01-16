package org.kucro3.parallelcraft.aopeng.asm.graph.manipulator;

import org.kucro3.parallelcraft.aopeng.asm.graph.Node;
import org.kucro3.parallelcraft.aopeng.asm.graph.Path;
import org.kucro3.parallelcraft.aopeng.asm.graph.manipulator.result.IntManipulationResult;
import org.kucro3.parallelcraft.aopeng.asm.graph.manipulator.result.ManipulationResult;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

/**
 * 有限制的节点操作器。<br>
 * 对节点连接的上路径数量与下路径数量进行限制。
 *
 * @see org.kucro3.parallelcraft.aopeng.asm.graph.manipulator.GraphNodeManipulator
 *
 * @param <T> 节点类型
 */
public class LimitedGraphNodeManipulator<T extends Node<T>> extends AbstractGraphNodeManipulator<T> {
    public LimitedGraphNodeManipulator(@Nonnegative int upperLimit,
                                       @Nonnegative int lowerLimit)
    {
        this.upperLimit = upperLimit;
        this.lowerLimit = lowerLimit;
    }

    @Override
    public boolean hasUpperLimit()
    {
        return true;
    }

    @Override
    public int getUpperLimit()
    {
        return upperLimit;
    }

    @Override
    public @Nonnull IntManipulationResult appendUpperPath(@Nonnull Path<T> path)
    {
        if (getUpperPathCount() < upperLimit)
            return super.appendUpperPath(path);

        return INT_RESULT_UPPER_LIMIT_EXCEEDED;
    }

    @Override
    public @Nonnull ManipulationResult pushUpperPath(@Nonnull Path<T> path)
    {
        if (getUpperPathCount() < upperLimit)
            return super.pushUpperPath(path);

        return RESULT_UPPER_LIMIT_EXCEEDED;
    }

    @Override
    public boolean hasLowerLimit()
    {
        return true;
    }

    @Override
    public int getLowerLimit()
    {
        return lowerLimit;
    }

    @Override
    public @Nonnull IntManipulationResult appendLowerPath(@Nonnull Path<T> path)
    {
        if (getLowerPathCount() < lowerLimit)
            return super.appendLowerPath(path);

        return INT_RESULT_LOWER_LIMIT_EXCEEDED;
    }

    @Override
    public @Nonnull ManipulationResult pushLowerPath(@Nonnull Path<T> path)
    {
        if (getLowerPathCount() < lowerLimit)
            return super.pushLowerPath(path);

        return RESULT_LOWER_LIMIT_EXCEEDED;
    }

    private final int upperLimit;

    private final int lowerLimit;

    private static final String MESSAGE_UPPER_LIMIT_EXCEEDED = "upper limit exceeded";

    private static final String MESSAGE_LOWER_LIMIT_EXCEEDED = "lower limit exceeded";

    static final ManipulationResult RESULT_UPPER_LIMIT_EXCEEDED =
            ManipulationResult.failed(MESSAGE_UPPER_LIMIT_EXCEEDED);

    static final ManipulationResult RESULT_LOWER_LIMIT_EXCEEDED =
            ManipulationResult.failed(MESSAGE_LOWER_LIMIT_EXCEEDED);

    static final IntManipulationResult INT_RESULT_UPPER_LIMIT_EXCEEDED =
            IntManipulationResult.failed(MESSAGE_UPPER_LIMIT_EXCEEDED, -1);

    static final IntManipulationResult INT_RESULT_LOWER_LIMIT_EXCEEDED =
            IntManipulationResult.failed(MESSAGE_LOWER_LIMIT_EXCEEDED, -1);
}
