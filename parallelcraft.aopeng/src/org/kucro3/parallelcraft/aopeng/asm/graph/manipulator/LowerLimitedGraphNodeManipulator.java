package org.kucro3.parallelcraft.aopeng.asm.graph.manipulator;

import org.kucro3.parallelcraft.aopeng.asm.graph.Node;
import org.kucro3.parallelcraft.aopeng.asm.graph.Path;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

public class LowerLimitedGraphNodeManipulator<T extends Node<T>> extends AbstractGraphNodeManipulator<T> {
    public LowerLimitedGraphNodeManipulator(@Nonnegative int limit)
    {
        this.limit = limit;
    }

    @Override
    public int appendLowerPath(@Nonnull Path<T> path)
    {
        if (getLowerPathCount() < limit)
            return super.appendLowerPath(path);

        return -1;
    }

    @Override
    public boolean pushLowerPath(@Nonnull Path<T> path)
    {
        if (getLowerPathCount() < limit)
            return super.pushLowerPath(path);

        return false;
    }

    private final int limit;
}
