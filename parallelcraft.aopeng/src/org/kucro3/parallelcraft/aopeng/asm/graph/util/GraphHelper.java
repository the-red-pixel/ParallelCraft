package org.kucro3.parallelcraft.aopeng.asm.graph.util;

import org.kucro3.parallelcraft.aopeng.asm.graph.Node;
import org.kucro3.parallelcraft.aopeng.asm.graph.Path;
import org.kucro3.parallelcraft.aopeng.asm.graph.manipulator.GraphNodeManipulator;
import org.kucro3.parallelcraft.aopeng.asm.graph.manipulator.result.IntManipulationResult;
import org.kucro3.parallelcraft.aopeng.asm.graph.manipulator.result.ManipulationResult;

import javax.annotation.Nonnull;

public final class GraphHelper {
    private GraphHelper()
    {
    }

    public static @Nonnull <T extends Node<T>> ManipulationResult append(@Nonnull T dst,
                                                                         @Nonnull T src,
                                                                         boolean phantom)
    {
        // ordinal not initialized at this moment
        Path<T> path = new Path<>(dst, 0, src, 0, phantom);

        GraphNodeManipulator<T>
                dstManipulator = dst.getManipulator(),
                srcManipulator = src.getManipulator();

        int lowerOrdinal, upperOrdinal;

        IntManipulationResult result;

        if (!(result = dstManipulator.appendLowerPath(path)).isPassed())
            return result;

        upperOrdinal = result.getValue();

        if (!(result = srcManipulator.appendUpperPath(path)).isPassed())
        {
            dstManipulator.truncateLowerPath();

            return result;
        }

        lowerOrdinal = result.getValue();

        path.setUpperOrdinal(upperOrdinal);
        path.setLowerOrdinal(lowerOrdinal);

        return ManipulationResult.passed();
    }

    public static @Nonnull <T extends Node<T>> ManipulationResult append(@Nonnull T dst,
                                                                         @Nonnull T src)
    {
        return append(dst, src, false);
    }

    public static @Nonnull <T extends Node<T>> ManipulationResult push(@Nonnull T dst,
                                                                       @Nonnull T src,
                                                                       boolean phantom)
    {
        // ordinal is 0 in push operation
        Path<T> path = new Path<>(dst, 0, src, 0, phantom);

        GraphNodeManipulator<T>
                dstManipulator = dst.getManipulator(),
                srcManipulator = src.getManipulator();

        ManipulationResult result;

        if (!(result = dstManipulator.pushLowerPath(path)).isPassed())
            return result;

        if (!(result = srcManipulator.pushUpperPath(path)).isPassed())
        {
            dstManipulator.truncateLowerPath();

            return result;
        }

        return ManipulationResult.passed();
    }

    public static @Nonnull <T extends Node<T>> ManipulationResult push(@Nonnull T dst,
                                                                       @Nonnull T src)
    {
        return push(dst, src, false);
    }

    public static @Nonnull <T extends Node<T>> ManipulationResult insertAndPush(@Nonnull Path<T> dst,
                                                                                @Nonnull T src,
                                                                                boolean lowerPhantom)
    {
        GraphNodeManipulator<T> manipulator = src.getManipulator();

        ManipulationResult result;

        if (!(result = manipulator.pushUpperPath(dst)).isPassed())
            return result;

        Path<T> lowerPath = new Path<>(src, 0, dst.getLowerNode(), dst.getLowerOrdinal(), lowerPhantom);

        if (!(result = manipulator.pushLowerPath(lowerPath)).isPassed())
        {
            manipulator.popUpperPath();

            return result;
        }

        dst.setLowerNode(src);
        dst.setLowerOrdinal(0);

        return ManipulationResult.passed();
    }

    public static @Nonnull <T extends Node<T>> ManipulationResult insertAndPush(@Nonnull Path<T> dst,
                                                                                @Nonnull T src)
    {
        return insertAndPush(dst, src, false);
    }

    public static @Nonnull <T extends Node<T>> ManipulationResult insertAndAppend(@Nonnull Path<T> dst,
                                                                                  @Nonnull T src,
                                                                                  boolean lowerPhantom)
    {
        GraphNodeManipulator<T> manipulator = src.getManipulator();

        IntManipulationResult result;

        int upperPathOrdinal;
        if (!(result = manipulator.appendUpperPath(dst)).isPassed())
            return result;

        upperPathOrdinal = result.getValue();

        // upper ordinal not initialized
        Path<T> lowerPath = new Path<>(src, 0, dst.getLowerNode(), dst.getLowerOrdinal(), lowerPhantom);

        int lowerPathOrdinal;
        if (!(result = manipulator.appendLowerPath(lowerPath)).isPassed())
        {
            manipulator.truncateUpperPath();

            return result;
        }

        lowerPathOrdinal = result.getValue();

        lowerPath.setUpperOrdinal(lowerPathOrdinal);

        dst.setLowerNode(src);
        dst.setLowerOrdinal(upperPathOrdinal);

        return ManipulationResult.passed();
    }

    public static @Nonnull <T extends Node<T>> ManipulationResult insertAndAppend(@Nonnull Path<T> dst,
                                                                                  @Nonnull T src)
    {
        return insertAndAppend(dst, src, false);
    }
}
