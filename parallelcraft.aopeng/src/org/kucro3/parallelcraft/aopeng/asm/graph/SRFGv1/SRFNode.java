package org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1;

import org.kucro3.parallelcraft.aopeng.asm.graph.DifferentialVisitMeta;
import org.kucro3.parallelcraft.aopeng.asm.graph.DifferentialVisitable;
import org.kucro3.parallelcraft.aopeng.asm.graph.Node;
import org.kucro3.parallelcraft.aopeng.asm.graph.manipulator.GraphNodeManipulator;
import org.kucro3.parallelcraft.aopeng.util.Attachable;
import org.kucro3.parallelcraft.aopeng.util.Attachment;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

// Stack Related Flow SRFGraph
public abstract class SRFNode implements Node<SRFNode>, DifferentialVisitable, Attachable {
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

    @Override
    public @Nonnull GraphNodeManipulator<SRFNode> getManipulator()
    {
        return manipulator;
    }

    @Override
    public int getVisitStamp()
    {
        return this.visitStamp;
    }

    @Override
    public void setVisitStamp(int visitStamp)
    {
        this.visitStamp = visitStamp;
    }

    @Override
    public @Nonnull Attachment getAttachments()
    {
        if (attachment == null)
            return attachment = new Attachment();

        return attachment;
    }

    private SRF escapeTarget;

    private Attachment attachment;

    protected final GraphNodeManipulator<SRFNode> manipulator;

    private final SRFNodeType type;

    private int visitStamp = DifferentialVisitMeta.DIFFERENTIAL_VISITABLE_NODE_INITIAL;

    private final boolean bypass;
}
