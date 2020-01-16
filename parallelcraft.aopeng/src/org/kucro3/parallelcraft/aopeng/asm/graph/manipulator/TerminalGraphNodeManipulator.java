package org.kucro3.parallelcraft.aopeng.asm.graph.manipulator;

import org.kucro3.parallelcraft.aopeng.asm.graph.Node;
import org.kucro3.parallelcraft.aopeng.asm.graph.Path;
import org.kucro3.parallelcraft.aopeng.asm.graph.manipulator.result.IntManipulationResult;
import org.kucro3.parallelcraft.aopeng.asm.graph.manipulator.result.ManipulationResult;

import javax.annotation.Nonnull;

/**
 * 终端节点操作器。<br>
 * 终端节点不允许连接任何的下路径。
 *
 * @param <T> 节点类型
 */
public class TerminalGraphNodeManipulator<T extends Node<T>> extends AbstractGraphNodeManipulator<T> {
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
    public @Nonnull IntManipulationResult appendLowerPath(@Nonnull Path<T> path)
    {
        return INT_RESULT_LINKING;
    }

    @Override
    public @Nonnull ManipulationResult pushLowerPath(@Nonnull Path<T> path)
    {
        return RESULT_LINKING;
    }

    private static final String MESSAGE_LINKING = "lower path not allowed for a terminal node";

    private static final ManipulationResult RESULT_LINKING
            = ManipulationResult.failed(MESSAGE_LINKING);

    private static final IntManipulationResult INT_RESULT_LINKING
            = IntManipulationResult.failed(MESSAGE_LINKING, -1);
}
