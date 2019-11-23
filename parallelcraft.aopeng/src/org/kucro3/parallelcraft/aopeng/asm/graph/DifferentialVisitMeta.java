package org.kucro3.parallelcraft.aopeng.asm.graph;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

public class DifferentialVisitMeta {
    public DifferentialVisitMeta()
    {
        this(DIFFERENTIAL_VISITMETA_INITIAL);
    }

    public DifferentialVisitMeta(@Nonnegative int visitStamp)
    {
        this.visitStamp = visitStamp;
    }

    public @Nonnegative int getVisitStamp()
    {
        return visitStamp;
    }

    public @Nonnull DifferentialVisitMeta next()
    {
        return new DifferentialVisitMeta(visitStamp + 1);
    }

    public boolean visited(@Nonnull DifferentialVisitable subject)
    {
        return subject.getVisitStamp() >= this.visitStamp;
    }

    public void visit(@Nonnull DifferentialVisitable subject)
    {
        if (!visited(subject))
            subject.setVisitStamp(this.visitStamp);
    }

    public static final int DIFFERENTIAL_VISITABLE_NODE_INITIAL = 0;

    public static final int DIFFERENTIAL_VISITMETA_INITIAL = 1;

    private final int visitStamp;
}
