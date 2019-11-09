package org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.node;

import org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.SRF;
import org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.SRFGv1NodeTypes;
import org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.SRFNode;
import org.kucro3.parallelcraft.aopeng.asm.graph.manipulator.LimitedGraphNodeManipulator;
import org.kucro3.parallelcraft.aopeng.asm.graph.manipulator.NormalGraphNodeManipulator;

import javax.annotation.Nonnull;
import java.util.Objects;

public class EscapeNode extends SRFNode {
    public EscapeNode(@Nonnull SRF target)
    {
        super(SRFGv1NodeTypes.ESCAPE, new LimitedGraphNodeManipulator<>(1, 1), true);

        this.target = Objects.requireNonNull(target);
    }

    public SRF getTarget()
    {
        return target;
    }

    private final SRF target;
}
