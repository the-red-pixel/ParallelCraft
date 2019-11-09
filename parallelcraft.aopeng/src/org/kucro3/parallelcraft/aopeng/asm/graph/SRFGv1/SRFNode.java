package org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1;

import org.kucro3.parallelcraft.aopeng.asm.graph.Node;
import org.kucro3.parallelcraft.aopeng.asm.graph.manipulator.GraphNodeManipulator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

// Stack Related Flow SRFGraph
public abstract class SRFNode implements Node<SRFNode> {
    protected SRFNode(@Nonnull SRFNodeType type,
                      @Nonnull GraphNodeManipulator<SRFNode> manipulator,
                      boolean bypass)
    {
        this.type = Objects.requireNonNull(type, "type");
        this.manipulator = Objects.requireNonNull(manipulator, "manipulator");
        this.bypass = bypass;
    }

    protected SRFNode(@Nonnull SRFNodeType type,
                      @Nonnull GraphNodeManipulator<SRFNode> manipulator)
    {
        this(type, manipulator, false);
    }

    public @Nonnull SRFNodeType getType()
    {
        return type;
    }

    public boolean verify()
    {
        return true;
    }

    public boolean isHead()
    {
        return manipulator.getUpperPaths().isEmpty();
    }

    public boolean isTerminal()
    {
        return manipulator.getLowerPaths().isEmpty();
    }

    public boolean isBypass()
    {
        return bypass;
    }

    public void setEscapeTarget(@Nullable SRF escapeTarget)
    {
        this.escapeTarget = escapeTarget;
    }

    public boolean hasEscapeTarget()
    {
        return escapeTarget != null;
    }

    public @Nullable SRF getEscapeTarget()
    {
        return escapeTarget;
    }

    private SRF escapeTarget;

    public @Nonnull GraphNodeManipulator<SRFNode> getManipulator()
    {
        return manipulator;
    }

    protected final GraphNodeManipulator<SRFNode> manipulator;

    private final SRFNodeType type;

    public int visitStamp;

    private final boolean bypass;
}
