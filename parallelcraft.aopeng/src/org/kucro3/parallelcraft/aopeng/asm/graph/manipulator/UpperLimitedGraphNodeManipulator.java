package org.kucro3.parallelcraft.aopeng.asm.graph.manipulator;

import org.kucro3.parallelcraft.aopeng.asm.graph.Node;
import org.kucro3.parallelcraft.aopeng.asm.graph.Path;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

public class UpperLimitedGraphNodeManipulator<T extends Node<T>> extends AbstractGraphNodeManipulator<T> {
    public UpperLimitedGraphNodeManipulator(@Nonnegative int limit)
    {
        this.limit = limit;
    }

    @Override
    public int appendUpperPath(@Nonnull Path<T> path)
    {
        if (getUpperPathCount() < limit)
            return super.appendUpperPath(path);

        return -1;
    }

    @Override
    public boolean pushUpperPath(@Nonnull Path<T> path)
    {
        if (getUpperPathCount() < limit)
            return super.pushUpperPath(path);

        return false;
    }

    private final int limit;
}
