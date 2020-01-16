package org.kucro3.parallelcraft.aopeng.asm.graph.manipulator;

import org.kucro3.parallelcraft.aopeng.asm.graph.Node;
import org.kucro3.parallelcraft.aopeng.asm.graph.Path;
import org.kucro3.parallelcraft.aopeng.asm.graph.manipulator.result.IntManipulationResult;
import org.kucro3.parallelcraft.aopeng.asm.graph.manipulator.result.ManipulationResult;

import javax.annotation.Nonnull;

/**
 * 头节点操作器。<br>
 * 头节点不允许连接任何的上路径。
 *
 * @see org.kucro3.parallelcraft.aopeng.asm.graph.manipulator.GraphNodeManipulator
 *
 * @param <T> 节点类型
 */
public class HeadGraphNodeManipulator<T extends Node<T>> extends AbstractGraphNodeManipulator<T> {
    @Override
    public @Nonnull ManipulationResult pushUpperPath(@Nonnull Path<T> path)
     {
         return RESULT_LINKING_UPPER_PATH;
     }

    @Override
    public int getUpperLimit()
    {
        return 0;
    }

    @Override
    public @Nonnull IntManipulationResult appendUpperPath(@Nonnull Path<T> path)
    {
        return INT_RESULT_LINKING_UPPER_PATH;
    }

    private static final String MESSAGE_LINKING_UPPER_PATH = "upper path not allowed for a head node";

    private static final IntManipulationResult INT_RESULT_LINKING_UPPER_PATH
            = IntManipulationResult.failed(MESSAGE_LINKING_UPPER_PATH, -1);

    private static final ManipulationResult RESULT_LINKING_UPPER_PATH
            = ManipulationResult.failed(MESSAGE_LINKING_UPPER_PATH);
}
