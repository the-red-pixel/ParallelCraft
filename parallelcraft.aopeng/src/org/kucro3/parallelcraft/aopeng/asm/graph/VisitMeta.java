package org.kucro3.parallelcraft.aopeng.asm.graph;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

public class VisitMeta {
    public VisitMeta()
    {
        this(1);
    }

    public VisitMeta(@Nonnegative int visitStamp)
    {
        this.visitStamp = visitStamp;
    }

    public @Nonnegative int getVisitStamp()
    {
        return visitStamp;
    }

    public @Nonnull VisitMeta next()
    {
        return new VisitMeta(visitStamp + 1);
    }

    private final int visitStamp;
}
