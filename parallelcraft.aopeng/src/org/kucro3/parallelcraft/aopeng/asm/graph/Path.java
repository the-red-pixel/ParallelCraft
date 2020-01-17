package org.kucro3.parallelcraft.aopeng.asm.graph;

import com.theredpixelteam.redtea.util.Predication;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

public class Path<T extends Node> {
    public Path(@Nonnull T upperNode,
                @Nonnegative int upperOrdinal,
                @Nonnull T lowerNode,
                @Nonnegative int lowerOrdinal,
                boolean phantom)
    {
        this.upperNode = Predication.requireNonNull(upperNode, "upperNode");
        this.upperOrdinal = Predication.requireNonNegative(upperOrdinal, "upperOrdinal");
        this.lowerNode = Predication.requireNonNull(lowerNode, "lowerNode");
        this.lowerOrdinal = Predication.requireNonNegative(lowerOrdinal, "lowerOrdinal");
        this.phantom = phantom;
    }

    public @Nonnull T getLowerNode()
    {
        return lowerNode;
    }

    public void setLowerNode(@Nonnull T node)
    {
        this.lowerNode = Predication.requireNonNull(node);
    }

    public @Nonnegative int getLowerOrdinal()
    {
        return lowerOrdinal;
    }

    public void setLowerOrdinal(@Nonnegative int ordinal)
    {
        this.lowerOrdinal = Predication.requireNonNegative(ordinal);
    }

    public void incLowerOrdinal()
    {
        this.lowerOrdinal++;
    }

    public void decLowerOrdinal()
    {
        if (lowerOrdinal == 0)
            throw new IllegalStateException("lower ordinal decreasing into negative");

        this.lowerOrdinal--;
    }

    public @Nonnull T getUpperNode()
    {
        return upperNode;
    }

    public void setUpperNode(@Nonnull T node)
    {
        this.upperNode = Predication.requireNonNull(node);
    }

    public @Nonnegative int getUpperOrdinal()
    {
        return upperOrdinal;
    }

    public void setUpperOrdinal(@Nonnegative int ordinal)
    {
        this.upperOrdinal = Predication.requireNonNegative(ordinal);
    }

    public void incUpperOrdinal()
    {
        this.upperOrdinal++;
    }

    public void decUpperOrdinal()
    {
        if (lowerOrdinal == 0)
            throw new IllegalStateException("upper ordinal decreasing into negative");

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
