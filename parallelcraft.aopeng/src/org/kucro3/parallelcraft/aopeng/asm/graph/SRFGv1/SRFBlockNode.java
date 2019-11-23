package org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1;

import org.kucro3.parallelcraft.aopeng.asm.graph.DifferentialVisitMeta;
import org.kucro3.parallelcraft.aopeng.asm.graph.DifferentialVisitable;
import org.kucro3.parallelcraft.aopeng.asm.graph.Node;
import org.kucro3.parallelcraft.aopeng.asm.graph.manipulator.GraphNodeManipulator;
import org.kucro3.parallelcraft.aopeng.asm.graph.manipulator.LowerLimitedGraphNodeManipulator;

import javax.annotation.Nonnull;
import java.util.Objects;

public class SRFBlockNode implements Node<SRFBlockNode>, DifferentialVisitable {
    public SRFBlockNode(@Nonnull SRFBlock flowBlock)
    {
        this.flowBlock = Objects.requireNonNull(flowBlock);
        this.manipulator = new LowerLimitedGraphNodeManipulator<>(1);
    }

    @Override
    public @Nonnull GraphNodeManipulator<SRFBlockNode> getManipulator()
    {
        return manipulator;
    }

    public @Nonnull SRFBlock getFlowBlock()
    {
        return flowBlock;
    }

    public void setFlowBlock(@Nonnull SRFBlock flowBlock)
    {
        this.flowBlock = Objects.requireNonNull(flowBlock);
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

    private int visitStamp = DifferentialVisitMeta.DIFFERENTIAL_VISITABLE_NODE_INITIAL;

    private SRFBlock flowBlock;

    private final GraphNodeManipulator<SRFBlockNode> manipulator;
}
