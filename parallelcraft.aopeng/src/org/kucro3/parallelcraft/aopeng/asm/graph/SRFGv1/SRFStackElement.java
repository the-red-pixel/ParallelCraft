package org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1;

import com.theredpixelteam.redtea.util.Predication;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

public class SRFStackElement implements StackComputation.Computational {
    public SRFStackElement(@Nonnegative int sourceSRF,
                           @Nonnull SRFNode upperNode,
                           @Nonnull StackElementType type)
    {
        this.sourceSRF = sourceSRF;
        this.upperNode = Predication.requireNonNull(upperNode, "sourceNode");
        this.type = Predication.requireNonNull(type, "type");
    }

    public @Nonnull SRFNode getUpperNode()
    {
        return upperNode;
    }

    public @Nonnegative int getSourceSRF()
    {
        return sourceSRF;
    }

    public void setSourceSRF(@Nonnegative int sourceSRF)
    {
        this.sourceSRF = sourceSRF;
    }

    public @Nonnull StackElementType getType()
    {
        return type;
    }

    public void setType(@Nonnull StackElementType type)
    {
        this.type = Predication.requireNonNull(type);
    }

    @Override
    public @Nonnull StackComputation.Operator toOperator()
    {
        return type.toOperator();
    }

    @Override
    public @Nonnull String toString()
    {
        return type.toString();
    }

    private int sourceSRF;

    private final SRFNode upperNode;

    private StackElementType type;
}
