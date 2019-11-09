package org.kucro3.parallelcraft.aopeng.asm.graph.manipulator;

import org.kucro3.parallelcraft.aopeng.asm.graph.Node;
import org.kucro3.parallelcraft.aopeng.asm.graph.Path;

import javax.annotation.Nonnull;

public class HeadGraphNodeManipulator<T extends Node<T>> extends AbstractGraphNodeManipulator<T> {
    @Override
    public boolean pushUpperPath(@Nonnull Path<T> path)
     {
         return false;
     }

    @Override
    public int appendUpperPath(@Nonnull Path<T> path)
    {
        return -1;
    }
}
