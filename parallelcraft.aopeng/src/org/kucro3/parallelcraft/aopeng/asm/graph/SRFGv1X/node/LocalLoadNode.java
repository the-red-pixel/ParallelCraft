package org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1X.node;

import org.kucro3.parallelcraft.aopeng.asm.graph.LocalRef;
import org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1X.SRFGv1XNodeTypes;
import org.kucro3.parallelcraft.aopeng.asm.graph.manipulator.HeadGraphNodeManipulator;

import javax.annotation.Nonnull;

public class LocalLoadNode extends LocalNodeBase {
    public LocalLoadNode(@Nonnull LocalRef localRef)
    {
        super(SRFGv1XNodeTypes.LOCAL_LOAD,
                new HeadGraphNodeManipulator<>(),
                localRef);
    }
}
