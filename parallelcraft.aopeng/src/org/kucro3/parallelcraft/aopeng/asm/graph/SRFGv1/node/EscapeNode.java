package org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.node;

import com.theredpixelteam.redtea.util.Predication;
import org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.SRF;
import org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.SRFGv1NodeTypes;
import org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.SRFNode;
import org.kucro3.parallelcraft.aopeng.asm.graph.manipulator.LimitedGraphNodeManipulator;

import javax.annotation.Nonnull;

public class EscapeNode extends SRFNode {
    public EscapeNode(@Nonnull SRF target)
    {
        super(SRFGv1NodeTypes.ESCAPE, new LimitedGraphNodeManipulator<>(1, 1), true);

        this.target = Predication.requireNonNull(target);
    }

    public SRF getTarget()
    {
        return target;
    }

    private final SRF target;
}
