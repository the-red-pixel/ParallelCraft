package org.kucro3.parallelcraft.aopeng.asm.graph.manipulator;

import com.theredpixelteam.redtea.util.Predication;
import org.kucro3.parallelcraft.aopeng.asm.graph.Node;
import org.kucro3.parallelcraft.aopeng.asm.graph.Path;
import org.kucro3.parallelcraft.aopeng.asm.graph.manipulator.result.IntManipulationResult;
import org.kucro3.parallelcraft.aopeng.asm.graph.manipulator.result.ManipulationResult;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

/**
 * 有上路径限制的节点操作器。<br>
 * 对节点连接的上路径数量进行限制。
 *
 * @param <T> 节点类型
 */
public class UpperLimitedGraphNodeManipulator<T extends Node<T>> extends AbstractGraphNodeManipulator<T> {
    /**
     * 构造函数。
     *
     * @param limit 上路径最大数量
     *
     * @throws IllegalArgumentException 若 limit 为负数则抛出此错误
     */
    public UpperLimitedGraphNodeManipulator(@Nonnegative int limit)
    {
        this.limit = Predication.requireNonNegative(limit);
    }

    @Override
    public boolean hasUpperLimit()
    {
        return true;
    }

    @Override
    public int getUpperLimit()
    {
        return limit;
    }

    @Override
    public @Nonnull IntManipulationResult appendUpperPath(@Nonnull Path<T> path)
    {
        if (getUpperPathCount() < limit)
            return super.appendUpperPath(path);

        return LimitedGraphNodeManipulator.INT_RESULT_UPPER_LIMIT_EXCEEDED;
    }

    @Override
    public @Nonnull ManipulationResult pushUpperPath(@Nonnull Path<T> path)
    {
        if (getUpperPathCount() < limit)
            return super.pushUpperPath(path);

        return LimitedGraphNodeManipulator.RESULT_UPPER_LIMIT_EXCEEDED;
    }

    private final int limit;
}
