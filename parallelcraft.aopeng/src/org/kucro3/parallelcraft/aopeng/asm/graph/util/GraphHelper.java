package org.kucro3.parallelcraft.aopeng.asm.graph.util;

import org.kucro3.parallelcraft.aopeng.asm.graph.Node;
import org.kucro3.parallelcraft.aopeng.asm.graph.Path;
import org.kucro3.parallelcraft.aopeng.asm.graph.manipulator.GraphNodeManipulator;

import javax.annotation.Nonnull;

public final class GraphHelper {
    private GraphHelper()
    {
    }

    public static <T extends Node<T>> boolean append(@Nonnull T dst,
                                                     @Nonnull T src,
                                                     boolean phantom)
    {
        // ordinal not initialized at this moment
        Path<T> path = new Path<>(dst, 0, src, 0, phantom);

        GraphNodeManipulator<T>
                dstManipulator = dst.getManipulator(),
                srcManipulator = src.getManipulator();

        int lowerOrdinal, upperOrdinal;

        if ((upperOrdinal = dstManipulator.appendLowerPath(path)) < 0)
            return false;

        if ((lowerOrdinal = srcManipulator.appendUpperPath(path)) < 0)
        {
            dstManipulator.truncateLowerPath();

            return false;
        }

        path.setUpperOrdinal(upperOrdinal);
        path.setLowerOrdinal(lowerOrdinal);

        return true;
    }

    public static <T extends Node<T>> boolean append(@Nonnull T dst,
                                                     @Nonnull T src)
    {
        return append(dst, src, false);
    }

    public static <T extends Node<T>> boolean push(@Nonnull T dst,
                                                   @Nonnull T src,
                                                   boolean phantom)
    {
        // ordinal is 0 in push operation
        Path<T> path = new Path<>(dst, 0, src, 0, phantom);

        GraphNodeManipulator<T>
                dstManipulator = dst.getManipulator(),
                srcManipulator = src.getManipulator();

        if (!dstManipulator.pushLowerPath(path))
            return false;

        if (!srcManipulator.pushUpperPath(path))
        {
            dstManipulator.truncateLowerPath();

            return false;
        }

        return true;
    }

    public static <T extends Node<T>> boolean push(@Nonnull T dst,
                                                   @Nonnull T src)
    {
        return push(dst, src, false);
    }

    public static <T extends Node<T>> boolean insertAndPush(@Nonnull Path<T> dst,
                                                            @Nonnull T src,
                                                            boolean lowerPhantom)
    {
        GraphNodeManipulator<T> manipulator = src.getManipulator();

        if (!manipulator.pushUpperPath(dst))
            return false;

        Path<T> lowerPath = new Path<>(src, 0, dst.getLowerNode(), dst.getLowerOrdinal(), lowerPhantom);

        if (!manipulator.pushLowerPath(lowerPath))
        {
            manipulator.popUpperPath();

            return false;
        }

        dst.setLowerNode(src);
        dst.setLowerOrdinal(0);

        return true;
    }

    public static <T extends Node<T>> boolean insertAndPush(@Nonnull Path<T> dst,
                                                            @Nonnull T src)
    {
        return insertAndPush(dst, src, false);
    }

    public static <T extends Node<T>> boolean insertAndAppend(@Nonnull Path<T> dst,
                                                              @Nonnull T src,
                                                              boolean lowerPhantom)
    {
        GraphNodeManipulator<T> manipulator = src.getManipulator();

        int upperPathOrdinal;
        if ((upperPathOrdinal = manipulator.appendUpperPath(dst)) < 0)
            return false;

        // upper ordinal not initialized
        Path<T> lowerPath = new Path<>(src, 0, dst.getLowerNode(), dst.getLowerOrdinal(), lowerPhantom);

        int lowerPathOrdinal;
        if ((lowerPathOrdinal = manipulator.appendLowerPath(lowerPath)) < 0)
        {
            manipulator.truncateUpperPath();

            return false;
        }

        lowerPath.setUpperOrdinal(lowerPathOrdinal);

        dst.setLowerNode(src);
        dst.setLowerOrdinal(upperPathOrdinal);

        return true;
    }

    public static <T extends Node<T>> boolean insertAndAppend(@Nonnull Path<T> dst,
                                                              @Nonnull T src)
    {
        return insertAndAppend(dst, src, false);
    }
}
