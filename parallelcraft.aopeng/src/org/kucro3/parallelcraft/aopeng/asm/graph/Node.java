package org.kucro3.parallelcraft.aopeng.asm.graph;

import org.kucro3.parallelcraft.aopeng.asm.graph.manipulator.GraphNodeManipulator;

import javax.annotation.Nonnull;

public interface Node<T extends Node<T>> {
    public @Nonnull GraphNodeManipulator<T> getManipulator();
}
