package org.kucro3.parallelcraft.aopeng.asm.graph.manipulator;

import org.kucro3.parallelcraft.aopeng.asm.graph.Node;
import org.kucro3.parallelcraft.aopeng.asm.graph.Path;

import javax.annotation.Nonnull;

public class TerminalGraphNodeManipulator<T extends Node<T>> extends AbstractGraphNodeManipulator<T> {
    @Override
    public int appendLowerPath(@Nonnull Path<T> path)
    {
        return -1;
    }

    @Override
    public boolean pushLowerPath(@Nonnull Path<T> path)
    {
        return false;
    }
}
