package org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1X.node;

import org.kucro3.parallelcraft.aopeng.asm.graph.LocalRef;
import org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.SRFNode;
import org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.SRFNodeType;
import org.kucro3.parallelcraft.aopeng.asm.graph.manipulator.GraphNodeManipulator;

import javax.annotation.Nonnull;
import java.util.Objects;

public abstract class LocalNodeBase extends SRFNode {
    public LocalNodeBase(@Nonnull SRFNodeType type,
                         @Nonnull GraphNodeManipulator<SRFNode> manipulator,
                         @Nonnull LocalRef localRef)
    {
        super(type, manipulator);
        this.localRef = Objects.requireNonNull(localRef);
    }

    public @Nonnull LocalRef getLocalRef()
    {
        return localRef;
    }

    public void setLocalRef(@Nonnull LocalRef localRef)
    {
        this.localRef = Objects.requireNonNull(localRef);
    }

    protected LocalRef localRef;
}
