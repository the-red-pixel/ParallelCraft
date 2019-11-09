package org.kucro3.parallelcraft.aopeng.asm.graph.manipulator;

import org.kucro3.parallelcraft.aopeng.asm.graph.Node;
import org.kucro3.parallelcraft.aopeng.asm.graph.Path;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

public class LimitedGraphNodeManipulator<T extends Node<T>> extends AbstractGraphNodeManipulator<T> {
    public LimitedGraphNodeManipulator(@Nonnegative int upperLimit,
                                       @Nonnegative int lowerLimit)
    {
        this.upperLimit = upperLimit;
        this.lowerLimit = lowerLimit;
    }

    @Override
    public int appendUpperPath(@Nonnull Path<T> path)
    {
        if (getUpperPathCount() < upperLimit)
            return super.appendUpperPath(path);

        return -1;
    }

    @Override
    public boolean pushUpperPath(@Nonnull Path<T> path)
    {
        if (getUpperPathCount() < upperLimit)
            return super.pushUpperPath(path);

        return false;
    }

    @Override
    public int appendLowerPath(@Nonnull Path<T> path)
    {
        if (getLowerPathCount() < lowerLimit)
            return super.appendLowerPath(path);

        return -1;
    }

    @Override
    public boolean pushLowerPath(@Nonnull Path<T> path)
    {
        if (getLowerPathCount() < lowerLimit)
            return super.pushLowerPath(path);

        return false;
    }

    private final int upperLimit;

    private final int lowerLimit;
}
