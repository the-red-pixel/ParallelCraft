package org.kucro3.parallelcraft.aopeng.asm.graph;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.Objects;

public class Path<T extends Node> {
    public Path(@Nonnull T upperNode,
                @Nonnegative int upperOrdinal,
                @Nonnull T lowerNode,
                @Nonnegative int lowerOrdinal,
                boolean phantom)
    {
        this.upperNode = Objects.requireNonNull(upperNode, "upperNode");
        this.upperOrdinal = upperOrdinal;
        this.lowerNode = Objects.requireNonNull(lowerNode, "lowerNode");
        this.lowerOrdinal = lowerOrdinal;
        this.phantom = phantom;
    }

    public @Nonnull T getLowerNode()
    {
        return lowerNode;
    }

    public void setLowerNode(@Nonnull T node)
    {
        this.lowerNode = Objects.requireNonNull(node);
    }

    public @Nonnegative int getLowerOrdinal()
    {
        return lowerOrdinal;
    }

    public void setLowerOrdinal(@Nonnegative int ordinal)
    {
        this.lowerOrdinal = ordinal;
    }

    public void incLowerOrdinal()
    {
        this.lowerOrdinal++;
    }

    public void decLowerOrdinal()
    {
        this.lowerOrdinal--;
    }

    public @Nonnull T getUpperNode()
    {
        return upperNode;
    }

    public void setUpperNode(@Nonnull T node)
    {
        this.upperNode = Objects.requireNonNull(node);
    }

    public @Nonnegative int getUpperOrdinal()
    {
        return upperOrdinal;
    }

    public void setUpperOrdinal(@Nonnegative int ordinal)
    {
        this.upperOrdinal = ordinal;
    }

    public void incUpperOrdinal()
    {
        this.upperOrdinal++;
    }

    public void decUpperOrdinal()
    {
        this.upperOrdinal--;
    }

    public boolean isPhantom()
    {
        return phantom;
    }

    public void setPhantom(boolean phantom)
    {
        this.phantom = phantom;
    }

    private T upperNode;

    private T lowerNode;

    private int upperOrdinal;

    private int lowerOrdinal;

    private boolean phantom;
}
